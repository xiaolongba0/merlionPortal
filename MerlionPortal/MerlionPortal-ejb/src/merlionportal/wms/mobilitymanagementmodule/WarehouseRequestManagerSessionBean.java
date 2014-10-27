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

        results.add(myCon.getDestination());

        results.add(myCon.getStorageType());

        return results;
    }

    public Boolean checkStorageBinAvailability(int quantity, int volume, int productId, String storageType, StorageBin myStorage) {

        if (myStorage.getBinType().equals(storageType)) {
            if (myStorage.getMaxQuantity() == myStorage.getAvailableSpace()) {
                return true;
            } else {
                if (myStorage.getStockList().get(0).getProductId() == productId && myStorage.getAvailableSpace() >= volume * quantity) {
                    return true;
                }
            }

        }
        return false;
    }

    public Map<String, Map<String, Map<String, Integer>>> selectionSetUp(ServicePO servicePo, int compId) {
        int volume = servicePo.getVolume();
        int quantity = servicePo.getProductQuantityPerTEU();
        int productId = servicePo.getProductId();
        String storageType = servicePo.getContract().getStorageType();

        Map<String, Map<String, Map<String, Integer>>> data = new HashMap<String, Map<String, Map<String, Integer>>>();
        Map<String, Map<String, Integer>> data2 = new HashMap<String, Map<String, Integer>>();
        Map<String, Integer> warehouses = new HashMap<String, Integer>();
        Map<String, Integer> warehouseZones = new HashMap<String, Integer>();
        Map<String, Integer> storageBins = new HashMap<String, Integer>();

        Query q = em.createQuery("SELECT w FROM Warehouse w WHERE w.companyId = :companyId");
        q.setParameter("companyId", compId);
        for (Object o : q.getResultList()) {
            Warehouse w = (Warehouse) o;
            List<WarehouseZone> zones = w.getWarehouseZoneList();
            Map<String, Integer> map2 = new HashMap<String, Integer>();
            for (Object p : zones) {
                WarehouseZone myZone = (WarehouseZone) p;
                List<StorageBin> binList = myZone.getStorageBinList();
                Map<String, Integer> map1 = new HashMap<String, Integer>();
                for (Object c : binList) {
                    StorageBin myBin = (StorageBin) c;
//                    if (this.checkStorageBinAvailability(quantity, volume, productId, storageType, myBin)) {
//                        map1.put(myBin.getBinName(), myBin.getStorageBinId());
//                    }
                }
                data2.put(myZone.getName(), map1);
            }
            data.put(w.getName(), data2);
        }
        return data;
    }
    
    public void reserveSpace(int storageBinId,ServicePO mypo){
        StorageBin myBin = em.find(StorageBin.class, storageBinId);
        int reSpace=mypo.getVolume()*mypo.getProductQuantityPerTEU();
        myBin.setReservedSpace(reSpace);
        int avail = myBin.getMaxQuantity()-reSpace;
        myBin.setAvailableSpace(avail);
        Stock newStock = new Stock();
        newStock.setProductId(mypo.getProductId());
        newStock.setStorageBin(myBin);
        mypo.setStatus(10);
        em.persist(newStock);
        em.merge(myBin);
        
    }

}
