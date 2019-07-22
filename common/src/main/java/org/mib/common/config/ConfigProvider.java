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

    private static final Pattern WITH_ENV_VALUE = Pattern.compile("\\$\\{(\\w+)(:.*)?}");

    private static final Properties CONFIGS = new Properties();
    static {
        try {
            String externalConfig = System.getProperty("config.location");
            boolean shouldLoad = true;
            InputStream is;
            if (StringUtils.isNotBlank(externalConfig)) {
                log.info("loading config from external config file {}...", externalConfig);
                is = new FileInputStream(externalConfig);
            } else {
                log.info("loading config from classpath config file application.properties...");
                is = ConfigProvider.class.getResourceAsStream("/application.properties");
                shouldLoad = is != null;
            }
            if (shouldLoad) {
                CONFIGS.load(is);
                log.info("loaded config as {}", CONFIGS);
            } else {
                log.warn("nothing loaded for config");
            }
        } catch (IOException e) {
            log.error("failed to load config", e);
            throw new IllegalStateException("unable to load config");
        }
    }

    public static String get(String key) {
        return getValueWithEnv(CONFIGS.getProperty(key));
    }

    public static String getValueWithEnv(String expression) {
        if (expression == null) return null;
        Matcher matcher = WITH_ENV_VALUE.matcher(expression);
        if (matcher.find()) {
            String env = matcher.group(1), defaultValueWithColon = matcher.group(2);
            String envValue = System.getenv(env);
            if (defaultValueWithColon != null) {
                String defaultValue = defaultValueWithColon.substring(1);
                return Optional.ofNullable(envValue).orElse(defaultValue);
            }
            return envValue;
        }
        return expression;
    }

    public static String get(String key, String defaultValue) {
        return Optional.ofNullable(get(key)).orElse(defaultValue);
    }

    public static Integer getInt(String key) {
        String value = get(key);
        return value == null ? null : Integer.parseInt(value);
    }

    public static int getInt(String key, int defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Integer.parseInt(str);
    }

    public static Integer getIntWithEnv(String expression) {
        String value = getValueWithEnv(expression);
        return value == null ? null : Integer.parseInt(value);
    }

    public static Long getLong(String key) {
        String value = get(key);
        return value == null ? null : Long.parseLong(value);
    }

    public static long getLong(String key, long defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Long.parseLong(str);
    }

    public static Long getLongWithEnv(String expression) {
        String value = getValueWithEnv(expression);
        return value == null ? null : Long.parseLong(value);
    }

    public static Short getShort(String key) {
        String value = get(key);
        return value == null ? null : Short.parseShort(value);
    }

    public static short getShort(String key, short defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Short.parseShort(str);
    }

    public static Short getShortWithEnv(String expression) {
        String value = getValueWithEnv(expression);
        return value == null ? null : Short.parseShort(value);
    }

    public static Double getDouble(String key) {
        String value = get(key);
        return value == null ? null : Double.parseDouble(value);
    }

    public static double getDouble(String key, double defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Double.parseDouble(str);
    }

    public static Double getDoubleWithEnv(String expression) {
        String value = getValueWithEnv(expression);
        return value == null ? null : Double.parseDouble(value);
    }

    public static Float getFloat(String key) {
        String value = get(key);
        return value == null ? null : Float.parseFloat(value);
    }

    public static float getFloat(String key, float defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Float.parseFloat(str);
    }

    public static Float getFloatWithEnv(String expression) {
        String value = getValueWithEnv(expression);
        return value == null ? null : Float.parseFloat(value);
    }

    public static Character getChar(String key) {
        String value = get(key);
        return value == null ? null : value.charAt(0);
    }

    public static char getChar(String key, char defaultValue) {
        String str = get(key);
        return str == null || str.isEmpty() ? defaultValue : str.charAt(0);
    }

    public static Character getCharWithEnv(String expression) {
        String value = getValueWithEnv(expression);
        return value == null ? null : value.charAt(0);
    }

    public static Boolean getBoolean(String key) {
        String value = get(key);
        return value == null ? null : Boolean.parseBoolean(value);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String str = get(key);
        return str == null ? defaultValue : Boolean.parseBoolean(str);
    }

    public static Boolean getBooleanWithEnv(String expression) {
        String value = getValueWithEnv(expression);
        return value == null ? null : Boolean.parseBoolean(value);
    }
}
