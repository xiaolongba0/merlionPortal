/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

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

/**
 *
 * @author Yuanbo
 */
@Named(value = "tOrderViewEditManagedBean")
@ViewScoped
public class TOrderViewEditManagedBean {

    @EJB
    private TOrderManagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private SystemUser loginedUser;
    private Integer companyId;

    private List<TransportationOrder> orders;

    /**
     * Creates a new instance of TOrderViewEditManagedBean
     */
    public TOrderViewEditManagedBean() {
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

    }

    public List<TransportationOrder> getOrders() {
        System.out.println("===============================[In Managed Bean - view Orders for company Id!]" + companyId);
        orders = tomsb.viewTOrderForCompany(companyId);

        // for checking
        for (Object obj : orders) {
            System.out.println(obj);
        }
        return orders;
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

}
