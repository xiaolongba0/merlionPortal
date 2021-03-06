/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.SystemUser;
import entity.Warehouse;
import entity.WarehouseZone;
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
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "createStorageBinManagedBean")
@ViewScoped
public class CreateStorageBinManagedBean {

    /**
     * Creates a new instance of CreateStorageBinManagerBean
     */
    @EJB
    private AssetManagementSessionBean amsb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private Integer warehouseId;
    private List<Warehouse> warehouses;

    private Integer storageTypeId;
    private List<WarehouseZone> warehouseZones;

    private String storageBinName;
    private String storagebinDescription;
    private String storageBinType;
    private Integer maxQuantity;
    private Double maxWeight;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer userId;
    private Boolean rented;
    private Integer rentedCompanyId;

    private List<String> listStorageBinType;

    public CreateStorageBinManagedBean() {
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
    }

    public void createStorageBin() {
        Integer storageBinId = amsb.addStorageBin(storageBinName, storageBinType, storagebinDescription, maxQuantity, maxWeight, storageTypeId, rented, rentedCompanyId);
        System.out.println("in CSBMB, createstoragebin ======================== rentedCompanyId = " + rentedCompanyId);
        if (storageBinId != null) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! New Storage Bin with Bin ID ="+ storageBinId+ "created!", ""));
            systemLogSB.recordSystemLog(userId, "WMS create storage bin");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void onChangeWarehouse() {
        System.out.println("[IN MANAGED BEAN -- Create BIN MB] ====================== onChangeWarehouse, Warehouse ID: " + warehouseId);
        if (warehouseId != null) {
            warehouseZones = amsb.viewWarehouseZoneForAWarehouse(warehouseId);
        }
    }

    private void clearAllFields() {
        warehouseId = null;
        storageTypeId = null;
        storageBinName = null;
        storagebinDescription = null;
        storageBinType = null;
        maxQuantity = null;
        maxWeight = null;
        rented = null;
        rentedCompanyId = null;
    }

    public void setListStorageBinType(List<String> listStorageBinType) {
        this.listStorageBinType = listStorageBinType;
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

    public Integer getStorageTypeId() {
        return storageTypeId;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public List<WarehouseZone> getWarehouseZones() {
        return warehouseZones;
    }

    public void setWarehouseZones(List<WarehouseZone> warehouseZones) {
        this.warehouseZones = warehouseZones;
    }

    public String getStorageBinName() {
        return storageBinName;
    }

    public void setStorageBinName(String storageBinName) {
        this.storageBinName = storageBinName;
    }

    public String getStoragebinDescription() {
        return storagebinDescription;
    }

    public void setStoragebinDescription(String storagebinDescription) {
        this.storagebinDescription = storagebinDescription;
    }

    public String getStorageBinType() {
        return storageBinType;
    }

    public void setStorageBinType(String storageBinType) {
        this.storageBinType = storageBinType;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
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

    public Boolean getRented() {
        return rented;
    }

    public void setRented(Boolean rented) {
        this.rented = rented;
    }

    public Integer getRentedCompanyId() {
        return rentedCompanyId;
    }

    public void setRentedCompanyId(Integer rentedCompanyId) {
        this.rentedCompanyId = rentedCompanyId;
    }

}
