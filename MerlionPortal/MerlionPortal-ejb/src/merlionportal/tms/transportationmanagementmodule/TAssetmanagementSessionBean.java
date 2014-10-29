/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.tms.transportationmanagementmodule;

import javax.ejb.Stateless;
import entity.Company;
import entity.Route;
import entity.Node;
import entity.Location;
import entity.TransportationAsset;
import entity.AssetSchedule;
import entity.TransportationOperator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import entity.TransportationOrder;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class TAssetmanagementSessionBean {

    @PersistenceContext
    EntityManager em;
    //-------Node
    private Node node;
    private ArrayList<Location> locationList;
    //------Route
    private Route route;
    private ArrayList<Route> routeList;
    //-------Location
    private Location location;
    private Integer locationId;
    private ArrayList<TransportationAsset> transportationAssetList;

    //-------------Asset
    private TransportationAsset asset;
    private Integer assetId;
    private ArrayList<TransportationOrder> orderList;
    //------------Schedule
    private ArrayList<AssetSchedule> scheduleList;

    //-----------Operator
    private ArrayList<TransportationOperator> operatorList;

    public List<Node> viewTheNodes() {

        System.out.println("VIEW Nodes");

        Query query = em.createNamedQuery("Node.findAll");
        List<Node> allMyNode = query.getResultList();

        return allMyNode;
    }

    public Boolean deleteNode(Integer nodeId) {

        Query query = em.createNamedQuery("Node.findAll");
        List<Node> allMyNode = query.getResultList();

        for (Node n : allMyNode) {
            if (Objects.equals(n.getNodeId(), nodeId)) {
                em.remove(n);
                em.flush();
                return true;
            }
        }
        return false;
    }

    public Integer addNewNode(String nodeName, String nodeType) {
        System.out.println("[INSIDE EJB]================================Add New Node");

        Node node = new Node();

        node.setLocationName(nodeName);
        node.setLocationType(nodeType);

        locationList = new ArrayList<Location>();
        node.setLocationList(locationList);

        System.out.println("==========Node Name========== :" + nodeName);
        System.out.println("==========Node Type========== :" + nodeType);

        em.persist(node);
        em.flush();

        System.out.println("[EJB]================================Successfully Added a New Node");

        return node.getNodeId();
    }

    public Integer editNode(String nodeName, String nodeType, Integer nodeId) {

        System.out.println("[EJB]================================edit Node");
        Query query = em.createNamedQuery("Node.findAll");
        List<Node> allMyNode = query.getResultList();

        Node node = new Node();
        for (Node l : allMyNode) {
            if (Objects.equals(l.getNodeId(), nodeId)) {
                node = l;
            }
        }

        node.setLocationName(nodeName);
        node.setLocationType(nodeType);

        em.merge(node);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED NODE");
        return node.getNodeId();

    }

    public List<Route> viewtheRouteForNode(Integer nodeId) {
        System.out.println("VIEW Routes");
        System.out.println("NodeId: " + nodeId);

        Query query = em.createNamedQuery("Node.findAll");
        List<Node> allMyNode = query.getResultList();

        Node tempnode = new Node();
        for (Node l : allMyNode) {
            if (Objects.equals(l.getNodeId(), nodeId)) {
                tempnode = l;
            }
        }
        System.out.println("tempnode Id"+ nodeId);
        List<Route> allMyRoute = new ArrayList();   
        allMyRoute = tempnode.getRouteList();

        return allMyRoute;
    }

    public Boolean deleteRoute(Integer routeId) {
        Query query = em.createNamedQuery("Route.findByRouteId").setParameter("routeId", routeId);
        List<Route> allMyRoute = query.getResultList();

        for (Route r : allMyRoute) {
            if (Objects.equals(r.getRouteId(), routeId)) {
                em.remove(r);
                em.flush();
                System.out.println("successfully deleted:" + routeId);
                return true;
            }
        }
        return false;
    }

    public Integer addRoute(Integer distance, String routeType, Integer startNodeId, Integer endNodeId) {
        System.out.println("[INSIDE EJB]================================Add New Route");

        Query query1 = em.createNamedQuery("Node.findAll");

        List<Node> allMyNode = query1.getResultList();

        Node nodee = new Node();
        for (Node n : allMyNode) {
            if (Objects.equals(n.getNodeId(), startNodeId)) {
                nodee = n;
            }
        }
        Query query2 = em.createNamedQuery("Node.findAll");

        List<Node> allMyNod1e = query2.getResultList();
        Node nodeend = new Node();
        for (Node e : allMyNod1e) {
            if (Objects.equals(e.getNodeId(), endNodeId)) {
                nodeend = e;
            }
        }
        route = new Route();

        route.setDestination(nodeend.getLocationName());
        route.setDistance(distance);
        route.setOrigin(nodee.getLocationName());
        route.setRouteType(routeType);
        route.setEndNodeId(nodeend.getNodeId());
        route.setStartNodeId(nodee);

        transportationAssetList = new ArrayList<TransportationAsset>();
        route.setTransportationAssetList(transportationAssetList);

        System.out.println("==========Route Orgin========== :" + nodee.getLocationName());
        System.out.println("==========Route Destination==== :" + nodeend.getLocationName());
        System.out.println("==========Route Distance======= :" + distance);
        System.out.println("==========Route Type=========== :" + routeType);

        em.persist(route);
        em.flush();
        System.out.println("[EJB]====Successfully created Route");

        return route.getRouteId();

    }

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

    public Integer addNewLocation(String locationName, String locationType, Integer companyId, Integer NodeId) {
        System.out.println("[INSIDE EJB]================================Add New Location");
        Query query = em.createNamedQuery("Node.findAll");

        List<Node> allMyNodes = query.getResultList();

        location = new Location();
        Node tempnode = new Node();
        for (Node n : allMyNodes) {
            if (Objects.equals(n.getNodeId(), NodeId)) {
                tempnode = n;
            }
        }
        location.setLocationName(locationName);
        location.setLocationType(locationType);
        location.setCompanyId(companyId);
        location.setNodeId(tempnode);

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

        System.out.println("[INSIDE EJB]================================Add Transportation Asset");
        Query query = em.createNamedQuery("Location.findByCompanyId").setParameter("companyId", companyId);

        List<Location> allMyLocations = query.getResultList();

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

        orderList = new ArrayList<TransportationOrder>();
        tAsset.setTransportationOrderList(orderList);

//        storageBinList = new ArrayList<StorageBin>();
//        storageType.setStorageBinList(storageBinList);
        System.out.println("Location : " + locationn);
        System.out.println("Location ID ============ : " + locationn.getLocationId());
        System.out.println("TAsset type :" + assetType);
        System.out.println("TAsset speed:" + speed);
        System.out.println("TAsset status: " + status);

        ///check later 
        if (locationn.getLocationId() == null) {
            System.out.println("Location ID IS NULL");
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
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }
        return locationTemp.getTransportationAssetList();

    }

    public Boolean deletetAsset(Integer assetId) {

        TransportationAsset tAsset = new TransportationAsset();
        Query query = em.createNamedQuery("TransportationAsset.findByAssetId").setParameter("tAssetId", assetId);
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
        Query query = em.createNamedQuery("TransportationAsset.findByAssetId").setParameter("assetId", assetId);
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

    public boolean addTAssetSchedule(Date startDate, Date endDate, Integer tAssetId, Integer operatorId) {

        System.out.println("[INSIDE EJB]================================Add Transportation Asset Schedule");
        Query query = em.createNamedQuery("TransportationAsset.findByAssetId").setParameter("assetId", tAssetId);

        TransportationAsset tAsset = (TransportationAsset) query.getSingleResult();
        if (tAsset != null) {
            AssetSchedule schedule = new AssetSchedule();
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);
            schedule.setTransporationAssetassetId(tAsset);

            schedule.setOperatorId(operatorId);
            tAsset.getAssetScheduleList().add(schedule);
            
            em.persist(schedule);
            em.merge(tAsset);

            em.flush();

            return true;
        } else {
            return false;
        }

    }

    public List<AssetSchedule> viewTAssetScheduleforTAsset(Integer tAssetId) {

        System.out.println("In viewTAssetScheduleforTAsset, TAsset ID ============================= : " + tAssetId);
        TransportationAsset assetTemp = null;

        if (tAssetId != null) {
            assetTemp = em.find(TransportationAsset.class, tAssetId);
            System.out.println("In view my transportation assets, finding location" + assetTemp);
        }
        return assetTemp.getAssetScheduleList();

    }

    // Edit in progress
    public Boolean deleteTAssetSchedule(Integer scheduleId) {

        Query query = em.createNamedQuery("AssetSchedule.findByScheduleId").setParameter("scheduleId", scheduleId);
        AssetSchedule schedule = (AssetSchedule) query.getSingleResult();
        System.out.println("Delete Schedule ================= : " + schedule);
        if (schedule == null) {
            return false;
        }
        TransportationAsset tAsset = schedule.getTransporationAssetassetId();
        tAsset.getAssetScheduleList().remove(schedule);
        em.merge(tAsset);
        em.remove(schedule);
        em.flush();

        System.out.println("END OF DELETE Schedule FUNCTION IN SESSION BEAN");
        return true;
    }

    //Edit in progress
    public boolean editTAssetSchedule(Date startDate, Date endDate, Integer assetScheduleId) {

        AssetSchedule schedule = new AssetSchedule();
        Query query = em.createNamedQuery("AssetSchedule.findByScheduleId").setParameter("scheduleId", assetScheduleId);
        schedule = (AssetSchedule) query.getSingleResult();
        if (schedule != null) {
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);

            em.merge(schedule);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

}
