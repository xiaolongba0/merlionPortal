/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.tms.transportationassetmaintenancemanagementmodule;

import javax.ejb.Stateless;
import entity.Company;
import entity.Location;
import entity.TransportationAsset;
import entity.AssetSchedule;
import entity.MaintenanceLog;
import entity.OperatorSchedule;
import entity.TransportationOperator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * /**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class TransportationAssetMaintenanceManagementSessionBean {

    @PersistenceContext
    EntityManager em;

    private Integer locationId;
    private ArrayList<TransportationAsset> transportationAssetList;

    //-------------Asset
    private TransportationAsset asset;
    private Integer tAssetId;
    //------------Schedule

    public List<MaintenanceLog> viewMyMaintenanceLog(Integer tAssetId) {
        List<MaintenanceLog> allMyMaintenanceLog = new ArrayList<>();
        System.out.println("In viewMyMaintenanceLog, TransportationAsset ID ============================= : " + tAssetId);
        System.out.println("Get This Transportation Asset===================== : " + tAssetId);
        TransportationAsset tAsset = new TransportationAsset();
        Query query = em.createNamedQuery("TransportationAsset.findByAssetId").setParameter("assetId", tAssetId);
        tAsset = (TransportationAsset) query.getSingleResult();

        allMyMaintenanceLog = tAsset.getMaintenanceLogList();

        return allMyMaintenanceLog;
    }

    public Boolean deleteMaintenanceLog(Integer logId) {

        Query query = em.createNamedQuery("MaintenanceLog.findByMaintenanceLogId").setParameter("logId", logId);
        List<MaintenanceLog> allMyMaintenanceLog = query.getResultList();

        for (MaintenanceLog m : allMyMaintenanceLog) {
            if (Objects.equals(m.getOperatorId(), logId)) {
                em.remove(m);
                em.flush();
                return true;
            }
        }
        return false;
    }

    public Integer addNewMaintenanceLog(String cost, String description, Integer operatorId, Date startDate, Date endDate, Integer tAssetId) {
        System.out.println("[INSIDE EJB]================================Add New Operator");

        Query query = em.createNamedQuery("TransportationAsset.findByAssetId").setParameter("assetId", tAssetId);

        TransportationAsset tAsset = (TransportationAsset) query.getSingleResult();
        if (tAsset != null) {
            MaintenanceLog maintain = new MaintenanceLog();
            maintain.setCost(cost);
            maintain.setDescription(description);
            maintain.setOperatorId(operatorId);
            maintain.setTransporationAssetassetId(tAsset);

            AssetSchedule maintainSchedule = new AssetSchedule();

            maintainSchedule.setStartDate(startDate);
            maintainSchedule.setEndDate(endDate);
            maintainSchedule.setTransporationAssetassetId(tAsset);

            System.out.println("==========Maintenance Cost========== :" + cost);
            System.out.println("==========Maintenance Description=== :" + description);
            System.out.println("==========Maintenance Operator====== :" + maintain.getMaintenanceLogId());

            em.persist(maintain);
            em.persist(maintainSchedule);
            em.flush();

            System.out.println("[EJB]================================Successfully Added a New Location");
            return maintain.getMaintenanceLogId();
        } else {
            return -1;
        }
    }

    public Integer editMaintenanceLog(String cost, String description, Integer logId) {

        System.out.println("[EJB]================================Edit Maintenance Log");
        Query query = em.createNamedQuery("MaintenanceLog.findByTransportationAssetId").setParameter("tAssetId", tAssetId);
        List<MaintenanceLog> allMyMaintenanceLog = query.getResultList();

        MaintenanceLog maintainn = new MaintenanceLog();
        for (MaintenanceLog m : allMyMaintenanceLog) {
            if (Objects.equals(m.getMaintenanceLogId(), logId)) {
                maintainn = m;
            }
        }

        maintainn.setCost(cost);
        maintainn.setDescription(description);

        em.merge(maintainn);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED location");
        return maintainn.getMaintenanceLogId();

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
