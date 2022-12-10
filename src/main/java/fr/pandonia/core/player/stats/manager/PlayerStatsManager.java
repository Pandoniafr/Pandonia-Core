package fr.pandonia.core.player.stats.manager;

import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.player.stats.IPlayerStats;
import fr.pandonia.api.player.stats.manager.IPlayerStatsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerStatsManager implements IPlayerStatsManager {

    private PandoniaApplication instance;

    private List<IPlayerStats> playerStatsList;

    public PlayerStatsManager(PandoniaApplication instance) {
        this.instance = instance;
        this.playerStatsList = new ArrayList<>();
    }

    @Override
    public IPlayerStats getPlayerStats(UUID playerUUID) {
        for (IPlayerStats playerStats : playerStatsList){
            if (playerStats.getPlayerUUID().equals(playerUUID)){
                return playerStats;
            }
        }
        return null;
    }

    @Override
    public void addPlayerStats(IPlayerStats stats) {
        playerStatsList.add(stats);
    }

    @Override
    public void removePlayerStats(UUID playerUUID) {
        playerStatsList.removeIf(playerStats -> playerStats.getPlayerUUID().equals(playerUUID));
    }

    @Override
    public List<IPlayerStats> getPlayerStatsList() {
        return playerStatsList;
    }

}
