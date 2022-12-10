package fr.pandonia.core.player;

import fr.pandonia.api.player.ISimplePlayer;
import fr.pandonia.api.player.nick.INick;
import fr.pandonia.api.rank.IRank;
import fr.pandonia.api.rank.manager.IRankManager;
import fr.pandonia.core.player.nick.Nick;
import org.bson.Document;

import java.util.Objects;
import java.util.UUID;

public class SimplePlayer implements ISimplePlayer {

    private UUID uuid;
    private String name;
    private String rank;
    private INick nick;
    private String customRankName;
    private String customRankColor;

    public SimplePlayer(UUID uuid, String name, String rank, INick nick, String customRankName, String customRankColor) {
        this.uuid = uuid;
        this.name = name;
        this.rank = rank;
        this.nick = nick;
        this.customRankName = customRankName;
        this.customRankColor = customRankColor;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName(){
        return nick == null ? this.name : this.nick.getNick();
    }

    @Override
    public String getRank() {
        return this.rank;
    }

    @Override
    public String getDisplayRank(){
        return nick != null && nick.getRankNick() != null ? nick.getRankNick() : rank;
    }

    @Override
    public INick getNick() {
        return nick;
    }

    @Override
    public void setNick(INick nick) {
        this.nick = nick;
    }

    @Override
    public IRank getRankObject(IRankManager rankManager) {
        return rankManager.getRank(getRank());
    }

    @Override
    public IRank getDisplayRankObject(IRankManager rankManager) {
        return rankManager.getRank(getDisplayRank());
    }

    @Override
    public String getPrefix(IRankManager rankManager){
        return rank.equals("JOUEUR") ? "§7" : (customRankName != null && rank.equals("ITACHI") ? (customRankColor == null ? "§3" : customRankColor) + "§l" + customRankName + (customRankColor == null ? "§3" : customRankColor): rankManager.getRank(rank).getColor() + "§l" + rankManager.getRank(rank).getName() + rankManager.getRank(rank).getColor()) + " ";
    }

    @Override
    public String getDisplayPrefix(IRankManager rankManager){
        return getDisplayRank().equals("JOUEUR") ? "§7" : (customRankName != null && getDisplayRank().equals("ITACHI") ? (customRankColor == null ? "§3" : customRankColor) + "§l" + customRankName + (customRankColor == null ? "§3" : customRankColor): rankManager.getRank(getDisplayRank()).getColor() + "§l" + rankManager.getRank(getDisplayRank()).getName() + rankManager.getRank(getDisplayRank()).getColor()) + " ";
    }

    @Override
    public String getCustomRankName() {
        return customRankName;
    }

    @Override
    public String getCustomRankColor() {
        return customRankColor;
    }

    @Override
    public void setCustomRankName(String customRankName) {
        this.customRankName = customRankName;
    }
    @Override
    public void setCustomRankColor(String customRankColor) {
        this.customRankColor = customRankColor;
    }

    @Override
    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public Document toDocument() {
        return toSimpleDocument();
    }

    @Override
    public Document toSimpleDocument(){
        Document document = new Document("uuid", uuid.toString()).append("name", name).append("rank", rank);
        if(rank.equals("ITACHI")){
            if(customRankColor != null){
                document.append("customRankColor", customRankColor);
            }
            if(customRankName != null){
                document.append("customRankName", customRankName);
            }
        }
        if(nick != null){
            document.append("nick", nick.toDocument());
        }
        return document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePlayer that = (SimplePlayer) o;

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    public static SimplePlayer fromDocument(Document document){
        UUID uuid = UUID.fromString(document.getString("uuid"));
        String name = document.getString("name");
        String rank = document.getString("rank");
        String customRankName = document.getString("customRankName");
        String customRankColor = document.getString("customRankColor");
        INick nick = null;
        if(document.get("nick", Document.class) != null){
            nick = Nick.fromDocument(document.get("nick", Document.class));
        }
        return new SimplePlayer(uuid, name, rank, nick, customRankName, customRankColor);

    }

}
