package com.brucecloud.dynamicstrategy.core.toml;

import lombok.Data;

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

    public void prepare() {
        if (strategyList == null || strategyList.isEmpty()) {
            throw new IllegalArgumentException("Configuration prepare failed, because strategyList is empty!");
        }

        if (handlerList == null || handlerList.isEmpty()) {
            throw new IllegalArgumentException("Configuration prepare failed, because handlerList is empty!");
        }

        strategyMap = new HashMap<>(strategyList.size());
        for (Strategy s : strategyList) {
            strategyMap.put(s.getName(), s);
        }
    }
}