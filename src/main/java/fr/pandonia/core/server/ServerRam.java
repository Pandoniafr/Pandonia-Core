package fr.pandonia.core.server;

import fr.pandonia.api.server.IServerRam;
import org.bson.Document;

public class ServerRam implements IServerRam {

    private int allocatedRam;
    private int maxRam;

    public ServerRam(int allocatedRam, int maxRam) {
        this.allocatedRam = allocatedRam;
        this.maxRam = maxRam;
    }

    @Override
    public int getAllocatedRam() {
        return allocatedRam;
    }

    @Override
    public int getMaxRam() {
        return maxRam;
    }

    @Override
    public Document toDocument(){
        return new Document("allocatedRam", allocatedRam).append("maxRam", maxRam);
    }

    public static ServerRam fromDocument(Document document){
        return new ServerRam(document.getInteger("allocatedRam"), document.getInteger("maxRam"));
    }

}
