/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.ServicePO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.wms.mobilitymanagementmodule.WarehouseRequestManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "warehouseReqeustManagedBean")
@ViewScoped
public class WarehouseReqeustManagedBean {

    @EJB
    private WarehouseRequestManagerSessionBean warehouseRMB;

    private Integer companyId;
    private Integer userId;
    private List<ServicePO> pendingOrders;
    private ServicePO selectedOrder;
    private List<String> contractInf;

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
    }

    public WarehouseReqeustManagedBean() {
    }

    public String showRequest() {

        if (selectedOrder == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one Order ."));
            return "viewallpendingrequests.xhtml?faces-redirect=true";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("selectedOrder", selectedOrder);
        return "viewserviceorderdetial.xhtml?faces-redirect=true";
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

    public List<ServicePO> getPendingOrders() {
        pendingOrders = warehouseRMB.viewPendingRequest(companyId);
        return pendingOrders;
    }

    public void setPendingOrders(List<ServicePO> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public ServicePO getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(ServicePO selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public String viewCompanyName(Integer cId) {
        String result = "";
        if (cId != null) {
            result = warehouseRMB.viewCompanyName(cId);
        }
        return result;
    }

    public String viewCompanyContactPersonName(Integer cId) {
        String result = "";
        if (cId != null) {
            result = warehouseRMB.viewCompanyContactPersonName(cId);
        }
        return result;
    }

    public String viewCompanyContact(Integer cId) {
        String result = "";
        if (cId != null) {
            result = warehouseRMB.viewCompanyContact(cId);
        }
        return result;
    }

    public List<String> getContractInf() {
        contractInf = warehouseRMB.viewContractInformation(selectedOrder.getServicePOId());
        return contractInf;
    }

    public void setContractInf(List<String> contractInf) {
        this.contractInf = contractInf;
    }

}
