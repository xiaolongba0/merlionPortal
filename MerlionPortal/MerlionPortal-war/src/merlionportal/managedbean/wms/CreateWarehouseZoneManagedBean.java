/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.SystemUser;
import entity.Warehouse;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "createWarehouseZoneManangedBean")
@ViewScoped
public class CreateWarehouseZoneManagedBean {

    /**
     * Creates a new instance of CreateWarehouseZoneManagerBean
     */
    @EJB
    private AssetManagementSessionBean assetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer userId;

    private Integer newWarehouseZoneId;
    private String warehouseZoneTypeName;
    private String zoneDescription;

    private Integer storageTypeId;
    private Integer selectedWarehouseId;
    private List<Warehouse> warehouses;

    public CreateWarehouseZoneManagedBean() {
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

    public void createNewWarehouseZone(ActionEvent warehouse) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Warehouse Zone");
            System.out.println("STORAGE TYPE NAMEEEEEEE ; " + warehouseZoneTypeName);
            newWarehouseZoneId = assetManagementSessionBean.addWarehouseZone(warehouseZoneTypeName, zoneDescription, companyId, selectedWarehouseId);
            System.out.println("NEW STORAGE TYPE ID =================: " + newWarehouseZoneId);
            if (newWarehouseZoneId > -1) {
                clearAllFields();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Warehouse Zone Added!", ""));
                systemLogSB.recordSystemLog(userId, "WMS created warehouse zone");
            } else {
                System.out.println("============== FAILED TO ADD STORAGE TYPE DUE TO WRONG WAREHOUSE ID ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to Add Warehouse Zone ", ""));

            }

            System.out.println("[WAR FILE]===========================Create New Warehouse Zone");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAllFields() {
        warehouseZoneTypeName = null;
        zoneDescription = null;
        selectedWarehouseId = null;
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

    public Integer getNewWarehouseZoneId() {
        return newWarehouseZoneId;
    }

    public void setNewWarehouseZoneId(Integer newWarehouseZoneId) {
        this.newWarehouseZoneId = newWarehouseZoneId;
    }

    public String getWarehouseZoneName() {
        return warehouseZoneTypeName;
    }

    public void setWarehouseZoneName(String warehouseZoneTypeName) {
        this.warehouseZoneTypeName = warehouseZoneTypeName;
    }

    public String getStoragetDescription() {
        return zoneDescription;
    }

    public void setStoragetDescription(String zoneDescription) {
        this.zoneDescription = zoneDescription;
    }

    public Integer getWarehouseZoneId() {
        return storageTypeId;
    }

    public void setWarehouseZoneId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public Integer getSelectedWarehouseId() {
        return selectedWarehouseId;
    }

    public void setSelectedWarehouseId(Integer selectedWarehouseId) {
        this.selectedWarehouseId = selectedWarehouseId;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}
