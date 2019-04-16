package org.mib.cache.client;

import org.mib.metrics.Metrics;
import org.mib.metrics.MetricsScope;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mib.common.validator.Validator.validateArrayNotEmptyContainsNoNull;
import static org.mib.common.validator.Validator.validateCollectionNotEmptyContainsNoNull;
import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateObjectNotNull;

@SuppressWarnings("Duplicates")
public interface CacheClient<K, V> {

    void _set(K key, V value);

    default void set(K key, V value) {
        validateObjectNotNull(key, "key");
        validateObjectNotNull(value, "value");
        long start = System.currentTimeMillis();
        _set(key, value);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("set", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("set", 1);
    }

    void _set(K key, V value, int ttlSeconds);

    default void set(K key, V value, int ttlSeconds) {
        validateObjectNotNull(key, "key");
        validateObjectNotNull(value, "value");
        validateIntPositive(ttlSeconds, "ttl");
        long start = System.currentTimeMillis();
        _set(key, value, ttlSeconds);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("setEx", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("setEx", 1);
    }

    default void _mset(List<K> keys, List<V> values) {
        for (int i = 0; i < keys.size(); i++) {
            _set(keys.get(i), values.get(i));
        }
    }

    default void mset(List<K> keys, List<V> values) {
        validateCollectionNotEmptyContainsNoNull(keys, "key list");
        validateCollectionNotEmptyContainsNoNull(values, "value list");
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("key count does not match with value count");
        }
        long start = System.currentTimeMillis();
        _mset(keys, values);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("mset", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("mset", 1);
        metrics.addCounter("r.mset.size", keys.size());
    }

    default void _mset(List<K> keys, List<V> values, int ttlSeconds) {
        for (int i = 0; i < keys.size(); i++) {
            _set(keys.get(i), values.get(i), ttlSeconds);
        }
    }

    default void mset(List<K> keys, List<V> values, int ttlSeconds) {
        validateCollectionNotEmptyContainsNoNull(keys, "key list");
        validateCollectionNotEmptyContainsNoNull(values, "value list");
        validateIntPositive(ttlSeconds, "ttl");
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("key count does not match with value count");
        }
        long start = System.currentTimeMillis();
        _mset(keys, values, ttlSeconds);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("msetEx", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("msetEx", 1);
        metrics.addCounter("r.msetEx.size", keys.size());
    }

    default void _mset(List<K> keys, List<V> values, List<Integer> ttlSeconds) {
        for (int i = 0; i < keys.size(); i++) {
            _set(keys.get(i), values.get(i), ttlSeconds.get(i));
        }
    }

    default void mset(List<K> keys, List<V> values, List<Integer> ttlSeconds) {
        validateCollectionNotEmptyContainsNoNull(keys, "key list");
        validateCollectionNotEmptyContainsNoNull(values, "value list");
        validateCollectionNotEmptyContainsNoNull(ttlSeconds, "ttl list");
        if (keys.size() != values.size() || keys.size() != ttlSeconds.size()) {
            throw new IllegalArgumentException("key count does not match with value count or ttl count");
        }
        ttlSeconds.forEach(ttl -> validateIntPositive(ttl, "ttl"));
        long start = System.currentTimeMillis();
        _mset(keys, values, ttlSeconds);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("msetEx", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("msetEx", 1);
        metrics.addCounter("r.msetEx.size", keys.size());
    }

    default void _mset(K[] keys, V[] values) {
        for (int i = 0; i < keys.length; i++) {
            _set(keys[i], values[i]);
        }
    }

    default void mset(K[] keys, V[] values) {
        validateArrayNotEmptyContainsNoNull(keys, "key array");
        validateArrayNotEmptyContainsNoNull(values, "value array");
        if (keys.length != values.length) {
            throw new IllegalArgumentException("key count does not match with value count");
        }
        long start = System.currentTimeMillis();
        _mset(keys, values);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("mset", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("mset", 1);
        metrics.addCounter("r.mset.size", keys.length);
    }

    default void _mset(K[] keys, V[] values, int ttlSeconds) {
        for (int i = 0; i < keys.length; i++) {
            _set(keys[i], values[i], ttlSeconds);
        }
    }

    default void mset(K[] keys, V[] values, int ttlSeconds) {
        validateArrayNotEmptyContainsNoNull(keys, "key array");
        validateArrayNotEmptyContainsNoNull(values, "value array");
        validateIntPositive(ttlSeconds, "ttl");
        if (keys.length != values.length) {
            throw new IllegalArgumentException("key count does not match with value count");
        }
        long start = System.currentTimeMillis();
        _mset(keys, values, ttlSeconds);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("msetEx", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("msetEx", 1);
        metrics.addCounter("r.msetEx.size", keys.length);
    }

    default void _mset(K[] keys, V[] values, int[] ttlSeconds) {
        for (int i = 0; i < keys.length; i++) {
            _set(keys[i], values[i], ttlSeconds[i]);
        }
    }

    default void mset(K[] keys, V[] values, int[] ttlSeconds) {
        validateArrayNotEmptyContainsNoNull(keys, "key array");
        validateArrayNotEmptyContainsNoNull(values, "value array");
        if (keys.length != values.length || keys.length != ttlSeconds.length) {
            throw new IllegalArgumentException("key count does not match with value count or ttl count");
        }
        Arrays.stream(ttlSeconds).forEach(ttl -> validateIntPositive(ttl, "ttl"));
        long start = System.currentTimeMillis();
        _mset(keys, values, ttlSeconds);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("msetEx", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("msetEx", 1);
        metrics.addCounter("r.msetEx.size", keys.length);
    }

    V _get(K key);

    default V get(K key) {
        validateObjectNotNull(key, "key");
        long start = System.currentTimeMillis();
        V result = _get(key);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("get", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("get", 1);
        metrics.addCounter("r.get.hit", result == null ? 0 : 1);
        return result;
    }

    default List<V> _mget(List<K> keys) {
        return keys.stream().map(this::_get).collect(Collectors.toList());
    }

    default List<V> mget(List<K> keys) {
        validateCollectionNotEmptyContainsNoNull(keys, "key list");
        long start = System.currentTimeMillis();
        List<V> result = _mget(keys);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("mget", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("mget", 1);
        metrics.addCounter("r.mget.size", keys.size());
        metrics.addCounter("r.mget.hit", result.stream().filter(Objects::nonNull).count());
        return result;
    }

    @SuppressWarnings("unchecked")
    default V[] _mget(K[] keys) {
        return (V[]) Arrays.stream(keys).map(this::_get).toArray();
    }

    default V[] mget(K[] keys) {
        validateArrayNotEmptyContainsNoNull(keys, "key array");
        long start = System.currentTimeMillis();
        V[] result = _mget(keys);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("mget", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("mget", 1);
        metrics.addCounter("r.mget.size", keys.length);
        metrics.addCounter("r.mget.hit", Arrays.stream(result).filter(Objects::nonNull).count());
        return result;
    }

    void _del(K key);

    default void del(K key) {
        validateObjectNotNull(key, "key");
        long start = System.currentTimeMillis();
        _del(key);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("del", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("del", 1);
    }

    default void _mdel(List<K> keys) {
        keys.forEach(this::_del);
    }

    default void mdel(List<K> keys) {
        validateCollectionNotEmptyContainsNoNull(keys, "key list");
        long start = System.currentTimeMillis();
        _mdel(keys);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("mdel", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("mdel", 1);
        metrics.addCounter("r.mdel.size", keys.size());
    }

    default void _mdel(K[] keys) {
        Arrays.stream(keys).forEach(this::_del);
    }

    default void mdel(K[] keys) {
        validateArrayNotEmptyContainsNoNull(keys, "key array");
        long start = System.currentTimeMillis();
        _mdel(keys);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("mdel", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("mdel", 1);
        metrics.addCounter("r.mdel.size", keys.length);
    }
}
