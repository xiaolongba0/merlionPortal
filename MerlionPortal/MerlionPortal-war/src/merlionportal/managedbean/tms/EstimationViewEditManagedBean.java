/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.Estimation;
import javax.inject.Named;
import entity.SystemUser;
import entity.TransportationOrder;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TOrderManagementSessionBean;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.tms.routeoptimizationmodule.RouteOptimizationSessionBean;

/**
 *
 * @author Yuanbo
 */
@Named(value = "estimationViewEditManagedBean")
@ViewScoped
public class EstimationViewEditManagedBean {

    @EJB
    private TOrderManagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private RouteOptimizationSessionBean rosb;

    private SystemUser loginedUser;
    private Integer companyId;

    private List<Estimation> estimations;

    /**
     * Creates a new instance of EstimationViewEditManagedBean
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
        estimations = rosb.viewTheEstimation(companyId);
    }

    public List<Estimation> getOrders() {
        System.out.println("===============================[In Managed Bean - view Orders for company Id!]" + companyId);
        estimations = rosb.viewTheEstimation(companyId);

        // for checking
        for (Object obj : estimations) {
            System.out.println(obj);
        }
        return estimations;
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

    public List<Estimation> getEstimations() {
        return estimations;
    }

    public void setEstimations(List<Estimation> estimations) {
        this.estimations = estimations;
    }

}
