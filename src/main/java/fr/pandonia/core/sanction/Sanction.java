package fr.pandonia.core.sanction;

import fr.pandonia.api.sanction.ISanction;
import fr.pandonia.api.sanction.SanctionType;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class Sanction implements ISanction {

    private UUID playerUUID;
    private SanctionType type;
    private Date when;
    private long duration;
    private String by;
    private String reason;
    private boolean removed;

    public Sanction(UUID playerUUID, SanctionType type, Date when, long duration, String by, String reason, boolean removed) {
        this.playerUUID = playerUUID;
        this.type = type;
        this.when = when;
        this.duration = duration;
        this.by = by;
        this.reason = reason;
        this.removed = removed;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public SanctionType getType() {
        return type;
    }

    @Override
    public Date getWhen() {
        return when;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String getBy() {
        return by;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Document toDocument(){
        return new Document("player", playerUUID.toString()).append("type", type.toString()).append("when", when).append("duration", duration).append("by", by).append("reason", reason).append("removed", removed);
    }

    public static Sanction fromDocument(Document document){
        return new Sanction(UUID.fromString(document.getString("player")), SanctionType.valueOf(document.getString("type")), document.getDate("when"), document.getLong("duration"), document.getString("by"), document.getString("reason"), document.getBoolean("removed"));
    }

}
