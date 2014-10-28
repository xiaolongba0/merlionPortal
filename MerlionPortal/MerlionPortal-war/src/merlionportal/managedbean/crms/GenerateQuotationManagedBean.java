/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceQuotation;
import entity.SystemUser;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.contractmanagementmodule.QuotationManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "generateQuotationManagedBean")
@ViewScoped
public class GenerateQuotationManagedBean {

    /**
     * Creates a new instance of GenerateQuotationManagedBean
     */
    @EJB
    QuotationManagementSessionBean quotationManagementSB;
    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

    private ServiceQuotation selectedRequest;

    private Double price;
    private int discountRate;

    private String senderCompanyName;
    private String receiverCompanyName;
    private SystemUser loginedUser;

    private int status;
    private String statusText;

    public GenerateQuotationManagedBean() {
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
        selectedRequest = (ServiceQuotation) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedRequest");
        senderCompanyName = userAccountSB.getCompany(companyId).getName();
        receiverCompanyName = userAccountSB.getCompany(selectedRequest.getSenderCompanyId()).getName();
        loginedUser = userAccountSB.getUser(userId);
        status = selectedRequest.getStatus();
        statusText = this.retriveStatus(selectedRequest.getStatus());

    }

    public void createQuotation() {
        if (selectedRequest != null) {
            int result = quotationManagementSB.createQuotation(selectedRequest.getQuotationId(), price, discountRate);
            if (result > 0) {
                this.clearAllFields();
                status = 2;
                statusText = "waiting for acception";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quotation Created", "Both sender and receiver will be able to view this quotation"));
                logSB.recordSystemLog(userId, "CRMS created a service quotation");

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong."));

            }
        }
    }

    public void rejectRequestForQuotation() {
        if (selectedRequest != null) {
            int result = quotationManagementSB.rejectRequestForQuotation(selectedRequest.getQuotationId());
            if (result > 0) {
                status = 4;
                statusText = "rejected request";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Quotation is successfully rejected"));
                logSB.recordSystemLog(userId, "CRMS rejected a request for quotation");

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong."));

            }
        }
    }

    public void fulfillmentCheck() {
        if (selectedRequest != null) {
            int result = quotationManagementSB.fulfillmentAvailabilityCheck(selectedRequest.getQuotationId());
            if (result > 0) {
                status = 6;
                statusText = "pending fulfillment check";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Please wait for fulfullment check result"));
                logSB.recordSystemLog(userId, "CRMS performed service fulfillment check");

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong."));

            }
        }
    }

    private void clearAllFields() {
        price = null;
        discountRate = 0;
    }

    private String retriveStatus(int status) {
        String text = "";
        if (status == 1) {
            text = "request for quotation";
        }
        if (status == 2) {
            text = "waiting for acception";
        }
        if (status == 3) {
            text = "valid";
        }
        if (status == 4) {
            text = "rejected request";
        }
        if (status == 5) {
            text = "rejected quotation";
        }
        if (status == 6) {
            text = "pending fulfillment check";
        }
        if (status == 7) {
            text = "fulfillment check fail";
        }
        if (status == 8) {
            text = "fulfillment check success";
        }
        return text;
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

    public ServiceQuotation getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(ServiceQuotation selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public String getSenderCompanyName() {
        return senderCompanyName;
    }

    public void setSenderCompanyName(String senderCompanyName) {
        this.senderCompanyName = senderCompanyName;
    }

    public String getReceiverCompanyName() {
        return receiverCompanyName;
    }

    public void setReceiverCompanyName(String receiverCompanyName) {
        this.receiverCompanyName = receiverCompanyName;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
