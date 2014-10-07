/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.warehousemanagementmodule;

import entity.Company;
import entity.Stock;
import entity.StorageBin;
import entity.StorageType;
import entity.Warehouse;
import java.util.ArrayList;
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
    private ArrayList<StorageType> storageTypeList;

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

        storageTypeList = new ArrayList<StorageType>();
        warehouse.setStorageTypeList(storageTypeList);

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
    public Integer addStorageType(String storageTypeName, String storagetDescription,
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
        StorageType storageType = new StorageType();
        storageType.setName(storageTypeName);
        storageType.setDescription(storagetDescription);
        storageType.setWarehousewarehouseId(warehse);

        storageBinList = new ArrayList<StorageBin>();
        storageType.setStorageBinList(storageBinList);

        System.out.println("Warehouse : " + warehse);
        System.out.println("Warehouse ID ============ : " + warehse.getWarehouseId());
        System.out.println("Storage Type Name :" + storageTypeName);
        System.out.println("Storage Description: " + storagetDescription);

        if (warehse.getWarehouseId() == null) {
            System.out.println("WAREHOUSE ID IS NULL");
            return -1;
        } else {
            em.persist(storageType);
            em.flush();

            warehse.getStorageTypeList().add(storageType);
            em.merge(warehse);
            em.flush();
            return storageType.getStorageTypeId();
        }

    }

    public List<StorageType> viewStorageTypesForAWarehouse(Integer warehouseId) {

        System.out.println("In viewMyStorageTypes, Warehouse ID ============================= : " + warehouseId);
        Warehouse warehouseTemp = null;

        if (warehouseId != null) {
            warehouseTemp = em.find(Warehouse.class, warehouseId);
            System.out.println("In viewMyStorageTypes, finding warehouse" + warehouseTemp);
        }
        return warehouseTemp.getStorageTypeList();

    }

    public Boolean deleteStorageType(Integer storageTypeId) {

        StorageType storageType = new StorageType();
        Query query = em.createNamedQuery("StorageType.findByStorageTypeId").setParameter("storageTypeId", storageTypeId);
        storageType = (StorageType) query.getSingleResult();
        System.out.println("DeleteStorageType ================= : " + storageType);
        if (storageType == null) {
            return false;
        }
        Warehouse warehouse = storageType.getWarehousewarehouseId();
        warehouse.getStorageTypeList().remove(storageType);
        em.merge(warehouse);
        em.remove(storageType);
        em.flush();
        System.out.println("END OF DELETE STORAGE TYPE FUNCTION IN SESSION BEAN");
        return true;
    }

    public Integer editStorageType(String storageTypeName, String description, Integer storageTypeId) {

        System.out.println("[EJB]================================edit storage type");
        StorageType storageType = new StorageType();
        Query query = em.createNamedQuery("StorageType.findByStorageTypeId").setParameter("storageTypeId", storageTypeId);
        storageType = (StorageType) query.getSingleResult();
        System.out.println("EditStorageType ================= : " + storageType);

        storageType.setName(storageTypeName);
        storageType.setDescription(description);

        em.merge(storageType);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED StorageType");
        return storageType.getStorageTypeId();

    }

    // Methods related to storage Bin
    public boolean addStorageBin(String binName, String binType, String description, Integer maxQuantity, Double maxWeight,
            Integer storageTypeId) {

        System.out.println("[INSIDE EJB]================================Add Storage Bin");
        Query query = em.createNamedQuery("StorageType.findByStorageTypeId").setParameter("storageTypeId", storageTypeId);

        StorageType storageType = (StorageType) query.getSingleResult();
        if (storageType != null) {
            StorageBin bin = new StorageBin();
            bin.setBinName(binName);
            bin.setBinType(binType);
            bin.setDescription(description);
            bin.setMaxQuantity(maxQuantity);
            bin.setMaxWeight(maxWeight);
            bin.setStorageTypestorageTypeId(storageType);
            storageType.getStorageBinList().add(bin);

            stockList = new ArrayList<Stock>();
            bin.setStockList(stockList);

            em.merge(storageType);
            em.persist(bin);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

    public List<StorageBin> viewStorageBinForStorageType(Integer storageTypeId) {

        System.out.println("In viewStorageBinForType, StorageTypeID ============================= : " + storageTypeId);
        StorageType typeTemp = null;

        if (storageTypeId != null) {
            typeTemp = em.find(StorageType.class, storageTypeId);
            System.out.println("In viewMyStorageTypes, finding warehouse" + typeTemp);
        }
        return typeTemp.getStorageBinList();

    }

    // Check if there are stock in the bin, if there are no stock, then the bin can be deleted
    public Boolean deleteStorageBin(Integer storageBinId) {

        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);
        StorageBin bin = (StorageBin) query.getSingleResult();

        System.out.println("DeleteStorageBin ================= : " + bin + "Check if Bin is Empty! " + bin.getStockList().isEmpty());
        if (bin == null || !bin.getStockList().isEmpty()) {
            return false;
        }
        StorageType storageType = bin.getStorageTypestorageTypeId();
        storageType.getStorageBinList().remove(bin);
        em.merge(storageType);
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
            Integer storageBinId) {

        System.out.println("[INSIDE EJB]================================Add Stock");
        System.out.println("[INSIDE EJB, Add Stock]===== StorageBinId: " + storageBinId);
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);

        StorageBin bin = (StorageBin) query.getSingleResult();
        System.out.println("StorageBin ================ " + bin);

        if (bin != null) {
            Stock stock = new Stock();

            stock.setName(stockName);
            stock.setComments(comments);
            stock.setQuantity(quantity);
            stock.setProductId(productId);
            stock.setStorageBin(bin);

            bin.getStockList().add(stock);

            em.merge(bin);
            em.persist(stock);
            em.flush();
            return true;
        } else {
            return false;
        }

    }

    // TO BE EDITED AND INTEGRATED WTTH MRP
    public List<String> listProductId(Integer companyId) {
        List<String> listProductId = new ArrayList<>();
        System.out.println("In ASSET MANAGEMENT SESSION BEAN ================ LIST PRODUCT IDs");

        List<Warehouse> allMyWarehouses = new ArrayList<>();
        allMyWarehouses = viewMyWarehouses(companyId);

        return listProductId;
    }

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
    
    public Integer countTotalStock(Integer productId){
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

}
