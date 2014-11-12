/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.TransportationOperator;
import entity.SystemUser;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationhumanresourcemanagementmodule.TOperatormanagementSessionBean;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tOperatorScheduleManagerBean")
@ViewScoped
public class TOperatorScheduleManagerBean {

    @EJB
    private TOperatormanagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<TransportationOperator> operators;
    private Integer operatorId;
    private SystemUser loginedUser;
    private Integer companyId;
    private Date startDate;
    private Date endDate;
    private Date today;

    /**
     * Creates a new instance of TOperatorScheduleManagerBean
     */
    public TOperatorScheduleManagerBean() {
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
        operators = tomsb.viewMyAvailableOperator(companyId);
        System.out.println("View Operators for company Id: " + companyId);
        today = Calendar.getInstance().getTime();
    }

    public void createOperatirSchedule() {
        boolean result = tomsb.addOschedule(startDate, endDate, operatorId);
        if (result) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "New Operator Schedule created!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void onChangeCompany() {
        if (companyId != null) {
            operators = tomsb.viewMyAvailableOperator(companyId);
        }
    }

    private void clearAllFields() {
        startDate = null;
        endDate = null;
        operatorId = null;
    }

    public List<TransportationOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<TransportationOperator> operators) {
        this.operators = operators;
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

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

}
