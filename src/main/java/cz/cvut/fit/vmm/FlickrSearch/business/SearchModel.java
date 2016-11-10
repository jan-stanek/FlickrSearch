package cz.cvut.fit.vmm.FlickrSearch.business;

import java.io.Serializable;


public class SearchModel implements Serializable {

    private String tags;
    private String color;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
