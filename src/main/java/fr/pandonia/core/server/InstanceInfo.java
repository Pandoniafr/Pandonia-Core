package fr.pandonia.core.server;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.player.ISimplePlayer;
import fr.pandonia.api.server.*;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InstanceInfo implements IInstanceInfo {

    private PandoniaAPI instance;

    private String name;
    private IServer server;
    private boolean isOfficialLocal;
    private ServerSettings serverSettings;

    public InstanceInfo(PandoniaAPI instance){
        this.instance = instance;

        this.name = (String) ((DedicatedServer) MinecraftServer.getServer()).propertyManager.properties.get("server-name");
        this.server = instance.getServerManager().getServerFromDatabase(name);
        this.isOfficialLocal = false;
        if (!instance.getServerManager().isUpdating()){
            Bukkit.getScheduler().runTaskTimerAsynchronously(instance.getPlugin(), this::updateInstanceInfo, 0, 20);
        }
        File file = new File(Bukkit.getWorldContainer().getPath() + "/serverSettings.json");
        if(file.exists()){
            try {
                serverSettings = ServerSettings.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(file.getPath());
        }
    }

    @Override
    public String getName(){
        return this.server.getName();
    }

    @Override
    public String getCustomName() {
        return this.server.getCustomName();
    }

    @Override
    public ServerSettings getServerSettings() {
        return serverSettings;
    }

    @Override
    public String getType(){
        return this.server.getType();
    }

    @Override
    public int getSlots(){
        return this.server.getSlots();
    }

    @Override
    public String getIP() {
        return server.getIP();
    }

    @Override
    public int getPort(){
        return this.server.getPort();
    }

    @Override
    public ServerStatus getStatus(){
        return this.server.getServerStatus();
    }

    @Override
    public List<ISimplePlayer> getPlayers(){
        return this.server.getPlayers();
    }

    @Override
    public ISimplePlayer getHost(){
        return this.server.getHost();
    }

    @Override
    public boolean isOfficial(){
        return isOfficialLocal;
    }

    @Override
    public void setOfficialLocal(boolean officialLocal) {
        isOfficialLocal = officialLocal;
    }

    @Override
    public void updateInstanceInfo(){

        Server server = (Server) instance.getServerManager().getServerFromDatabase(this.name);

        if (server != null){
            this.server = server;
        }

    }

    @Override
    public IServer getServer() {
        return server;
    }
}
