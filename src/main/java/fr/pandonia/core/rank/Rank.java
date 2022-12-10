package fr.pandonia.core.rank;

import fr.pandonia.api.rank.IRank;
import org.bson.Document;

public class Rank implements IRank {

    private String name;
    private String color;
    private String prefix;
    private String suffix;
    private int power;
    private int preConfigs;
    private double boost;
    private boolean isHost;
    private boolean isPriority;
    private boolean isDefault;
    private boolean isAdmin;
    private boolean isStaff;

    public Rank(String name, String color, String prefix, String suffix, int power, int preConfigs, double boost, boolean isHost, boolean isPriority, boolean isDefault, boolean isAdmin, boolean isStaff) {
        this.name = name;
        this.color = color;
        this.prefix = prefix;
        this.suffix = suffix;
        this.power = power;
        this.preConfigs = preConfigs;
        this.boost = boost;
        this.isHost = isHost;
        this.isPriority = isPriority;
        this.isDefault = isDefault;
        this.isAdmin = isAdmin;
        this.isStaff = isStaff;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getPreConfigs() {
        return preConfigs;
    }

    @Override
    public double getBoost() {
        return boost;
    }

    @Override
    public boolean isHost() {
        return isHost;
    }

    @Override
    public boolean isPriority() {
        return isPriority;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public boolean isStaff() {
        return isStaff;
    }

    public static Rank fromDocument(Document document){
        return new fr.pandonia.core.rank.Rank(
                document.getString("name"),
                document.getString("color"),
                document.getString("prefix"),
                document.getString("suffix"),
                document.getInteger("power"),
                document.getInteger("preConfigs"),
                document.getDouble("boost"),
                document.getBoolean("isHost"),
                document.getBoolean("isPriority"),
                document.getBoolean("isDefault"),
                document.getBoolean("isAdmin"),
                document.getBoolean("isStaff"));
    }

}