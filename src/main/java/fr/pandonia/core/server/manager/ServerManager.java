package fr.pandonia.core.server.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.player.ISimplePlayer;
import fr.pandonia.api.server.IServer;
import fr.pandonia.api.server.manager.IServerManager;
import fr.pandonia.core.PandoniaCore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServerManager implements IServerManager {

    private PandoniaAPI instance;

    private List<IServer> servers;

    private boolean updating;

    private boolean hosts;

    public ServerManager(PandoniaAPI instance) {
        this.instance = instance;

        this.servers = new ArrayList<>();
        this.updating = false;
        this.hosts = true;
    }

    @Override
    public IServer getServer(String serverName){
        for (IServer server : servers){
            if (server.getName().equalsIgnoreCase(serverName)){
                return server;
            }
        }
        return null;
    }

    @Override
    public boolean exists(String name){
        return servers.stream().anyMatch(server -> server.getName().equalsIgnoreCase(name));
    }

    @Override
    public void addServer(IServer server){
        this.servers.add(server);
    }

    @Override
    public void removeServer(IServer server){
        this.servers.remove(server);
    }

    @Override
    public void removeServer(String serverName){
        this.servers.removeIf(server -> server.getName().equalsIgnoreCase(serverName));
    }

    @Override
    public IServer getServerFromDatabase(String name){
        return PandoniaCore.getCore().getServerDataManager().getServer(name);
    }

    @Override
    public List<IServer> getServerListByType(String serverTypeName){
        return servers.stream().filter(server -> server.getType().equalsIgnoreCase(serverTypeName)).collect(Collectors.toList());
    }

    @Override
    public int getNumberOfPlayersByType(String serverTypeName){
        int players = 0;
        for (IServer server : servers){
            if (server.getType().equalsIgnoreCase(serverTypeName)){
                players += server.getPlayers().size();
            }
        }
        return players;
    }

    @Override
    public void updateServers(){
        this.servers = new ArrayList<>(PandoniaCore.getCore().getServerDataManager().getAll());
    }

    @Override
    public void startUpdateTask(){
        instance.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(instance.getPlugin(), this::updateServers, 0, 20);
        this.updating = true;
    }

    @Override
    public boolean isUpdating() {
        return updating;
    }

    @Override
    public List<IServer> getServers() {
        return servers;
    }

    @Override
    public List<ISimplePlayer> getAllPlayers(){
        List<ISimplePlayer> players = new ArrayList<>();
        for (IServer s : servers){
            players.addAll(s.getPlayers());
        }
        return players;
    }

    @Override
    public IServer getPlayerServer(UUID uuid){
        for (IServer s : servers){
            for (ISimplePlayer player : s.getPlayers()) {
                if(player.getUUID().equals(uuid)){
                    return s;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isHosts() {
        return hosts;
    }

    @Override
    public void setHosts(boolean hosts) {
        this.hosts = hosts;
    }
}
