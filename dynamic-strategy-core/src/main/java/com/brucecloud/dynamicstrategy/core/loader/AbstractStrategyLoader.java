package com.brucecloud.dynamicstrategy.core.loader;

import com.brucecloud.dynamicstrategy.core.manager.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * This class is template for loading Strategy config file.
 * created at 2019-08-08 14:39.
 *
 * @author sephiroth.
 */
abstract class AbstractStrategyLoader implements StrategyLoader {
    /**
     * logger
     */
    protected Logger logger = LoggerFactory.getLogger(AbstractStrategyLoader.class);

    /**
     * jar file dir
     */
    protected String jarFileDir;

    /**
     * config file name
     */
    protected String configFileName;

    /**
     * poller thread for reloading
     */
    protected ExecutorService executor = new ThreadPoolExecutor(1
            , 1
            , 0
            , TimeUnit.MILLISECONDS
            , new LinkedBlockingQueue<Runnable>()
            , new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Poller-Pool-1");
                    return thread;
                }
            });

    /**
     * poller thread running flag
     */
    protected boolean isRunning = true;

    /**
     * configuration manager
     */
    protected ConfigurationManager configurationManager;

    protected AbstractStrategyLoader(String jarFileDir, String configFileName) {
        this.jarFileDir = jarFileDir;
        this.configFileName = configFileName;
    }
}