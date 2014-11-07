/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.mobilitymanagementmodule;

import entity.MovingStock;
import entity.ServicePO;
import entity.Stock;
import entity.StorageBin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class OrderFulfillmentSessionBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    private ReceivingPutawaySessionBean rpsb;

    @EJB
    private AssetManagementSessionBean amsb;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    // FOR MANLI
    public boolean reserveStockRentedBins(Integer companyId, Integer quantityNeeded, Integer productId) {

        System.out.println("[IN OFSB] =================== checkStockMyBins");
        Integer availableQuantity = countTotalAvailbleStockInRentedBin(companyId, productId);

        if (availableQuantity >= quantityNeeded) {
            // reserve the stock
            List<Stock> allStocks = new ArrayList<>();
            allStocks = rpsb.viewStockInRentedBin(companyId, productId);

            Integer newQ = quantityNeeded;
            Integer count = 0;
            while (count < allStocks.size() & newQ != 0) {
                Stock tempStock = new Stock();
                tempStock = allStocks.get(count);

                if (newQ >= tempStock.getQuantity()) {
                    newQ = newQ - tempStock.getQuantity();
                    rpsb.reserveStock(tempStock.getStockId(), tempStock.getQuantity());
                } else {
                    newQ = 0;
                    rpsb.reserveStock(tempStock.getStockId(), tempStock.getQuantity());
                }
                count++;
            }
            return true;
        } else {
            return false;

        }
    }

    // FOR MANLI
    public Integer countTotalAvailbleStockInRentedBin(Integer rentedCompanyId, Integer productId) {
        Stock tempStock = null;
        Integer totalQuantity = 0;

        List<Stock> stocks = new ArrayList<>();
        stocks = rpsb.viewStockInRentedBin(rentedCompanyId, productId);

        for (Object o : stocks) {
            tempStock = (Stock) o;
            totalQuantity = totalQuantity + tempStock.getAvailableStock();
            System.out.println("[OFSB] =============== Stock: " + tempStock + "Quantity: " + tempStock.getAvailableStock());
        }

        return totalQuantity;
    }

    
    // check for total stock in rented bins + bins that I own
    // haven't written
    
    
    // to be edited
    // When internal order = false 
    public boolean sendOrderFromRentedBin(Integer servicePOId) {
        System.out.println("In sendOrderFromRentedBin, ServicePO ============================= : " + servicePOId);
        ServicePO servicePO = new ServicePO();
        servicePO = em.find(ServicePO.class, servicePOId);

        if (servicePO != null) {

            // receive stock into the warehouse        
            Integer outgointQuantity = servicePO.getProductQuantityPerTEU() * servicePO.getVolume();

            // find bins which belongs to the rented company
            Integer rentedCompanyId = servicePO.getSenderCompanyId();
            List<StorageBin> allBins = new ArrayList<>();
            //allBins = amsb.viewRentedBins(rentedCompanyId);

            // go through all the relevant storage bins to retrieve the stock
            int i = 0;
            while (i < allBins.size()) {

                StorageBin bin = new StorageBin();
                bin = allBins.get(i);
                System.out.println("BIN = " + bin);
                List<Stock> stockList = new ArrayList();
                stockList = bin.getStockList();

                Stock tempStock = new Stock();
                tempStock = stockList.get(0);
                // sort by created date
                if (tempStock.getExpiryDate() == null) {
                    Collections.sort(stockList, new Comparator<Stock>() {
                        @Override
                        public int compare(Stock o1, Stock o2) {
                            return o1.getCreatedDate().compareTo(o2.getCreatedDate());
                        }
                    });
                } // sort the list of stocks according to expiry date
                else {
                    Collections.sort(stockList, new Comparator<Stock>() {
                        @Override
                        public int compare(Stock o1, Stock o2) {
                            return o1.getExpiryDate().compareTo(o2.getExpiryDate());
                        }
                    });
                }

                int j = 0;
                while (j < stockList.size() & outgointQuantity != 0) {
                    Stock stock = new Stock();
                    stock = stockList.get(j);
                    Integer stockQuantity = stock.getQuantity();
                    
                    Integer newStockQuantity = 0;
                    System.out.println("Stock Quantity = " + stockQuantity);
                    System.out.println("Incoming Quantity = " + outgointQuantity);

                    if (stockQuantity > outgointQuantity) {
                        newStockQuantity = stockQuantity - outgointQuantity;
                        System.out.println("NEW STOCK QUANTITY = " + newStockQuantity);

                        // update the relevant stock related attributes
                        stock.setQuantity(newStockQuantity);
                        Integer reservedStock = newStockQuantity - stock.getAvailableStock();
                        stock.setReservedStock(reservedStock);
                        System.out.println("NEW RESERVED STOCK = " + reservedStock);
                        outgointQuantity = 0;
                        em.merge(stock);
                        em.flush();

                    } else if (stockQuantity <= outgointQuantity) {
                        System.out.println("Delete stock");
                        outgointQuantity = outgointQuantity - stockQuantity;
                        rpsb.deleteStock(stock.getStockId());
                    }
                    j++;
                }
                i++;
            }

            // service PO order fulfilled
            servicePO.setStatus(6);
            em.merge(servicePO);
            em.flush();
            return true;
        }
        return false;
    }

}
