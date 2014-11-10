/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.mobilitymanagementmodule;

import entity.ServicePO;
import entity.Stock;
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
    
//    Status
//    1. Processing
//    2. Rejected
//    3. Compeleted
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
    // View all Own internal orders that are incoming for receiving and putaway
    public List<WmsOrder> viewMyIncomingOrders(Integer companyId, Integer warehouseId) {
        System.out.println("[INSIDE WMS ORDER EJB]================================viewIncomingOrders");

        List<WmsOrder> allmyOrders = new ArrayList();
        Query query = em.createNamedQuery("WmsOrder.findByMyCompanyId").setParameter("myCompanyId", companyId);

        int i = 0;
        while (i < query.getResultList().size()) {
            WmsOrder wmsOrder = new WmsOrder();
            wmsOrder = (WmsOrder) query.getResultList().get(i);
            if (wmsOrder.getOrderType().equalsIgnoreCase("Receiving Order") && (int) wmsOrder.getWarehouseId() == warehouseId) {
                if (wmsOrder.getInternalOrder()) {
                    allmyOrders.add(wmsOrder);
                }
            }
            i++;

        }
        return allmyOrders;
    }

    // View all Own internal orders that are incoming for receiving and putaway
    public List<WmsOrder> viewOthersIncomingOrders(Integer companyId, Integer warehouseId) {
        System.out.println("[INSIDE WMS ORDER EJB]================================viewIncomingOrders");
        
        List<WmsOrder> allmyOrders = new ArrayList();
        Query query = em.createNamedQuery("WmsOrder.findByMyCompanyId").setParameter("myCompanyId", companyId);
        System.out.println("Query: " + query.getResultList().size());

        int i = 0;
        while (i < query.getResultList().size()) {
            WmsOrder wmsOrder = (WmsOrder) query.getResultList().get(i);
            if (wmsOrder.getOrderType().equalsIgnoreCase("Receiving Order") && (int) wmsOrder.getWarehouseId() == warehouseId) {
                if (!wmsOrder.getInternalOrder()) {
                    allmyOrders.add(wmsOrder);
                }
            }
            i++;
        }
        System.out.println("Number: " + allmyOrders.size());
        return allmyOrders;
    }

    // View all orders that are going out for fulfillment 
    public List<WmsOrder> viewMyFulfillmentOrders(Integer companyId, Integer warehouseId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================viewFulfillmentOrders");
        List<WmsOrder> allOrders = new ArrayList();
        Query query = em.createNamedQuery("WmsOrder.findByMyCompanyId").setParameter("myCompanyId", companyId);

        int i = 0;
        while (i < query.getResultList().size()) {
            WmsOrder wmsOrder = new WmsOrder();
            wmsOrder = (WmsOrder) query.getResultList().get(i);
            if (wmsOrder.getOrderType().equalsIgnoreCase("Fulfillment Order") && (int) wmsOrder.getWarehouseId() == warehouseId) {
                if (wmsOrder.getInternalOrder()) {
                    allOrders.add(wmsOrder);
                }
            }
            i++;
        }
        return allOrders;
    }

    public List<WmsOrder> viewOthersFulfillmentOrders(Integer companyId, Integer warehouseId) {

        System.out.println("[INSIDE WMS ORDER EJB]================================viewFulfillmentOrders");
        List<WmsOrder> allOrders = new ArrayList();
        Query query = em.createNamedQuery("WmsOrder.findByMyCompanyId").setParameter("myCompanyId", companyId);

        int i = 0;
        while (i < query.getResultList().size()) {
            WmsOrder wmsOrder = new WmsOrder();
            wmsOrder = (WmsOrder) query.getResultList().get(i);
            if (wmsOrder.getOrderType().equalsIgnoreCase("Fulfillment Order") && (int) wmsOrder.getWarehouseId() == warehouseId) {
                if (!wmsOrder.getInternalOrder()) {
                    allOrders.add(wmsOrder);
                }
            }
            i++;
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

    public boolean convertExternalOrderToInternalWOrder(Integer poId, Integer companyId) {
        WmsOrder wmsOrder = new WmsOrder();
        ServicePO po = em.find(ServicePO.class, poId);
        if (po != null) {
            wmsOrder.setMyCompanyId(companyId);
            wmsOrder.setOrderType(po.getWarehousePOtype());
            wmsOrder.setFulfillmentDate(po.getServiceFulfilmentDate());
            wmsOrder.setReceiveDate(po.getServiceReceiveDate());
            wmsOrder.setQuantity(po.getAmountOfProduct().intValue());
            wmsOrder.setProductId(po.getProductId());
            wmsOrder.setServicePOId(po.getServicePOId());
            wmsOrder.setServicePOId(po.getServicePOId());
            wmsOrder.setInternalOrder(false);
            wmsOrder.setWarehouseId(po.getWarehouseId());
            wmsOrder.setStatus("Processing");

            em.persist(wmsOrder);
            
            po.setStatus(12);
            em.merge(po);
            em.flush();

            return true;
        }
        return false;
    }

}
