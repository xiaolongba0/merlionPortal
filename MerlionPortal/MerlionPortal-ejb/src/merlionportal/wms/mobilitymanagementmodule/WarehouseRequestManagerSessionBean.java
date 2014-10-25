/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.mobilitymanagementmodule;

import entity.Company;
import entity.Contract;
import entity.ServicePO;
import entity.StorageBin;
import entity.Warehouse;
import entity.WarehouseZone;
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
public class WarehouseRequestManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public WarehouseRequestManagerSessionBean() {
    }

    public List<ServicePO> viewPendingRequest(int companyId) {

        List<ServicePO> searchResult = new ArrayList();
        Query q = em.createQuery("SELECT s FROM ServicePO s WHERE s.receiverCompanyId = :receiverCompanyId");
        q.setParameter("receiverCompanyId", companyId);
        for (Object o : q.getResultList()) {
            ServicePO wOrder = (ServicePO) o;
            if (wOrder.getStatus() == 5) {
                searchResult.add(wOrder);
            }
        }
        return searchResult;

    }

    public String viewCompanyName(int companyId) {
        Company myCom = em.find(Company.class, companyId);
        String result = myCom.getName();
        return result;
    }

    public String viewCompanyContactPersonName(int companyId) {
        Company myCom = em.find(Company.class, companyId);
        String result = myCom.getContactPersonName();
        return result;
    }

    public String viewCompanyContact(int companyId) {
        Company myCom = em.find(Company.class, companyId);
        String result = myCom.getContactNumber();
        return result;
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public List<String> viewContractInformation(int serveOrderId) {
        List<String> results = new ArrayList();
        ServicePO myorder = em.find(ServicePO.class, serveOrderId);
        
        Contract myCon = myorder.getContract();
        results.add(myCon.getContractId().toString());
        results.add(myCon.getConditionText());
        
        Warehouse myware = em.find(Warehouse.class, myCon.getWarehouseId());
        String warehouseInfo = myware.getStreet() + ", " + myware.getCity() + ", " + myware.getCountry();
        results.add(myware.getName());
        results.add(warehouseInfo);
        results.add(myware.getDescription());
        
        WarehouseZone myzone = em.find(WarehouseZone.class, myCon.getStorageZoneId());
        results.add(myzone.getName());
        results.add(myzone.getDescription());
        
        StorageBin mybin = em.find(StorageBin.class, myCon.getStorageBinId());
        results.add(mybin.getBinName());
        results.add(mybin.getBinType());
        results.add(mybin.getDescription());
           
        return results;
    }

}
