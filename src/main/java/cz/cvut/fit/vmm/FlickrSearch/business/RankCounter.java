package cz.cvut.fit.vmm.FlickrSearch.business;

import cz.cvut.fit.vmm.FlickrSearch.entity.Color;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

/**
 * Created by jan on 16.11.2016.
 */
public class RankCounter implements Runnable {

    List<Photo> photos;
    Color color;

    public RankCounter(List<Photo> photos, Color color) {
        this.photos = photos;
        this.color = color;
    }

    public void run() {
        for (Photo photo : photos) {
            try {
                photo.setRank(countRank(photo));
            } catch (Exception e) {
                photo.setRank(Double.NaN);
            }
        }
    }

    private double countRank(Photo photo) throws Exception {
        if (color == null)
            return 0;

        BufferedImage bufferedImage = ImageIO.read(new URL(photo.getThumbUrl()));

        double distance = 0;

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int pixelColor = bufferedImage.getRGB(x, y);
                Color c = new Color((pixelColor & 0x00ff0000) >> 16, (pixelColor & 0x0000ff00) >> 8, pixelColor & 0x000000ff);
                double dist = c.distanceTo(color);
                distance += dist;
            }
        }

        return distance / (bufferedImage.getWidth() * bufferedImage.getHeight());
    }
}
