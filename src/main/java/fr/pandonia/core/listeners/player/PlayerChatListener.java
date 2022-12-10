package fr.pandonia.core.listeners.player;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.settings.IPlayerSettings;
import fr.pandonia.api.rank.IRank;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerChatListener implements Listener {

    private PandoniaAPI instance;
    private Map<Player, String> lastMsg = new HashMap<>();

    public PlayerChatListener(PandoniaAPI instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void defaultFormat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();

        IPPlayer pPlayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        IRank rank = pPlayer.getDisplayRankObject(instance.getRankManager());

        e.setFormat(pPlayer.getDisplayPrefix(instance.getRankManager()) + pPlayer.getDisplayName() + " §8➤ " + (rank.getPower() >= 25 ? "§f" : "§7") + e.getMessage().replace("%", "%%"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {

        if(e.getMessage().startsWith("!link")){
            e.getPlayer().sendMessage("§3§lDISCORD §8» §rVous devez envoyer cette commande dans le channel §7#┋bots §rsur le §3Discord de Pandonia§r.");
            e.setCancelled(true);
            return;
        }

        Player p = e.getPlayer();
        IPPlayer pPlayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        final String last = lastMsg.get(p);

        if (pPlayer.getRankObject(instance.getRankManager()).getPower() < instance.getRankManager().getRank("STAFF").getPower()) {

            if (e.getMessage().contains("http://") || e.getMessage().contains("https://") ||  e.getMessage().contains(".fr") ||  e.getMessage().contains(".com") ||  e.getMessage().contains(".net")) {

                p.sendMessage(PandoniaMessages.getPrefix() + "§fVous n'avez pas le §3droit§f d'envoyer des liens");
                e.setCancelled(true);

            } else if (last != null && last.equals(e.getMessage())) {

                p.sendMessage(PandoniaMessages.getPrefix() + "§fVous avez déjà envoyé ce message");
                e.setCancelled(true);

            }
            lastMsg.put(p, e.getMessage());
        }

        if(!e.isCancelled()){
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(e.getFormat().toLowerCase().contains(player.getName().toLowerCase()) && !e.getPlayer().equals(player)){
                    IPlayerSettings playerSettings = PandoniaAPI.get().getPlayerSettingsManager().getPlayerSettings(player.getUniqueId());
                    if(playerSettings.isMentions()){
                        int index = e.getFormat().lastIndexOf("§");
                        String color = e.getFormat().substring(index, index+2);
                        player.sendMessage(e.getFormat().replaceAll("(?i)" + player.getName(), "§e@" + player.getName() + color));
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                    }else{
                        player.sendMessage(e.getFormat());
                    }
                }else{
                    player.sendMessage(e.getFormat());
                }
            }
            e.setCancelled(true);
        }

    }
}
