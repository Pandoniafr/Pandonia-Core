package fr.pandonia.core.player.nick.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.events.PlayerRankUpdateEvent;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.nick.INick;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.tools.MojangAPIUtil;
import fr.pandonia.tools.npc.utils.Reflection;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class NickManager {

    private PandoniaCore instance;

    public NickManager(PandoniaCore instance) {
        this.instance = instance;
    }

    public void updateNick(Player p, boolean updateSkin, boolean login){
        IPPlayer pp = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        if(pp != null){
            INick nick = pp.getNick();
            EntityPlayer ep = ((CraftPlayer)p).getHandle();
            PlayerConnection connection = ep.playerConnection;
            String value = null;
            String signature = null;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep));
            GameProfile profile = ep.getProfile();
            if(nick != null){
                if(nick.getNick() != null){
                    Reflection.getField(GameProfile.class, "name", String.class).set(profile, nick.getNick());
                }
                if(nick.getSkinName() != null){
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(nick.getSkinName());
                    if(offlinePlayer != null){
                        MojangAPIUtil.Result<MojangAPIUtil.SkinData> data = MojangAPIUtil.getSkinData(offlinePlayer.getUniqueId());
                        if(data.wasSuccessful()){
                            if(data.getValue().getBase64() != null && data.getValue().getSignedBase64() != null){
                                value = data.getValue().getBase64();
                                signature = data.getValue().getSignedBase64();
                                profile.getProperties().removeAll("textures");
                                profile.getProperties().put("textures", new Property("textures", value, signature));
                            }
                        }
                    }
                }
            }else{
                Reflection.getField(GameProfile.class, "name", String.class).set(profile, pp.getName());
                MojangAPIUtil.Result<MojangAPIUtil.SkinData> data = MojangAPIUtil.getSkinData(p.getUniqueId());
                if(data.wasSuccessful()){
                    if(data.getValue().getBase64() != null && data.getValue().getSignedBase64() != null){
                        value = data.getValue().getBase64();
                        signature = data.getValue().getSignedBase64();
                        profile.getProperties().removeAll("textures");
                        profile.getProperties().put("textures", new Property("textures", value, signature));
                    }
                }
            }
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep));
            Location loc = p.getLocation();
            double maxHEalth = p.getMaxHealth();
            boolean flying = p.isFlying();
            Vector v = p.getVelocity();
            int slot = p.getInventory().getHeldItemSlot();
            if(!login){
                connection.sendPacket(new PacketPlayOutRespawn(ep.dimension, ep.getWorld().getDifficulty(), ep.getWorld().getWorldData().getType(), ep.playerInteractManager.getGameMode()));
            }
            Bukkit.getScheduler().runTaskLater(instance.getPlugin(), () -> {
                if(!login){
                    p.teleport(loc);
                    p.setFlying(flying);
                    p.setVelocity(v);
                    p.updateInventory();
                    p.setMaxHealth(maxHEalth);
                    p.getInventory().setHeldItemSlot(slot);
                }
                if(!login || !PandoniaAPI.get().getInstanceInfo().getServer().getType().equals("hub")){
                    Bukkit.getScheduler().runTaskLater(instance.getPlugin(), () -> {
                        if(nick != null){
                            if(nick.getRankNick() != null){
                                Bukkit.getPluginManager().callEvent(new PlayerRankUpdateEvent(p, pp.getRankObject(PandoniaAPI.get().getRankManager()), PandoniaAPI.get().getRankManager().getRank(nick.getRankNick())));
                            }
                        }else{
                            Bukkit.getPluginManager().callEvent(new PlayerRankUpdateEvent(p, instance.getRankManager().getRank("JOUEUR"), pp.getRankObject(instance.getRankManager())));
                        }
                        Bukkit.getOnlinePlayers().stream().filter(player -> player.canSee(p)).forEach(player -> {
                            IPPlayer target = PandoniaAPI.get().getPlayerManager().getPlayer(player.getUniqueId());
                            if(target != null){
                                player.hidePlayer(p);
                                Bukkit.getScheduler().runTaskLater(instance.getPlugin(), () -> player.showPlayer(p), 1);
                            }
                        });
                    }, 1);
                }
            }, 5);
            if(updateSkin && value != null && signature != null){
                instance.getProxyLink().updatePlayerNick(pp, value, signature);
            }
        }
    }

    public void updateNick(Player p, boolean updateSkin){
        updateNick(p, updateSkin, false);
    }
}
