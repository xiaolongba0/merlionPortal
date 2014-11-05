/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.mobilitymanagementmodule;

import entity.Stock;
import entity.StorageBin;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
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
            currentQuantity = countStockInBin(stockTemp, currentQuantity, quantity);
        }
        Integer maxQuantity = bin.getMaxQuantity();
        System.out.println("RETURNED QUANTITY IN BIN: " + currentQuantity);

        if (bin != null && currentQuantity <= maxQuantity) {
            boolean result = amsb.calculateBinSpace(bin, quantity, 0);
            if (result) {
                Stock stock = new Stock();

                stock.setName(stockName);
                stock.setComments(comments);
                stock.setQuantity(quantity);
                stock.setProductId(productId);
                stock.setStorageBin(bin);
                stock.setExpiryDate(expiryDate);
                stock.setAvailableStock(maxQuantity);
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

        System.out.println("[IN EJB AMSB, reserveStock] ======================================");

        if (stock != null) {

            reserveQ = stock.getReservedStock() + reserveQ;
            Integer availableStock = stock.getQuantity() - reserveQ;
            System.out.println("ReserveQ = "+ reserveQ + "  Available Stock = " + availableStock);
            stock.setReservedStock(reserveQ);
            stock.setAvailableStock(availableStock);
            em.merge(stock);
            em.flush();

            return true;

        }
        return false;
    }

    public Integer countStockInBin(List<Stock> stockTemp, Integer currentQuantity, Integer quantity) {

        System.out.println("[INSIDE AMSB EJB, count stock in BIN] ================== BIN IS NOT EMPTY");
        for (Object o : stockTemp) {
            stock = (Stock) o;
            currentQuantity = currentQuantity + stock.getQuantity();
            System.out.println("CURRENT QUANTITY IN BIN: " + currentQuantity);

        }
        currentQuantity = currentQuantity + quantity;
        System.out.println("NEW CURRENT QUANTITY : " + currentQuantity);
        return currentQuantity;
    }

    // consider Company
    public List<Stock> viewStock(Integer companyId, Integer productId) {

        System.out.println("In viewStock, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        String stockId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getWarehouseZone().getWarehouse().getCompanyId() == companyId) {
                allStocks.add(stock);
                System.out.println("Stock: " + stock);
            }
        }
        return allStocks;
    }

    public List<Stock> getWarehouseStock(Integer warehouseId, Integer productId) {

        System.out.println("In getWarehouseStock, Product ID ============================= : " + productId);

        List<Stock> allStocks = new ArrayList<>();
        String stockId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getWarehouseZone().getWarehouse().getWarehouseId() == warehouseId) {
                allStocks.add(stock);
                System.out.println("Stock: " + stock);
            }
        }
        return allStocks;
    }

    public Integer countTotalAvailbleStock(Integer companyId, Integer productId) {
        Stock tempStock = null;
        Integer totalQuantity = 0;

        List<Stock> stocks = new ArrayList<>();
        stocks = viewStock(companyId, productId);

        for (Object o : stocks) {
            tempStock = (Stock) o;
            totalQuantity = totalQuantity + tempStock.getAvailableStock();
            System.out.println("[AMSB] =============== Stock: " + tempStock + "Quantity: " + tempStock.getAvailableStock());
        }

        return totalQuantity;
    }

    public Integer countStockInWarehouse(Integer warehouseId, Integer productId) {
        Stock tempStock = null;
        Integer totalQuantity = 0;

        List<Stock> allStocks = new ArrayList<>();
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stock = (Stock) o;
            if (stock.getStorageBin().getWarehouseZone().getWarehouse().getWarehouseId() == warehouseId) {
                allStocks.add(stock);
                System.out.println("Stock: " + stock);
            }
        }

        for (Object o : allStocks) {
            tempStock = (Stock) o;
            totalQuantity = totalQuantity + tempStock.getQuantity();
            System.out.println("[AMSB] =============== countStockInWarehouse " + tempStock + "Quantity: " + tempStock.getQuantity());
        }

        return totalQuantity;
    }

    public Boolean deleteStock(Integer stockId) {

        Query query = em.createNamedQuery("Stock.findByStockId").setParameter("stockId", stockId);
        Stock stock = (Stock) query.getSingleResult();
        System.out.println("[IN EJB AMSB, deleteStock] ======================================");

        if (stock != null) {
            StorageBin bin = stock.getStorageBin();
            bin.getStockList().remove(stock);
            em.merge(bin);
            em.remove(stock);
            em.flush();

            System.out.println("END OF DELETE STOCK FUNCTION IN SESSION BEAN");
            return true;
        } else {
            return false;
        }
    }

    public boolean editStock(String stockName, String comments, Integer stockId, Date expiryDate) {

        Query query = em.createNamedQuery("Stock.findByStockId").setParameter("stockId", stockId);
        Stock stock = (Stock) query.getSingleResult();

        System.out.println("[IN EJB AMSB, editStock] ======================================");

        if (stock != null) {

            stock.setName(stockName);
            stock.setComments(comments);
            stock.setExpiryDate(expiryDate);
            em.merge(stock);
            em.flush();

            return true;

        }
        return false;

    }

    public boolean addTOStock(String stockName, String comments, Integer quantity, Integer productId,
            Integer storageBinId, Date expiryDate, Date createdDate) {

        // need to check to product ID
        System.out.println("[INSIDE AMSB EJB]================================Add  TO Stock");
        System.out.println("[INSIDE AMSB EJB, Add Stock]===== StorageBinId: " + storageBinId);
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);

        StorageBin bin = (StorageBin) query.getSingleResult();
        System.out.println("StorageBin ================ " + bin);

        List<Stock> stockTemp = new ArrayList();
        stockTemp = bin.getStockList();

        if (bin != null) {
            Stock stock = new Stock();

            stock.setName(stockName);
            stock.setComments(comments);
            stock.setQuantity(quantity);
            stock.setProductId(productId);
            stock.setStorageBin(bin);
            stock.setExpiryDate(expiryDate);
// set created date
            bin.getStockList().add(stock);

            em.merge(bin);
            em.persist(stock);
            em.flush();
            return true;
        }
        return false;
    }

}
