/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import entity.SystemLog;
import java.io.IOException;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;

/**
 *
 * @author wenxin274
 */
@Named(value = "feedbackManagedBean")
@ViewScoped
public class FeedbackManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    private Integer companyId;
    private Integer userId;

    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

            if (userId != null) {
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
    }
    public void postFeedback(){
        
    }
    public void replyFeedback(){
        
    }
    public void voteFeedback(){
        
    }

    public SystemLogSessionBean getSystemLogSB() {
        return systemLogSB;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setSystemLogSB(SystemLogSessionBean systemLogSB) {
        this.systemLogSB = systemLogSB;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
