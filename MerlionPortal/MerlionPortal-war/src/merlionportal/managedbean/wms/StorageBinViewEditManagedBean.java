/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.StorageBin;
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
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author yunwei
 */
@Named(value = "storageBinViewEditManagedBean")
@ViewScoped
public class StorageBinViewEditManagedBean {

    @EJB
    private AssetManagementSessionBean assetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private List<Warehouse> warehouses;
    private List<WarehouseZone> warehouseZones;
    private List<StorageBin> bins;
    private Integer warehouseId;
    private Integer storageTypeId;
    private StorageBin bin;

    private SystemUser loginedUser;
    private Integer companyId;
     private Integer userId;


    /**
     * Creates a new instance of StorageBinViewEditManagedBean
     */
    public StorageBinViewEditManagedBean() {
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
        warehouses = assetManagementSessionBean.viewMyWarehouses(companyId);

    }

    public void onWarehouseChange() {
        if (warehouseId != null) {
            warehouseZones = assetManagementSessionBean.viewWarehouseZoneForAWarehouse(warehouseId);
        }
    }

    public void onWarehouseZoneChange() {
        if (storageTypeId != null) {
            bins = assetManagementSessionBean.viewStorageBinForWarehouseZone(storageTypeId);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        bin = new StorageBin();
        bin = (StorageBin) event.getObject();
        boolean result = assetManagementSessionBean.editStorageBin(bin.getBinName(), bin.getDescription(),bin.getStorageBinId());
        if (result) {
            systemLogSB.recordSystemLog(userId, "WMS edit storage bin");
            FacesMessage msg = new FacesMessage("Bin with Storage Bin ID = " + bin.getStorageBinId() + " has sucessfully been edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Edit Cancelled"));
    }

    public void deleteStorageBin(StorageBin bin) {
        try {
            boolean result = assetManagementSessionBean.deleteStorageBin(bin.getStorageBinId());
            if (result) {
                bins.remove(bin);
                systemLogSB.recordSystemLog(userId, "WMS delete storage bin");
                FacesMessage msg = new FacesMessage("Bin with Storage Bin ID = " + bin.getStorageBinId() + " has sucessfully been deleted");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Please check if Bin contains Stock.", ""));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public List<String> getStorageBinType() {
//        storageBinType = assetManagementSessionBean.listStorageBinTypes();
//        System.out.println("[In WAR FILE - get storage bin type]" + storageBinType);
//        return storageBinType;
//    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<WarehouseZone> getWarehouseZones() {
        return warehouseZones;
    }

    public void setWarehouseZones(List<WarehouseZone> warehouseZones) {
        this.warehouseZones = warehouseZones;
    }

   

    public List<StorageBin> getBins() {
        return bins;
    }

    public void setBins(List<StorageBin> bins) {
        this.bins = bins;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getStorageTypeId() {
        return storageTypeId;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public StorageBin getBin() {
        return bin;
    }

    public void setBin(StorageBin bin) {
        this.bin = bin;
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
