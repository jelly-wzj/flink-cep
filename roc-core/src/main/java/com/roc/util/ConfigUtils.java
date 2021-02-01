package com.roc.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jelly.wang
 */
public final class ConfigUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtils.class);
    private static Map<String, Properties> cache = new ConcurrentHashMap<>();

    private ConfigUtils() {

    }

    /**
     * 加载properties文件， 默认读取缓存
     *
     * @param filePath 文件路径,相对于classpath路径
     * @return {@code Properties}, 如果filePath为空返回null
     */
    public static Properties loadProperties(String filePath) {
        return loadProperties(filePath, true);
    }

    /**
     * 加载properties文件
     *
     * @param filePath 文件路径,相对于classpath路径
     * @param isCache  {@code true} 读取缓存, 否则实时加载
     * @return {@code Properties}, 如果filePath为空返回null
     */
    public static Properties loadProperties(String filePath, boolean isCache) {
        if (isCache) {
            Properties props = cache.get(filePath);
            if (null != props) {
                return props;
            }
        }

        Properties props = new Properties();
        if (StringUtils.isNotEmpty(filePath)) {
            InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(filePath);
            try {
                if (null == inputStream) inputStream = new FileInputStream(filePath);
                props.load(inputStream);
                cache.put(filePath, props);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return props;
    }

    /**
     * 获取Config.properties配置信息
     *
     * @param key 键
     * @return 值
     */
    public static String getString(String key) {
        Properties props = loadProperties("config.properties");
        return props.getProperty(key);
    }

    /**
     * 获取Config.properties配置信息
     *
     * @param key 键
     * @return 值
     */
    public static String getString(String key, String defaultValue) {
        String result = getString(key);
        if (result != null) {
            return result;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取Config.properties配置信息
     *
     * @param key
     * @return
     */
    public static Integer getInt(String key, Integer defaultValue) {
        String result = getString(key);
        if (result != null) {
            return Integer.valueOf(result);
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取Config.properties配置信息
     *
     * @param key key
     * @return String[]
     */
    public static String[] getStrArray(String key) {
        String result = getString(key);
        if (StringUtils.isNotEmpty(result)) {
            return result.split(",");
        } else {
            return new String[]{};
        }
    }
}