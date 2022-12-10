package fr.pandonia.core.server;

import fr.pandonia.api.server.IServerType;
import fr.pandonia.api.settings.InstanceSettings;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerType implements IServerType {

    private String name;
    private String customName;
    private int slots;
    private List<String> plugins;
    private List<String> files;
    private ServerRam ram;
    private Map<String, String> maps;
    private boolean hub;
    private boolean host;
    private boolean miniGame;
    private boolean showTab;
    private boolean leaveQueue;
    private String state;

    private InstanceSettings instanceSettings;

    public ServerType(String name, String customName, int slots, List<String> plugins, List<String> files, ServerRam ram, Map<String, String> maps, boolean hub, boolean host, boolean miniGame, boolean showTab, boolean leaveQueue, InstanceSettings instanceSettings, String state) {
        this.name = name;
        this.customName = customName;
        this.slots = slots;
        this.plugins = plugins;
        this.files = files;
        this.ram = ram;
        this.maps = maps;
        this.hub = hub;
        this.host = host;
        this.miniGame = miniGame;
        this.showTab = showTab;
        this.leaveQueue = leaveQueue;
        this.instanceSettings = instanceSettings;
        this.state = state;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCustomName() {
        return customName;
    }

    @Override
    public int getSlots() {
        return slots;
    }

    @Override
    public List<String> getPlugins() {
        return plugins;
    }

    @Override
    public List<String> getFiles() {
        return files;
    }

    @Override
    public ServerRam getRam() {
        return ram;
    }

    @Override
    public Map<String, String> getMaps() {
        return maps;
    }

    @Override
    public boolean isHub() {
        return hub;
    }

    @Override
    public boolean isHost() {
        return host;
    }

    @Override
    public boolean isMiniGame() {
        return miniGame;
    }

    @Override
    public boolean isShowTab() {
        return showTab;
    }

    @Override
    public boolean isLeaveQueue() {
        return leaveQueue;
    }

    @Override
    public InstanceSettings getInstanceSettings() {
        return instanceSettings;
    }

    @Override
    public String getState() {
        return state;
    }

    public static ServerType fromDocument(Document document){
        String name = document.getString("name");
        String customName = document.getString("customName");
        int slots = document.getInteger("slots");
        List<String> plugins = document.getList("plugins", String.class);
        List<String> files = document.getList("files", String.class);
        ServerRam ram = ServerRam.fromDocument(document.get("ram", Document.class));

        Map<String, String> maps = new HashMap<>();
        Document mapsDocument = document.get("maps", Document.class);

        for (Map.Entry<String, Object> entry : mapsDocument.entrySet()){
            if (entry.getValue() instanceof String){
                maps.put(entry.getKey(), (String) entry.getValue());
            }
        }

        boolean hub = document.getBoolean("hub");
        boolean host = document.getBoolean("host");
        boolean miniGame = document.getBoolean("minigame");
        boolean showTab = document.getBoolean("showTab");
        boolean leaveQueue = document.getBoolean("leaveQueue");

        InstanceSettings instanceSettings = new InstanceSettings();
        if(document.get("whatToGet", Document.class) != null){
            instanceSettings = InstanceSettings.fromDocument(document.get("whatToGet", Document.class));
        }

        return new ServerType(name, customName, slots, plugins, files, ram, maps, hub, host, miniGame, showTab, leaveQueue, instanceSettings, document.getString("state"));
    }


}
