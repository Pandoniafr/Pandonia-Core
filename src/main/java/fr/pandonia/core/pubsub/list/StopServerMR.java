package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.api.server.ServerStatus;
import org.bson.Document;

public class StopServerMR extends MessageReceiver {

    public StopServerMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        if (getMessage().getString("server").equalsIgnoreCase(PandoniaAPI.get().getInstanceInfo().getName())){
            PandoniaAPI.get().getProxyLink().setServerStatus(ServerStatus.STOPPING);
            PandoniaAPI.get().getPlugin().getServer().shutdown();
        }
    }

}
