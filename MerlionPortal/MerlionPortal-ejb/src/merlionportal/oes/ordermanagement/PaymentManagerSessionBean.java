/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.ordermanagement;

import entity.ProductInvoice;
import entity.ProductOrder;
import entity.ProductPayment;
import java.util.ArrayList;
import java.util.Date;
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
public class PaymentManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public PaymentManagerSessionBean() {
    }

    public List<ProductInvoice> getAllUnpaidInvoice(int customerId) {
        List<ProductInvoice> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM ProductInvoice p WHERE p.customerId = :customerId AND p.status= :status");
        q.setParameter("customerId", customerId);
        q.setParameter("status", "Invoiced");
        for (Object o : q.getResultList()) {
            ProductInvoice ordr = (ProductInvoice) o;
            result.add(ordr);
        }
        return result;
    }

    public List<ProductInvoice> getAllWaitingInvoice(int companyId) {
        List<ProductInvoice> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM ProductInvoice p");
        for (Object o : q.getResultList()) {
            ProductInvoice ordr = (ProductInvoice) o;
            ProductOrder myOrder = em.find(ProductOrder.class, ordr.getSalesOrderId());
            if (myOrder.getCompanyId() == companyId && !ordr.getStatus().equalsIgnoreCase("Paid")) {
                result.add(ordr);
            }
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

    public List<String> findServiceOrder(int orderId) {
        List<String> result = new ArrayList();
        ProductOrder myOrder = em.find(ProductOrder.class, orderId);
        result.add(myOrder.getContactPersonName());
        result.add(myOrder.getContactPersonPhoneNumber());
        result.add(myOrder.getShipTo());
        return result;
    }

    public int updatePaymentStatus(ProductInvoice productInvoice) {
        if (productInvoice != null) {
            productInvoice.setStatus("Paid");
            ProductOrder po = em.find(ProductOrder.class, productInvoice.getSalesOrderId());
            po.setStatus(4);
            em.merge(po);
            em.merge(productInvoice);
            em.flush();
            return 1;
        }
        return 0;

    }

    public Boolean makePaymentT(Integer method, Double amount, ProductInvoice myInvoice, Integer swift, String accontInfo) {
        if (!amount.equals(myInvoice.getTotalPrice())) {
            return false;
        }
        ProductPayment myPayment = new ProductPayment();
        myPayment.setMethod(method);
        myPayment.setPaymentId(myInvoice.getInvoiceId());
        myPayment.setAccountInfo(accontInfo);
        myPayment.setAmount(amount);
        myPayment.setSwiftCode(swift);
        myPayment.setProductInvoice(myInvoice);
        Date todayDate = new Date();
        myPayment.setCreatedDate(todayDate);
        em.persist(myPayment);
        myInvoice.setProductPayment(myPayment);
        myInvoice.setStatus("Waiting For Confirmation");
        System.out.println("record PaymentInfor=================3" + myInvoice.getInvoiceId() + swift + accontInfo);
        em.merge(myInvoice);
        return true;
    }

    public Boolean makePaymentC(Integer method,Double amount, ProductInvoice myInvoice, String cardNumber, String accontInfo) {
        if (!amount.equals(myInvoice.getTotalPrice())) {
            return false;
        }
        ProductPayment myPayment = new ProductPayment();
        myPayment.setMethod(method);
        myPayment.setPaymentId(myInvoice.getInvoiceId());
        myPayment.setAccountInfo(accontInfo);
        myPayment.setAmount(amount);
        myPayment.setCreditCardNo(cardNumber);
        myPayment.setProductInvoice(myInvoice);
        Date todayDate = new Date();
        myPayment.setCreatedDate(todayDate);
        em.persist(myPayment);
        myInvoice.setProductPayment(myPayment);
        myInvoice.setStatus("Waiting For Confirmation");
        em.merge(myInvoice);
        em.flush();
        return true;
    }

    public Boolean makePaymentCheck(Integer method,Double amount, ProductInvoice myInvoice, String cardNumber) {
        if (!amount.equals(myInvoice.getTotalPrice())) {
            return false;
        }
        ProductPayment myPayment = new ProductPayment();
        myPayment.setMethod(method);
        myPayment.setPaymentId(myInvoice.getInvoiceId());
        myPayment.setAmount(amount);
        myPayment.setCreditCardNo(cardNumber);
        myPayment.setProductInvoice(myInvoice);
        Date todayDate = new Date();
        myPayment.setCreatedDate(todayDate);
        em.persist(myPayment);
        myInvoice.setProductPayment(myPayment);
        myInvoice.setStatus("Waiting For Confirmation");
        em.merge(myInvoice);
        em.flush();
        return true;
    }
}
