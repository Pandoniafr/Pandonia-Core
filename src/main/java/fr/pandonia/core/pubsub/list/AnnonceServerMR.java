package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.core.server.Server;
import fr.pandonia.tools.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.bukkit.Bukkit;

public class AnnonceServerMR extends MessageReceiver {

    public AnnonceServerMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        String serverName = getMessage().getString("serverName");
        Server server = Server.fromDocument((Document) getMessage().get("server"));
        if(PandoniaAPI.get().getInstanceInfo().getName().startsWith("hub-")){
            TextComponent annonce = new TextComponent(server.getHost().getPrefix(PandoniaAPI.get().getRankManager()) + server.getHost().getName() + " §7lance son serveur §8[§7» §e§lRejoindre§8]");
            annonce.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Utils.getServerComponent(server)));
            annonce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + serverName));
            Bukkit.spigot().broadcast(annonce);
        }
    }
}
