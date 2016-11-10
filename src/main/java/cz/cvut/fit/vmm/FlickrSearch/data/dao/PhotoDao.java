package cz.cvut.fit.vmm.FlickrSearch.data.dao;

import cz.cvut.fit.vmm.FlickrSearch.business.PhotoList;
import cz.cvut.fit.vmm.FlickrSearch.business.SearchModel;

import java.io.Serializable;

/**
 * Created by jan on 02.11.2016.
 */
public interface PhotoDao extends Serializable {

    PhotoList search(SearchModel searchModel);

}
