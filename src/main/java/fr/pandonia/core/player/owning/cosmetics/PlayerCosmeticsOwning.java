package fr.pandonia.core.player.owning.cosmetics;

import fr.pandonia.api.player.owning.cosmetics.IPlayerCosmeticsOwning;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PlayerCosmeticsOwning implements IPlayerCosmeticsOwning {

    private List<Integer> cosmetics;

    public PlayerCosmeticsOwning(List<Integer> cosmetics) {
        this.cosmetics = cosmetics;
    }

    public PlayerCosmeticsOwning(){
        this.cosmetics = new ArrayList<>();
    }

    @Override
    public List<Integer> getCosmetics() {
        return cosmetics;
    }

    @Override
    public void addCosmetic(int id){
        if (!cosmetics.contains(id)){
            cosmetics.add(id);
        }
    }

    @Override
    public void removeCosmetic(int id){
        cosmetics.remove(id);
    }

    @Override
    public boolean isOwningCosmetic(int id){
        return cosmetics.contains(id);
    }

    @Override
    public Document toDocument(){
        return new Document("cosmetics", cosmetics);
    }

    @Override
    public void update(Document document) {
        this.cosmetics = document.getList("cosmetics", Integer.class);
    }

    public static PlayerCosmeticsOwning fromDocument(Document document) {
        List<Integer> cosmetics = new ArrayList<>();
        if (document.getList("cosmetics", Integer.class) != null){
            cosmetics = document.getList("cosmetics", Integer.class);
        }
        return new PlayerCosmeticsOwning(cosmetics);
    }

    public static PlayerCosmeticsOwning getDefault(){
        return new PlayerCosmeticsOwning();
    }
}