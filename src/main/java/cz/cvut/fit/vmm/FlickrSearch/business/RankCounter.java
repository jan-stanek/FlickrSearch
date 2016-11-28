package cz.cvut.fit.vmm.FlickrSearch.business;

import cz.cvut.fit.vmm.FlickrSearch.entity.Color;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                photo.setRanks(countRanks(photo));
            } catch (Exception e) {
                photo.setRanks(null);
            }
        }
    }

    private Map<Metric, Double> countRanks(Photo photo) throws Exception {
        if (color == null)
            return null;

        Map<Metric, Double> res = new HashMap<Metric, Double>();

        BufferedImage bufferedImage = ImageIO.read(new URL(photo.getThumbUrl()));

        double[] distances = new double[Metric.values().length];

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelColor = bufferedImage.getRGB(x, y);
                Color c = new Color((pixelColor & 0x00ff0000) >> 16, (pixelColor & 0x0000ff00) >> 8, pixelColor & 0x000000ff);
                for (Metric metric : Metric.values()) {
                    distances[metric.ordinal()] += c.distanceTo(color, metric);
                }
            }
        }

        for (Metric metric : Metric.values()) {
            res.put(metric, distances[metric.ordinal()] / (width * height));
        }

        return res;
    }
}
