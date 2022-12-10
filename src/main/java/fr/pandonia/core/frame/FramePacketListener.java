package fr.pandonia.core.frame;

import fr.pandonia.api.events.packets.PacketWriteEvent;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.tools.npc.utils.Reflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class FramePacketListener implements Listener {

    @EventHandler
    public void onPacket(PacketWriteEvent e){
        if(e.getPacket() instanceof PacketPlayOutSpawnEntity){
            int entityType = Reflection.getField(PacketPlayOutSpawnEntity.class, "j", int.class).get(e.getPacket());
            if(entityType == 71){
                //Spawn d'un Item Frame
                int entityID = Reflection.getField(PacketPlayOutSpawnEntity.class, "a", int.class).get(e.getPacket());
                Frame frame = PandoniaCore.getCore().getImageMapManager().getFrame(entityID);

                if(frame != null){
                    Bukkit.getScheduler().runTaskLater(PandoniaCore.get().getPlugin(), () -> frame.sendTo(e.getPlayer()), 10L);
                }

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        if(event.getRightClicked() instanceof ItemFrame){
            Frame frame = PandoniaCore.getCore().getImageMapManager().getFrame(event.getRightClicked().getEntityId());
            if(frame != null){
                Bukkit.getScheduler().runTask(PandoniaCore.get().getPlugin(), () -> frame.sendTo(event.getPlayer()));
                event.setCancelled(true);
            }
        }
    }

    private BlockFace convertDirectionToBlockFace(int direction) {
        switch(direction) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.EAST;
            default:
                return BlockFace.NORTH;
        }
    }

}
