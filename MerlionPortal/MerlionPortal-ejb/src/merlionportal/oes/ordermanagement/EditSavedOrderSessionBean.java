/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.ordermanagement;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
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

    public void persist(Object object) {
        em.persist(object);
    }

    public EditSavedOrderSessionBean() {
    }

    public void updatePoLineItem(ProductOrderLineItem poLine) {
        em.merge(poLine);
        em.flush();

    }
    public void updateProductOrder(ProductOrder po){
        em.merge(po);
        em.flush();
    }

}
