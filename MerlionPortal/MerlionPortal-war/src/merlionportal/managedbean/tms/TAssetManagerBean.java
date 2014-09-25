/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import javax.inject.Named;

import entity.SystemUser;
import entity.Location;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import java.util.List;


/**
 *
 * @author Yuanbo
 */
@Named(value = "tAssetManagedBean")
@RequestScoped
public class TAssetManagerBean {


    @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    




    private Integer companyId;

    private SystemUser loginedUser;
    
//    ----------Asset
    private String assetType; 

    private Integer capacity;
    private Integer speed;
    private Integer price;
    private Integer locationlocationId;
    private String status;
    private Integer newAssetId;
    private Integer quantity;
    private List<Location> locations;


    /**
     * Creates a new instance of AssestManagedBean
     */
    public TAssetManagerBean() {
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
    


    public void createNewTAsset (ActionEvent location) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Asset");
            System.out.println("TransportationAssetType:" + assetType);
           
            for(int i= 0; i<quantity; i++){
            newAssetId = tassetManagementSessionBean.addTAsset(assetType, capacity, locationlocationId, price, speed, companyId, status);
            System.out.println("NEW TRANSPORTATION ASSET ID =================: " + newAssetId);
            if (newAssetId == -1) {
                clearAllFields();
                System.out.println("============== FAILED TO ADD TRANSPORTATION ASSET DUE TO LOCATION ID ===============");

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to Add Transportation Asset. Please check LOCATION ID! ", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New TransportationAsset Added!", ""));
            }
            }
            System.out.println("[WAR FILE]===========================Create New Storage Type");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }   
    
    private void clearAllFields(){
        
        assetType = null;
        capacity = null;
        price = null;
        speed = null;
        locationlocationId = null;
        status = null;
    }
    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the loginedUser
     */
    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    /**
     * @param loginedUser the loginedUser to set
     */
    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
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
    public Integer getLocationlocationId() {
        return locationlocationId;
    }

    /**
     * @param locationlocationId the locationlocationId to set
     */
    public void setLocationlocationId(Integer locationlocationId) {
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
     * @return the locations
     */
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    

  
    
}
