package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.core.player.settings.PlayerSettings;
import org.bson.Document;

import java.util.UUID;

public class UpdatePlayerSettingsMR extends MessageReceiver {

    public UpdatePlayerSettingsMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {

        UUID uuid = UUID.fromString(getMessage().getString("playerUUID"));

        PlayerSettings playerSettings = (PlayerSettings) PandoniaAPI.get().getPlayerSettingsManager().getPlayerSettings(uuid);

        if (playerSettings != null){

            playerSettings.update(getMessage().get("settings", Document.class));

        }

    }

}
