/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.AssetSchedule;
import entity.SystemUser;
import entity.Location;
import entity.TransportationAsset;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;


/**
 *
 * @author Yuanbo
 */
@Named(value = "tAssetScheduleManagerBean")
@ViewScoped
public class TAssetScheduleManagerBean {
    @EJB
    private TAssetmanagementSessionBean tamsb;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private Integer locationId;
    private List<Location> locations;

    private Integer tAssetId;
    private List<TransportationAsset> tAssets;
    
    private Date startDate;
    private Date endDate;


    private SystemUser loginedUser;
    private Integer companyId;

    public TAssetScheduleManagerBean() {
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
        locations = tamsb.viewMyLocations(companyId);
    }

    public void createTassetSchedule() {
        boolean result = tamsb.addTAssetSchedule(startDate, endDate, tAssetId);
        if (result) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "New Schedule created!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void onChangeLocation() {
        if (locationId != null) {
            tAssets = tamsb.viewtAssetForALocation(locationId);
        }
    }
    private void clearAllFields() {
        startDate = null;
        endDate = null;
        

    }
    /**
     * @return the tamsb
     */
    public TAssetmanagementSessionBean getTamsb() {
        return tamsb;
    }

    /**
     * @param tamsb the tamsb to set
     */
    public void setTamsb(TAssetmanagementSessionBean tamsb) {
        this.tamsb = tamsb;
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

    /**
     * @return the tAssetId
     */
    public Integer gettAssetId() {
        return tAssetId;
    }

    /**
     * @param tAssetId the tAssetId to set
     */
    public void settAssetId(Integer tAssetId) {
        this.tAssetId = tAssetId;
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
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
     * Creates a new instance of TAssetScheduleManagerBean
     */

    
}
