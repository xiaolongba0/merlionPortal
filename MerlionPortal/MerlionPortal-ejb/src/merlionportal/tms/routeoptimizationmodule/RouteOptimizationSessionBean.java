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

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class RouteOptimizationSessionBean {

    @PersistenceContext
    EntityManager em;

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
        if (this.checkConnectivity(originId, destinationId, type)) {

            Node start = em.find(Node.class, originId);
            Node end = em.find(Node.class, destinationId);

            newEst.setCompanyId(companyId);
            newEst.setOrigin(start.getLocationName());
            newEst.setDestination(end.getLocationName());
            newEst.setEndDate(endDate);
            Integer days = this.calculateDays(start, end, type);
            Date startDate = this.addDays(endDate, days);
            newEst.setStartDate(startDate);
            Integer cost = this.calculateCost(start, end, type, totalLoad);
            newEst.setCost((double) cost);

            List<PlanAssetSchedule> planList = new ArrayList();
            newEst.setPlanAssetScheduleList(planList);

            System.out.println("==========Origin========== :" + start.getLocationName());
            System.out.println("==========Destination===== :" + end.getLocationName());

            em.persist(newEst);
            em.flush();

            System.out.println("[EJB]================================Successfully Created Empty Estimation");
            return newEst.getEstId();
        }
        return -1;
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

    public Integer calculateDays(Node start, Node end, String type) {
        List<Route> bestRoutes = new ArrayList();
        bestRoutes = this.routeOptimize(start, end, type);
        Integer days = 0;
        for (Route r : bestRoutes) {
            Integer temp = 0;
            switch (type) {
                case "Land":
                    temp = r.getDistance() / 1000;
                    break;
                case "Sea":
                    temp = r.getDistance() / 500;
                    break;
                case "Air":
                    temp = (int) Math.ceil(r.getDistance() / 15000);
                    break;
            }
            days = days + temp;
        }
        return days;
    }

    public Integer calculateCost(Node start, Node end, String type, Integer totalLoad) {
        List<Route> bestRoutes = new ArrayList();
        bestRoutes = this.routeOptimize(start, end, type);
        Integer cost = 0;
        for (Route r : bestRoutes) {
            Integer temp = 0;
            Integer quantity = 0;
            switch (type) {
                case "Land":
                    quantity = (int) Math.ceil(totalLoad / 2);
                    temp = quantity * r.getDistance() * 2;
                    break;
                case "Sea":
                    quantity = (int) Math.ceil(totalLoad / 100);
                    temp = quantity * r.getDistance() * 125;
                    break;
                case "Air":
                    quantity = (int) Math.ceil(totalLoad / 3);
                    temp = quantity * r.getDistance() * 18000;
                    break;
            }
            cost = cost + temp;
        }
        return cost;
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

    public Integer checkDestNode(Integer originId, Integer destinationId, String type) {
        Node origin = em.find(Node.class, originId);
        for (Route o : origin.getRouteList()) {
            Integer destId = o.getEndNodeId();
            Node destNode = em.find(Node.class, destId);
            for (Route x : this.getRoutesExcludeReturn(origin, destNode, type)) {
                return x.getEndNodeId();
            }
        }
        return -1;
    }

    public Boolean checkConnectivity(Integer originId, Integer destinationId, String type) {
        boolean A = true;
        while (A) {
            Node origin = em.find(Node.class, originId);
            for (Route o : origin.getRouteList()) {
                Integer destId = o.getEndNodeId();
                if (Objects.equals(destId, destinationId)) {
                    A = false;
                    return true;
                } else if (checkDestNode(destId, destinationId, type) == destinationId) {
                    A = false;
                    return true;
                }
            }
            
        }
        return false;
    }

    public List<Route> routeOptimize(Node start, Node end, String Type) {
        List<Route> routes = new ArrayList();
        if (this.checkConnectivity(start.getNodeId(), end.getNodeId(), Type)) {

        }
        return routes;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
