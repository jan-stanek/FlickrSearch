package cz.cvut.fit.vmm.FlickrSearch.entity;

import java.io.Serializable;


public class Photo implements Serializable {
    private String id;
    private String title;
    private String thumbUrl;
    private String squareUrl;
    private String imageUrl;
    private double rank;

    public Photo(String id, String title, String thumbUrl, String squareUrl, String imageUrl) {
        this.id = id;
        this.title = title;
        this.squareUrl = squareUrl;
        this.thumbUrl = thumbUrl;
        this.imageUrl = imageUrl;
        this.rank = 0;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getRank() {
        return rank;
    }

    public String getSquareUrl() {
        return squareUrl;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}
