package org.mib.cache.client.redis;

import org.mib.common.ser.KeyExtractor;
import org.mib.common.ser.ValueTranslator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.mib.common.validator.Validator.validateStringNotBlank;

public class SingleInstanceRedisClient<K, V> extends RedisClient<K, V> {

    private final JedisPool pool;

    public SingleInstanceRedisClient(final KeyExtractor<K> keyExtractor, final ValueTranslator<K, V> valueTranslator,
                                     final String endpoint) {
        super(keyExtractor, valueTranslator);
        validateStringNotBlank(endpoint, "endpoint");
        this.pool = fromEndpoint(endpoint);
    }

    @Override
    protected Jedis getJedis(String key, boolean isWrite) {
        return pool.getResource();
    }
}
