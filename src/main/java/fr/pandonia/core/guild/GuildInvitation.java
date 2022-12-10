package fr.pandonia.core.guild;

import fr.pandonia.api.guild.IGuildInvitation;
import fr.pandonia.api.player.ISimplePlayer;
import fr.pandonia.core.player.SimplePlayer;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class GuildInvitation implements IGuildInvitation {

    private UUID guild;
    private ISimplePlayer sender;
    private ISimplePlayer receiver;
    private Date when;

    public GuildInvitation(UUID guild, ISimplePlayer sender, ISimplePlayer receiver, Date when) {
        this.guild = guild;
        this.sender = sender;
        this.receiver = receiver;
        this.when = when;
    }

    @Override
    public UUID getGuild() {
        return guild;
    }

    @Override
    public ISimplePlayer getSender() {
        return sender;
    }

    @Override
    public ISimplePlayer getReceiver() {
        return receiver;
    }

    @Override
    public Date getWhen() {
        return when;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildInvitation that = (GuildInvitation) o;

        if (guild != null ? !guild.equals(that.guild) : that.guild != null) return false;
        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null) return false;
        return when != null ? when.equals(that.when) : that.when == null;
    }

    @Override
    public int hashCode() {
        int result = guild != null ? guild.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (when != null ? when.hashCode() : 0);
        return result;
    }

    @Override
    public Document toDocument() {
        return new Document("guild", guild.toString()).append("sender", sender.toSimpleDocument()).append("receiver", receiver.toSimpleDocument()).append("when", when);
    }

    public static GuildInvitation fromDocument(Document document){
        return new GuildInvitation(UUID.fromString(document.getString("guild")), SimplePlayer.fromDocument(document.get("sender", Document.class)), SimplePlayer.fromDocument(document.get("sender", Document.class)), document.getDate("when"));
    }
}
