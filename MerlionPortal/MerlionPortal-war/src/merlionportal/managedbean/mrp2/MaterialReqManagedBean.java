/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.SystemUser;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.mpsmodule.MpsSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "materialReqManagedBean")
@ViewScoped
public class MaterialReqManagedBean {

    @EJB
    MpsSessionBean mpsSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    private SystemUser loginedUser;
    Integer companyId;

    int wk1Demand;
    int wk2Demand;
    int wk3Demand;
    int wk4Demand;
    int wk5Demand;
    
    List<String> testing;

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

        wk1Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk1Demand");
        wk2Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk2Demand");
        wk3Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk3Demand");
        wk4Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk4Demand");
        wk5Demand = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("wk5Demand");

    }

    public MaterialReqManagedBean() {
    }
    
    //For testing passing a list only
    public List<String> testingList(){
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
    
    
    
    
    
    
    

    public String backToMPSResult() {
        return ("mpsresult");
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
    
    
    

}
