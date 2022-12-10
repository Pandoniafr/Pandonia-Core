package fr.pandonia.core.player.stats;

import fr.pandonia.api.player.stats.IPlayerStats;
import fr.pandonia.api.player.stats.uhc.IPlayerUHCStats;
import fr.pandonia.core.player.stats.uhc.PlayerUHCStats;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerStats implements IPlayerStats {

    private UUID playerUUID;

    private List<IPlayerUHCStats> stats;

    public PlayerStats(UUID playerUUID, List<IPlayerUHCStats> stats) {
        this.playerUUID = playerUUID;
        this.stats = stats;
    }

    @Override
    public IPlayerUHCStats getStats(String mode){
        for (IPlayerUHCStats stat : stats) {
            if(stat.getMode().equalsIgnoreCase(mode)){
                return stat;
            }
        }
        IPlayerUHCStats stat = PlayerUHCStats.getDefault(mode);
        stats.add(stat);
        return stat;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public Document toDocument() {
        List<Document> stats = new ArrayList<>();
        this.stats.forEach(iPlayerUHCStats -> stats.add(iPlayerUHCStats.toDocument()));
        return new Document("playerUUID", playerUUID.toString()).append("stats", stats);
    }

    @Override
    public void update(Document document) {
        this.stats = fill(document.getList("stats", Document.class));
    }

    public static PlayerStats getDefault(UUID playerUUID){
        return new PlayerStats(playerUUID, new ArrayList<>());
    }

    public static List<IPlayerUHCStats> fill(List<Document> documents){
        List<IPlayerUHCStats> list = new ArrayList<>();
        documents.forEach(document -> list.add(PlayerUHCStats.fromDocument(document)));
        return list;
    }

    public static PlayerStats fromDocument(Document document){
        return new PlayerStats(UUID.fromString(document.getString("playerUUID")), fill(document.getList("stats", Document.class)));
    }

}