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
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.ordermanagementmodule.POProcessingManagementSessionBean;
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
    @EJB
    POProcessingManagementSessionBean poProcessingSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

    private Date deliveryDate;
    private Date serviceFulfillmentDate;
    private Date serviceReceiveDate;
    private String warehouseOrderType;
    private Integer volume;
    private Double price;
    private Integer volume2;
    private Integer productId;
    private Integer productQuantityPerTEU;
    private Integer productId2;
    private Integer productQuantityPerTEU2;

    private ServicePO selectedServicePO;
    private String status;
    private Date today;
    private String senderCompanyName;
    private String receiverCompanyName;

    private Integer compareStatus;

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
            compareStatus = selectedServicePO.getStatus();
            this.getStatusText(selectedServicePO.getStatus());
            deliveryDate = selectedServicePO.getServiceDeliveryDate();
            warehouseOrderType = selectedServicePO.getWarehousePOtype();
            serviceFulfillmentDate = selectedServicePO.getServiceFulfilmentDate();
            serviceReceiveDate = selectedServicePO.getServiceReceiveDate();
            
            volume = selectedServicePO.getVolume();
            productId = selectedServicePO.getProductId();
            productQuantityPerTEU = selectedServicePO.getProductQuantityPerTEU();
            price = selectedServicePO.getPrice();
            senderCompanyName = userAccountSB.getCompany(selectedServicePO.getSenderCompanyId()).getName();
            receiverCompanyName = userAccountSB.getCompany(selectedServicePO.getReceiverCompanyId()).getName();

        }

    }

    public void updatePO() {
        //Need to check status 
        if (selectedServicePO != null) {

            boolean canUpdateServicePO = false;

            if (selectedServicePO.getServiceType().equals("Transportation")) {
                if (deliveryDate.after(selectedServicePO.getContract().getStartDate()) && deliveryDate.before(selectedServicePO.getContract().getEndDate())) {
//                Valid delivery date
                    canUpdateServicePO = true;
                } else {
                    //Your date must be within specified contract validity period
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));

                }
            } else {

                if (warehouseOrderType.equals("Fulfillment Order")) {

                    if (serviceFulfillmentDate.after(selectedServicePO.getContract().getStartDate()) && serviceFulfillmentDate.before(selectedServicePO.getContract().getEndDate())) {
                    canUpdateServicePO = true;
                    } else {
                        //Your date must be within specified contract validity period
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));
                    }
                }
                if (warehouseOrderType.equals("Receiving Order")) {
                    if (serviceReceiveDate.after(selectedServicePO.getContract().getStartDate()) && serviceReceiveDate.before(selectedServicePO.getContract().getEndDate())) {
                    canUpdateServicePO = true;
                    } else {
                        //Your date must be within specified contract validity period
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));
                    }
                }
            }
           

            if (canUpdateServicePO) {

                int result = servicePOSB.updateServicePO(selectedServicePO.getServicePOId(), deliveryDate, serviceFulfillmentDate, serviceReceiveDate, volume2, userId, productId2, productQuantityPerTEU2, warehouseOrderType);
                if (result == 1) {
                    volume = volume2;
                    productId = productId2;
                    productQuantityPerTEU = productQuantityPerTEU2;
                    price = (volume2 * selectedServicePO.getContract().getPrice());

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is updated", ""));
                    logSB.recordSystemLog(userId, "CRMS updated service po detail");

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
                }
            }
        }
    }

    public void deletePO() {
        //Need to check status
        if (selectedServicePO != null) {
            if (selectedServicePO.getStatus() == 1) {
                int result = servicePOSB.deleteServicePO(selectedServicePO.getServicePOId(), companyId);
                if (result == 1) {
                    status = "PO Deleted";
                    compareStatus = 2;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is marked as deleted", ""));
                    logSB.recordSystemLog(userId, "CRMS deleted a service po");

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

    public void releasePOHold() {
        if (selectedServicePO != null) {
            int result = poProcessingSB.releasePOHold(selectedServicePO.getServicePOId());
            if (result == 1) {
                compareStatus = 5;
                status = "SO Waiting for fulfillment";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO Hold is released", ""));
                logSB.recordSystemLog(userId, "CRMS released a service po hold");

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong, please go back to previous page"));
        }
    }

    public void rejectPO() {
        if (selectedServicePO != null) {
            int result = poProcessingSB.rejectPO(selectedServicePO.getServicePOId());
            if (result == 1) {
                compareStatus = 4;
                status = "PO Rejected";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is rejected", ""));
                logSB.recordSystemLog(userId, "CRMS rejected a service po");

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong, please go back to previous page"));
        }
    }

    public void checkCredit() {
        if (selectedServicePO != null) {
            boolean result = poProcessingSB.passCreditCheck(selectedServicePO.getSenderCompanyId(), selectedServicePO.getContract().getContractId(), selectedServicePO.getServicePOId());
            if (result) {
                compareStatus = 5;
                status = "SO Waiting for fulfillment";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit Check passed", "SO is generated"));
            } else {
                compareStatus = 3;
                status = "PO Hold";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credit Check failed!", "Previous order payment under this contract is not settled. PO is on Hold"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong, please go back to previous page"));
        }
        logSB.recordSystemLog(userId, "CRMS peformed credit check");

    }

    public String generateInvoice() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedServicePO");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedServicePO", selectedServicePO);
        return "generateserviceinvoice.xhtml?faces-redirect=true";
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

    public Date getServiceFulfillmentDate() {
        return serviceFulfillmentDate;
    }

    public void setServiceFulfillmentDate(Date serviceFulfillmentDate) {
        this.serviceFulfillmentDate = serviceFulfillmentDate;
    }

    public Date getServiceReceiveDate() {
        return serviceReceiveDate;
    }

    public void setServiceReceiveDate(Date serviceReceiveDate) {
        this.serviceReceiveDate = serviceReceiveDate;
    }

    public String getWarehouseOrderType() {
        return warehouseOrderType;
    }

    public void setWarehouseOrderType(String warehouseOrderType) {
        this.warehouseOrderType = warehouseOrderType;
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
