package cz.cvut.fit.vmm.FlickrSearch.presentation;

import cz.cvut.fit.vmm.FlickrSearch.business.PhotoRepository;
import cz.cvut.fit.vmm.FlickrSearch.business.SearchModel;
import cz.cvut.fit.vmm.FlickrSearch.entity.Photo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;


@Named
@SessionScoped
public class SearchBean implements Serializable {

    private PhotoRepository photoRepository;

    private SearchModel searchModel;

    private List<Photo> sortedPhotos;
    private List<Photo> unsortedPhotos;
    private List<Photo> photoResult;

    private boolean renderEmpty;
    private boolean renderSwitches;
    private boolean renderPhotos;
    private boolean renderDistances;
    private boolean renderSorted;

    private String sortedButtonType;
    private String unsortedButtonType;

    private Properties config;

    @Inject
    private ServletContext context;

    @PostConstruct
    void init() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        config = new Properties();
        try {
            config.load(servletContext.getResourceAsStream("/WEB-INF/config.properties"));
            searchModel = new SearchModel();
            photoRepository = new PhotoRepository(config);
        } catch (IOException e) {
            System.err.println("Can't load config file.");
        }
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    public List<Photo> getPhotoResult() {
        return photoResult;
    }

    public boolean isRenderEmpty() {
        return renderEmpty;
    }

    public boolean isRenderSwitches() {
        return renderSwitches;
    }

    public boolean isRenderPhotos() {
        return renderPhotos;
    }

    public boolean isRenderDistances() {
        return renderDistances;
    }

    public boolean isRenderSorted() {
        return renderSorted;
    }

    public String getSortedButtonType() {
        return sortedButtonType;
    }

    public String getUnsortedButtonType() {
        return unsortedButtonType;
    }

    public void searchPhotos() {
        photoRepository.search(searchModel);
        sortedPhotos = photoRepository.getSortedPhotos();
        unsortedPhotos = photoRepository.getUnsortedPhotos();

        if (sortedPhotos == null) {
            renderSwitches = false;
            renderDistances = false;
            showUnsorted();
        } else {
            renderSwitches = true;
            renderDistances = true;
            showSorted();
        }

        if (photoResult.isEmpty()) {
            renderEmpty = true;
            renderPhotos = false;
            renderSwitches = false;
        } else {
            renderEmpty = false;
            renderPhotos = true;
        }

    }

    public void showSorted() {
        renderSorted = true;
        unsortedButtonType = "primary";
        sortedButtonType = "default";
        photoResult = sortedPhotos;
    }

    public void showUnsorted() {
        renderSorted = false;
        sortedButtonType = "primary";
        unsortedButtonType = "default";
        photoResult = unsortedPhotos;
    }
}