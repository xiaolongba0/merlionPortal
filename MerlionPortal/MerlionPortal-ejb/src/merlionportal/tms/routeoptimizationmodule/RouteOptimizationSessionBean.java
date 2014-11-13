/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.tms.routeoptimizationmodule;

import javax.ejb.LocalBean;
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
import entity.Estimation;
import entity.PlanAssetSchedule;
import java.util.Calendar;
import javax.ejb.EJB;
import merlionportal.tms.routeoptimizationmodule.DijkstraAir;
import merlionportal.tms.routeoptimizationmodule.DijkstraLand;
import merlionportal.tms.routeoptimizationmodule.DijkstraSea;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class RouteOptimizationSessionBean {

    @PersistenceContext
    EntityManager em;
    @EJB
    private DijkstraLand dijLand;
    @EJB
    private DijkstraAir dijAir;
    @EJB
    private DijkstraSea dijSea;

    public Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public List<Estimation> viewTheEstimation(Integer companyId) {

        System.out.println("View Estimation for CompanyId: " + companyId);

        Query query = em.createNamedQuery("Estimation.findByCompanyId").setParameter("companyId", companyId);

        List<Estimation> returnThisList = query.getResultList();

        return returnThisList;
    }

    public Boolean deleteEstimation(Integer estId, Integer companyId) {

        System.out.println("View Estimation for CompanyId: " + companyId);

        Query query = em.createNamedQuery("Estimation.findByCompanyId").setParameter("companyId", companyId);

        List<Estimation> returnThisList = query.getResultList();

        for (Estimation n : returnThisList) {
            if (Objects.equals(n.getEstId(), estId)) {
                em.remove(n);
                em.flush();
                return true;
            }
        }
        return false;
    }

    public Integer addNewEstimation(Integer companyId, Integer originId, Integer destinationId, String type, Date endDate, Integer totalLoad) {
        System.out.println("[INSIDE Route Optimization >>>>>>>>> Creating new Estimation");

        Estimation newEst = new Estimation();

        Node start = em.find(Node.class, originId);
        Node end = em.find(Node.class, destinationId);

        newEst.setCompanyId(companyId);
        newEst.setOrigin(start.getLocationName());
        newEst.setDestination(end.getLocationName());
        newEst.setEndDate(endDate);
        Integer distance = this.solve(start, end, type);
        Integer days = this.calculateDays(distance, type);
        System.out.println("Calculated Days Needed:  " + days);
        Date startDate = this.addDays(endDate, -days);
        newEst.setStartDate(startDate);
        Integer cost = this.calculateCost(distance, type, totalLoad);
        newEst.setCost((double) cost);
        newEst.setTotalLoad(totalLoad);
        newEst.setType(type);

        List<PlanAssetSchedule> planList = new ArrayList();
        newEst.setPlanAssetScheduleList(planList);

        System.out.println("==========Origin========== :" + start.getLocationName());
        System.out.println("==========Destination===== :" + end.getLocationName());

        em.persist(newEst);
        em.flush();

        System.out.println("[EJB]================================Successfully Created Empty Estimation");
        return newEst.getEstId();
    }

    public Boolean runEstimation(Integer companyId, Integer originId, Integer destinationId, String type, Date endDate, Integer totalLoad) {
        Integer estId = this.addNewEstimation(companyId, originId, destinationId, type, endDate, totalLoad);
        if (estId > 0) {
            Node start = em.find(Node.class, originId);
            Node end = em.find(Node.class, destinationId);

            for (Route r : this.routeOptimize(start, end, type)) {
                this.addPlannedSchedules(estId, r, type, totalLoad, endDate);
            }
            return true;
        } else {
            return false;
        }
    }

    public List<PlanAssetSchedule> viewthePlannedSchedulesForEstimation(Integer estId) {
        System.out.println("In Route Optimization Planned Schedule List");
        System.out.println("Estimation ID: " + estId);

        Estimation tempEst = em.find(Estimation.class, estId);

        List<PlanAssetSchedule> tempList = tempEst.getPlanAssetScheduleList();

        return tempList;
    }

    public Boolean deletePlannedSchedules(Integer estId) {

        if (estId > -1) {
            PlanAssetSchedule delete = em.find(PlanAssetSchedule.class, estId);
            Estimation temp = delete.getEstimation();

            temp.getPlanAssetScheduleList().remove(delete);

            em.merge(temp);
            em.remove(delete);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

    public Date addPlannedSchedules(Integer estId, Route route, String type, Integer totalLoading, Date endDate) {
        System.out.println("[INSIDE ROUTE Optimization]================================Add New Planned Schedule");

        Estimation tempEst = em.find(Estimation.class, estId);
        PlanAssetSchedule schedule = new PlanAssetSchedule();
        schedule.setEstimation(tempEst);
        schedule.setOrigin(route.getOrigin());
        schedule.setDestination(route.getDestination());
        schedule.setEndDate(endDate);

        Date startDate = new Date();
        Integer free = 0;
        Integer quantity = 0;
        Integer days = 0;
        switch (type) {
            case "Land":
                schedule.setType(type);
                schedule.setAssetLoad(2);
                quantity = (int) Math.ceil(totalLoading / 2);
                free = 2 * quantity - totalLoading;
                days = (route.getDistance()) / 1000;
                startDate = this.addDays(endDate, days);

                break;
            case "Sea":
                schedule.setType(type);
                schedule.setAssetLoad(100);
                quantity = (int) Math.ceil(totalLoading / 100);
                free = 2 * quantity - totalLoading;
                days = (route.getDistance()) / 500;
                startDate = this.addDays(endDate, days);
                break;
            case "Air":
                schedule.setType(type);
                schedule.setAssetLoad(100);
                quantity = (int) Math.ceil(totalLoading / 3);
                free = 2 * quantity - totalLoading;
                days = (int) Math.ceil(route.getDistance() / 15000);
                startDate = this.addDays(endDate, days);
                break;
        }
        schedule.setAssetQuantity(quantity);
        schedule.setFreeSpace(free);

        tempEst.getPlanAssetScheduleList().add(schedule);

        em.persist(schedule);
        em.merge(tempEst);
        em.flush();

        System.out.println("[EJB]====Successfully created PlanAssetSchedule");

        return startDate;

    }

    public Integer solve(Node start, Node end, String type) {
        Integer temp = 0;
        switch (type) {
            case "Land":
                temp = dijLand.solve(10, start.getNodeId() - 1, end.getNodeId() - 1);
                System.out.println("FUCK this LAND temp : " + temp);
                break;
            case "Sea":
                temp = dijSea.solve(10, start.getNodeId() - 1, end.getNodeId() - 1);
                System.out.println("FUCK this Sea temp : " + temp);
                break;
            case "Air":
                temp = dijAir.solve(10, start.getNodeId() - 1, end.getNodeId() - 1);
                System.out.println("FUCK this Air temp : " + temp);
                break;
        }
        
        return temp;
    }

    public Integer calculateDays(Integer distance, String type) {
        Integer days = 0;

        Integer temp = 0;
        switch (type) {
            case "Land":
                temp = distance / 1000;
                System.out.println("FUCK DAYS Land this temp : " + temp);
                break;
            case "Sea":
                temp = distance / 500;
                System.out.println("FUCK DAYS Sea this temp : " + temp);
                break;
            case "Air":
                temp = distance / 15000;
                System.out.println("FUCK DAYS Air this temp : " + temp);
                break;
        }
        days = days + temp;
        return days;
    }

    public Integer calculateCost(Integer distance, String type, Integer totalLoad) {
        Integer temp = 0;
        Integer quantity = 0;
        switch (type) {
            case "Land":
                quantity = (int) Math.ceil(totalLoad / 2);
                temp = distance * 2 * quantity;
                break;
            case "Sea":
                quantity = (int) Math.ceil(totalLoad / 100);
                temp = distance * 125 * quantity;
                break;
            case "Air":
                quantity = (int) Math.ceil(totalLoad / 3);
                temp = distance * 18000 * quantity;
                break;

        }
        return temp;
    }

    public List<Route> getRoutesExcludeReturn(Node start, Node end, String type) {
        List<Route> temp = end.getRouteList();
        List<Route> leeee = new ArrayList();

        for (Route r : temp) {
            if (!Objects.equals(r.getEndNodeId(), start.getNodeId())) {
                if (r.getRouteType().equals(type)) {
                    leeee.add(r);
                }
            }
        }
        return leeee;
    }
// return destId of route; compare with DestinationId

    public Integer checkConnectivity(Integer originId, Integer destinationId, String type) {
        Node origin = em.find(Node.class, originId);
        for (Route o : origin.getRouteList()) {
            Integer destId = o.getEndNodeId();
            if (destId == destinationId) {
                return 1;
            } else {
                Node destNode = em.find(Node.class, destId);
                for (Route x : this.getRoutesExcludeReturn(origin, destNode, type)) {
                    return checkConnectivity(x.getEndNodeId(), destinationId, type);
                }
            }
            return -1;
        }
        return -1;
    }
    //    public Boolean checkConnectivity(Integer originId, Integer destinationId, String type) {
    //        boolean A = true;
    //        Integer tempId = originId;
    //        while (A) {
    //            Node origin = em.find(Node.class, tempId);
    //            oloop:
    //            for (Route o : origin.getRouteList()) {
    //                Integer destId = o.getEndNodeId();
    //                if(destId == destinationId){
    //                    A = false;
    //                    return true;
    //                }
    //                Node destNode = em.find(Node.class, destId);
    //                for (Route x : this.getRoutesExcludeReturn(origin, destNode, type)) {
    //                    if(x.getEndNodeId()==destinationId){
    //                        A = false;
    //                        return true;
    //                    }
    //                }
    //                tempId = destId;
    //                origin = em.find(Node.class, tempId);
    //            }
    //
    //        }
    //        return false;
    //    }

    public List<Route> routeOptimize(Node start, Node end, String Type) {
        List<Route> routes = new ArrayList();
        if (this.checkConnectivity(start.getNodeId(), end.getNodeId(), Type) > 1) {

            for (int i = 5; i > 0; i--) {
                for (Route o : start.getRouteList()) {
                    if (Objects.equals(o.getEndNodeId(), end.getNodeId())) {
                        routes.add(o);
                    }

                }
            }

        }
        return routes;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
