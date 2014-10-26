/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.SystemUser;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.materialrequirementmodule.MaterialReqPlanningSessionBean;
import merlionportal.mrp.mpsmodule.MpsSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "mPSBufferManagedBean")
@ViewScoped
public class MPSBufferManagedBean {
  
 
    @EJB
    MpsSessionBean mpsSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    private SystemUser loginedUser;
    Integer companyId;

    List<Integer> weeklyDemand;
    Vector<Integer> forecastData;
    Vector<String> forecastDate;

    
    int thisMonthDemand = 4000;
    int buffer;
    int currentInv;
    int requiredDemand;
    int requiredAmt1;
    
    Integer productId;

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

        forecastData = (Vector<Integer>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("forecastR");
        forecastDate = (Vector<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("monthlyDateR");
        productId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productId");
        computeDemand(); 
    }

    public MPSBufferManagedBean() {
    }

 
    
    public void computeDemand() {
        requiredAmt1 = forecastData.get(1);
        
        currentInv = mpsSessionBean.getCurrentInventory(productId);
        requiredDemand = requiredAmt1 - currentInv;
        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFff");
        System.out.println("currentInv  " + currentInv);
        System.out.println("requiredDemand " + requiredDemand);
    }

    //intake string to display
    public void computeDate() {

    }

 /*   public void onBufferChange() {
        if (buffer != 0) {
            requiredDemand = requiredAmt1 - currentInv + buffer;
            System.out.println("RRRRRRRRRRRRRRR");
        }
    }*/

    public int getThisMonthDemand() {
        return thisMonthDemand;
    }


    public int getRequiredAmt1() {
        return requiredAmt1;
    }

   
    public void setBuffer(int buffer) {
        this.buffer = buffer;
        
    }

    public void setCurrentInv(int currentInv) {
        this.currentInv = currentInv;
    }

    public int getBuffer() {
        return buffer;
    }

    public int getCurrentInv() {
        return currentInv;
    }

    public void setRequiredDemand(int requiredDemand) {
        this.requiredDemand = requiredDemand;
    }

    public int getRequiredDemand() {
        return requiredDemand;
    }

    public String backToHistory() {
        return ("forecastresult");
    }

    public String proceedToMPSResult() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("buffer", buffer);

        return ("mpsresult");
    }

}
