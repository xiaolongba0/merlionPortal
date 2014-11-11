/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this start1late file, choose Tools | Templates
 * and open the start1late in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.SystemUser;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;

@Named(value = "forecastGetParameterManagedBean")
@ViewScoped
public class ForecastGetParameterManagedBean implements Serializable {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private Integer companyId;

    private double expectedGrowth;
    private int periodicity;
    private int size;
    private SystemUser loginedUser;

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

    }

    public String onParameterChange() {

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expectedGrowth", expectedGrowth);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("periodicity", periodicity);

        size = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("size");

        if (periodicity < 1) {
            FacesMessage msg = new FacesMessage("Periodicity must be greater than 1 month, please input periodicity again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "null";
        } else if (periodicity >= size) {
            FacesMessage msg = new FacesMessage("Periodicity can not be equal or greater than 24 months or greater than the number of months of sales history avaiable, please input periodicity again.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "null";
        } else {
            systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "MRP gets forecasting parameters from system user. ");
            return "forecastresult?faces-redirect=true";
        }

    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public double getExpectedGrowth() {
        return expectedGrowth;
    }

    public void setExpectedGrowth(double expectedGrowth) {
        this.expectedGrowth = expectedGrowth;
    }

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

}
