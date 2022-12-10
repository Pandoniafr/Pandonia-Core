package fr.pandonia.core.cosmetics.gadget;

import fr.pandonia.api.cosmetics.ICosmetic;
import fr.pandonia.api.cosmetics.gadget.IGadget;
import fr.pandonia.core.cosmetics.Cosmetic;
import fr.pandonia.tools.ItemBuilder;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Gadget extends Cosmetic implements IGadget {

    private ItemStack item;

    public Gadget(ICosmetic cosmetic, ItemStack item) {
        super(cosmetic.getID(), cosmetic.getType(), cosmetic.getName(), cosmetic.getPrice(), cosmetic.getPowerRequired(), cosmetic.getHowToGetIt());
        this.item = item;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    public static Gadget fromDocument(Document document){
        return new Gadget(Cosmetic.fromDocument(document), new ItemBuilder(Material.valueOf(document.getString("material"))).setName(document.getString("itemName")).toItemStack());
    }
}

