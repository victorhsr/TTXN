package io.github.victorhsr.sample;

import io.github.victorhsr.ttxn.TenantTransactionAOP;
import io.github.victorhsr.ttxn.handler.vendor.postgresql.PostgresqlSchemaTenantTransactionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
@EnableAspectJAutoProxy
public class TenantTransactionConfiguration {

    private final String defaultSchema;

    @PersistenceContext
    private EntityManager entityManager;

    public TenantTransactionConfiguration(@Value("${default-tenant}") String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }


    @Bean
    public TenantTransactionAOP getAop(){

        final PostgresqlSchemaTenantTransactionHandler postgresqlSchemaTenantTransactionHandler = new PostgresqlSchemaTenantTransactionHandler(this.entityManager);
        return new TenantTransactionAOP(postgresqlSchemaTenantTransactionHandler, this.defaultSchema);
    }

}
