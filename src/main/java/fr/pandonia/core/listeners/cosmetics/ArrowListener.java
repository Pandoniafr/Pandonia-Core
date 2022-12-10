package fr.pandonia.core.listeners.cosmetics;

import fr.pandonia.api.cosmetics.arrow.IArrow;
import fr.pandonia.api.player.settings.IPlayerSettings;
import fr.pandonia.core.PandoniaCore;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ArrowListener implements Listener {

    private final PandoniaCore instance;
    private final Map<UUID, BukkitTask> launchs;
    private final List<UUID> criticals;

    public ArrowListener(PandoniaCore instance) {
        this.instance = instance;
        this.launchs = new HashMap<>();
        this.criticals = new ArrayList<>();
    }

    @EventHandler
    public void onProjectilLaunch(ProjectileLaunchEvent e){
        if(e.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) e.getEntity();
            if(arrow.getShooter() instanceof Player){
                Player p = (Player) arrow.getShooter();
                IPlayerSettings playerSettings = instance.getPlayerSettingsManager().getPlayerSettings(p.getUniqueId());
                if(playerSettings != null && playerSettings.getActiveArrowID() != 0){
                    if(arrow.isCritical()){
                        criticals.add(arrow.getUniqueId());
                        arrow.setCritical(false);
                    }
                    IArrow iArrow = instance.getCosmeticsManager().getArrow(playerSettings.getActiveArrowID());
                    if(iArrow != null){
                        EnumParticle particle = EnumParticle.valueOf(iArrow.getParticle());
                        BukkitTask bukkitTask = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(arrow.getLocation() != null){
                                    Location loc = arrow.getLocation();
                                    for (IPlayerSettings settings : instance.getPlayerSettingsManager().getPlayerSettingsList()) {
                                        Player show = Bukkit.getPlayer(settings.getPlayerUUID());
                                        if(settings.isParticles() && show != null && show.getWorld().equals(loc.getWorld()) && show.getLocation().distance(loc) < 50){
                                            Random r = new Random();
                                            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true,  (float) loc.getX(), (float)  loc.getY(), (float)  loc.getZ(), (float) r.nextInt(255)/255, (float) r.nextInt(255)/255, (float) r.nextInt(255)/255, 1f, 0);
                                            ((CraftPlayer)show).getHandle().playerConnection.sendPacket(packet);
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(instance.getPlugin(), 0, 1);
                        launchs.put(arrow.getUniqueId(), bukkitTask);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectilHit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Arrow){
            if(criticals.remove(e.getEntity().getUniqueId())){
                ((Arrow)e.getEntity()).setCritical(true);
            }
            if(launchs.containsKey(e.getEntity().getUniqueId())){
                launchs.remove(e.getEntity().getUniqueId()).cancel();
            }
        }
    }

}
