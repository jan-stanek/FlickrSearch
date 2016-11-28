package cz.cvut.fit.vmm.FlickrSearch.entity;

import cz.cvut.fit.vmm.FlickrSearch.business.Metric;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Photo implements Serializable {
    private String id;
    private String title;
    private String thumbUrl;
    private String squareUrl;
    private String imageUrl;
    private Map<Metric, Double> ranks;

    public Photo(String id, String title, String thumbUrl, String squareUrl, String imageUrl) {
        this.id = id;
        this.title = title;
        this.squareUrl = squareUrl;
        this.thumbUrl = thumbUrl;
        this.imageUrl = imageUrl;
        this.ranks = new HashMap<Metric, Double>();
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

    public double getRank(Metric metric) {
        return ranks.get(metric);
    }

    public String getSquareUrl() {
        return squareUrl;
    }

    public void setRank(Metric metric, double rank) {
        ranks.put(metric, rank);
    }

    public void setRanks(Map<Metric, Double> ranks) {
        this.ranks = ranks;
    }

    public boolean hasRanks() {
        return ranks != null;
    }

}
