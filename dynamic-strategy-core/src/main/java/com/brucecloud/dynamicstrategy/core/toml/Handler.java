package com.brucecloud.dynamicstrategy.core.toml;

import lombok.Data;

/**
 * Represent a handler config object.
 * created at 2019-08-08 14:19.
 *
 * @author sephiroth.
 */
@Data
public class Handler {
    /**
     * handler name
     */
    private String name;

    /**
     * handler jar file name
     */
    private String jar;

    /**
     * handler class name
     */
    private String className;
}