package com.brucecloud.dynamicstrategy.core.manager;

import com.brucecloud.dynamicstrategy.core.HandlerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To manage strategy class.
 * created at 2019-08-08 15:25.
 *
 * @author sephiroth.
 */
public class StrategyManager {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(StrategyManager.class);

    private ConfigurationManager configurationManager;

    /**
     * IoDH
     */
    private static class Holder {
        private static StrategyManager instance = new StrategyManager();
    }

    /**
     * obtain singleton StrategyManager
     *
     * @return StrategyManager
     */
    public static StrategyManager getInstance() {
        return StrategyManager.Holder.instance;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @SuppressWarnings("unchecked")
    public <Context> HandlerClass<Context> getHandlerClass(String handlerName) {
        return configurationManager.getHandlerClass(handlerName);
    }
}