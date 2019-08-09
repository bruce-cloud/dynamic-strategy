package com.brucecloud.dynamicstrategy.core;

/**
 * Handler to be implemented.
 * created at 2019-08-09 09:27.
 *
 * @author sephiroth.
 */
public interface HandlerClass<Context> {
    /**
     * Handle method to be implemented.
     *
     * @param context context
     * @return context
     */
    Context handler(Context context);
}