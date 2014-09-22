/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.SystemUser;
import entity.Warehouse;
import entity.StorageType;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "assetManagedBean")
@RequestScoped
public class AssetManagedBean {

    @EJB
    private AssetManagementSessionBean assetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    private Integer newWarehouseId;
    private Integer warehouseId;
    private Integer storageTypeId;
    
    private String warehouseName;
    private String country;
    private String city;
    private String street;
    private String description;
    private Integer zipcode;
    private Integer companyId;
    private Warehouse warehouse;

    private Integer newStorageTypeId;
    private String storageTypeName;
    private String storagetDescription;

    private SystemUser loginedUser;

    /**
     * Creates a new instance of AssestManagedBean
     */
    public AssetManagedBean() {
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

    }

    public void createNewWarehouse(ActionEvent warehouse) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Warehouse");
            newWarehouseId = assetManagementSessionBean.addNewWarehouse(warehouseName, country, city, street, description, zipcode, companyId);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Warehouse Added!", ""));
            System.out.println("[WAR FILE]===========================Create New Warehouse");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createNewStorageType(ActionEvent warehouse) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Storage Type");
            System.out.println("STORAGE TYPE NAMEEEEEEE ; " + storageTypeName);
            newStorageTypeId = assetManagementSessionBean.addStorageType(storageTypeName, storagetDescription, companyId, warehouseId);
            System.out.println("NEW STORAGE TYPE ID =================: " + newStorageTypeId);
            if (newStorageTypeId == -1) {
                System.out.println("============== FAILED TO ADD STORAGE TYPE DUE TO WRONG WAREHOUSE ID ===============");

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to Add Storage Type. Please check warehouseID! ", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Storage Type Added!", ""));
            }

            System.out.println("[WAR FILE]===========================Create New Storage Type");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteWarehouse(Warehouse warehouse) {
        try {
            warehouseId = warehouse.getWarehouseId();
            System.out.println("[In WAR FILE - Delete Warehouse Function] Warehouse ID========== :" + warehouseId);
            assetManagementSessionBean.deleteWarehouse(companyId, warehouseId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteStorageType(StorageType storageType) {
        try {
            System.out.println("[In WAR FILE - Delete StorageType Function]" + storageType);
            storageTypeId = storageType.getStorageTypeId();
            System.out.println("[In WAR FILE - Delete StorageType Function] storageType ID========== :" + storageTypeId);
            assetManagementSessionBean.deleteStorageType(storageTypeId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getNewWarehouseId() {
        return newWarehouseId;
    }

    public void setNewWarehouseId(Integer newWarehouseId) {
        this.newWarehouseId = newWarehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getNewStorageTypeId() {
        return newStorageTypeId;
    }

    public void setNewStorageTypeId(Integer newStorageTypeId) {
        this.newStorageTypeId = newStorageTypeId;
    }

    public String getStorageTypeName() {
        return storageTypeName;
    }

    public void setStorageTypeName(String storageTypeName) {
        this.storageTypeName = storageTypeName;
    }

    public String getStoragetDescription() {
        return storagetDescription;
    }

    public void setStoragetDescription(String storagetDescription) {
        this.storagetDescription = storagetDescription;
    }

}
