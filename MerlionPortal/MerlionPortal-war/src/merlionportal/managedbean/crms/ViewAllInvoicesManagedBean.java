/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.managedbean.crms;

import entity.ServiceInvoice;
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
@Named(value = "viewAllInvoicesManagedBean")
@ViewScoped
public class ViewAllInvoicesManagedBean {

    /**
     * Creates a new instance of ViewAllInvoicesManagedBean
     */
    public ViewAllInvoicesManagedBean() {
    }
    
    @EJB
    ServicePOManagementSessionBean servicepoMSB;

    private Integer companyId;
    private Integer userId;

    private List<ServiceInvoice> sentInvoices;
    private List<ServiceInvoice> receivedInvoices;
    private List<ServiceInvoice> filteredInvoices;

    private ServiceInvoice selectedSentInvoice;
    private ServiceInvoice selectedReceivedInvoice;

    private List<String> status;
    private String statusNumber;


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
        sentInvoices = (List<ServiceInvoice>) servicepoMSB.viewAllSentInvoices(companyId);
        receivedInvoices = (List<ServiceInvoice>) servicepoMSB.viewAllReceivedInvoices(companyId);

        status = new ArrayList<>();
        status.add("Invoiced");
        status.add("Payment information recorded");
        status.add("Paid");
        

    }

    public String viewASentInvoice() {
        if (selectedSentInvoice != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedInvoice");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedInvoice", selectedSentInvoice);
            return "viewinvoicedetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select an invoice to view!", ""));
            return null;
        }
    }

    public String viewAReceivedInvoice() {
        if (selectedReceivedInvoice != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedInvoice");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedInvoice", selectedReceivedInvoice);
            return "viewinvoicedetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select an invoice to view!", ""));
            return null;
        }
    }

    public String getStatusNumber(int passedStatus) {
        if (passedStatus == 1) {
            statusNumber = "Invoiced";
        }
        if (passedStatus == 2) {
            statusNumber = "Payment information recorded";
        }
        if (passedStatus == 3) {
            statusNumber = "Paid";
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

    public List<ServiceInvoice> getSentInvoices() {
        return sentInvoices;
    }

    public void setSentInvoices(List<ServiceInvoice> sentInvoices) {
        this.sentInvoices = sentInvoices;
    }

    public List<ServiceInvoice> getReceivedInvoices() {
        return receivedInvoices;
    }

    public void setReceivedInvoices(List<ServiceInvoice> receivedInvoices) {
        this.receivedInvoices = receivedInvoices;
    }

    public List<ServiceInvoice> getFilteredInvoices() {
        return filteredInvoices;
    }

    public void setFilteredInvoices(List<ServiceInvoice> filteredInvoices) {
        this.filteredInvoices = filteredInvoices;
    }

    public ServiceInvoice getSelectedSentInvoice() {
        return selectedSentInvoice;
    }

    public void setSelectedSentInvoice(ServiceInvoice selectedSentInvoice) {
        this.selectedSentInvoice = selectedSentInvoice;
    }

    public ServiceInvoice getSelectedReceivedInvoice() {
        return selectedReceivedInvoice;
    }

    public void setSelectedReceivedInvoice(ServiceInvoice selectedReceivedInvoice) {
        this.selectedReceivedInvoice = selectedReceivedInvoice;
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
