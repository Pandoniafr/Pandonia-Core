package fr.pandonia.core.player.friends;

import fr.pandonia.api.player.friends.IFriend;
import fr.pandonia.api.player.friends.IFriendRequest;
import fr.pandonia.api.player.friends.IPlayerFriends;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlayerFriends implements IPlayerFriends {

    private final UUID playerUUID;
    private final List<IFriend> friends;
    private final List<IFriendRequest> request;
    private final List<UUID> asks;

    public PlayerFriends(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.friends = new ArrayList<>();
        this.request = new ArrayList<>();
        this.asks = new ArrayList<>();
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public List<IFriend> getFriends() {
        return friends;
    }

    @Override
    public boolean isFriendWith(UUID uuid){
        return friends.stream().anyMatch(iFriend -> iFriend.getUuid().equals(uuid));
    }

    @Override
    public IFriend getFriend(UUID uuid){
        for (IFriend friend : friends) {
            if(friend.getUuid().equals(uuid)){
                return friend;
            }
        }
        return null;
    }

    @Override
    public IFriendRequest getFriendRequest(UUID from){
        for (IFriendRequest iFriendRequest : request) {
            if(iFriendRequest.getFrom().equals(from)){
                return iFriendRequest;
            }
        }
        return null;
    }

    @Override
    public void sendFriendRequest(UUID from){
        request.add(new FriendRequest(from, System.currentTimeMillis()));
    }

    @Override
    public void sendFriendAccept(UUID from, String name){
        friends.add(new Friend(from, name, new Date(System.currentTimeMillis()), true));
    }

    public List<Document> getFriendList(){
        List<Document> list = new ArrayList<>();
        friends.forEach(iFriend -> list.add(iFriend.toDocument()));
        return list;
    }

    @Override
    public Document toDocument(){
        return new Document("playerUUID", playerUUID.toString()).append("friends", getFriendList());
    }

    public static PlayerFriends fromDocument(Document document){
        PlayerFriends playerFriends = new PlayerFriends(UUID.fromString(document.getString("playerUUID")));
        document.getList("friends", Document.class).forEach(document1 -> playerFriends.getFriends().add(Friend.fromDocument(document1)));
        return playerFriends;
    }

    @Override
    public List<UUID> getAsks() {
        return asks;
    }
}
