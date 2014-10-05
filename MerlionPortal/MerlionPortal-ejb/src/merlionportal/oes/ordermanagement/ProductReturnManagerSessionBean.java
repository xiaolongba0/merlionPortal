/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.ordermanagement;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class ProductReturnManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    private ProductOrder order;

    public ProductReturnManagerSessionBean() {
    }

    public ProductOrder searchForOrder(int orderId) {
        order = em.find(ProductOrder.class, orderId);
        return order;
    }

    public void rejectAllOrder(ProductOrder myorder, int reason) {
        myorder.setStatus(reason);
        em.merge(myorder);
    }

    public void rejectLineItem(ProductOrder myOrder, ProductOrderLineItem myLine) {
        List<ProductOrderLineItem> myList = new ArrayList();
        Double totalPrice=0.0;
        Double myPrice = myLine.getPrice();
        Double orderPrice = myOrder.getPrice();
        orderPrice = orderPrice - myPrice;
        myOrder.setPrice(orderPrice);
        myLine.setStatus("Rejected");
        myLine.setPrice(0.0);
        em.merge(myLine);
        myList = myOrder.getProductOrderLineItemList();
        for (Object o : myList) {
            ProductOrderLineItem nLine = (ProductOrderLineItem) o;
            totalPrice += nLine.getPrice();
        }
        myOrder.setPrice(totalPrice);
        em.merge(myOrder);

    }

    public Boolean checkOrderValidity(int orderId) {
        System.out.println("================================" + orderId);
        ProductOrder newOrder = this.searchForOrder(orderId);
        System.out.println("stats" + newOrder.getStatus());

        if (newOrder.getStatus() == 2 || newOrder.getStatus() == 3 || newOrder.getStatus() == 4 || newOrder.getStatus() == 5) {
            System.out.println("&&&%%%%%%%%%%%%%%%%%%%%%%% false");
            return true;
        }
        return false;
    }

}
