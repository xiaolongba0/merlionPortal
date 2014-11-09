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

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class RouteOptimizationSessionBean {
    
    private EntityManager em;
    
    public boolean checkConnectivity (Integer originId, Integer destinationId,String Type){
        return false;
    }
    public List<String> checkTransportationType (Integer originId, Integer destinationId){
        List<String> methods = new ArrayList();
        return methods;
    }
    
    public boolean checkAvailability(Integer originId, Integer destinationId){
        return false;
    }
    
    
    public List<Route> routeOptimize (Integer originId, Integer destinationId){
        List<Route> routes = new ArrayList();
        return routes;  
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
