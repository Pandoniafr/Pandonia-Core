package fr.pandonia.core.sanction.infos;

import fr.pandonia.api.sanction.infos.ISanctionCategory;
import org.bson.Document;
import org.bukkit.Material;

public class SanctionCategory implements ISanctionCategory {

    private String name;
    private Material material;
    private byte glassColor;
    private int invSlot;

    public SanctionCategory(String name, Material material, byte glassColor, int invSlot) {
        this.name = name;
        this.material = material;
        this.glassColor = glassColor;
        this.invSlot = invSlot;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public byte getGlassColor() {
        return glassColor;
    }

    @Override
    public int getInvSlot() {
        return invSlot;
    }

    public static SanctionCategory fromDocument(Document document){
        return new SanctionCategory(document.getString("name"), Material.valueOf(document.getString("material")), document.getInteger("glassColor").byteValue(), document.getInteger("invSlot"));
    }

}
