package fr.pandonia.core.server;

import fr.pandonia.api.application.PandoniaApplication;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerDataManager {

    private PandoniaApplication instance;

    private String serverKey;

    public ServerDataManager(PandoniaApplication instance) {
        this.instance = instance;
        serverKey = "servers";
    }

    public Server getServer(String field) {
        Jedis jedis = getJedis();
        Document server = Document.parse(jedis.hget(serverKey, field));
        jedis.close();
        return Server.fromDocument(server);
    }

    public void setServer(Server server) {
        Jedis jedis = getJedis();
        jedis.hset(serverKey, server.getName(), server.toDocument().toJson());
        jedis.close();
    }

    public List<Server> getAll() {
        Jedis jedis = getJedis();
        List<Server> serversList = new ArrayList<>();
        for(Map.Entry<String, String> entry : jedis.hgetAll(serverKey).entrySet()) {
            serversList.add(Server.fromDocument(Document.parse(entry.getValue())));
        }
        jedis.close();
        return serversList;
    }

    public void deleteServer(String field) {
        Jedis jedis = getJedis();
        jedis.hdel(serverKey, field);
        jedis.close();
    }

    public void clear(){
        Jedis jedis = getJedis();
        jedis.del(serverKey);
        jedis.close();
    }

    public Jedis getJedis(){
        return instance.getRedisConnection().getResource();
    }

}
