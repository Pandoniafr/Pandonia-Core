package fr.pandonia.core.player.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.ISimplePlayer;
import fr.pandonia.api.player.battlepass.IPlayerBattlePass;
import fr.pandonia.api.player.manager.IPlayerManager;
import fr.pandonia.api.player.owning.IPlayerOwning;
import fr.pandonia.api.player.settings.IPlayerSettings;
import fr.pandonia.api.player.stats.IPlayerStats;
import fr.pandonia.core.player.SimplePlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager implements IPlayerManager {

    private PandoniaAPI instance;

    private List<IPPlayer> players;
    private List<UUID> playersToAdd;
    private List<UUID> playersToRemove;

    public PlayerManager(PandoniaAPI instance){
        this.instance = instance;

        this.players = new ArrayList<>();
        this.playersToAdd = new ArrayList<>();
        this.playersToRemove = new ArrayList<>();
        instance.getPlugin().getServer().getScheduler().runTaskTimer(instance.getPlugin(), this::updateCooldowns, 0, 1);
        instance.getPlugin().getServer().getScheduler().runTaskTimer(instance.getPlugin(), () -> {
            if(!playersToAdd.isEmpty()){
                instance.getProxyLink().addPlayersToServer(playersToAdd);

                playersToAdd.clear();
            }
            if(!playersToRemove.isEmpty()){
                instance.getProxyLink().removePlayersToServer(playersToRemove);
                playersToRemove.clear();
            }
        }, 0, 10);
    }

    public void updateCooldowns(){
        for (IPPlayer pp : players){
            ((fr.pandonia.core.player.PPlayer) pp).removeCooldown();
        }
    }

    @Override
    public IPPlayer getPlayer(UUID uuid) {
        for (IPPlayer player : players){
            if (player.getUUID().equals(uuid)){
                return player;
            }
        }
        return null;
    }

    @Override
    public IPPlayer getPlayer(String name) {
        for (IPPlayer player : players){
            if (player.getName().equalsIgnoreCase(name)){
                return player;
            }
        }
        return null;
    }

    @Override
    public void savePlayer(UUID uuid){
        IPPlayer player = getPlayer(uuid);

        if(player != null){
            instance.getProxyLink().updateAllPlayer(uuid);
        }
    }

    @Override
    public ISimplePlayer SimplePlayerFromDocument(Document document){
        return SimplePlayer.fromDocument(document);
    }

    @Override
    public void removeAllPlayer(UUID uuid){
        IPPlayer player = getPlayer(uuid);

        if(player != null){

            IPlayerOwning playerOwning = instance.getPlayerOwningManager().getPlayerOwning(uuid);
            if (playerOwning != null){
                instance.getPlayerOwningManager().removePlayerOwning(uuid);
            }

            IPlayerSettings playerSettings = instance.getPlayerSettingsManager().getPlayerSettings(uuid);
            if (playerSettings != null){
                instance.getPlayerSettingsManager().removePlayerSettings(uuid);
            }

            IPlayerStats playerStats = instance.getPlayerStatsManager().getPlayerStats(uuid);
            if (playerStats != null){
                instance.getPlayerStatsManager().removePlayerStats(uuid);
            }

            IPlayerBattlePass playerBattlePass = instance.getPlayerBattlePassManager().getPlayerBattlePass(uuid);
            if (playerBattlePass != null){
                instance.getPlayerBattlePassManager().removePlayerBattlePass(uuid);
            }

            instance.getGuildManager().uncachePlayer(uuid, Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList()));

            removePlayer(uuid);
        }
    }

    @Override
    public void addPlayer(IPPlayer player) {
        if (player != null){
            if (players.stream().noneMatch(player1 -> player1.getUUID().equals(player.getUUID()))){
                players.add(player);
                playersToAdd.add(player.getUUID());
            }
        }
    }

    @Override
    public void removePlayer(UUID player) {
        players.removeIf(hPlayer -> hPlayer.getUUID().equals(player));
        playersToRemove.add(player);
    }

    @Override
    public List<IPPlayer> getPlayers() {
        return this.players;
    }


}
