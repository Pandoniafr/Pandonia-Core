package fr.pandonia.core.player.owning;

import fr.pandonia.api.player.owning.IPlayerOwning;
import fr.pandonia.api.player.owning.cosmetics.IPlayerCosmeticsOwning;
import fr.pandonia.core.player.owning.cosmetics.PlayerCosmeticsOwning;
import org.bson.Document;

import java.util.UUID;

public class PlayerOwning implements IPlayerOwning {

    private UUID playerUUID;
    private IPlayerCosmeticsOwning playerCosmeticsOwning;

    public PlayerOwning(UUID playerUUID, IPlayerCosmeticsOwning playerCosmeticsOwning) {
        this.playerUUID = playerUUID;
        this.playerCosmeticsOwning = playerCosmeticsOwning;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public IPlayerCosmeticsOwning getPlayerCosmeticsOwning() {
        return playerCosmeticsOwning;
    }

    @Override
    public void setPlayerCosmeticsOwning(IPlayerCosmeticsOwning playerCosmeticsOwning) {
        this.playerCosmeticsOwning = playerCosmeticsOwning;
    }

    @Override
    public Document toDocument() {
        return new Document("playerUUID", playerUUID.toString()).append("cosmetics", playerCosmeticsOwning.toDocument());
    }

    @Override
    public void update(Document document){
        playerCosmeticsOwning.update(document.get("cosmetics", Document.class));
    }

    public static PlayerOwning fromDocument(Document document) {
        IPlayerCosmeticsOwning cosmetics = new PlayerCosmeticsOwning();
        if (document.get("cosmetics", Document.class) != null){
            cosmetics = PlayerCosmeticsOwning.fromDocument(document.get("cosmetics", Document.class));
        }
        return new PlayerOwning(UUID.fromString(document.getString("playerUUID")), cosmetics);
    }

    public static PlayerOwning getDefault(UUID uuid){
        return new PlayerOwning(uuid, PlayerCosmeticsOwning.getDefault());
    }

}