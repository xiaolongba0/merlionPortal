/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.mrp.backordermodule;

import entity.ForecastResult;
import entity.Mps;
import entity.Product;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yao
 */
@Stateless
@LocalBean
public class BackorderSessionBean {
 @PersistenceContext
    private EntityManager entityManager;
 
 
     public String getWaitingTime(Integer productId, int quantity){
        //When WMS is done, this method should check stock before computing waitingTime
        
           Product product = entityManager.find(Product.class, productId);
           int waitingTime = 0;
           for(int i=0; i<product.getComponentList().size(); i++){
              if(waitingTime <= product.getComponentList().get(i).getLeadTime()) {
                  waitingTime = product.getComponentList().get(i).getLeadTime();
              }
           }
           int days = waitingTime * 7;
           DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
           Date date = new Date();
           Calendar c = Calendar.getInstance();
           c.setTime(date);
           c.add(Calendar.DATE, days);

           String reportDate = df.format(c.getTime());
           System.out.println("--------------reportDate: " + reportDate);
           return reportDate;
    }
   
}
