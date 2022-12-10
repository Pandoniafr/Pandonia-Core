package fr.pandonia.core.cosmetics.pet;

import fr.pandonia.api.cosmetics.pet.IPet;
import fr.pandonia.core.cosmetics.Cosmetic;
import org.bson.Document;
import org.bukkit.Color;
import org.bukkit.Material;

public class Pet extends Cosmetic implements IPet {

    private String category;
    private int invSlot; //-1 si non affichable
    private String headTexture; //Texture (minecraft heads)
    private Material chestplate; //Material.AIR si pas de chestplate
    private Color chestplateColor;


    public Pet(Cosmetic cosmetic, String category, String headTexture, Material chestplate, Color chestplateColor) {
        super(cosmetic.getID(), cosmetic.getType(), cosmetic.getName(), cosmetic.getPrice(), cosmetic.getPowerRequired(), cosmetic.getHowToGetIt());
        this.category = category;
        this.headTexture = headTexture;
        this.chestplate = chestplate;
        this.chestplateColor = chestplateColor;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getHeadTexture() {
        return headTexture;
    }

    @Override
    public Material getChestplate() {
        return chestplate;
    }

    @Override
    public Color getChestplateColor() {
        return chestplateColor;
    }

    public static Pet fromDocument(Document document){
        Cosmetic cosmetic = Cosmetic.fromDocument(document);
        Document colorDoc = document.get("color", Document.class);
        Color color = null;
        if (!colorDoc.isEmpty()){
            color = Color.fromRGB(colorDoc.getInteger("r"), colorDoc.getInteger("g"), colorDoc.getInteger("b"));
        }
        return new Pet(cosmetic, document.getString("category"), document.getString("headTexture"), Material.getMaterial(document.getString("chestplate")), color);
    }

}
