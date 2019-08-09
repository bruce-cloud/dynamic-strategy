package com.brucecloud.dynamicstrategy.core;

/**
 * Handler to be implemented.
 * created at 2019-08-09 09:27.
 *
 * @author sephiroth.
 */
public interface HandlerClass<R, P> {
    /**
     * Handle method to be implemented.
     *
     * @param param
     *            param
     * @return handle result
     */
    R handler(P param);
}