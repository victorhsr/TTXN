package io.github.victorhsr.ttxn.handler;

import io.github.victorhsr.ttxn.handler.vendor.postgresql.PostgresqlSchemaTenantTransactionHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class TenantTransactionHandlerProvider {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public TenantTransactionHandler getHandler(){
        return new PostgresqlSchemaTenantTransactionHandler(entityManager);
    }

}
