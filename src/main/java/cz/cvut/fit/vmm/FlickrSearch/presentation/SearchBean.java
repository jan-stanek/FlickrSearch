package cz.cvut.fit.vmm.FlickrSearch.presentation;

import cz.cvut.fit.vmm.FlickrSearch.business.PhotoRepository;
import cz.cvut.fit.vmm.FlickrSearch.business.SearchModel;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@SessionScoped
public class SearchBean implements Serializable {

    private PhotoRepository photoRepository;

    private SearchModel searchModel;

    private List<Photo> searchResult;

    @PostConstruct
    void init() {
        searchModel = new SearchModel();
        photoRepository = new PhotoRepository();
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
        searchResult = photoRepository.findPhotos(searchModel);
    }
}