/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.mrp.materialrequirementmodule;

import entity.MRPList;
import entity.Mps;
import entity.Mrp;
import entity.Product;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    Mrp mrp;
    

    public List<Mrp> addNewMrpList(Integer productId, Integer mpsId, int wk1Demand, int wk2Demand, int wk3Demand, int wk4Demand, int wk5Demand, int minOnHand) {
        
          System.out.println("@@@@@@@@@@@@@@@@MPS id" + mpsId);
          System.out.println("@@@@@@@@@@@@@@@@product id" + productId);
        /*    Mps mps = entityManager.find(Mps.class, mpsId);
         productMRP = mps.getMRPList();
         productMRP.setMrpDate(new Date());
         productMRP.setProductId(productId);
         mrps = new ArrayList<Mrp>();
         productMRP.setMrpList(mrps);
         entityManager.merge(productMRP);
         entityManager.merge(mps);
         entityManager.flush();*/
        Mps mps = entityManager.find(Mps.class, mpsId);
        MRPList productMRP = new MRPList();
        productMRP.setMrpListId(mpsId);

        productMRP.setMrpDate(new Date());
        productMRP.setProductId(productId);
        productMRP.setMps(mps);
        ArrayList<Mrp> mrps = new ArrayList<Mrp>();
        productMRP.setMrpList(mrps);
        entityManager.persist(productMRP);
        mps.setMRPList(productMRP);
        entityManager.merge(mps);
        entityManager.flush();

        //draw out lead time
        product = entityManager.find(Product.class, productId);
        int size = product.getComponentList().size();
        for (int i = 0; i < size; i++) {
            mrp = new Mrp();

            //attribute
            int GrossReq1;
            int GrossReq2;
            int GrossReq3;
            int GrossReq4;
            int GrossReq5;
            int scheduledRec1;
            int scheduledRec2;
            int scheduledRec3;
            int scheduledRec4;
            int scheduledRec5;
            int plannedRec1;
            int plannedRec2;
            int plannedRec3;
            int plannedRec4;
            int plannedRec5;
            int onHand1;
            int onHand2;
            int onHand3;
            int onHand4;
            int onHand5;
            int plannedOrder1;
            int plannedOrder2;
            int plannedOrder3;
            int plannedOrder4;
            int plannedOrder5;
            int leadTime;
            int initialOnHand;
            int minOnHandtemp;
            int orderQuantity;
            System.out.println("##########@@@@@@@@@@@@@@@@@"+ product.getComponentList().get(i).getOrderQuantity());
            orderQuantity = product.getComponentList().get(i).getOrderQuantity();
            

            minOnHandtemp = minOnHand * product.getComponentList().get(i).getQuantity();
            GrossReq1 = product.getComponentList().get(i).getQuantity() * wk1Demand;
            GrossReq2 = product.getComponentList().get(i).getQuantity() * wk2Demand;
            GrossReq3 = product.getComponentList().get(i).getQuantity() * wk3Demand;
            GrossReq4 = product.getComponentList().get(i).getQuantity() * wk4Demand;
            GrossReq5 = product.getComponentList().get(i).getQuantity() * wk5Demand;
            leadTime = product.getComponentList().get(i).getLeadTime();
            //draw last month's history
            if (leadTime > 0) {
                scheduledRec1 = 2000;
            } else {
                scheduledRec1 = 0;
            }
            if (leadTime > 1) {
                scheduledRec2 = 2000;
            } else {
                scheduledRec2 = 0;
            }
            if (leadTime > 2) {
                scheduledRec3 = 2000;
            } else {
                scheduledRec3 = 0;
            }
            if (leadTime > 3) {
                scheduledRec4 = 2000;
            } else {
                scheduledRec4 = 0;
            }
            if (leadTime > 4) {
                scheduledRec5 = 2000;
            } else {
                scheduledRec5 = 0;
            }

            initialOnHand = 1000;
            if (leadTime > 0) {
                plannedRec1 = 0;
                onHand1 = scheduledRec1 + initialOnHand - GrossReq1;
            } else {
                plannedRec1 = 0;
                onHand1 = initialOnHand - GrossReq1;
                while (onHand1 < minOnHandtemp) {
                    plannedRec1 = plannedRec1 + orderQuantity;
                    onHand1 = onHand1 + orderQuantity;
                }

            }

            if (leadTime > 1) {
                plannedRec2 = 0;
                onHand2 = scheduledRec2 + onHand1 - GrossReq2;
            } else {

                plannedRec2 = 0;
                onHand2 = onHand1 - GrossReq2;
                while (onHand2 < minOnHandtemp) {
                    plannedRec2 = plannedRec2 + orderQuantity;
                    onHand2 = onHand2 + orderQuantity;
                }
            }

            if (leadTime > 2) {
                plannedRec3 = 0;
                onHand3 = scheduledRec3 + onHand2 - GrossReq3;
            } else {

                plannedRec3 = 0;
                onHand3 = onHand2 - GrossReq3;
                while (onHand3 < minOnHandtemp) {
                    plannedRec3 = plannedRec3 + orderQuantity;
                    onHand3 = onHand3 + orderQuantity;
                }
            }

            if (leadTime > 3) {
                plannedRec4 = 0;
                onHand4 = scheduledRec4 + onHand3 - GrossReq4;
            } else {
                plannedRec4 = 0;
                onHand4 = onHand3 - GrossReq4;
                while (onHand4 < minOnHandtemp) {
                    plannedRec4 = plannedRec4 + orderQuantity;
                    onHand4 = onHand4 + orderQuantity;
                }

            }

            if (leadTime > 4) {
                plannedRec5 = 0;
                onHand5 = scheduledRec5 + onHand4 - GrossReq5;
            } else {
                plannedRec5 = 0;
                onHand5 = onHand4 - GrossReq5;
                while (onHand5 < minOnHandtemp) {
                    plannedRec5 = plannedRec5 + orderQuantity;
                    onHand5 = onHand5 + orderQuantity;
                }

            }

            if (leadTime == 0) {
                plannedOrder1 = plannedRec1;
                plannedOrder2 = plannedRec2;
                plannedOrder3 = plannedRec3;
                plannedOrder4 = plannedRec4;
                plannedOrder5 = plannedRec5;
            } else if (leadTime == 1) {
                plannedOrder1 = plannedRec2;
                plannedOrder2 = plannedRec3;
                plannedOrder3 = plannedRec4;
                plannedOrder4 = plannedRec5;
                plannedOrder5 = 0;
            } else if (leadTime == 2) {
                plannedOrder1 = plannedRec3;
                plannedOrder2 = plannedRec4;
                plannedOrder3 = plannedRec5;
                plannedOrder4 = 0;
                plannedOrder5 = 0;
            } else if (leadTime == 3) {
                plannedOrder1 = plannedRec4;
                plannedOrder2 = plannedRec5;
                plannedOrder3 = 0;
                plannedOrder4 = 0;
                plannedOrder5 = 0;
            } else if (leadTime == 4) {
                plannedOrder1 = plannedRec5;
                plannedOrder2 = 0;
                plannedOrder3 = 0;
                plannedOrder4 = 0;
                plannedOrder5 = 0;
            } else {
                plannedOrder1 = 0;
                plannedOrder2 = 0;
                plannedOrder3 = 0;
                plannedOrder4 = 0;
                plannedOrder5 = 0;
            }

            mrp.setComponentId(product.getComponentList().get(i).getComponentId());
            mrp.setComponentName(product.getComponentList().get(i).getComponentName());
            mrp.setOrderQuantity(product.getComponentList().get(i).getOrderQuantity());
            mrp.setOnHand0(initialOnHand);

            mrp.setGrossReq1(GrossReq1);
            mrp.setGrossReq2(GrossReq2);
            mrp.setGrossReq3(GrossReq3);
            mrp.setGrossReq4(GrossReq4);
            mrp.setGrossReq5(GrossReq5);
            mrp.setScheduledRec1(scheduledRec1); //draw from database
            mrp.setScheduledRec2(scheduledRec2);
            mrp.setScheduledRec3(scheduledRec3);
            mrp.setScheduledRec4(scheduledRec4);
            mrp.setScheduledRec5(scheduledRec5);
            mrp.setPlannedRec1(plannedRec1);
            mrp.setPlannedRec2(plannedRec2);
            mrp.setPlannedRec3(plannedRec3);
            mrp.setPlannedRec4(plannedRec4);
            mrp.setPlannedRec5(plannedRec5);
            mrp.setOnHand1(onHand1);
            mrp.setOnHand2(onHand2);
            mrp.setOnHand3(onHand3);
            mrp.setOnHand4(onHand4);
            mrp.setOnHand5(onHand5);
            mrp.setPlannedOrder1(plannedOrder1);
            mrp.setPlannedOrder2(plannedOrder2);
            mrp.setPlannedOrder3(plannedOrder3);
            mrp.setPlannedOrder4(plannedOrder4);
            mrp.setPlannedOrder5(plannedOrder5);
            mrp.setLeadTime(leadTime);
            mrp.setMrpList(productMRP);
            entityManager.persist(mrp);
            entityManager.flush();
            mrps.add(mrp);

        }

        productMRP.setMrpList(mrps);
        entityManager.merge(productMRP);
        entityManager.flush();
        
        System.out.println("FY 5NOV:mrpListId in Mrp sesson bean: " + productMRP.getMrpListId());
        return mrps;
    }

  /* public List<Mrp> addNewMrpListBackorder(Integer productId, Integer demand) {

        MRPList mrpList = new MRPList();
        mrpList.setMrpDate(new Date());
        mrpList.setProductId(productId);
       //test if can dun add
        //   Mps mps = new Mps();
        //   productMRP.setMps(mps);

        mrps = new ArrayList<Mrp>();
        MRPList productMRP1 = new MRPList();
        productMRP1.setMrpList(mrps);
        entityManager.persist(productMRP1);
    //    mps.setMRPList(productMRP);
        //  entityManager.merge(mps);
        //   entityManager.flush();

        int waitingTime = 0;
        for (int i = 0; i < product.getComponentList().size(); i++) {
            if (waitingTime <= product.getComponentList().get(i).getLeadTime()) {
                waitingTime = product.getComponentList().get(i).getLeadTime();
            }
        }

        //draw out lead time
        product = entityManager.find(Product.class, productId);
        int size = product.getComponentList().size();
        for (int i = 0; i < size; i++) {
            mrp = new Mrp();

            //attribute
            int GrossReq1 = demand * product.getComponentList().get(i).getQuantity();
            int GrossReq2 = 0;
            int GrossReq3 = 0;
            int GrossReq4 = 0;
            int GrossReq5 = 0;
            int scheduledRec1 = 0;
            int scheduledRec2 = 0;
            int scheduledRec3 = 0;
            int scheduledRec4 = 0;
            int scheduledRec5 = 0;
            int plannedRec1 = 0;
            int plannedRec2 = 0;
            int plannedRec3 = 0;
            int plannedRec4 = 0;
            int plannedRec5 = 0;
            int onHand1 = 0;
            int onHand2 = 0;
            int onHand3 = 0;
            int onHand4 = 0;
            int onHand5 = 0;
            int plannedOrder1 = 0;
            int plannedOrder2 = 0;
            int plannedOrder3 = 0;
            int plannedOrder4 = 0;
            int plannedOrder5 = 0;
            while (product.getComponentList().get(i).getOrderQuantity() >= GrossReq1) {
                plannedOrder1 = plannedOrder1 + product.getComponentList().get(i).getOrderQuantity();
            }

            int leadTime = product.getComponentList().get(i).getLeadTime();

            mrp.setComponentId(product.getComponentList().get(i).getComponentId());
            mrp.setComponentName(product.getComponentList().get(i).getComponentName());
            mrp.setOrderQuantity(product.getComponentList().get(i).getOrderQuantity());
            mrp.setOnHand0(0);

            mrp.setGrossReq1(GrossReq1);
            mrp.setGrossReq2(GrossReq2);
            mrp.setGrossReq3(GrossReq3);
            mrp.setGrossReq4(GrossReq4);
            mrp.setGrossReq5(GrossReq5);
            mrp.setScheduledRec1(scheduledRec1); //draw from database
            mrp.setScheduledRec2(scheduledRec2);
            mrp.setScheduledRec3(scheduledRec3);
            mrp.setScheduledRec4(scheduledRec4);
            mrp.setScheduledRec5(scheduledRec5);
            mrp.setPlannedRec1(plannedRec1);
            mrp.setPlannedRec2(plannedRec2);
            mrp.setPlannedRec3(plannedRec3);
            mrp.setPlannedRec4(plannedRec4);
            mrp.setPlannedRec5(plannedRec5);
            mrp.setOnHand1(onHand1);
            mrp.setOnHand2(onHand2);
            mrp.setOnHand3(onHand3);
            mrp.setOnHand4(onHand4);
            mrp.setOnHand5(onHand5);
            mrp.setPlannedOrder1(plannedOrder1);
            mrp.setPlannedOrder2(plannedOrder2);
            mrp.setPlannedOrder3(plannedOrder3);
            mrp.setPlannedOrder4(plannedOrder4);
            mrp.setPlannedOrder5(plannedOrder5);
            mrp.setLeadTime(leadTime);
            mrp.setMrpList(productMRP1);
            entityManager.persist(mrp);
            entityManager.flush();
            mrps.add(mrp);

        }

        productMRP1.setMrpList(mrps);
        entityManager.merge(productMRP1);
        entityManager.flush();
        return mrps;
    }*/

}
