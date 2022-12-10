package fr.pandonia.core.player.stats.uhc;

import fr.pandonia.api.player.stats.uhc.IPlayerUHCStats;
import org.bson.Document;

import java.lang.reflect.Field;

public class PlayerUHCStats implements IPlayerUHCStats {

    private String mode;
    private int kills;
    private int assists;
    private int deaths;
    private int wins;
    private int gamesPlayed;
    private double damagesReceived;
    private double damagesInflected;

    public PlayerUHCStats(String mode, int kills, int assists, int deaths, int wins, int gamesPlayed, double damagesReceived, double damagesInflected) {
        this.mode = mode;
        this.kills = kills;
        this.assists = assists;
        this.deaths = deaths;
        this.wins = wins;
        this.gamesPlayed = gamesPlayed;
        this.damagesReceived = damagesReceived;
        this.damagesInflected = damagesInflected;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void addKill() {
        kills++;
    }

    @Override
    public int getAssists() {
        return assists;
    }

    @Override
    public void addAssist() {
        assists++;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public void addDeath() {
        deaths++;
    }

    @Override
    public int getWins() {
        return wins;
    }

    @Override
    public void addWin() {
        wins++;
    }

    @Override
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    @Override
    public void addGamePlayed() {
        gamesPlayed++;
    }

    @Override
    public double getDamagesReceived() {
        return damagesReceived;
    }

    @Override
    public void addDamagesReceived(double received) {
        damagesReceived += received;
    }

    @Override
    public double getDamagesInflected() {
        return damagesInflected;
    }

    @Override
    public void addDamagesInflected(double inflected) {
        damagesInflected += inflected;
    }

    @Override
    public double getRatio() {
        if(deaths > 0){
            return ((double)kills+(double) assists)/(double)deaths;
        }
        return 0;
    }

    @Override
    public double getWinRate() {
        if(gamesPlayed > 0){
            return (double)wins/(double)gamesPlayed;
        }
        return 0;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                document.append(field.getName(), field.get(this));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public void update(Document document) {
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if(document.get(field.getName()) != null){
                    field.setAccessible(true);
                    field.set(this, document.get(field.getName()));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static PlayerUHCStats getDefault(String mode){
        return new PlayerUHCStats(mode, 0, 0, 0, 0, 0, 0, 0);
    }

    public static PlayerUHCStats fromDocument(Document document){
        try {
            PlayerUHCStats playerUHCStats = getDefault("");
            for (Field field : playerUHCStats.getClass().getDeclaredFields()) {
                if(document.get(field.getName()) != null){
                    field.setAccessible(true);
                    field.set(playerUHCStats, document.get(field.getName()));
                }
            }
            return playerUHCStats;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
