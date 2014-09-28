/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.oes.ordermanagement.InvoiceMangerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "generateInvoiceManagedBean")
@ViewScoped
public class GenerateInvoiceManagedBean {

    @EJB
    private InvoiceMangerSessionBean invoiceMB;
    private Integer companyId;
    private Integer userId;
    private ProductOrder unInvoiced;
    private Double totalPrice;
    private String inputText;

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
        unInvoiced = (ProductOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("unInvoiced");
        totalPrice = this.getPrice();

    }

    private Double getPrice() {
        totalPrice = 0.0;
        for (Object o : unInvoiced.getProductOrderLineItemList()) {
            ProductOrderLineItem line = (ProductOrderLineItem) o;
            totalPrice = +line.getPrice();
        }
        return totalPrice;
    }

    public GenerateInvoiceManagedBean() {
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

    public ProductOrder getUnInvoiced() {
        return unInvoiced;
    }

    public void setUnInvoiced(ProductOrder unInvoiced) {
        this.unInvoiced = unInvoiced;
    }

    public String generateInvoice() {
        if (inputText == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", "Terms and conditions cannot be empty"));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Credit check pass"));
        invoiceMB.generateInvoice(unInvoiced, unInvoiced.getProductPOId(), totalPrice, unInvoiced.getCreatorId());
        return "orderswaitingforinvoice.xhtml";
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

}
