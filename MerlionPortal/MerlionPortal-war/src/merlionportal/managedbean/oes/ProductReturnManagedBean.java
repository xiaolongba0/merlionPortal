/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.oes.ordermanagement.ProductReturnManagerSessionBean;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "productReturnManagedBean")
@ViewScoped
public class ProductReturnManagedBean {

    @EJB
    private ProductReturnManagerSessionBean returnMB;
    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrderMB;

    private Integer companyId;
    private Integer userId;
    private List<String> customerInfor;
    private ProductOrder myOrder;
    private int orderId;
    private int returenOrderId;
    private ProductOrder returnOrder;
    private List<String> returnReasons;
    private String reason;

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

    public void searchForOrder() {
        myOrder = returnMB.searchForOrder(orderId);
    }

    public ProductReturnManagedBean() {
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

    public List<String> getCustomerInfor() {
        customerInfor = purchaseOrderMB.getCustomerCompany(myOrder.getCreatorId());
        return customerInfor;
    }

    public int getReturenOrderId() {
        return returenOrderId;
    }

    public void setReturenOrderId(int returenOrderId) {
        this.returenOrderId = returenOrderId;
    }

    public void returnMyOrder() {
        System.out.println("======***************" + returenOrderId);
        if (returnMB.checkOrderValidity(returenOrderId)) {
            returnOrder = returnMB.searchForOrder(returenOrderId);
            System.out.println("*&&&&&&&&&&&&&&&======***************" + returnOrder.getProductPOId());

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Quotation id invalid"));
        }
    }

    public ProductOrder getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(ProductOrder returnOrder) {
        this.returnOrder = returnOrder;
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
        reasonList.add("08 Unable to fulfill this order");
        return reasonList;
    }

    public List<String> getReturnReasons() {
        returnReasons = this.setReasons();
        return returnReasons;
    }

    public void setReturnReasons(List<String> returnReasons) {
        this.returnReasons = returnReasons;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void rejectWhole() {
        System.out.println(reason);
        int s = Integer.parseInt(reason.substring(0, 2));
        System.out.println("=======================" + s);
        s = s + 5;
        System.out.println("=======================" + s);
        returnMB.rejectAllOrder(myOrder, s);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "This order has benn rejected."));

    }

    public void rejectLineItem(ProductOrderLineItem myLine) {

        returnMB.rejectLineItem(returnOrder, myLine);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "This Line Item has benn rejected."));

    }

}
