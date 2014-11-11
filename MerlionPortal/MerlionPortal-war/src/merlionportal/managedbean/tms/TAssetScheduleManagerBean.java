/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.SystemUser;
import entity.Location;
import entity.TransportationAsset;
import entity.TransportationOperator;
import entity.Route;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.tms.transportationhumanresourcemanagementmodule.TOperatormanagementSessionBean;
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
    @EJB
    private TOperatormanagementSessionBean tomsb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private Integer locationId;
    private List<Location> locations;
    private Integer tassetId;
    private Integer loading;
    private List<TransportationAsset> transassetss;
    private Date startDate;
    private Date endDate;
    private SystemUser loginedUser;
    private Integer companyId;
    private Date today;
    private Integer operatorId;
    private List<TransportationOperator> operators;
    private Integer routeId;
    private List<Route> routes;

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
        operators = tomsb.viewMyOperator(companyId);
        today = Calendar.getInstance().getTime();
    }

    public void createTassetSchedule() {
        System.out.println("tAsset Id: " + tassetId);
        Integer result = tamsb.addTAssetSchedule(startDate, endDate, loading, tassetId, operatorId, routeId);
        if (result > 0) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "New Schedule created!"));
            systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS created new schedule");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void onChangeLocation() {
        if (locationId != null) {
            transassetss = tamsb.viewtAvailableAssetForALocation(locationId);

        }
    }

    public void onChangeLocationS() {
        List<Route> updateList = new ArrayList();
        String temp = new String();
        if (locationId != null) {
            if (tassetId != null) {
                List<Route> tempList = tamsb.viewRoutesForLocation(locationId);
                String type = tamsb.getAsset(tassetId).getAssetType();
                if (type.equals("Truck")) {
                    temp = "Land";
                } else if (type.equals("Ship")) {
                    temp = "Sea";
                } else if (type.equals("Plane")) {
                    temp = "Air";
                }
                for (Route r : tempList) {
                    if (r.getRouteType().equals(temp)) {
                        updateList.add(r);
                    }
                }

            }

        }
        routes = updateList;
    }

    private void clearAllFields() {
        locationId = null;
        tassetId = null;
        startDate = null;
        endDate = null;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Integer getTassetId() {
        return tassetId;
    }

    public void setTassetId(Integer tassetId) {
        this.tassetId = tassetId;
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

    public List<TransportationAsset> getTransassetss() {
        return transassetss;
    }

    public void setTransassetss(List<TransportationAsset> transassetss) {
        this.transassetss = transassetss;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public List<TransportationOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<TransportationOperator> operators) {
        this.operators = operators;
    }

    public Integer getLoading() {
        return loading;
    }

    public void setLoading(Integer loading) {
        this.loading = loading;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
