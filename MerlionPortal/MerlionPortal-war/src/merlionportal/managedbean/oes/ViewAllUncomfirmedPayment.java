/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductInvoice;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.oes.ordermanagement.PaymentManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "viewAllUncomfirm")
@ViewScoped
public class ViewAllUncomfirmedPayment {

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private PaymentManagerSessionBean paymentMB;

    private Integer companyId;
    private Integer userId;
    private List<ProductInvoice> unpaidOrderList;
    private ProductInvoice selectedOrder;

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
        unpaidOrderList = paymentMB.getAllWaitingInvoice(companyId);
    }

    public ViewAllUncomfirmedPayment() {
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

    public List<ProductInvoice> getUnpaidOrderList() {
        return unpaidOrderList;
    }

    public void setUnpaidOrderList(List<ProductInvoice> unpaidOrderList) {
        this.unpaidOrderList = unpaidOrderList;
    }

    public ProductInvoice getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(ProductInvoice selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public String processPayament() {
        if (selectedOrder == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one invoice."));
            return "allwaitinginvoice.xhtml";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("unpaidOrder", selectedOrder);

        return "comfirmpayment.xhtml?faces-redirect=true;";
    }
}
