package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.player.friends.IFriend;
import fr.pandonia.api.player.friends.IPlayerFriends;
import fr.pandonia.api.player.settings.IPlayerSettings;
import fr.pandonia.api.pubsub.MessageReceiver;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class SendFriendNotificationMR extends MessageReceiver {

    public SendFriendNotificationMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        List<String> playersUUIDs = getMessage().getList("playerUUIDs", String.class);
        UUID from = UUID.fromString(getMessage().getString("from"));
        String type = getMessage().getString("type");
        for (String playersUUID : playersUUIDs) {
            Player p = Bukkit.getPlayer(UUID.fromString(playersUUID));
            if(p != null){
                IPlayerFriends pf = PandoniaAPI.get().getFriendsManager().getPlayersFriends(p.getUniqueId());
                IPlayerSettings ps = PandoniaAPI.get().getPlayerSettingsManager().getPlayerSettings(p.getUniqueId());
                if(pf != null){
                    IFriend friend = pf.getFriend(from);
                    if(friend != null){
                        if(ps != null && ps.isFriendsNotification()){
                            p.sendMessage("§8[§d❤§8] §3§l" + friend.getName() + "§r vient de se " + (type.equals("connect") ? "§aconnecter" : "§cdéconnecter") +".");
                        }
                        friend.setOnline(type.equals("connect"));
                    }
                }
            }
        }
    }
}
