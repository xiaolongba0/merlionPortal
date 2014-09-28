/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import javax.inject.Named;
import entity.SystemUser;
import entity.Location;
import entity.TransportationAsset;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
/**
 *
 * @author Yuanbo
 */


    
@Named(value = "tLocationManagerBean")
@RequestScoped
public class TLocationManagerBean {
    
    
    @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    
    private Integer newLocationId;
    private Integer locationId;
    private String locationName;
    private String locationType;
    private Integer companyId;
    private Location location; 
    private SystemUser loginedUser;
       /**
     * Creates a new instance of TLocationManagerBean
     */
    public TLocationManagerBean() {
    }
    
  @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            setLoginedUser(getUamb().getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")));
            setCompanyId((Integer) (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId"));
            if (getLoginedUser() != null) {
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

    }
    
        public void createNewLocation(ActionEvent location) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New location");
            newLocationId = tassetManagementSessionBean.addNewLocation(locationName, locationType, companyId);
            if(newLocationId > -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Location Added!", ""));
            clearAllFields();
            System.out.println("[WAR FILE]===========================Create New Location");
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR, "Something went wrong!",""));
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
        
    private void clearAllFields(){
        
        locationName = null;
        locationType = null;
  
    }
   

    /**
     * @return the uamb
     */
    public UserAccountManagementSessionBean getUamb() {
        return uamb;
    }

    /**
     * @param uamb the uamb to set
     */
    public void setUamb(UserAccountManagementSessionBean uamb) {
        this.uamb = uamb;
    }

    /**
     * @return the newLocationId
     */
    public Integer getNewLocationId() {
        return newLocationId;
    }

    /**
     * @param newLocationId the newLocationId to set
     */
    public void setNewLocationId(Integer newLocationId) {
        this.newLocationId = newLocationId;
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
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the locationType
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * @param locationType the locationType to set
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
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

 
    
}
