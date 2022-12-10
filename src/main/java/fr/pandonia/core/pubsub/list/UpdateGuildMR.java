package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.guild.Guild;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.Collectors;

public class UpdateGuildMR extends MessageReceiver {

    public UpdateGuildMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        Document guildDoc = getMessage().get("guild", Document.class);
        if (guildDoc != null){
            Guild guild = (Guild) PandoniaCore.getCore().getGuildManager().getGuildByUUID(UUID.fromString(guildDoc.getString("uuid")));
            if (guild != null){
                guild.update(guildDoc);
            }else{
                guild = Guild.fromDocument(guildDoc);
                if (guild.containsMemberInList(PandoniaAPI.get().getPlugin().getServer().getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList()))){
                    PandoniaAPI.get().getGuildManager().addGuild(guild);
                }
            }
        }
    }

}
