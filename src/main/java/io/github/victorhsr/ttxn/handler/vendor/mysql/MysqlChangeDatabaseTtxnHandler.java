package io.github.victorhsr.ttxn.handler.vendor.mysql;

import io.github.victorhsr.ttxn.TenantWrapper;
import io.github.victorhsr.ttxn.handler.TenantChangeException;
import io.github.victorhsr.ttxn.handler.TenantTransactionHandler;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of {@link TenantTransactionHandler} that works
 * in Mysql sgbd <br/>
 * <p/>
 * Mysql treats the database and schema as one, so, in this strategy
 * the handler make a switch between databases to execut the commands
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
public class MysqlChangeDatabaseTtxnHandler implements TenantTransactionHandler {

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

    public MysqlChangeDatabaseTtxnHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void useTenant(TenantWrapper tenantWrapper) throws TenantChangeException {

        final String query = String.format("USE %s", tenantWrapper.getTenant());
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
