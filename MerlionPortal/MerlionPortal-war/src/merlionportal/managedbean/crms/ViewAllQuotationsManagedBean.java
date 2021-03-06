/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceQuotation;
import entity.SystemUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "viewAllQuotationsManagedBean")
@ViewScoped
public class ViewAllQuotationsManagedBean {

    /**
     * Creates a new instance of ViewAllQuotationsManagedBean
     */
    @EJB
    QuotationManagementSessionBean quotationManagementSB;
    @EJB
    UserAccountManagementSessionBean userAccountSB;
    private Integer companyId;
    private Integer userId;

    private List<ServiceQuotation> sentQuotations;
    private List<ServiceQuotation> receivedQuotations;
    private List<ServiceQuotation> filteredQuotations;

    private ServiceQuotation selectedSentQuotation;
    private ServiceQuotation selectedReceivedQuotation;
    private SystemUser loginedUser;

    private List<String> status;
    private String statusNumber;

    public ViewAllQuotationsManagedBean() {
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
        sentQuotations = (List<ServiceQuotation>) quotationManagementSB.viewAllQuotationsSent(companyId);
        receivedQuotations = (List<ServiceQuotation>) quotationManagementSB.viewAllQuotationsReceived(companyId);
        loginedUser = userAccountSB.getUser(userId);

        status = new ArrayList<>();
//        status.add("Request for quotation");
        status.add("Waiting for acception");
        status.add("Valid");
        status.add("Rejected request");
        status.add("Rejected quotation");
//        status.add("Pending fulfillment check");
//        status.add("Fulfillment check fail");
//        status.add("Fulfillment check success");
    }

    public String viewSentQuotation() {
        if (selectedSentQuotation != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedQuotation");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedQuotation", selectedSentQuotation);
            return "viewquotationdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a quotation to view!", ""));
            return null;
        }
    }

    public String viewReceivedQuotation() {
        if (selectedReceivedQuotation != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedQuotation");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedQuotation", selectedReceivedQuotation);
            return "viewquotationdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a quotation to view!", ""));
            return null;
        }
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

    public List<ServiceQuotation> getSentQuotations() {
        return sentQuotations;
    }

    public void setSentQuotations(List<ServiceQuotation> sentQuotations) {
        this.sentQuotations = sentQuotations;
    }

    public List<ServiceQuotation> getReceivedQuotations() {
        return receivedQuotations;
    }

    public void setReceivedQuotations(List<ServiceQuotation> receivedQuotations) {
        this.receivedQuotations = receivedQuotations;
    }

    public ServiceQuotation getSelectedSentQuotation() {
        return selectedSentQuotation;
    }

    public void setSelectedSentQuotation(ServiceQuotation selectedSentQuotation) {
        this.selectedSentQuotation = selectedSentQuotation;
    }

    public ServiceQuotation getSelectedReceivedQuotation() {
        return selectedReceivedQuotation;
    }

    public void setSelectedReceivedQuotation(ServiceQuotation selectedReceivedQuotation) {
        this.selectedReceivedQuotation = selectedReceivedQuotation;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public List<ServiceQuotation> getFilteredQuotations() {
        return filteredQuotations;
    }

    public void setFilteredQuotations(List<ServiceQuotation> filteredQuotations) {
        this.filteredQuotations = filteredQuotations;
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

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

}
