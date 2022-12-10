package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.pubsub.MessageReceiver;
import org.bson.Document;

import java.util.UUID;

public class AskToSaveMR extends MessageReceiver {

    public AskToSaveMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        UUID playerUUID = UUID.fromString(getMessage().getString("playerUUID"));

        IPPlayer player = PandoniaAPI.get().getPlayerManager().getPlayer(playerUUID);

        if (player != null){
            PandoniaMessages.debug("Demande de save reçue pour " + player.getName() + " " + PandoniaMessages.getActualDate());
            PandoniaAPI.get().getPlayerManager().savePlayer(playerUUID);
            player.setSaved(true);
        }
    }

}
