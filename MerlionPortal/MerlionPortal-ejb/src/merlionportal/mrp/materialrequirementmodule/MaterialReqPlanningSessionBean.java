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
    MRPList productMRP;
    Mrp mrp;
    ArrayList<Mrp> mrps;

    public List<Mrp> addNewMrpList(Integer productId, Integer mpsId, int wk1Demand, int wk2Demand, int wk3Demand, int wk4Demand, int wk5Demand, int minOnHand) {
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
        productMRP = new MRPList();
        productMRP.setMrpListId(mps.getMpsId());

        productMRP.setMrpDate(new Date());
        productMRP.setProductId(productId);
        productMRP.setMps(mps);
        mrps = new ArrayList<Mrp>();
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
            int orderQuantity = 1000;

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
        return mrps;
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
