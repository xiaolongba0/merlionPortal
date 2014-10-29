/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.managedbean.tms;

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
@Named(value = "transportationRequestsMB")
@ViewScoped
public class TransportationRequestsManagedBean {

    /**
     * Creates a new instance of TransportationRequestsManagedBean
     */
     @EJB
    ServicePOManagementSessionBean servicePOSB;

    private Integer companyId;
    private Integer userId;

    private List<ServicePO> sentTServicePO;
    private List<ServicePO> receivedTServicePO;
    private List<ServicePO> filteredTServicePO;

    private ServicePO selectedSentTServicePO;
    private ServicePO selectedReceivedTServicePO;

    private List<String> status;
    private String statusNumber;
    
    public TransportationRequestsManagedBean() {
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
        sentTServicePO = (List<ServicePO>) servicePOSB.retrieveSentTransportationRequests(companyId);
        receivedTServicePO = (List<ServicePO>) servicePOSB.retrieveReceivedTransportationRequests(companyId);

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
    public String viewSentTransportationServicePO() {
        if (selectedSentTServicePO != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedTransServicePO");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedTransServicePO", selectedSentTServicePO);
            return "viewtransportationrequestdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a service PO to view!", ""));
            return null;
        }
    }

    public String viewReceivedTransportationServicePO() {
        if (selectedReceivedTServicePO != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedTransServicePO");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedTransServicePO", selectedReceivedTServicePO);
            return "viewtransportationrequestdetail.xhtml?faces-redirect=true";
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

    public List<ServicePO> getSentTServicePO() {
        return sentTServicePO;
    }

    public void setSentTServicePO(List<ServicePO> sentTServicePO) {
        this.sentTServicePO = sentTServicePO;
    }

    public List<ServicePO> getReceivedTServicePO() {
        return receivedTServicePO;
    }

    public void setReceivedTServicePO(List<ServicePO> receivedTServicePO) {
        this.receivedTServicePO = receivedTServicePO;
    }

    public List<ServicePO> getFilteredTServicePO() {
        return filteredTServicePO;
    }

    public void setFilteredTServicePO(List<ServicePO> filteredTServicePO) {
        this.filteredTServicePO = filteredTServicePO;
    }

    public ServicePO getSelectedSentTServicePO() {
        return selectedSentTServicePO;
    }

    public void setSelectedSentTServicePO(ServicePO selectedSentTServicePO) {
        this.selectedSentTServicePO = selectedSentTServicePO;
    }

    public ServicePO getSelectedReceivedTServicePO() {
        return selectedReceivedTServicePO;
    }

    public void setSelectedReceivedTServicePO(ServicePO selectedReceivedTServicePO) {
        this.selectedReceivedTServicePO = selectedReceivedTServicePO;
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
