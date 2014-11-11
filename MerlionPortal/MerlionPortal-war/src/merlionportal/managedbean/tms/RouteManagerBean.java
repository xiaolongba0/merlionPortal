/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.Node;
import javax.inject.Named;
import entity.SystemUser;
import entity.Route;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;

/**
 *
 * @author Yuanbo
 */
@Named(value = "routeManagerBean")
@ViewScoped
public class RouteManagerBean {

    @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    private Boolean newRouteId;
    private Integer routeId;
    private Integer companyId;
    private Route route;
    private SystemUser loginedUser;
    private Integer nodeId;
    private Integer distance;
    private String routeType;
    private Integer startNodeId;
    private Integer endNodeId;
    private List<Node> startNodes;
    private List<Node> endNodes;

    /**
     * Creates a new instance of RouteManagerBean
     */
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
        startNodes = tassetManagementSessionBean.viewTheNodes();
        endNodes = tassetManagementSessionBean.viewTheNodes();
    }

    public void createNewRoute(ActionEvent route) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Route");
            System.out.println("=====distance:" + distance);
            System.out.println("=====routeType:" + routeType);
            System.out.println("=====startNode Id:" + startNodeId);
            System.out.println("=====endNode Id:" + endNodeId);
            
            newRouteId = tassetManagementSessionBean.addRoute(distance, routeType, startNodeId, endNodeId);
            if (newRouteId) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Route Added!", ""));
                clearAllFields();
                System.out.println("[WAR FILE]===========================Create New Route");
                systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS created new route");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAllFields() {
        startNodeId = null;
        distance = null;
        endNodeId = null;
        routeType = null;

    }

    public Boolean getNewRouteId() {
        return newRouteId;
    }

    public void setNewRouteId(Boolean newRouteId) {
        this.newRouteId = newRouteId;
    }


    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public Integer getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(Integer startNodeId) {
        this.startNodeId = startNodeId;
    }

    public Integer getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(Integer endNodeId) {
        this.endNodeId = endNodeId;
    }

    public List<Node> getStartNodes() {
        return startNodes;
    }

    public void setStartNodes(List<Node> startNodes) {
        this.startNodes = startNodes;
    }

    public List<Node> getEndNodes() {
        return endNodes;
    }

    public void setEndNodes(List<Node> endNodes) {
        this.endNodes = endNodes;
    }

}
