/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.warehousemanagementmodule;

import entity.Stock;
import entity.StockAudit;
import entity.StorageBin;
import entity.Warehouse;
import entity.WarehouseZone;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class StockAuditSessionBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    private ReceivingPutawaySessionBean rpsb;

    @EJB
    private AssetManagementSessionBean amsb;

    private StockAudit stockAuditTemp;
    private StockAudit stockAudit;

    public boolean addStockAudit(Integer storageBinId, Integer staffId, Date scheduledDate, Date actualDate,
            Integer stockAuditStatus, Integer expectedQuantity, Integer passQuantity, Integer failQuantity, Integer actualQuantity, String remarks) {

        // stockAuditStatus:
        // 0: Not done
        // 1: Done, completed
        // 2: ON HOLD
        System.out.println("[INSIDE SASB EJB]================================Add Stock Audit");

        // pass and fail quantity reflects quality of the stock
        passQuantity = 0;
        failQuantity = 0;
        actualQuantity = 0;
        stockAuditStatus = 0;
        expectedQuantity = 0;
        remarks = "-";
        // expected quantity will be counted on the day stock audit is being carried out
        actualDate = null;

        // check if storage bin is empty
        List<Stock> allStocks = new ArrayList<>();
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);
        StorageBin bin = new StorageBin();
        bin = em.find(StorageBin.class, storageBinId);
        allStocks = bin.getStockList();

        System.out.println("[INSIDE SASB EJB, addStockAudit] ========== query: " + query.getResultList());
        if (allStocks.isEmpty()) {
            return false;
        } else {

            stockAuditTemp = new StockAudit();

            stockAuditTemp.setStorageBinId(storageBinId);
            stockAuditTemp.setStaffId(staffId);

            stockAuditTemp.setStockAuditStatus(stockAuditStatus);

            stockAuditTemp.setCreatedDate(scheduledDate);
            stockAuditTemp.setActualDate(actualDate);

            stockAuditTemp.setExpectedQuantity(expectedQuantity);
            stockAuditTemp.setFailQuantity(failQuantity);
            stockAuditTemp.setPassQuantity(passQuantity);
            stockAuditTemp.setActualQuantity(actualQuantity);
            stockAuditTemp.setRemarks(remarks);

            em.persist(stockAuditTemp);
            em.flush();
            return true;
        }

    }

    public List<StockAudit> viewStockAuditsForAWarehouse(Integer warehouseId) {

        List<StockAudit> allMyStockAudits = new ArrayList<>();
        System.out.println("In viewStockAudits=============================");

        List<WarehouseZone> allWarehouseZones = new ArrayList<>();
        allWarehouseZones = amsb.viewWarehouseZoneForAWarehouse(warehouseId);

        List<StorageBin> allBins = new ArrayList<>();
        int j = 0;
        while (j < allWarehouseZones.size()) {
            allBins.addAll(allWarehouseZones.get(j).getStorageBinList());
            j++;
        }

        int i = 0;
        while (i < allBins.size()) {
            StorageBin bin = new StorageBin();
            bin = allBins.get(i);
            Integer storageBinId = bin.getStorageBinId();
//            System.out.println(i + " In viewStockAudits, ALL BINS =============================" + storageBinId);
            Query query = em.createNamedQuery("StockAudit.findByStorageBinId").setParameter("storageBinId", storageBinId);

            for (Object o : query.getResultList()) {
                stockAudit = (StockAudit) o;
                allMyStockAudits.add(stockAudit);
            }
            i++;
        }
        return allMyStockAudits;
    }

    public Boolean deleteStockAudit(Integer stockAuditId) {

        Query query = em.createNamedQuery("StockAudit.findByStockAuditId").setParameter("stockAuditId", stockAuditId);
        StockAudit stockAudit = (StockAudit) query.getSingleResult();
        System.out.println("[IN EJB SASB, deleteStockAudit] ======================================");

        if (stockAudit != null) {
            em.remove(stockAudit);
            em.flush();

            System.out.println("END OF DELETE STOCK AUDIT FUNCTION IN SESSION BEAN");
            return true;
        } else {
            return false;
        }
    }

    public boolean editStockAudit(Integer stockAuditId, Integer staffId, Date scheduledDate,
            Integer stockAuditStatus, String remarks) {

        Query query = em.createNamedQuery("StockAudit.findByStockAuditId").setParameter("stockAuditId", stockAuditId);
        StockAudit stockAudit = (StockAudit) query.getSingleResult();

        System.out.println("[IN EJB SASB, editStockAudit] ======================================" + stockAuditId);

        if (stockAudit != null) {

            System.out.println("[IN EJB SASB, editStockAudit] ======================================");
            stockAudit.setStaffId(staffId);
            stockAudit.setStockAuditStatus(stockAuditStatus);

            stockAudit.setCreatedDate(scheduledDate);
            stockAudit.setRemarks(remarks);
            em.merge(stockAudit);
            em.flush();
            return true;

        } else {
            return false;
        }

    }

    public List<StockAudit> viewDueStockAuditsForAWarehouse(Integer warehouseId) {

        List<StockAudit> allMyStockAudits = new ArrayList<>();
        System.out.println("In viewDUEStockAudits=============================");

        List<WarehouseZone> allWarehouseZones = new ArrayList<>();
        allWarehouseZones = amsb.viewWarehouseZoneForAWarehouse(warehouseId);

        List<StorageBin> allBins = new ArrayList<>();
        int j = 0;
        while (j < allWarehouseZones.size()) {
            allBins.addAll(allWarehouseZones.get(j).getStorageBinList());
            j++;
        }

        int i = 0;
        while (i < allBins.size()) {
            StorageBin bin = new StorageBin();
            bin = allBins.get(i);
            Integer storageBinId = bin.getStorageBinId();
            System.out.println(i + " In viewStockAudits, ALL BINS =============================" + storageBinId);
            Query query = em.createNamedQuery("StockAudit.findByStorageBinId").setParameter("storageBinId", storageBinId);

            for (Object o : query.getResultList()) {
                stockAudit = (StockAudit) o;
                if (compareDate(stockAudit.getCreatedDate())) {
                    allMyStockAudits.add(stockAudit);
                }
            }
            i++;
        }
        return allMyStockAudits;
    }

    public boolean carryOutStockAudit(Integer stockAuditId, Integer stockAuditStatus, Integer passQuantity, Integer failQuantity, Integer actualQuantity, String remarks) {

        Query query = em.createNamedQuery("StockAudit.findByStockAuditId").setParameter("stockAuditId", stockAuditId);
        StockAudit stockAudit = (StockAudit) query.getSingleResult();

        System.out.println("[IN EJB SASB, carryOutStockAudit] Stock Audit Status======================================" + stockAuditStatus);

        if (stockAudit != null) {
            
            Date actualDate = new Date();
            System.out.println("[IN EJB SASB, carryOutStockAudit] ======================================"+ actualDate);

            //Stock Audit is completed
            
            stockAudit.setFailQuantity(failQuantity);
            stockAudit.setPassQuantity(passQuantity);
            stockAudit.setActualQuantity(actualQuantity);
            stockAudit.setActualDate(actualDate);
            stockAudit.setStockAuditStatus(stockAuditStatus);
            
            em.merge(stockAudit);
            em.flush();
            return true;

        } else {
            return false;
        }

    }

    public Boolean compareDate(Date scheduledDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();

        System.out.println(sdf.format(todayDate));
        System.out.println(sdf.format(scheduledDate));
        System.out.println("Compare Dates===================" + todayDate.compareTo(scheduledDate));

        if (todayDate.compareTo(scheduledDate) == 1) {
            return true;
        }
        return false;
    }

    public StockAudit getStockAudit(int stockAuditId) {
        StockAudit audit = em.find(StockAudit.class, stockAuditId);

        StorageBin bin = em.find(StorageBin.class, audit.getStorageBinId());
        List<Stock> stockTemp = new ArrayList();
        stockTemp = bin.getStockList();

        Integer expectedQuantity;
        if (!stockTemp.isEmpty()) {
            expectedQuantity = rpsb.countStockInBin(stockTemp, 0, 0);
            System.out.println("[IN EJB SASB, getStockAudit] ======================================: " + expectedQuantity);

            audit.setExpectedQuantity(expectedQuantity);
            em.merge(audit);
            em.flush();

        }

        return audit;
    }
}
