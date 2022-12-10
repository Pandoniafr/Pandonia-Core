package fr.pandonia.core.player;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.nick.INick;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.TimeUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class PPlayer extends SimplePlayer implements IPPlayer {

    private Date firstLogin;
    private Date lastLogin;
    private Date rankEnd;
    private int saphirs;
    private int hosts;
    private int lootboxes;
    private int playingTime;

    private double cooldown;
    private boolean saved;

    public PPlayer(UUID uniqueId, String name, String rank, INick nick, Date firstLogin, Date lastLogin, Date rankEnd, int saphirs, int hosts, int lootboxes, int playingTime) {
        super(uniqueId, name, rank, nick, null, null);
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.rankEnd = rankEnd;
        this.saphirs = saphirs;
        this.hosts = hosts;
        this.lootboxes = lootboxes;
        this.playingTime = playingTime;

        this.cooldown = 2;
        this.saved = false;
    }

    public PPlayer(SimplePlayer sp, Date firstLogin, Date lastLogin, Date rankEnd, int saphirs, int hosts, int lootboxes, int playingTime) {
        super(sp.getUUID(), sp.getName(), sp.getRank(), sp.getNick(), sp.getCustomRankName(), sp.getCustomRankColor());
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.rankEnd = rankEnd;
        this.saphirs = saphirs;
        this.hosts = hosts;
        this.lootboxes = lootboxes;
        this.playingTime = playingTime;

        this.saved = false;
    }

    @Override
    public Player getBukkitPlayer(){
        return Bukkit.getPlayer(getUUID());
    }

    @Override
    public void sendMessage(String... messages) {
        Player p = getBukkitPlayer();
        if (p != null){
            p.sendMessage(messages);
        }
    }

    @Override
    public Date getFirstLogin() {
        return firstLogin;
    }

    @Override
    public Date getLastLogin() {
        return lastLogin;
    }

    @Override
    public Date getRankEnd() {
        return rankEnd;
    }

    @Override
    public void setRankEnd(Date rankEnd) {
        this.rankEnd = rankEnd;
    }

    @Override
    public int getSaphirs() {
        return saphirs;
    }

    @Override
    public void addSaphirs(int saphirs){
        this.saphirs += saphirs;
    }

    @Override
    public void removeSaphirs(int saphirs){
        this.saphirs -= saphirs;
    }

    @Override
    public void addSaphirsWithReason(int saphirs, String reason){
        saphirs *= getRankObject(PandoniaAPI.get().getRankManager()).getBoost();
        addSaphirs(saphirs);
        Player p = getBukkitPlayer();
        if (p != null){
            p.sendMessage(PandoniaMessages.getPrefix() + "§f+§3"+ saphirs +" Saphirs §f(§3"+reason+"§f)");
        }
    }

    @Override
    public int getHosts() {
        return hosts;
    }

    @Override
    public void addHosts(int hosts) {
        this.hosts+=hosts;
    }

    @Override
    public void removeLoobox(int lootboxes){
        this.lootboxes -= lootboxes;
    }

    @Override
    public void removeHosts(int hosts){
        this.hosts -= hosts;
    }

    @Override
    public int getLootboxes() {
        return lootboxes;
    }

    @Override
    public void addLootbox(int lootboxes){
        this.lootboxes += lootboxes;
    }

    @Override
    public void removeLootbox(){
        lootboxes--;
    }

    @Override
    public int getPlayingTime() {
        return playingTime;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public void removeCooldown(){
        if (this.cooldown > 0){
            this.cooldown--;
        }
    }

    @Override
    public boolean canSeeNick(IPPlayer cpp){
        return getRankObject(PandoniaAPI.get().getRankManager()).getPower() >= cpp.getRankObject(PandoniaAPI.get().getRankManager()).getPower();
    }

    @Override
    public String getNickPrefix(IPPlayer cpp){
        return canSeeNick(cpp) ? cpp.getPrefix(PandoniaAPI.get().getRankManager()) : cpp.getDisplayPrefix(PandoniaAPI.get().getRankManager());
    }

    @Override
    public String getNickDisplayName(IPPlayer cpp){
        return canSeeNick(cpp) && !cpp.getUUID().equals(getUUID()) ? cpp.getName() : cpp.getDisplayName();
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @Override
    public ItemStack getProfileHead(){
        return new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(getName()).setName("§8┃ §fProfil: " + getName()).setLore("", " §8• §fGrade §f: §3" + getRank(), " §8• §fTemps restant §f: §3" + (getRankEnd() != null ? TimeUtils.timeToString((getRankEnd().getTime()-System.currentTimeMillis())/1000) : "Infini"), "", " §8• §fSaphirs §f: §3" + getSaphirs(), " §8• §fHosts §f: §3" + getHosts(), " §8• §fLootboxs §f: §3" + getLootboxes(), "").toItemStack();
    }

    @Override
    public Document toDocument() {
        return super.toDocument().append("firstLogin", firstLogin).append("lastLogin", lastLogin).append("rankEnd", rankEnd).append("saphirs", saphirs).append("hosts", hosts).append("lootboxes", lootboxes).append("playingTime", playingTime);
    }

    public static PPlayer fromDocument(Document document){
        SimplePlayer sp = SimplePlayer.fromDocument(document);
        return new PPlayer(sp, document.getDate("firstLogin"), document.getDate("lastLogin"), document.getDate("rankEnd"), document.getInteger("saphirs"), document.getInteger("hosts"), document.getInteger("lootboxes"), document.getInteger("playingTime"));
    }

}
