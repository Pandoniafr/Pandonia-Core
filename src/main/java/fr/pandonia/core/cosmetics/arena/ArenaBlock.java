package fr.pandonia.core.cosmetics.arena;

import fr.pandonia.api.cosmetics.arena.IArenaBlock;
import fr.pandonia.core.cosmetics.Cosmetic;
import org.bson.Document;

public class ArenaBlock extends Cosmetic implements IArenaBlock {

    private int blockID;
    private short blockData;

    public ArenaBlock(Cosmetic cosmetic, int blockID, int blockData) {
        super(cosmetic.getID(), cosmetic.getType(), cosmetic.getName(), cosmetic.getPrice(), cosmetic.getPowerRequired(), cosmetic.getHowToGetIt());
        this.blockID = blockID;
        this.blockData = (short) blockData;
    }

    @Override
    public int getBlockID() {
        return blockID;
    }

    @Override
    public short getBlockData() {
        return blockData;
    }

    public static ArenaBlock fromDocument(Document document){
        int blockData = 0;
        if(document.getInteger("blockData") != null){
            blockData = document.getInteger("blockData");
        }
        return new ArenaBlock(Cosmetic.fromDocument(document), document.getInteger("blockID"), blockData);
    }
}

