package cz.cvut.fit.vmm.FlickrSearch.business.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.SearchParameters;
import cz.cvut.fit.vmm.FlickrSearch.business.PhotoFacade;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FlickrFacade implements PhotoFacade {

    private Flickr flickr;

    public FlickrFacade() {
        String apiKey = "077058273325165605911344e25e08ca";
        String sharedSecret = "4efacc8562efec7f";

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
