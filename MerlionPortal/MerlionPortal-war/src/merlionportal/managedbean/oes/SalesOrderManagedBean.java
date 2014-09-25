/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "salesOrderManagedBean")
@Dependent
public class SalesOrderManagedBean {

    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrderMB;

    private Integer companyId;
    private Integer userId;
    private ProductOrder myOrder;
    private List<String> customerInfor;
    private List<String> rejectReasons;
    private String reason;
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

    }

    private void setReasons() {
        rejectReasons.add("01 Wrong product");
        rejectReasons.add("02 Wrong product quantity");
        rejectReasons.add("03 Wrong price");
        rejectReasons.add("04 Wrong ship to address");
        rejectReasons.add("05 Wrong contact person");
        rejectReasons.add("06 Credit check fail ");
        rejectReasons.add("07 Others please contact sales for more information");
        rejectReasons.add("08 Customer request for cancelation");
        rejectReasons.add("09 Unable to fulfill this order");

    }

    public void generateSo() {
        if (myOrder.getStatus() == 1 && credit) {
            purchaseOrderMB.generateSo(userId,myOrder);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Sales order has benn created."));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", "Unable to generate sales order."));
        }
    }

    public void checkCredit() {
        credit = purchaseOrderMB.checkCredit(myOrder.getCreatorId());
        if (credit) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Credit check pass"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "Credit check fail"));

        }
    }

    public void rejectPo() {
        int s = Integer.parseInt(reason.substring(0, 2));
        s = s + 6;
        purchaseOrderMB.rejectPo(userId,myOrder, s);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "This order has benn rejected."));

    }

    public ProductOrder getMyOrder() {
        myOrder = (ProductOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
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

    public List<String> getRejectReasons() {
        return rejectReasons;
    }

    public void setRejectReasons(List<String> rejectReasons) {
        this.rejectReasons = rejectReasons;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
