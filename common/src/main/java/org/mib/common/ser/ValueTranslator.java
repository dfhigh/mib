package org.mib.common.ser;

public interface ValueTranslator<K, V> {

    String translate(V v);

    V translateBack(K key, String str);
}
