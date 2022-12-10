package fr.pandonia.core.cosmetics;

import fr.pandonia.api.cosmetics.CosmeticType;
import fr.pandonia.api.cosmetics.ICosmetic;
import org.bson.Document;

public class Cosmetic implements ICosmetic {

    private int id;
    private CosmeticType type;
    private String name;
    private int price; //(-1 si non achetable)
    private int powerRequired;
    private String howToGetIt;

    public Cosmetic(int id, CosmeticType type, String name, int price, int powerRequired, String howToGetIt) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
        this.powerRequired = powerRequired;
        this.howToGetIt = howToGetIt;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public CosmeticType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getPowerRequired() {
        return powerRequired;
    }

    @Override
    public String getHowToGetIt() {
        return howToGetIt;
    }

    public static Cosmetic fromDocument(Document document){
        return new Cosmetic(document.getInteger("id"), CosmeticType.valueOf(document.getString("type")), document.getString("name"), document.getInteger("price"), document.getInteger("powerRequired"), document.getString("howToGetIt"));
    }

}
