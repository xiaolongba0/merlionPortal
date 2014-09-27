/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.io.IOException;
import java.util.ArrayList;
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
        myOrder = (ProductOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
        credit = purchaseOrderMB.checkCredit(myOrder.getCreatorId());

    }

    private List<String> setReasons() {
        List<String> reasonList = new ArrayList();
        reasonList.add("01 Wrong product");
        reasonList.add("02 Wrong product quantity");
        reasonList.add("03 Wrong price");
        reasonList.add("04 Wrong ship to address");
        reasonList.add("05 Wrong contact person");
        reasonList.add("06 Credit check fail ");
        reasonList.add("07 Others please contact sales for more information");
        reasonList.add("08 Customer request for cancelation");
        reasonList.add("09 Unable to fulfill this order");
        return reasonList;
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
        int s = Integer.parseInt(reason.substring(0, 2));
        s = s + 6;
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

    public List<String> getRejectReasons() {
        rejectReasons=this.setReasons();
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
