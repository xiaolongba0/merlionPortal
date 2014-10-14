/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import entity.SystemLog;
import entity.SystemUser;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "systemLogManagedBean")
@ViewScoped
public class SystemLogManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    private List<SystemLog> allLogList;
    private Integer companyId;
    private Integer userId; 
    private List<SystemLog> filteredLogs;
    private Date startDate;
    private Date endDate;
    private String actionerName;
    private Integer actionerId;

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

        allLogList = systemLogSB.getAllLog();

    }

    public SystemLogManagedBean() {
    }

    public List<SystemLog> getAllLogList() {
        return allLogList;
    }

    public void setAllLogList(List<SystemLog> allLogList) {
        this.allLogList = allLogList;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<SystemLog> getFilteredLogs() {
        return filteredLogs;
    }

    public void setFilteredLogs(List<SystemLog> filteredLogs) {
        this.filteredLogs = filteredLogs;
    }

    public String getCompanyName(SystemLog sLog){
        SystemUser myUser=sLog.getSystemUsersystemUserId();      
        return systemLogSB.getCompanyName(myUser);
    }
    
    public String getActionerName(SystemLog sLog){
        return sLog.getSystemUsersystemUserId().getFirstName()+" "+sLog.getSystemUsersystemUserId().getLastName();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getActionerName() {
        return actionerName;
    }

    public void setActionerName(String actionerName) {
        this.actionerName = actionerName;
    }

    public Integer getActionerId() {
        return actionerId;
    }

    public void setActionerId(Integer actionerId) {
        this.actionerId = actionerId;
    }
    
    public void navigationSearch(){
        if(actionerId!=null){
           allLogList= systemLogSB.searchForLogByUserId(allLogList, userId);
        }
        else if(actionerName!=null){
            allLogList=systemLogSB.searchForLogByListOfUser(allLogList, actionerName);
        }
        if(startDate!=null){
            allLogList = systemLogSB.searchForLogAfter(allLogList, startDate);
        }
        if(startDate!=null){
            allLogList = systemLogSB.searchForLogBefore(allLogList, endDate);
        }
              
    }

}
