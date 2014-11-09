/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.Node;
import javax.inject.Named;
import entity.SystemUser;
import entity.TransportationOperator;
import entity.TransportationOrder;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TOrderManagementSessionBean;
import merlionportal.tms.transportationhumanresourcemanagementmodule.TOperatormanagementSessionBean;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tOLogManagerBean")
@ViewScoped
public class TOLogManagerBean {

    @EJB
    private TOrderManagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private TOperatormanagementSessionBean tpomsb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private SystemUser loginedUser;
    private Integer companyId;
    private List<TransportationOrder> orders;
    private List<TransportationOperator> operators;
    private Integer newLogId;

    private String action;
    private String actionMessage;
    private Integer orderId;
    private Integer OperatorId;
    private Date timeStamp;

    /**
     * Creates a new instance of TOLogManagerBean
     */
    public TOLogManagerBean() {
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
        orders = tomsb.viewTOrderForCompany(companyId);
        operators = tpomsb.viewMyOperator(companyId);
        timeStamp = Calendar.getInstance().getTime();
    }

    public void createNewLog(ActionEvent log) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Node");
            newLogId = tomsb.addNewLog(orderId, OperatorId, action, actionMessage, timeStamp);
            if (newLogId > -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Node Order!", ""));
                systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS created new log");
                clearAllFields();
                System.out.println("[WAR FILE]===========================Create New Node");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAllFields() {

        action = null;
        actionMessage = null;
        orderId = null;
        OperatorId = null;
        timeStamp = null;

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

    public List<TransportationOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<TransportationOrder> orders) {
        this.orders = orders;
    }

    public List<TransportationOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<TransportationOperator> operators) {
        this.operators = operators;
    }

    public Integer getNewLogId() {
        return newLogId;
    }

    public void setNewLogId(Integer newLogId) {
        this.newLogId = newLogId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(Integer OperatorId) {
        this.OperatorId = OperatorId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
