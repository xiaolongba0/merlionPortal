/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServicePO;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.crms.ordermanagementmodule.ServicePOManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewServicePODetailManagedBean")
@ViewScoped
public class ViewServicePODetailManagedBean {

    /**
     * Creates a new instance of ViewServicePODetailManagedBean
     */
    @EJB
    ServicePOManagementSessionBean servicePOSB;
    @EJB
    UserAccountManagementSessionBean userAccountSB;

    private Integer companyId;
    private Integer userId;

    private Date deliveryDate;
    private Date serviceStartDate;
    private Date serviceEndDate;
    private Integer volume;
    private Double price;
    private Integer volume2;

    private ServicePO selectedServicePO;
    private String status;
    private Date today;
    private String senderCompanyName;
    private String receiverCompanyName;

    public ViewServicePODetailManagedBean() {
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
        today = new Date();
        if (selectedServicePO != null) {
            this.getStatusText(selectedServicePO.getStatus());
            deliveryDate = selectedServicePO.getServiceDeliveryDate();
            serviceStartDate = selectedServicePO.getServiceStartDate();
            serviceEndDate = selectedServicePO.getServiceEndDate();
            volume = selectedServicePO.getVolume();
            price = selectedServicePO.getPrice();
            senderCompanyName = userAccountSB.getCompany(selectedServicePO.getSenderCompanyId()).getName();
            receiverCompanyName = userAccountSB.getCompany(selectedServicePO.getReceiverCompanyId()).getName();
        }

    }

    public void updatePO() {
        //Need to check status 
        if (selectedServicePO != null) {
            
            int result = servicePOSB.updateServicePO(selectedServicePO.getServicePOId(), deliveryDate, serviceStartDate, serviceEndDate, volume2, userId);
            if (result == 1) {
                volume = volume2;
                price = (volume2 * selectedServicePO.getContract().getPrice());

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is updated", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong, please go back to previous page"));
        }
    }

    public void deletePO() {
        //Need to check status
        if (selectedServicePO != null) {
            if (selectedServicePO.getStatus() == 1) {
                int result = servicePOSB.deleteServicePO(selectedServicePO.getServicePOId(), companyId);
                if (result == 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is marked as deleted", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "PO cannot be deleted", "It is processed by service provider, if you still wish to delete, please contact service provider directly"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong, please go back to previous page"));
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(Date serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public Date getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(Date serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getVolume2() {
        return volume2;
    }

    public void setVolume2(Integer volume2) {
        this.volume2 = volume2;
    }

}
