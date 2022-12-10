package fr.pandonia.core.guild.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.guild.IGuild;
import fr.pandonia.api.guild.IGuildMember;
import fr.pandonia.api.guild.manager.IGuildManager;
import fr.pandonia.core.guild.Guild;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuildManager implements IGuildManager {

    private PandoniaApplication instance;

    private List<IGuild> guilds;

    public GuildManager(PandoniaApplication instance) {
        this.instance = instance;

        this.guilds = new ArrayList<>();
    }

    @Override
    public void startTask(){
        Bukkit.getScheduler().runTaskTimer(PandoniaAPI.get().getPlugin(), this::task, 0, 20);
    }

    public void task(){
        guilds.forEach(guild -> {
            guild.getInvitations().removeIf(invit -> invit.getWhen().getTime() + 5*60*1000 <= System.currentTimeMillis());
        });
    }

    @Override
    public List<IGuild> getGuilds() {
        return guilds;
    }

    @Override
    public IGuild getGuild(String name){
        for (IGuild guild : guilds) {
            if (guild.getName().equalsIgnoreCase(name)){
                return guild;
            }
        }
        return null;
    }

    @Override
    public IGuild getGuildByUUID(UUID guildUUID){
        for (IGuild guild : guilds) {
            if (guild.getUUID().equals(guildUUID)){
                return guild;
            }
        }
        return null;
    }

    @Override
    public IGuild getGuild(UUID playerUUID){
        for (IGuild guild : guilds) {
            for (IGuildMember member : guild.getMembers()){
                if (member.getUUID().equals(playerUUID)){
                    return guild;
                }
            }
        }
        return null;
    }

    public void cachePlayer(UUID playerUUID){
        if (getGuild(playerUUID) == null){
            Document guildDoc = instance.getMongoConnection().getMongoDatabase().getCollection("guilds").find(new Document("members", new Document("$elemMatch", new Document("uuid", playerUUID.toString())))).first();
            if (guildDoc != null){
                IGuild guild = Guild.fromDocument(guildDoc);
                if (!guilds.contains(guild)){
                    guilds.add(guild);
                }
            }
        }
    }

    @Override
    public boolean uncachePlayer(UUID playerUUID, List<UUID> onlinePlayers){
        onlinePlayers.remove(playerUUID);
        this.guilds.forEach(g -> {
            g.removeInvitation(playerUUID);
        });
        return this.guilds.removeIf(g -> g.getMembers().stream().noneMatch(guildMember -> onlinePlayers.contains(guildMember.getUUID())));
    }

    public void saveAndUncachePlayer(UUID playerUUID, List<UUID> onlinePlayers){
        IGuild guild = getGuild(playerUUID);
        if (guild != null && uncachePlayer(playerUUID, onlinePlayers)){
            instance.getMongoConnection().getMongoDatabase().getCollection("guilds").updateOne(new Document("uuid", guild.getUUID().toString()), new Document("$set", guild.toDocument()));
        }
    }

    @Override
    public void addGuild(IGuild guild){
        if (getGuildByUUID(guild.getUUID()) == null){
            guilds.add(guild);
        }
    }

    @Override
    public void removeGuild(String name){
        guilds.removeIf(guild -> guild.getName().equalsIgnoreCase(name));
    }

    @Override
    public void removeGuild(UUID uuid){
        guilds.removeIf(guild -> guild.getUUID().equals(uuid));
    }
}
