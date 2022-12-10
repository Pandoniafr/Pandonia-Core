package fr.pandonia.core.player.settings;

import fr.pandonia.api.player.settings.FriendsSetting;
import fr.pandonia.api.player.settings.IPlayerArmorSettings;
import fr.pandonia.api.player.settings.IPlayerSettings;
import org.bson.Document;

import java.util.Arrays;
import java.util.UUID;

public class PlayerSettings implements IPlayerSettings {

    private UUID playerUUID;
    private FriendsSetting mp;
    private FriendsSetting showPlayerHub;
    private boolean friendsRequest;
    private boolean friendsNotification;
    private boolean mentions;
    private boolean receiveHelpop;
    private boolean helpopGlobal;
    private boolean staffChat;
    private int activePetID;
    private int activeArenaBlockID;
    private int activeArrowID;
    private int activeGadgetID;
    private int activeKillAnimationID;
    private IPlayerArmorSettings playerArmorSettings;
    private boolean particles;
    private boolean vanish;

    public PlayerSettings(UUID playerUUID, FriendsSetting mp, FriendsSetting showPlayerHub, boolean friendsRequest, boolean friendsNotification, boolean mentions, boolean receiveHelpop, boolean helpopGlobal, boolean staffChat, int activePetID, int activeArenaBlockID, int activeArrowID, int activeGadgetID, int activeKillAnimationID, IPlayerArmorSettings playerArmorSettings, boolean particles, boolean vanish){
        this.playerUUID = playerUUID;
        this.mp = mp;
        this.showPlayerHub = showPlayerHub;
        this.friendsRequest = friendsRequest;
        this.friendsNotification = friendsNotification;
        this.mentions = mentions;
        this.receiveHelpop = receiveHelpop;
        this.helpopGlobal = helpopGlobal;
        this.staffChat = staffChat;
        this.activePetID = activePetID;
        this.activeArenaBlockID = activeArenaBlockID;
        this.activeArrowID = activeArrowID;
        this.activeGadgetID = activeGadgetID;
        this.activeKillAnimationID = activeKillAnimationID;
        this.playerArmorSettings = playerArmorSettings;
        this.particles = particles;
        this.vanish = vanish;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public boolean isMP(FriendsSetting ... mps) {
        return Arrays.asList(mps).contains(mp);
    }

    @Override
    public void setMP(FriendsSetting mp) {
        this.mp = mp;
    }

    @Override
    public FriendsSetting getMp() {
        return mp;
    }

    @Override
    public boolean isShowPlayerHub(FriendsSetting... showPlayerHubs) {
        return Arrays.asList(showPlayerHubs).contains(showPlayerHub);
    }

    @Override
    public void setShowPlayerHub(FriendsSetting showPlayerHub) {
        this.showPlayerHub = showPlayerHub;
    }

    @Override
    public FriendsSetting getShowPlayerHub() {
        return showPlayerHub;
    }

    @Override
    public boolean isFriendsRequest() {
        return friendsRequest;
    }

    @Override
    public void setFriendsRequest(boolean friendsRequest) {
        this.friendsRequest = friendsRequest;
    }

    @Override
    public boolean isFriendsNotification() {
        return friendsNotification;
    }

    @Override
    public void setFriendsNotification(boolean friendsNotification) {
        this.friendsNotification = friendsNotification;
    }

    @Override
    public boolean isMentions() {
        return mentions;
    }

    @Override
    public void setMentions(boolean mentions) {
        this.mentions = mentions;
    }

    @Override
    public boolean isReceiveHelpop() {
        return receiveHelpop;
    }

    @Override
    public void setReceiveHelpop(boolean receiveHelpop) {
        this.receiveHelpop = receiveHelpop;
    }

    @Override
    public boolean isHelpopGlobal() {
        return helpopGlobal;
    }

    @Override
    public void setHelpopGlobal(boolean helpopGlobal) {
        this.helpopGlobal = helpopGlobal;
    }

    @Override
    public boolean isStaffChat() {
        return staffChat;
    }

    @Override
    public void setStaffChat(boolean staffChat) {
        this.staffChat = staffChat;
    }

    @Override
    public int getActivePetID() {
        return activePetID;
    }

    @Override
    public void setActivePetID(int activePetID) {
        this.activePetID = activePetID;
    }

    @Override
    public int getActiveArenaBlockID() {
        return activeArenaBlockID;
    }

    @Override
    public void setActiveArenaBlockID(int activeArenaBlockID) {
        this.activeArenaBlockID = activeArenaBlockID;
    }

    @Override
    public int getActiveArrowID() {
        return activeArrowID;
    }

    @Override
    public void setActiveArrowID(int activeArrowID) {
        this.activeArrowID = activeArrowID;
    }

    @Override
    public int getActiveGadgetID() {
        return activeGadgetID;
    }

    @Override
    public void setActiveGadgetID(int activeGadgetID) {
        this.activeGadgetID = activeGadgetID;
    }

    @Override
    public int getActiveKillAnimationID() {
        return activeKillAnimationID;
    }

    @Override
    public void setActiveKillAnimationID(int activeKillAnimationID) {
        this.activeKillAnimationID = activeKillAnimationID;
    }

    @Override
    public boolean isParticles() {
        return particles;
    }

    @Override
    public void setParticles(boolean particles) {
        this.particles = particles;
    }

    @Override
    public IPlayerArmorSettings getPlayerArmorSettings() {
        return playerArmorSettings;
    }

    @Override
    public void setVanish(boolean vanish) {
        this.vanish = vanish;
    }

    @Override
    public boolean isVanish() {
        return vanish;
    }

    @Override
    public Document toDocument(){
        Document settings = new Document("playerUUID", playerUUID.toString());
        settings.append("mp", mp.toString());
        settings.append("showPlayerHub", showPlayerHub.toString());
        settings.append("friendsRequest", friendsRequest);
        settings.append("friendsNotification", friendsNotification);
        settings.append("mentions", mentions);
        settings.append("receiveHelpop", receiveHelpop);
        settings.append("helpopGlobal", helpopGlobal);
        settings.append("staffChat", staffChat);
        settings.append("activePetID", activePetID);
        settings.append("activeArenaBlockID", activeArenaBlockID);
        settings.append("activeArrowID", activeArrowID);
        settings.append("activeGadgetID", activeGadgetID);
        settings.append("activeKillAnimationID", activeKillAnimationID);
        settings.append("playerArmorSettings", playerArmorSettings.toDocument());
        settings.append("particles", particles);
        settings.append("vanish", vanish);
        return settings;
    }

    @Override
    public void update(Document document) {
        this.mp = FriendsSetting.fromString(document.getString("mp"));
        this.showPlayerHub = FriendsSetting.fromString(document.getString("showPlayerHub"));
        this.friendsRequest = document.getBoolean("friendsRequest");
        this.friendsNotification = document.getBoolean("friendsNotification");
        this.mentions = document.getBoolean("mentions");
        this.receiveHelpop = document.getBoolean("receiveHelpop");
        this.helpopGlobal = document.getBoolean("helpopGlobal");
        this.staffChat = document.getBoolean("staffChat");
        this.activePetID = document.getInteger("activePetID");
        this.activeArenaBlockID = document.getInteger("activeArenaBlockID");
        this.activeArrowID = document.getInteger("activeArrowID");
        this.activeGadgetID = document.getInteger("activeGadgetID");
        this.activeKillAnimationID = document.getInteger("activeKillAnimationID");
        this.playerArmorSettings = PlayerArmorSettings.fromDocument(document.get("playerArmorSettings", Document.class));
        this.particles = document.getBoolean("particles");
        this.vanish = document.getBoolean("vanish");
    }

    public static PlayerSettings fromDocument(Document document){
        FriendsSetting mp = FriendsSetting.ALL;
        if(FriendsSetting.fromString(document.getString("mp")) != null){
            mp = FriendsSetting.fromString(document.getString("mp"));
        }
        FriendsSetting showPlayerHub = FriendsSetting.ALL;
        if(FriendsSetting.fromString(document.getString("showPlayerHub")) != null){
            showPlayerHub = FriendsSetting.fromString(document.getString("showPlayerHub"));
        }
        boolean friendsRequest = true;
        if (document.getBoolean("friendsRequest") != null){
            friendsRequest = document.getBoolean("friendsRequest");
        }
        boolean friendsNotification = true;
        if (document.getBoolean("friendsNotification") != null){
            friendsNotification = document.getBoolean("friendsNotification");
        }
        boolean receiveHelpop = true;
        if (document.getBoolean("receiveHelpop") != null){
            receiveHelpop = document.getBoolean("receiveHelpop");
        }
        boolean helpopGlobal = true;
        if (document.getBoolean("helpopGlobal") != null){
            helpopGlobal = document.getBoolean("helpopGlobal");
        }
        boolean staffChat = true;
        if (document.getBoolean("staffChat") != null){
            staffChat = document.getBoolean("staffChat");
        }
        boolean mentions = true;
        if (document.getBoolean("mentions") != null){
            mentions = document.getBoolean("mentions");
        }
        int activePetId = 0;
        if (document.getInteger("activePetID") != null){
            activePetId = document.getInteger("activePetID");
        }
        int activeArenaBlockID = 1000;
        if (document.getInteger("activeArenaBlockID") != null){
            activeArenaBlockID = document.getInteger("activeArenaBlockID");
        }
        int activeArrowID = 0;
        if (document.getInteger("activeArrowID") != null){
            activeArrowID = document.getInteger("activeArrowID");
        }
        int activeGadgetID = 0;
        if (document.getInteger("activeGadgetID") != null){
            activeGadgetID = document.getInteger("activeGadgetID");
        }
        int activeKillAnimationID = 0;
        if (document.getInteger("activeKillAnimationID") != null){
            activeKillAnimationID = document.getInteger("activeKillAnimationID");
        }
        IPlayerArmorSettings playerArmorSettings = PlayerArmorSettings.getDefault();
        if(document.get("playerArmorSettings", Document.class) != null){
            playerArmorSettings = PlayerArmorSettings.fromDocument(document.get("playerArmorSettings", Document.class));
        }
        boolean particles = true;
        if(document.getBoolean("particles") != null){
            particles = document.getBoolean("particles");
        }
        boolean vanish = false;
        if(document.getBoolean("vanish") != null){
            vanish = document.getBoolean("vanish");
        }
        return new PlayerSettings(UUID.fromString(document.getString("playerUUID")), mp, showPlayerHub, friendsRequest, friendsNotification, mentions, receiveHelpop, helpopGlobal, staffChat, activePetId, activeArenaBlockID, activeArrowID, activeGadgetID, activeKillAnimationID, playerArmorSettings, particles, vanish);
    }

    public static PlayerSettings getDefault(UUID uuid){
        return new PlayerSettings(uuid, FriendsSetting.ALL, FriendsSetting.ALL, true, true, true, true, true, true, 0, 1000, 0, 0, 0, PlayerArmorSettings.getDefault(), true, false);
    }


}
