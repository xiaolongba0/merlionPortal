/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.TransportationOperator;
import entity.OperatorSchedule;
import entity.SystemUser;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationhumanresourcemanagementmodule.TOperatormanagementSessionBean;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tOperatorScheduleViewEditManagedBean")
@ViewScoped
public class TOperatorScheduleViewEditManagedBean {

    @EJB
    private TOperatormanagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<TransportationOperator> operators;
    private SystemUser loginedUser;
    private Integer companyId;
    private Date startDate;
    private Date endDate;

    private Integer operatorId;
    private List<OperatorSchedule> oschedules;
    private OperatorSchedule oschedule;

    /**
     * Creates a new instance of TOperatorScheduleViewEditManagedBean
     */
    public TOperatorScheduleViewEditManagedBean() {
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
        operators = tomsb.viewMyNotAvailableOperator(companyId);
    }

    public void onOperatorChange() {
        if (operators != null) {
            oschedules = tomsb.viewtOscheduleforOperator(operatorId);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        oschedule = new OperatorSchedule();
        oschedule = (OperatorSchedule) event.getObject();
        boolean result = tomsb.editOSchedule(startDate, endDate, operatorId);
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Operator Schedule is edited"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Edit Cancelled"));
    }

    public void deleteAssetSchedule(OperatorSchedule oschedule) {
        try {
            boolean result = tomsb.deleteOschedule(oschedule.getOperatorScheduleId());
            if (result) {
                oschedules.remove(oschedule);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Operator is Updated and can back to work! Schedule is deleted"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public List<OperatorSchedule> getOschedules() {
        return oschedules;
    }

    public void setOschedules(List<OperatorSchedule> oschedules) {
        this.oschedules = oschedules;
    }

    public OperatorSchedule getOschedule() {
        return oschedule;
    }

    public void setOschedule(OperatorSchedule oschedule) {
        this.oschedule = oschedule;
    }

}
