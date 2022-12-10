package fr.pandonia.core.listeners.player;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class PlayerCraftListener implements Listener {

    private PandoniaAPI instance;

    public PlayerCraftListener(PandoniaAPI instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        if (e.getRecipe() != null){
            if (e.getRecipe().getResult() != null){
                if (e.getRecipe().getResult().getType() != null){
                    if (e.getRecipe().getResult().getType().equals(Material.BEACON)){
                        e.setCancelled(true);
                        if (e.getWhoClicked() instanceof Player){
                            e.getWhoClicked().sendMessage(PandoniaMessages.getPrefix() + "§fLe craft du §3beacon §fest §cdésactivé§f!");
                        }
                    }
                }
            }
        }
    }
}
