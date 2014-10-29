/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

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
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.ordermanagementmodule.POProcessingManagementSessionBean;
import merlionportal.crms.ordermanagementmodule.ServicePOManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewTransportationRequestDetailMB")
@ViewScoped
public class ViewTransportationRequestDetailManagedBean {

    /**
     * Creates a new instance of ViewTransportationRequestDetailManagedBean
     */
    @EJB
    ServicePOManagementSessionBean servicePOSB;
    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    POProcessingManagementSessionBean poProcessingSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

    private Date deliveryDate;
    private Date serviceStartDate;
    private Date serviceEndDate;
    private Integer volume;
    private Double price;
    private Integer volume2;
    private Integer productId;
    private Integer productQuantityPerTEU;
    private Integer productId2;
    private Integer productQuantityPerTEU2;

    private ServicePO selectedTServicePO;
    private String status;
    private Date today;
    private String senderCompanyName;
    private String receiverCompanyName;

    private Integer compareStatus;

    public ViewTransportationRequestDetailManagedBean() {
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
        selectedTServicePO = (ServicePO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedTransServicePO");
        today = new Date();
        if (selectedTServicePO != null) {
            compareStatus = selectedTServicePO.getStatus();
            this.getStatusText(selectedTServicePO.getStatus());
            deliveryDate = selectedTServicePO.getServiceDeliveryDate();
            serviceStartDate = selectedTServicePO.getServiceStartDate();
            serviceEndDate = selectedTServicePO.getServiceEndDate();
            volume = selectedTServicePO.getVolume();
            productId = selectedTServicePO.getProductId();
            productQuantityPerTEU = selectedTServicePO.getProductQuantityPerTEU();
            price = selectedTServicePO.getPrice();
            senderCompanyName = userAccountSB.getCompany(selectedTServicePO.getSenderCompanyId()).getName();
            receiverCompanyName = userAccountSB.getCompany(selectedTServicePO.getReceiverCompanyId()).getName();

        }

    }

    public void updatePO() {
        //Need to check status 
        if (selectedTServicePO != null) {

            boolean canUpdateServicePO = false;

            if (selectedTServicePO.getServiceType().equals("Transportation")) {
                if (deliveryDate.after(selectedTServicePO.getContract().getStartDate()) && deliveryDate.before(selectedTServicePO.getContract().getEndDate())) {
//                Valid delivery date
                    canUpdateServicePO = true;
                } else {
                    //Your date must be within specified contract validity period
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));

                }
            } else {
                if (serviceStartDate.after(selectedTServicePO.getContract().getStartDate()) && serviceEndDate.after(selectedTServicePO.getContract().getStartDate()) && serviceStartDate.before(selectedTServicePO.getContract().getEndDate()) && serviceEndDate.before(selectedTServicePO.getContract().getEndDate())) {
                    canUpdateServicePO = true;

                } else {
                    //Your date must be within specified contract validity period
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));

                }
            }

            if (canUpdateServicePO) {

                int result = servicePOSB.updateServicePO(selectedTServicePO.getServicePOId(), deliveryDate, serviceStartDate, serviceEndDate, volume2, userId, productId2, productQuantityPerTEU2);
                if (result == 1) {
                    volume = volume2;
                    productId = productId2;
                    productQuantityPerTEU = productQuantityPerTEU2;
                    price = (volume2 * selectedTServicePO.getContract().getPrice());

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is updated", ""));
                    logSB.recordSystemLog(userId, "TMS updated service po detail");

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
                }
            }
        }
    }

    public void deletePO() {
        //Need to check status
        if (selectedTServicePO != null) {
            if (selectedTServicePO.getStatus() == 1) {
                int result = servicePOSB.deleteServicePO(selectedTServicePO.getServicePOId(), companyId);
                if (result == 1) {
                    status = "PO Deleted";
                    compareStatus = 2;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is marked as deleted", ""));
                    logSB.recordSystemLog(userId, "TMS deleted a service po");

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

    public void rejectPO() {
        if (selectedTServicePO != null) {
            int result = poProcessingSB.rejectPO(selectedTServicePO.getServicePOId());
            if (result == 1) {
                compareStatus = 4;
                status = "PO Rejected";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is rejected", ""));
                logSB.recordSystemLog(userId, "TMS rejected a service po");

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
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

    public ServicePO getSelectedTServicePO() {
        return selectedTServicePO;
    }

    public void setSelectedTServicePO(ServicePO selectedTServicePO) {
        this.selectedTServicePO = selectedTServicePO;
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

    public Integer getProductId2() {
        return productId2;
    }

    public void setProductId2(Integer productId2) {
        this.productId2 = productId2;
    }

    public Integer getProductQuantityPerTEU2() {
        return productQuantityPerTEU2;
    }

    public void setProductQuantityPerTEU2(Integer productQuantityPerTEU2) {
        this.productQuantityPerTEU2 = productQuantityPerTEU2;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductQuantityPerTEU() {
        return productQuantityPerTEU;
    }

    public void setProductQuantityPerTEU(Integer productQuantityPerTEU) {
        this.productQuantityPerTEU = productQuantityPerTEU;
    }

    public Integer getCompareStatus() {
        return compareStatus;
    }

    public void setCompareStatus(Integer compareStatus) {
        this.compareStatus = compareStatus;
    }

}
