package com.brucecloud.dynamicstrategy.core.loader;

import com.brucecloud.dynamicstrategy.core.manager.ConfigurationManager;
import com.brucecloud.dynamicstrategy.core.manager.StrategyManager;

import static java.lang.Thread.sleep;

/**
 * Loader class for loading Strategy config file<br>
 * and handler class in some dirs.<br>
 * created at 2019-08-08 13:18.
 *
 * @author sephiroth.
 */
public class SimpleStrategyLoader extends AbstractStrategyLoader {

    protected SimpleStrategyLoader(String jarFileDir, String configFileName) {
        super(jarFileDir, configFileName);
    }

    /**
     * load strategy
     */
    @Override
    public void load() {
        logger.info("Prepare to load strategy config file and handler class");

        // load config
        configurationManager = ConfigurationManager.getInstance();
        configurationManager.load(configFileName, jarFileDir);

        // set configurationManager to StrategyManager
        StrategyManager.getInstance().setConfigurationManager(configurationManager);

        logger.info("Loaded strategy config file:[" + configFileName + "]");

        // start poller for change
        startPoller();
        logger.info("Started poller for dynamic strategy loading.");
    }

    /**
     * start the poller
     */
    private void startPoller() {
        Runnable poller = new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        sleep(10 * 1000);
                        reload();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        executor.execute(poller);
    }

    /**
     * reload strategy
     */
    public void reload() {
        // reload if the configuration file is updated
        configurationManager.reload(configFileName, jarFileDir);
    }
}