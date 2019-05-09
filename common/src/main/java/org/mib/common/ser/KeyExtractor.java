package org.mib.common.ser;

@FunctionalInterface
public interface KeyExtractor<K> {
    String key(K k);
}
