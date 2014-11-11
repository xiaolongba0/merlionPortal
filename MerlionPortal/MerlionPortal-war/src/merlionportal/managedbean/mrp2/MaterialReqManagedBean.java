/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.Mrp;
import entity.SystemUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.materialrequirementmodule.MaterialReqPlanningSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "materialReqManagedBean")
@ViewScoped
public class MaterialReqManagedBean {

    @EJB
    MaterialReqPlanningSessionBean materialReqPlanningSessionBean;
   @EJB
    private SystemLogSessionBean systemLogSB;
    
    @EJB
    UserAccountManagementSessionBean uamb;
    private SystemUser loginedUser;
    private Integer companyId;
    private Integer productId;

    private int wk1Demand;
    private int wk2Demand;
    private int wk3Demand;
    private int wk4Demand;
    private int wk5Demand;

   private int quantity;
   private int grossReq1;
   private int grossReq2;
   private int grossReq3;
   private int grossReq4;
   private int grossReq5;

  private  int scheduledRec1;
  private  int scheduledRec2;
   private int scheduledRec3;
   private int scheduledRec4;
   private int scheduledRec5;

   private int plannedRec1;
   private int plannedRec2;
   private int plannedRec3;
   private int plannedRec4;
   private int plannedRec5;
   private int onHand1;
   private int onHand2;
   private int onHand3;
   private int onHand4;
   private int onHand5;
   private int plannedOrder1;
   private int plannedOrder2;
   private int plannedOrder3;
   private int plannedOrder4;
   private int plannedOrder5;
   private int leadTime;

   private Integer mpsId;
   private List<Mrp> mrps;
   private Mrp mrp;
   private List<String> testing;
   private int minOnHand;
   private List<Integer> listOfSentPO;

    @PostConstruct
    public void init() {

        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            if (loginedUser != null) {
                redirect = false;
            }
        }
        if (redirect) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        productId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productId");
        //    quantity = materialReqSessionBean.getComponentsForAProduct(productId).get(0).getQuantity();
        //  System.out.println("!!!!!!!!!!!!getComponentNAme: " + materialReqSessionBean.getComponentsForAProduct(productId).get(0).getComponentName());
        //System.out.println("!!!!!!!!!!!!quantity: " + quantity);

        quantity = 2;

        wk1Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk1Demand");
        wk2Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk2Demand");
        wk3Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk3Demand");
        wk4Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk4Demand");
        wk5Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk5Demand");

        grossReq1 = wk1Demand * quantity;
        grossReq2 = wk2Demand * quantity;
        grossReq3 = wk3Demand * quantity;
        grossReq4 = wk4Demand * quantity;
        grossReq5 = wk5Demand * quantity;

        mpsId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mpsId");
        minOnHand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("minOnHand");
        systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "MRP compute material requirement planning. ");
        mrps = materialReqPlanningSessionBean.addNewMrpList(productId, mpsId, wk1Demand, wk2Demand, wk3Demand, wk4Demand, wk5Demand, minOnHand);
         
        listOfSentPO = new ArrayList<Integer>();
    }

    public MaterialReqManagedBean() {

    }
    
      public String backToMPSResult() {
        return ("mpsresult");
    }
      
      public String proceedToPO(){
           FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mrps", mrps);
           FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listOfSentPO", listOfSentPO);
          return "allpo?faces-redirect=true";
      }
      
      public String doNotToPO(){
          return "mrp?faces-redirect=true";
      }
          

    //For testing passing a list only
    public List<String> testingList() {
        testing.add("111");
        testing.add("222");
        testing.add("333");
        testing.add("444");
        testing.add("555");

        return testing;
    }

    public List<String> getTesting() {
        return testing;
    }


    public int getWk1Demand() {
        return wk1Demand;
    }

    public int getWk2Demand() {
        return wk2Demand;
    }

    public int getWk3Demand() {
        return wk3Demand;
    }

    public int getWk4Demand() {
        return wk4Demand;
    }

    public int getWk5Demand() {
        return wk5Demand;
    }

    public int getGrossReq1() {
        return grossReq1;
    }

    public int getGrossReq2() {
        return grossReq2;
    }

    public int getGrossReq3() {
        return grossReq3;
    }

    public int getGrossReq4() {
        return grossReq4;
    }

    public int getGrossReq5() {
        return grossReq5;
    }

    public Integer getProductId() {
        return productId;
    }

    public int getScheduledRec1() {
        return scheduledRec1;
    }

    public int getScheduledRec2() {
        return scheduledRec2;
    }

    public int getScheduledRec3() {
        return scheduledRec3;
    }

    public int getScheduledRec4() {
        return scheduledRec4;
    }

    public int getScheduledRec5() {
        return scheduledRec5;
    }

    public int getPlannedRec1() {
        return plannedRec1;
    }

    public int getPlannedRec2() {
        return plannedRec2;
    }

    public int getPlannedRec3() {
        return plannedRec3;
    }

    public int getPlannedRec4() {
        return plannedRec4;
    }

    public int getPlannedRec5() {
        return plannedRec5;
    }

    public int getOnHand1() {
        return onHand1;
    }

    public int getOnHand2() {
        return onHand2;
    }

    public int getOnHand3() {
        return onHand3;
    }

    public int getOnHand4() {
        return onHand4;
    }

    public int getOnHand5() {
        return onHand5;
    }

    public int getPlannedOrder1() {
        return plannedOrder1;
    }

    public int getPlannedOrder2() {
        return plannedOrder2;
    }

    public int getPlannedOrder3() {
        return plannedOrder3;
    }

    public int getPlannedOrder4() {
        return plannedOrder4;
    }

    public int getPlannedOrder5() {
        return plannedOrder5;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public Integer getMpsId() {
        return mpsId;
    }

    public List<Mrp> getMrps() {
        return mrps;
    }

    public Mrp getMrp() {
        return mrp;
    }

}
