/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.mrp.mpsmodule;

import entity.ForecastResult;
import entity.MRPList;
import entity.Mps;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class MpsSessionBean {

    @PersistenceContext
    private EntityManager entityManager;
    List<Integer> weeklyDemand;

    //temp
    int endingInv;

    //take in productId (used to check ending inv)
    //take in forecast figure (this month and next month)
    //take in buffer
    public List<Integer> computeMPSDemand(Integer productId, List<Integer> demand, int buffer) {
        //get ending inventory for this product
        //get all quantity fr all storage bean
        // do computation later, now assume ending inventory = 100;
        
        endingInv = 100;
        
        //1. try this month
        
        //2. try no of weeks per month
        
        //3. try segment into weeks
        
        weeklyDemand.add(500);
        weeklyDemand.add(1000);

        return weeklyDemand;
    }
    
    
    public int getCurrentInventory(Integer productId){
        return 100;
    }
    
    public Integer storeMPS(Integer buffer, Integer demand, Integer forecastResultID){
       ForecastResult fResult = entityManager.find(ForecastResult.class, forecastResultID);
       System.out.println("TESTINGGGG:1 " + fResult.getForecastResultId());
       Mps mps = fResult.getMps();
       mps.setBuffer(buffer);
       mps.setDemand(demand);

       MRPList mrplist = new MRPList();
       mps.setMRPList(mrplist);

       entityManager.merge(mps);
       entityManager.flush();
 
       return 1;
    }
    

}
