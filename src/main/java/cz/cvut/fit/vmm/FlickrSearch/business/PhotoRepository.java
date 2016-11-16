package cz.cvut.fit.vmm.FlickrSearch.business;

import cz.cvut.fit.vmm.FlickrSearch.business.flickr.FlickrFacade;
import cz.cvut.fit.vmm.FlickrSearch.entity.Color;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jan on 08.11.2016.
 */
public class PhotoRepository {

    public List<Photo> findPhotos(SearchModel searchModel) {
        PhotoFacade photoFacade = new FlickrFacade();
        List<Photo> photos = new ArrayList<Photo>();

        List<Photo> photoList = photoFacade.search(searchModel.getTags());

        Color color = null;
        if (searchModel.getColor().contains("#"))
            color = new Color(searchModel.getColor());

        int threadsCount = 32;
        Thread [] threads = new Thread[threadsCount];

        for (int i = 0; i < threadsCount; i++) {
            int from = (photoList.size() * i) / threadsCount;
            int to = (photoList.size() * (i+1)) / threadsCount;
            RankCounter rankCounter = new RankCounter(photoList.subList(from, to), color);
            threads[i] = new Thread(rankCounter);
            threads[i].start();
        }

        try {
            for (int i = 0; i < threadsCount; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Photo photo : photoList) {
            if (!Double.isNaN(photo.getRank()))
                photos.add(photo);
        }

        if (color != null)
            sort(photos);

        return photos;
    }

    private void sort(List<Photo> photos) {
        Collections.sort(photos, new Comparator<Photo>() {
            public int compare(final Photo p1, final Photo p2) {
                return (int) Math.signum(p1.getRank() - p2.getRank());
            }
        });
    }
}
