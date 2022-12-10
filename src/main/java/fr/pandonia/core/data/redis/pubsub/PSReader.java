package fr.pandonia.core.data.redis.pubsub;

import fr.pandonia.api.data.redis.IRedisConnection;
import fr.pandonia.api.data.redis.pubsub.IPSReader;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PSReader implements IPSReader {

    private IRedisConnection redisConnection;
    private Jedis jedis;
    private JedisPubSub jedisPubSub;
    private String[] channels;

    public PSReader(IRedisConnection redisConnection, JedisPubSub jedisPubSub, String... channels){
        this.redisConnection = redisConnection;
        this.jedis = redisConnection.getResource();
        this.jedisPubSub = jedisPubSub;
        this.channels = channels;
    }

    @Override
    public void register(){
        new Thread(() -> {
            try {
                jedis.subscribe(jedisPubSub, channels);
            } catch (Exception e) {
                e.printStackTrace();
                register();
            }
        }).start();
    }

    @Override
    public void close(){
        this.jedis.close();
    }

    @Override
    public void addChannels(String... channels){
        jedis.subscribe(jedisPubSub, channels);
    }

    @Override
    public void addChannel(String channel){
        jedis.subscribe(jedisPubSub, channel);
    }

    @Override
    public Jedis getJedis() {
        return jedis;
    }

    @Override
    public JedisPubSub getJedisPubSub() {
        return jedisPubSub;
    }

    @Override
    public String[] getChannels() {
        return channels;
    }
}
