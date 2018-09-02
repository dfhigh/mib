package org.mib.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ConfigProvider {

    private static final Pattern WITH_ENV_VALUE = Pattern.compile("\\$\\{(\\w+):(.*)}");

    private static final Properties PREDICTOR_CONFIGS = new Properties();
    static {
        try {
            String externalConfig = System.getProperty("config.location");
            InputStream is;
            if (StringUtils.isNotBlank(externalConfig)) {
                log.info("loading config from external config file {}...", externalConfig);
                is = new FileInputStream(externalConfig);
            } else {
                log.info("loading config from classpath config file application.properties...");
                is = ConfigProvider.class.getResourceAsStream("/application.properties");
            }
            PREDICTOR_CONFIGS.load(is);
            log.info("loaded config as {}", PREDICTOR_CONFIGS);
        } catch (IOException e) {
            log.error("failed to load config", e);
            throw new IllegalStateException("unable to load config");
        }
    }

    public static String get(String key) {
        String raw = PREDICTOR_CONFIGS.getProperty(key);
        if (raw != null) {
            Matcher matcher = WITH_ENV_VALUE.matcher(raw);
            if (matcher.find()) {
                String env = matcher.group(1), defaultValue = matcher.group(2);
                String envValue = System.getenv(env);
                return Optional.ofNullable(envValue).orElse(defaultValue);
            }
        }
        return raw;
    }

    public static String get(String key, String defaultValue) {
        return Optional.ofNullable(get(key)).orElse(defaultValue);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static int getInt(String key, int defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Integer.parseInt(str);
    }

    public static long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public static long getLong(String key, long defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Long.parseLong(str);
    }

    public static short getShort(String key) {
        return Short.parseShort(get(key));
    }

    public static short getShort(String key, short defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Short.parseShort(str);
    }

    public static double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    public static double getDouble(String key, double defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Double.parseDouble(str);
    }

    public static float getFloat(String key) {
        return Float.parseFloat(get(key));
    }

    public static float getFloat(String key, float defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Float.parseFloat(str);
    }

    public static char getChar(String key) {
        return get(key).charAt(0);
    }

    public static char getChar(String key, char defaultValue) {
        String str = get(key);
        return str == null || str.isEmpty() ? defaultValue : str.charAt(0);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Boolean.parseBoolean(str);
    }
}
