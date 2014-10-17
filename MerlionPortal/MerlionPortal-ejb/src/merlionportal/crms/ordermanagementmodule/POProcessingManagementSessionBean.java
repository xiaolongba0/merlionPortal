/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.ordermanagementmodule;

import entity.Payment;
import entity.ServiceInvoice;
import entity.ServicePO;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class POProcessingManagementSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
//    PO Status
//    1. PO Waiting to be processed
//    2. PO Deleted
//    3. PO Hold
//    4. PO Rejected
//    5. SO Waiting for fulfillment
//    6. SO Fulfilled (For WMS only)
//    7. SO Invoiced
//    8. SO Closed
//    9. Transportation SO in transit (For TMS only)

    public int rejectPO(Integer servicePOId) {
        ServicePO po = em.find(ServicePO.class, servicePOId);
        if (po != null) {
            po.setStatus(4);
            em.merge(po);
            em.flush();

            return 1;
        }
        return 0;

    }

    private void generateSO(Integer servicePOId) {
        ServicePO po = em.find(ServicePO.class, servicePOId);
        if (po != null) {
            po.setStatus(5);
            em.merge(po);
            em.flush();

        }

    }

    //Invoice status
    //1. Invoiced
    //2. Payment information recorded
    //3. Paid
    public int generateInvoice(Integer servicePOId, Integer userId, String conditionText) {
        ServicePO po = em.find(ServicePO.class, servicePOId);
        if (po != null) {
            po.setStatus(7);

            ServiceInvoice invoice = new ServiceInvoice();
            invoice.setPrice(po.getPrice());
            invoice.setCreatorId(userId);
            invoice.setSenderCompanId(po.getReceiverCompanyId());
            invoice.setReceiverCompanyId(po.getSenderCompanyId());
            invoice.setServicePO(po);
            invoice.setStatus(1);
            invoice.setConditionText(conditionText);
            invoice.setCreateDate(new Date());

            po.setServiceInvoice(invoice);
            po.setStatus(7);

            em.persist(invoice);
            em.merge(po);
            em.flush();

            return 1;
        }
        return 0;
    }

    public int releasePOHold(Integer servicePOId) {
        ServicePO po = em.find(ServicePO.class, servicePOId);
        if (po != null) {
            po.setStatus(5);
            em.merge(po);
            em.flush();

            return 1;
        }
        return 0;
    }

    public boolean passCreditCheck(Integer companyId, Integer contractId, Integer servicePOId) {
        boolean result = true;
        System.out.println("Enter EJB passCreditCheck");
        Query q = em.createNamedQuery("ServicePO.findBySenderCompanyId").setParameter("senderCompanyId", companyId);
        for (Object o : q.getResultList()) {
            System.out.println("Enter EJB Loop passCreditCheck");

            ServicePO po = (ServicePO) o;
            //This contract, but not this po
            if (po.getContract().getContractId() == (int) contractId) {
                //not paid
                if (po.getStatus() != 7) {
                    System.out.println("Enter EJB Order not paid" + po.getServicePOId());
                    result = false;
                }
            }
        }
        if (result == false) {
            ServicePO thisPO = em.find(ServicePO.class, servicePOId);
            thisPO.setStatus(3);
            em.merge(thisPO);
            em.flush();
        } else {
            this.generateSO(servicePOId);
            result = true;
        }
        //passes for all iteration
        return result;

    }

    //Payment method
    //1. Telegrapgic Transfer
    //2. Credit Card
    //3. Paper Check
    public boolean recordPaymentInfo(Integer invoiceId, Integer method, Date receivedDate, Date createdDate, String accountInfo, String creditCardNo, Double amount, Integer swiftcode, Integer checkNumber) {
        Payment payment = new Payment();
        payment.setMethod(method);
        if (method == 1) {
            payment.setSwiftCode(swiftcode);
            payment.setAccountInfo(accountInfo);
            payment.setAmount(amount);
            payment.setReceiveDate(receivedDate);

        } else if (method == 2) {
            payment.setCreditCardNo(creditCardNo);
            payment.setAmount(amount);
            payment.setReceiveDate(receivedDate);

        } else if (method == 3) {
            payment.setCheckNumber(checkNumber);
            payment.setReceiveDate(receivedDate);
        } else {
            //Method not supported

        }
        payment.setCreatedDate(new Date());
        ServiceInvoice invoice = em.find(ServiceInvoice.class, invoiceId);
        invoice.setPayment(payment);
        invoice.setStatus(2);
        payment.setServiceInvoice(invoice);

        em.persist(payment);
        em.merge(invoice);
        em.flush();

        return true;

    }

    public int updatePaymentStatus(Integer invoiceId) {
        ServiceInvoice invoice = em.find(ServiceInvoice.class, invoiceId);
        if (invoice != null) {
            invoice.setStatus(3);
            ServicePO po = invoice.getServicePO();
            po.setStatus(7);
            em.merge(po);

            em.merge(invoice);
            em.flush();

            return 1;
        }
        return 0;

    }
}
