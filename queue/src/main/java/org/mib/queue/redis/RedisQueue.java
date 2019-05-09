package org.mib.queue.redis;

import org.mib.common.ser.ValueTranslator;
import org.mib.queue.Queue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.common.validator.Validator.validateStringNotBlank;

public class RedisQueue<T> implements Queue<T> {

    private final String name;
    private final JedisPool pool;
    private final ValueTranslator<?, T> valueTranslator;

    public RedisQueue(final String endpoint, final ValueTranslator<?, T> valueTranslator) {
        this(randomAlphanumeric(10), endpoint, valueTranslator);
    }

    public RedisQueue(final String name, final String endpoint, final ValueTranslator<?, T> valueTranslator) {
        validateStringNotBlank(name, "queue name");
        validateStringNotBlank(endpoint, "redis endpoint");
        validateObjectNotNull(valueTranslator, "value translator");
        this.name = name;
        this.pool = fromEndpoint(endpoint);
        this.valueTranslator = valueTranslator;
    }

    protected JedisPool fromEndpoint(String endpoint) {
        String[] fields = endpoint.split(":");
        if (fields.length != 2) throw new IllegalArgumentException("invalid endpoint " + endpoint);
        String host = fields[0];
        int port = Integer.parseInt(fields[1]);
        return new JedisPool(host, port);
    }

    @Override
    public void _push(T element) {
        try (Jedis jedis = pool.getResource()) {
            jedis.lpush(name, valueTranslator.translate(element));
        }
    }

    @Override
    public T _pop() {
        try (Jedis jedis = pool.getResource()) {
            String str = jedis.rpop(name);
            return str == null ? null : valueTranslator.translateBack(null, str);
        }
    }
}
