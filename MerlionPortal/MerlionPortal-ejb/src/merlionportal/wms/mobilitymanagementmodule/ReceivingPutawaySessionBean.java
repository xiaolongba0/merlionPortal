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
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
            currentQuantity = countStockInBin(stockTemp, currentQuantity, quantity);
        }
        Integer maxQuantity = bin.getMaxQuantity();
        System.out.println("RETURNED QUANTITY IN BIN: " + currentQuantity);
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
            currentQuantity = countStockInBin(stockTemp, currentQuantity, quantity);
        }
            Integer maxQuantity = bin.getMaxQuantity();
            currentQuantity = currentQuantity + quantity - stock.getQuantity();
            System.out.println("Returned CURRENT QUANTITY : " + currentQuantity);

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
