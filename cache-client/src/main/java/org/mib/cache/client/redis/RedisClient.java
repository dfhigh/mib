package org.mib.cache.client.redis;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.mib.cache.client.CacheClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.Arrays;
import java.util.List;

import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateObjectNotNull;

@Slf4j
public abstract class RedisClient<K, V> implements CacheClient<K, V> {

    private static final String OK = "OK";

    private final KeyExtractor<K> keyExtractor;
    private final ValueTranslator<K, V> valueTranslator;

    protected RedisClient(final KeyExtractor<K> keyExtractor, final ValueTranslator<K, V> valueTranslator) {
        validateObjectNotNull(keyExtractor, "key extractor");
        validateObjectNotNull(valueTranslator, "value translator");
        this.keyExtractor = keyExtractor;
        this.valueTranslator = valueTranslator;
    }

    protected JedisPool fromEndpoint(String endpoint) {
        return fromEndpoint(endpoint, GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
    }

    protected JedisPool fromEndpoint(String endpoint, int maxConn) {
        validateIntPositive(maxConn, "max connection");
        String[] fields = endpoint.split(":");
        if (fields.length != 2) throw new IllegalArgumentException("invalid endpoint " + endpoint);
        String host = fields[0];
        int port = Integer.parseInt(fields[1]);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxConn);
        config.setMaxIdle(maxConn);
        return new JedisPool(config, host, port);
    }

    protected abstract Jedis getJedis(String key, boolean isWrite);

    @Override
    public void _set(K key, V value) {
        String cacheKey = keyExtractor.key(key), cacheValue = valueTranslator.translate(value);
        log.debug("setting key {} as {} and value {} as {}...", key, cacheKey, value, cacheValue);
        try (Jedis jedis = getJedis(cacheKey, true)) {
            String statusCode = jedis.set(cacheKey, cacheValue);
            log.debug("set key {} with status {}", cacheKey, statusCode);
            if (!OK.equals(statusCode)) throw new RuntimeException("failed to set key " + key);
        }
    }

    @Override
    public void _set(K key, V value, int ttlSeconds) {
        String cacheKey = keyExtractor.key(key), cacheValue = valueTranslator.translate(value);
        log.debug("setting key {} as {} and value {} as {} with ttl {}...", key, cacheKey, value, cacheValue, ttlSeconds);
        try (Jedis jedis = getJedis(cacheKey, true)) {
            String statusCode = jedis.setex(cacheKey, ttlSeconds, cacheValue);
            log.debug("setex key {} with status {}", cacheKey, statusCode);
            if (!OK.equals(statusCode)) throw new RuntimeException("failed to setex key " + key);
        }
    }

    @Override
    public void _mset(List<K> keys, List<V> values) {
        String[] kvs = compact(keys, values);
        log.debug("setting keys and values {}...", (Object) kvs);
        try (Jedis jedis = getJedis(kvs[0], true)) {
            String statusCode = jedis.mset(kvs);
            log.debug("mset keys and values {} with status {}", kvs, statusCode);
            if (!OK.equals(statusCode)) throw new RuntimeException("failed to mset");
        }
    }

    @Override
    public void _mset(List<K> keys, List<V> values, int ttlSeconds) {
        log.debug("setting keys {} and values {} with ttl {}...", keys, values, ttlSeconds);
        String[] cacheKeys = keys.stream().map(keyExtractor::key).toArray(String[]::new);
        String[] cacheValues = values.stream().map(valueTranslator::translate).toArray(String[]::new);
        try (Jedis jedis = getJedis(cacheKeys[0], true)) {
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < cacheKeys.length; i++) {
                p.setex(cacheKeys[i], ttlSeconds, cacheValues[i]);
            }
            p.sync();
        }
    }

    @Override
    public void _mset(List<K> keys, List<V> values, List<Integer> ttlSeconds) {
        log.debug("setting keys {} and values {} with ttl {}...", keys, values, ttlSeconds);
        String[] cacheKeys = keys.stream().map(keyExtractor::key).toArray(String[]::new);
        String[] cacheValues = values.stream().map(valueTranslator::translate).toArray(String[]::new);
        try (Jedis jedis = getJedis(cacheKeys[0], true)) {
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < cacheKeys.length; i++) {
                p.setex(cacheKeys[i], ttlSeconds.get(i), cacheValues[i]);
            }
            p.sync();
        }
    }

    @Override
    public void _mset(K[] keys, V[] values) {
        String[] kvs = compact(keys, values);
        log.debug("setting keys and values {}...", (Object) kvs);
        try (Jedis jedis = getJedis(kvs[0], true)) {
            String statusCode = jedis.mset(kvs);
            log.debug("mset keys and values {} with status {}", kvs, statusCode);
            if (!OK.equals(statusCode)) throw new RuntimeException("failed to mset");
        }
    }

    @Override
    public void _mset(K[] keys, V[] values, int ttlSeconds) {
        log.debug("setting keys {} and values {} with ttl {}...", keys, values, ttlSeconds);
        String[] cacheKeys = Arrays.stream(keys).map(keyExtractor::key).toArray(String[]::new);
        String[] cacheValues = Arrays.stream(values).map(valueTranslator::translate).toArray(String[]::new);
        try (Jedis jedis = getJedis(cacheKeys[0], true)) {
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < cacheKeys.length; i++) {
                p.setex(cacheKeys[i], ttlSeconds, cacheValues[i]);
            }
            p.sync();
        }
    }

    @Override
    public void _mset(K[] keys, V[] values, int[] ttlSeconds) {
        log.debug("setting keys {} and values {} with ttl {}...", keys, values, ttlSeconds);
        String[] cacheKeys = Arrays.stream(keys).map(keyExtractor::key).toArray(String[]::new);
        String[] cacheValues = Arrays.stream(values).map(valueTranslator::translate).toArray(String[]::new);
        try (Jedis jedis = getJedis(cacheKeys[0], true)) {
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < cacheKeys.length; i++) {
                p.setex(cacheKeys[i], ttlSeconds[i], cacheValues[i]);
            }
            p.sync();
        }
    }

    @Override
    public V _get(K key) {
        String cacheKey = keyExtractor.key(key);
        log.debug("retrieving by key {}...", cacheKey);
        try (Jedis jedis = getJedis(cacheKey, false)) {
            String cacheValue = jedis.get(cacheKey);
            log.debug("retrieved {} for key {}", cacheValue, cacheKey);
            return cacheValue == null ? null : valueTranslator.translateBack(key, cacheValue);
        }
    }

    @Override
    public List<V> _mget(List<K> keys) {
        String[] cacheKeys = keys.stream().map(keyExtractor::key).toArray(String[]::new);
        log.debug("retrieving by keys {}...", (Object) cacheKeys);
        try (Jedis jedis = getJedis(cacheKeys[0], false)) {
            List<String> cacheValues = jedis.mget(cacheKeys);
            log.debug("retrieved values {} for keys {}", cacheValues, cacheKeys);
            List<V> values = Lists.newArrayListWithCapacity(keys.size());
            for (int i = 0; i < keys.size(); i++) {
                values.add(cacheValues.get(i) == null ? null : valueTranslator.translateBack(keys.get(i), cacheValues.get(i)));
            }
            return values;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] _mget(K[] keys) {
        String[] cacheKeys = Arrays.stream(keys).map(keyExtractor::key).toArray(String[]::new);
        log.debug("retrieving by keys {}...", (Object) cacheKeys);
        try (Jedis jedis = getJedis(cacheKeys[0], false)) {
            List<String> cacheValues = jedis.mget(cacheKeys);
            log.debug("retrieved values {} for keys {}", cacheValues, cacheKeys);
            V[] values = (V[]) new Object[keys.length];
            for (int i = 0; i < keys.length; i++) {
                values[i] = cacheValues.get(i) == null ? null : valueTranslator.translateBack(keys[i], cacheValues.get(i));
            }
            return values;
        }
    }

    @Override
    public void _del(K key) {
        String cacheKey = keyExtractor.key(key);
        log.debug("deleting by key {}...", cacheKey);
        try (Jedis jedis = getJedis(cacheKey, true)) {
            long deleted = jedis.del(cacheKey);
            log.debug("deleted {} key {}", deleted, cacheKey);
        }
    }

    @Override
    public void _mdel(List<K> keys) {
        String[] cacheKeys = keys.stream().map(keyExtractor::key).toArray(String[]::new);
        log.debug("deleting by keys {}...", (Object) cacheKeys);
        try (Jedis jedis = getJedis(cacheKeys[0], true)) {
            long deleted = jedis.del(cacheKeys);
            log.debug("deleted {} keys {}", deleted, cacheKeys);
        }
    }

    @Override
    public void _mdel(K[] keys) {
        String[] cacheKeys = Arrays.stream(keys).map(keyExtractor::key).toArray(String[]::new);
        log.debug("deleting by keys {}...", (Object) cacheKeys);
        try (Jedis jedis = getJedis(cacheKeys[0], true)) {
            long deleted = jedis.del(cacheKeys);
            log.debug("deleted {} keys {}", deleted, cacheKeys);
        }
    }

    private String[] compact(List<K> keys, List<V> values) {
        String[] kvs = new String[keys.size() + values.size()];
        for (int i = 0; i < keys.size(); i++) {
            int index = 2 * i;
            kvs[index] = keyExtractor.key(keys.get(i));
            kvs[index+1] = valueTranslator.translate(values.get(i));
        }
        return kvs;
    }

    private String[] compact(K[] keys, V[] values) {
        String[] kvs = new String[keys.length + values.length];
        for (int i = 0; i < keys.length; i++) {
            int index = 2 * i;
            kvs[index] = keyExtractor.key(keys[i]);
            kvs[index+1] = valueTranslator.translate(values[i]);
        }
        return kvs;
    }
}
