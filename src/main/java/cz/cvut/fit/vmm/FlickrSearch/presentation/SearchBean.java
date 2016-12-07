package cz.cvut.fit.vmm.FlickrSearch.presentation;

import cz.cvut.fit.vmm.FlickrSearch.business.PhotoRepository;
import cz.cvut.fit.vmm.FlickrSearch.business.SearchModel;
import cz.cvut.fit.vmm.FlickrSearch.business.Metric;
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

    private List<Photo> sortedPhotosRGB;
    private List<Photo> sortedPhotosCIE76;
    private List<Photo> sortedPhotosCIE2000;
    private List<Photo> unsortedPhotos;
    private List<Photo> photoResult;

    private boolean renderEmpty;
    private boolean renderSwitches;
    private boolean renderPhotos;
    private boolean renderDistances;

    private boolean sortedRGBButtonSelected;
    private boolean sortedCIE76ButtonSelected;
    private boolean sortedCIE2000ButtonSelected;

    private Metric metric;

    private boolean unsortedButtonSelected;

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

    public boolean isUnsortedButtonSelected() {
        return unsortedButtonSelected;
    }

    public boolean isSortedRGBButtonSelected() {
        return sortedRGBButtonSelected;
    }

    public boolean isSortedCIE76ButtonSelected() {
        return sortedCIE76ButtonSelected;
    }

    public boolean isSortedCIE2000ButtonSelected() {
        return sortedCIE2000ButtonSelected;
    }

    public Metric getMetric() {
        return metric;
    }

    public void searchPhotos() {
        if (searchModel.getCount() == null)
            searchModel.setCount(500);
        else if (searchModel.getCount() > 500)
            searchModel.setCount(500);

        photoRepository.search(searchModel);
        sortedPhotosCIE76 = photoRepository.getSortedPhotos(Metric.CIE76);
        sortedPhotosCIE2000 = photoRepository.getSortedPhotos(Metric.CIE2000);
        sortedPhotosRGB = photoRepository.getSortedPhotos(Metric.RGB);
        unsortedPhotos = photoRepository.getUnsortedPhotos();

        if (sortedPhotosRGB == null || sortedPhotosCIE76 == null || sortedPhotosCIE2000 == null) {
            renderSwitches = false;
            showUnsorted();
        } else {
            renderSwitches = true;
            showSortedCIE76();
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

    public void showSortedRGB() {
        sortedCIE76ButtonSelected = false;
        sortedCIE2000ButtonSelected = false;
        sortedRGBButtonSelected = true;
        unsortedButtonSelected = false;

        renderDistances = true;
        metric = Metric.RGB;

        photoResult = sortedPhotosRGB;
    }

    public void showSortedCIE76() {
        sortedCIE76ButtonSelected = true;
        sortedCIE2000ButtonSelected = false;
        sortedRGBButtonSelected = false;
        unsortedButtonSelected = false;

        renderDistances = true;
        metric = Metric.CIE76;

        photoResult = sortedPhotosCIE76;
    }

    public void showSortedCIE2000() {
        sortedCIE76ButtonSelected = false;
        sortedCIE2000ButtonSelected = true;
        sortedRGBButtonSelected = false;
        unsortedButtonSelected = false;

        renderDistances = true;
        metric = Metric.CIE2000;

        photoResult = sortedPhotosCIE2000;
    }

    public void showUnsorted() {
        sortedCIE76ButtonSelected = false;
        sortedCIE2000ButtonSelected = false;
        sortedRGBButtonSelected = false;
        unsortedButtonSelected = true;

        renderDistances = false;

        photoResult = unsortedPhotos;
    }
}