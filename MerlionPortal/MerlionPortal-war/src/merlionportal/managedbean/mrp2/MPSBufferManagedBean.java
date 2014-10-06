/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.SystemUser;
import java.io.IOException;
import java.util.Vector;
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
@Named(value = "mPSBufferManagedBean")
@ViewScoped
public class MPSBufferManagedBean {

    @EJB
    MpsSessionBean mpsSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    private SystemUser loginedUser;
    Integer companyId;

    Vector<Integer> forecastData;

    int buffer1;
    int currentInv;
    int requiredAmt1;

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
        requiredAmt1 = forecastData.get(1);
        currentInv = 100; //to be retrieved fr database

    }

    public MPSBufferManagedBean() {
    }

        public int getCurrentInv() {
        return currentInv;
    }
        
    public int getRequiredAmt1() {
        return requiredAmt1;
    }

    public void setBuffer(int buffer1) {
        System.out.println("can set buffer");
        this.buffer1 = buffer1;
    }

    public int getBuffer1() {
        return buffer1;
    }
    
    
    
    public String onBufferChange() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("buffer1", buffer1);
        System.out.println("IF can get to on buffer change!");
        return ("mpsresult");

}
    
        public String backToForecast() {
        return ("forecastresult");
}
    
}


