package org.mib.common.ser;

public class StringValueTranslator<K> implements ValueTranslator<K, String> {

    private static volatile ValueTranslator instance = null;

    private StringValueTranslator() {}

    @Override
    public String translate(String s) {
        return s;
    }

    @Override
    public String translateBack(K key, String str) {
        return str;
    }

    public static ValueTranslator getInstance() {
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
