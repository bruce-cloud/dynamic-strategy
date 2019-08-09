package com.brucecloud.dynamicstrategy.core;

import com.brucecloud.dynamicstrategy.core.manager.ConfigurationManager;
import com.brucecloud.dynamicstrategy.core.manager.StrategyManager;
import com.brucecloud.dynamicstrategy.core.toml.Strategy;
import com.brucecloud.dynamicstrategy.core.util.LogUtils;
import com.brucecloud.dynamicstrategy.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A class for Strategy Executing by the clients.
 * created at 2019-08-09 16:08.
 *
 * @author sephiroth.
 */
public class StrategyExecutor {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(StrategyExecutor.class);

    private ConfigurationManager configurationManager;

    private StrategyManager strategyManager;

    private StrategyExecutor() {
        configurationManager = ConfigurationManager.getInstance();
        strategyManager = StrategyManager.getInstance();
    }

    /**
     * IoDH
     */
    public static class Holder {
        static StrategyExecutor instance = new StrategyExecutor();
    }

    /**
     * obtain singleton StrategyExecutor
     *
     * @return StrategyExecutor
     */
    public static StrategyExecutor getInstance() {
        return Holder.instance;
    }

    /**
     * Execute strategy.
     *
     * @param strategyName strategy name
     * @param context      first handler's context
     * @return last handler's return value
     */
    public <Context> Context execute(String strategyName, Context context) {
        // Executor's exec begin time
        long executorBeginTime = System.currentTimeMillis();

        if (StringUtils.isEmpty(strategyName)) {
            throw new IllegalArgumentException("strategy name is empty");
        }

        Strategy strategy = configurationManager.getStrategy(strategyName);
        if (null == strategy) {
            logger.error("Strategy:[" + strategyName + "] not exist.");
            throw new IllegalArgumentException("Strategy:[" + strategyName + "] not exist.");
        }

        List<String> sequence = strategy.getSequence();
        if (sequence.isEmpty()) {
            logger.warn("Strategy:[" + strategyName + "] sequence is empty.");
        }

        // Only one handler
        if (sequence.size() == 1) {

            // get handler class from strategy
            HandlerClass<Context> handlerClass = strategyManager.getHandlerClass(sequence.get(0));
            long handlerBeginTime = System.currentTimeMillis();
            Context result = handlerClass.handler(context);
            long handlerEndTime = System.currentTimeMillis();
            LogUtils.logHandlerExecTime(logger, handlerEndTime - handlerBeginTime, sequence.get(0));

            // Executor's exec end time
            long executorEndTime = System.currentTimeMillis();
            logger.debug("Executor finished in [{}] ms.", executorEndTime - executorBeginTime);
            return result;
        }

        for (String handlerName : sequence) {
            HandlerClass<Context> handlerClass = strategyManager.getHandlerClass(handlerName);
            if (null == handlerClass) {
                logger.error("Handler class is null, for strategy:[{}] -> handler:[{}]", strategyName, handlerName);
                throw new RuntimeException("Handler class is null, for strategy:[" + strategyName + "]");
            }

            long handlerBegTime = System.currentTimeMillis();
            context = handlerClass.handler(context);
            long handlerEndTime = System.currentTimeMillis();
            LogUtils.logHandlerExecTime(logger, handlerEndTime - handlerBegTime, handlerName);
        }

        long executorEndTime = System.currentTimeMillis();
        logger.debug("Executor finished in [{}] ms", executorEndTime - executorBeginTime);

        return context;
    }
}