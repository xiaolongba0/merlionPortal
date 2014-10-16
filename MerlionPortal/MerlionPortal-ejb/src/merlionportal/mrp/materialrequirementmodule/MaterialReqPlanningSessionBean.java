/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.mrp.materialrequirementmodule;

import entity.MRPList;
import entity.Mrp;
import entity.Product;
import java.util.ArrayList;
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
public class MaterialReqPlanningSessionBean {
    
    @PersistenceContext
    private EntityManager entityManager;

    private Product product;
    MRPList productMRP;
    Mrp mrp;
    ArrayList<Mrp> mrps;

    public void addNewMrpList(Integer productId) {
        productMRP = new MRPList();
        productMRP.setMrpDate(new Date());
        productMRP.setProductId(productId);
        mrps = new ArrayList<Mrp>();
        productMRP.setMrpList(mrps);
        entityManager.persist(productMRP);
        entityManager.flush();

    }

    /*      public void addNewMrpList(Integer productId) {
     productMRP = new MRPList();
     productMRP.setMrpDate(new Date());
     productMRP.setProductId(productId);

     mrp = new Mrp();
     mrp.setGrossReq1(55);
     mrp.setGrossReq2(55);
     mrp.setGrossReq3(55);
     mrp.setGrossReq4(55);
     mrp.setGrossReq5(55);
     mrp.setScheduledRec1(55);
     mrp.setScheduledRec2(55);
     mrp.setScheduledRec3(55);
     mrp.setScheduledRec4(55);
     mrp.setScheduledRec5(55);
     mrp.setPlannedRec1(55);
     mrp.setPlannedRec2(55);
     mrp.setPlannedRec3(55);
     mrp.setPlannedRec4(55);
     mrp.setPlannedRec5(55);
     mrp.setOnHand1(55);
     mrp.setOnHand2(55);
     mrp.setOnHand3(55);
     mrp.setOnHand4(55);
     mrp.setOnHand5(55);
     mrp.setPlannedOrder1(55);
     mrp.setPlannedOrder2(55);
     mrp.setPlannedOrder3(55);
     mrp.setPlannedOrder4(55);
     mrp.setPlannedOrder5(55);
     mrp.setLeadTime(5);
     mrp.setMrpList(productMRP);
     System.out.println("!!!!!!!!!!!!!!CAN");
     entityManager.persist(mrp);
     entityManager.flush();
     System.out.println("!!!!!!!!!!!!!!CAN");

     mrps.add(mrp);
     productMRP.setMrpList(mrps);
     entityManager.persist(productMRP);
     entityManager.flush();

     }  */
}
