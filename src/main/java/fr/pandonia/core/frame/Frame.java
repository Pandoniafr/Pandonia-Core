package fr.pandonia.core.frame;

import fr.pandonia.core.images.FakeMapCanvas;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItemFrame;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.map.RenderData;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Frame {
    private final int id;
    private ItemFrame entity;
    private final BlockFace face;
    private final Location loc;
    private BufferedImage image;
    private PacketPlayOutEntityMetadata cachedItemPacket = null;
    private PacketPlayOutMap cachedDataPacket = null;

    public Frame(int id, BufferedImage image, Location loc, BlockFace face) {
        this.id = id;
        this.image = image;
        this.loc = loc;
        this.face = face;
    }

    public boolean isLoaded() {
        return this.entity != null;
    }

    public int getId() {
        return this.id;
    }

    public short getMapId() {
        return (short)(2300 + this.id);
    }

    public Location getLocation() {
        return this.loc;
    }

    public ItemFrame getEntity() {
        return this.entity;
    }

    public BlockFace getFacing() {
        return this.face;
    }

    public void setEntity(ItemFrame entity) {
        this.entity = entity;
        this.cachedItemPacket = null;
    }

    public void clearCache() {
        this.cachedDataPacket = null;
        this.cachedItemPacket = null;
    }

    public BufferedImage getBufferImage() {
        return image;
    }

    private RenderData getRenderData() {
        RenderData render = new RenderData();
        MapRenderer mapRenderer = this.generateRenderer();
        Arrays.fill(render.buffer, (byte)0);
        render.cursors.clear();
        FakeMapCanvas canvas = new FakeMapCanvas();
        canvas.setBase(render.buffer);
        mapRenderer.render(canvas.getMapView(), canvas, (Player)null);
        byte[] buf = canvas.getBuffer();

        for(int i = 0; i < buf.length; ++i) {
            byte color = buf[i];
            if (color >= 0 || color <= -113) {
                render.buffer[i] = color;
            }
        }

        return render;
    }

    public void sendTo(Player player) {
        this.sendItemMeta(player);
        this.sendMapData(player);
    }

    private void sendItemMeta(Player player) {
        if (this.isLoaded()) {
            if (this.cachedItemPacket == null) {
                EntityItemFrame entity = ((CraftItemFrame)this.entity).getHandle();
                ItemStack item = new ItemStack(Material.MAP);
                item.setDurability(this.getMapId());
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                nmsItem.count = 1;
                nmsItem.a(entity);
                DataWatcher watcher = new DataWatcher(entity);
                watcher.a(8, nmsItem);
                watcher.a(3, (byte)0);
                watcher.update(8);
                this.cachedItemPacket = new PacketPlayOutEntityMetadata(entity.getId(), watcher, false);
            }

            if (player != null) {
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(this.cachedItemPacket);
            }

        }
    }

    private void sendMapData(Player player) {
        if (this.cachedDataPacket == null) {
            RenderData data = this.getRenderData();
            this.cachedDataPacket = new PacketPlayOutMap(this.getMapId(), (byte)3, new ArrayList(), data.buffer, 0, 0, 128, 128);
        }

        if (player != null) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(cachedDataPacket);
        }

    }

    public MapRenderer generateRenderer() {
        BufferedImage image = this.getBufferImage();
        if (image != null)  {
            return new ImageRenderer(image);
        }
        return null;
    }
}
