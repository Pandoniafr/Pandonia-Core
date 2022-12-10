package fr.pandonia.core.data.redis.pubsub;

import fr.pandonia.api.data.redis.IRedisConnection;
import fr.pandonia.api.data.redis.pubsub.IPSWriter;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class PSWriter implements IPSWriter {

    private IRedisConnection redisConnection;
    private Jedis jedis;

    private int i;

    public PSWriter(IRedisConnection redisConnection){
        this.redisConnection = redisConnection;
        this.jedis = redisConnection.getResource();

        this.i = 0;
    }

    @Override
    public void send(String channel, String subChannel, Document msg){
        Document document = new Document("subChannel", subChannel);
        document.append("message", msg);
        try {
            jedis.publish(channel, document.toJson());
        }catch (JedisException e){
            this.jedis.close();
            this.jedis = redisConnection.getResource();
            if (i < 5){
                send(channel, subChannel, msg);
                System.out.println("Message renvoyé");
                i++;
            }else{
                i = 0;
                System.out.println("Boucle broken");
            }
        }

    }

    public void close(){
        this.jedis.close();;
    }

}
