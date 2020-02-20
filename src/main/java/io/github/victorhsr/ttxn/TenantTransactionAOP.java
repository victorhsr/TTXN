package io.github.victorhsr.ttxn;

import io.github.victorhsr.ttxn.commons.reflection.Reflections;
import io.github.victorhsr.ttxn.bean.TenantTransactionHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Class that detects the use of {@link TenantTransaction} annotation
 * and trigger the disered behavior
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
@Aspect
public class TenantTransactionAOP {

    private final TenantTransactionHandler tenantTransactionHandler;
    private final String defaultTenant;

    public TenantTransactionAOP(final TenantTransactionHandler tenantTransactionHandler, final String defaultTenant) {
        this.tenantTransactionHandler = tenantTransactionHandler;
        this.defaultTenant = defaultTenant;
        this.splashScreen();
    }

    private void splashScreen() {
        System.out.println();
        System.out.println(" /$$$$$$$$ /$$$$$$$$ /$$   /$$ /$$   /$$");
        System.out.println("|__  $$__/|__  $$__/| $$  / $$| $$$ | $$");
        System.out.println("   | $$      | $$   |  $$/ $$/| $$$$| $$");
        System.out.println("   | $$      | $$    \\  $$$$/ | $$ $$ $$");
        System.out.println("   | $$      | $$     >$$  $$ | $$  $$$$");
        System.out.println("   | $$      | $$    /$$/\\  $$| $$\\  $$$");
        System.out.println("   | $$      | $$   | $$  \\ $$| $$ \\  $$");
        System.out.println("   |__/      |__/   |__/  |__/|__/  \\__/");
        System.out.println("   :: Tenant Transaction ::    (v1.0.0)");
        System.out.println();
    }

    private TenantWrapper extractTenantWrapper(final JoinPoint joinPoint) {

        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();

        final Optional<Integer> paramAnnotationIndexOpt = Reflections.findParamsAnnotationIndex(method.getParameterAnnotations(), TenantWrapperIdentifier.class);

        if (!paramAnnotationIndexOpt.isPresent()) {
            throw new TenantWrapperNotFoundException();
        }

        final int tenantTransactionIdentifierIndex = paramAnnotationIndexOpt.get();

        final TenantWrapper tenantWrapper = (TenantWrapper) joinPoint.getArgs()[tenantTransactionIdentifierIndex];
        return tenantWrapper;
    }

    /**
     * To ensure the execution of the transaction without side-effects,
     * the {@link TenantTransaction} make uses of the spring's {@link Transactional}
     * annotation and runs after it's start-handlers.
     * After spring initial work, we need to
     * <ol>
     *     <li>Change the tenant</li>
     *     <li>Perform the disered job</li>
     *     <li>Make sure that all the queries was sent to the database</li>
     *     <li>Rollback the tenat to it's default value</li>
     * </ol>
     */
    @Around("@annotation(io.github.victorhsr.ttxn.TenantTransaction)")
    public Object execute(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        final TenantWrapper desiredTenant = this.extractTenantWrapper(proceedingJoinPoint);
        this.tenantTransactionHandler.useTenant(desiredTenant);

        final Object processingResult = proceedingJoinPoint.proceed();

        /**
         * Before performing the 'rollback' in current tenant
         * to the defaultTenant, we need to ensure that all
         * the jobs done in the transaction is already in the database.
         * Thus, we'll flush it
         */
        this.tenantTransactionHandler.flush();
        this.tenantTransactionHandler.useTenant(() -> this.defaultTenant);
        return processingResult;
    }

}
