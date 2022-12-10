package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.core.PandoniaCore;
import org.bson.Document;

public class UpdateHostStatusMR extends MessageReceiver {

    public UpdateHostStatusMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        boolean status = getMessage().getBoolean("status");
        PandoniaCore.getCore().getServerManager().setHosts(status);
    }

}
