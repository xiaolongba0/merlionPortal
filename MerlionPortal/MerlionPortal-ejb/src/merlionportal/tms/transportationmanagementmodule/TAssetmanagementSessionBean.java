/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.tms.transportationmanagementmodule;

import javax.ejb.Stateless;
import entity.Company;
import entity.Location;
import entity.TransportationAsset;
import entity.AssetSchedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class TAssetmanagementSessionBean{

    @PersistenceContext
    EntityManager em;
    
    //-------Location
    private Location location;
    private Integer locationId;
    private ArrayList<TransportationAsset> transportationAssetList;

    
    //-------------Asset
    private TransportationAsset asset;
    private Integer assetId;
    
    //------------Schedule
    private ArrayList<AssetSchedule> scheduleList;
    

    public List<Location> viewMyLocations(Integer companyId) {
        List<Location> allMyLocation = new ArrayList<>();
        System.out.println("In viewMyLocation, Company ID ============================= : " + companyId);
        Query query = em.createNamedQuery("Location.findByCompanyId").setParameter("companyId", companyId);
        for (Object o : query.getResultList()) {
            location = (Location) o;
            allMyLocation.add(location);
        }

        return allMyLocation;
    }

    public Boolean deleteLocation(Integer companyId, Integer locationId) {

        Query query = em.createNamedQuery("Location.findByCompanyId").setParameter("companyId", companyId);
        List<Location> allMyLocation = query.getResultList();

        for (Location l : allMyLocation) {
            if (Objects.equals(l.getLocationId(), locationId)) {
                em.remove(l);
                em.flush();
                return true;
            }
        }
        return false;
    }
       
    public Integer addNewLocation(String locationName, String locationType, Integer companyId) {
        System.out.println("[INSIDE EJB]================================Add New Warehouse");

        location = new Location();

        location.setLocationName(locationName);
        location.setLocationType(locationType);
        location.setCompanyId(companyId);

        transportationAssetList = new ArrayList<TransportationAsset>();
        location.setTransportationAssetList(transportationAssetList);

        System.out.println("==========Location Name========== :" + locationName);
        System.out.println("==========Location Type========== :" + locationType);
        System.out.println("=============Company ID============ :" + companyId);

        em.persist(location);
        em.flush();

        System.out.println("[EJB]================================Successfully Added a New Location");

        return location.getLocationId();
    }
    
    public Integer editLocation(String locationName, String locationType, Integer companyId, Integer locationId) {

        System.out.println("[EJB]================================edit Location");
        Query query = em.createNamedQuery("Location.findByCompanyId").setParameter("companyId", companyId);
        List<Location> allMyLocations = query.getResultList();

        Location locationn = new Location();
        for (Location l : allMyLocations) {
            if (Objects.equals(l.getLocationId(), locationId)) {
                locationn = l;
            }
        }

        locationn.setLocationName(locationName);
        locationn.setLocationType(locationType);

        em.merge(locationn);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED location");
        return locationn.getLocationId();

    }

    public Integer addTAsset(String assetType, Integer capacity, Integer locationlocationId, Integer price, Integer speed, Integer companyId, String status) {

        System.out.println("[INSIDE EJB]================================Add Storage Type");
        Query query = em.createNamedQuery("Warehouse.findByCompanyId").setParameter("companyId", companyId);

        List<Location> allMyLocations = query.getResultList();
        Integer i ;
        
        Location locationn = new Location();
        for (Location l : allMyLocations) {
            if (Objects.equals(l.getLocationId(), locationlocationId)) {
                locationn = l;
            }
        }
        
        TransportationAsset tAsset = new TransportationAsset();
        tAsset.setAssetType(assetType);
        tAsset.setCapacity(capacity);
        tAsset.setLocationlocationId(locationn);
        tAsset.setPrice(price);
        tAsset.setSpeed(speed);
        tAsset.setIsAvailable(Boolean.TRUE);
        tAsset.setIsMaintain(Boolean.FALSE);
        tAsset.setStatus(status);

        scheduleList = new ArrayList<AssetSchedule>();
        tAsset.setAssetScheduleList(scheduleList);
        
//        storageBinList = new ArrayList<StorageBin>();
//        storageType.setStorageBinList(storageBinList);

        System.out.println("Location : " + locationn);
        System.out.println("Location ID ============ : " + locationn.getLocationId());
        System.out.println("TAsset type :" + assetType);
        System.out.println("TAsset speed:" + speed);
        System.out.println("TAsset status: " + status);
            
        ///check later 
        if (locationn.getLocationId() == null) {
            System.out.println("WAREHOUSE ID IS NULL");
            return -1;
        } else {
            em.persist(tAsset);
            em.flush();

            location.getTransportationAssetList().add(tAsset);
            em.merge(location);
            em.flush();
            return tAsset.getAssetId();
        }
        
    }

    public List<TransportationAsset> viewtAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        Location locationTemp = null;

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }
        return locationTemp.getTransportationAssetList();

    }

    public Boolean deletetAsset(Integer assetId) {

        TransportationAsset tAsset = new TransportationAsset();
        Query query = em.createNamedQuery("TransportationAsset.findbyAssetId").setParameter("tAssetId", assetId);
        tAsset = (TransportationAsset) query.getSingleResult();
        System.out.println("DeleteStorageType ================= : " + tAsset);
        if (tAsset == null) {
            return false;
        }

        em.remove(tAsset);
        em.flush();
        System.out.println("END OF DELETE TRANSPORTATUINI ASSET FUNCTION IN SESSION BEAN");
        return true;
    }

    public Integer edittAsset(Integer capacity, Integer price, Integer speed, String status, Integer assetId) {

        System.out.println("[EJB]================================edit transportatuinAsset");
        TransportationAsset tAsset = new TransportationAsset();
        Query query = em.createNamedQuery("Transportation.findbyassetId").setParameter("assetId", assetId);
        tAsset = (TransportationAsset) query.getSingleResult();
        System.out.println("EditTransportationAsset ================= : " + tAsset);

        tAsset.setCapacity(capacity);
        tAsset.setPrice(price);
        tAsset.setSpeed(speed);
        tAsset.setStatus(status);
        

        em.merge(tAsset);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED TransportationAsset");
        return tAsset.getAssetId();
       
    }

    
  
    //Edit in progress
    public boolean addTAssetSchedule(Date startDate, Date endDate,Integer tAssetId) {

        System.out.println("[INSIDE EJB]================================Add Storage Bin");
        Query query = em.createNamedQuery("TransportationAsset.findBytAssetId").setParameter("tAssetId", tAssetId);

        TransportationAsset tAsset = (TransportationAsset) query.getSingleResult();
        if (tAsset != null) {
            AssetSchedule schedule = new AssetSchedule();
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);
            schedule.setTransporationAssetassetId(tAsset);
            tAsset.getAssetScheduleList().add(schedule);
            

            em.merge(tAsset);
            em.persist(schedule);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

//Edit in progress
    public List<AssetSchedule> viewTAssetScheduleforTAsset(Integer tAssetId) {

        System.out.println("In viewTAssetScheduleforTAsset, TAsset ID ============================= : " + tAssetId);
        TransportationAsset assetTemp = null;

        if (tAssetId != null) {
            assetTemp = em.find(TransportationAsset.class, tAssetId);
            System.out.println("In viewMyStorageTypes, finding warehouse" + assetTemp);
        }
        return assetTemp.getAssetScheduleList();

    }

    // Edit in progress
    public Boolean deleteTAssetSchedule(Integer scheduleId) {

        Query query = em.createNamedQuery("StorageBin.findByStorageBinId").setParameter("storageBinId", scheduleId);
        AssetSchedule schedule = (AssetSchedule) query.getSingleResult();
        System.out.println("DeleteStorageBin ================= : " + schedule);
        if (schedule == null) {
            return false;
        }
        TransportationAsset tAsset = schedule.getTransporationAssetassetId();
        tAsset.getAssetScheduleList().remove(schedule);
        em.merge(tAsset);
        em.remove(schedule);
        em.flush();

        System.out.println("END OF DELETE STORAGE BIN FUNCTION IN SESSION BEAN");
        return true;
    }

    //Edit in progress
    public boolean editTAssetSchedule(Date startDate, Date endDate,Integer assetScheduleId) {

        AssetSchedule schedule = new AssetSchedule();
        Query query = em.createNamedQuery("AssetSchedule.findByscheduleId").setParameter("scheduleId", assetScheduleId);
        schedule = (AssetSchedule) query.getSingleResult();
        if (schedule != null) {
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);

            em.merge(schedule);
            em.flush();

            return true;
        }
        else{
            return false;
        }

    }  

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
