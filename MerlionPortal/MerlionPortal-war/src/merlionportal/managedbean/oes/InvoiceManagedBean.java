/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

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
    private Integer companyId;
    private Integer userId;
    private List<ProductOrder> allUnInvoiced;
    private ProductOrder selectedOrder;

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
        allUnInvoiced=invoiceMB.getAllWaitingForInvoice(companyId);
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
    
    public String generateInvoice(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("unInvoiced", selectedOrder);

        return "invoice.xhtml";
    }



}
