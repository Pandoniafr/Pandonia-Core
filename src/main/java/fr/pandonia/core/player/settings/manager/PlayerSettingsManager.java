package fr.pandonia.core.player.settings.manager;

import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.player.settings.IPlayerSettings;
import fr.pandonia.api.player.settings.manager.IPlayerSettingsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerSettingsManager implements IPlayerSettingsManager {

    private PandoniaApplication instance;

    private List<IPlayerSettings> playerSettings;

    public PlayerSettingsManager(PandoniaApplication instance) {
        this.instance = instance;

        this.playerSettings = new ArrayList<>();
    }

    @Override
    public IPlayerSettings getPlayerSettings(UUID playerUUID){
        for (IPlayerSettings settings : playerSettings){
            if (settings.getPlayerUUID().equals(playerUUID)){
                return settings;
            }
        }
        return null;
    }

    @Override
    public void addPlayerSettings(IPlayerSettings settings){
        if (settings != null){
            if (playerSettings.stream().noneMatch(playerSettings1 -> playerSettings1.getPlayerUUID().equals(settings.getPlayerUUID()))){
                playerSettings.add(settings);
            }
        }
    }

    @Override
    public void removePlayerSettings(UUID playerUUID){
        playerSettings.removeIf(playerSettings1 -> playerSettings1.getPlayerUUID().equals(playerUUID));
    }

    @Override
    public List<IPlayerSettings> getPlayerSettingsList() {
        return playerSettings;
    }
}
