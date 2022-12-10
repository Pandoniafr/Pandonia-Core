package fr.pandonia.core.listeners.player;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.core.player.PPlayer;
import fr.pandonia.tools.npc.api.events.NPCInteractEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCooldownListener implements Listener {

    private PandoniaAPI instance;

    public PlayerCooldownListener(PandoniaAPI instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEvent(PlayerCommandPreprocessEvent e){
        PPlayer pp = (PPlayer) instance.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if (pp != null){
            if (pp.getCooldown() > 0){
                ((Cancellable) e).setCancelled(true);
                e.getPlayer().sendMessage(PandoniaMessages.getPrefix() + "§fOula... Tu vas un peu vite !");
            }else{
                pp.setCooldown(2);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(NPCInteractEvent e){
        PPlayer pp = (PPlayer) instance.getPlayerManager().getPlayer(e.getWhoClicked().getUniqueId());
        if (pp != null){
            if (pp.getCooldown() > 0){
                ((Cancellable) e).setCancelled(true);
                e.getWhoClicked().sendMessage(PandoniaMessages.getPrefix() + "§fOula... Tu vas un peu vite !");
            }else{
                pp.setCooldown(2);
            }
        }
    }

}
