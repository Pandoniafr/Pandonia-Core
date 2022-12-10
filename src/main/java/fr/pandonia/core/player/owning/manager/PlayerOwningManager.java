package fr.pandonia.core.player.owning.manager;

import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.player.owning.IPlayerOwning;
import fr.pandonia.api.player.owning.manager.IPlayerOwningManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerOwningManager implements IPlayerOwningManager {

    private PandoniaApplication instance;

    private List<IPlayerOwning> playerOwningList;

    public PlayerOwningManager(PandoniaApplication instance) {
        this.instance = instance;

        this.playerOwningList = new ArrayList<>();
    }

    @Override
    public IPlayerOwning getPlayerOwning(UUID playerUUID){
        for (IPlayerOwning owning : playerOwningList){
            if (owning.getPlayerUUID().equals(playerUUID)){
                return owning;
            }
        }
        return null;
    }

    @Override
    public void addPlayerOwning(IPlayerOwning settings){
        if (settings != null){
            if (playerOwningList.stream().noneMatch(playerSettings1 -> playerSettings1.getPlayerUUID().equals(settings.getPlayerUUID()))){
                playerOwningList.add(settings);
            }
        }
    }

    @Override
    public void removePlayerOwning(UUID playerUUID){
        playerOwningList.removeIf(playerSettings1 -> playerSettings1.getPlayerUUID().equals(playerUUID));
    }

    @Override
    public List<IPlayerOwning> getPlayerOwningList() {
        return playerOwningList;
    }

}
