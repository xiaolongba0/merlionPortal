/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductInvoice;
import entity.ProductOrder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.oes.ordermanagement.InvoiceMangerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "invoiceManagedBean")
@ViewScoped
public class InvoiceManagedBean {

    @EJB
    private InvoiceMangerSessionBean invoiceMB;
    @EJB
    private SystemAccessRightSessionBean systemAccessRightSB;

    private Integer companyId;
    private Integer userId;
    private List<ProductOrder> allUnInvoiced;
    private ProductOrder selectedOrder;
    private List<ProductInvoice> allInvoice;
    private ProductInvoice filteredInvoice;
    private ProductInvoice selectedInvoice;
    private List<String> invoiceStatus;

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

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
        allUnInvoiced = invoiceMB.getAllWaitingForInvoice(companyId);
        invoiceStatus = invoiceMB.setAllStatus();
    }

    public InvoiceManagedBean() {
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<ProductOrder> getAllUnInvoiced() {
        return allUnInvoiced;
    }

    public void setAllUnInvoiced(List<ProductOrder> allUnInvoiced) {
        this.allUnInvoiced = allUnInvoiced;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ProductOrder getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(ProductOrder selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public String generateInvoice() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("unInvoiced", selectedOrder);

        return "invoice.xhtml";
    }

    public List<ProductInvoice> getAllInvoice() {
        if (!systemAccessRightSB.checkOESGeneratePO(userId)) {
            System.out.println("=====================View all quoation This is staff =====================");
            allInvoice = invoiceMB.viewAllInvoice(companyId);

        } else {
            System.out.println(" =====================View all quoation This is customer=====================");
            allInvoice = invoiceMB.viewAllInvoice(companyId, userId);

        }

        return allInvoice;
    }

    public void setAllInvoice(List<ProductInvoice> allInvoice) {
        this.allInvoice = allInvoice;
    }

    public ProductInvoice getFilteredInvoice() {
        return filteredInvoice;
    }

    public void setFilteredInvoice(ProductInvoice filteredInvoice) {
        this.filteredInvoice = filteredInvoice;
    }

    public ProductInvoice getSelectedInvoice() {
        return selectedInvoice;
    }

    public void setSelectedInvoice(ProductInvoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;
    }

    public List<String> getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(List<String> invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String viewInvoice() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("selectedInvoice", selectedInvoice);
        return "displayinvoicedetail.xhtml";
    }

}
