package org.mib.cache.client.redis;

public interface ValueTranslator<V> {

    String translate(V v);

    V translateBack(String str);
}
