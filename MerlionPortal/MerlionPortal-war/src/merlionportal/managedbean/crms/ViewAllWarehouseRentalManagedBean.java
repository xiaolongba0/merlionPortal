/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.OtherInvoice;
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
import merlionportal.crms.ordermanagementmodule.GRNSOrderSessionBean;
import merlionportal.crms.ordermanagementmodule.ServicePOManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewWarehouseRentalMB")
@ViewScoped
public class ViewAllWarehouseRentalManagedBean {

    /**
     * Creates a new instance of ViewAllWarehouseRentalManagedBean
     */
    @EJB
    GRNSOrderSessionBean grnsSB;

    private Integer companyId;
    private Integer userId;

    private List<OtherInvoice> sentInvoices;
    private List<OtherInvoice> receivedInvoices;
    private List<OtherInvoice> filteredInvoices;

    private OtherInvoice selectedSentInvoice;
    private OtherInvoice selectedReceivedInvoice;

    private List<String> status;
    private String statusNumber;

    public ViewAllWarehouseRentalManagedBean() {
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
        sentInvoices = (List<OtherInvoice>) grnsSB.getAllWarehouseRentalInvoiceSent(companyId);
        receivedInvoices = (List<OtherInvoice>) grnsSB.getAllWarehouseRentalInvoiceReceived(companyId);
        //1. Confirmed
        //2. Payment Info Recorded
        //3. Paid
        status = new ArrayList<>();
        status.add("Confirmed");
        status.add("Payment Info Recorded");
        status.add("Paid");

    }
    public String viewASentInvoice() {
        if (selectedSentInvoice != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedWarehouseRentalInvoice");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedWarehouseRentalInvoice", selectedSentInvoice);
            return "viewwarehouserentalinvoicedetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select an invoice to view!", ""));
            return null;
        }
    }

    public String viewAReceivedInvoice() {
        if (selectedReceivedInvoice != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedWarehouseRentalInvoice");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedWarehouseRentalInvoice", selectedReceivedInvoice);
            return "viewwarehouserentalinvoicedetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select an invoice to view!", ""));
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

    public List<OtherInvoice> getSentInvoices() {
        return sentInvoices;
    }

    public void setSentInvoices(List<OtherInvoice> sentInvoices) {
        this.sentInvoices = sentInvoices;
    }

    public List<OtherInvoice> getReceivedInvoices() {
        return receivedInvoices;
    }

    public void setReceivedInvoices(List<OtherInvoice> receivedInvoices) {
        this.receivedInvoices = receivedInvoices;
    }

    public List<OtherInvoice> getFilteredInvoices() {
        return filteredInvoices;
    }

    public void setFilteredInvoices(List<OtherInvoice> filteredInvoices) {
        this.filteredInvoices = filteredInvoices;
    }

    public OtherInvoice getSelectedSentInvoice() {
        return selectedSentInvoice;
    }

    public void setSelectedSentInvoice(OtherInvoice selectedSentInvoice) {
        this.selectedSentInvoice = selectedSentInvoice;
    }

    public OtherInvoice getSelectedReceivedInvoice() {
        return selectedReceivedInvoice;
    }

    public void setSelectedReceivedInvoice(OtherInvoice selectedReceivedInvoice) {
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
