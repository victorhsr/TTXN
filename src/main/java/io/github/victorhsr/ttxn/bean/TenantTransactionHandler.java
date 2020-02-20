package io.github.victorhsr.ttxn.bean;

import io.github.victorhsr.ttxn.TenantWrapper;

/**
 * Contract that defines how to change the tenant that
 * will be used in current transaction/subsequent queries
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
public interface TenantTransactionHandler {

    /**
     * Set the tenant to be used in current transaction/subsequent queries
     *
     * @param tenantWrapper wrapper that knows how to obtain a tenant
     * @throws TenantChangeException when the operation fails
     */
    void useTenant(final TenantWrapper tenantWrapper) throws TenantChangeException;

    /**
     * Flush all the operations performed in current transaction
     * to database
     */
    default void flush(){
        // Implement if you need it
    }

}
