package fr.pandonia.core.server;

import fr.pandonia.api.player.ISimplePlayer;
import fr.pandonia.api.server.IServer;
import fr.pandonia.api.server.ServerStatus;
import fr.pandonia.core.player.SimplePlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Server implements IServer {

    private String name;
    private String customName;
    private String userFriendlyName;
    private String type;
    private int slots;
    private String ip;
    private int port;
    private ServerStatus status;
    private List<ISimplePlayer> players;
    private List<UUID> alivePlayers;
    private int specs;
    private ISimplePlayer host;
    private boolean whitelistStatus;
    private boolean queueStatus;
    private List<String> whitelist;
    private List<String> scenarios;
    boolean isOfficial;
    private boolean linkNeeded;

    public Server(String type, String name, String customName, String userFriendlyName, int slots, String ip, int port, ServerStatus status, List<ISimplePlayer> players, List<UUID> alivePlayers, int specs, ISimplePlayer host, boolean whitelistStatus, boolean queueStatus, List<String> whitelist, List<String> scenarios, boolean isOfficial, boolean linkNeeded) {
        this.type = type;
        this.name = name;
        this.customName = customName;
        this.userFriendlyName = userFriendlyName;
        this.slots = slots;
        this.ip = ip;
        this.port = port;
        this.status = status;
        this.players = players;
        this.alivePlayers = alivePlayers;
        this.specs = specs;
        this.host = host;
        this.whitelistStatus = whitelistStatus;
        this.queueStatus = queueStatus;
        this.whitelist = whitelist;
        this.scenarios = scenarios;
        this.isOfficial = isOfficial;
        this.linkNeeded = linkNeeded;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public String getUserFriendlyName() {
        return userFriendlyName;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public ServerStatus getServerStatus() {
        return status;
    }

    @Override
    public boolean isServerStatus(ServerStatus... serverStatuses){
        return Arrays.asList(serverStatuses).contains(this.status);
    }

    public void setServerStatus(ServerStatus status) {
        this.status = status;
    }

    @Override
    public List<ISimplePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(ISimplePlayer player){
        players.add(player);
    }

    public boolean removePlayer(UUID player){
        return players.removeIf(simplePlayer -> simplePlayer.getUUID().equals(player));
    }

    public int getNumberOfPlayers(){
        return players.size();
    }

    @Override
    public ISimplePlayer getHost() {
        return host;
    }

    @Override
    public List<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    @Override
    public int getSpecs() {
        return specs;
    }

    public void setSpecs(int specs) {
        this.specs = specs;
    }

    public void setHost(ISimplePlayer host) {
        this.host = host;
    }

    @Override
    public boolean isWhitelist() {
        return whitelistStatus;
    }

    public void setWhitelist(boolean whitelistStatus) {
        this.whitelistStatus = whitelistStatus;
    }

    @Override
    public boolean isQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(boolean queueStatus) {
        this.queueStatus = queueStatus;
    }

    @Override
    public List<String> getWhitelist() {
        return whitelist;
    }

    @Override
    public boolean isWhiteList(String name){
        for (String s : whitelist) {
            if(s.equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public void addPlayerToWhitelist(String player){
        this.whitelist.add(player);
    }

    public void removePlayerToWhitelist(String player){
        this.whitelist.remove(player);
    }

    @Override
    public List<String> getScenarios() {
        return scenarios;
    }

    public void addScenario(String scenario){
        this.scenarios.add(scenario);
    }

    public void removeScenario(String scenario){
        this.scenarios.remove(scenario);
    }

    public int getNumberOfPlayersWithRank(String rank){
        int nb = 0;
        for (ISimplePlayer sp : players){
            if (sp.getRank().equalsIgnoreCase(rank)){
                nb++;
            }
        }
        return nb;
    }

    @Override
    public int getNumberOfPlayersWithDisplayRank(String rank){
        int nb = 0;
        for (ISimplePlayer sp : players){
            if (sp.getDisplayRank().equalsIgnoreCase(rank)){
                nb++;
            }
        }
        return nb;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    @Override
    public boolean isLinkNeeded() {
        return linkNeeded;
    }

    public void setLinkNeeded(boolean linkNeeded) {
        this.linkNeeded = linkNeeded;
    }

    public List<UUID> getPlayersUUID(){
        List<UUID> uuids = new ArrayList<>();
        for (ISimplePlayer sp : players){
            uuids.add(sp.getUUID());
        }
        return uuids;
    }

    public Document toDocument(){
        Document document = new Document("name", name);
        document.append("customName", customName);
        document.append("userFriendlyName", userFriendlyName);
        document.append("type", type);
        document.append("slots", slots);
        document.append("ip", ip);
        document.append("port", port);
        document.append("status", status.toString());

        List<Document> playersDocuments = new ArrayList<>();
        for (ISimplePlayer sp : players){
            playersDocuments.add(sp.toSimpleDocument());
        }

        document.append("players", playersDocuments);
        if(alivePlayers != null && !alivePlayers.isEmpty()){
            document.append("alivePlayers", alivePlayers.stream().map(UUID::toString).collect(Collectors.toList()));
        }
        if (host != null){
            document.append("host", host.toSimpleDocument());
        }
        document.append("specs", specs);
        document.append("whitelistStatus", whitelistStatus);
        document.append("queueStatus", queueStatus);
        document.append("whitelist", whitelist);
        document.append("scenarios", scenarios);
        document.append("isOfficial", isOfficial);
        document.append("linkNeeded", linkNeeded);

        return document;
    }

    public void update(Document document){
        this.name = document.getString("name");
        this.customName = document.getString("customName");
        this.userFriendlyName = document.getString("userFriendlyName");
        this.type = document.getString("type");
        this.slots = document.getInteger("slots");
        this.ip = document.getString("ip");
        this.port = document.getInteger("port");
        this.status = ServerStatus.valueOf(document.getString("status"));
        List<Document> playersDocument = document.getList("players", Document.class);
        List<ISimplePlayer> players = new ArrayList<>();
        for (Document playerDocument : playersDocument){
            players.add(SimplePlayer.fromDocument(playerDocument));
        }
        this.alivePlayers = new ArrayList<>();
        if(document.getList("alivePlayers", String.class) != null){
            document.getList("alivePlayers", String.class).forEach(s -> alivePlayers.add(UUID.fromString(s)));
        }
        this.players = players;
        if (document.containsKey("host")){
            this.host = SimplePlayer.fromDocument(document.get("host", Document.class));
        }
        this.specs = document.getInteger("specs");
        this.whitelistStatus = document.getBoolean("whitelistStatus");
        this.queueStatus = document.getBoolean("queueStatus");
        this.whitelist = document.getList("whitelist", String.class);
        this.scenarios = document.getList("scenarios", String.class);
        this.isOfficial = document.getBoolean("isOfficial");
        boolean linkNeeded = false;
        if (document.containsKey("linkNeeded")){
            linkNeeded = document.getBoolean("linkNeeded");
        }
        this.linkNeeded = linkNeeded;
    }

    public static Server fromDocument(Document document){

        String name = document.getString("name");
        String customName = document.getString("customName");
        String userFriendlyName = document.getString("userFriendlyName");
        String type = document.getString("type");
        int slots = document.getInteger("slots");
        String ip = document.getString("ip");
        int port = document.getInteger("port");
        ServerStatus status = ServerStatus.valueOf(document.getString("status"));

        List<Document> playersDocument = document.getList("players", Document.class);
        List<ISimplePlayer> players = new ArrayList<>();
        for (Document playerDocument : playersDocument){
            players.add(SimplePlayer.fromDocument(playerDocument));
        }
        List<UUID> alivePlayers = new ArrayList<>();
        if(document.getList("alivePlayers", String.class) != null){
            document.getList("alivePlayers", String.class).forEach(s -> alivePlayers.add(UUID.fromString(s)));
        }
        ISimplePlayer host = null;
        if (document.containsKey("host")){
            host = SimplePlayer.fromDocument((Document) document.get("host"));
        }

        List<ISimplePlayer> inGamePlayers = null;
        if (document.getList("inGamePlayers", Document.class) != null){
            List<Document> inGamePlayersDocument = document.getList("inGamePlayers", Document.class);
            inGamePlayers = new ArrayList<>();
            for (Document playerDocument : inGamePlayersDocument){
                inGamePlayers.add(fr.pandonia.core.player.SimplePlayer.fromDocument(playerDocument));
            }
        }
        int specs = document.getInteger("specs");
        boolean whitelistStatus = document.getBoolean("whitelistStatus");
        boolean queueStatus = document.getBoolean("queueStatus");
        List<String> whitelist = document.getList("whitelist", String.class);
        List<String> scenarios = document.getList("scenarios", String.class);
        boolean isOfficial = document.getBoolean("isOfficial");
        boolean linkNeeded = false;
        if (document.containsKey("linkNeeded")){
            linkNeeded = document.getBoolean("linkNeeded");
        }

        return new Server(type, name, customName, userFriendlyName, slots, ip, port, status, players, alivePlayers, specs, host, whitelistStatus, queueStatus, whitelist, scenarios, isOfficial, linkNeeded);
    }

}
