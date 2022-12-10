package fr.pandonia.core.battlepass;

import fr.pandonia.api.battlepass.IBPChallenge;
import org.bukkit.inventory.ItemStack;

public class BPChallenge implements IBPChallenge {

    private int id;
    private String name;
    private String mode;
    private int gain;
    private ItemStack item;

    public BPChallenge(int id, String name, String mode, int gain, ItemStack item) {
        this.id = id;
        this.name = name;
        this.mode = mode;
        this.gain = gain;
        this.item = item;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public int getGain() {
        return gain;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }
}
