/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import javax.inject.Named;
import entity.SystemUser;
import entity.TransportationOrder;
import entity.TransportationLog;
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
import javax.faces.view.ViewScoped;
import merlionportal.tms.transportationhumanresourcemanagementmodule.TOperatormanagementSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tOLogViewEditManagedBean")
@ViewScoped
public class TOLogViewEditManagedBean {

    @EJB
    private TOrderManagementSessionBean tomsb;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private SystemUser loginedUser;
    private Integer companyId;
    private List<TransportationOrder> orders;
    private List<TransportationLog> logs;
    private Integer logId;
    private TransportationLog logg;

    private String action;
    private String actionMessage;
    private Integer orderId;
    private Date timeStamp;

    /**
     * Creates a new instance of TOLogViewEditManagedBean
     */
    public TOLogViewEditManagedBean() {
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
        timeStamp = Calendar.getInstance().getTime();
    }

    public List<TransportationLog> getLogs() {
        System.out.println("===============================[In Managed Bean - view Orders for company Id!]" + companyId);
        logs = tomsb.viewLogforOrder(orderId);

        // for checking
        for (Object obj : logs) {
            System.out.println(obj);
        }
        return logs;
    }

    public void onOrderChange() {
        System.out.println("===============================[In Managed Bean - get Locations]");
        System.out.println("[In Managed Bean - getLocation] location ID : " + orderId);
        if (orderId != null) {
            logs = tomsb.viewLogforOrder(orderId);
            if (orderId == null) {
                System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Storage Type. Please check if warehouse ID exists! ", ""));
            }

        }

    }
    public void deleteLog(TransportationLog log) {
        try {
            logId = log.getLogId();
            System.out.println("[In WAR FILE - Delete Log Function] Log ID========== :" + logId);
            tomsb.deleteLog(logId);
            System.out.println("Successfully deleted logId: " + logId);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("IN ROW EDIT =================");
        logg = new TransportationLog();
        logg = (TransportationLog) event.getObject();
        System.out.println("[AFTER EDIT] Log.GETId(): " + logg.getLogId());
        tomsb.editLog(logg.getLogId(), action, actionMessage, timeStamp);
        FacesMessage msg = new FacesMessage("Transportation Asset with tAsset ID = " + logg.getLogId() + " has sucessfully been edited");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
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

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public TransportationLog getLogg() {
        return logg;
    }

    public void setLogg(TransportationLog logg) {
        this.logg = logg;
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

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

}
