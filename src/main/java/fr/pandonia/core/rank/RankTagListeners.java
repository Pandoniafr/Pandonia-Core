package fr.pandonia.core.rank;

import com.mojang.authlib.GameProfile;
import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.events.CustomJoinEvent;
import fr.pandonia.api.events.PlayerGetDisplayNameEvent;
import fr.pandonia.api.events.PlayerRankUpdateEvent;
import fr.pandonia.api.events.packets.PacketWriteEvent;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.rank.IRank;
import fr.pandonia.api.tablist.manager.ITeamTagManager;
import fr.pandonia.tools.npc.utils.Reflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.List;

public class RankTagListeners implements Listener {

    private PandoniaAPI instance;
    private ITeamTagManager teamTagManager;
    private boolean init;

    public RankTagListeners(PandoniaAPI instance) {
        this.instance = instance;
        this.teamTagManager = PandoniaAPI.get().getTeamTagManager();
        this.init = false;
    }

    public void init(){
        teamTagManager.createView("rank");
        this.init = true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPacket(PacketWriteEvent e){
        if(e.getPacket() instanceof PacketPlayOutPlayerInfo){
            PacketPlayOutPlayerInfo packet = (PacketPlayOutPlayerInfo) e.getPacket();
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction action = Reflection.getField(packet.getClass(), "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.class).get(packet);
            if(action.equals(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER)){
                IPPlayer pp = PandoniaAPI.get().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
                if(pp != null){
                    try {
                        Field field = packet.getClass().getDeclaredField("b");
                        field.setAccessible(true);
                        List<PacketPlayOutPlayerInfo.PlayerInfoData> list = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) field.get(packet);
                        for (PacketPlayOutPlayerInfo.PlayerInfoData playerInfoData : list) {
                            IPPlayer player = PandoniaAPI.get().getPlayerManager().getPlayer(playerInfoData.a().getId());
                            if(player != null){
                                PlayerGetDisplayNameEvent event = new PlayerGetDisplayNameEvent(e.getPlayer(), pp, player);
                                Bukkit.getPluginManager().callEvent(event);
                                GameProfile gameProfile = new GameProfile(player.getUUID(), event.getDisplayName());
                                Field map = gameProfile.getClass().getDeclaredField("properties");
                                Field legacy = gameProfile.getClass().getDeclaredField("properties");
                                map.setAccessible(true);
                                legacy.setAccessible(true);
                                map.set(gameProfile, map.get(playerInfoData.a()));
                                legacy.set(gameProfile, legacy.get(playerInfoData.a()));
                                Reflection.getField(playerInfoData.getClass(), "d", GameProfile.class).set(playerInfoData, gameProfile);
                            }
                        }
                    } catch (IllegalAccessException | NoSuchFieldException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                }
                ;
            }
        }
    }

    @EventHandler
    public void onJoin(CustomJoinEvent e){
        if(!init){
            init();
        }
        IPPlayer player = PandoniaAPI.get().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if(player != null){
            IRank rank = player.getRankObject(PandoniaAPI.get().getRankManager());
            teamTagManager.getView("rank").setNameTag(player.getName(), player.getPrefix(PandoniaAPI.get().getRankManager()), "", rank.getPower());
            if(player.getNick() != null){
                teamTagManager.getView("rank").setNameTag(player.getDisplayName(), player.getDisplayPrefix(PandoniaAPI.get().getRankManager()), "", player.getDisplayRankObject(PandoniaAPI.get().getRankManager()).getPower());
            }
        }
    }

    @EventHandler
    public void onRankChange(PlayerRankUpdateEvent e){
        IPPlayer player = PandoniaAPI.get().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if(player != null){
            IRank rank = player.getDisplayRankObject(PandoniaAPI.get().getRankManager());
            teamTagManager.getView("rank").setNameTag(player.getDisplayName(), player.getDisplayPrefix(PandoniaAPI.get().getRankManager()), "", rank.getPower());
        }
    }

}
