/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.warehousemanagementmodule;

import merlionportal.wms.mobilitymanagementmodule.ReceivingPutawaySessionBean;
import entity.Stock;
import entity.StockAudit;
import entity.StorageBin;
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
public class StockAuditSessionBean {

    @PersistenceContext
    EntityManager em;
    
    @EJB
    private ReceivingPutawaySessionBean rpsb;
    
    private Stock stock;
    private StockAudit stockAuditTemp;
    private StockAudit stockAudit;

    public boolean addStockAudit(Integer storageBinId, Integer supervisorId, Integer staffId, Date scheduledDate, Date actualDate,
            Integer stockAuditStatus, Integer stockAuditType, Integer expectedQuantity, Integer passQuantity, Integer failQuantity, Integer actualQuantity, String remarks) {

        // stockAuditType:
        // 1: Random Sampling (In this case, expected quantity and actual quantity is the same, usually used to check quality only)
        // 2: Count All
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
        // expected quantity will be counted on the day stock audit is being carried out
        actualDate = null;

        // check if storage bin is empty
        List<Stock> allStocks = new ArrayList<>();
        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", storageBinId);

        System.out.println("[INSIDE SASB EJB, addStockAudit] ========== query: " + query.getResultList());
        if (query.getResultList().isEmpty()) {
            return false;
        } else {

            stockAuditTemp = new StockAudit();

            stockAuditTemp.setStorageBinId(storageBinId);
            stockAuditTemp.setSupervisorId(supervisorId);
            stockAuditTemp.setStaffId(staffId);

            stockAuditTemp.setStockAuditStatus(stockAuditStatus);
            stockAuditTemp.setStockAuditType(stockAuditType);

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

    public List<StockAudit> viewStockAudits(Integer productId) {

        System.out.println("In viewStock, Product ID ============================= : " + productId);

        List<StockAudit> allStockAudit = new ArrayList<>();
        String stockAuditId = null;
        Query query = em.createNamedQuery("Stock.findByProductId").setParameter("productId", productId);

        for (Object o : query.getResultList()) {
            stockAudit = (StockAudit) o;
            allStockAudit.add(stockAudit);
            System.out.println("Stock Audit: " + stockAudit);
        }
        return allStockAudit;
    }

}
