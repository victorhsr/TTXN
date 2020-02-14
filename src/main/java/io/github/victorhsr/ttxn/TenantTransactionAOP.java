package io.github.victorhsr.ttxn;

import io.github.victorhsr.ttxn.commons.reflection.Reflections;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.Optional;

@Aspect
@Component
public class TenantTransactionAOP {

    @PersistenceContext
    private EntityManager entityManager;

    private void updateEntityManagerTenant(final JoinPoint joinPoint) {

        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();

        final Optional<Integer> paramAnnotationIndexOpt = Reflections.findParamsAnnotationIndex(method.getParameterAnnotations(), TenantWrapperIdentifier.class);

        if(!paramAnnotationIndexOpt.isPresent()){
            throw new TenantWrapperNotFoundException();
        }

        final int tenantTransactionIdentifierIndex = paramAnnotationIndexOpt.get();

        final TenantWrapper tenantWrapper = (TenantWrapper) joinPoint.getArgs()[tenantTransactionIdentifierIndex];
        this.prepareEntityManager(tenantWrapper);
    }

    @Around("@annotation(TenantTransaction)")
    public Object execute(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        this.updateEntityManagerTenant(proceedingJoinPoint);

        final Object processingResult = proceedingJoinPoint.proceed();
        /**
         * O entityManager armazena as instruçoes que serao executadas no banco em cache,
         * ou seja, so sao enviadas com o commit da sua transaçao.<br>
         *
         * Por outro lado, o 'SET SEARCH PATH TO my_schema' eh executado via JDBC e performado
         * just in time. Para evitar side effects desastrosos, garantimos que as instruçoes
         * referentes ao metodo a ser executado sejam imediatamente enviadas ao banco de dados
         * para que entao possamos reverter a mudanca de schema da consulta
         */
        this.entityManager.flush();

        this.revertEntityManagerUpdate(proceedingJoinPoint);
        return processingResult;
    }

    /**
     * Concluída a transação,revertemos a alteração que realizamos no tenant do
     * entityManager
     */
    private void revertEntityManagerUpdate(final JoinPoint joinPoint) {
        this.prepareEntityManager(() -> "public");
    }

    /**
     * Prepara o entityManager para utilização do novo esquema
     */
    private void prepareEntityManager(final TenantWrapper tenantWrapper) {

        String tenant = tenantWrapper.getTenant();
        System.out.println("tenant = " + tenant);
        final Session session = entityManager.unwrap(Session.class);
        session.doWork((connection) -> {
            try (PreparedStatement stmt = connection
                    .prepareStatement("SET search_path TO " + tenantWrapper.getTenant())) {
                stmt.execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}
