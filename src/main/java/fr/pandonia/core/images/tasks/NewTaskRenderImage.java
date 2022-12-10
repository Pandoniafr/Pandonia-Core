package fr.pandonia.core.images.tasks;

import fr.pandonia.api.images.tasks.ITaskRenderImage;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.frame.Frame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.image.BufferedImage;

public class NewTaskRenderImage extends BukkitRunnable implements ITaskRenderImage {

    private JavaPlugin instance;
    private BufferedImage image;
    private World world;
    private Location topLeftCorner;
    private BlockFace blockFace;

    public NewTaskRenderImage(JavaPlugin instance, BufferedImage image, World world, Location topLeftCorner, BlockFace blockFace){
        this.instance = instance;
        this.image = image;
        this.world = world;
        this.topLeftCorner = topLeftCorner;
        this.blockFace = blockFace;
    }

    @Override
    public void run() {
        System.out.println("new task");
        final int rows = image.getHeight() / 128;
        final int cols = image.getWidth() / 128;
        int id = PandoniaCore.getCore().getImageMapManager().getNewFrameID();
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){

                double x = topLeftCorner.getX();
                double y = topLeftCorner.getY()-i;
                double z = topLeftCorner.getZ();

                if (blockFace == BlockFace.NORTH){
                    x = x - j;
                    z = z -1;
                }else if (blockFace == BlockFace.SOUTH){
                    x = x + j;
                    z = z + 1;
                }else if (blockFace == BlockFace.EAST){
                    z = z - j;
                    x = x + 1;
                }else if (blockFace == BlockFace.WEST){
                    z = z + j;
                    x = x - 1;
                }

                Location location = new Location(world, x, y, z);

                ItemFrame itemFrame = world.spawn(location, ItemFrame.class);
                itemFrame.setFacingDirection(blockFace, false);

                Frame frame = new Frame(id++, image.getSubimage(j * 128, i * 128, 128, 128), location, blockFace);
                frame.setEntity(itemFrame);
                PandoniaCore.getCore().getImageMapManager().getFrames().add(frame);

                Bukkit.getScheduler().runTaskLater(PandoniaCore.get().getPlugin(), () -> PandoniaCore.getCore().getImageMapManager().sendFrame(frame), 1);
            }
        }

    }

}
