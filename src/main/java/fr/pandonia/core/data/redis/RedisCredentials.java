package fr.pandonia.core.data.redis;

import fr.pandonia.api.data.redis.IRedisCredentials;

public class RedisCredentials implements IRedisCredentials {

    private String ip;
    private int port;
    private String password;
    private String clientName;

    public RedisCredentials(String ip, int port, String password, String clientName) {
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.clientName = clientName;
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
    public String getClientName() {
        return clientName;
    }

}
