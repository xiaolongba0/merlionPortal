/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.MovingStock;
import entity.SystemUser;
import entity.TransportOrder;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.TransportOrderSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "movingStockViewManagedBean")
@ViewScoped
public class MovingStockViewManagedBean {

    @EJB
    private UserAccountManagementSessionBean uamb;

    @EJB
    private TransportOrderSessionBean tosb;

    @EJB
    private SystemLogSessionBean systemLogSB;

    @EJB
    private AssetManagementSessionBean amsb;

    private Integer companyId;
    private SystemUser loginedUser;
    private Integer userId;

    private TransportOrder selectedOrder;
    private List<MovingStock> sourceMovingStocks;
    private List<MovingStock> destMovingStocks;

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
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
        selectedOrder = (TransportOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
        getSourceMovingStocks(selectedOrder);
        getDestMovingStocks(selectedOrder);
        System.out.println("[In Moving Stock View Managed Bean] ================ Selected Order: " + selectedOrder);
    }

    public void getSourceMovingStocks(TransportOrder transportOrder) {
        Integer transportOrderId = transportOrder.getTransportOrderId();
        System.out.println("[In Managed Bean - getSourceMovingStocks] ===============================: " + transportOrderId);
        if (transportOrderId != null) {
            sourceMovingStocks = tosb.viewSourceMovingStock(transportOrderId);
            System.out.println("SourceMovingStocks = " + sourceMovingStocks);
            systemLogSB.recordSystemLog(userId, "WMS view moving stock from source warehouse");
            if (transportOrderId == null) {
                System.out.println("============== FAILED TO VIEW MOVING STOCKS ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View", ""));
            }
        }

    }

    public void getDestMovingStocks(TransportOrder transportOrder) {
        Integer transportOrderId = transportOrder.getTransportOrderId();
        System.out.println("[In Managed Bean - getdestMovingStocks] ===============================: " + transportOrderId);
        if (transportOrderId != null) {
            destMovingStocks = tosb.viewDestMovingStock(transportOrderId);
            systemLogSB.recordSystemLog(userId, "WMS view moving stock from dest warehouse");
            if (transportOrderId == null) {
                System.out.println("============== FAILED TO VIEW MOVING STOCKS ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View", ""));
            }
        }
    }

    public void sendOrder(TransportOrder transportOrder) {
        System.out.println("In MovingStockViewManagedBean, send order ======================" + transportOrder);

        boolean result = tosb.sendTransportOrder(transportOrder.getTransportOrderId());
        if (result) {
            systemLogSB.recordSystemLog(userId, "WMS send tranrsport order");
            FacesMessage msg = new FacesMessage("Transport Order with ID = " + transportOrder.getTransportOrderId() + " has sucessfully been processed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ""));
        }

    }

        public void receiveOrder(TransportOrder transportOrder) {
        System.out.println("In MovingStockViewManagedBean, receive order ======================" + transportOrder);

        boolean result = tosb.receiveTransportOrder(transportOrder.getTransportOrderId());
        if (result) {
            systemLogSB.recordSystemLog(userId, "WMS receive transport order");
            FacesMessage msg = new FacesMessage("Transport Order with ID = " + transportOrder.getTransportOrderId() + " has sucessfully been processed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ""));
        }

    }
    /**
     * Creates a new instance of TransportOrderViewManagedBean
     */
    public MovingStockViewManagedBean() {
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public TransportOrder getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(TransportOrder selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public List<MovingStock> getSourceMovingStocks() {
        return sourceMovingStocks;
    }

    public void setSourceMovingStocks(List<MovingStock> sourceMovingStocks) {
        this.sourceMovingStocks = sourceMovingStocks;
    }

    public List<MovingStock> getDestMovingStocks() {
        return destMovingStocks;
    }

    public void setDestMovingStocks(List<MovingStock> destMovingStocks) {
        this.destMovingStocks = destMovingStocks;
    }

}
