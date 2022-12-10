package fr.pandonia.core.listeners.pluginmessage;

import fr.pandonia.core.PandoniaCore;

public class PMManager {

    private PandoniaCore instance;

    public PMManager(PandoniaCore instance) {
        this.instance = instance;
        register();
    }

    public void register(){
        instance.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(instance.getPlugin(), "proxy:openmenu", new OpenMenuPM());
    }
}
