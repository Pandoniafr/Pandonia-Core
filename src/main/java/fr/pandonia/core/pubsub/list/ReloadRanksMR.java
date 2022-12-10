package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.pubsub.MessageReceiver;
import org.bson.Document;

public class ReloadRanksMR extends MessageReceiver {

    public ReloadRanksMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        PandoniaAPI.get().getRankManager().register();
    }

}

