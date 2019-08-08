package com.brucecloud.dynamicstrategy.core.manager;

import com.brucecloud.dynamicstrategy.core.toml.Configuration;
import com.brucecloud.dynamicstrategy.core.toml.Handler;
import com.moandjiezana.toml.Toml;

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
     */
    public void load(String configFileName) {
        // load config file
        File configFile = new File(absolutePathUrl.getPath(), configFileName);

        transformFromTomlToConfiguration(configFile);
    }

    /**
     * reload config file
     *
     * @param configFileName config file name
     * @return true or false
     */
    public boolean reload(String configFileName) {
        // load config file
        File configFile = new File(absolutePathUrl.getPath(), configFileName);

        // reload if the configuration file is updated
        if (this.lastModified < configFile.lastModified()) {
            transformFromTomlToConfiguration(configFile);
            return true;
        }
        return false;
    }

    /**
     * toml transform to Configuration
     *
     * @param configFile config file
     */
    private void transformFromTomlToConfiguration(File configFile) {
        // update last modified time
        lastModified = configFile.lastModified();

        // file transform toml
        Toml toml = new Toml().read(configFile);

        // toml transform configuration
        configuration = toml.to(Configuration.class);
        configuration.prepare();
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