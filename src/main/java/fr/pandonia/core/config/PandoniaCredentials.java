package fr.pandonia.core.config;

import org.bson.Document;

public class PandoniaCredentials {

    private Document response;

    public PandoniaCredentials(String mongoHost, int mongoPort, String mongoUsername, String mongoPassword, String mongoDatabase, String redisHost, int redisPort, String redisUsername, String redisPassword){
        response = new Document();
        Document mongo = new Document();
        mongo.append("host", mongoHost);
        mongo.append("port", mongoPort);
        mongo.append("username", mongoUsername);
        mongo.append("password", mongoPassword);
        mongo.append("database", mongoDatabase);
        Document redis = new Document();
        redis.append("host", redisHost);
        redis.append("port", redisPort);
        redis.append("username", redisUsername);
        redis.append("password", redisPassword);
        response.append("mongo", mongo);
        response.append("redis", redis);
    }

    public Document getResponse() {
        return response;
    }

    public Document getMongoDocument(){
        return (Document) getResponse().get("mongo");
    }

    public Document getRedisDocument(){
        return (Document) getResponse().get("redis");
    }

    public String getMongoHost(){
        return getMongoDocument().getString("host");
    }

    public int getMongoPort(){
        return getMongoDocument().getInteger("port");
    }

    public String getMongoUsername(){
        return getMongoDocument().getString("username");
    }

    public String getMongoPassword(){
        return getMongoDocument().getString("password");
    }

    public String getMongoDatabase(){
        return getMongoDocument().getString("database");
    }

    public String getRedisHost(){
        return getRedisDocument().getString("host");
    }

    public int getRedisPort(){
        return getRedisDocument().getInteger("port");
    }

    public String getRedisUsername(){
        return getRedisDocument().getString("username");
    }

    public String getRedisPassword(){
        return getRedisDocument().getString("password");
    }

}