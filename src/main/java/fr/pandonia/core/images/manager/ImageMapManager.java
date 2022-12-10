package fr.pandonia.core.images.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.images.manager.IImageMapManager;
import fr.pandonia.core.frame.Frame;
import fr.pandonia.core.frame.FramePacketListener;
import fr.pandonia.core.images.tasks.NewTaskRenderImage;
import fr.pandonia.tools.images.ImageUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageMapManager implements IImageMapManager, Listener {

    private PandoniaAPI instance;
    private List<Frame> frames;

    public ImageMapManager(PandoniaAPI instance) {
        this.instance = instance;
        this.frames = new ArrayList<>();
        instance.getPlugin().getServer().getPluginManager().registerEvents(new FramePacketListener(), instance.getPlugin());
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public Frame getFrame(int entityID){
        return frames.stream().filter(frame -> frame.getEntity() != null && frame.getEntity().getEntityId() == entityID).findFirst().orElse(null);
    }

    public int getNewFrameID() {
        //int highestId = new Random().nextInt(Short.MAX_VALUE);
        int highestId = PandoniaAPI.get().getInstanceInfo().getServerSettings().getFrameID();
        for (Frame frame : frames) {
            if(frame.getMapId() == highestId){
                highestId++;
            }
        }
        return highestId + 1;
    }

    @Override
    public void drawImage(String path, Location topLeftCorner, BlockFace blockFace){
        try {
            new NewTaskRenderImage(instance.getPlugin(), ImageUtils.getImage(path), topLeftCorner.getWorld(), topLeftCorner, blockFace).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawImage(BufferedImage image, Location topLeftCorner, BlockFace blockFace){
        new NewTaskRenderImage(instance.getPlugin(), image, topLeftCorner.getWorld(), topLeftCorner, blockFace).run();
    }

    public void sendFrame(Frame frame) {
        ItemFrame entity = frame.getEntity();
        WorldServer worldServer = ((CraftWorld)entity.getWorld()).getHandle();
        EntityTracker tracker = worldServer.tracker;
        EntityTrackerEntry trackerEntry = tracker.trackedEntities.d(entity.getEntityId());
        if (trackerEntry != null) {
            for (EntityPlayer playerNMS : trackerEntry.trackedPlayers) {
                Player player = playerNMS.getBukkitEntity();
                frame.sendTo(player);
            }
        }
    }




}
