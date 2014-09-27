/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "orderDetailMangedBean")
@RequestScoped
public class OrderDetailMangedBean {

    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrderMB;

    private ProductOrder order;
    private List<String> customerInfor;
    private String rejectReason;
    private List<String> reasonList;

    public OrderDetailMangedBean() {
    }

    public ProductOrder getOrder() {
        order = (ProductOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("viewOrder");
        return order;
    }

    public void setOrder(ProductOrder order) {
        this.order = order;
    }

    public List<String> getCustomerInfor() {
        customerInfor = purchaseOrderMB.getCustomerCompany(order.getCreatorId());
        return customerInfor;
    }

    public void setCustomerInfor(List<String> customerInfor) {
        this.customerInfor = customerInfor;
    }

    public String displayStatus() {
        String result = purchaseOrderMB.viewMyOrderStatus(order.getStatus());
        return result;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public List<String> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<String> reasonList) {
        this.reasonList = reasonList;
    }

}