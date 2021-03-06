/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.ordermanagementmodule;

import entity.GrnsServiceOrder;
import entity.OtherInvoice;
import java.math.BigInteger;
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
 * @author manliqi
 */
@Stateless
@LocalBean
public class GRNSOrderSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //Status
    //1. Confirmed
    //2. Payment Info Recorded
    //3. Paid
    //4. Processed
    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public List<GrnsServiceOrder> viewAllSentOrders(Integer myCompId) {
        Query q = em.createNamedQuery("GrnsServiceOrder.findByServiceRequester").setParameter("serviceRequester", myCompId);
        return (List<GrnsServiceOrder>) q.getResultList();
    }

    public List<GrnsServiceOrder> viewAllReceivedOrders(Integer myCompId) {
        Query q = em.createNamedQuery("GrnsServiceOrder.findByServiceProvider").setParameter("serviceProvider", myCompId);
        return (List<GrnsServiceOrder>) q.getResultList();
    }

    public List<GrnsServiceOrder> viewAllTransportationOrder(List<GrnsServiceOrder> all) {
        List<GrnsServiceOrder> returnList = new ArrayList<>();
        for (GrnsServiceOrder order : all) {
            if (order.getServiceType().equals("Transportation")) {
                returnList.add(order);
            }
        }
        return returnList;
    }

    public List<GrnsServiceOrder> viewAllWarehouseOrder(List<GrnsServiceOrder> all) {
        List<GrnsServiceOrder> returnList = new ArrayList<>();
        for (GrnsServiceOrder order : all) {
            if (order.getServiceType().equals("Warehouse")) {
                returnList.add(order);
            }
        }
        return returnList;
    }

    public boolean recordPaymentInformation(Integer invoiceId, String method, Date receivedDate, String accountInfo, BigInteger creditCardNo, Integer swiftcode, BigInteger checkNumber) {
        OtherInvoice invoice = em.find(OtherInvoice.class, invoiceId);
        invoice.setMethod(method);
        if (method.equals("Telegraphic Transfer")) {
            invoice.setSwiftcode(swiftcode);
            invoice.setAccountInfo(accountInfo);
            invoice.setReceiveDate(receivedDate);

        } else if (method.equals("Credit Card")) {
            invoice.setCreditCardNo(creditCardNo);
            invoice.setReceiveDate(receivedDate);

        } else if (method.equals("Paper Check")) {
            invoice.setCheckNumber(checkNumber);
            invoice.setReceiveDate(receivedDate);

        } else {
            //Method not supported

        }
        if (invoice.getGrnsOrder()) {
            GrnsServiceOrder grnsOrder = em.find(GrnsServiceOrder.class, invoice.getGrnsOrderId());
            grnsOrder.setStatus("Payment Info Recorded");
            em.merge(grnsOrder);
        }
        invoice.setStatus("Payment Info Recorded");
        em.merge(invoice);
        em.flush();

        return true;
    }

    public boolean updateGRNSPaymentStatus(Integer otherInvoiceId) {
        OtherInvoice invoice = em.find(OtherInvoice.class, otherInvoiceId);
        invoice.setStatus("Paid");
        GrnsServiceOrder grnsOrder = em.find(GrnsServiceOrder.class, invoice.getGrnsOrderId());
        grnsOrder.setStatus("Paid");
        em.merge(grnsOrder);
        em.merge(invoice);
        em.flush();

        return true;
    }

    public boolean updateContractPaymentStatus(Integer otherInvoiceId) {
        OtherInvoice invoice = em.find(OtherInvoice.class, otherInvoiceId);
        invoice.setStatus("Paid");

        em.merge(invoice);
        em.flush();

        return true;
    }

    public boolean createGRNSOrderInvoice(Integer orderId, Integer creatorId, Integer requester, Integer provider, Double price) {
        OtherInvoice invoice = new OtherInvoice();
        invoice.setCreatedDate(new Date());
        invoice.setStatus("Confirmed");
        invoice.setGrnsOrder(true);
        invoice.setGrnsOrderId(orderId);
        invoice.setSenderCompanyId(provider);
        invoice.setReceiverCompanyId(requester);
        invoice.setPrice(price);
        invoice.setCreatorId(creatorId);

        em.persist(invoice);
        em.flush();

        return true;
    }

    public boolean createWarehouseSpaceContractInvoice(Integer contractId, Integer creatorId, Integer sender, Integer receiver, Double price) {
        OtherInvoice invoice = new OtherInvoice();
        invoice.setCreatedDate(new Date());
        invoice.setStatus("Confirmed");
        invoice.setGrnsOrder(false);
        invoice.setContractId(contractId);
        invoice.setSenderCompanyId(sender);
        invoice.setReceiverCompanyId(receiver);
        invoice.setPrice(price);
        invoice.setCreatorId(creatorId);

        em.persist(invoice);
        em.flush();

        return true;
    }

    public Integer getOtherInvoiceIdFromOrder(Integer orderId) {
        Query q = em.createNamedQuery("OtherInvoice.findByGrnsOrderId").setParameter("grnsOrderId", orderId);
        OtherInvoice in = (OtherInvoice) q.getSingleResult();
        return in.getInvoiceId();
    }

    public OtherInvoice getOtherInvoiceFromOrder(Integer orderId) {
        Query q = em.createNamedQuery("OtherInvoice.findByGrnsOrderId").setParameter("grnsOrderId", orderId);
        OtherInvoice in = (OtherInvoice) q.getSingleResult();
        return in;
    }

    public List<OtherInvoice> getAllWarehouseRentalInvoiceSent(Integer myCompanyId) {
        Query q = em.createQuery("SELECT o FROM OtherInvoice o WHERE o.senderCompanyId = :senderCompanyId AND o.grnsOrder = :grnsOrder");
        q.setParameter("senderCompanyId", myCompanyId);
        q.setParameter("grnsOrder", false);

        return q.getResultList();
    }

    public List<OtherInvoice> getAllWarehouseRentalInvoiceReceived(Integer myCompanyId) {
        Query q = em.createQuery("SELECT o FROM OtherInvoice o WHERE o.receiverCompanyId = :receiverCompanyId AND o.grnsOrder = :grnsOrder");
        q.setParameter("receiverCompanyId", myCompanyId);
        q.setParameter("grnsOrder", false);

        return q.getResultList();
    }
}
