/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.ordermanagement;

import entity.ProductOrder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class PaymentManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public PaymentManagerSessionBean() {
    }

    public List<ProductOrder> getAllUnpaidOrder(int customerId) {
        List<ProductOrder> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM ProductOrder p WHERE p.creatorId = :creatorId AND p.status =:status");
        q.setParameter("creatorId", customerId);
        q.setParameter("status", 5);
        for (Object o : q.getResultList()) {
            ProductOrder ordr = (ProductOrder) o;
            result.add(ordr);

        }
        return result;
    }

    public List<String> allPaymentMethods() {
        List<String> paymentMethods = new ArrayList();
        paymentMethods.add("Credit card");
        paymentMethods.add("Telegraphic transfer");
        paymentMethods.add("Telegraphic transfer");

        return paymentMethods;
    }
    
    public Boolean makePayement(ProductOrder myorder, String billto){
        if(myorder.getStatus()!=5){
            return false;
        }
        myorder.setBillTo(billto);
        myorder.setStatus(4);
        em.merge(myorder);
        return true;
    }

}
