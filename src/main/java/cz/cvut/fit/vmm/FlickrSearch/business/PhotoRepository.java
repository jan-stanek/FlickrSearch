package cz.cvut.fit.vmm.FlickrSearch.business;

import cz.cvut.fit.vmm.FlickrSearch.business.flickr.FlickrFacade;
import cz.cvut.fit.vmm.FlickrSearch.entity.Color;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import java.util.*;

/**
 * Created by jan on 08.11.2016.
 */
public class PhotoRepository {

    private List<Photo> sortedList;
    private List<Photo> unsortedList;

    private PhotoFacade photoFacade;

    private Color color;

    final private int THREADS_COUNT = 32;

    public PhotoRepository(Properties config) {
        photoFacade = new FlickrFacade(config);
    }

    public void search(SearchModel searchModel) {
        unsortedList = photoFacade.search(searchModel.getTags());
        sortedList = null;

        color = null;
        if (searchModel.getColor().contains("#"))
            color = new Color(searchModel.getColor());
    }

    public List<Photo> getSortedPhotos() {
        if (color == null)
            return null;

        if (sortedList != null)
            return sortedList;

        sortedList = new ArrayList<Photo>();

        Thread [] threads = new Thread[THREADS_COUNT];

        for (int i = 0; i < THREADS_COUNT; i++) {
            int from = (unsortedList.size() * i) / THREADS_COUNT;
            int to = (unsortedList.size() * (i+1)) / THREADS_COUNT;
            RankCounter rankCounter = new RankCounter(unsortedList.subList(from, to), color);
            threads[i] = new Thread(rankCounter);
            threads[i].start();
        }

        try {
            for (int i = 0; i < THREADS_COUNT; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Photo photo : unsortedList) {
            if (!Double.isNaN(photo.getRank()))
                sortedList.add(photo);
        }

        if (color != null)
            sort(sortedList);

        return sortedList;
    }

    public List<Photo> getUnsortedPhotos() {
        return unsortedList;
    }

    private void sort(List<Photo> photos) {
        Collections.sort(photos, new Comparator<Photo>() {
            public int compare(final Photo p1, final Photo p2) {
                return (int) Math.signum(p1.getRank() - p2.getRank());
            }
        });
    }
}
