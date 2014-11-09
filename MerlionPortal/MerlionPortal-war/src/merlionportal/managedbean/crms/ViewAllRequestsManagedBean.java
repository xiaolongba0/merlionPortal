/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceQuotation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.contractmanagementmodule.QuotationManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewAllRequestsManagedBean")
@ViewScoped
public class ViewAllRequestsManagedBean {

    @EJB
    QuotationManagementSessionBean quotationManagementSB;
    @EJB
    private SystemLogSessionBean logSB;
    private Integer companyId;
    private Integer userId;

    private List<ServiceQuotation> sentRequests;
    private List<ServiceQuotation> receivedRequests;
    private List<ServiceQuotation> filteredRequests;

    private ServiceQuotation selectedRequest;
    private ServiceQuotation requestToView;

    private List<String> status;
    private String statusNumber;

    /**
     * Creates a new instance of ViewAllRequestsManagedBean
     */
    public ViewAllRequestsManagedBean() {
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
        sentRequests = (List<ServiceQuotation>) quotationManagementSB.viewAllRequestsSent(companyId);
        receivedRequests = (List<ServiceQuotation>) quotationManagementSB.viewAllRequestReceived(companyId);
        status = new ArrayList<>();
        status.add("Request for quotation");
//        status.add("Waiting for acception");
//        status.add("Valid");
//        status.add("Rejected request");
//        status.add("Rejected quotation");
        status.add("Pending fulfillment check");
        status.add("Fulfillment check fail");
        status.add("Fulfillment check success");
    }

    public String generateQuotation() {

        if (selectedRequest != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedRequest");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedRequest", selectedRequest);
            logSB.recordSystemLog(userId, "CRMS generated quotation");
            return "generateservicequotation.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a service to request!", ""));
            return null;
        }
    }

    public void viewARequest(ServiceQuotation viewRequest) {
        requestToView = viewRequest;
    }

    public String getStatusNumber(int passedStatus) {
        if (passedStatus == 1) {
            statusNumber = "Request for quotation";
        }
        if (passedStatus == 2) {
            statusNumber = "Waiting for acception";
        }
        if (passedStatus == 3) {
            statusNumber = "Valid";
        }
        if (passedStatus == 4) {
            statusNumber = "Rejected request";
        }
        if (passedStatus == 5) {
            statusNumber = "Rejected quotation";
        }
        if (passedStatus == 6) {
            statusNumber = "Pending fulfillment check";
        }
        if (passedStatus == 7) {
            statusNumber = "Fulfillment check fail";
        }
        if (passedStatus == 8) {
            statusNumber = "Fulfillment check success";
        }
        return statusNumber;
    }

    public void setStatusNumber(String statusNumber) {
        this.statusNumber = statusNumber;
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

    public List<ServiceQuotation> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<ServiceQuotation> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<ServiceQuotation> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<ServiceQuotation> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public ServiceQuotation getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(ServiceQuotation selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public List<ServiceQuotation> getFilteredRequests() {
        return filteredRequests;
    }

    public void setFilteredRequests(List<ServiceQuotation> filteredRequests) {
        this.filteredRequests = filteredRequests;
    }

    public ServiceQuotation getRequestToView() {
        return requestToView;
    }

    public void setRequestToView(ServiceQuotation requestToView) {
        this.requestToView = requestToView;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

}
