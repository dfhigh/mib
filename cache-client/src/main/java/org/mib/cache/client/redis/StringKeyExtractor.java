package org.mib.cache.client.redis;

public class StringKeyExtractor implements KeyExtractor<String> {

    private static volatile KeyExtractor<String> instance = null;

    private StringKeyExtractor() {}

    @Override
    public String key(String s) {
        return s;
    }

    public static KeyExtractor<String> getInstance() {
        if (instance == null) {
            synchronized (StringKeyExtractor.class) {
                if (instance == null) {
                    instance = new StringKeyExtractor();
                }
            }
        }
        return instance;
    }
}
