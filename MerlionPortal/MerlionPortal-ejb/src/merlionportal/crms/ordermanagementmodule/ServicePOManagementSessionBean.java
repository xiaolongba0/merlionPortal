/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.ordermanagementmodule;

import entity.Contract;
import entity.ServiceInvoice;
import entity.ServicePO;
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
public class ServicePOManagementSessionBean {

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
//    6. SO Fulfilled
//    7. SO Invoiced
//    8. SO Closed
//    9. Transportation SO in transit
    public boolean createServicePO(Integer contractId, Integer creatorId, Integer volume, Date serviceStartDate, Date serviceEndDate, Date serviceDeliveryDate) {
        ServicePO po = new ServicePO();
        Contract contract = em.find(Contract.class, contractId);
        if (contract.getStatus() == 5) {
            po.setServiceType(contract.getServiceType());
            po.setCreatedDate(new Date());
            po.setStatus(1);
            po.setVolume(volume);
            po.setPrice(volume * contract.getPrice());
            po.setReceiverCompanyId(contract.getPartyA());
            po.setSenderCompanyId(contract.getPartyB());
            po.setCreatorId(creatorId);
            po.setServiceDeliveryDate(serviceDeliveryDate);
            po.setServiceStartDate(serviceStartDate);
            po.setServiceEndDate(serviceEndDate);
            po.setContract(contract);

            contract.getServicePOList().add(po);
            em.persist(po);
            em.merge(contract);
            em.flush();

            return true;

        } else {
            return false;
        }
    }

    public List<ServicePO> viewAllSentServicePO(Integer myCompanyId) {
        Query q = em.createNamedQuery("ServicePO.findBySenderCompanyId").setParameter("senderCompanyId", myCompanyId);
        return (List<ServicePO>) q.getResultList();
    }

    public List<ServicePO> viewAllReceivedServicePO(Integer myCompanyId) {
        Query q = em.createNamedQuery("ServicePO.findByReceiverCompanyId").setParameter("receiverCompanyId", myCompanyId);
        return (List<ServicePO>) q.getResultList();
    }

    public int deleteServicePO(Integer servicePOId, Integer myCompanyId) {
        ServicePO po = em.find(ServicePO.class, servicePOId);
        if (po.getSenderCompanyId() == (int) myCompanyId) {
            if (po.getStatus() == 1) {
                em.remove(po);
                em.flush();
                return 1;
            } else {
//                PO is alr processed, promte user to contact service provider directly
                return 2;
            }
        }
        return -1;
    }

    public List<ServiceInvoice> viewAllReceivedInvoices(Integer myCompanyId) {
        Query q = em.createNamedQuery("ServiceInvoice.findByReceiverCompanyId").setParameter("receiverCompanyId", myCompanyId);
        return (List<ServiceInvoice>) q.getResultList();
    }

    public List<ServiceInvoice> viewAllSentInvoices(Integer myCompanyId) {
        Query q = em.createNamedQuery("ServiceInvoice.findBySenderCompanId").setParameter("senderCompanId", myCompanyId);
        return (List<ServiceInvoice>) q.getResultList();
    }

    public Contract searchAValidContract(Integer contractId, Integer myCompanyId) {
        Contract contract = em.find(Contract.class, contractId);
        if (contract != null) {
            if (contract.getStatus() == 5 && (int) contract.getPartyB() == myCompanyId) {
                return contract;
            }
        }
        return null;
    }

    public int updateServicePO(Integer servicePOId, Date deliveryDate, Date serviceStartDate, Date serviceEndDate, Integer volume, Integer creatorId) {
        ServicePO po = em.find(ServicePO.class, servicePOId);
        po.setServiceStartDate(serviceStartDate);
        po.setServiceEndDate(serviceEndDate);
        po.setServiceDeliveryDate(deliveryDate);
        po.setVolume(volume);
        System.out.println("volume is :" + volume);
        po.setPrice(volume*po.getContract().getPrice());
        
        em.merge(po);
        em.flush();
        
        return 1;
    }

}
