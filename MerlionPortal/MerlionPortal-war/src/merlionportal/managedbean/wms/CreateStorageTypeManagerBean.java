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
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "createStorageTypeManagerBean")
@ViewScoped
public class CreateStorageTypeManagerBean {

    /**
     * Creates a new instance of CreateStorageTypeManagerBean
     */
    @EJB
    private AssetManagementSessionBean assetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private SystemUser loginedUser;
    private Integer companyId;

    private Integer newStorageTypeId;
    private String storageTypeName;
    private String storagetDescription;

    private Integer storageTypeId;
    private Integer warehouseId;
    private List<Warehouse> warehouses;

    
    public CreateStorageTypeManagerBean() {
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
        warehouses = assetManagementSessionBean.viewMyWarehouses(companyId);
    }
    
    

    public void createNewStorageType(ActionEvent warehouse) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Storage Type");
            System.out.println("STORAGE TYPE NAMEEEEEEE ; " + storageTypeName);
            newStorageTypeId = assetManagementSessionBean.addStorageType(storageTypeName, storagetDescription, companyId, warehouseId);
            System.out.println("NEW STORAGE TYPE ID =================: " + newStorageTypeId);
            if (newStorageTypeId == -1) {
                System.out.println("============== FAILED TO ADD STORAGE TYPE DUE TO WRONG WAREHOUSE ID ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to Add Storage Type. Please check warehouseID! ", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Storage Type Added!", ""));
            }

            System.out.println("[WAR FILE]===========================Create New Storage Type");
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public Integer getStorageTypeId() {
        return storageTypeId;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
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
}

