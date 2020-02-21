package io.github.victorhsr.ttxn.handler.vendor.mysql;

import io.github.victorhsr.ttxn.TenantWrapper;
import io.github.victorhsr.ttxn.handler.TenantChangeException;
import io.github.victorhsr.ttxn.handler.TenantTransactionHandler;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlChangeDatabaseTenantTransactionHandler implements TenantTransactionHandler {

    private final EntityManager entityManager;

    public MysqlChangeDatabaseTenantTransactionHandler(EntityManager entityManager) {
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
