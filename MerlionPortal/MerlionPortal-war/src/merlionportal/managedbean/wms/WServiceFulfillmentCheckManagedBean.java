/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.managedbean.wms;

import entity.ServiceQuotation;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.contractmanagementmodule.QuotationManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "wServiceFulfillmentCheckMB")
@ViewScoped
public class WServiceFulfillmentCheckManagedBean {

    /**
     * Creates a new instance of WServiceFulfillmentCheckManagedBean
     */
    @EJB
    QuotationManagementSessionBean quotationManagementSB;
    @EJB
    SystemLogSessionBean systemLogSB;
    private Integer companyId;
    private Integer userId;
    private List<ServiceQuotation> pendingFulfillmentCheckList;
    private List<ServiceQuotation> filteredPendingFulfillmentCheckList;
    private String statusNumber;
    private ServiceQuotation requestToView;
    
    public WServiceFulfillmentCheckManagedBean() {
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
        pendingFulfillmentCheckList = quotationManagementSB.retrieveAllWarehouseFulfillmentcheckList(companyId);
        if (pendingFulfillmentCheckList != null) {

        }
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

    public void viewAPendingFulfillmentCheck(ServiceQuotation quotation) {
        requestToView = quotation;
    }

    public void passCheck() {
        quotationManagementSB.passFulfillmentCheck(requestToView.getQuotationId());
        pendingFulfillmentCheckList.remove(requestToView);
        systemLogSB.recordSystemLog(userId, "WMS fulfillment check passed");
    }

    public void failCheck() {
        quotationManagementSB.failFulfillmentCheck(requestToView.getQuotationId());
        pendingFulfillmentCheckList.remove(requestToView);
        systemLogSB.recordSystemLog(userId, "WMS fulfillment check failed");

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

    public List<ServiceQuotation> getPendingFulfillmentCheckList() {
        return pendingFulfillmentCheckList;
    }

    public void setPendingFulfillmentCheckList(List<ServiceQuotation> pendingFulfillmentCheckList) {
        this.pendingFulfillmentCheckList = pendingFulfillmentCheckList;
    }

    public List<ServiceQuotation> getFilteredPendingFulfillmentCheckList() {
        return filteredPendingFulfillmentCheckList;
    }

    public void setFilteredPendingFulfillmentCheckList(List<ServiceQuotation> filteredPendingFulfillmentCheckList) {
        this.filteredPendingFulfillmentCheckList = filteredPendingFulfillmentCheckList;
    }

    public String getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(String statusNumber) {
        this.statusNumber = statusNumber;
    }

    public ServiceQuotation getRequestToView() {
        return requestToView;
    }

    public void setRequestToView(ServiceQuotation requestToView) {
        this.requestToView = requestToView;
    }

    
}
