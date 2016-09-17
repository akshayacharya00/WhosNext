package appsshoppy.com.whosnext.model;

import java.io.Serializable;

/**
 * Created by akshayacharya on 18/09/16.
 */
public class AddService implements Serializable {
    private String serviceTitle;
    private String serviceFor;
    private String serviceDuration;
    private String listedOnline;
    private String isFeature;
    private String offerAvailable;
    private String description;
    private String price;

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getServiceFor() {
        return serviceFor;
    }

    public void setServiceFor(String serviceFor) {
        this.serviceFor = serviceFor;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getListedOnline() {
        return listedOnline;
    }

    public void setListedOnline(String listedOnline) {
        this.listedOnline = listedOnline;
    }

    public String getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(String isFeature) {
        this.isFeature = isFeature;
    }

    public String getOfferAvailable() {
        return offerAvailable;
    }

    public void setOfferAvailable(String offerAvailable) {
        this.offerAvailable = offerAvailable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
