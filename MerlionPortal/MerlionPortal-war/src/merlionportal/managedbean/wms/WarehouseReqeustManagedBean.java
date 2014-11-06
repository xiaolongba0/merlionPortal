/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.ServicePO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.crms.ordermanagementmodule.ServicePOManagementSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "warehouseReqeustManagedBean")
@ViewScoped
public class WarehouseReqeustManagedBean {

    @EJB
    ServicePOManagementSessionBean servicePOSB;

    private Integer companyId;
    private Integer userId;

    private List<ServicePO> sentWServicePO;
    private List<ServicePO> receivedWServicePO;
    private List<ServicePO> filteredWServicePO;

    private ServicePO selectedSentWServicePO;
    private ServicePO selectedReceivedWServicePO;

    private List<String> status;
    private String statusNumber;

    public WarehouseReqeustManagedBean() {
    }

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
        sentWServicePO = (List<ServicePO>) servicePOSB.retrieveSentWarehouseRequests(companyId);
        receivedWServicePO = (List<ServicePO>) servicePOSB.retrieveReceivedWarehouseRequests(companyId);

        status = new ArrayList<>();
        status.add("PO Waiting to be processed");
        status.add("PO Deleted");
        status.add("PO Hold");
        status.add("PO Rejected");
        status.add("SO Waiting for fulfillment");
        status.add("SO Fulfilled");
        status.add("SO Invoiced");
        status.add("SO Closed");
        status.add("Transportation SO in transit");
    }

    public String viewSentWarehouseServicePO() {
        if (selectedSentWServicePO != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedWarehouseServicePO");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedWarehouseServicePO", selectedSentWServicePO);
            return "viewserviceorderdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a service Order to view!", ""));
            return null;
        }
    }

    public String viewReceivedWarehouseServicePO() {
        if (selectedReceivedWServicePO != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedWarehouseServicePO");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedWarehouseServicePO", selectedReceivedWServicePO);
            return "viewserviceorderdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a service Order to view!", ""));
            return null;
        }
    }

    public String getStatusNumber(int passedStatus) {
        if (passedStatus == 1) {
            statusNumber = "PO Waiting to be processed";
        }
        if (passedStatus == 2) {
            statusNumber = "PO Deleted";
        }
        if (passedStatus == 3) {
            statusNumber = "PO Hold";
        }
        if (passedStatus == 4) {
            statusNumber = "PO Rejected";
        }
        if (passedStatus == 5) {
            statusNumber = "SO Waiting for fulfillment";
        }
        if (passedStatus == 6) {
            statusNumber = "SO Fulfilled";
        }
        if (passedStatus == 7) {
            statusNumber = "SO Invoiced";
        }
        if (passedStatus == 8) {
            statusNumber = "SO Closed";
        }
        if (passedStatus == 9) {
            statusNumber = "Transportation SO in transit";
        }
        if (passedStatus == 10) {
            statusNumber = "Packing in progress";
        }
        if (passedStatus == 11) {
            statusNumber = "Receiving order rejected";
        }
        return statusNumber;
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

    public List<ServicePO> getSentWServicePO() {
        return sentWServicePO;
    }

    public void setSentWServicePO(List<ServicePO> sentWServicePO) {
        this.sentWServicePO = sentWServicePO;
    }

    public List<ServicePO> getReceivedWServicePO() {
        return receivedWServicePO;
    }

    public void setReceivedWServicePO(List<ServicePO> receivedWServicePO) {
        this.receivedWServicePO = receivedWServicePO;
    }

    public List<ServicePO> getFilteredWServicePO() {
        return filteredWServicePO;
    }

    public void setFilteredWServicePO(List<ServicePO> filteredWServicePO) {
        this.filteredWServicePO = filteredWServicePO;
    }

    public ServicePO getSelectedSentWServicePO() {
        return selectedSentWServicePO;
    }

    public void setSelectedSentWServicePO(ServicePO selectedSentWServicePO) {
        this.selectedSentWServicePO = selectedSentWServicePO;
    }

    public ServicePO getSelectedReceivedWServicePO() {
        return selectedReceivedWServicePO;
    }

    public void setSelectedReceivedWServicePO(ServicePO selectedReceivedWServicePO) {
        this.selectedReceivedWServicePO = selectedReceivedWServicePO;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public String getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(String statusNumber) {
        this.statusNumber = statusNumber;
    }

}
