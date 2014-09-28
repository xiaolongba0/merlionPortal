/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.ordermanagement;

import entity.ProductInvoice;
import entity.ProductOrder;
import java.util.ArrayList;
import java.util.List;
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
public class InvoiceMangerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public InvoiceMangerSessionBean() {
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public List<ProductOrder> getAllWaitingForInvoice(int companyId) {
        List<ProductOrder> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM ProductOrder p WHERE p.companyId = :companyId AND p.status =:status");
        q.setParameter("companyId", companyId);
        q.setParameter("status", 2);
        for (Object o : q.getResultList()) {
            ProductOrder order = (ProductOrder) o;
            result.add(order);
        }
        return result;
    }

    public void generateInvoice(ProductOrder myOrder, int orderId, Double totalPrice, int customerId, String text) {
        ProductInvoice newInvoice = new ProductInvoice();
        newInvoice.setCustomerId(customerId);
        newInvoice.setSalesOrderId(orderId);
        newInvoice.setTotalPrice(totalPrice);
        newInvoice.setStatus("Invoiced");
        newInvoice.setCondition(text);
        em.persist(newInvoice);
        myOrder.setStatus(5);
        em.merge(myOrder);

    }

    public List<ProductInvoice> viewAllInvoice(int companyId) {
        Query q = em.createQuery("SELECT p FROM ProductInvoice p");
        int salesOrderId;
        List<ProductInvoice> result = new ArrayList();
        for (Object o : q.getResultList()) {
            ProductInvoice invoice = (ProductInvoice) o;
            salesOrderId = invoice.getSalesOrderId();
            ProductOrder myOrder = em.find(ProductOrder.class, salesOrderId);
            if (companyId == myOrder.getCompanyId()) {
                result.add(invoice);
            }

        }
        return result;

    }

    public List<ProductInvoice> viewAllInvoice(int companyId, int userId) {
        Query q = em.createQuery("SELECT p FROM ProductInvoice p WHERE p.customerId = :customerId").setParameter("customerId", userId);
        int salesOrderId;
        List<ProductInvoice> result = new ArrayList();
        for (Object o : q.getResultList()) {
            ProductInvoice invoice = (ProductInvoice) o;
            salesOrderId = invoice.getSalesOrderId();
            ProductOrder myOrder = em.find(ProductOrder.class, salesOrderId);
            if (companyId == myOrder.getCompanyId()) {
                result.add(invoice);
            }

        }
        return result;
    }
    
    public List<String> setAllStatus(){
        List<String> allStatus =new ArrayList();
        allStatus.add("Invoiced");
        allStatus.add("Waiting for credit");
        return allStatus;
    }
    
    public ProductOrder getMyOrder(int orderId){
        ProductOrder myOrder = em.find(ProductOrder.class, orderId);
        return myOrder;
    }
}
