/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.ServicePO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.wms.mobilitymanagementmodule.WarehouseRequestManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "orderFulfillManagedBean")
@ViewScoped
public class OrderFulfillManagedBean {

    @EJB
    private WarehouseRequestManagerSessionBean warehouseRMB;

    private Integer companyId;
    private Integer userId;
    private ServicePO selectedOrder;
    private List<String> contractInf;
    private String warehouse; 
    private String warehouseZone;
    private String storageBin;
    private Map<String, Map<String, Map<String, Integer>>> data = new HashMap<String, Map<String, Map<String, Integer>>>();
    private Map<String, Map<String, Integer>> data2 = new HashMap<String, Map<String, Integer>>();
    private Map<String, Integer> storageBins = new HashMap<String, Integer>();

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

        selectedOrder = (ServicePO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
        contractInf = warehouseRMB.viewContractInformation(selectedOrder.getServicePOId());
        data = warehouseRMB.selectionSetUp(selectedOrder, companyId);
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
        if (warehouse != null && !warehouse.equals("")) {
            data2 = data.get(warehouse);
        } else {
            data2 = new HashMap<String, Map<String,Integer>>();
        }
    }
    public void onWarehouseZoneChange(){
        if (warehouseZone != null && !warehouseZone.equals("")) {
            storageBins = data2.get(warehouseZone);
        } else {
            storageBins = new HashMap<String, Integer>();
        }
    }
    
    public void reserveSpace(){
        int myBinId=storageBins.get(storageBin);
        warehouseRMB.reserveSpace(myBinId, selectedOrder);
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWarehouseZone() {
        return warehouseZone;
    }

    public void setWarehouseZone(String warehouseZone) {
        this.warehouseZone = warehouseZone;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
    }

    public Map<String, Map<String, Map<String, Integer>>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, Map<String, Integer>>> data) {
        this.data = data;
    }

    public Map<String, Map<String, Integer>> getData2() {
        return data2;
    }

    public void setData2(Map<String, Map<String, Integer>> data2) {
        this.data2 = data2;
    }

    public Map<String, Integer> getStorageBins() {
        return storageBins;
    }

    public void setStorageBins(Map<String, Integer> storageBins) {
        this.storageBins = storageBins;
    }
   
}
