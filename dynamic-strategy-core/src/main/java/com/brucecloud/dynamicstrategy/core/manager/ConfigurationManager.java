package com.brucecloud.dynamicstrategy.core.manager;

import com.brucecloud.dynamicstrategy.core.toml.Configuration;
import com.brucecloud.dynamicstrategy.core.toml.Handler;
import com.moandjiezana.toml.Toml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * This class provides some methods to manager configuration.
 * created at 2019-08-08 13:29.
 *
 * @author sephiroth.
 */
public class ConfigurationManager {
    /**
     * logger
     */
    protected Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

    /**
     * strategy config object
     */
    private Configuration configuration;

    /**
     * config file's parent director's<br>
     * absolute path url
     */
    private final URL absolutePathUrl;

    /**
     * config file's last modified million time
     */
    private long lastModified;

    /**
     * private constructor
     */
    private ConfigurationManager() {
        absolutePathUrl = Thread.currentThread().getContextClassLoader().getResource("/");
    }

    /**
     * IoDH
     */
    private static class Holder {
        private static ConfigurationManager instance = new ConfigurationManager();
    }

    /**
     * obtain singleton ConfigurationManager
     *
     * @return ConfigurationManager
     */
    public static ConfigurationManager getInstance() {
        return Holder.instance;
    }

    /**
     * load config file
     *
     * @param configFileName config file name
     * @param jarFileDir     jar path
     */
    public void load(String configFileName, String jarFileDir) {
        // load config file
        File configFile = new File(absolutePathUrl.getPath(), configFileName);

        transformFromTomlToConfiguration(configFile, jarFileDir);
    }

    /**
     * reload config file
     *
     * @param configFileName config file name
     * @param jarFileDir     jar path
     * @return true or false
     */
    public boolean reload(String configFileName, String jarFileDir) {
        // load config file
        File configFile = new File(absolutePathUrl.getPath(), configFileName);

        // reload if the configuration file is updated
        if (this.lastModified < configFile.lastModified()) {
            logger.info("Strategy config file changed, reloading...");
            transformFromTomlToConfiguration(configFile, jarFileDir);
            logger.info("Reload Finished");
            return true;
        }
        return false;
    }

    /**
     * toml transform to Configuration
     *
     * @param configFile config file
     * @param jarFileDir jar path
     */
    private void transformFromTomlToConfiguration(File configFile, String jarFileDir) {
        // update last modified time
        lastModified = configFile.lastModified();

        // file transform toml
        Toml toml = new Toml().read(configFile);

        // toml transform configuration
        configuration = toml.to(Configuration.class);
        configuration.prepare(jarFileDir);
    }

    /**
     * obtain handlers
     *
     * @return handlers
     */
    public List<Handler> getHandlerList() {
        return this.configuration.getHandlerList();
    }
}