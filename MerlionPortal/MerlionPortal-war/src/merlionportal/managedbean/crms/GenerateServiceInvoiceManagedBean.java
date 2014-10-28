/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServicePO;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.ordermanagementmodule.POProcessingManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "generateServiceInvoiceManagedBean")
@ViewScoped
public class GenerateServiceInvoiceManagedBean {

    /**
     * Creates a new instance of GenerateServiceInvoiceManagedBean
     */
    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    POProcessingManagementSessionBean poProcessSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

    private ServicePO selectedServicePO;
    private String senderCompanyName;
    private String receiverCompanyName;

    private String conditionText;
    private String status;

    public GenerateServiceInvoiceManagedBean() {
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
        selectedServicePO = (ServicePO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedServicePO");

        senderCompanyName = userAccountSB.getCompany(selectedServicePO.getReceiverCompanyId()).getName();
        receiverCompanyName = userAccountSB.getCompany(selectedServicePO.getSenderCompanyId()).getName();
        this.getStatusText(selectedServicePO.getStatus());

    }

    public void createInvoice() {
        int result = poProcessSB.generateInvoice(selectedServicePO.getServicePOId(), userId, conditionText);
        if (result == 1) {
            conditionText = null;
            this.getStatusText(7);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Invoice is generated", ""));
            logSB.recordSystemLog(userId, "CRMS created a  service invoice");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    private void getStatusText(int statusNumber) {

        if (statusNumber == 1) {
            status = "PO Waiting to be processed";
        }
        if (statusNumber == 2) {
            status = "PO Deleted";
        }
        if (statusNumber == 3) {
            status = "PO Hold";
        }
        if (statusNumber == 4) {
            status = "PO Rejected";
        }
        if (statusNumber == 5) {
            status = "SO Waiting for fulfillment";
        }
        if (statusNumber == 6) {
            status = "SO Fulfilled";
        }
        if (statusNumber == 7) {
            status = "SO Invoiced";
        }
        if (statusNumber == 8) {
            status = "SO Closed";
        }
        if (statusNumber == 9) {
            status = "Transportation SO in transit";
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

    public ServicePO getSelectedServicePO() {
        return selectedServicePO;
    }

    public void setSelectedServicePO(ServicePO selectedServicePO) {
        this.selectedServicePO = selectedServicePO;
    }

    public String getSenderCompanyName() {
        return senderCompanyName;
    }

    public void setSenderCompanyName(String senderCompanyName) {
        this.senderCompanyName = senderCompanyName;
    }

    public String getReceiverCompanyName() {
        return receiverCompanyName;
    }

    public void setReceiverCompanyName(String receiverCompanyName) {
        this.receiverCompanyName = receiverCompanyName;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
