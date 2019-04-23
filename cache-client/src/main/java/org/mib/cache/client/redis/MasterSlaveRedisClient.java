package org.mib.cache.client.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.mib.common.validator.Validator.validateCollectionNotEmptyContainsNoNull;
import static org.mib.common.validator.Validator.validateStringNotBlank;

public class MasterSlaveRedisClient<K, V> extends RedisClient<K, V> {

    private final JedisPool masterPool;
    private final List<JedisPool> slavePools;

    public MasterSlaveRedisClient(final String masterEndpoint, final Set<String> slaveEndpoints,
                                  final KeyExtractor<K> keyExtractor, final ValueTranslator<K, V> valueTranslator) {
        super(keyExtractor, valueTranslator);
        validateStringNotBlank(masterEndpoint, "master endpoint");
        validateCollectionNotEmptyContainsNoNull(slaveEndpoints, "slave endpoints");
        slaveEndpoints.forEach(se -> validateStringNotBlank(se, "slave endpoint"));
        this.masterPool = fromEndpoint(masterEndpoint);
        slaveEndpoints.add(masterEndpoint);
        this.slavePools = slaveEndpoints.stream().map(this::fromEndpoint).collect(Collectors.toList());
    }

    @Override
    protected Jedis getJedis(String key, boolean isWrite) {
        return isWrite ? masterPool.getResource() : slavePools.get(ThreadLocalRandom.current().nextInt(slavePools.size())).getResource();
    }
}
