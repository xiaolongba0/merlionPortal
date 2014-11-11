/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.SystemUser;
import entity.Warehouse;
import entity.WmsOrder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.wms.mobilitymanagementmodule.ReceivingPutawaySessionBean;
import merlionportal.wms.mobilitymanagementmodule.WMSOrderSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "receivingPutawayManagedBean")
@ViewScoped
public class ReceivingPutawayManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;

    @EJB
    private ReceivingPutawaySessionBean rpsb;

    @EJB
    private AssetManagementSessionBean amsb;

    @EJB
    private WMSOrderSessionBean osb;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer userId;

    private Integer warehouseId;
    private Integer warehouseId2;
    private List<Warehouse> warehouses;

    private List<WmsOrder> wmsOrders;
    private Boolean internalOrder;

    /**
     * Creates a new instance of ReceivingPutawayManagedBean
     */
    public ReceivingPutawayManagedBean() {
    }

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
        warehouses = amsb.viewMyWarehouses(companyId);
    }

    public void viewMyOrdersToBeReceived() {
        System.out.println("[In Managed Bean - viewOrdersToBeReceived] =============================== Source company ID : " + companyId);

        if (warehouseId != null) {
            wmsOrders = osb.viewMyIncomingOrders(companyId, warehouseId);
            if (wmsOrders == null) {
                System.out.println("NULL NULL NULL check it out!");
            } else {
                System.out.println("order " + wmsOrders);

            }
            systemLogSB.recordSystemLog(userId, "WMS view my orders to be received");
        }
    }

    // view others order that are coming to my warehouse 
    public void viewOthersOrderToBeReceived() {
        System.out.println("[In Managed Bean - viewOthersOrderToBeReceived] =============================== Source company ID : " + companyId);

        if (warehouseId2 != null) {
            wmsOrders = osb.viewOthersIncomingOrders(companyId, warehouseId2);
            System.out.println("WMS ORDERS = " + wmsOrders);
            if (wmsOrders == null) {
                System.out.println("NULL NULL NULL check it out!");
            } else {
                System.out.println("order " + wmsOrders);
            }
            systemLogSB.recordSystemLog(userId, "WMS view others orders to be received");
        }
    }

    public String getOrder(WmsOrder wmsOrder) {
        System.out.println("RECEVINGPUTAWAYMANAGEDBEAN =============== getOrder " + wmsOrder);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        WmsOrder selectedOrder = wmsOrder;
        sessionMap.put("selectedOrder", selectedOrder);
        System.out.println("GETORDER ========" + selectedOrder);

        return "receivingputawayprocess?faces-redirect=true";
    }

    public void rejectOrder(WmsOrder wmsOrder) {
        System.out.println("RECEVINGPUTAWAYMANAGEDBEAN =============== rejectOrder " + wmsOrder.getOrderId());
        boolean result = osb.rejectOrder(wmsOrder.getOrderId());
        if (result) {
            wmsOrder.setStatus("Rejected");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! Order rejected!", ""));
            systemLogSB.recordSystemLog(userId, "WMS reject receiving putaway order");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! ", ""));
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<WmsOrder> getWmsOrders() {
        return wmsOrders;
    }

    public void setWmsOrders(List<WmsOrder> wmsOrders) {
        this.wmsOrders = wmsOrders;
    }

    public Boolean isInternalOrder() {
        return internalOrder;
    }

    public void setInternalOrder(Boolean internalOrder) {
        this.internalOrder = internalOrder;
    }

    public Integer getWarehouseId2() {
        return warehouseId2;
    }

    public void setWarehouseId2(Integer warehouseId2) {
        this.warehouseId2 = warehouseId2;
    }

}
