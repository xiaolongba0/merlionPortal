/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.MovingStock;
import entity.Product;
import entity.SystemUser;
import entity.TransportOrder;
import entity.Warehouse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
@Named(value = "transportOrderManagedBean")
@ViewScoped
public class TransportOrderManagedBean {

    @EJB
    private UserAccountManagementSessionBean uamb;

    @EJB
    private TransportOrderSessionBean tosb;

    @EJB
    private SystemLogSessionBean systemLogSB;

    @EJB
    private AssetManagementSessionBean amsb;

    @EJB
    private ProductSessionBean psb;

    private Integer companyId;
    private SystemUser loginedUser;
    private Integer userId;
    private Integer radioValue;

    private List<Warehouse> warehouses;
    private List<Product> productList;
    private List<TransportOrder> transportOrders;

    private Date transportDate;
    private Integer staffId;
    private Integer productId;
    private Integer totalQuantity;
    private Integer sourceWarehouseId;
    private Integer destWarehouseId;
    private String toStatus;

    private TransportOrder transportOrder;
    private List<MovingStock> movingStocks;

//    private Integer sourceWarehouseIdtemp;
//    private Integer destWarehouseIdtemp;
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
        warehouses = amsb.viewMyWarehouses(companyId);
        productList = psb.getMyProducts(companyId);
    }

    public void createTransportOrder() {
        System.out.println("[IN MANAGED BEAN -- TRANSPORT ORDER MB] ====================== add transport order");

        boolean result = tosb.addTransportOrder(transportDate, staffId, productId, totalQuantity, sourceWarehouseId, destWarehouseId);
        if (result) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! Transport Order Added!", ""));
            systemLogSB.recordSystemLog(userId, "WMS create transport order");
        } else {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ""));
        }
    }

    private void clearAllFields() {
        transportDate = null;
        staffId = null;
        productId = null;
        totalQuantity = null;
        sourceWarehouseId = null;
        destWarehouseId = null;
    }

    public void viewSourceWarehouses() {
        System.out.println("[In Managed Bean - viewSourceWarehouses] =============================== Source Warehouse ID : " + sourceWarehouseId);

        if (sourceWarehouseId != null) {
            transportOrders = tosb.viewAllTransportOrdersForSource(sourceWarehouseId);
//            systemLogSB.recordSystemLog(userId, "WMS view transport orders from source warehouse");
            if (sourceWarehouseId == null) {
                System.out.println("============== FAILED TO VIEW SOURCE WAREHOUSES ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View", ""));
            }
        }
    }

    public void viewDestWarehouses() {
        System.out.println("[In Managed Bean - viewDestWarehouses] =============================== Dest Warehouse ID : " + destWarehouseId);

        if (destWarehouseId != null) {
            transportOrders = tosb.viewAllTransportOrdersForDest(destWarehouseId);
//            systemLogSB.recordSystemLog(userId, "WMS view transport orders from dest warehouse");
            if (destWarehouseId == null) {
                System.out.println("============== FAILED TO VIEW SOURCE WAREHOUSES ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View", ""));
            }
        }
    }

    public String getSourceOrder(TransportOrder transportOrder) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        TransportOrder selectedOrder = transportOrder;
        sessionMap.put("selectedOrder", selectedOrder);
        System.out.println("GETORDER ========" + selectedOrder);

        return "viewstockmovingout?faces-redirect=true";
    }

    public String getDestOrder(TransportOrder transportOrder) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        TransportOrder selectedOrder = transportOrder;
        sessionMap.put("selectedOrder", selectedOrder);
        System.out.println("GETORDER ========" + selectedOrder);

        return "viewstockcoming?faces-redirect=true";
    }

    public void cancelOrder(TransportOrder transportOrder) {
        System.out.println("In TOMB, cancel order ======================");

        boolean result = tosb.cancelTransportOrder(transportOrder.getTransportOrderId());
        if (result) {
            transportOrder.setStatus(1);
            systemLogSB.recordSystemLog(userId, "WMS cancel transport order");
            FacesMessage msg = new FacesMessage("Transport Order with ID = " + transportOrder.getTransportOrderId() + " has sucessfully been cancelled");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ""));
        }

    }  

    public String getTransportOrderStatus(int transportOrderStatus) {
        if (transportOrderStatus == 0) {
            toStatus = "Not Processed";
        }
        if (transportOrderStatus == 1) {
            toStatus = "Cancelled";
        }
        if (transportOrderStatus == 2) {
            toStatus = "Sent";
        }
        if (transportOrderStatus == 3) {
            toStatus = "Completed";
        }

        return toStatus;
    }

    /**
     * Creates a new instance of TransportOrderManagedBean
     */
    public TransportOrderManagedBean() {
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

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Date getTransportDate() {
        return transportDate;
    }

    public void setTransportDate(Date transportDate) {
        this.transportDate = transportDate;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getSourceWarehouseId() {
        return sourceWarehouseId;
    }

    public void setSourceWarehouseId(Integer sourceWarehouseId) {
        this.sourceWarehouseId = sourceWarehouseId;
    }

    public Integer getDestWarehouseId() {
        return destWarehouseId;
    }

    public void setDestWarehouseId(Integer destWarehouseId) {
        this.destWarehouseId = destWarehouseId;
    }

    public Integer getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(Integer radioValue) {
        this.radioValue = radioValue;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<TransportOrder> getTransportOrders() {
        return transportOrders;
    }

    public void setTransportOrders(List<TransportOrder> transportOrders) {
        this.transportOrders = transportOrders;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    public List<MovingStock> getMovingStocks() {
        return movingStocks;
    }

    public void setMovingStocks(List<MovingStock> movingStocks) {
        this.movingStocks = movingStocks;
    }

}
