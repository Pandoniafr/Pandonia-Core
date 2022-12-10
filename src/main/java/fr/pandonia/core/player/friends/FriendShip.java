package fr.pandonia.core.player.friends;

import fr.pandonia.api.player.friends.IFriendShip;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class FriendShip implements IFriendShip {

    private UUID uuid1;
    private UUID uuid2;
    private Date date;

    public FriendShip(UUID uuid1, UUID uuid2, Date date) {
        this.uuid1 = uuid1;
        this.uuid2 = uuid2;
        this.date = date;
    }

    @Override
    public UUID getUuid1() {
        return uuid1;
    }

    @Override
    public UUID getUuid2() {
        return uuid2;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Document toDocument(){
        return new Document().append("uuid1", uuid1.toString()).append("uuid2", uuid2.toString()).append("date", date);
    }

    public static FriendShip fromDocument(Document document){
        return new FriendShip(UUID.fromString(document.getString("uuid1")), UUID.fromString(document.getString("uuid2")), document.getDate("date"));
    }
}
