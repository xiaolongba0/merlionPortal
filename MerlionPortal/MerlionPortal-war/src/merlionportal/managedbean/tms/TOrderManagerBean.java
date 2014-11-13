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

/**
 *
 * @author Yuanbo
 */
@Named(value = "tOrderManagerBean")
@ViewScoped
public class TOrderManagerBean {

    @EJB
    private TOrderManagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private TAssetmanagementSessionBean tasb;

    private SystemUser loginedUser;
    private Integer companyId;

    private String cargoType;
    private Integer cargoWeight;
    private Integer creatorId;
    private Integer destination;
    private Integer origin;
    private Integer referenceId;
    private Integer referenceType;
    private Date timeEnd;
    private Date timeStart;
    private Integer newOrderId;
    private List<Location> origins;
    private List<Location> destinations;

    /**
     * Creates a new instance of TOrderManagerBean
     */
    public TOrderManagerBean() {
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
        origins = tasb.viewMyLocations(companyId);
        destinations = tasb.viewMyLocations(companyId);
        creatorId = loginedUser.getSystemUserId();

    }

    public void createNewOrder(ActionEvent order) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Order");
            newOrderId = tomsb.AddNewTransportationOrder(cargoType, cargoWeight, companyId, creatorId, destination, origin, referenceId, 1, timeEnd, timeStart);
            
            
            if (newOrderId > -1) {
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

        cargoType = null;
        cargoWeight = null;
        creatorId = null;
        destination = null;
        timeEnd = null;
        timeStart = null;
        origin = null;
        referenceId = null;
        referenceType = null;
        creatorId = null;

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

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public Integer getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(Integer cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }


    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(Integer referenceType) {
        this.referenceType = referenceType;
    }

    public Integer getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(Integer newOrderId) {
        this.newOrderId = newOrderId;
    }



    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
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
