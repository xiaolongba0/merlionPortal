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
import entity.Node;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
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
    @EJB
    private SystemLogSessionBean systemLogSB;
    
    private Integer newLocationId;
    private Integer locationId;
    private String locationName;
    private String locationType;
    private Integer companyId;
    private Location location; 
    private SystemUser loginedUser;
    private Integer nodeId;
    private List<Node> nodes;
       /**
     * Creates a new instance of TLocationManagerBean
     */
    public TLocationManagerBean() {
    }
    
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
        nodes = tassetManagementSessionBean.viewTheNodes();
    }
    
    public void createNewLocation(ActionEvent location) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New location");
            System.out.println("LocationName :" + locationName);
            System.out.println("Location Type :" + locationType);
            System.out.println("companyId :" + companyId);
            System.out.println("NodeId :" + nodeId);
            newLocationId = tassetManagementSessionBean.addNewLocation(locationName, locationType, companyId, nodeId);
            if (newLocationId > -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Location Added!", ""));
                systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS added new location");
                clearAllFields();
                System.out.println("[WAR FILE]===========================Create New Location");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAllFields() {

        locationName = null;
        locationType = null;

    }
   

    /**
     * @return the uamb


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

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

 
    
}
