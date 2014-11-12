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
import entity.TransportationLog;
import entity.TransportationLog;
import entity.TransportationOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class TOrderManagementSessionBean {

    @PersistenceContext
    EntityManager em;

//    private String cargoType;
//    private Integer cargoWeight;
//    private Integer companyId;
//    private Integer creatorId;
//    private String destination;
//    private String origin;
//    private Integer referenceId;
//    private String referenceType;
//    private Date timeStart;
//    private Date timeEnd;

    private String action;
    private String actionMessage;
    private Integer logId;
    private Integer OperatorId;
    private Date timeStamp;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public List<TransportationOrder> viewTOrderForCompany(Integer companyId) {
        List<TransportationOrder> allMyTOrder = new ArrayList();
        System.out.println("========view transportation order for company:" + companyId);

        Query query = em.createNamedQuery("TransportationOrder.findByCompanyId").setParameter("companyId", companyId);
        allMyTOrder = query.getResultList();

        return allMyTOrder;

    }

    public boolean deleteTransportationOrder(Integer companyId, Integer orderId) {
        System.out.println("========Delete Transportation Order Id" + orderId + "of company Id" + companyId);
        Query query = em.createNamedQuery("TransportationOrder.findByCompanyId").setParameter("companyId", companyId);
        List<TransportationOrder> allMyOrders = query.getResultList();

        for (TransportationOrder o : allMyOrders) {
            if (Objects.equals(o.getTransportationOrderId(), orderId)) {
                em.remove(o);
                em.flush();
                return true;
            }
        }
        return false;

    }
//    cargoType == transType；
//    cargoWeight == () TEU；
//    creatorId == get.User id();
//    absed on destination and origin ==> routeList involvedroutes;
//    Distance = for involvedroutes(total route.distance;
//    In put End Date ==>>> StartDate = Distance / asset.Speed
//    startTime1 & endTime1 for route1; ==> Create assetScheduleId for route1, route1.assetList.add Asset
//    startTime2 & endTime2 for route2; ==> Create assetSchedule for route2
//    etc.
//    
//    

//    If asset Is available ==>
    public Integer AddNewTransportationOrder(String cargoType, Integer cargoWeight, Integer companyId, Integer creatorId, Integer destination, Integer origin, Integer referenceId, Integer referenceType, Date endDate, Date startDate) {
        System.out.println("[INSIDE EJB]================================Add New Transportation Order");
        if(origin>-1){
            
        Location ll = new Location();
        ll = em.find(Location.class, origin);
      
        TransportationOrder order = new TransportationOrder();

        order.setCargoType(cargoType);
        order.setCargoWeight(cargoWeight);
        order.setCompanyId(companyId);
        order.setCreatorId(creatorId);
        Location l = new Location();
        l = em.find(Location.class, destination);
        String des = l.getLocationName();
        order.setDestination(des);
        String ori = ll.getLocationName();
        order.setOrigin(ori);
        order.setReferenceId(referenceId);
        order.setReferenceType(referenceId);
        order.setTimeEnd(endDate);
        Date timeSstart = new Date();
        timeSstart = endDate;
        Calendar c = Calendar.getInstance();
        c.setTime(timeSstart);
        c.add(Calendar.DATE, -5);
        timeSstart = c.getTime();

        order.setTimeStart(timeSstart);
        List<TransportationAsset> assetList = new ArrayList();
        order.setTransportationAssetList(assetList);

        
        List<TransportationLog> logList = new ArrayList();
        order.setTransportationLogList(logList);

        System.out.println("==========TransportationOrder companyId========== :" + companyId);
        System.out.println("==========TransportationOrder Destination========== :" + destination);
        System.out.println("==========TransportationOrder Origin========== :" + origin);
        System.out.println("==========TransportationOrder Shipping Date========== :" + timeSstart);
        System.out.println("==========TransportationOrder Delivery Date========== :" + endDate);
        
        em.persist(order);
        em.flush();

        System.out.println("[EJB]================================Successfully Added a New order");

        return order.getTransportationOrderId();
        }
        return null;
    }

    public List<TransportationLog> viewLogforOrder(Integer orderId) {

        List<TransportationLog> allMyLog = new ArrayList();
        System.out.println("Viewing shipping record for Order Id: " + orderId);
  

        TransportationOrder o = em.find(TransportationOrder.class, orderId);
        System.out.println("Successfully find Order Id: " + orderId);
        allMyLog = o.getTransportationLogList();

        return allMyLog;
    }

    public Boolean deleteLog(Integer logId) {
        System.out.println("========Delete Transportation Order Id" + logId);
        Query query = em.createNamedQuery("TransportationLog.findByLogId").setParameter("logId", logId);
        List<TransportationLog> allMyLogs = query.getResultList();

        for (TransportationLog o : allMyLogs) {
            if (Objects.equals(o.getLogId(), logId)) {
                em.remove(o);
                em.flush();
                return true;
            }
        }
        return false;

    }

    public Integer addNewLog(Integer orderId, Integer operatorId, String action, String actionMessage, Date timeStamp) {
        TransportationOrder order = new TransportationOrder();
        
        order = em.find(TransportationOrder.class, orderId);

        TransportationLog log = new TransportationLog();
        
        log.setAction(action);
        log.setActionMessage(actionMessage);
        log.setOperatorId(operatorId);
        log.setTimeStamp(timeStamp);
        log.setTransportationOrdertransportationOrderId(order);
        

        order.getTransportationLogList().add(log);

        em.persist(log);
        em.merge(order);
        em.flush();

        return log.getLogId();

    }

    public Integer editLog(Integer logId, String action, String actionMessage, Date timeStamp) {
        TransportationLog log = new TransportationLog();

        Query query = em.createNamedQuery("TransportationLog.findByLogId").setParameter("logId", logId);
        log = (TransportationLog) query.getSingleResult();

        log.setAction(action);
        log.setActionMessage(actionMessage);
        log.setTimeStamp(timeStamp);

        em.persist(log);
        em.flush();

        return log.getLogId();

    }
}
