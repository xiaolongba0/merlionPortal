/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.Contract;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.crms.contractmanagementmodule.ContractManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "renewContractManagedBean")
@ViewScoped
public class RenewContractManagedBean {

    /**
     * Creates a new instance of RenewContractManagedBean
     */
    @EJB
    ContractManagementSessionBean contractManagementSB;

    private Integer companyId;
    private Integer userId;

    //    user input
    private Integer contractId;
    private Date startDate;
    private Date endDate;

    private Contract contract;



    public RenewContractManagedBean() {
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

    public void searchAValidContract() {
        if (contractManagementSB.searchAValidContractToRenew(contractId, companyId) == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Not a valid Contract!", "You can only search for a valid contract"));
            contract = null;
        } else {
            contract = contractManagementSB.searchAValidContractToRenew(contractId, companyId);
        }
    }

    public void renewServiceContract() {
        int result = contractManagementSB.renewContract(contractId, startDate, endDate, userId);
        if (result == 1) {
            this.clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contract is renewed!", "please wait for the other party to review"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops!", "Something went wrong!"));

        }
    }

    private void clearAllFields() {
        startDate = null;
        endDate = null;
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

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
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

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

}
