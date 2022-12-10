package fr.pandonia.core.player.nick;

import fr.pandonia.api.player.nick.ISkinNickInfo;
import org.bson.Document;

import java.util.UUID;

public class SkinNickInfo implements ISkinNickInfo {

    private UUID playerUUID;
    private String value;
    private String signature;
    private boolean reset;

    public SkinNickInfo(UUID playerUUID, String value, String signature) {
        this.playerUUID = playerUUID;
        this.value = value;
        this.signature = signature;
        this.reset = false;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public void setReset(boolean reset) {
        this.reset = reset;
    }

    @Override
    public boolean isReset() {
        return reset;
    }

    public static SkinNickInfo fromDocument(Document document){
        return new SkinNickInfo(UUID.fromString(document.getString("playerUUID")), document.getString("value"), document.getString("signature"));
    }

    @Override
    public Document toDocument(){
        return new Document("playerUUID", playerUUID.toString()).append("value", value).append("signature", signature);
    }
}