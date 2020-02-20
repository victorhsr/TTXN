package io.github.victorhsr.ttxn.handler;

import io.github.victorhsr.ttxn.TenantTransactionAOP;
import io.github.victorhsr.ttxn.handler.vendor.postgresql.PostgresqlSchemaTenantTransactionHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class TenantTransactionBeansProvider {

    private static final String DEFAULT_SCHEMA = "public";

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public TenantTransactionHandler getHandler(){
        return new PostgresqlSchemaTenantTransactionHandler(entityManager);
    }

    @Bean
    public TenantTransactionAOP getAop(){

        final PostgresqlSchemaTenantTransactionHandler postgresqlSchemaTenantTransactionHandler = new PostgresqlSchemaTenantTransactionHandler(this.entityManager);
        return new TenantTransactionAOP(postgresqlSchemaTenantTransactionHandler, DEFAULT_SCHEMA);
    }

}
