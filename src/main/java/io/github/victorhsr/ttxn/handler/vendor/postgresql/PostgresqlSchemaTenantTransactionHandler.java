package io.github.victorhsr.ttxn.handler.vendor.postgresql;

import io.github.victorhsr.ttxn.TenantWrapper;
import io.github.victorhsr.ttxn.handler.TenantTransactionHandler;
import io.github.victorhsr.ttxn.handler.TenantChangeException;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of {@link TenantTransactionHandler} that works
 * in Postgresql sgbd <br/>
 * <p>
 * Postgres can have multiple schemas in a single database,
 * this strategy consists in use the command 'SET SEARCH_PATH TO'
 * to alter the schema tha will be used in subsequent queries
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 * @see <a href="https://www.postgresql.org/docs/9.6/ddl-schemas.html">https://www.postgresql.org/docs/9.6/ddl-schemas.html</a>
 */
public class PostgresqlSchemaTenantTransactionHandler implements TenantTransactionHandler {

    /**
     * Each thread have it's own proxy of the {@link EntityManager},
     * thus, when we modify a entityManager connection, we modify
     * all the thread life cicle connection. No matters if we use
     * the {@link EntityManager} or some {@link JdbcTemplate},
     * they all will be affected
     *
     * @see DataSourceUtils#getConnection
     */
    private final EntityManager entityManager;

    public PostgresqlSchemaTenantTransactionHandler(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void useTenant(TenantWrapper tenantWrapper) throws TenantChangeException {

        final String query = String.format("SET SEARCH_PATH TO %s", tenantWrapper.getTenant());
        final Session session = entityManager.unwrap(Session.class);
        session.doWork((connection) -> {
            try (PreparedStatement stmt = connection
                    .prepareStatement(query)) {
                stmt.execute();
            } catch (final SQLException ex) {
                throw new TenantChangeException(ex);
            }
        });
    }

    @Override
    public void flush() {
        this.entityManager.flush();
    }
}
