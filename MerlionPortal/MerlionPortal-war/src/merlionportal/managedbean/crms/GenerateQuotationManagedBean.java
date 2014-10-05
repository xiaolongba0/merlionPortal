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

    private Integer companyId;
    private Integer userId;

    private ServiceQuotation selectedRequest;

    private Double price;
    private int discountRate;

    private String senderCompanyName;
    private String receiverCompanyName;
    private SystemUser loginedUser;

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

    }

    public void createQuotation() {
        if (selectedRequest != null) {
            int result = quotationManagementSB.createQuotation(selectedRequest.getQuotationId(), price, discountRate);
            if (result > 0) {
                this.clearAllFields();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quotation Created", "Both sender and receiver will be able to view this quotation"));

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong."));

            }
        }
    }

    public void rejectRequestForQuotation() {
        if (selectedRequest != null) {
            int result = quotationManagementSB.rejectRequestForQuotation(selectedRequest.getQuotationId());
            if (result > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Quotation is successfully rejected"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong."));

            }
        }
    }

    public void fulfillmentCheck(){
        if (selectedRequest != null) {
            int result = quotationManagementSB.fulfillmentAvailabilityCheck(selectedRequest.getQuotationId());
            if (result > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Please wait for fulfullment check result"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong."));

            }
        }
    }
    
    private void clearAllFields() {
        price = null;
        discountRate = 0;
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

}
