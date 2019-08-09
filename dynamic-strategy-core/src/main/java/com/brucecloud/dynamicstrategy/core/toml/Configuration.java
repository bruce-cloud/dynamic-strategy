package com.brucecloud.dynamicstrategy.core.toml;

import com.brucecloud.dynamicstrategy.core.HandlerClass;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent the toml config file.
 * created at 2019-08-08 14:12.
 *
 * @author sephiroth.
 */
@Data
public class Configuration {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    /**
     * handler list
     */
    private List<Handler> handlerList;

    /**
     * strategy list
     */
    private List<Strategy> strategyList;

    /**
     * key: strategyName
     * value: Strategy Bean
     */
    private Map<String, Strategy> strategyMap;

    /**
     * key: handlerName
     * value: HandlerClass instance
     */
    private Map<String, HandlerClass> handlerMap;

    public Strategy getStrategy(String name) {
        return strategyMap.get(name);
    }

    public HandlerClass getHandlerClass(String handlerName) {
        return handlerMap.get(handlerName);
    }

    public void prepare(String jarFileDir) {
        composeStrategy();
        composeHandler(jarFileDir);
    }

    private void composeStrategy() {
        if (strategyList == null || strategyList.isEmpty()) {
            throw new IllegalArgumentException("Configuration prepare failed, because strategyList is empty!");
        }

        strategyMap = new HashMap<>(strategyList.size());
        for (Strategy strategy : strategyList) {
            strategyMap.put(strategy.getName(), strategy);
        }
    }

    private void composeHandler(String jarFileDir) {
        if (handlerList == null || handlerList.isEmpty()) {
            throw new IllegalArgumentException("Configuration prepare failed, because handlerList is empty!");
        }

        File jarDir = new File(jarFileDir);

        if (!jarDir.exists()) {
            logger.error("Strategy jar does'nt exist! jarFileDir={}", jarFileDir);
            throw new IllegalArgumentException("jarFileDir is invalid!");
        }

        handlerMap = new HashMap<>(handlerList.size());
        for (Handler handler : handlerList) {
            File jarFile = new File(jarDir, handler.getJar());
            URL jarFileUrl = null;
            try {
                jarFileUrl = jarFile.toURI().toURL();
            } catch (MalformedURLException e) {
                logger.error("obtain jarFileUrl failed!", e);
                throw new IllegalArgumentException();
            }

            try (URLClassLoader loader = new URLClassLoader(new URL[]{jarFileUrl}, Thread.currentThread().getContextClassLoader())) {
                logger.info("Strategy class loader:" + loader);

                URL[] urls = loader.getURLs();

                for (URL url : urls) {
                    logger.info("Strategy class loader paths : " + url.getPath());
                }

                @SuppressWarnings("unchecked")
                Class<HandlerClass> clazz = (Class<HandlerClass>) Class.forName(handler.getClassName());

                // handlerClass instance
                HandlerClass handlerClass = clazz.newInstance();

                handlerMap.put(handler.getName(), handlerClass);
                logger.info("Finish loading handlerClass:[" + handler.getClassName() + "].");

            } catch (IOException e) {
                logger.error("obtain URLClassLoader failed!", e);
                throw new IllegalArgumentException();
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                e.printStackTrace();
                throw new IllegalArgumentException();
            }
        }
    }
}