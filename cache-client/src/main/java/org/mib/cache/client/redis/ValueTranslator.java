package org.mib.cache.client.redis;

public interface ValueTranslator<K, V> {

    String translate(V v);

    V translateBack(K key, String str);
}
