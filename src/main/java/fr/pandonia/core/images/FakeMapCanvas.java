package fr.pandonia.core.images;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.map.CraftMapCanvas;
import org.bukkit.craftbukkit.v1_8_R3.map.CraftMapView;

import java.lang.reflect.Field;

public class FakeMapCanvas extends CraftMapCanvas {
    public FakeMapCanvas() {
        super((CraftMapView)null);
    }

    public byte[] getBuffer() {
        return super.getBuffer();
    }

    public void setBase(byte[] base) {
        super.setBase(base);
    }

    public CraftMapView getMapView() {
        return (CraftMapView)((CraftMapView)(Bukkit.getMap((short)0) == null ? Bukkit.createMap((World)Bukkit.getWorlds().get(0)) : Bukkit.getMap((short)0)));
    }

    public void setPixel(int x, int y, byte color) {
        Field field;
        byte[] buffer;
        try {
            field = CraftMapCanvas.class.getDeclaredField("buffer");
            field.setAccessible(true);
            buffer = (byte[])((byte[])field.get(this));
        } catch (Exception var8) {
            var8.printStackTrace();
            return;
        }

        if (x >= 0 && y >= 0 && x < 128 && y < 128) {
            if (buffer[y * 128 + x] != color) {
                buffer[y * 128 + x] = color;
            }

            try {
                field.set(this, buffer);
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        }
    }
}