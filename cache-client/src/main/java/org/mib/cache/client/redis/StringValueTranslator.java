package org.mib.cache.client.redis;

public class StringValueTranslator implements ValueTranslator<String> {

    private static volatile ValueTranslator<String> instance = null;

    private StringValueTranslator() {}

    @Override
    public String translate(String s) {
        return s;
    }

    @Override
    public String translateBack(String str) {
        return str;
    }

    public static ValueTranslator<String> getInstance() {
        if (instance == null) {
            synchronized (StringValueTranslator.class) {
                if (instance == null) {
                    instance = new StringValueTranslator();
                }
            }
        }
        return instance;
    }
}
