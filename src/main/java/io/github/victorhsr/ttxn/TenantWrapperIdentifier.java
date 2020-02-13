package io.github.victorhsr.ttxn;

import java.lang.annotation.*;

/**
 * Annotation that indicate which parameter of a metod annoted with {@link TenantTransaction}
 * is the {@link TenantWrapper}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantWrapperIdentifier {

}
