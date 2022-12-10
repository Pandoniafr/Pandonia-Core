package fr.pandonia.core.listeners.player;

import fr.pandonia.api.PandoniaAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractListener implements Listener {

    private PandoniaAPI instance;

    public PlayerInteractListener(PandoniaAPI instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e){
        if (e.getRightClicked() != null){
            if (e.getRightClicked().getType() == EntityType.ITEM_FRAME){
                e.setCancelled(true);
            }
        }
    }

}
