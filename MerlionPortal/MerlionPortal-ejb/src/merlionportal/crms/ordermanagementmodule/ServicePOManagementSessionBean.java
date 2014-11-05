/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.ordermanagementmodule;

import entity.Contract;
import entity.ServiceInvoice;
import entity.ServicePO;
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
    public boolean createServicePO(Integer contractId, Integer creatorId, Integer volume, Date fulfillmentDate, Date receiveDate, Date serviceDeliveryDate, int QuantityPerTEU, Integer productId, String warehouseOrderType) {
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
            po.setWarehousePOtype(warehouseOrderType);
            po.setServiceFulfilmentDate(fulfillmentDate);
            po.setServiceReceiveDate(receiveDate);
            po.setProductQuantityPerTEU(QuantityPerTEU);
            po.setProductId(productId);
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

    public int updateServicePO(Integer servicePOId, Date deliveryDate, Date fulfillDate, Date receiveDate, Integer volume, Integer creatorId, Integer productId, Integer productQuantityPerTEU, String warehousePOType) {
        ServicePO po = em.find(ServicePO.class, servicePOId);
        po.setWarehousePOtype(warehousePOType);
        po.setServiceFulfilmentDate(fulfillDate);
        po.setServiceReceiveDate(receiveDate);
        po.setServiceDeliveryDate(deliveryDate);
        po.setVolume(volume);
        po.setProductId(productId);
        po.setProductQuantityPerTEU(productQuantityPerTEU);
        System.out.println("volume is :" + volume);
        po.setPrice(volume*po.getContract().getPrice());
        
        em.merge(po);
        em.flush();
        
        return 1;
    }

    public List<ServicePO> retrieveSentTransportationRequests(Integer myCompanyId){
        Query q = em.createQuery("SELECT s FROM ServicePO s WHERE s.senderCompanyId = :senderCompanyId AND s.serviceType = :serviceType");
        q.setParameter("senderCompanyId", myCompanyId);
        q.setParameter("serviceType", "Transportation");
       
        return (List<ServicePO>)q.getResultList();
    }
    public List<ServicePO> retrieveReceivedTransportationRequests(Integer myCompanyId){
        Query q = em.createQuery("SELECT s FROM ServicePO s WHERE s.receiverCompanyId = :receiverCompanyId AND s.serviceType = :serviceType");
        q.setParameter("receiverCompanyId", myCompanyId);
        q.setParameter("serviceType", "Transportation");
        List<ServicePO> returnedList = new ArrayList<>();
        for(Object o : q.getResultList()){
            ServicePO po = (ServicePO) o;
            if(po.getStatus()==5 || po.getStatus()==6 || po.getStatus()==7 ||po.getStatus()==8 || po.getStatus()==9 ){
                returnedList.add(po);
            }
        }
        return returnedList;
    }
}
