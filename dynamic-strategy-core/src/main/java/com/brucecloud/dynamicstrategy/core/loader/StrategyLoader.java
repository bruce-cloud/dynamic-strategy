package com.brucecloud.dynamicstrategy.core.loader;

import java.io.FileNotFoundException;

/**
 * A strategy loader.
 * created at 2019-08-08 14:44.
 *
 * @author sephiroth.
 */
public interface StrategyLoader {

    void load() throws FileNotFoundException;
}