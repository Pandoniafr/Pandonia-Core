package fr.pandonia.core.rank.manager;

import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.rank.IRank;
import fr.pandonia.api.rank.manager.IRankManager;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RankManager implements IRankManager {

    private PandoniaApplication instance;

    private List<IRank> ranks;

    public RankManager(PandoniaApplication instance) {
        this.instance = instance;
        this.ranks = new ArrayList<>();
    }

    public void register() {
        this.ranks = new ArrayList<>();
        instance.getMongoConnection().getMongoDatabase().getCollection("rank").find().forEach((Consumer<Document>) document -> {
            ranks.add(fr.pandonia.core.rank.Rank.fromDocument(document));
        });
    }

    public List<IRank> getRanks() {
        return this.ranks;
    }

    public IRank getRank(String name) {
        for (IRank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    @Override
    public boolean isPowerHigherOrEqual(String rank, String toTest){
        return getRank(rank).getPower() >= getRank(toTest).getPower();
    }

    public IRank getRankByPower(int power) {
        for (IRank rank : ranks) {
            if (rank.getPower() == power) {
                return rank;
            }
        }
        return null;
    }

    public IRank getDefaultRank() {
        for (IRank rank : ranks){
            if (rank.isDefault()){
                return rank;
            }
        }
        return null;
    }

}
