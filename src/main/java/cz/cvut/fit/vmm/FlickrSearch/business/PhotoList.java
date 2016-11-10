package cz.cvut.fit.vmm.FlickrSearch.business;

import cz.cvut.fit.vmm.FlickrSearch.data.entity.Color;
import cz.cvut.fit.vmm.FlickrSearch.data.entity.Photo;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jan on 08.11.2016.
 */
public class PhotoList {
    private ArrayList<Photo> photos;
    private Color color;

    public PhotoList(Color color) {
        this.photos = new ArrayList<Photo>();
        this.color = color;
    }

    public void add(Photo photo) {
        photo.setRank(countRank(photo));
        photos.add(photo);
    }

    public void sort() {
        Collections.sort(photos, new Comparator<Photo>() {
            public int compare(final Photo p1, final Photo p2) {
                return (int) (p1.getRank() - p2.getRank());
            }
        });
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    private double countRank(Photo photo) {
        if (color == null)
            return 0;

        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(photo.getThumbUrl()));

            double distance = 0;

            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    int color = bufferedImage.getRGB(x, y);
                    Color c = new Color((color & 0x00ff0000) >> 16, (color & 0x0000ff00) >> 8, color & 0x000000ff);
                    distance += c.distanceTo(this.color);
                }
            }

            return distance / (bufferedImage.getWidth() * bufferedImage.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
