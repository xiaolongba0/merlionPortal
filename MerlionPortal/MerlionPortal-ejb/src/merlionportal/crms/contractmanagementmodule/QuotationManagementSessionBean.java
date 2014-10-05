/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.contractmanagementmodule;

import entity.Contract;
import entity.ServiceCatalog;
import entity.ServiceQuotation;
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
public class QuotationManagementSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

//
//    Quotation status:
//    1 request for quotation
//    2 waiting for acception
//    3 valid
//    4 rejected request
//    5 rejected quotation
//    6 pending fulfillment check
//    7 fulfillment check fail
//    8 fulfillment check success
//    
    public int createRequestForQuotation(Integer serviceCatalogId, String serviceType, Date startDate, Date endDate, Integer senderCompanyId, Integer receiverCompanyId,
            String origin, String destination) {
        ServiceQuotation serviceQuotation = new ServiceQuotation();
        serviceQuotation.setServiceType(serviceType);
        serviceQuotation.setCreatedDate(new Date());
        serviceQuotation.setStartDate(startDate);
        serviceQuotation.setEndDate(endDate);
        serviceQuotation.setSenderCompanyId(senderCompanyId);
        serviceQuotation.setReceiverCompanyId(receiverCompanyId);
        serviceQuotation.setStatus(1);

        if (serviceType.equals("Transportation") && !origin.isEmpty() && !destination.isEmpty()) {
            serviceQuotation.setDestination(destination);
            serviceQuotation.setOrigin(origin);
        }

        List<Contract> contracts = new ArrayList<>();
        serviceQuotation.setContractList(contracts);

        ServiceCatalog serviceCatalog = em.find(ServiceCatalog.class, serviceCatalogId);
        serviceCatalog.getServiceQuotationList().add(serviceQuotation);

        serviceQuotation.setServiceCatalog(serviceCatalog);

        em.persist(serviceQuotation);
        em.merge(serviceCatalog);
        em.flush();

        System.out.println("QuotationId is " + serviceQuotation.getQuotationId());
        return serviceQuotation.getQuotationId();

    }

    public int rejectRequestForQuotation(Integer quotationId) {
        ServiceQuotation serviceQuotation = em.find(ServiceQuotation.class, quotationId);
        serviceQuotation.setStatus(4);

        em.merge(serviceQuotation);
        em.flush();

        return serviceQuotation.getQuotationId();
    }

    public int createQuotation(Integer quotationId, Double price, int discountRate) {
        ServiceQuotation serviceQuotation = em.find(ServiceQuotation.class, quotationId);
        serviceQuotation.setStatus(2);
        serviceQuotation.setPrice(price);
        serviceQuotation.setDiscountRate(discountRate);
        serviceQuotation.setCreatedDate(new Date());

        em.merge(serviceQuotation);
        em.flush();

        return serviceQuotation.getQuotationId();

    }

    public List<ServiceQuotation> viewAllRequestsSent(Integer myCompanyId) {
        Query q = em.createQuery("SELECT s FROM ServiceQuotation s WHERE s.senderCompanyId = :senderCompanyId AND (s.status=1 OR s.status=6)").setParameter("senderCompanyId", myCompanyId);
        return (List<ServiceQuotation>) q.getResultList();

    }

    public List<ServiceQuotation> viewAllRequestReceived(Integer myCompanyId) {
        Query q = em.createQuery("SELECT s FROM ServiceQuotation s WHERE s.receiverCompanyId = :receiverCompanyId AND (s.status=1 OR s.status=6 OR s.status=7 OR s.status=8)").setParameter("receiverCompanyId", myCompanyId);
        return (List<ServiceQuotation>) q.getResultList();
    }

    public int acceptQuotation(Integer quotationId) {
        ServiceQuotation serviceQuotation = em.find(ServiceQuotation.class, quotationId);
        serviceQuotation.setStatus(3);

        em.merge(serviceQuotation);
        em.flush();

        return serviceQuotation.getQuotationId();

    }

    public int rejectQuotation(Integer quotationId) {
        ServiceQuotation serviceQuotation = em.find(ServiceQuotation.class, quotationId);
        serviceQuotation.setStatus(5);

        em.merge(serviceQuotation);
        em.flush();

        return serviceQuotation.getQuotationId();

    }

    public int fulfillmentAvailabilityCheck(Integer quotationId) {
        ServiceQuotation serviceQuotation = em.find(ServiceQuotation.class, quotationId);
        serviceQuotation.setStatus(6);

        em.merge(serviceQuotation);
        em.flush();

        return serviceQuotation.getQuotationId();
    }
    
    public List<ServiceQuotation> viewAllQuotationsSent(Integer myCompanyId){
        Query q = em.createQuery("SELECT s FROM ServiceQuotation s WHERE s.receiverCompanyId = :receiverCompanyId AND (s.status =2 OR s.status =3 OR s.status =4 OR s.status =5)").setParameter("receiverCompanyId", myCompanyId);
        return (List<ServiceQuotation>) q.getResultList();
    }
    public List<ServiceQuotation> viewAllQuotationsReceived(Integer myCompanyId){
        Query q = em.createQuery("SELECT s FROM ServiceQuotation s WHERE s.senderCompanyId = :senderCompanyId AND (s.status =2 OR s.status =3 OR s.status =4 OR s.status =5)").setParameter("senderCompanyId", myCompanyId);
        return (List<ServiceQuotation>) q.getResultList();
    }

}
