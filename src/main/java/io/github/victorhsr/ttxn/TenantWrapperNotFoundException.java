package io.github.victorhsr.ttxn;

/**
 * Exception thrown when no {@link TenantWrapper} is found in {@link TenantTransaction}
 * method
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
public class TenantWrapperNotFoundException extends RuntimeException {

    public TenantWrapperNotFoundException() {
        super(String.format("No %s implementation found. Are you using %s?", TenantWrapper.class.getName(), TenantWrapperIdentifier.class.getName()));
    }
}
