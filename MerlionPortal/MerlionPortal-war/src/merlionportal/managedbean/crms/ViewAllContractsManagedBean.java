/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.Contract;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "viewAllContractsManagedBean")
@ViewScoped
public class ViewAllContractsManagedBean {

    /**
     * Creates a new instance of ViewAllContractsManagedBean
     */
    @EJB
    ContractManagementSessionBean contractManagementSB;
 
    private Integer companyId;
    private Integer userId;

    private List<Contract> sentContracts;
    private List<Contract> receivedContracts;
    private List<Contract> filteredContracts;

    private Contract selectedSentContract;
    private Contract selectedReceivedContract;

    private List<String> status;
    private String statusNumber;

    public ViewAllContractsManagedBean() {
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
        sentContracts = (List<Contract>) contractManagementSB.viewSentContracts(companyId);
        receivedContracts = (List<Contract>) contractManagementSB.viewReceivedContracts(companyId);

        status = new ArrayList<>();
        status.add("Initial contract");
        status.add("Request to modify");
        status.add("Waiting for review");
        status.add("Waiting to be signed");
        status.add("Valid");

    }

    public String viewSentContract() {
        if (selectedSentContract != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedContract");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedContract", selectedSentContract);
            return "viewcontractdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a contract to view!", ""));
            return null;
        }
    }

    public String viewReceivedContract() {
        if (selectedReceivedContract != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedContract");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedContract", selectedReceivedContract);
            return "viewcontractdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(":form:msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a contract to view!", ""));
            return null;
        }
    }

    public String getStatusNumber(int passedStatus) {
        if (passedStatus == 1) {
            statusNumber = "Initial contract";
        }
        if (passedStatus == 2) {
            statusNumber = "Request to modify";
        }
        if (passedStatus == 3) {
            statusNumber = "Waiting for review";
        }
        if (passedStatus == 4) {
            statusNumber = "Waiting to be signed";
        }
        if (passedStatus == 5) {
            statusNumber = "Valid";
        }

        return statusNumber;
    }

    public void setStatusNumber(String statusNumber) {
        this.statusNumber = statusNumber;
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

    public List<Contract> getSentContracts() {
        return sentContracts;
    }

    public void setSentContracts(List<Contract> sentContracts) {
        this.sentContracts = sentContracts;
    }

    public List<Contract> getReceivedContracts() {
        return receivedContracts;
    }

    public void setReceivedContracts(List<Contract> receivedContracts) {
        this.receivedContracts = receivedContracts;
    }

    public List<Contract> getFilteredContracts() {
        return filteredContracts;
    }

    public void setFilteredContracts(List<Contract> filteredContracts) {
        this.filteredContracts = filteredContracts;
    }

    public Contract getSelectedSentContract() {
        return selectedSentContract;
    }

    public void setSelectedSentContract(Contract selectedSentContract) {
        this.selectedSentContract = selectedSentContract;
    }

    public Contract getSelectedReceivedContract() {
        return selectedReceivedContract;
    }

    public void setSelectedReceivedContract(Contract selectedReceivedContract) {
        this.selectedReceivedContract = selectedReceivedContract;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

}
