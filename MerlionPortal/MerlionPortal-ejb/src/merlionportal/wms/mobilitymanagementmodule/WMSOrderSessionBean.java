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

    @EJB
    private OrderFulfillmentSessionBean ofsb;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //check for bins and reserve space
    //check for stock and reserve space
    public Integer createInternalOrder(Integer myCompanyId, String orderType, Date fulfillmentDate, Date receiveDate, Integer quantity,
            Integer productId, Boolean internalOrder, Integer servicePOId, Integer warehouseId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================Add create internal order");
        Integer orderId = null;

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

            Boolean result = ofsb.reserveStockInMyBin(myCompanyId, warehouseId, quantity, productId);
            if (result) {
                orderId = createWarehouseOrder(myCompanyId, orderType, fulfillmentDate, receiveDate, quantity,
                        productId, internalOrder, servicePOId, warehouseId);
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

//    // cancel wms order
//    public boolean cancelWMSOrder(Integer companyId, Integer wmsOrderId) {
//        System.out.println("[INSIDE WMS ORDER EJB]================================cancelWMSOrder");
//        WmsOrder wmsOrder = new WmsOrder();
//        wmsOrder = em.find(WmsOrder.class, wmsOrderId);
//        
//        String orderType = wmsOrder.getOrderType();
//        if (orderType.equalsIgnoreCase("Receiving Order")){
//            // unreserve bin space
//            rpsb.receivePutawayForMyBin(companyId, wmsOrderId);
//        }
//        if (orderType.equalsIgnoreCase("Fulfillment Order")){
//            // unreserve stock
//            
//        }
//        return false;
//    }

    // View all orders that are incoming for receiving and putaway
    public List<WmsOrder> viewIncomingOrders(Integer companyId, Integer warehouseId, Boolean internalOrder) {
        System.out.println("[INSIDE WMS ORDER EJB]================================viewIncomingOrders");

        List<WmsOrder> allmyOrders = new ArrayList();
        List<WmsOrder> allOthersOrders = new ArrayList();
        Query query = em.createNamedQuery("WmsOrder.findByMyCompanyId").setParameter("myCompanyId", companyId);

        int i = 0;
        while (i < query.getResultList().size()) {
            WmsOrder wmsOrder = new WmsOrder();
            wmsOrder = (WmsOrder) query.getResultList().get(i);
            if (wmsOrder.getOrderType().equalsIgnoreCase("Receiving Order")) {
                if (wmsOrder.getInternalOrder()) {
                    allmyOrders.add(wmsOrder);
                    return allmyOrders;
                } else {
                    allOthersOrders.add(wmsOrder);
                    return allOthersOrders;
                }

            }
        }
        return null;
    }

    // View all orders that are going out for fulfillment 
    public List<WmsOrder> viewFulfillmentOrders(Integer companyId, Integer warehouseId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================viewFulfillmentOrders");
        List<WmsOrder> allOrders = new ArrayList();
        Query query = em.createNamedQuery("WmsOrder.findByMyCompanyId").setParameter("myCompanyId", companyId);

        int i = 0;
        while (i < query.getResultList().size()) {
            WmsOrder wmsOrder = new WmsOrder();
            wmsOrder = (WmsOrder) query.getResultList().get(i);
            if (wmsOrder.getOrderType().equalsIgnoreCase("Fulfillment Order")) {
                allOrders.add(wmsOrder);
            }
        }
        return allOrders;
    }

    // perform order fulfillment
    public boolean performOrderFulfillment(Integer companyId, Integer wmsOrderId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================performOrderFulfillment");
        WmsOrder wmsOrder = new WmsOrder();
        wmsOrder = em.find(WmsOrder.class, wmsOrderId);

        // check if the order is mine or for rented bins
        if (wmsOrder.getInternalOrder()) {
            Boolean result = ofsb.fulfillOrderFromMyBin(wmsOrderId, companyId);
            if (result) {
                return true;
            } else {
                return false;
            }
        } else {
            Boolean result = ofsb.fulfillOrderFromRentedBin(wmsOrderId, companyId);
            if (result) {
                return true;
            } else {
                return false;
            }
        }
    }

    // perform receiving putaway
    public boolean performReceivingPutaway(Integer companyId, Integer wmsOrderId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================performRecevingPutaway");
        WmsOrder wmsOrder = new WmsOrder();
        wmsOrder = em.find(WmsOrder.class, wmsOrderId);

        // check if the order is mine or for rented bins
        if (wmsOrder.getInternalOrder()) {
            Boolean result = rpsb.receivePutawayForMyBin(companyId, wmsOrderId);
            if (result) {
                return true;
            } else {
                return false;
            }
        } else {
            Boolean result = rpsb.receivePutawayForRentedBin(companyId, wmsOrderId);
            if (result) {
                return true;
            } else {
                return false;
            }
        }
    }

    // fulfillment & receiving and putaway method needed then direct to the respective types, rented or not
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
