package io.github.victorhsr.ttxn;

/**
 * Contract that defines how to dynamically retrieve the tenant during
 * a transaction
 * 
 * @author victorhsr <victor.hugo.origins@gmail.com>
 *
 */
public interface TenantWrapper {

	/**
	 * @return the tenant that will be used in the transaction
	 */
	String getTenant();

}
