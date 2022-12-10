package fr.pandonia.core.player.friends;

import fr.pandonia.api.player.friends.IFriendRequest;

import java.util.UUID;

public class FriendRequest implements IFriendRequest {

    private final UUID from;
    private final long time;

    public FriendRequest(UUID from, long time) {
        this.from = from;
        this.time = time;
    }

    @Override
    public UUID getFrom() {
        return from;
    }

    @Override
    public long getTime() {
        return time;
    }
}

