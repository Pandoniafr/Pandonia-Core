package fr.pandonia.core.plugin;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.core.PandoniaCore;
import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        //Créé et set l'instance principale
        PandoniaAPI.setPandora(new PandoniaCore(this));

        PandoniaAPI.get().onLoad();
        getServer().getConsoleSender().sendMessage("§7(§3Pandonia§f) §7Démarrage de l'§3API§7.");
    }

    @Override
    public void onEnable() {
        PandoniaAPI.get().onEnable();
        getServer().getConsoleSender().sendMessage("§7(§3Pandonia§f) §3API §7démarrée.");
    }

    @Override
    public void onDisable() {
        PandoniaAPI.get().onDisable();
        getServer().getConsoleSender().sendMessage("§7(§3Pandonia§f) §3API §7arrêtée.");
    }


}
