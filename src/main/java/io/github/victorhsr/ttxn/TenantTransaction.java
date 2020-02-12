package io.github.victorhsr.ttxn;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Decorating the traditional {@link Transactional} annotation, giving to it the
 * hability to change in runtime wich tenant the transaction will run on
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
@Documented
@Transactional
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantTransaction {

    /**
     * @see Transactional#value()
     */
    @AliasFor(annotation = Transactional.class, attribute = "value")
    String value() default "";

    /**
     * @see Transactional#transactionManager()
     */
    @AliasFor(annotation = Transactional.class, attribute = "transactionManager")
    String transactionManager() default "";

    /**
     * @see Transactional#propagation()
     */
    @AliasFor(annotation = Transactional.class, attribute = "propagation")
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * @see Transactional#isolation()
     */
    @AliasFor(annotation = Transactional.class, attribute = "Isolation")
    Isolation isolation() default Isolation.DEFAULT;

    /**
     * @see Transactional#timeout()
     */
    @AliasFor(annotation = Transactional.class, attribute = "timeout")
    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    /**
     * @see Transactional#readOnly()
     */
    @AliasFor(annotation = Transactional.class, attribute = "readOnly")
    boolean readOnly() default false;

    /**
     * @see Transactional#rollbackFor()
     */
    @AliasFor(annotation = Transactional.class, attribute = "rollbackFor")
    Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * @see Transactional#rollbackForClassName()
     */
    @AliasFor(annotation = Transactional.class, attribute = "rollbackForClassName")
    String[] rollbackForClassName() default {};

    /**
     * @see Transactional#noRollbackFor()
     */
    @AliasFor(annotation = Transactional.class, attribute = "noRollbackFor")
    Class<? extends Throwable>[] noRollbackFor() default {};

    /**
     * @see Transactional#noRollbackForClassName()
     */
    @AliasFor(annotation = Transactional.class, attribute = "noRollbackForClassName")
    String[] noRollbackForClassName() default {};
}
