/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.SystemUser;
import entity.TransportOrder;
import entity.Warehouse;
import entity.WmsOrder;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import merlionportal.wms.mobilitymanagementmodule.WMSOrderSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "internalWarehouseOrderManagedBean")
@ViewScoped
public class InternalWarehouseOrderManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private WMSOrderSessionBean osb;
    @EJB
    private AssetManagementSessionBean amsb;
    @EJB
    private ProductSessionBean psb;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer userId;
    private Integer radioValue;

    private Integer myCompanyId;
    private String orderType;
    private Date fulfillmentDate;
    private Date receiveDate;
    private Integer quantity;
    private Boolean internalOrder;
    private Integer servicePOId;
    private String binType;

    private Integer warehouseId;
    private List<Warehouse> warehouses;

    private Integer productId;
    private List<Product> productList;
    private Set<String> binTypes;

    /**
     * Creates a new instance of InternalWarehouseOrderManagedBean
     */
    public InternalWarehouseOrderManagedBean() {
    }

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

    public void createNewWarehouseOrder() {

        try {
            System.out.println("[INSIDE INTERNAL WAREHOUSE ORDER MB]===========================Warehouse Order, productId" + productId);
            myCompanyId = companyId;
            internalOrder = true;
            Integer newOrderId = osb.createInternalOrder(myCompanyId, orderType, fulfillmentDate, receiveDate, quantity, productId, internalOrder, servicePOId, warehouseId, binType);
            if (newOrderId != null & productId != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! New Order with  ID = " + newOrderId + " is created!", ""));
                clearAllFields();
                systemLogSB.recordSystemLog(userId, "WMS create warehouse internal order");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void getBinTypes (Integer warehouseId){
        
        binTypes = osb.displayBinType(warehouseId);
    }

    public void clearAllFields() {

        myCompanyId = null;
        orderType = null;
        fulfillmentDate = null;
        receiveDate = null;
        quantity = null;
        productId = null;
        internalOrder = null;
        servicePOId = null;
        warehouseId = null;
        binType = null;

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

    public Integer getMyCompanyId() {
        return myCompanyId;
    }

    public void setMyCompanyId(Integer myCompanyId) {
        this.myCompanyId = myCompanyId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getFulfillmentDate() {
        return fulfillmentDate;
    }

    public void setFulfillmentDate(Date fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Boolean getInternalOrder() {
        return internalOrder;
    }

    public void setInternalOrder(Boolean internalOrder) {
        this.internalOrder = internalOrder;
    }

    public Integer getServicePOId() {
        return servicePOId;
    }

    public void setServicePOId(Integer servicePOId) {
        this.servicePOId = servicePOId;
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

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public Set<String> getBinTypes() {
        return binTypes;
    }

    public void setBinTypes(Set<String> binTypes) {
        this.binTypes = binTypes;
    }

}
