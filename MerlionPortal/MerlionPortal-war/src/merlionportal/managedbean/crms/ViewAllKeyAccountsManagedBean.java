/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.CustomerAccount;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.accountmanagementmodule.KeyAccountManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewAllKeyAccountsManagedBean")
@ViewScoped
public class ViewAllKeyAccountsManagedBean {

    /**
     * Creates a new instance of ViewAllKeyAccountsManagedBean
     */
    private Integer companyId;
    private Integer userId;
    @EJB
    KeyAccountManagementSessionBean keyAccountMSB;
    private List<CustomerAccount> keyAccounts;
    private CustomerAccount seletedAccount;
    private CustomerAccount filteredAccount;

    private String newRemarks;
    @EJB
    SystemLogSessionBean logSB;

    public ViewAllKeyAccountsManagedBean() {
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
        keyAccounts = keyAccountMSB.viewAllKeyAccounts(companyId);
    }

    public void viewAKeyAccount(CustomerAccount account) {
        seletedAccount = account;
    }

    public void deleteAKeyAccount(CustomerAccount account) {
        boolean result = keyAccountMSB.deleteKeyAccount(companyId, account.getCustomerCompanyId());
        if (result) {
            keyAccounts.remove(account);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Key Account deleted!", "This customer is no longer a key account"));
            logSB.recordSystemLog(userId, "CRMS deleted a key account");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong", ""));

        }
    }

    public void updateAKeyAccount(CustomerAccount account) {
        seletedAccount = account;
    }

    public void updateKeyAccountDetail() {
        boolean result = keyAccountMSB.updateKeyAccount(companyId, seletedAccount.getCustomerCompanyId(), newRemarks);
        if (result) {
            keyAccounts = keyAccountMSB.viewAllKeyAccounts(companyId);
            newRemarks = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Key Account updated!", "Remarks is updated"));
            logSB.recordSystemLog(userId, "updated key account detail");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong", ""));
        }
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

    public List<CustomerAccount> getKeyAccounts() {
        return keyAccounts;
    }

    public void setKeyAccounts(List<CustomerAccount> keyAccounts) {
        this.keyAccounts = keyAccounts;
    }

    public CustomerAccount getFilteredAccount() {
        return filteredAccount;
    }

    public void setFilteredAccount(CustomerAccount filteredAccount) {
        this.filteredAccount = filteredAccount;
    }

    public CustomerAccount getSeletedAccount() {
        return seletedAccount;
    }

    public void setSeletedAccount(CustomerAccount seletedAccount) {
        this.seletedAccount = seletedAccount;
    }

    public String getNewRemarks() {
        return newRemarks;
    }

    public void setNewRemarks(String newRemarks) {
        this.newRemarks = newRemarks;
    }

}
