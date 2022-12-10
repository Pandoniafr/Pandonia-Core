package fr.pandonia.core.player.friends.manager;

import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.player.friends.IPlayerFriends;
import fr.pandonia.api.player.friends.manager.IFriendsManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendsManager implements IFriendsManager {

    private final PandoniaApplication instance;
    private List<IPlayerFriends> playerFriends;

    public FriendsManager(PandoniaApplication instance) {
        this.instance = instance;
        this.playerFriends = new ArrayList<>();
    }

    @Override
    public void addPlayerFriends(IPlayerFriends playerFriend){
        if(playerFriend != null){
            playerFriends.add(playerFriend);
            System.out.println("Friends " + playerFriend.getPlayerUUID());
        }else{
            System.out.println("Erreur Friends");
        }
    }

    @Override
    public void removePlayerFriends(UUID uuid){
        playerFriends.removeIf(playerFriends1 -> playerFriends1.getPlayerUUID().equals(uuid));
    }

    @Override
    public IPlayerFriends getPlayersFriends(UUID playerUUID){
        for (IPlayerFriends playerFriend : playerFriends) {
            if(playerFriend != null && playerFriend.getPlayerUUID().equals(playerUUID)){
                return playerFriend;
            }
        }
        return null;
    }

    @Override
    public void onQuit(Player p){
        playerFriends.removeIf(playerFriends1 -> playerFriends1.getPlayerUUID().equals(p.getUniqueId()));
    }
}
