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
@Named(value = "viewQuotationManagedBean")
@ViewScoped
public class ViewQuotationDetailManagedBean {

    @EJB
    QuotationManagementSessionBean quotationManagementSB;
    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    SystemLogSessionBean logSB;
    private Integer companyId;
    private Integer userId;

    private ServiceQuotation selectedQuotation;
    private SystemUser loginedUser;

    private String senderCompanyName;
    private String receiverCompanyName;
    private Double discountedPrice;
    private Double finalPrice;

    private String origin;
    private String destination;

    private String status;
    private Integer compareStatus;

    /**
     * Creates a new instance of ViewQuotationManagedBean
     */
    public ViewQuotationDetailManagedBean() {
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

        loginedUser = userAccountSB.getUser(userId);
        selectedQuotation = (ServiceQuotation) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedQuotation");
        if (selectedQuotation != null) {
            senderCompanyName = userAccountSB.getCompany(selectedQuotation.getReceiverCompanyId()).getName();
            receiverCompanyName = userAccountSB.getCompany(selectedQuotation.getSenderCompanyId()).getName();
            if (selectedQuotation.getServiceType().equals("Transportation")) {
                origin = selectedQuotation.getOrigin().replace("^", " ,");
                destination = selectedQuotation.getDestination().replace("^", " ,");
            }
            if (selectedQuotation.getDiscountRate() != null) {
                discountedPrice = selectedQuotation.getDiscountRate() * selectedQuotation.getPrice() / 100;
                finalPrice = selectedQuotation.getPrice() - discountedPrice;
            } else {
                discountedPrice = 0.0;
                finalPrice = selectedQuotation.getPrice();
            }
        }
        compareStatus = selectedQuotation.getStatus();
        this.StatusText(selectedQuotation.getStatus());

    }

    public void rejectQuotation() {
        int result = quotationManagementSB.rejectQuotation(selectedQuotation.getQuotationId());
        if (result > 0) {
            compareStatus = 5;
            this.StatusText(compareStatus);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Quotation is rejected"));
            logSB.recordSystemLog(userId, "CRMS rejected a service quotation");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops", "Something went wrong!"));

        }
    }

    public void acceptQuotation() {
        int result = quotationManagementSB.acceptQuotation(selectedQuotation.getQuotationId());
        if (result > 0) {
            compareStatus = 3;
            this.StatusText(compareStatus);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Quotation is accepted"));
            logSB.recordSystemLog(userId, "CRMS accepted a service quotation");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops", "Something went wrong!"));

        }
    }

    private String StatusText(int passedStatus) {
        if (passedStatus == 1) {
            status = "Request for quotation";
        }
        if (passedStatus == 2) {
            status = "Waiting for acception";
        }
        if (passedStatus == 3) {
            status = "Valid";
        }
        if (passedStatus == 4) {
            status = "Rejected request";
        }
        if (passedStatus == 5) {
            status = "Rejected quotation";
        }
        if (passedStatus == 6) {
            status = "Pending fulfillment check";
        }
        if (passedStatus == 7) {
            status = "Fulfillment check fail";
        }
        if (passedStatus == 8) {
            status = "Fulfillment check success";
        }
        return status;
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

    public ServiceQuotation getSelectedQuotation() {
        return selectedQuotation;
    }

    public void setSelectedQuotation(ServiceQuotation selectedQuotation) {
        this.selectedQuotation = selectedQuotation;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
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

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCompareStatus() {
        return compareStatus;
    }

    public void setCompareStatus(Integer compareStatus) {
        this.compareStatus = compareStatus;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}
