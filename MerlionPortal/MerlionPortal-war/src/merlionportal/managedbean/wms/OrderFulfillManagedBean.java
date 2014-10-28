/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.ServicePO;
import entity.StorageBin;
import entity.Warehouse;
import entity.WarehouseZone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.wms.mobilitymanagementmodule.WarehouseRequestManagerSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "orderFulfillManagedBean")
@ViewScoped
public class OrderFulfillManagedBean {

    @EJB
    private WarehouseRequestManagerSessionBean warehouseRMB;
    @EJB
    private AssetManagementSessionBean amsb;

    private Integer companyId;
    private Integer userId;
    private ServicePO selectedOrder;
    private List<String> contractInf;
    private Integer warehouse;
    private List<Warehouse> warehouses;

    private Integer warehouseZone;
    private List<WarehouseZone> warehouseZones;

    private List<Integer> selectedStorageBins;
    private List<String> listStorageBinType;
    private List<StorageBin> storageBins;

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

            if (userId != null) {
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
        selectedOrder = (ServicePO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
        contractInf = warehouseRMB.viewContractInformation(selectedOrder.getServicePOId());

    }

    public OrderFulfillManagedBean() {
    }

    public String viewCompanyName(int cId) {
        String result = warehouseRMB.viewCompanyName(cId);
        return result;
    }

    public String viewCompanyContactPersonName(int cId) {
        String result = warehouseRMB.viewCompanyContactPersonName(cId);
        return result;
    }

    public String viewCompanyContact(int cId) {
        String result = warehouseRMB.viewCompanyContact(cId);
        return result;
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

    public ServicePO getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(ServicePO selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public List<String> getContractInf() {
        return contractInf;
    }

    public void setContractInf(List<String> contractInf) {
        this.contractInf = contractInf;
    }

    public void onWarehouseChange() {
        if (warehouse != null) {
            warehouseZones = amsb.viewWarehouseZoneForAWarehouse(warehouse);
        }
    }

    public void onWarehouseZoneChange() {
        storageBins = new ArrayList();
        if (warehouse != null & warehouseZone != null) {
            for (Object o : amsb.viewStorageBinForWarehouseZone(warehouseZone)) {
                StorageBin myBin = (StorageBin) o;
                if (warehouseRMB.checkStorageBinAvailability(selectedOrder, myBin)) {
                    storageBins.add(myBin);
                }
            }
        }
    }

    public void reserveSpace() {
        int totalAvailable = 0;
        int reserveSpace = 0;
        int requiredSpace = selectedOrder.getVolume() * selectedOrder.getProductQuantityPerTEU();
        for (Object o : selectedStorageBins) {
            StorageBin mybin = (StorageBin) o;
            totalAvailable += mybin.getAvailableSpace();
        }
        if(totalAvailable< requiredSpace){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not enough space", "Please select more storage bin"));
        }
        else{
            for (Object o : selectedStorageBins) {
                StorageBin mybin = (StorageBin) o;
                reserveSpace =mybin.getAvailableSpace();
                if (requiredSpace > mybin.getAvailableSpace()) {
                    warehouseRMB.reserveSpace(mybin, selectedOrder,reserveSpace);
                    requiredSpace =requiredSpace-reserveSpace;
                }
                else{
                    warehouseRMB.reserveSpace(mybin, selectedOrder,requiredSpace);
                }
            }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Space Reserved"));
        }
    }

    public Integer getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Integer warehouse) {
        this.warehouse = warehouse;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Integer getWarehouseZone() {
        return warehouseZone;
    }

    public void setWarehouseZone(Integer warehouseZone) {
        this.warehouseZone = warehouseZone;
    }

    public List<WarehouseZone> getWarehouseZones() {
        return warehouseZones;
    }

    public void setWarehouseZones(List<WarehouseZone> warehouseZones) {
        this.warehouseZones = warehouseZones;
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

    public List<Integer> getSelectedStorageBins() {
        return selectedStorageBins;
    }

    public void setSelectedStorageBins(List<Integer> selectedStorageBins) {
        this.selectedStorageBins = selectedStorageBins;
    }
//    <p:outputLabel  value="Storage Bin " />
//                        <p:selectOneMenu id="storageBin" value="#{orderFulfillManagedBean.storageBin}" style="width:150px">
//                            <f:selectItem itemLabel="Select City" itemValue="" noSelectionOption="true" />
//                            <f:selectItems value="#{orderFulfillManagedBean.selectedStorageBins}"var="storageBin" itemLabel="ID:#{storageBin.storageBinId}   Name:#{storageBin.binName} Bin Type: Name:#{storageBin.binType}"  itemValue="#{storageBin.storageBinId}" />
//                        </p:selectOneMenu>

}
