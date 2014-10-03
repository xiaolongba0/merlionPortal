/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.StorageType;
import entity.SystemUser;
import entity.Warehouse;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "createStorageBinManagerBean")
@ViewScoped
public class CreateStorageBinManagedBean {

    /**
     * Creates a new instance of CreateStorageBinManagerBean
     */
    @EJB
    private AssetManagementSessionBean amsb;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private Integer warehouseId;
    private List<Warehouse> warehouses;

    private Integer storageTypeId;
    private List<StorageType> storageTypes;

    private String storageBinName;
    private String storagebinDescription;
    private String storageBinType;
    private Integer maxQuantity;
    private Double maxWeight;

    private SystemUser loginedUser;
    private Integer companyId;

    private List<String> listStorageBinType;

    public CreateStorageBinManagedBean() {
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
        warehouses = amsb.viewMyWarehouses(companyId);
    }

    public void createStorageBin() {
        boolean result = amsb.addStorageBin(storageBinName, storageBinType, storagebinDescription, maxQuantity, maxWeight, storageTypeId);
        if (result) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "New Storage Bin created!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void onChangeWarehouse() {
        if (warehouseId != null) {
            storageTypes = amsb.viewStorageTypesForAWarehouse(warehouseId);
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

    }

    public List<String> getListStorageBinType() {
        listStorageBinType = amsb.listStorageBinTypes();
        System.out.println("[In WAR FILE - get storage bin type]" + listStorageBinType);
        return listStorageBinType;
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

    public List<StorageType> getStorageTypes() {
        return storageTypes;
    }

    public void setStorageTypes(List<StorageType> storageTypes) {
        this.storageTypes = storageTypes;
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

}
