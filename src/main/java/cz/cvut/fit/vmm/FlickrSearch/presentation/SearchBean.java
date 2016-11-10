package cz.cvut.fit.vmm.FlickrSearch.presentation;

import cz.cvut.fit.vmm.FlickrSearch.business.SearchModel;
import cz.cvut.fit.vmm.FlickrSearch.data.dao.PhotoDao;
import cz.cvut.fit.vmm.FlickrSearch.data.entity.Photo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.List;


@Named
@SessionScoped
public class SearchBean implements Serializable {

    @Inject
    private PhotoDao photoDao;

    private SearchModel searchModel;

    private List<Photo> searchResult;

    @PostConstruct
    void init() {
        searchModel = new SearchModel();
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    public List<Photo> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<Photo> searchResult) {
        this.searchResult = searchResult;
    }

    public void searchPhotos() {
        searchResult = photoDao.search(searchModel).getPhotos();
    }
}