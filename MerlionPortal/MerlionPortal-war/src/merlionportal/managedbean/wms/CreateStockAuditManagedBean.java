/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.StorageBin;
import entity.WarehouseZone;
import entity.SystemUser;
import entity.Warehouse;
import java.io.IOException;
import java.util.Date;
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
import merlionportal.wms.warehousemanagementmodule.StockAuditSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "stockAuditManagedBean")
@ViewScoped
public class CreateStockAuditManagedBean {

    /**
     * Creates a new instance of StockAuditManagedBean
     */
    public CreateStockAuditManagedBean() {
    }
    @EJB
    private StockAuditSessionBean sasb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private ProductSessionBean psb;
    @EJB
    private AssetManagementSessionBean amsb;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer radioValue;
    private Integer userId;

    private Integer productId;
    private List<Product> productList;

    private Integer warehouseId;
    private List<Warehouse> warehouses;

    private Integer warehouseZoneId;
    private List<WarehouseZone> warehouseZones;

    private Integer storageBinId;
    private List<String> listStorageBinType;
    private List<StorageBin> storageBins;

    private Integer staffId;
    private Date scheduledDate;
    private Date actualDate;
    private Integer stockAuditStatus;
    private Integer expectedQuantity;
    private Integer passQuantity;
    private Integer failQuantity;
    private Integer actualQuantity;
    private String remarks;

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

    public void addStockAudit() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK AUDIT MB] ====================== add stock audit, productId: " + productId);

        boolean result = sasb.addStockAudit(storageBinId, staffId, scheduledDate, actualDate, stockAuditStatus,expectedQuantity, passQuantity, failQuantity, actualQuantity, remarks);
        if (result) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! Stock Audit Schedule Added!", ""));
            systemLogSB.recordSystemLog(userId, "WMS add stock audit");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Please check if storage bin is empty!", ""));
        }
    }

    private void clearAllFields() {
        warehouseId = null;
        warehouseZoneId = null;
        storageBinId = null;
        productId = null;
        staffId = null;
        staffId = null;
        scheduledDate = null;
        actualDate = null;
        stockAuditStatus = null;
        expectedQuantity = null;
        passQuantity = null;
        failQuantity = null;
        actualQuantity = null;
        remarks = null;
        radioValue = null;
    }

    public void onChangeWarehouse() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK AUDIT MB] ====================== onChangeWarehouse, Warehouse ID: " + warehouseId);
        if (warehouseId != null) {
            warehouseZones = amsb.viewWarehouseZoneForAWarehouse(warehouseId);
        }
    }

    public void onChangeWarehouseZone() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK AUDIT MB] ====================== onChangeWarehouseZone");
        if (warehouseId != null & warehouseZoneId != null) {
            storageBins = amsb.viewStorageBinForWarehouseZone(warehouseZoneId);
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public Integer getStockAuditStatus() {
        return stockAuditStatus;
    }

    public void setStockAuditStatus(Integer stockAuditStatus) {
        this.stockAuditStatus = stockAuditStatus;
    }

    public Integer getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(Integer expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public Integer getPassQuantity() {
        return passQuantity;
    }

    public void setPassQuantity(Integer passQuantity) {
        this.passQuantity = passQuantity;
    }

    public Integer getFailQuantity() {
        return failQuantity;
    }

    public void setFailQuantity(Integer failQuantity) {
        this.failQuantity = failQuantity;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(Integer radioValue) {
        this.radioValue = radioValue;
    }

    public Integer getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Integer storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<Warehouse> getWarehouses() {
        System.out.println("IN getWarehouses Function");
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Integer getWarehouseZoneId() {
        return warehouseZoneId;
    }

    public void setWarehouseZoneId(Integer storageTypeId) {
        this.warehouseZoneId = storageTypeId;
    }

    public List<WarehouseZone> getWarehouseZones() {
        return warehouseZones;
    }

    public void setWarehouseZones(List<WarehouseZone> storageTypes) {
        this.warehouseZones = storageTypes;
    }

    public List<String> getListStorageBinType() {
        return listStorageBinType;
    }

    public void setListStorageBinType(List<String> listStorageBinType) {
        this.listStorageBinType = listStorageBinType;
    }

    public List<StorageBin> getStorageBins() {
        return storageBins;
    }

    public void setStorageBins(List<StorageBin> storageBins) {
        this.storageBins = storageBins;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
