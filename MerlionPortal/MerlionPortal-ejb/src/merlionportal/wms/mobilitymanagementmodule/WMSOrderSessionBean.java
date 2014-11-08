/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.mobilitymanagementmodule;

import entity.Stock;
import entity.StorageBin;
import entity.WmsOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
public class WMSOrderSessionBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    private ReceivingPutawaySessionBean rpsb;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //check for bins and reserve space
    //check for stock and reserve space
    public Integer createInternalOrder(Integer myCompanyId, String orderType, Date fulfillmentDate, Date receiveDate, Integer quantity,
            Integer productId, Boolean internalOrder, Integer servicePOId, Integer warehouseId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================Add create internal order");
        Integer orderId;

        orderId = createWarehouseOrder(myCompanyId, orderType, fulfillmentDate, receiveDate, quantity,
                productId, internalOrder, servicePOId, warehouseId);

        if (orderType.equalsIgnoreCase("Receiving Order")) {
            System.out.println("====================== Receiving Order");

            String neededBinType = getBinType(productId);
            Boolean result = rpsb.checkBinSpaceForAWarehouse(warehouseId, quantity, neededBinType);
            if (result) {
                orderId = createWarehouseOrder(myCompanyId, orderType, fulfillmentDate, receiveDate, quantity,
                        productId, internalOrder, servicePOId, warehouseId);
            } else {
                orderId = null;
            }
        }

        if (orderType.equalsIgnoreCase("Fulfillment Order")) {
            System.out.println("====================== Fulfillment Order");

            Integer availableStock = rpsb.countAvailbleStockInWarehouse(warehouseId, productId);

            // WMS has enoughs stock from this warehouse to fulfill
            if (availableStock >= quantity) {
                List<Stock> allStocks = new ArrayList<>();
                // get stocks that belong to that warehouse only
                allStocks = rpsb.getWarehouseStock(warehouseId, productId);
                Stock stock = allStocks.get(0);

                System.out.println("[UNSORTED] AllStocks = " + allStocks);
                // sort by created date
                if (stock.getExpiryDate() == null) {
                    Collections.sort(allStocks, new Comparator<Stock>() {
                        @Override
                        public int compare(Stock o1, Stock o2) {
                            return o1.getCreatedDate().compareTo(o2.getCreatedDate());
                        }
                    });
                } // sort the list of stocks according to expiry date
                else {
                    Collections.sort(allStocks, new Comparator<Stock>() {
                        @Override
                        public int compare(Stock o1, Stock o2) {
                            return o1.getExpiryDate().compareTo(o2.getExpiryDate());
                        }
                    });
                }

                System.out.println("[SORTED] AllStocks = " + allStocks);

                // get list of source bins
                Integer newTotalQ = quantity;
                Boolean result = null;

                Integer count = 0;
                while (count < allStocks.size() & newTotalQ != 0 & result != false) {

                    Stock tempStock = new Stock();
                    tempStock = allStocks.get(count);
                    if (newTotalQ >= tempStock.getQuantity()) {
                        newTotalQ = newTotalQ - tempStock.getQuantity();
                        // reserve the stock
                        result = rpsb.reserveStock(tempStock.getStockId(), tempStock.getQuantity());
                        System.out.println(count + " SourceBinId1: " + tempStock.getStorageBin().getStorageBinId());
                    } else {
                        result = rpsb.reserveStock(tempStock.getStockId(), newTotalQ);
                        newTotalQ = 0;
                        // reserve the stock
                        System.out.println(count + " SourceBinId2: " + tempStock.getStorageBin().getStorageBinId());
                    }
                    count++;
                }
            } else {
                orderId = null;
            }
        }

        return orderId;

    }

    public String getBinType(Integer productId) {
        Query query = em.createQuery("SELECT s FROM Stock s WHERE s.productId = :inProductId");
        query.setParameter("inProductId", productId);
        List<Stock> stocks = query.getResultList();
        return stocks.get(0).getStorageBin().getBinType();
    }
    
    public List<WmsOrder> viewIncomingOrders (Integer companyId, Integer warehouseId){
       
        List<WmsOrder> allOrders = new ArrayList();
        Query query = em.createNamedQuery("WmsOrder.findByMyCompanyId").setParameter("myCompanyId", companyId);
        
        int i = 0;
        while (i < allOrders.size()){
            WmsOrder wmsOrder = new WmsOrder();
            wmsOrder = allOrders.get(i);
            
            
        }              
        return allOrders;
    }

    public boolean processReceivedOrder(Integer wmsOrderId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================processReceivedOrder");
        WmsOrder wmsOrder = new WmsOrder();
        wmsOrder = em.find(WmsOrder.class, wmsOrderId);
        
        // check if order is mine or for rented bins
        return true;
    }

    // GENERAL create method
    public Integer createWarehouseOrder(Integer myCompanyId, String orderType, Date fulfillmentDate, Date receiveDate, Integer quantity,
            Integer productId, Boolean internalOrder, Integer servicePOId, Integer warehouseId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================Add Warehouse Order");

        if (myCompanyId != null) {
            WmsOrder order = new WmsOrder();

            order.setMyCompanyId(myCompanyId);
            order.setOrderType(orderType);
            order.setFulfillmentDate(fulfillmentDate);
            order.setReceiveDate(receiveDate);
            order.setQuantity(quantity);
            order.setProductId(productId);
            order.setServicePOId(servicePOId);
            order.setInternalOrder(internalOrder);
            order.setWarehouseId(warehouseId);

            em.persist(order);
            em.flush();

            return order.getOrderId();
        } else {
            return null;
        }
    }

}
