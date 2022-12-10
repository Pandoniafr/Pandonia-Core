package fr.pandonia.core.listeners.manager;

import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.listeners.cosmetics.ArrowListener;
import fr.pandonia.core.listeners.menu.MenuListener;
import fr.pandonia.core.listeners.packets.PacketListeners;
import fr.pandonia.core.listeners.player.*;
import fr.pandonia.core.listeners.playerequiparmor.ArmorListener;
import fr.pandonia.core.rank.RankTagListeners;

import java.util.ArrayList;

public class ListenersManager {

    private PandoniaCore instance;

    public ListenersManager(PandoniaCore instance) {
        this.instance = instance;
    }

    public void register(){

        //PLAYERS
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PlayerCooldownListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PlayerChatListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PlayerCommandListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PlayerCraftListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PlayerInteractListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PlayerNickListener(), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new ArrowListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new RankTagListeners(instance), instance.getPlugin());

        //AUTRES
        instance.getPlugin().getServer().getPluginManager().registerEvents(new PacketListeners(), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(new MenuListener(instance), instance.getPlugin());
        instance.getPlugin().getServer().getPluginManager().registerEvents(instance.getNPCManager(), instance.getPlugin());

    }

}
