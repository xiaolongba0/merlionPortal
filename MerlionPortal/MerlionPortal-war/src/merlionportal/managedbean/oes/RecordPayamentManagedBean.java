/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.oes.ordermanagement.PaymentManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "recordPayamentManagedBean")
@ViewScoped
public class RecordPayamentManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private PaymentManagerSessionBean paymentMB;
    private Integer companyId;
    private Integer userId;
    private ProductOrder unpaidOrder;
    private String billTo;
    private List<String> maymentMethods;
    private String method;

    public RecordPayamentManagedBean() {
    }

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
        unpaidOrder = (ProductOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("unpaidOrder");
        maymentMethods = paymentMB.allPaymentMethods();
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

    public ProductOrder getUnpaidOrder() {
        return unpaidOrder;
    }

    public void setUnpaidOrder(ProductOrder unpaidOrder) {
        this.unpaidOrder = unpaidOrder;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public List<String> getMaymentMethods() {
        return maymentMethods;
    }

    public void setMaymentMethods(List<String> maymentMethods) {
        this.maymentMethods = maymentMethods;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void makePayment() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Pay successfully "));
        paymentMB.makePayement(unpaidOrder, method);
        systemLogSB.recordSystemLog(userId, "OES Make Payment ");
    }

}
