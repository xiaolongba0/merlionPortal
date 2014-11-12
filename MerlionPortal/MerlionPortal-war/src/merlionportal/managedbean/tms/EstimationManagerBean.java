/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.Location;
import javax.inject.Named;
import entity.SystemUser;
import entity.TransportationOrder;
import entity.Estimation;
import entity.PlanAssetSchedule;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TOrderManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import merlionportal.tms.routeoptimizationmodule.RouteOptimizationSessionBean;

/**
 *
 * @author Yuanbo
 */
@Named(value = "estimationManagerBean")
@ViewScoped
public class EstimationManagerBean {

    @EJB
    private TOrderManagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private TAssetmanagementSessionBean tasb;
    @EJB
    private RouteOptimizationSessionBean rosb;

    private SystemUser loginedUser;
    private Integer companyId;

    private String type;
    private Integer totalLoad;
    private Integer destinationId;
    private Integer originId;
    private Date timeEnd;
    private Integer newEstId;
    private List<Location> origins;
    private List<Location> destinations;

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
        origins = tasb.viewMyLocations(companyId);
        destinations = tasb.viewMyLocations(companyId);

    }

    public void createNewOrder(ActionEvent order) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Order");
            newEstId = rosb.addNewEstimation(companyId, originId, destinationId, type, timeEnd, totalLoad);

            if (newEstId > -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Node Order!", ""));
                clearAllFields();
                System.out.println("[WAR FILE]===========================Create New Order");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAllFields() {
        type = null;
        totalLoad = null;
        destinationId = null;
        originId = null;
        timeEnd = null;
        newEstId = null;

    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalLoad() {
        return totalLoad;
    }

    public void setTotalLoad(Integer totalLoad) {
        this.totalLoad = totalLoad;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getNewEstId() {
        return newEstId;
    }

    public void setNewEstId(Integer newEstId) {
        this.newEstId = newEstId;
    }

    public List<Location> getOrigins() {
        return origins;
    }

    public void setOrigins(List<Location> origins) {
        this.origins = origins;
    }

    public List<Location> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Location> destinations) {
        this.destinations = destinations;
    }


}
