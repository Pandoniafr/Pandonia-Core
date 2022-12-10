package fr.pandonia.core.player.battlepass;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.battlepass.IBPChallenge;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.battlepass.IPlayerBattlePass;
import fr.pandonia.api.player.battlepass.PlayerBattlePassUtils;
import org.bson.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerBattlePass implements IPlayerBattlePass {

    private UUID playerUUID;
    private int xp;
    private boolean buy;
    private boolean boosted;
    private Map<Integer, Date> claimedItems;
    private Map<Integer, Date> challenge;

    public PlayerBattlePass(UUID playerUUID, int xp, boolean buy, boolean boosted, Map<Integer, Date> claimedItems, Map<Integer, Date> challenge) {
        this.playerUUID = playerUUID;
        this.xp = xp;
        this.buy = buy;
        this.boosted = boosted;
        this.claimedItems = claimedItems;
        this.challenge = challenge;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public int getLevel() {
        return PlayerBattlePassUtils.getLevelFromXP(xp);
    }

    @Override
    public int getXP() {
        return xp;
    }

    @Override
    public int getActualXP() {
        return xp-PlayerBattlePassUtils.getAllNeededXPForALevel(getLevel());
    }

    @Override
    public void addXP(int xp) {
        this.xp += xp;
    }

    @Override
    public void addXPWithReason(int xp, String reason) {
        /*IPPlayer pp = PandoniaAPI.get().getPlayerManager().getPlayer(playerUUID);
        if (pp != null){
            double boost = pp.getRankObject(PandoniaAPI.get().getRankManager()).getBoost();
            if (boost > 1){
                xp *= boost;
            }else if (boosted || boost < 1.25){
                xp *= 1.25;
            }
        }
        int oldLevel = getLevel();
        addXP(xp);
        boolean lu = false;
        if (getLevel() > oldLevel){
            lu = true;
        }
        Player p = Bukkit.getPlayer(getPlayerUUID());
        if (p != null){
            p.sendMessage(PandoniaMessages.getPrefix() + "§f+§6"+ xp +" XP §f(§6"+reason+"§f)" + (lu ? " §7§o(Passage au niveau §6§o" + getLevel() + "§7§o! Félicitations!)" : ""));
        }*/
    }

    @Override
    public int getPercent() {
        double d = (double) getActualXP()/PlayerBattlePassUtils.getNeededXPForALevel(getLevel()+1);
        return (int) Math.round(d*100);
    }

    @Override
    public int getAllNeededXPForNextLevel() {
        return PlayerBattlePassUtils.getAllNeededXPForALevel(getLevel()+1);
    }

    @Override
    public int getNeededXPForNextLevel() {
        return PlayerBattlePassUtils.getNeededXPForALevel(getLevel()+1);
    }

    @Override
    public int getRemainingXP() {
        return getAllNeededXPForNextLevel()-getXP();
    }

    @Override
    public boolean isBuy() {
        if (PandoniaAPI.get() != null){
            IPPlayer p = PandoniaAPI.get().getPlayerManager().getPlayer(getPlayerUUID());
            if (p != null){
                if (p.getRankObject(PandoniaAPI.get().getRankManager()).isStaff()){
                    return true;
                }
            }
        }
        return buy;
    }

    @Override
    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    @Override
    public boolean isBoosted() {
        return boosted;
    }

    @Override
    public void setBoosted(boolean boosted) {
        this.boosted = boosted;
    }

    @Override
    public Map<Integer, Date> getClaimedItems() {
        return claimedItems;
    }

    @Override
    public Map<Integer, Date> getChallenge() {
        return challenge;
    }

    @Override
    public void addChallenge(int id){
        IBPChallenge challenge = PandoniaAPI.get().getBattlePassItemsManager().getChallenge(id);
        if (challenge != null){
            if (!getChallenge().containsKey(id)){
                getChallenge().put(id, new Date(System.currentTimeMillis()));
                IPPlayer pp = PandoniaAPI.get().getPlayerManager().getPlayer(getPlayerUUID());
                if (pp != null){
                    addXPWithReason(challenge.getGain(), challenge.getName());
                }
            }
        }
    }

    @Override
    public Document toDocument() {
        Document claimedItems = new Document();
        this.claimedItems.forEach((integer, date) -> claimedItems.append(String.valueOf(integer), date));
        Document challenge = new Document();
        this.challenge.forEach((integer, date) -> challenge.append(String.valueOf(integer), date));
        return new Document("playerUUID", playerUUID.toString()).append("xp", xp).append("buy", buy).append("boosted", boosted).append("claimedItems", claimedItems).append("challenge", challenge);
    }

    @Override
    public void update(Document document) {
        this.xp = document.getInteger("xp");
        this.buy = document.getBoolean("buy");
        this.boosted = document.getBoolean("boosted");
        Map<Integer, Date> claimedItems = new HashMap<>();
        document.get("claimedItems", Document.class).forEach((s, o) -> claimedItems.put(Integer.parseInt(s), (Date) o));
        this.claimedItems = claimedItems;
        Map<Integer, Date> challenge = new HashMap<>();
        document.get("challenge", Document.class).forEach((s, o) -> challenge.put(Integer.parseInt(s), (Date) o));
        this.challenge = challenge;
    }

    public static PlayerBattlePass fromDocument(Document document){
        Map<Integer, Date> claimedItems = new HashMap<>();
        document.get("claimedItems", Document.class).forEach((s, o) -> claimedItems.put(Integer.parseInt(s), (Date) o));
        Map<Integer, Date> challenge = new HashMap<>();
        document.get("challenge", Document.class).forEach((s, o) -> challenge.put(Integer.parseInt(s), (Date) o));
        return new PlayerBattlePass(UUID.fromString(document.getString("playerUUID")), document.getInteger("xp"), document.getBoolean("buy"), document.getBoolean("boosted"), claimedItems, challenge);
    }

    public static PlayerBattlePass getDefault(UUID playerUUID){
        return new PlayerBattlePass(playerUUID, 0, false, false, new HashMap<Integer, Date>(){{
            put(0, new Date(System.currentTimeMillis()));
        }}, new HashMap<>());
    }

}