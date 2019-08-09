package com.brucecloud.dynamicstrategy.core.util;

import org.slf4j.Logger;

/**
 * .
 * created at 2019-08-09 17:16.
 *
 * @author sephiroth.
 */
public class LogUtils {
    private static final int MAX_STRATEGY_EXEC_MS_TIME_FOR_INFO_LOG = 100;

    public static void logHandlerExecTime(Logger logger, long execTime, String handler) {

        if (execTime >= MAX_STRATEGY_EXEC_MS_TIME_FOR_INFO_LOG) {
            logger.info("Handler:[{}] finished in [{}] ms.", handler, execTime);
        } else {
            logger.debug("Handler:[{}] finished in [{}] ms.", handler, execTime);
        }
    }
}