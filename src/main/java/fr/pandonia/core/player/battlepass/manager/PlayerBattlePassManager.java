package fr.pandonia.core.player.battlepass.manager;

import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.player.battlepass.IPlayerBattlePass;
import fr.pandonia.api.player.battlepass.manager.IPlayerBattlePassManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerBattlePassManager implements IPlayerBattlePassManager {

    private PandoniaApplication instance;

    private List<IPlayerBattlePass> playerBattlePassList;

    public PlayerBattlePassManager(PandoniaApplication instance) {
        this.instance = instance;

        this.playerBattlePassList = new ArrayList<>();
    }

    @Override
    public IPlayerBattlePass getPlayerBattlePass(UUID playerUUID){
        for (IPlayerBattlePass battlePass : playerBattlePassList){
            if (battlePass.getPlayerUUID().equals(playerUUID)){
                return battlePass;
            }
        }
        return null;
    }

    @Override
    public void addPlayerBattlePass(IPlayerBattlePass battlePass){
        if (battlePass != null){
            if (playerBattlePassList.stream().noneMatch(playerBattlePass -> playerBattlePass.getPlayerUUID().equals(battlePass.getPlayerUUID()))){
                playerBattlePassList.add(battlePass);
            }
        }
    }

    @Override
    public void removePlayerBattlePass(UUID playerUUID){
        playerBattlePassList.removeIf(playerBattlePass -> playerBattlePass.getPlayerUUID().equals(playerUUID));
    }

    @Override
    public List<IPlayerBattlePass> getPlayerBattlePassList() {
        return playerBattlePassList;
    }

}
