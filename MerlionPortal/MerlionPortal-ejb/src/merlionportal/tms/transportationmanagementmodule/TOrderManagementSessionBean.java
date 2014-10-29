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
import entity.TransportationOrder;
import java.util.ArrayList;
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
    
    private String cargoType;
    private Integer cargoWeight;
    private Integer companyId;
    private Integer creatorId;
    private String destination;
    private String origin;
    private Integer referenceId;
    private String  referenceType;
    private Date timeStart;
    private Date timeEnd;
    
    private String action;
    private String actionMessage;
    private Integer logId;
    private Integer OperatorId;
    private Date timeStamp;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public List<TransportationOrder> viewTOrderForCompany(Integer companyId){
        List<TransportationOrder> allMyTOrder = new ArrayList();
        System.out.println("========view transportation order for company:" + companyId);
        
        Query query = em.createNamedQuery("TransportationOrder.findByCompanyId").setParameter("companyId", companyId);
        allMyTOrder = query.getResultList();
            
        return allMyTOrder;
    
    }
    
    public boolean deleteTransportationOrder(Integer companyId, Integer orderId){
        System.out.println("========Delete Transportation Order Id"+ orderId +"of company Id"+ companyId);
        Query query = em.createNamedQuery("TransportationOrder.findByCompanyId").setParameter("companyId", companyId);
        List<TransportationOrder> allMyOrders = query.getResultList();
        
        for(TransportationOrder o : allMyOrders){
            if(Objects.equals(o.getTransportationOrderId(), orderId)){
                em.remove(o);
                em.flush();
                return true;
            }
        }
        return false;
        
    }
//    public Integer AddNewTransportationOrder(String cargoType, Integer cargoWeight, Integer companyId, Integer creatorId, String destination, String orgin, Integer referenceId, String referenceType, Date startDate) {
//        
//    }
//    public Integer editTransportationOrder(){
//        
//    }
}
