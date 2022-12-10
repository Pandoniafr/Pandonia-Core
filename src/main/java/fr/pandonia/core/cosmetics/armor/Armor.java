package fr.pandonia.core.cosmetics.armor;

import fr.pandonia.api.cosmetics.armor.IArmor;
import fr.pandonia.core.cosmetics.Cosmetic;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.playerequiparmor.ArmorType;
import org.bson.Document;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Armor extends Cosmetic implements IArmor{

    private String armorType;
    private Color color;

    public Armor(Cosmetic cosmetic, String armorType, Color color) {
        super(cosmetic.getID(), cosmetic.getType(), cosmetic.getName(), cosmetic.getPrice(), cosmetic.getPowerRequired(), cosmetic.getHowToGetIt());
        this.armorType = armorType;
        this.color = color;
    }

    @Override
    public String getName(ArmorType armorType) {
        if(getName().contains("-")){
            return getName().split("-")[0] + armorType.getName() + " " + getName().split("-")[1];
        }else{
            return "§3" + armorType.getName() + " " + getName();
        }
    }

    @Override
    public ItemStack getPiece(ArmorType armorType){
        Material m = Material.valueOf(this.armorType.toUpperCase() + "_" + armorType.name());
        ItemBuilder it = new ItemBuilder(m);
        if(m.toString().contains("LEATHER") && color != null){
            it.setLeatherArmorColor(color);
        }
        return it.toItemStack();
    }

    public static Armor fromDocument(Document document){
        Document colorDoc = document.get("color", Document.class);
        Color color = null;
        if (colorDoc != null && !colorDoc.isEmpty()){
            color = Color.fromRGB(colorDoc.getInteger("r"), colorDoc.getInteger("g"), colorDoc.getInteger("b"));
        }
        return new Armor(Cosmetic.fromDocument(document), document.getString("armorType"), color);
    }
}