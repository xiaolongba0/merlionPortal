/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import java.io.IOException;
import merlionportal.utility.MD5Generator;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.LoginSessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;

@ManagedBean
@Named(value = "loginBean")
@ViewScoped
public class LoginBean {

    private String username;
    private String password;
    
    private boolean locked;
    private boolean needsReset;
    
    @EJB
    LoginSessionBean loginSessionBean;
    @EJB
    UserAccountManagementSessionBean uamsb;


    public void login(ActionEvent event) {

        int tries = 0;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("logintries")) {

            tries = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("logintries");
            if (tries > 5) {
                //Reset tries after 30 minutes
                Long startTry = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("firstLoginTry");
                startTry += 1800000; //30 minutes
                Long nowDate = new Date().getTime();
                if (nowDate > startTry) {
                    tries = 0;
                }
            }
        }
        
        if (username != null) {
            if (password != null) {
                if (tries <= 5) {
                    HashMap<String, Integer> sessionMap = loginSessionBean.verifyAccount(username, MD5Generator.hash(password));
                    if (sessionMap == null) {
                        //Login failed
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong username/password!", "Please try again"));
                        if (tries == 0) {
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("firstLoginTry", new Date().getTime());
                        }
                        tries += 1;
                        //Set number of tries
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("logintries", tries);
                    } else {
                        //Login Successful
                        tries = 0;
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("logintries", tries);
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", sessionMap.get("userId"));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("companyId", sessionMap.get("companyId"));
                        try {
                            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/admin/dashboard.xhtml");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    //Sorry you have tried more than 5 times...
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorry you have try more than 5 times!", "Please wait for 30 minutes before trying again."));
                }
            }
        }
    }

    public void checkLocked(){
        locked = uamsb.checkLocked((int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
    }
    
    public void checkResetPassword(){
        needsReset = uamsb.checkResetPasswordUponLogin((int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
    }
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isNeedsReset() {
        return needsReset;
    }

    public void setNeedsReset(boolean needsReset) {
        this.needsReset = needsReset;
    }
//</editor-fold>

    
}
