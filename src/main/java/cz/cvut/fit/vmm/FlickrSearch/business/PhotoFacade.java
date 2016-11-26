package cz.cvut.fit.vmm.FlickrSearch.business;

import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jan on 02.11.2016.
 */
public interface PhotoFacade extends Serializable {
    List<Photo> search(String tags);
}
