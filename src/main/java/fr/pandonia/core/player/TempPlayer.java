package fr.pandonia.core.player;

import fr.pandonia.api.player.ITempPlayer;

import java.util.UUID;

public class TempPlayer implements ITempPlayer {

    private UUID uuid;
    private boolean loaded;

    public TempPlayer(UUID uuid) {
        this.uuid = uuid;

        this.loaded = false;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

}
