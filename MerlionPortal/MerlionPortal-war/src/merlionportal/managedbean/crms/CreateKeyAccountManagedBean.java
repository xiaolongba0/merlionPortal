/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.CustomerAccount;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.accountmanagementmodule.KeyAccountManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "createKeyAccountManagedBean")
@ViewScoped
public class CreateKeyAccountManagedBean {

    /**
     * Creates a new instance of CreateKeyAccountManagedBean
     */
    private Integer companyId;
    private Integer userId;

    //User inputs
    private Integer customerId;
    private String remarks;
    private String companyName;

    @EJB
    KeyAccountManagementSessionBean keyAccountMSB;
    @EJB
    UserAccountManagementSessionBean uamsb;
    @EJB
    SystemLogSessionBean logSB;

    public CreateKeyAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
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

    public void searchAValidCustomer() {
        if (keyAccountMSB.searchAValidCustomer(customerId, companyId) == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Not a valid Customer!", "You can only search for a valid customer"));
        } else {
            CustomerAccount customerAccount = keyAccountMSB.searchAValidCustomer(customerId, companyId);
            if (customerAccount.getKeyAccount()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "This Customer is currently a key account holder!", ""));

            } else {
                //Fill in all fields with customer data
                companyName = uamsb.getCompany(customerId).getName();

            }
        }
    }

    public void createKeyAccount() {
        int result = keyAccountMSB.createKeyAccount(companyId, customerId, remarks);
        if (result == 1) {
            this.clearAllFields();
            logSB.recordSystemLog(userId, "CRMS created a key account");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Key Account created!", "This customer is marked as key account"));
        } else if (result == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "This Customer is already a key account holder!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Something went wrong", ""));
        }
    }
    
    private void clearAllFields(){
        remarks = null;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
