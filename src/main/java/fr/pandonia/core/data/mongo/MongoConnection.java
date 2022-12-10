package fr.pandonia.core.data.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import fr.pandonia.api.data.mongo.IMongoConnection;

public class MongoConnection implements IMongoConnection {

    public static MongoConnection instance;
    private final MongoCredentials mongoCredentials;
    private MongoClient mongoClient;

    public MongoConnection(MongoCredentials mongoCredentials) {
        instance = this;
        this.mongoCredentials = mongoCredentials;
    }

    public void init() {
        String strUri = "mongodb://"+mongoCredentials.getUsername()+":"+mongoCredentials.getPassword()+"@"+mongoCredentials.getIP()+":"+mongoCredentials.getPort()+"/?authSource=admin";
        MongoClientURI uri = new MongoClientURI(strUri);
        mongoClient = new MongoClient(uri);
    }

    @Override
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public MongoDatabase getMongoDatabase(){
        return getMongoClient().getDatabase(mongoCredentials.getDatabase());
    }
}

