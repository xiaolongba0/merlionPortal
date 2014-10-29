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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tLocationViewEditManagedBean")
@ViewScoped
public class TLocationViewEditManagedBean {

    @EJB
    private TAssetmanagementSessionBean tassetmanagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<Location> locations;
    private Integer locationId;
    private String locationName;
    private Integer companyId;
    private Location location;

    private SystemUser loginedUser;

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            setLoginedUser(uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")));
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

    public List<Location> getLocations() {
        System.out.println("===============================[In Managed Bean - getLocation!]");
        locations = tassetmanagementSessionBean.viewMyLocations(companyId);

        // for checking
        for (Object obj : locations) {
            System.out.println(obj);
        }
        return locations;
    }

    public void deleteLocation(Location location) {
        try {
            locationId = location.getLocationId();
            System.out.println("[In WAR FILE - Delete Warehouse Function] Location ID========== :" + locationId);
            tassetmanagementSessionBean.deleteLocation(companyId, locationId);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the tassetmanagementSessionBean
     */
    public TAssetmanagementSessionBean getTassetmanagementSessionBean() {
        return tassetmanagementSessionBean;
    }

    /**
     * @param tassetmanagementSessionBean the tassetmanagementSessionBean to set
     */
    public void setTassetmanagementSessionBean(TAssetmanagementSessionBean tassetmanagementSessionBean) {
        this.tassetmanagementSessionBean = tassetmanagementSessionBean;
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
     * @param locations the locations to set
     */
    public void setLocations(List<Location> locations) {
        this.locations = locations;
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

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Location Edited");
        location = new Location();
        location = (Location) event.getObject();
        System.err.println("location.getName(): " + location.getLocationName());
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
    /**
     * Creates a new instance of AssetViewEditManagedBean
     */

}
