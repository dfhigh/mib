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

    private static final String REGEX = "\\$\\{(.*?)}";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

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
        String[] fields = expression.split(REGEX);
        Matcher matcher = PATTERN.matcher(expression);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            if (i < fields.length) sb.append(fields[i++]);
            String exp = matcher.group(1);
            int index = exp.indexOf(':');
            if (index > 0) {
                String env = exp.substring(0, index), defaultValue = exp.substring(index+1);
                sb.append(Optional.ofNullable(System.getenv(env)).orElse(defaultValue));
            } else {
                sb.append(System.getenv(exp));
            }
        }
        return sb.length() == 0 ? expression : sb.toString();
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
