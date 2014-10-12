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
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author YunWei
 */
@Named(value = "storageTypeViewEditManagedBean")
@ViewScoped
public class StorageTypeViewEditManagedBean {

    @EJB
    private AssetManagementSessionBean assetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<Warehouse> warehouses;
    private List<StorageType> storagetypes;
    private Integer warehouseId;
    private StorageType storagetype;

    private SystemUser loginedUser;
    private Integer companyId;

    /**
     * Creates a new instance of StorageTypeViewEditManagedBean
     */
    public StorageTypeViewEditManagedBean() {
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

    public List<StorageType> onWarehouseChange() {
        System.out.println("===============================[In Managed Bean - onWarehouseChange]");
        System.out.println("[In Managed Bean - onWarehouseChange] warehouse ID : " + warehouseId);
        if (warehouseId != null) {
            storagetypes = assetManagementSessionBean.viewStorageTypesForAWarehouse(warehouseId);
            if (storagetypes == null) {
                System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Warehouse Zone.", ""));
            }
            return storagetypes;
        }
        return null;
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("ON ROW EDIT ===============================");
        storagetype = new StorageType();
        storagetype = (StorageType) event.getObject();
        // System.out.println("[Checking if input is correct] ====================== : " + warehouseName);
        System.out.println("[AFTER EDIT] storageType.getName(): " + storagetype.getName());
        assetManagementSessionBean.editStorageType(storagetype.getName(), storagetype.getDescription(), storagetype.getStorageTypeId());
        FacesMessage msg = new FacesMessage("Storage Type with Storage Type ID = " + storagetype.getStorageTypeId() + " has sucessfully been edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        System.out.println("ON ROW CANCEL =============================");
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deleteStorageType(StorageType storageType) {
        try {
            System.out.println("[In WAR FILE - Delete StorageType Function]" + storageType);
            System.out.println("[In WAR FILE - Delete StorageType Function] storageType ID========== :" + storageType.getStorageTypeId());
            assetManagementSessionBean.deleteStorageType(storageType.getStorageTypeId());
            storagetypes.remove(storageType);
            FacesMessage msg = new FacesMessage("Storage Type with Storage Type ID = " + storagetype.getStorageTypeId() + " has sucessfully been edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<StorageType> getStoragetypes() {
        return storagetypes;
    }

    public void setStoragetypes(List<StorageType> storagetypes) {
        this.storagetypes = storagetypes;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public StorageType getStoragetype() {
        return storagetype;
    }

    public void setStoragetype(StorageType storagetype) {
        this.storagetype = storagetype;
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
