/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceQuotation;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.contractmanagementmodule.ContractManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "createServiceContractManagedBean")
@ViewScoped
public class CreateServiceContractManagedBean {

    /**
     * Creates a new instance of CreateServiceContractManagedBean
     */
    @EJB
    ContractManagementSessionBean contractManagementSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

//    user input
    private Integer quotationId;
    private String conditionText;
    private String serviceType;

    private ServiceQuotation quotation;

//    contract information fields
    private String partyAName;
    private String partyBName;

    public CreateServiceContractManagedBean() {
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

    public void createServiceContract() {
        if (serviceType.equals("Transportation")) {

            int result = contractManagementSB.createTransportationServiceContract(quotationId, conditionText, userId);
            if (result > 0) {
                logSB.recordSystemLog(userId, "CRMS created a transportation service contract");

                this.clearAllFields();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Transportation Service Contract created!", ""));

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

            }
        } else {
            int result = contractManagementSB.createWarehouseServiceContract(quotationId, conditionText, userId);
            if (result > 0) {
                this.clearAllFields();
                logSB.recordSystemLog(userId, "CRMS created a warehouse service contract");

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Warehouse Service Contract created!", ""));

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

            }
        }
    }

    public void searchAValidQuotation() {
        if (contractManagementSB.searchAValidQuotation(quotationId, companyId) == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Not a valid Quotation!", "You can only search for a valid quotation"));
            quotation = null;

        } else if (contractManagementSB.hasDuplicateContract(quotationId)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Contract Exist!", "Contract Exsit for this quotation, if you wish to renew contract, please go to renew contract page"));
            quotation = null;
        } else {
            quotation = contractManagementSB.searchAValidQuotation(quotationId, companyId);
            partyAName = contractManagementSB.retrieveCompanyName(quotation.getReceiverCompanyId());
            partyBName = contractManagementSB.retrieveCompanyName(quotation.getSenderCompanyId());

            if (quotation.getServiceType().equals("Transportation")) {
                serviceType = "Transportation";
//                fill in all fields necessary for transportation contract
            } else {
                serviceType = "Warehouse";
//                fill in all fields necessary for warehouse contract
            }

        }
    }

    private void clearAllFields() {
        conditionText = null;
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

    public Integer getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Integer quotationId) {
        this.quotationId = quotationId;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceQuotation getQuotation() {
        return quotation;
    }

    public void setQuotation(ServiceQuotation quotation) {
        this.quotation = quotation;
    }

    public String getPartyAName() {
        return partyAName;
    }

    public void setPartyAName(String partyAName) {
        this.partyAName = partyAName;
    }

    public String getPartyBName() {
        return partyBName;
    }

    public void setPartyBName(String partyBName) {
        this.partyBName = partyBName;
    }

}
