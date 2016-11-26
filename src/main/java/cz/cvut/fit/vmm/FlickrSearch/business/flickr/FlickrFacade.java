package cz.cvut.fit.vmm.FlickrSearch.business.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.SearchParameters;
import cz.cvut.fit.vmm.FlickrSearch.business.PhotoFacade;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlickrFacade implements PhotoFacade {

    private Flickr flickr;

    public FlickrFacade(Properties config) {
        String apiKey = config.getProperty("api_key");
        String sharedSecret = config.getProperty("shared_secret");

        flickr = new Flickr(apiKey, sharedSecret, new REST());
    }

    public List<Photo> search(String tags) {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setTags(tags.split(","));
        searchParameters.setSort(SearchParameters.RELEVANCE);
        com.flickr4java.flickr.photos.PhotoList<com.flickr4java.flickr.photos.Photo> photoList = null;

        try {
            photoList = flickr.getPhotosInterface().search(searchParameters, 500, 1);
        } catch (FlickrException e) {
            e.printStackTrace();
        }

        List<Photo> photos = new ArrayList<Photo>();

        for (com.flickr4java.flickr.photos.Photo flickrPhoto : photoList) {
            Photo photo = new Photo(flickrPhoto.getId(), flickrPhoto.getTitle(), flickrPhoto.getThumbnailUrl(),
                    flickrPhoto.getSquareLargeUrl(), flickrPhoto.getLargeUrl());
            photos.add(photo);
        }

        return photos;
    }
}
