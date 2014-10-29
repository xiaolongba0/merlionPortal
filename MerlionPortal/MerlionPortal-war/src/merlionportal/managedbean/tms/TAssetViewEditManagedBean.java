/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.TransportationAsset;
import entity.Location;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import entity.SystemUser;

/**
 *
 * @author Yuanbo
 */
@Named(value = "TTassetViewEditManagedBean")
@ViewScoped
public class TAssetViewEditManagedBean {

    @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<TransportationAsset> tassets;
    private List<Location> locations;
    private TransportationAsset tAsset;
    private String description;

    private Integer locationId;
    private String assetType;
    private Integer capacity;
    private Integer speed;
    private Integer price;
    private Location locationlocationId;
    private String status;
    private Integer newAssetId;
    private Integer quantity;
    private Integer companyId;

    private SystemUser loginedUser;

    public TAssetViewEditManagedBean() {
    }

    @PostConstruct
    public void init() {

        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            if (loginedUser != null) {
                redirect = false;
            }
        }
        if (redirect) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        locations = tassetManagementSessionBean.viewMyLocations(companyId);

    }

    public void onLocationChange() {
        System.out.println("===============================[In Managed Bean - get Locations]");
        System.out.println("[In Managed Bean - getLocation] location ID : " + locationId);
        if (locationId != null) {
            tassets = tassetManagementSessionBean.viewtAssetForALocation(locationId);
            if (tassets == null) {
                System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Storage Type. Please check if warehouse ID exists! ", ""));
            }

        }

    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("IN ROW EDIT =================");
        tAsset = new TransportationAsset();
        tAsset = (TransportationAsset) event.getObject();
        System.out.println("[AFTER EDIT] Asset.getName(): " + tAsset.getAssetType());
        tassetManagementSessionBean.edittAsset(tAsset.getCapacity(), tAsset.getPrice(), tAsset.getSpeed(), tAsset.getStatus(), tAsset.getAssetId());
        FacesMessage msg = new FacesMessage("Transportation Asset with tAsset ID = " + tAsset.getAssetId() + " has sucessfully been edited");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteTAsset(TransportationAsset tAsset) {
        try {
            System.out.println("[In WAR FILE - Delete TransportationAsset Function]" + tAsset);
            System.out.println("[In WAR FILE - Delete TransportationAsset Function] TransportationAsset Id===========:" + tAsset.getAssetId());
            tassetManagementSessionBean.deletetAsset(tAsset.getAssetId());
            tassets.remove(tAsset);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Transportation Asset is deleted"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<TransportationAsset> getTassets() {
        return tassets;
    }

    public void setTassets(List<TransportationAsset> tassets) {
        this.tassets = tassets;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public TransportationAsset gettAsset() {
        return tAsset;
    }

    public void settAsset(TransportationAsset tAsset) {
        this.tAsset = tAsset;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Location getLocationlocationId() {
        return locationlocationId;
    }

    public void setLocationlocationId(Location locationlocationId) {
        this.locationlocationId = locationlocationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNewAssetId() {
        return newAssetId;
    }

    public void setNewAssetId(Integer newAssetId) {
        this.newAssetId = newAssetId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

}
