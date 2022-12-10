package fr.pandonia.core.data.mongo;

import fr.pandonia.api.data.mongo.IMongoCredentials;

public class MongoCredentials implements IMongoCredentials {

    private String ip;
    private int port;
    private String password;
    private String username;
    private String database;

    public MongoCredentials(String ip, int port, String password, String username, String database) {
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.username = username;
        this.database = database;
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getDatabase(){
        return database;
    }

}
