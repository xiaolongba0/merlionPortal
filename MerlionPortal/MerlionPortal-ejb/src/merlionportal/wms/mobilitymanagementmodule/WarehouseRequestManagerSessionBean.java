/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.mobilitymanagementmodule;

import entity.Company;
import entity.Contract;
import entity.ServicePO;
import entity.Stock;
import entity.StorageBin;
import entity.Warehouse;
import entity.WarehouseZone;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            if (wOrder.getStatus() == 5||wOrder.getStatus() == 6||wOrder.getStatus() == 7) {
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

        results.add(myCon.getDestination());

        results.add(myCon.getStorageType());

        return results;
    }

    public Boolean checkStorageBinAvailability(ServicePO myPO, StorageBin myStorage) {
        int totalQuantity = myPO.getVolume() * myPO.getProductQuantityPerTEU();
        int productId = myPO.getProductId();
        String storageType = myPO.getContract().getStorageType();
        if (myStorage.getBinType().equals(storageType)) {
            if (myStorage.getMaxQuantity() == myStorage.getAvailableSpace()) {
                return true;
            } else {
                if (myStorage.getStockList().get(0).getProductId() == productId && myStorage.getAvailableSpace() >= totalQuantity) {
                    return true;
                }
            }

        }
        return false;
    }

    public void reserveSpace(StorageBin myBin, ServicePO mypo,int reserveSpace) {       
        myBin.setReservedSpace(reserveSpace);
        int avail = myBin.getMaxQuantity() - reserveSpace;
        myBin.setAvailableSpace(avail);
        Stock newStock = new Stock();
        newStock.setProductId(mypo.getProductId());
        newStock.setStorageBin(myBin);
        mypo.setStatus(10);
        em.persist(newStock);
        em.merge(myBin);

    }

}
