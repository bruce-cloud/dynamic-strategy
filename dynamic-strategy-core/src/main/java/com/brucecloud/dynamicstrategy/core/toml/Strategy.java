package com.brucecloud.dynamicstrategy.core.toml;

import lombok.Data;

import java.util.List;

/**
 * Represent a strategy config object.
 * created at 2019-08-08 14:21.
 *
 * @author sephiroth.
 */
@Data
public class Strategy {
    /**
     * unique strategy name
     */
    private String name;

    /**
     * sequence list
     */
    private List<String> sequence;
}