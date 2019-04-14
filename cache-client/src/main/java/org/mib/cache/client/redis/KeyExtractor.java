package org.mib.cache.client.redis;

@FunctionalInterface
public interface KeyExtractor<K> {
    String key(K k);
}
