/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.warehousemanagementmodule;

import entity.Stock;
import entity.StorageBin;
import entity.Warehouse;
import entity.WarehouseZone;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author YunWei
 */
@Stateless
@LocalBean
public class AssetManagementSessionBean {

    @PersistenceContext
    EntityManager em;
    private Warehouse warehouse;
    private Stock stock;

    // Storage Type is renamed to Warehouse Zone at the front end to minimize confusion
    private ArrayList<WarehouseZone> warehouseZoneList;
    private ArrayList<StorageBin> storageBinList;
    private ArrayList<Stock> stockList;

    // Methods related to warehouse
    public Integer addNewWarehouse(String warehouseName, String country, String city, String street,
            String description, Integer zipcode, Integer companyId) {
        System.out.println("[INSIDE EJB]================================Add New Warehouse");

        warehouse = new Warehouse();

        warehouse.setName(warehouseName);
        warehouse.setCountry(country);
        warehouse.setCity(city);
        warehouse.setStreet(street);
        warehouse.setDescription(description);
        warehouse.setZipcode(zipcode);
        warehouse.setCompanyId(companyId);

        warehouseZoneList = new ArrayList<WarehouseZone>();
        warehouse.setWarehouseZoneList(warehouseZoneList);

        System.out.println("==========Warehouse Name========== :" + warehouseName);
        System.out.println("==========Country========== :" + country);
        System.out.println("==========City========== :" + city);
        System.out.println("==========Street========== :" + street);
        System.out.println("==========Description========== :" + description);
        System.out.println("==========Zip Code========== :" + zipcode);
        System.out.println("=============Company ID============ :" + companyId);

        em.persist(warehouse);
        em.flush();

        System.out.println("[EJB]================================Successfully Added a New Warehouse");

        return warehouse.getWarehouseId();
    }

    public List<Warehouse> viewMyWarehouses(Integer companyId) {
        List<Warehouse> allMyWarehouses = new ArrayList<>();
        System.out.println("In viewMyWarehouses, Company ID ============================= : " + companyId);
        Query query = em.createNamedQuery("Warehouse.findByCompanyId").setParameter("companyId", companyId);

        for (Object o : query.getResultList()) {
            warehouse = (Warehouse) o;
            allMyWarehouses.add(warehouse);
        }
        return allMyWarehouses;
    }

    public Boolean deleteWarehouse(Integer warehouseId) {

        Query query = em.createNamedQuery("Warehouse.findByWarehouseId").setParameter("warehouseId", warehouseId);
        Warehouse warehouse = (Warehouse) query.getSingleResult();

        if (warehouse != null) {
            em.remove(warehouse);
            em.flush();
            return true;

        }
        System.out.println("warehouse is null");
        return false;
    }

    public Integer editWarehouse(String warehouseName, String country, String city, String street,
            String description, Integer zipcode, Integer companyId, Integer warehouseId) {

        System.out.println("[EJB]================================edit Warehouse");
        Query query = em.createNamedQuery("Warehouse.findByCompanyId").setParameter("companyId", companyId);
        List<Warehouse> allMyWarehouses = query.getResultList();

        Warehouse warehse = new Warehouse();
        for (Warehouse w : allMyWarehouses) {
            if (Objects.equals(w.getWarehouseId(), warehouseId)) {
                warehse = w;
            }
        }

        warehse.setName(warehouseName);
        warehse.setCountry(country);
        warehse.setCity(city);
        warehse.setStreet(street);
        warehse.setDescription(description);
        warehse.setZipcode(zipcode);

        em.merge(warehse);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED Warehouse");
        return warehse.getWarehouseId();

    }

    // Methods related to storage type
    public Integer addWarehouseZone(String storageTypeName, String storagetDescription,
            Integer companyId, Integer warehouseId) {

        System.out.println("[INSIDE EJB]================================Add Storage Type");
        Query query = em.createNamedQuery("Warehouse.findByCompanyId").setParameter("companyId", companyId);

        List<Warehouse> allMyWarehouses = query.getResultList();

        Warehouse warehse = new Warehouse();
        for (Warehouse w : allMyWarehouses) {
            if (Objects.equals(w.getWarehouseId(), warehouseId)) {
                warehse = w;
            }
        }
        WarehouseZone warehouseZone = new WarehouseZone();
        warehouseZone.setName(storageTypeName);
        warehouseZone.setDescription(storagetDescription);
        warehouseZone.setWarehouse(warehse);

        storageBinList = new ArrayList<StorageBin>();
        warehouseZone.setStorageBinList(storageBinList);

        System.out.println("Warehouse : " + warehse);
        System.out.println("Warehouse ID ============ : " + warehse.getWarehouseId());
        System.out.println("Storage Type Name :" + storageTypeName);
        System.out.println("Storage Description: " + storagetDescription);

        if (warehse.getWarehouseId() == null) {
            System.out.println("WAREHOUSE ID IS NULL");
            return -1;
        } else {
            em.persist(warehouseZone);
            em.flush();

            warehse.getWarehouseZoneList().add(warehouseZone);
            em.merge(warehse);
            em.flush();
            return warehouseZone.getWarehouseZoneId();
        }

    }

    public List<WarehouseZone> viewWarehouseZoneForAWarehouse(Integer warehouseId) {

        System.out.println("In viewMyStorageTypes, Warehouse ID ============================= : " + warehouseId);
        Warehouse warehouseTemp = null;

        if (warehouseId != null) {
            warehouseTemp = em.find(Warehouse.class, warehouseId);
            System.out.println("In viewMyStorageTypes, finding warehouse" + warehouseTemp);
        }
        return warehouseTemp.getWarehouseZoneList();

    }

    public Boolean deleteWarehouseZone(Integer storageTypeId) {

        WarehouseZone warehouseZone = new WarehouseZone();
        Query query = em.createNamedQuery("WarehouseZone.findByWarehouseZoneId").setParameter("warehouseZoneId", storageTypeId);
        warehouseZone = (WarehouseZone) query.getSingleResult();
        System.out.println("DeleteStorageType ================= : " + warehouseZone);
        if (warehouseZone == null) {
            return false;
        }
        Warehouse warehouse = warehouseZone.getWarehouse();
        warehouse.getWarehouseZoneList().remove(warehouseZone);
        em.merge(warehouse);
        em.remove(warehouseZone);
        em.flush();
        System.out.println("END OF DELETE STORAGE TYPE FUNCTION IN SESSION BEAN");
        return true;
    }

    public Integer editWarehouseZone(String storageTypeName, String description, Integer storageTypeId) {

        System.out.println("[EJB]================================edit storage type");
        WarehouseZone warehouseZone = new WarehouseZone();
        Query query = em.createNamedQuery("WarehouseZone.findByWarehouseZoneId").setParameter("warehouseZoneId", storageTypeId);
        warehouseZone = (WarehouseZone) query.getSingleResult();
        System.out.println("EditStorageType ================= : " + warehouseZone);

        warehouseZone.setName(storageTypeName);
        warehouseZone.setDescription(description);

        em.merge(warehouseZone);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED StorageType");
        return warehouseZone.getWarehouseZoneId();

    }

    // Methods related to storage Bin
    public boolean addStorageBin(String binName, String binType, String description, Integer maxQuantity, Double maxWeight,
            Integer storageTypeId) {

        System.out.println("[INSIDE EJB]================================Add Storage Bin");
        Query query = em.createNamedQuery("WarehouseZone.findByWarehouseZoneId").setParameter("warehouseZoneId", storageTypeId);

        WarehouseZone warehouseZone = (WarehouseZone) query.getSingleResult();
        if (warehouseZone != null) {
            StorageBin bin = new StorageBin();
            bin.setBinName(binName);
            bin.setBinType(binType);
            bin.setDescription(description);
            bin.setMaxQuantity(maxQuantity);
            bin.setMaxWeight(maxWeight);
            bin.setWarehouseZone(warehouseZone);
            warehouseZone.getStorageBinList().add(bin);

            stockList = new ArrayList<Stock>();
            bin.setStockList(stockList);

            em.merge(warehouseZone);
            em.persist(bin);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

    public List<StorageBin> viewStorageBinForWarehouseZone(Integer storageTypeId) {

        System.out.println("In viewStorageBinForType, StorageTypeID ============================= : " + storageTypeId);
        WarehouseZone typeTemp = null;

        if (storageTypeId != null) {
            typeTemp = em.find(WarehouseZone.class, storageTypeId);
            System.out.println("In viewMyStorageTypes, finding warehouse" + typeTemp);
        }
        return typeTemp.getStorageBinList();

    }

    // Check if there are stock in the bin, if there are no stock, then the bin can be deleted
    public boolean deleteStorageBin(Integer storageBinId) {

        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);
        StorageBin bin = (StorageBin) query.getSingleResult();

        System.out.println("DeleteStorageBin ================= : " + bin + "Check if Bin is Empty! " + bin.getStockList().isEmpty());
        if (bin == null || !bin.getStockList().isEmpty()) {
            return false;
        }
        WarehouseZone warehouseZone = bin.getWarehouseZone();
        warehouseZone.getStorageBinList().remove(bin);
        em.merge(warehouseZone);
        em.remove(bin);
        em.flush();

        System.out.println("END OF DELETE STORAGE BIN FUNCTION IN SESSION BEAN");
        
        return true;
    }

    public boolean editStorageBin(String binName, String description, String type, Integer maxQuantity, Double maxWeight, Integer storageBinId) {

        StorageBin bin = new StorageBin();
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);
        bin = (StorageBin) query.getSingleResult();
        if (bin != null) {
            bin.setBinName(binName);
            bin.setBinType(type);
            bin.setDescription(description);
            bin.setMaxQuantity(maxQuantity);
            bin.setMaxWeight(maxWeight);

            em.merge(bin);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

    public List<String> listStorageBinTypes() {
        List<String> allStorageBinTypes = new ArrayList<>();
        System.out.println("In ASSET MANAGEMENT SESSION BEAN ================ LIST STORAGE BIN TYPES");

        allStorageBinTypes.add("Stacking Bins");
        allStorageBinTypes.add("Shelf Bins");
        allStorageBinTypes.add("Containers");
        allStorageBinTypes.add("Styrofoam Containers");
        allStorageBinTypes.add("Others");

        return allStorageBinTypes;
    }

    // Methods related to stock
    public boolean addStock(String stockName, String comments, Integer quantity, Integer productId,
            Integer storageBinId, Date expiryDate) {

        System.out.println("[INSIDE AMSB EJB]================================Add Stock");
        System.out.println("[INSIDE AMSB EJB, Add Stock]===== StorageBinId: " + storageBinId);
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);

        StorageBin bin = (StorageBin) query.getSingleResult();
        System.out.println("StorageBin ================ " + bin);

        Integer currentQuantity = 0;
        List<Stock> stockTemp = new ArrayList();
        stockTemp = bin.getStockList();

        // check if bin is empty, or bin already has stocks
        if (!stockTemp.isEmpty()) {
            System.out.println("[INSIDE AMSB EJB, Add Stock] ================== BIN IS NOT EMPTY");
            for (Object o : stockTemp) {
                stock = (Stock) o;
                currentQuantity = currentQuantity + stock.getQuantity();
                System.out.println("CURRENT QUANTITY IN BIN: " + currentQuantity);
            }
        }
        Integer maxQuantity = bin.getMaxQuantity();
        currentQuantity = currentQuantity + quantity;
        System.out.println("NEW CURRENT QUANTITY : " + currentQuantity);
        System.out.println("EXPIRY DATE: " + expiryDate);

        if (bin != null && currentQuantity <= maxQuantity) {
            Stock stock = new Stock();

            stock.setName(stockName);
            stock.setComments(comments);
            stock.setQuantity(quantity);
            stock.setProductId(productId);
            stock.setStorageBin(bin);
            stock.setExpiryDate(expiryDate);

            bin.getStockList().add(stock);

            em.merge(bin);
            em.persist(stock);
            em.flush();
            return true;
        } else {
            return false;
        }

    }

//    TO BE EDITED AND INTEGRATED WTTH MRP
//    public List<String> listProductId(Integer companyId) {
//        List<String> listProductId = new ArrayList<>();
//        System.out.println("In ASSET MANAGEMENT SESSION BEAN ================ LIST PRODUCT IDs");
//
//        List<Warehouse> allMyWarehouses = new ArrayList<>();
//        allMyWarehouses = viewMyWarehouses(companyId);
//
//        return listProductId;
//    }
    public List<Stock> viewStock(Integer productId) {

        System.out.println("In viewStock, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        String stockId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            allStocks.add(stock);
            System.out.println("Stock: " + stock);
        }
        return allStocks;
    }

    public Integer countTotalStock(Integer productId) {
        Stock tempStock = null;
        Integer totalQuantity = 0;

        List<Stock> stocks = new ArrayList<>();
        stocks = viewStock(productId);

        for (Object o : stocks) {
            tempStock = (Stock) o;
            totalQuantity = totalQuantity + tempStock.getQuantity();
            System.out.println("[AMSB] =============== Stock: " + tempStock + "Quantity: " + tempStock.getQuantity());
        }

        return totalQuantity;
    }

    public Boolean deleteStock(Integer stockId) {

        Query query = em.createNamedQuery("Stock.findByStockId").setParameter("stockId", stockId);
        Stock stock = (Stock) query.getSingleResult();

        System.out.println("[IN EJB AMSB, deleteStock] ======================================");

        StorageBin bin = stock.getStorageBin();
        bin.getStockList().remove(stock);
        em.merge(bin);
        em.remove(stock);
        em.flush();

        System.out.println("END OF DELETE STOCK FUNCTION IN SESSION BEAN");
        return true;
    }

    public boolean editStock(String stockName, String comments, Integer quantity, Integer productId,
            Integer stockId, Date expiryDate) {

        Query query = em.createNamedQuery("Stock.findByStockId").setParameter("stockId", stockId);
        Stock stock = (Stock) query.getSingleResult();

        System.out.println("[IN EJB AMSB, editStock] ======================================");
        System.out.println("Quantity  = " + quantity);

        if (stock != null) {

            // check if max quantity of bin has exceeded
            StorageBin bin = new StorageBin();
            bin = stock.getStorageBin();
            Integer currentQuantity = 0;
            List<Stock> stockTemp = new ArrayList();
            stockTemp = bin.getStockList();

            // check if bin is empty, or bin already has stocks
            if (!stockTemp.isEmpty()) {
                System.out.println("[INSIDE AMSB EJB, EDIT Stock] ================== BIN IS NOT EMPTY");
                for (Object o : stockTemp) {
                    stock = (Stock) o;
                    currentQuantity = currentQuantity + stock.getQuantity();
                    System.out.println("CURRENT QUANTITY IN BIN: " + currentQuantity);
                }
            }
            Integer maxQuantity = bin.getMaxQuantity();
            currentQuantity = currentQuantity + quantity - stock.getQuantity();
            System.out.println("NEW CURRENT QUANTITY : " + currentQuantity);

            if (currentQuantity <= maxQuantity) {
                stock.setName(stockName);
                stock.setComments(comments);
                stock.setProductId(productId);
                stock.setQuantity(quantity);
                stock.setExpiryDate(expiryDate);
                em.merge(stock);
                em.flush();

                return true;

            }
            return false;

        } else {
            return false;
        }

    }
}
