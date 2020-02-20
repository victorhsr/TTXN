package io.github.victorhsr.ttxn.bean;

/**
 * Exception thrown when {@link TenantTransactionHandler#useTenant} fails to alter
 * the tenant
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
public class TenantChangeException extends RuntimeException {

    public TenantChangeException(String message) {
        super(message);
    }

    public TenantChangeException(Throwable cause) {
        super(cause);
    }

    public TenantChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
