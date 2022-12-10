package fr.pandonia.core.images.tasks;

import fr.pandonia.api.images.tasks.ITaskRenderImage;
import fr.pandonia.core.images.FakeMapCanvas;
import fr.pandonia.core.images.ImageMapRenderer;
import fr.pandonia.tools.images.ImageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.map.RenderData;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class TaskRenderImage extends BukkitRunnable implements ITaskRenderImage {

    private JavaPlugin instance;
    private BufferedImage image;
    private World world;
    private Location topLeftCorner;
    private BlockFace blockFace;
    private int startID;

    public TaskRenderImage(JavaPlugin instance, String path, World world, Location topLeftCorner, BlockFace blockFace){
        this.instance = instance;
        this.world = world;
        this.topLeftCorner = topLeftCorner;
        this.blockFace = blockFace;
        this.startID = 0;
        try {
            image = ImageUtils.getImage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TaskRenderImage(JavaPlugin instance, BufferedImage image, World world, Location topLeftCorner, BlockFace blockFace, int startID){
        this.instance = instance;
        this.image = image;
        this.world = world;
        this.topLeftCorner = topLeftCorner;
        this.blockFace = blockFace;
        this.startID = startID;
    }

    @Override
    public void run() {

        final ArrayList<Short> mapsIds = new ArrayList<>();

        final int rows = image.getHeight() / 128;
        final int cols = image.getWidth() / 128;

        MapView map;
        HashMap<Integer, Block> itemFrames = new HashMap<>();
        int index = 0;
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                map = Bukkit.createMap(world);
                resetRenderers(map);
                map.setScale(MapView.Scale.FARTHEST);

                ImageMapRenderer imageMapRenderer = new ImageMapRenderer(image.getSubimage(j * 128, i * 128, 128, 128));
                map.addRenderer(imageMapRenderer);

                RenderData render = new RenderData();
                Arrays.fill(render.buffer, (byte)0);
                render.cursors.clear();
                FakeMapCanvas canvas = new FakeMapCanvas();
                canvas.setBase(render.buffer);
                imageMapRenderer.render(canvas.getMapView(), canvas, null);
                byte[] buf = canvas.getBuffer();

                for(int h = 0; h < buf.length; ++h) {
                    byte color = buf[h];
                    if (color >= 0 || color <= -113) {
                        render.buffer[h] = color;
                    }
                }



                mapsIds.add(map.getId());

                Location location = null;

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

                location = new Location(world, x, y, z);

                itemFrames.put(index, location.getBlock());

                index++;

            }
        }

        for (Map.Entry<Integer, Block> block : itemFrames.entrySet()){
            ItemFrame itemFrame;

            itemFrame = world.spawn(block.getValue().getLocation(), ItemFrame.class);
            itemFrame.setFacingDirection(blockFace, false);

            itemFrame.setItem(new ItemStack(Material.MAP, 1, mapsIds.get(block.getKey())));

        }

    }



    public void resetRenderers(MapView map){
        final Iterator<MapRenderer> iterator = map.getRenderers().iterator();

        while (iterator.hasNext()){
            map.removeRenderer(iterator.next());
        }
    }

}
