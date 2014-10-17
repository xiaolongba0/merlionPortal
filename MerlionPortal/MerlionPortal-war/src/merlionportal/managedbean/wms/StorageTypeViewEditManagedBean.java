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
    private List<WarehouseZone> warehouseZones;
    private Integer warehouseId;
    private WarehouseZone warehouseZone;

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

    public List<WarehouseZone> onWarehouseChange() {
        System.out.println("===============================[In Managed Bean - onWarehouseChange]");
        System.out.println("[In Managed Bean - onWarehouseChange] warehouse ID : " + warehouseId);
        if (warehouseId != null) {
            warehouseZones = assetManagementSessionBean.viewWarehouseZoneForAWarehouse(warehouseId);
            if (warehouseZones == null) {
                System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Warehouse Zone.", ""));
            }
            return warehouseZones;
        }
        return null;
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("ON ROW EDIT ===============================");
        warehouseZone = new WarehouseZone();
        warehouseZone = (WarehouseZone) event.getObject();
        // System.out.println("[Checking if input is correct] ====================== : " + warehouseName);
        System.out.println("[AFTER EDIT] warehouseZone.getName(): " + warehouseZone.getName());
        assetManagementSessionBean.editWarehouseZone(warehouseZone.getName(), warehouseZone.getDescription(), warehouseZone.getWarehouseZoneId());
        FacesMessage msg = new FacesMessage("Storage Type with Storage Type ID = " + warehouseZone.getWarehouseZoneId()+ " has sucessfully been edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        System.out.println("ON ROW CANCEL =============================");
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deleteStorageType(WarehouseZone warehouseZone) {
        try {
            System.out.println("[In WAR FILE - Delete warehouseZone Function]" + warehouseZone);
            System.out.println("[In WAR FILE - Delete warehouseZone Function] warehouseZone ID========== :" + warehouseZone.getWarehouseZoneId());
            assetManagementSessionBean.deleteWarehouseZone(warehouseZone.getWarehouseZoneId());
            warehouseZones.remove(warehouseZone);
            FacesMessage msg = new FacesMessage("Storage Type with Storage Type ID = " + warehouseZone.getWarehouseZoneId()+ " has sucessfully been edited");
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

    
    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<WarehouseZone> getWarehouseZones() {
        return warehouseZones;
    }

    public void setWarehouseZones(List<WarehouseZone> warehouseZones) {
        this.warehouseZones = warehouseZones;
    }

    public WarehouseZone getWarehouseZone() {
        return warehouseZone;
    }

    public void setWarehouseZone(WarehouseZone warehouseZone) {
        this.warehouseZone = warehouseZone;
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
