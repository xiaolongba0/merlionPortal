/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductInvoice;
import entity.ProductOrder;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import merlionportal.oes.ordermanagement.InvoiceMangerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "displayInvoiceManagedBean")
@Dependent
public class DisplayInvoiceManagedBean {

    @EJB
    private InvoiceMangerSessionBean invoiceMB;
    private Integer companyId;
    private Integer userId;
    private ProductInvoice myInvoice;
    private ProductOrder myOrder;
    private int orderId;

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
        myInvoice = (ProductInvoice) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedInvoice");
        orderId=myInvoice.getSalesOrderId();
        myOrder = invoiceMB.getMyOrder(orderId);

    }

    public DisplayInvoiceManagedBean() {
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

    public ProductInvoice getMyInvoice() {
        return myInvoice;
    }

    public void setMyInvoice(ProductInvoice myInvoice) {
        this.myInvoice = myInvoice;
    }

    public ProductOrder getMyOrder() {
        return myOrder;
    }

    public void setMyOrder(ProductOrder myOrder) {
        this.myOrder = myOrder;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    

}
