package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.player.friends.IPlayerFriends;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.core.PandoniaCore;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.UUID;

public class SendFriendAcceptMR extends MessageReceiver {

    public SendFriendAcceptMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        UUID from = UUID.fromString(getMessage().getString("from"));
        UUID to = UUID.fromString(getMessage().getString("to"));
        String nameFrom = getMessage().getString("nameFrom");
        String nameTo = getMessage().getString("nameTo");
        IPlayerFriends pfTo = PandoniaCore.getCore().getFriendsManager().getPlayersFriends(to);
        IPlayerFriends pfFrom = PandoniaCore.getCore().getFriendsManager().getPlayersFriends(from);
        if(pfTo != null){
            pfTo.sendFriendAccept(from, nameFrom);
        }
        if(pfFrom != null && Bukkit.getPlayer(to) != null){
            pfFrom.sendFriendAccept(to, nameTo);
        }
    }

}