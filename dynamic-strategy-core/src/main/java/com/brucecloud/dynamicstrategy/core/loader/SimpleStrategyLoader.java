package com.brucecloud.dynamicstrategy.core.loader;

import com.brucecloud.dynamicstrategy.core.manager.ConfigurationManager;
import com.brucecloud.dynamicstrategy.core.toml.Handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

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

    @Override
    public void load() throws FileNotFoundException {
        logger.info("Prepare to load strategy config file and handler class");

        // load config
        configurationManager = ConfigurationManager.getInstance();
        configurationManager.load(configFileName);

        logger.info("Loaded strategy config file:[" + configFileName + "]");

        // obtain handlers
        List<Handler> handlerList = configurationManager.getHandlerList();

        File jarDir = new File(jarFileDir);

        if (!jarDir.exists()) {
            throw new FileNotFoundException("Strategy jar does'nt exist");
        }
    }
}