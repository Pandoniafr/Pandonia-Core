package fr.pandonia.core.player.nick;

import fr.pandonia.api.player.nick.INick;
import org.bson.Document;

public class Nick implements INick {

    private String nick;
    private String rankNick;
    private String skinName;

    public Nick(String nick, String rankNick, String skinName) {
        this.nick = nick;
        this.rankNick = rankNick;
        this.skinName = skinName;
    }

    public Nick(String nick) {
        this.nick = nick;
        this.rankNick = "JOUEUR";
    }

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String getRankNick() {
        return rankNick;
    }

    @Override
    public void setRankNick(String rankNick) {
        this.rankNick = rankNick;
    }

    @Override
    public String getSkinName() {
        return skinName;
    }

    @Override
    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public static Nick fromDocument(Document document){
        return new Nick(document.getString("nick"), document.getString("rankNick"), document.getString("skinName"));
    }

    @Override
    public Document toDocument(){
        Document document = new Document("nick", nick);
        if(rankNick != null){
            document.append("rankNick", rankNick);
        }
        if(skinName != null){
            document.append("skinName", skinName);
        }
        return document;
    }
}
