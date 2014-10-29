/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.warehousemanagementmodule;

import entity.MovingStock;
import entity.Product;
import entity.Stock;
import entity.StockAudit;
import entity.StorageBin;
import entity.TransportOrder;
import entity.Warehouse;
import entity.WarehouseZone;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import merlionportal.wms.mobilitymanagementmodule.ReceivingPutawaySessionBean;

/**
 *
 * @author YunWei
 */
@Stateless
@LocalBean
public class TransportOrderSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    @EJB
    private AssetManagementSessionBean amsb;

    @EJB
    private ReceivingPutawaySessionBean rpsb;

//    private TransportOrder transportOrder;
    private List<MovingStock> movingStockList;
//    private MovingStock movingStock;

    public boolean addTransportOrder(Date transportDate, Integer staffId, Integer productId,
            Integer totalQuantity, Integer sourceWarehouseId, Integer destWarehouseId) {

        System.out.println("[INSIDE TOSB EJB]================================Add Transport Order");

        // check total number of stocks in the warehouse
        Integer eQuantity = 0;

        eQuantity = rpsb.countStockInWarehouse(sourceWarehouseId, productId);
        System.out.println("eQuantity = " + eQuantity);
        // 0 for to be processed, 1 for cancelled, 2 for sent, 3 for received and completed
        Integer orderStatus = 0;

        if (eQuantity >= totalQuantity & totalQuantity != 0) {
            // check for space in destination warehouse
            // check storage type
            // handle the case with more than 1 source bin T_T
            Warehouse sourceWarehouse = em.find(Warehouse.class, sourceWarehouseId);
            Warehouse destWarehouse = em.find(Warehouse.class, destWarehouseId);
            // retrive bin storage type
            Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);
            Stock stock = (Stock) query.getResultList().get(0);
            String sourceBinType = stock.getStorageBin().getBinType();

            // get list of source bins
            List<Integer> sourceBinIds = new ArrayList<>();
            List<Integer> sourceQuantity = new ArrayList<>();
            List<Stock> allStocks = new ArrayList<>();
            Integer newTotalQ = totalQuantity;
            Integer tempTotalQuantity = totalQuantity;

            Integer count = 0;
            while (count < query.getResultList().size() & newTotalQ != 0) {
                Stock tempStock = new Stock();
                tempStock = (Stock) query.getResultList().get(count);
                if (tempStock.getStorageBin().getWarehouseZone().getWarehouse().getWarehouseId() == sourceWarehouseId) {
                    if (newTotalQ >= stock.getQuantity()) {
                        newTotalQ = newTotalQ - stock.getQuantity();
                        allStocks.add(stock);
                        sourceBinIds.add(tempStock.getStorageBin().getStorageBinId());
                        sourceQuantity.add(stock.getQuantity());
                        System.out.println(count + " SourceBinId1: " + tempStock.getStorageBin().getStorageBinId());
                    } else {
                        allStocks.add(stock);
                        sourceQuantity.add(newTotalQ);
                        newTotalQ = 0;
                        sourceBinIds.add(tempStock.getStorageBin().getStorageBinId());
                        System.out.println(count + " SourceBinId2: " + tempStock.getStorageBin().getStorageBinId());
                    }

                }
                count++;
            }
            System.out.println("Exited while loop!");

            List<WarehouseZone> destWarehouseZones;
            destWarehouseZones = destWarehouse.getWarehouseZoneList();

            List<Integer> destBinIds = new ArrayList<>();
            List<Integer> destReservedQuantity = new ArrayList<>();
            List<StorageBin> allBins = new ArrayList<>();
            int j = 0;
            while (j < destWarehouseZones.size()) {
                allBins.addAll(destWarehouseZones.get(j).getStorageBinList());
                j++;
            }
            System.out.println("Warehouse Destination Bins " + allBins);
            if (allBins.isEmpty()) {
                return false;
            }

            int i = 0;
            while (i < allBins.size()) {
                // stop if the total quantity is 0
                System.out.println("[INSIDE TOSB EJB, add TO]================================ checking bins");
                if (tempTotalQuantity == 0) {
                    break;
                }
                StorageBin destBin = new StorageBin();
                destBin = allBins.get(i);
                // same bin type
                System.out.println("SOURCE BIN TYPE: " + sourceBinType);
                System.out.println("Destination BIN TYPE: " + destBin.getBinType());
                if (sourceBinType.equalsIgnoreCase(destBin.getBinType()) & tempTotalQuantity != 0);
                {
                    System.out.println("[INSIDE TOSB EJB, add TO]================================ bins have the same bin type");
                    System.out.println("[INSIDE TOSB EJB, add TO]================================ bins available space" + destBin.getAvailableSpace());
                    // check for space in BIN quantity
                    if (destBin.getAvailableSpace() == 0) {
                        i++;

                    } else if (tempTotalQuantity < destBin.getAvailableSpace() & tempTotalQuantity != 0) {
                        Integer reservedQuantity = 0;
                        reservedQuantity = tempTotalQuantity;
                        tempTotalQuantity = 0;
                        System.out.println("[INSIDE TOSB EJB]================================Add Transport Order, Reserved Quantity1: " + reservedQuantity);
                        destBinIds.add(destBin.getStorageBinId());
                        destReservedQuantity.add(reservedQuantity);
                        i++;
                    } else if ((tempTotalQuantity >= destBin.getAvailableSpace()) & tempTotalQuantity != 0) {
                        Integer reservedQuantity = 0;
                        reservedQuantity = tempTotalQuantity - destBin.getAvailableSpace();
                        System.out.println("[INSIDE TOSB EJB]================================Add Transport Order, Reserved Quantity2: " + reservedQuantity);
                        tempTotalQuantity = tempTotalQuantity - reservedQuantity;
                        destBinIds.add(destBin.getStorageBinId());
                        destReservedQuantity.add(reservedQuantity);
                        i++;
                    } else {
                        return false;
                    }
                }

            }

            // create transport order
            TransportOrder transportOrder = new TransportOrder();
            transportOrder.setTransportDate(transportDate);
            transportOrder.setStaffId(staffId);
            transportOrder.setStatus(orderStatus);
            transportOrder.setProductId(productId);
            transportOrder.setTotalQuantity(totalQuantity);
            transportOrder.setSourceWarehouseId(sourceWarehouseId);
            transportOrder.setDestWarehouseId(destWarehouseId);

            System.out.println("TransportDate: " + transportDate + staffId + orderStatus + productId + totalQuantity + sourceWarehouseId + destWarehouseId);

            movingStockList = new ArrayList<MovingStock>();
            transportOrder.setMovingStockList(movingStockList);

            em.persist(transportOrder);
            em.flush();
            System.out.println("Persisted Transport Order");

            // generate moving stock list
            movingStockList = generateMovingStock(sourceBinIds, sourceQuantity, destBinIds, destReservedQuantity, transportOrder);
            transportOrder.setMovingStockList(movingStockList);
            em.merge(transportOrder);
            em.flush();

            // set reserve bin space
            int n = 0;
            while (n < destBinIds.size()) {
                StorageBin destBinTemp = new StorageBin();
                System.out.println("Destination Bin ID" + destBinIds.get(n));
                destBinTemp = em.find(StorageBin.class, destBinIds.get(n));

                boolean result = amsb.calculateTOBinSpace(destBinTemp, 0, destReservedQuantity.get(n));
                System.out.println("Destination bin : " + destBinTemp);
                System.out.println("Reserved Quantity : " + destReservedQuantity.get(n));

                if (!result) {
                    return false;
                }
                n++;
            }
            System.out.println("END OF CREATE TRANSPORT ORDER FUNCTION");
            return true;
        } else {
            return false;
        }

    }

    public List<MovingStock> generateMovingStock(List<Integer> sourceStorageBinId, List<Integer> sourceQuantity, List<Integer> destStorageBinId, List<Integer> destQuantity, TransportOrder transportOrder) {
        List<MovingStock> tempStockList = new ArrayList();

        System.out.println("INSIDE GENERATE MOVING STOCK FUNCTION");
        int i = 0;
        while (i < sourceStorageBinId.size()) {
            System.out.println("I = " + i);
            MovingStock movingStock = new MovingStock();
            movingStock.setDestStorageBinId(null);
            movingStock.setMovingQuantity(sourceQuantity.get(i));
            movingStock.setSourceStorageBinId(sourceStorageBinId.get(i));
            movingStock.setTransportOrder(transportOrder);
            tempStockList.add(movingStock);

            em.persist(movingStock);
            em.flush();
            i++;
        }

        int j = 0;
        while (j < destStorageBinId.size()) {
            System.out.println("J = " + j);
            MovingStock movingStock2 = new MovingStock();
            movingStock2.setDestStorageBinId(destStorageBinId.get(j));
            movingStock2.setMovingQuantity(destQuantity.get(j));
            movingStock2.setSourceStorageBinId(null);
            movingStock2.setTransportOrder(transportOrder);
            tempStockList.add(movingStock2);

            em.persist(movingStock2);
            em.flush();
            j++;
        }

        int k = 0;
        while (k < tempStockList.size()) {
            System.out.println("TEMP MOVING STOCK LIST" + tempStockList);
            k++;
        }

        return tempStockList;
    }

    public List<TransportOrder> viewAllTransportOrdersForSource(Integer sourceWarehouseId) {
        System.out.println("In viewALLTransportOrderForSOURCE, Warehouse ID ============================= : " + sourceWarehouseId);
        List<TransportOrder> allOrders = new ArrayList();

        TransportOrder transportOrder = new TransportOrder();
        Query query = em.createNamedQuery("TransportOrder.findBySourceWarehouseId").setParameter("sourceWarehouseId", sourceWarehouseId);

        for (Object o : query.getResultList()) {
            transportOrder = (TransportOrder) o;
            allOrders.add(transportOrder);
        }

        return allOrders;
    }

    public List<TransportOrder> viewAllTransportOrdersForDest(Integer destWarehouseId) {
        System.out.println("In viewALLTransportOrderForDEST, Warehouse ID ============================= : " + destWarehouseId);
        List<TransportOrder> allOrders = new ArrayList();

        TransportOrder transportOrder = new TransportOrder();
        Query query = em.createNamedQuery("TransportOrder.findByDestWarehouseId").setParameter("destWarehouseId", destWarehouseId);

        for (Object o : query.getResultList()) {
            transportOrder = (TransportOrder) o;
            allOrders.add(transportOrder);
        }

        return allOrders;
    }

    public TransportOrder viewATransportOrder(Integer transportOrderId) {
        System.out.println("In viewATransportOrder, TransportOrder ============================= : " + transportOrderId);
        TransportOrder transportOrder = new TransportOrder();
        transportOrder = em.find(TransportOrder.class, transportOrderId);

        return transportOrder;
    }

    public List<MovingStock> viewSourceMovingStock(Integer transportOrderId) {
        TransportOrder transportOrder = new TransportOrder();
        transportOrder = em.find(TransportOrder.class, transportOrderId);
        System.out.println("[INSIDE TOSB EJB]================================View Source Moving Stock");

        List<MovingStock> allStocks = new ArrayList();
        List<MovingStock> allSourceStocks = new ArrayList();
        allStocks = transportOrder.getMovingStockList();

        int i = 0;
        while (i < allStocks.size()) {
            if (allStocks.get(i).getDestStorageBinId() == null) {
                allSourceStocks.add(allStocks.get(i));
            }
            i++;
        }
        return allSourceStocks;
    }

    public List<MovingStock> viewDestMovingStock(Integer transportOrderId) {
        TransportOrder transportOrder = new TransportOrder();
        transportOrder = em.find(TransportOrder.class, transportOrderId);
        System.out.println("[INSIDE TOSB EJB]================================View Dest Moving Stock");
        List<MovingStock> allStocks = new ArrayList();
        List<MovingStock> allDestStocks = new ArrayList();
        allStocks = transportOrder.getMovingStockList();

        int i = 0;
        while (i < allStocks.size()) {
            if (allStocks.get(i).getSourceStorageBinId() == null) {
                allDestStocks.add(allStocks.get(i));
            }
            i++;
        }
        return allDestStocks;
    }

    public boolean cancelTransportOrder(Integer transportOrderId) {
        System.out.println("In cancelTransportOrder, TransportOrder ============================= : " + transportOrderId);
        TransportOrder transportOrder = new TransportOrder();
        transportOrder = em.find(TransportOrder.class, transportOrderId);

        if (transportOrder != null) {

            // undo reservation            
            List<MovingStock> allDestStocks = new ArrayList();
            allDestStocks = viewDestMovingStock(transportOrderId);
            Integer destBinId;
            Integer availableQuantity;
            int i = 0;
            while (i < allDestStocks.size()) {
                destBinId = allDestStocks.get(i).getDestStorageBinId();
                StorageBin bin = new StorageBin();
                bin = em.find(StorageBin.class, destBinId);
                System.out.println("Bin available quantity: " + bin.getAvailableSpace());
                System.out.println("Bin moving quantity: " + allDestStocks.get(i).getMovingQuantity());
                availableQuantity = bin.getAvailableSpace() + allDestStocks.get(i).getMovingQuantity();
                bin.setReservedSpace(0);
                System.out.println("NEW BIN AVAILABLE QUANTITY" + availableQuantity);
                bin.setAvailableSpace(availableQuantity);
                em.merge(bin);
                em.flush();
                i++;
            }

            // to cancel, status = 1
            transportOrder.setStatus(1);
            em.merge(transportOrder);
            em.flush();
            return true;
        }
        return false;
    }

    public boolean receiveTransportOrder(Integer transportOrderId) {
        System.out.println("In receiveTransportOrder, TransportOrder ============================= : " + transportOrderId);
        TransportOrder transportOrder = new TransportOrder();
        transportOrder = em.find(TransportOrder.class, transportOrderId);

        // get product name
        String stockName = "";
        Product product = new Product();
        product = em.find(Product.class, transportOrder.getProductId());
        stockName = product.getProductName();

        if (transportOrder != null) {

            // receive stock into the warehouse        
            List<MovingStock> allDestStocks = new ArrayList();
            allDestStocks = viewDestMovingStock(transportOrderId);

            Integer destBinId;
            Integer availableQuantity;
            int i = 0;
            while (i < allDestStocks.size()) {
                destBinId = allDestStocks.get(i).getDestStorageBinId();
                StorageBin bin = new StorageBin();
                bin = em.find(StorageBin.class, destBinId);
                bin.setReservedSpace(bin.getReservedSpace() - allDestStocks.get(i).getMovingQuantity());
                System.out.println(bin.getReservedSpace() - allDestStocks.get(i).getMovingQuantity());

                // add stocks
                rpsb.addTOStock(stockName, "Transported Stock", allDestStocks.get(i).getMovingQuantity(), transportOrder.getProductId(), destBinId, null);
                em.merge(bin);
                em.flush();
                i++;
            }

            // to complete, status = 3
            transportOrder.setStatus(3);
            em.merge(transportOrder);
            em.flush();
            return true;
        }
        return false;
    }

    // might need to edit later, to integrate with receiving and putaway submodule
    public boolean sendTransportOrder(Integer transportOrderId) {
        System.out.println("In sendTransportOrder, TransportOrder ============================= : " + transportOrderId);
        TransportOrder transportOrder = new TransportOrder();
        transportOrder = em.find(TransportOrder.class, transportOrderId);

        if (transportOrder != null) {

            //process the order
            // update stock quantity in storagebin
            List<MovingStock> allSourceStocks = new ArrayList();
            allSourceStocks = viewSourceMovingStock(transportOrderId);
            Integer transportQuantity = transportOrder.getTotalQuantity();

            Integer sourceBinId;
            int i = 0;
            while (i < allSourceStocks.size()) {
                sourceBinId = allSourceStocks.get(i).getSourceStorageBinId();
                StorageBin bin = new StorageBin();
                bin = em.find(StorageBin.class, sourceBinId);
                List<Stock> stockList = new ArrayList();
                stockList = bin.getStockList();

                int j = 0;
                while (j < stockList.size() & transportQuantity != 0) {
                    Stock stock = new Stock();
                    stock = stockList.get(j);
                    Integer stockQuantity = stock.getQuantity();
                    Integer newStockQuantity = 0;
                    System.out.println("Stock Quantity = " + stockQuantity);
                    System.out.println("Transport Quantity = " + transportQuantity);

                    if (stockQuantity > transportQuantity) {
                        newStockQuantity = stockQuantity - transportQuantity;
                        System.out.println("NEW STOCK QUANTITY = " + newStockQuantity);
                        stock.setQuantity(newStockQuantity);
                        transportQuantity = 0;
                        em.merge(stock);
                        em.flush();

                    } else if (stockQuantity <= transportQuantity) {
                        System.out.println("Delete stock");
                        transportQuantity = transportQuantity - stockQuantity;
                        rpsb.deleteStock(stock.getStockId());
                    }
                    j++;
                }
                i++;
            }

            // to send, status = 2
            transportOrder.setStatus(2);
            em.merge(transportOrder);
            em.flush();
            return true;
        }

        return false;
    }

    // check the status of the transport order, if the status is completed for cancelled, the transport order can be deleted
    // function not completed
//    public boolean deleteTransportOrder(Integer transportOrderId) {
//
//        System.out.println("[INSIDE TOSB EJB]================================Delete");
//        TransportOrder transportOrder = em.find(TransportOrder.class, transportOrderId);
//
//        if (transportOrder == null) {
//            return false;
//        } else if (transportOrder.getStatus() == 1 || transportOrder.getStatus() == 2) {
//            em.remove(transportOrder.getMovingStockList());
//            em.remove(transportOrder);
//            em.flush();
//        }
//
////        WarehouseZone warehouseZone = bin.getWarehouseZone();
////        warehouseZone.getStorageBinList().remove(bin);
////        em.merge(warehouseZone);
////        em.remove(bin);
////        em.flush();
////
////        System.out.println("END OF DELETE STORAGE BIN FUNCTION IN SESSION BEAN");
//        return true;
//    }
}
