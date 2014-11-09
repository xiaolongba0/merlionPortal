/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.ServicePO;
import java.io.IOException;
import java.math.BigInteger;
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
import merlionportal.wms.mobilitymanagementmodule.WMSOrderSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewWarehouseRequestDetailMB")
@ViewScoped
public class ViewWarehouseRequestDetailManagedBean {

    /**
     * Creates a new instance of ViewWarehouseRequestDetailManagedBean
     */
    @EJB
    ServicePOManagementSessionBean servicePOSB;
    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    POProcessingManagementSessionBean poProcessingSB;
    @EJB
    WMSOrderSessionBean wmsBean;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

    private String warehouseOrderType;
    private Date fulfillmentDate;
    private Date receiveDate;
    private Integer volume;
    private Double price;
    private Integer volume2;
    private Integer productId;
    private Integer productQuantityPerTEU;
    private Integer productId2;
    private Integer productQuantityPerTEU2;
    private BigInteger amt;
    private BigInteger amt2;

    private ServicePO selectedWServicePO;
    private String status;
    private Date today;
    private String senderCompanyName;
    private String receiverCompanyName;

    private Integer compareStatus;

    public ViewWarehouseRequestDetailManagedBean() {
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
        selectedWServicePO = (ServicePO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedWarehouseServicePO");
        today = new Date();
        if (selectedWServicePO != null) {
            compareStatus = selectedWServicePO.getStatus();
            this.getStatusText(selectedWServicePO.getStatus());
            warehouseOrderType = selectedWServicePO.getWarehousePOtype();
            fulfillmentDate = selectedWServicePO.getServiceFulfilmentDate();
            receiveDate = selectedWServicePO.getServiceReceiveDate();
            volume = selectedWServicePO.getVolume();
            amt = selectedWServicePO.getAmountOfProduct();

            productId = selectedWServicePO.getProductId();
            productQuantityPerTEU = selectedWServicePO.getProductQuantityPerTEU();
            price = selectedWServicePO.getPrice();
            senderCompanyName = userAccountSB.getCompany(selectedWServicePO.getSenderCompanyId()).getName();
            receiverCompanyName = userAccountSB.getCompany(selectedWServicePO.getReceiverCompanyId()).getName();

        }

    }

    public void updatePO() {
        //Need to check status 
        if (selectedWServicePO != null) {

            boolean canUpdateServicePO = false;

            if (selectedWServicePO.getServiceType().equals("Transportation")) {

            } else {

                if (warehouseOrderType.equals("Fulfillment Order")) {

                    if (fulfillmentDate.after(selectedWServicePO.getContract().getStartDate()) && fulfillmentDate.before(selectedWServicePO.getContract().getEndDate())) {
                        canUpdateServicePO = true;
                    } else {
                        //Your date must be within specified contract validity period
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));
                    }
                }
                if (warehouseOrderType.equals("Receiving Order")) {
                    if (receiveDate.after(selectedWServicePO.getContract().getStartDate()) && receiveDate.before(selectedWServicePO.getContract().getEndDate())) {
                        canUpdateServicePO = true;
                    } else {
                        //Your date must be within specified contract validity period
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));
                    }
                }
            }

            if (canUpdateServicePO) {

                int result = servicePOSB.updateServicePO(selectedWServicePO.getServicePOId(), null, fulfillmentDate, receiveDate, volume2, userId, productId2, 0, warehouseOrderType, amt2);
                if (result == 1) {
                    amt = amt2;
                    productId = productId2;
                    price = amt2.doubleValue() * selectedWServicePO.getContract().getPrice();

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is updated", ""));
                    logSB.recordSystemLog(userId, "WMS updated service po detail");

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
                }
            }
        }
    }

    public void deletePO() {
        //Need to check status
        if (selectedWServicePO != null) {
            if (selectedWServicePO.getStatus() == 1) {
                int result = servicePOSB.deleteServicePO(selectedWServicePO.getServicePOId(), companyId);
                if (result == 1) {
                    status = "PO Deleted";
                    compareStatus = 2;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PO is marked as deleted", ""));
                    logSB.recordSystemLog(userId, "WMS deleted a service po");

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

    public void processToBeInternal() {
        boolean result = wmsBean.convertExternalOrderToInternalWOrder(selectedWServicePO.getServicePOId(), companyId);
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Convert to External Order to Internal Order", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong", ""));

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
        if (statusNumber == 10) {
            status = "Packing in progress";
        }
        if (statusNumber == 11) {
            status = "Receiving order rejected";
        }

    }

    public SystemLogSessionBean getLogSB() {
        return logSB;
    }

    public void setLogSB(SystemLogSessionBean logSB) {
        this.logSB = logSB;
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

    public String getWarehouseOrderType() {
        return warehouseOrderType;
    }

    public void setWarehouseOrderType(String warehouseOrderType) {
        this.warehouseOrderType = warehouseOrderType;
    }

    public Date getFulfillmentDate() {
        return fulfillmentDate;
    }

    public void setFulfillmentDate(Date fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public ServicePO getSelectedWServicePO() {
        return selectedWServicePO;
    }

    public void setSelectedWServicePO(ServicePO selectedWServicePO) {
        this.selectedWServicePO = selectedWServicePO;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
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

    public ServicePO getSelectedTServicePO() {
        return selectedWServicePO;
    }

    public void setSelectedTServicePO(ServicePO selectedWServicePO) {
        this.selectedWServicePO = selectedWServicePO;
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

    public Integer getCompareStatus() {
        return compareStatus;
    }

    public void setCompareStatus(Integer compareStatus) {
        this.compareStatus = compareStatus;
    }

    public BigInteger getAmt() {
        return amt;
    }

    public void setAmt(BigInteger amt) {
        this.amt = amt;
    }

    public BigInteger getAmt2() {
        return amt2;
    }

    public void setAmt2(BigInteger amt2) {
        this.amt2 = amt2;
    }

}
