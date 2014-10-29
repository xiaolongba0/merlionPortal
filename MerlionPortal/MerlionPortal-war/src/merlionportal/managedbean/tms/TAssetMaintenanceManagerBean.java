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
import entity.TransportationOperator;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import merlionportal.tms.transportationassetmaintenancemanagementmodule.TransportationAssetMaintenanceManagementSessionBean;
import merlionportal.tms.transportationhumanresourcemanagementmodule.TOperatormanagementSessionBean;
import java.util.List;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tAssetMaintenanceManagerBean")
@ViewScoped
public class TAssetMaintenanceManagerBean {

    @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private TransportationAssetMaintenanceManagementSessionBean tammsb;
    @EJB
    private TOperatormanagementSessionBean tomsb;
    
    private Integer companyId;
    private Integer tassssssetId;
    private Integer logId;
    private Integer locationId;
    private Date today;
    private Date startDate;
    private Date endDate;
    private Integer operatorId;
    private String cost;
    private String description;
    private List<TransportationOperator> operators;
    

    private SystemUser loginedUser;
    private List<Location> locations;
    private List<TransportationAsset> transassetss;

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
        operators = tomsb.viewMyOperator(companyId);
        today = Calendar.getInstance().getTime();

    }

    public void createMaintenanceLog() {
        System.out.println("tAsset Id: " + tassssssetId);
        Integer result = tammsb.addNewMaintenanceLog(cost, description, operatorId, startDate, endDate, tassssssetId);
        if (result> -1) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "New Maintenance created!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void onChangeLocation() {
        if (locationId != null) {
            transassetss = tassetManagementSessionBean.viewtAssetForALocation(locationId);
        }
    }

    private void clearAllFields() {
        locationId = null;
        tassssssetId = null;
        cost = null;
        description = null;
        operatorId = null;
        startDate = null;
        endDate = null;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getTassssssetId() {
        return tassssssetId;
    }

    public void setTassssssetId(Integer tassssssetId) {
        this.tassssssetId = tassssssetId;
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

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
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

    public List<TransportationAsset> getTransassetss() {
        return transassetss;
    }

    public void setTransassetss(List<TransportationAsset> transassetss) {
        this.transassetss = transassetss;
    }

    public List<TransportationOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<TransportationOperator> operators) {
        this.operators = operators;
    }

     
}
