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
import entity.MaintenanceLog;
import entity.TransportationOperator;
import entity.PlanAssetSchedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import entity.TransportationOrder;
import javax.ejb.EJB;
import merlionportal.tms.routeoptimizationmodule.DijkstraLand;
import merlionportal.tms.routeoptimizationmodule.DijkstraAir;
import merlionportal.tms.routeoptimizationmodule.DijkstraSea;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class TAssetmanagementSessionBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    private DijkstraLand dijLand;
    @EJB
    private DijkstraAir dijAir;
    @EJB
    private DijkstraSea dijSea;

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

        List<Location> locationList = new ArrayList();
        node.setLocationList(locationList);

        List<Route> RouteList = new ArrayList<Route>();
        node.setRouteList(RouteList);

        System.out.println("==========Node Name========== :" + nodeName);
        System.out.println("==========Node Type========== :" + nodeType);

        em.persist(node);
        em.flush();

        System.out.println("[EJB]================================Successfully Added a New Node");

        return node.getNodeId();
    }

    public List<Node> findDist() {
        Query query = em.createNamedQuery("Node.findByLocationType").setParameter("locationType", "Dist");
        List<Node> dists = query.getResultList();

        return dists;
    }

    public List<Node> findMaint() {
        Query query = em.createNamedQuery("Node.findByLocationType").setParameter("locationType", "Maint");
        List<Node> maints = query.getResultList();

        return maints;
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
        System.out.println("tempnode Id" + nodeId);
        List<Route> allMyRoute = new ArrayList();
        allMyRoute = tempnode.getRouteList();

        return allMyRoute;
    }

    public Boolean deleteRoute(Integer routeId) {
        Query query = em.createNamedQuery("Route.findByRouteId").setParameter("routeId", routeId);
        List<Route> allMyRoute = query.getResultList();

        for (Route r : allMyRoute) {
            if (r.getRouteId().equals(routeId)) {
                Node tempNode = r.getStartNodeId();
                tempNode.getRouteList().remove(r);

                em.merge(tempNode);
                em.remove(r);

                em.flush();
                System.out.println("successfully deleted:" + routeId);
                return true;
            }
        }
        return false;
    }

//        Query query = em.createNamedQuery("Node.findAll");
//        List<Node> allMyNode = query.getResultList();
//
//        for (Node n : allMyNode) {
//            if (Objects.equals(n.getNodeId(), nodeId)) {
//                em.remove(n);
//                em.flush();
//                return true;
//            }
//        }
//        return false;
    public Integer addRoute2(Integer distance, String routeType, Integer startNodeId, Integer endNodeId) {
        Boolean test = false;
        switch (routeType) {
            case "Land":
                test= dijLand.createConnection(startNodeId-1, endNodeId-1, distance);
                break;
            case "Sea":
                test=dijSea.createConnection(startNodeId-1, endNodeId-1, distance);
                break;
            case "Air":
                test=dijAir.createConnection(startNodeId-1, endNodeId-1, distance);
                break;
        }
        System.out.println("DIJ Create Successfully"+test);
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
        Route route = new Route();

        route.setDestination(nodeend.getLocationName());
        route.setDistance(distance);
        route.setOrigin(nodee.getLocationName());
        route.setRouteType(routeType);
        route.setEndNodeId(nodeend.getNodeId());
        route.setStartNodeId(nodee);

        List<AssetSchedule> assetScheduleList = new ArrayList();
        route.setAssetScheduleList(assetScheduleList);

        System.out.println("==========Route Orgin========== :" + nodee.getLocationName());
        System.out.println("==========Route Destination==== :" + nodeend.getLocationName());
        System.out.println("==========Route Distance======= :" + distance);
        System.out.println("==========Route Type=========== :" + routeType);

        nodee.getRouteList().add(route);

        em.persist(route);
        em.merge(nodee);
        em.flush();
        System.out.println("[EJB]====Successfully created Route");

        return route.getRouteId();

    }

    public Boolean addRoute(Integer distance, String routeType, Integer startNodeId, Integer endNodeId) {
        this.addRoute2(distance, routeType, startNodeId, endNodeId);
        this.addRoute2(distance, routeType, endNodeId, startNodeId);
        return true;
    }

    public List<Location> viewMyDistLocations(Integer companyId) {
        List<Location> allMyLocation = new ArrayList<>();
        System.out.println("In viewMyLocation, Company ID ============================= : " + companyId);

        Query query = em.createNamedQuery("Location.findByCompanyId").setParameter("companyId", companyId);
        for (Object o : query.getResultList()) {
            Location location = (Location) o;
            Node tempNode = location.getNodeId();
            if (tempNode.getLocationType().equals("Dist")) {
                allMyLocation.add(location);
            }
        }

        return allMyLocation;
    }

    public Integer findMyDestinationLocationId(Node node, Integer companyId) {
        List<Location> allLocations = this.viewMyLocations(companyId);
        for (Location o : allLocations) {
            if (o.getNodeId() == node) {
                return o.getLocationId();
            }
        }
        return -1;
    }

    public List<Location> viewMyMaintLocations(Integer companyId) {
        List<Location> allMyLocation = new ArrayList<>();
        System.out.println("In viewMyLocation, Company ID ============================= : " + companyId);
        Query query = em.createNamedQuery("Location.findByCompanyId").setParameter("companyId", companyId);
        for (Object o : query.getResultList()) {
            Location location = (Location) o;
            Node tempNode = location.getNodeId();
            if (tempNode.getLocationType().equals("Maint")) {
                allMyLocation.add(location);
            }
        }

        return allMyLocation;
    }

    public List<Location> viewMyLocations(Integer companyId) {
        List<Location> allMyLocation = new ArrayList<>();
        System.out.println("In viewMyLocation, Company ID ============================= : " + companyId);

        Query query = em.createNamedQuery("Location.findByCompanyId").setParameter("companyId", companyId);
        for (Object o : query.getResultList()) {
            Location location = (Location) o;
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

        Location location = new Location();
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

        List<TransportationAsset> transportationAssetList = new ArrayList();
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

    public List<Route> viewRoutesForLocation(Integer locationId) {
        List<Route> returnThisRoutes = new ArrayList();
        if (locationId != null) {
            Location tempLocation = em.find(Location.class, locationId);
            Node node = tempLocation.getNodeId();

            returnThisRoutes = this.viewtheRouteForNode(node.getNodeId());

        }
        return returnThisRoutes;

    }

    public List<Route> viewRoutestoLocation(Integer locationId) {
        List<Route> returnThisRoutes = new ArrayList();
        String destination = new String();
        if (locationId != null) {
            Location tempLocation = em.find(Location.class, locationId);
            Node node = tempLocation.getNodeId();
            System.out.println("endNode Id" + node.getNodeId());
            destination = node.getLocationName();
            System.out.println("destination" + destination);
            Query query = em.createNamedQuery("Route.findAll");
            List<Route> tempList = query.getResultList();

            for (Route o : tempList) {
                if (o.getDestination().equals(destination)) {
                    returnThisRoutes.add(o);
                }
            }
        }
        return returnThisRoutes;
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
        tAsset.setIsAvailable(true);
        tAsset.setIsMaintain(false);
        tAsset.setStatus(status);
        tAsset.setAssetLoad(0);
        tAsset.setRouteId(0);

        List<MaintenanceLog> maintLogList = new ArrayList();
        tAsset.setMaintenanceLogList(maintLogList);

        List<AssetSchedule> scheduleList = new ArrayList();
        tAsset.setAssetScheduleList(scheduleList);

        List<TransportationOrder> orderList = new ArrayList();
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

            locationn.getTransportationAssetList().add(tAsset);

            em.merge(locationn);
            em.flush();
            return tAsset.getAssetId();
        }

    }

    public TransportationAsset getAsset(Integer assetId) {
        TransportationAsset asset = em.find(TransportationAsset.class, assetId);
        return asset;
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

    public List<TransportationAsset> viewMainttAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }

        List<TransportationAsset> temp = new ArrayList();
        List<TransportationAsset> available = new ArrayList();
        temp = locationTemp.getTransportationAssetList();
        for (TransportationAsset tAsset : temp) {
            if (tAsset.getIsMaintain() == true) {
                available.add(tAsset);
            }
        }
        return available;
    }

    public Integer counttAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }
        Integer count = 0;
        List<TransportationAsset> temp = locationTemp.getTransportationAssetList();
        for (TransportationAsset tAsset : temp) {
            if (tAsset.getIsAvailable() == true) {
                count++;
            }
        }
        return count;

    }

    public List<TransportationAsset> viewtAvailableAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }

        List<TransportationAsset> temp = new ArrayList();
        List<TransportationAsset> available = new ArrayList();
        temp = locationTemp.getTransportationAssetList();
        for (TransportationAsset tAsset : temp) {
            if (tAsset.getIsAvailable() == true) {
                available.add(tAsset);
            }
        }
        return available;
    }

    public List<TransportationAsset> viewtNotAvailableAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }

        List<TransportationAsset> temp = new ArrayList();
        List<TransportationAsset> available = new ArrayList();
        temp = locationTemp.getTransportationAssetList();
        for (TransportationAsset tAsset : temp) {
            if (tAsset.getIsAvailable() == false) {
                available.add(tAsset);
            }
        }
        return available;
    }

    public Integer countAvailableAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }

        List<TransportationAsset> temp;

        temp = locationTemp.getTransportationAssetList();
        Integer count = 0;
        for (TransportationAsset tAsset : temp) {
            if (tAsset.getIsAvailable() == true) {
                count++;
            }
        }
        return count;
    }

    public List<TransportationAsset> viewMaintAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }

        List<TransportationAsset> temp = new ArrayList();
        List<TransportationAsset> maint = new ArrayList();
        temp = locationTemp.getTransportationAssetList();
        for (TransportationAsset tAsset : temp) {
            if (tAsset.getIsMaintain() == true) {
                maint.add(tAsset);
            }
        }
        return maint;
    }

    public Integer countMaintAssetForALocation(Integer locationId) {

        System.out.println("In viewMyTransportationAsset at, Location ID ============================= : " + locationId);
        System.out.println("viewAssetForALocation");
        Location locationTemp = new Location();

        if (locationId != null) {
            locationTemp = em.find(Location.class, locationId);
            System.out.println("In viewMyTransportationAssets, finding location" + locationTemp);
        }

        List<TransportationAsset> temp = new ArrayList();
        List<TransportationAsset> maint = new ArrayList();
        temp = locationTemp.getTransportationAssetList();
        Integer count = 0;
        for (TransportationAsset tAsset : temp) {
            if (tAsset.getIsMaintain() == true) {
                maint.add(tAsset);
                count++;
            }
        }
        return count;
    }

    public Boolean deletetAsset(Integer assetId) {

        TransportationAsset tAsset = new TransportationAsset();
        tAsset = em.find(TransportationAsset.class, assetId);
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

    public List<TransportationAsset> viewAssetOnRoute(Integer routeId) {
        Route tempRoute = em.find(Route.class, routeId);
        List<AssetSchedule> tempScheduleList = tempRoute.getAssetScheduleList();
        TransportationAsset adding = new TransportationAsset();
        List<TransportationAsset> returnThisList = new ArrayList();
        for (AssetSchedule o : tempScheduleList) {
            adding = o.getTransporationAssetassetId();
            returnThisList.add(adding);
        }
        return returnThisList;
    }

    public List<AssetSchedule> viewAssetScheduleOnRoute(Integer routeId) {
        Route tempRoute = em.find(Route.class, routeId);
        List<AssetSchedule> tempScheduleList = tempRoute.getAssetScheduleList();
        return tempScheduleList;
    }

    public Integer addTAssetSchedule(Date startDate, Date endDate, Integer loading, Integer tAssetId, Integer operatorId, Integer routeId) {

        System.out.println("[INSIDE EJB]================================Add Transportation Asset Schedule");
        Query query = em.createNamedQuery("TransportationAsset.findByAssetId").setParameter("assetId", tAssetId);

        TransportationAsset tAsset = (TransportationAsset) query.getSingleResult();
        if (tAsset != null) {
            AssetSchedule schedule = new AssetSchedule();
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);
            schedule.setTransporationAssetassetId(tAsset);
            schedule.setAssetLoad(loading);
            Route tempRoute = em.find(Route.class, routeId);
            schedule.setRoute(tempRoute);
            tempRoute.getAssetScheduleList().add(schedule);

            schedule.setOperatorId(operatorId);
            tAsset.getAssetScheduleList().add(schedule);
            tAsset.setIsAvailable(Boolean.FALSE);

            em.persist(schedule);
            em.merge(tAsset);
            em.merge(tempRoute);

            em.flush();

            return schedule.getScheduleId();
        } else {
            return -1;
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
    public Boolean deleteTAssetSchedule(Integer scheduleId, Integer companyId) {

        AssetSchedule schedule = em.find(AssetSchedule.class, scheduleId);
        System.out.println("Delete Schedule ================= : " + schedule);
        if (schedule == null) {
            return false;
        }
        TransportationAsset tAsset = schedule.getTransporationAssetassetId();
        tAsset.setIsAvailable(Boolean.TRUE);
        Integer assetId = tAsset.getAssetId();

        String destination = schedule.getRoute().getDestination();
        Query query = em.createNamedQuery("Route.findByDestination").setParameter("destination", destination);
        List<Route> thisRouteList = query.getResultList();
        Route thisRoute = new Route();
        for (Route o : thisRouteList) {
            if (o == schedule.getRoute()) {
                thisRoute = o;
            }
        }

        String endNodename = thisRoute.getDestination();
        System.out.println("==================================The destination is :" + endNodename);
        Query query2 = em.createNamedQuery("Node.findByLocationName").setParameter("locationName", endNodename);
        Node endnode = (Node) query2.getSingleResult();

        Integer locationId = this.findMyDestinationLocationId(endnode, companyId);
        Location destinationLocation = em.find(Location.class, locationId);
        System.out.println("The Destination Entity" + destinationLocation);
        System.out.println("The Destination Location Name: " + destinationLocation.getLocationName());
        destinationLocation.getTransportationAssetList().add(tAsset);
        tAsset.setLocationlocationId(destinationLocation);
        em.merge(tAsset);
        em.flush();
        Integer originId = this.findMyDestinationLocationId(thisRoute.getStartNodeId(), companyId);
        Location originLocation = em.find(Location.class, originId);
        originLocation.getTransportationAssetList().remove(tAsset);

        Route route = schedule.getRoute();
        route.getAssetScheduleList().remove(schedule);
        tAsset.getAssetScheduleList().remove(schedule);
        em.merge(destinationLocation);
        em.merge(route);
        em.merge(originLocation);

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

//    public Integer addPLANAssetSchedule(Date startDate, Date endDate, Integer loading, Integer tAssetId, Integer operatorId) {
//
//        System.out.println("[INSIDE EJB]================================Add Transportation Asset Schedule");
//
//        TransportationAsset tAsset = em.find(TransportationAsset.class, tAssetId);
//        if (tAsset != null) {
//            PlanAssetSchedule schedule = new PlanAssetSchedule();
//            schedule.setStartDate(startDate);
//            schedule.setEndDate(endDate);
//            schedule.setTransportationAsset(tAsset);
//            schedule.setAssetLoad(loading);
//
//            schedule.setOperatorId(operatorId);
//            tAsset.getPlanAssetScheduleList().add(schedule);
//            tAsset.setIsAvailable(Boolean.FALSE);
//
//            em.persist(schedule);
//            em.merge(tAsset);
//
//            em.flush();
//
//            return schedule.getPlanScheduleId();
//        } else {
//            return -1;
//        }
//
//    }
// check if asset is free during start date and end date;
    public boolean checkAssetAvailableSchedule(Date startDate, Date endDate, Integer assetId) {
        TransportationAsset tempAsset = em.find(TransportationAsset.class, assetId);

        List<AssetSchedule> tempScheduleList = new ArrayList();
        tempScheduleList = tempAsset.getAssetScheduleList();

        for (AssetSchedule o : tempScheduleList) {
            Date tempStart = o.getStartDate();
            Date tempEnd = o.getEndDate();
            if (startDate.after(tempStart) && startDate.before(tempEnd)) {
                return false;
            } else if (endDate.before(tempEnd) && endDate.after(tempStart)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public boolean checkAssetAvailablePLANSchedule(Date startDate, Date endDate, Integer assetId) {
        TransportationAsset tempAsset = em.find(TransportationAsset.class, assetId);

        List<AssetSchedule> tempScheduleList = new ArrayList();
        tempScheduleList = tempAsset.getAssetScheduleList();

        for (AssetSchedule o : tempScheduleList) {
            Date tempStart = o.getStartDate();
            Date tempEnd = o.getEndDate();
            if (startDate.after(tempStart) && startDate.before(tempEnd)) {
                return false;
            } else if (endDate.before(tempEnd) && endDate.after(tempStart)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

// check not full asset on route;
//    public List<TransportationAsset> findNotFullAssetOnRoute(Integer routeId) {
//        Route tempRoute = em.find(Route.class, routeId);
//
//        List<TransportationAsset> tempAssetList = tempRoute.getTransportationAssetList();
//        List<TransportationAsset> notFullAssetList = new ArrayList();
//
//        for (TransportationAsset o : tempAssetList) {
//            if ((o.getCapacity() - o.getLoad()) != 0) {
//                notFullAssetList.add(o);
//            }
//        }
//        return notFullAssetList;
//    }
//    public List<TransportationAsset> findNotFullAssetonRouteAfterDate(Date startDate, Integer routeId) {
//
//        List<TransportationAsset> tempAssetList = this.findNotFullAssetOnRoute(routeId);
//        List<TransportationAsset> returnThisList = new ArrayList();
//        for (TransportationAsset o : tempAssetList) {
//            List<AssetSchedule> tempScheduleList = new ArrayList();
//            tempScheduleList = o.getAssetScheduleList();
//            test:
//            for (AssetSchedule k : tempScheduleList) {
//                if (k.getStartDate().after(startDate)) {
//                    returnThisList.add(o);
//                    break;
//                }
//            }
//        }
//
//        return returnThisList;
//    }
    public Integer numberOfFreeTruckOnLocation(Integer locationId) {
        Location templocation = em.find(Location.class, locationId);

        List<TransportationAsset> temptrans = templocation.getTransportationAssetList();

        Integer count = 0;
        for (TransportationAsset o : temptrans) {
            if (o.getAssetType().equals("Truck")) {
                if (o.getAssetLoad() == 0) {
                    count++;
                }
            }
        }

        return count;

    }

//    public Integer numberOfFreeShipOnLocation(Integer locationId) {
//
//    }
}
