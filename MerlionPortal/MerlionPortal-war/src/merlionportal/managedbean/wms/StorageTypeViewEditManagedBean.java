/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.StorageType;
import entity.Warehouse;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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

    private List<StorageType> storagetypes;
    private StorageType storagetype;
    private Integer storageTypeId;
    private String description;

    private Integer warehouseId;

    public List<StorageType> getStoragetypes() {
        System.out.println("===============================[In Managed Bean - getStorageTypes]");
        // Dunno how to take in warehouse ID as input for delete function, and dunno why delete function don't update the
        // output shown on webpage after it was deleted
        warehouseId = 8;
        System.out.println("[In Managed Bean - getStorageTypes] warehouse ID : " + warehouseId);
        if (warehouseId != null) {
            if (storagetypes == null) {
                storagetypes = assetManagementSessionBean.viewStorageTypesForAWarehouse(warehouseId);
                if (storagetypes == null) {
                    System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Storage Type. Please check if warehouse ID exists! ", ""));
                }
            }
        } else {

            System.out.println("[In Managed Bean - getStorageTypes] DELETE storageType ID : " + storagetype.getWarehousewarehouseId().getWarehouseId());
            storagetypes = assetManagementSessionBean.viewStorageTypesForAWarehouse(storagetype.getWarehousewarehouseId().getWarehouseId());

        }

        // for checking
        for (Object obj : storagetypes) {
            System.out.println(obj);
        }
        return storagetypes;
    }

    public StorageType getStoragetype() {
        return storagetype;
    }

    public void setStoragetype(StorageType storagetype) {
        this.storagetype = storagetype;
    }

    public Integer getStorageTypeId() {
        return storageTypeId;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
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

    /**
     * Creates a new instance of StorageTypeViewEditManagedBean
     */
    public StorageTypeViewEditManagedBean() {
    }

}
