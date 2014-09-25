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
@Named(value = "tAssetViewEditManagedBean")
@ViewScoped
public class TAssetViewEditManagedBean {
   @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
   @EJB
   private UserAccountManagementSessionBean uamb;

    private List<TransportationAsset> tAssets;
    private List<Location> locations;
    private TransportationAsset tAsset;
    private String description;

    private Integer locationId;
    private Location location;
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

    public List<TransportationAsset> onLocationChange() {
        System.out.println("===============================[In Managed Bean - get Locations]");
        // Dunno how to take in warehouse ID as input for delete function, and dunno why delete function don't update the
        // output shown on webpage after it was deleted
        System.out.println("[In Managed Bean - getLocation] location ID : " + locationId);
        if (locationId != null) {
            tAssets = tassetManagementSessionBean.viewtAssetForALocation(locationId);
            if (tAssets == null) {
                System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Storage Type. Please check if warehouse ID exists! ", ""));
            }
            return tAssets;
        }
        return null;
    }
    
    public void onRowEdit(RowEditEvent event) {
        System.out.println("IN ROW EDIT =================");
        tAsset = new TransportationAsset();
        tAsset = (TransportationAsset) event.getObject();
        System.out.println("[AFTER EDIT] Asset.getName(): " + tAsset.getAssetType());
        tassetManagementSessionBean.edittAsset(tAsset.getCapacity(), tAsset.getPrice(), tAsset.getSpeed(), tAsset.getStatus(), tAsset.getAssetId());
        FacesMessage msg = new FacesMessage("Transportation Asset with tAsset ID = " + tAsset.getAssetId()+ " has sucessfully been edited");

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
            tAssets.remove(tAsset);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Transportation Asset is deleted"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new instance of TAssetViewEditManagedBean
     */

    /**
     * @return the tassetManagementSessionBean
     */
    public TAssetmanagementSessionBean getTassetManagementSessionBean() {
        return tassetManagementSessionBean;
    }

    /**
     * @param tassetManagementSessionBean the tassetManagementSessionBean to set
     */
    public void setTassetManagementSessionBean(TAssetmanagementSessionBean tassetManagementSessionBean) {
        this.tassetManagementSessionBean = tassetManagementSessionBean;
    }

    /**
     * @return the tAssets
     */
    public List<TransportationAsset> gettAssets() {
        return tAssets;
    }

    /**
     * @param tAssets the tAssets to set
     */
    public void settAssets(List<TransportationAsset> tAssets) {
        this.tAssets = tAssets;
    }

    /**
     * @return the tAsset
     */
    public TransportationAsset gettAsset() {
        return tAsset;
    }

    /**
     * @param tAsset the tAsset to set
     */
    public void settAsset(TransportationAsset tAsset) {
        this.tAsset = tAsset;
    }

    /**
     * @return the tAssetid
     */


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the locationId
     */
    public Integer getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the assetType
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * @param assetType the assetType to set
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * @return the capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the speed
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    /**
     * @return the price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return the locationlocationId
     */
    public Location getLocationlocationId() {
        return locationlocationId;
    }

    /**
     * @param locationlocationId the locationlocationId to set
     */
    public void setLocationlocationId(Location locationlocationId) {
        this.locationlocationId = locationlocationId;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the newAssetId
     */
    public Integer getNewAssetId() {
        return newAssetId;
    }

    /**
     * @param newAssetId the newAssetId to set
     */
    public void setNewAssetId(Integer newAssetId) {
        this.newAssetId = newAssetId;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}
