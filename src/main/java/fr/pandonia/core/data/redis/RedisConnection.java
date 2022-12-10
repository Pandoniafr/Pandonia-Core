package fr.pandonia.core.data.redis;

import fr.pandonia.api.data.redis.IRedisConnection;
import fr.pandonia.api.data.redis.IRedisCredentials;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection implements IRedisConnection {

    private JedisPool jedisPool;
    private final IRedisCredentials redisCredentials;
    private final int database;

    public RedisConnection(IRedisCredentials redisCredentials, int database) {
        this.redisCredentials = redisCredentials;
        this.database = database;
    }

    @Override
    public JedisPool setupJedis(IRedisCredentials redisCredentials, int database) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(5120);
        jedisPoolConfig.setMaxIdle(1000);
        jedisPoolConfig.setJmxEnabled(false);
        return new JedisPool(jedisPoolConfig, redisCredentials.getIP(), redisCredentials.getPort(), 6000,
                redisCredentials.getPassword(), database, redisCredentials.getClientName());
    }

    @Override
    public void init() {
        if (jedisPool == null) {
            jedisPool = setupJedis(redisCredentials, database);
        }
    }

    @Override
    public void close() {
        if (!jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

    @Override
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public Jedis getResource() {
        return getJedisPool().getResource();
    }

}
