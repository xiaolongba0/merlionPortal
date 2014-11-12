/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import javax.inject.Named;
import entity.SystemUser;
import entity.Route;
import java.io.IOException;
import entity.Node;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@Named(value = "routeViewEditManagedBean")
@ViewScoped
public class RouteViewEditManagedBean {

    @EJB
    private TAssetmanagementSessionBean tassetmanagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    private List<Route> routes;
    private Integer routeId;
    private Integer nodeId;
    private Integer companyId;
    private Route route;
    private List<Node> nodes;

    private SystemUser loginedUser;

    /**
     * Creates a new instance of RouteViewEditManagedBean
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
        nodes = tassetmanagementSessionBean.viewTheNodes();

    }

//    public List<Route> viewRoutes() {
//        System.out.println("===============================[In Managed Bean - get Route!]");
//        routes = tassetmanagementSessionBean.viewtheRouteForNode(nodeId);
//
//        // for checking
//        for (Object obj : routes) {
//            System.out.println(obj);
//        }
//        return routes;
//    }

    public void deleteRoute(Route route) {
        try {
            routeId = route.getRouteId();
            System.out.println("[In WAR FILE - Delete Route Function] Route ID========== :" + routeId);
            tassetmanagementSessionBean.deleteRoute(routeId);
            routes.remove(route);
            System.out.println("successfully deleted Route" +routeId);
            systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS deleted route");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onNodeChange() {
        System.out.println("===============================[In Managed Bean - get Node]");
        System.out.println("[In Managed Bean - getLocation] location ID : " + nodeId);
        if (nodeId != null) {
            routes = tassetmanagementSessionBean.viewtheRouteForNode(nodeId);
            if (routes == null) {
                System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Storage Type. Please check if warehouse ID exists! ", ""));
            }

        }

    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
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

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

}
