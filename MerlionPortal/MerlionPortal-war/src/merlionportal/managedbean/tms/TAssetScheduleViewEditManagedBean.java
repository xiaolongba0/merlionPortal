/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.AssetSchedule;
import entity.TransportationAsset;
import entity.SystemUser;
import entity.Location;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tAssetScheduleViewEditManagedBean")
@ViewScoped
public class TAssetScheduleViewEditManagedBean {

    
    @EJB
    private TAssetmanagementSessionBean tamsb;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<Location> locations;
    private List<TransportationAsset> tAssets;
    private List<AssetSchedule> schedules;
    private Integer locationId;
    private Integer tAssetId;
    private AssetSchedule schedule;

    private SystemUser loginedUser;
    private Integer companyId;

    
    /**
     * Creates a new instance of TAssetScheduleViewEditManagedBean
     */
    public TAssetScheduleViewEditManagedBean() {
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
    
    public void onLocationChange() {
        if (locationId != null) {
            tAssets = tamsb.viewtAssetForALocation(locationId);
        }
    }

    public void onTAssetChange() {
        if (tAssetId != null) {
            schedules = tamsb.viewTAssetScheduleforTAsset(tAssetId);
        }
    }
 
    public void onRowEdit(RowEditEvent event) {
        schedule = new AssetSchedule();
        schedule = (AssetSchedule) event.getObject();
        boolean result = tamsb.editTAssetSchedule(schedule.getStartDate(), schedule.getEndDate(), schedule.getScheduleId());
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Asset Schedule is edited"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Edit Cancelled"));
    }

    public void deleteAssetSchedule(AssetSchedule schedule) {
        try {
            boolean result = tamsb.deleteTAssetSchedule(schedule.getScheduleId());
            if (result) {
                schedules.remove(schedule);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Asset Schedule is deleted"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
     * @return the schedules
     */
    public List<AssetSchedule> getSchedules() {
        return schedules;
    }

    /**
     * @param schedules the schedules to set
     */
    public void setSchedules(List<AssetSchedule> schedules) {
        this.schedules = schedules;
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
     * @return the schedule
     */
    public AssetSchedule getSchedule() {
        return schedule;
    }

    /**
     * @param schedule the schedule to set
     */
    public void setSchedule(AssetSchedule schedule) {
        this.schedule = schedule;
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
 
}
