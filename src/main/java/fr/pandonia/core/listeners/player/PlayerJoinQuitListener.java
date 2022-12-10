package fr.pandonia.core.listeners.player;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.events.CustomJoinEvent;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.manager.IPlayerManager;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.player.PPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerJoinQuitListener implements Listener {

    private PandoniaAPI instance;

    public PlayerJoinQuitListener(PandoniaAPI instance){
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e){
        PandoniaMessages.debug("Event: PlayerJoinEvent " + e.getPlayer().getName() + " " + PandoniaMessages.getActualDate());
        e.setJoinMessage(null);

        /*UUID player = e.getPlayer().getUniqueId();

        TempPlayer tempPlayer = new TempPlayer(player);

        InstanceSettings settings = instance.getInstanceSettings();

        instance.getPlayerManager().addTempPlayer(tempPlayer);
        instance.getProxyLink().getAccount(player, settings.isGetOwning(), settings.isGetSettings(), settings.isGetStats(), settings.isGetBattlePass(), settings.isGetFriends(), settings.isGetGuilds());

        Bukkit.getScheduler().scheduleSyncDelayedTask(instance.getPlugin(), () -> {
            if (!tempPlayer.isLoaded()){
                e.getPlayer().kickPlayer("§f§m-------------------\n\n§3Une erreur est survenue...\n§fVeuillez patienter.\n§fERREUR A01\n\n§f§m-------------------");
            }
            instance.getPlayerManager().removeTempPlayer(player);
        }, 30);*/
        BukkitTask task = new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(PandoniaAPI.get().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) == null){
                    i++;
                }else{
                    Bukkit.getPluginManager().callEvent(new CustomJoinEvent(e.getPlayer(), PandoniaAPI.get().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()), PandoniaAPI.get().getPlayerSettingsManager().getPlayerSettings(e.getPlayer().getUniqueId()), PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(e.getPlayer().getUniqueId())));
                    cancel();
                }
                if(i > 30){
                    e.getPlayer().kickPlayer("§f§m-------------------\n\n§3Une erreur est survenue...\n§fVeuillez patienter.\n§fERREUR A02\n\n§f§m-------------------");
                    cancel();
                }
            }
        }.runTaskTimer(instance.getPlugin(), 0, 1);
    }

    @EventHandler
    public void onCustomJoin(CustomJoinEvent e){
        PandoniaMessages.debug("Event: CustomJoinEvent " + e.getPlayer().getName() + " " + PandoniaMessages.getActualDate());

        Player p = e.getPlayer();
        PPlayer pp = (PPlayer) instance.getPlayerManager().getPlayer(p.getUniqueId());
        if (pp != null){
            if(pp.getNick() != null){
                PandoniaCore.getCore().getNickManager().updateNick(p, false, true);
            }
            Bukkit.getScheduler().runTaskLater(instance.getPlugin(), () -> {
                instance.getCosmeticsManager().onJoin(pp);
                PandoniaCore.getCore().getScoreboardManager().onLogin(p);
                if (instance.getNPCManager().isEnabled()){
                    instance.getNPCManager().onJoin(p);
                }
            }, 5);

        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e){
        PandoniaMessages.debug("Event: PlayerQuitEvent " + e.getPlayer().getName() + " " + PandoniaMessages.getActualDate());

        Player p = e.getPlayer();
        IPlayerManager manager = instance.getPlayerManager();
        IPPlayer player = manager.getPlayer(p.getUniqueId());

        if(player != null){
            if (!player.isSaved()){
                manager.savePlayer(player.getUUID());
            }

            manager.removeAllPlayer(player.getUUID());
        }
        instance.getFriendsManager().onQuit(p);
        instance.getCosmeticsManager().onQuit(p.getUniqueId());
        PandoniaCore.getCore().getScoreboardManager().onLogout(e.getPlayer());

    }

}
