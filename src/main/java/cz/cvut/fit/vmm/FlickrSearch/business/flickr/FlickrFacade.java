package cz.cvut.fit.vmm.FlickrSearch.business.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.SearchParameters;
import cz.cvut.fit.vmm.FlickrSearch.business.PhotoList;
import cz.cvut.fit.vmm.FlickrSearch.entity.Color;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;
import cz.cvut.fit.vmm.FlickrSearch.business.SearchModel;
import cz.cvut.fit.vmm.FlickrSearch.business.PhotoFacade;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FlickrFacade implements PhotoFacade {

    private Flickr flickr;

    public FlickrFacade() {
        String apiKey = "077058273325165605911344e25e08ca";
        String sharedSecret = "4efacc8562efec7f";

        flickr = new Flickr(apiKey, sharedSecret, new REST());
    }

    public PhotoList search(SearchModel searchModel) {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setTags(searchModel.getTags().split(","));
        searchParameters.setSort(SearchParameters.RELEVANCE);
        com.flickr4java.flickr.photos.PhotoList<com.flickr4java.flickr.photos.Photo> photoList = null;

        try {
            photoList = flickr.getPhotosInterface().search(searchParameters, 100, 1);
        } catch (FlickrException e) {
            e.printStackTrace();
        }

        Color color = null;
        if (searchModel.getColor().contains("#"))
            color = new Color(searchModel.getColor());

        PhotoList photos = new PhotoList(color);

        for (com.flickr4java.flickr.photos.Photo flickrPhoto : photoList) {
            Photo photo = new Photo(flickrPhoto.getId(), flickrPhoto.getTitle(), flickrPhoto.getThumbnailUrl(),
                    flickrPhoto.getSquareLargeUrl(), flickrPhoto.getLargeUrl());
            photos.add(photo);
        }

        photos.sort();

        return photos;
    }
}
