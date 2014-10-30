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
import entity.MaintenanceLog;
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
import merlionportal.tms.transportationassetmaintenancemanagementmodule.TransportationAssetMaintenanceManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tAssetMaintenanceViewEditManagedBean")
@ViewScoped
public class TAssetMaintenanceViewEditManagedBean {

    @EJB
    private TAssetmanagementSessionBean tamsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private TransportationAssetMaintenanceManagementSessionBean tammsb;

    private Integer companyId;
    private Integer gtAssssssssetId;
    private Integer logId;
    private Integer locationId;

    private String cost;
    private String description;

    private SystemUser loginedUser;
    private List<Location> locations;
    private List<TransportationAsset> tassetss;
    private List<MaintenanceLog> logs;
    private MaintenanceLog log;

    /**
     * Creates a new instance of TAssetMaintenanceViewEditManagedBean
     */
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
            tassetss = tamsb.viewtAssetForALocation(locationId);
        }
    }

    public void onTAssetChange() {
        if (gtAssssssssetId != null) {
            logs = tammsb.viewMyMaintenanceLog(gtAssssssssetId);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("IN ROW EDIT =================");
        log = new MaintenanceLog();
        log = (MaintenanceLog) event.getObject();
        System.out.println("[AFTER EDIT] Log.getDescription(): " + log.getDescription());
        tammsb.editMaintenanceLog(cost, description, logId);
        FacesMessage msg = new FacesMessage("MaintenanceLog" + log.getMaintenanceLogId() + " has sucessfully been edited");

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

    public void deleteMaintenance(MaintenanceLog log) {
        try {
            boolean result = tammsb.deleteMaintenanceLog(log.getMaintenanceLogId());
            if (result) {
                logs.remove(log);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "MaintenanceLog is deleted"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getGtAssssssssetId() {
        return gtAssssssssetId;
    }

    public void setGtAssssssssetId(Integer gtAssssssssetId) {
        this.gtAssssssssetId = gtAssssssssetId;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<TransportationAsset> getTassetss() {
        return tassetss;
    }

    public void setTassetss(List<TransportationAsset> tassetss) {
        this.tassetss = tassetss;
    }

    public List<MaintenanceLog> getLogs() {
        return logs;
    }

    public void setLogs(List<MaintenanceLog> logs) {
        this.logs = logs;
    }

    public MaintenanceLog getLog() {
        return log;
    }

    public void setLog(MaintenanceLog log) {
        this.log = log;
    }

}
