package fr.pandonia.core.battlepass;

import fr.pandonia.api.battlepass.IBPItem;
import fr.pandonia.api.battlepass.ItemGiveEventHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BPItem implements IBPItem {

    private int level;
    private String name;
    private String[] description;
    private ItemStack item;
    private boolean free;
    private ItemGiveEventHandler event;

    public BPItem(int level, String name, ItemStack item, boolean free, ItemGiveEventHandler event) {
        this.level = level;
        this.name = name;
        this.item = item;
        this.free = free;
        this.event = event;
    }

    public BPItem(int level, String name, ItemStack item, boolean free, ItemGiveEventHandler event, String ... description) {
        this.level = level;
        this.name = name;
        this.description = description;
        this.item = item;
        this.free = free;
        this.event = event;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getItem(Player p) {
        return item;
    }

    @Override
    public boolean isFree() {
        return free;
    }

    @Override
    public ItemGiveEventHandler getEvent() {
        return event;
    }

}
