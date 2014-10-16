/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServicePO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.crms.ordermanagementmodule.ServicePOManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewAllServicePO")
@ViewScoped
public class ViewAllServicePOManagedBean {

    /**
     * Creates a new instance of viewAllServicePO
     */
    @EJB
    ServicePOManagementSessionBean servicePOSB;

    private Integer companyId;
    private Integer userId;

    private List<ServicePO> sentServicePO;
    private List<ServicePO> receivedServicePO;
    private List<ServicePO> filteredServicePO;

    private ServicePO selectedSentServicePO;
    private ServicePO selectedReceivedServicePO;

    private List<String> status;
    private String statusNumber;

    public ViewAllServicePOManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
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
        sentServicePO = (List<ServicePO>) servicePOSB.viewAllSentServicePO(companyId);
        receivedServicePO = (List<ServicePO>) servicePOSB.viewAllReceivedServicePO(companyId);

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

    public String viewSentServicePO() {
        if (selectedSentServicePO != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedServicePO");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedServicePO", selectedSentServicePO);
            return "viewservicepodetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a service PO to view!", ""));
            return null;
        }
    }

    public String viewReceivedServicePO() {
        if (selectedReceivedServicePO != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedServicePO");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedServicePO", selectedReceivedServicePO);
            return "viewservicepodetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a service PO to view!", ""));
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

    public List<ServicePO> getSentServicePO() {
        return sentServicePO;
    }

    public void setSentServicePO(List<ServicePO> sentServicePO) {
        this.sentServicePO = sentServicePO;
    }

    public List<ServicePO> getReceivedServicePO() {
        return receivedServicePO;
    }

    public void setReceivedServicePO(List<ServicePO> receivedServicePO) {
        this.receivedServicePO = receivedServicePO;
    }

    public List<ServicePO> getFilteredServicePO() {
        return filteredServicePO;
    }

    public void setFilteredServicePO(List<ServicePO> filteredServicePO) {
        this.filteredServicePO = filteredServicePO;
    }

    public ServicePO getSelectedSentServicePO() {
        return selectedSentServicePO;
    }

    public void setSelectedSentServicePO(ServicePO selectedSentServicePO) {
        this.selectedSentServicePO = selectedSentServicePO;
    }

    public ServicePO getSelectedReceivedServicePO() {
        return selectedReceivedServicePO;
    }

    public void setSelectedReceivedServicePO(ServicePO selectedReceivedServicePO) {
        this.selectedReceivedServicePO = selectedReceivedServicePO;
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
