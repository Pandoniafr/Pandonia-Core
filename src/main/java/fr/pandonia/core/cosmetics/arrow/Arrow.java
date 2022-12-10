package fr.pandonia.core.cosmetics.arrow;

import fr.pandonia.api.cosmetics.ICosmetic;
import fr.pandonia.api.cosmetics.arrow.IArrow;
import fr.pandonia.core.cosmetics.Cosmetic;
import org.bson.Document;

public class Arrow extends Cosmetic implements IArrow {

    private String particle;
    private int matID;

    public Arrow(ICosmetic cosmetic, String particle, int matID) {
        super(cosmetic.getID(), cosmetic.getType(), cosmetic.getName(), cosmetic.getPrice(), cosmetic.getPowerRequired(), cosmetic.getHowToGetIt());
        this.particle = particle;
        this.matID = matID;
    }

    @Override
    public String getParticle() {
        return particle;
    }

    @Override
    public int getMatID() {
        return matID;
    }

    public static Arrow fromDocument(Document document){
        return new Arrow(Cosmetic.fromDocument(document), document.getString("particle"), document.getInteger("matID"));
    }
}

