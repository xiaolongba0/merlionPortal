/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceQuotation;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
    private Integer companyId;
    private Integer userId;

    private List<ServiceQuotation> sentRequests;
    private List<ServiceQuotation> receivedRequests;
    private List<ServiceQuotation> filteredRequests;

    private ServiceQuotation selectedRequest;
    private ServiceQuotation requestToView;

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
    }

    public void generateQuotation() {
        if (selectedRequest != null) {
            int result = quotationManagementSB.createQuotation(selectedRequest.getQuotationId(), selectedRequest.getPrice(), selectedRequest.getDiscountRate());
            if (result > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quotation Created", "Both sender and receiver will be able to view this quotation"));

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong."));

            }
        }

    }

    public void viewARequest(ServiceQuotation viewRequest){
        requestToView = viewRequest;
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
    
}
