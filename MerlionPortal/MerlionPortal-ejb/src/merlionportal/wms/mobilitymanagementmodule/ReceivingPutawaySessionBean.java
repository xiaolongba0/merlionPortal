/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.mobilitymanagementmodule;

import entity.ServicePO;
import entity.Stock;
import entity.StorageBin;
import entity.Warehouse;
import entity.WarehouseZone;
import entity.WmsOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author YunWei
 */
@Stateless
@LocalBean
public class ReceivingPutawaySessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    private Stock stock;

    @EJB
    private AssetManagementSessionBean amsb;

    // Methods related to stock
    public boolean addStock(String stockName, String comments, Integer quantity, Integer productId,
            Integer storageBinId, Date expiryDate) {

        // need to check to product ID
        System.out.println("[INSIDE  RPSB EJB]================================Add Stock");
        System.out.println("[INSIDE RPB EJB, Add Stock]===== StorageBinId: " + storageBinId);
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);

        StorageBin bin = (StorageBin) query.getSingleResult();
        System.out.println("StorageBin ================ " + bin);

        Integer currentQuantity = 0;
        List<Stock> stockTemp = new ArrayList();
        stockTemp = bin.getStockList();

        // check if bin is empty, or bin already has stocks
        if (!stockTemp.isEmpty()) {
            currentQuantity = countStockInBin(stockTemp, currentQuantity, quantity);
        }
        Integer maxQuantity = bin.getMaxQuantity();
        System.out.println("RETURNED QUANTITY IN BIN: " + currentQuantity);

        if (bin != null && currentQuantity <= maxQuantity) {
            boolean result = amsb.calculateBinSpace(bin, quantity, 0);
            if (result) {
                Stock stock = new Stock();

                Date currentDate = new Date();
                System.out.println("Current Date = " + currentDate);
                stock.setName(stockName);
                stock.setComments(comments);
                stock.setQuantity(quantity);
                stock.setProductId(productId);
                stock.setStorageBin(bin);
                stock.setExpiryDate(expiryDate);
                stock.setAvailableStock(quantity);
                stock.setCreatedDate(currentDate);
                stock.setReservedStock(0);
                bin.getStockList().add(stock);

                em.merge(bin);
                em.persist(stock);
                em.flush();
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    public boolean reserveStock(Integer stockId, Integer reserveQ) {

        Query query = em.createNamedQuery("Stock.findByStockId").setParameter("stockId", stockId);
        Stock stock = (Stock) query.getSingleResult();
        System.out.println("reservedQ = " + reserveQ);
        System.out.println("[IN EJB RPSB, reserveStock] ============================dd==========");

        System.out.println("[IN EJB RPSB, reserveStock] ======================================");

        if (stock != null) {
            int tempQ = reserveQ;
            System.out.println("###############reserved stock" + stock.getReservedStock());
            System.out.println("###############avail stock" + stock.getAvailableStock());
            reserveQ = stock.getReservedStock() + tempQ;
            Integer availableStock = stock.getAvailableStock() - tempQ;
            System.out.println("ReserveQ = " + reserveQ + "  Available Stock = " + availableStock);
            stock.setReservedStock(reserveQ);
            stock.setAvailableStock(availableStock);
            em.flush();
            em.merge(stock);
            em.flush();

            return true;

        }
        return false;
    }

    public Integer countStockInBin(List<Stock> stockTemp, Integer currentQuantity, Integer quantity) {

        System.out.println("[INSIDE EJB, count stock in BIN] ================== BIN IS NOT EMPTY");
        for (Object o : stockTemp) {
            stock = (Stock) o;
            currentQuantity = currentQuantity + stock.getQuantity();
            System.out.println("CURRENT QUANTITY IN BIN: " + currentQuantity);

        }
        currentQuantity = currentQuantity + quantity;
        System.out.println("NEW CURRENT QUANTITY : " + currentQuantity);
        return currentQuantity;
    }

    // view my stock, excluding stock in rented bins
    public List<Stock> viewAllStocks(Integer companyId, Integer productId) {

        System.out.println("In viewStock, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        String stockId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getWarehouseZone().getWarehouse().getCompanyId() == companyId) {
                if (!stock.getStorageBin().getRented()) {
                    allStocks.add(stock);
                    System.out.println("Stock: " + stock);
                }
            }
        }
        return allStocks;
    }

    // View stocks in my rented bin that belongs to 1 company
    public List<Stock> viewStockInRentedBin(Integer ownerCompanyId, Integer rentedCompanyId, Integer productId) {

        System.out.println("In viewStockInRentedBin, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        String stockId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getRentedCompanyId() == rentedCompanyId) {
                if (stock.getStorageBin().getWarehouseZone().getWarehouse().getCompanyId() == ownerCompanyId) {
                    allStocks.add(stock);
                    System.out.println("Stock: " + stock);
                }

            }
        }
        return allStocks;
    }

    // view all my stocks in bins that I've rented from others
    public List<Stock> viewAllStockInRentedBin(Integer rentedCompanyId, Integer productId) {

        System.out.println("In viewStockInRentedBin, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        String stockId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getRentedCompanyId() == rentedCompanyId) {
                allStocks.add(stock);
                System.out.println("Stock: " + stock);

            }
        }
        return allStocks;
    }

    // View other people's stock which are in the bins that they have rented from me
    public List<Stock> viewClientStockInRentedBin(Integer myCompanyId, Integer productId) {
        System.out.println("In viewClientStockInRentedBin, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        String stockId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getWarehouseZone().getWarehouse().getCompanyId() == myCompanyId) {
                if (stock.getStorageBin().getRented()) {
                    allStocks.add(stock);
                    System.out.println("Stock: " + stock);
                }
            }
        }
        return allStocks;
    }

    // View stocks in a warehouse only, excluding those which are rented
    public List<Stock> getWarehouseStock(Integer warehouseId, Integer productId) {

        System.out.println("In getWarehouseStock, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getWarehouseZone().getWarehouse().getWarehouseId() == warehouseId) {
                if (!stock.getStorageBin().getRented()) {
                    allStocks.add(stock);
                    System.out.println("Stock: " + stock);
                }
            }
        }
        return allStocks;
    }

    // Count available stock in 1 of  my warehouse only, exclude rented bins
    public Integer countAvailbleStockInWarehouse(Integer warehouseId, Integer productId) {

        System.out.println("[In RPSB, count available stock in warehouse]");
        Integer totalQuantity = 0;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getWarehouseZone().getWarehouse().getWarehouseId() == warehouseId) {
                if (!stock.getStorageBin().getRented()) {
                    totalQuantity = totalQuantity + stock.getAvailableStock();
                    System.out.println("Stock: " + stock);
                }

            }
        }

        return totalQuantity;
    }

    // Count all my available stocks in my company, exclude rented bins
    public Integer countAvailbleStocksInCompany(Integer companyId, Integer productId) {

        Integer totalQuantity = 0;
        List<Stock> allStocks = new ArrayList<>();
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;

            if (!stock.getStorageBin().getRented()) {
                totalQuantity = totalQuantity + stock.getAvailableStock();
                System.out.println("Total Quantity  " + totalQuantity);

            }
        }

        return totalQuantity;
    }

    // count available stock in all rented bins
    public Integer countAvailbleStockInAllRentedBin(Integer rentedCompanyId, Integer productId) {
        System.out.println("RPSB =============== countAvailbleStockInAllRentedBin");
        Stock tempStock = null;
        Integer totalQuantity = 0;

        List<Stock> stocks = new ArrayList<>();
        stocks = viewAllStockInRentedBin(rentedCompanyId, productId);

        for (Object o : stocks) {
            tempStock = (Stock) o;
            totalQuantity = totalQuantity + tempStock.getAvailableStock();
            System.out.println("[RPSB] =============== Stock: " + tempStock + "Quantity: " + tempStock.getAvailableStock());
        }

        return totalQuantity;
    }

    public Boolean deleteStock(Integer stockId) {

        Query query = em.createNamedQuery("Stock.findByStockId").setParameter("stockId", stockId);
        Stock stock = (Stock) query.getSingleResult();
        System.out.println("[IN EJB RPSB, deleteStock] ======================================");

        if (stock != null) {
            StorageBin bin = stock.getStorageBin();
            bin.getStockList().remove(stock);
            bin.setInuseSpace(bin.getInuseSpace() - stock.getQuantity());
            bin.setAvailableSpace(bin.getAvailableSpace() + stock.getQuantity());
            em.merge(bin);
            em.remove(stock);
            em.flush();

            System.out.println("END OF DELETE STOCK FUNCTION IN SESSION BEAN");
            return true;
        } else {
            return false;
        }
    }

    public boolean editStock(String stockName, String comments, Integer stockId, Date expiryDate, Date createdDate) {

        Query query = em.createNamedQuery("Stock.findByStockId").setParameter("stockId", stockId);
        Stock stock = (Stock) query.getSingleResult();

        System.out.println("[IN EJB RPSB, editStock] ======================================");

        if (stock != null) {

            stock.setName(stockName);
            stock.setComments(comments);
            stock.setExpiryDate(expiryDate);
            stock.setCreatedDate(createdDate);
            em.merge(stock);
            em.flush();

            return true;

        }
        return false;

    }

    public boolean addTOStock(String stockName, String comments, Integer quantity, Integer productId,
            Integer storageBinId, Date expiryDate, Date createdDate) {

        // need to check to product ID
        System.out.println("[INSIDE RPSB EJB]================================Add  TO Stock");
        System.out.println("[INSIDE RPSB EJB, Add Stock]===== StorageBinId: " + storageBinId);
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);

        StorageBin bin = (StorageBin) query.getSingleResult();
        System.out.println("StorageBin ================ " + bin);

        List<Stock> stockTemp = new ArrayList();
        stockTemp = bin.getStockList();

        if (bin != null) {
            Stock stock = new Stock();
            Date currentDate = new Date();

            stock.setName(stockName);
            stock.setComments(comments);
            stock.setQuantity(quantity);
            stock.setProductId(productId);
            stock.setStorageBin(bin);
            stock.setExpiryDate(expiryDate);
            stock.setAvailableStock(quantity);
            stock.setCreatedDate(currentDate);
            stock.setReservedStock(0);
            bin.getStockList().add(stock);

            em.merge(bin);
            em.persist(stock);
            em.flush();
            return true;
        }
        return false;
    }

    // to be EDITED
//    public List<ServicePO> viewOrdersToBeReceived(int companyId) {
//
//        List<ServicePO> searchResult = new ArrayList();
//        Query q = em.createQuery("SELECT s FROM ServicePO s WHERE s.receiverCompanyId = :receiverCompanyId");
//        q.setParameter("receiverCompanyId", companyId);
//
//        for (Object o : q.getResultList()) {
//            ServicePO wOrder = (ServicePO) o;
//            if (wOrder.getStatus() == 6 || wOrder.getStatus() == 11) {
//                searchResult.add(wOrder);
//            }
//        }
//        return searchResult;
//
//    }
    // This method is called is after goods have arrived at warehouse then they are rejected
    public boolean rejectOrder(Integer servicePOId) {

        System.out.println("[IN EJB RPSB, rejectedOrder] ======================================");
        ServicePO servicePO = new ServicePO();
        servicePO = em.find(ServicePO.class, servicePOId);

        if (servicePO != null) {
            // SO Rejected = 12
            servicePO.setStatus(12);
            em.merge(servicePO);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    // To Check Bin Space, also calls the reserveBinSpace method, excluding rented bin
    public boolean checkBinSpaceForAWarehouse(Integer warehouseId, Integer totalQuantityReserved, String neededBinType) {

        System.out.println("[IN EJB RPSB, checkBinSpaceForAWarehouse]===================");
        // add all the bins together
        List<StorageBin> myBins = new ArrayList();
        myBins = amsb.viewAllMyBinsInAWarehouse(warehouseId);
        System.out.println("ALL MY BINS EXCLUDING " + myBins);
        // reserve the bin space
        boolean result = reserveBinSpace(myBins, totalQuantityReserved, neededBinType);

        if (result) {
            return true;
        }
        return false;
    }

    // FOR MANLI
    // my company ID = person who created the bin
    public boolean checkBinSpaceForRentedBins(Integer myCompanyId, Integer rentedCompanyId, Integer totalQuantityReserved, String neededBinType) {

        System.out.println("[IN EJB RPSB, checkBinSpaceForRentedBins]===================");
        // add all the bins together
        List<StorageBin> myBins = new ArrayList();
        myBins = amsb.viewRentedBinsInOneCompany(myCompanyId, rentedCompanyId);
        System.out.println("ALL MY BINS INCLUDING RENTED" + myBins);
        // reserve the bin space
        boolean result = reserveBinSpace(myBins, totalQuantityReserved, neededBinType);

        if (result) {
            return true;
        }
        return false;
    }

    // Reserve bin space method
    public boolean reserveBinSpace(List<StorageBin> allBins, Integer totalQuantityReserved, String neededBinType) {
        System.out.println("[IN EJB RPSB, reserveBinSpace] ======================================");

        Integer tempTotalQuantity = totalQuantityReserved;
        List<Integer> destBinIds = new ArrayList<>();
        List<Integer> destReservedQuantity = new ArrayList<>();

        int i = 0;
        while (i < allBins.size()) {
            // stop if the total quantity is 0
            System.out.println("[IN EJB RPSB, reserveBinSpace] ================================ checking bins");
            if (tempTotalQuantity == 0) {
                break;
            }
            StorageBin destBin = new StorageBin();
            destBin = allBins.get(i);
            // sam check for the bin type
            System.out.println("NEEDED BIN TYPE: " + neededBinType);
            System.out.println("Available BIN TYPE: " + destBin.getBinType());
            System.out.println("Available  BIN ID: " + destBin.getStorageBinId());

            if (neededBinType.equalsIgnoreCase(destBin.getBinType()) & tempTotalQuantity != 0);
            {
                System.out.println("[IN EJB RPSB, reserveBinSpace]================================ bins have the same bin type");
                System.out.println("[IN EJB RPSB, reserveBinSpace]================================ bins available space " + destBin.getStorageBinId());
                // check for space in BIN quantity
                if (destBin.getAvailableSpace() == 0) {
                    i++;
                } else if (tempTotalQuantity <= destBin.getAvailableSpace() & tempTotalQuantity != 0) {
                    Integer reservedQuantity = 0;
                    reservedQuantity = tempTotalQuantity;
                    tempTotalQuantity = 0;
                    System.out.println("[INSIDE EJB RPSB, reserveBinSpace]================================ Reserved Quantity1: " + reservedQuantity);
                    destBinIds.add(destBin.getStorageBinId());
                    destReservedQuantity.add(reservedQuantity);
                    i++;
                } else if ((tempTotalQuantity > destBin.getAvailableSpace()) & tempTotalQuantity != 0) {
                    Integer reservedQuantity = 0;
                    reservedQuantity = destBin.getAvailableSpace();
                    System.out.println("[INSIDE EJB RPSB,reserveBinSpace]================================ Reserved Quantity2: " + reservedQuantity);
                    tempTotalQuantity = tempTotalQuantity - reservedQuantity;
                    destBinIds.add(destBin.getStorageBinId());
                    destReservedQuantity.add(reservedQuantity);
                    i++;
                } else {
                }
            }
        }
        if (tempTotalQuantity != 0) {
            return false;
        }

        // reserve bin space
        int n = 0;
        while (n < destBinIds.size()) {
            StorageBin destBinTemp = new StorageBin();
            System.out.println("Bin ID" + destBinIds.get(n));
            destBinTemp = em.find(StorageBin.class, destBinIds.get(n));

            boolean result = amsb.calculateTOBinSpace(destBinTemp, 0, destReservedQuantity.get(n));
            System.out.println("Bin : " + destBinTemp);
            System.out.println("Reserved Quantity : " + destReservedQuantity.get(n));

            if (!result) {
                return false;
            }
            n++;
        }
        System.out.println("END OF RESERVE BIN SPACE FUNCTION");
        return true;
    }

    //This method is called after goods have arrived at the warehouse, for rented space
    public boolean receivePutawayForRentedBin(Integer myCompanyId, Integer WmsOrderId) {
        System.out.println("[IN EJB RPSB, receivePutawayForRentedSpace] ======================================");
        WmsOrder wmsOrder = new WmsOrder();
        wmsOrder = em.find(WmsOrder.class, WmsOrderId);

        Integer servicePOId = wmsOrder.getServicePOId();
        ServicePO servicePO = new ServicePO();
        servicePO = em.find(ServicePO.class, servicePOId);

        if (wmsOrder != null) {

            // receive stock into the warehouse    
            Integer totalQuantity = wmsOrder.getQuantity();

            Integer rentedCompanyId;
            // find bins which are reserved and unreserve 
            rentedCompanyId = servicePO.getSenderCompanyId();
            List<StorageBin> allBins = new ArrayList<>();

            allBins = amsb.viewRentedBinsInOneCompany(myCompanyId, rentedCompanyId);

            int i = 0;
            while (i < allBins.size() & totalQuantity != 0) {

                StorageBin bin = new StorageBin();
                bin = allBins.get(i);

                if (totalQuantity <= bin.getReservedSpace()) {
                    bin.setReservedSpace(bin.getReservedSpace() - totalQuantity);
                    bin.setAvailableSpace(bin.getAvailableSpace() + totalQuantity);
                    System.out.println(bin.getReservedSpace() - totalQuantity);
                    totalQuantity = 0;
                } //totalQuantity is more than reserved space
                else {
                    totalQuantity = totalQuantity - bin.getReservedSpace();
                    bin.setReservedSpace(0);
                    bin.setAvailableSpace(bin.getMaxQuantity() - bin.getInuseSpace());
                }

                em.merge(bin);
                em.flush();
                i++;
            }

            // to complete, set service PO Fulfilled = 6
            servicePO.setStatus(6);
            em.merge(servicePO);
            em.flush();
            return true;
        }
        return false;
    }

    // Receive and putaway into my warehouse
    // After this method, user will be directed to the add stock page
    public boolean receivePutawayForMyBin(Integer companyId, Integer WmsOrderId) {
        System.out.println("[IN EJB RPSB, receivePutawayForMyBin] ======================================");

        WmsOrder wmsOrder = new WmsOrder();
        wmsOrder = em.find(WmsOrder.class, WmsOrderId);
        Warehouse warehouse = new Warehouse();
        Integer warehouseId = wmsOrder.getWarehouseId();
        Integer totalQuantity = wmsOrder.getQuantity();
        warehouse = em.find(Warehouse.class, warehouseId);

        // find bins which are reserved and unreserve 
        List<StorageBin> myBins = amsb.viewAllMyBinsInAWarehouse(warehouseId);

        int i = 0;
        while (i < myBins.size() & totalQuantity != 0) {

            StorageBin bin = new StorageBin();
            bin = myBins.get(i);

            if (totalQuantity <= bin.getReservedSpace()) {
                System.out.println("totalQuantity <= bin.getReservedSpace()");
                bin.setReservedSpace(bin.getReservedSpace() - totalQuantity);
                bin.setAvailableSpace(bin.getAvailableSpace() + totalQuantity);
                System.out.println(bin.getAvailableSpace() + totalQuantity);
                totalQuantity = 0;
            } //totalQuantity is more than reserved space
            else {
                System.out.println("totalQuantity is more than reserved space");
                totalQuantity = totalQuantity - bin.getReservedSpace();
                System.out.println("Total Quantity = " + totalQuantity);
                bin.setReservedSpace(0);
                bin.setAvailableSpace(bin.getMaxQuantity() - bin.getInuseSpace());
            }
            em.merge(bin);
            em.flush();
            i++;
        }
        return true;
    }

}
