/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.ordermanagement;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
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
public class EditSavedOrderSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;


    public EditSavedOrderSessionBean() {
    }

    public void updatePoLineItem(ProductOrderLineItem poLine) {
        em.merge(poLine);
        em.flush();

    }

    public void updateProductOrder(ProductOrder po) {
        em.merge(po);
        em.flush();
    }

    public void clearList(ProductOrder myOrder) {
        List<ProductOrderLineItem> list = myOrder.getProductOrderLineItemList();
        myOrder.getProductOrderLineItemList().clear();
        em.merge(myOrder);
        for (Object o : list) {
            ProductOrderLineItem pLine = (ProductOrderLineItem) o;
            em.remove(pLine);
        }

    }
}
