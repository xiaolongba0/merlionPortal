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
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "salesOrderManagedBean")
@ViewScoped
public class SalesOrderManagedBean {

    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrderMB;

    private Integer companyId;
    private Integer userId;
    private ProductOrder myOrder;
    private List<String> customerInfor;
    private List<String> rejectReasons;
    private String rejectReason;
    private Boolean credit;

    public SalesOrderManagedBean() {
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
        myOrder = (ProductOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
        credit = purchaseOrderMB.checkCredit(myOrder.getCreatorId());

    }

 

    public void generateSo() {
        if (myOrder.getStatus() == 1 && credit) {
            purchaseOrderMB.generateSo(userId, myOrder);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Sales order has benn created."));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", "Unable to generate sales order."));
        }
    }

    public void checkCredit() {
        if (credit) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Credit check pass"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "Credit check fail"));

        }
    }

    public void rejectPo() {
        System.out.println(rejectReason);
        int s = Integer.parseInt(rejectReason.substring(0, 2));
        System.out.println("=======================" + s);
        s = s + 5;
        System.out.println("=======================" + s);
        purchaseOrderMB.rejectPo(userId, myOrder, s);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "This order has benn rejected."));

    }

    public ProductOrder getMyOrder() {
        return myOrder;
    }

    public void setMyOrder(ProductOrder myOrder) {
        this.myOrder = myOrder;
    }

    public List<String> getCustomerInfor() {
        customerInfor = purchaseOrderMB.getCustomerCompany(userId);
        return customerInfor;
    }

    public void setCustomerInfor(List<String> customerInfor) {
        this.customerInfor = customerInfor;
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


    public void setRejectReasons(List<String> rejectReasons) {
        this.rejectReasons = rejectReasons;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

  

}
