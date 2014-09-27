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
import merlionportal.oes.ordermanagement.ProductReturnManagerSessionBean;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "productReturnManagedBean")
@RequestScoped
public class ProductReturnManagedBean {

    @EJB
    private ProductReturnManagerSessionBean returnMB;
    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrderMB;

    private List<String> customerInfor;
    private ProductOrder myOrder;
    private int orderId;

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

}
