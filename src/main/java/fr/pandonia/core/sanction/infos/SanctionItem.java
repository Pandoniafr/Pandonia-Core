package fr.pandonia.core.sanction.infos;

import fr.pandonia.api.sanction.infos.ISanctionItem;
import org.bson.Document;
import org.bukkit.Material;

import java.util.List;

public class SanctionItem implements ISanctionItem {

    private int id;
    private String category;
    private String name;
    private Material material;
    private int invSlot;
    private boolean warn;
    private List<String> levels;

    public SanctionItem(int id, String category, String name, Material material, int invSlot, boolean warn, List<String> levels) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.material = material;
        this.invSlot = invSlot;
        this.warn = warn;
        this.levels = levels;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getCategory() {
        return category;
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
    public int getInvSlot() {
        return invSlot;
    }

    @Override
    public boolean isWarn() {
        return warn;
    }

    @Override
    public List<String> getLevels() {
        return levels;
    }

    public static SanctionItem fromDocument(Document document){
        return new SanctionItem(document.getInteger("id"), document.getString("category"), document.getString("name"), Material.valueOf(document.getString("material")), document.getInteger("invSlot"), document.getBoolean("warn"), document.getList("levels", String.class));
    }

}
