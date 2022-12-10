package fr.pandonia.core.images;

import fr.pandonia.api.images.IImageMapRenderer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class ImageMapRenderer extends MapRenderer implements IImageMapRenderer {

    private boolean shouldRender;
    private BufferedImage image;

    public ImageMapRenderer(BufferedImage image) {
        this.shouldRender = true;
        this.image = image;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (shouldRender){
            mapCanvas.drawImage(0, 0, image);
            shouldRender = false;
        }
    }

}
