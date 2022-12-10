package fr.pandonia.core.cosmetics.killanimation;

import fr.pandonia.api.cosmetics.ICosmetic;
import fr.pandonia.api.cosmetics.killanimation.IKillAnimation;
import fr.pandonia.core.cosmetics.Cosmetic;
import fr.pandonia.tools.ItemBuilder;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class KillAnimation extends Cosmetic implements IKillAnimation {

    private String methodName;
    private ItemStack item;

    public KillAnimation(ICosmetic cosmetic, String methodName, ItemStack item) {
        super(cosmetic.getID(), cosmetic.getType(), cosmetic.getName(), cosmetic.getPrice(), cosmetic.getPowerRequired(), cosmetic.getHowToGetIt());
        this.methodName = methodName;
        this.item = item;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    public static KillAnimation fromDocument(Document document){
        Material material = Material.valueOf(document.getString("material"));
        ItemBuilder ib = new ItemBuilder(material);
        if (material.equals(Material.SKULL_ITEM)){
            String headTexture = document.getString("headTexture");
            ib = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3);
            ib.getSkull(headTexture);
        }
        return new KillAnimation(Cosmetic.fromDocument(document), document.getString("methodName"), ib.toItemStack());
    }

}