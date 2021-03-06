/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.managedbean.wms;

import entity.SystemUser;
import entity.WmsOrder;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.wms.mobilitymanagementmodule.WMSOrderSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "orderFulfillmentProcessManagedBean")
@ViewScoped
public class OrderFulfillmentProcessManagedBean {

    /**
     * Creates a new instance of OrderFulfillmentProcessManagedBean
     */
    public OrderFulfillmentProcessManagedBean() {
    }
    
     @EJB
    private SystemLogSessionBean systemLogSB;

    @EJB
    private WMSOrderSessionBean osb;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer userId;

    private WmsOrder selectedOrder;
    private Integer wmsOrderId;
    
     @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            if (userId != null) {
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
        selectedOrder = (WmsOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
    }
    
      public void carryOutOrderFulfillment() {
        Integer wmsOrderId = selectedOrder.getOrderId();
        System.out.println("[In Managed Bean - carryOutOrderFulfillment] ===============================: " + wmsOrderId);
        if (wmsOrderId != null) {
            osb.performOrderFulfillment(companyId, wmsOrderId);
            systemLogSB.recordSystemLog(userId, "WMS order fulfillment process");
            if (wmsOrderId == null) {
                System.out.println("============== FAILED TO CARRY OUT RECEIVING PUTAWAY ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View", ""));
            }
        }

    }

    public WMSOrderSessionBean getOsb() {
        return osb;
    }

    public void setOsb(WMSOrderSessionBean osb) {
        this.osb = osb;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public WmsOrder getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(WmsOrder selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public Integer getWmsOrderId() {
        return wmsOrderId;
    }

    public void setWmsOrderId(Integer wmsOrderId) {
        this.wmsOrderId = wmsOrderId;
    }
    
}
