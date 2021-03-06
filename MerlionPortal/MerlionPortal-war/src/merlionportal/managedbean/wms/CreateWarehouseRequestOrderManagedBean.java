/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Contract;
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
import merlionportal.crms.ordermanagementmodule.ServicePOManagementSessionBean;
import merlionportal.wms.mobilitymanagementmodule.OrderFulfillmentSessionBean;
import merlionportal.wms.mobilitymanagementmodule.ReceivingPutawaySessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "createWarehouseRequestMB")
@ViewScoped
public class CreateWarehouseRequestOrderManagedBean {

    /**
     * Creates a new instance of CreateWarehouseRequestOrderManagedBean
     */
    @EJB
    ServicePOManagementSessionBean servicePOSB;
    @EJB
    UserAccountManagementSessionBean uamsb;
    @EJB
    SystemLogSessionBean logSB;
    @EJB
    ReceivingPutawaySessionBean receivingSB;
    @EJB
    OrderFulfillmentSessionBean orderFulfillSB;

    private Integer companyId;
    private Integer userId;
    private Contract contract;

    //user input
    private Integer contractId;
    private Integer volume;
    private Date serviceFulfillmentDate;
    private Date serviceReceiveDate;
    private String warehouseOrderType;
    private Date serviceDeliveryDate;
    private String serviceType;
    private Integer productQuantityPerTEU;
    private Integer productId;
    private BigInteger amount;

    private Date today;
    private String partyAName;
    private String partyBName;

    public CreateWarehouseRequestOrderManagedBean() {
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

    public void createServicePO() {
        boolean canCreateServicePO = false;
        Date today = new Date();
        if (contract != null) {
            if (contract.getServiceType().equals("Transportation")) {
                if (serviceDeliveryDate.after(contract.getStartDate()) && serviceDeliveryDate.before(contract.getEndDate())) {
//                Valid delivery date
                    canCreateServicePO = true;
                } else {
                    //Your date must be within specified contract validity period
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));

                }
            } else {

                if (warehouseOrderType.equals("Fulfillment Order")) {

                    if (serviceFulfillmentDate.after(contract.getStartDate()) && serviceFulfillmentDate.before(contract.getEndDate())) {
                        canCreateServicePO = true;
                    } else {
                        //Your date must be within specified contract validity period
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));
                    }
                }
                if (warehouseOrderType.equals("Receiving Order")) {
                    if (serviceReceiveDate.after(contract.getStartDate()) && serviceReceiveDate.before(contract.getEndDate())) {
                        canCreateServicePO = true;
                    } else {
                        //Your date must be within specified contract validity period
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Your date must be within specified contract validity period"));
                    }
                }
            }
        }
        if (canCreateServicePO) {
            boolean result;

            if (warehouseOrderType.equals("Fulfillment Order")) {
                boolean inStock = orderFulfillSB.reserveStockRentedBins(contract.getPartyA(), contract.getPartyB(), amount.intValue(), productId);
                if (inStock) {
                    result = servicePOSB.createServicePO(contractId, userId, 0, serviceFulfillmentDate, serviceReceiveDate, serviceDeliveryDate, 0, productId, warehouseOrderType, amount);
                } else {
                    result = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Insufficeint Stock!", "Not enough Stock in service provider's warehouse"));
                }
            } else {
                boolean available = receivingSB.checkBinSpaceForRentedBins(contract.getPartyA(), contract.getPartyB(), amount.intValue(), contract.getStorageType());
                if (available) {
                    result = servicePOSB.createServicePO(contractId, userId, 0, serviceFulfillmentDate, serviceReceiveDate, serviceDeliveryDate, 0, productId, warehouseOrderType, amount);
                } else {
                    result = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Insufficeint Bin Space!", "Not enough Space in service provider's warehouse"));
                }
            }
            if (result) {
                this.clearAllFields();

                if (serviceType.equals("Transportation")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Transportation Service PO created!", ""));
                    logSB.recordSystemLog(userId, "WMS created a  transportation service po");

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Warehouse Service PO created!", ""));
                    logSB.recordSystemLog(userId, "WMS created a  warehouse service po");

                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

            }
        }
    }

    public void searchAValidContract() {
        if (servicePOSB.searchAValidContract(contractId, companyId) == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Not a valid Contract!", "You can only search for a valid contract to generate service PO"));
            contract = null;
        } else {
            contract = servicePOSB.searchAValidContract(contractId, companyId);
            if (contract.getServiceType().equals("Transportation")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Not a Warehouse Service Contract!", "You can only search for a warehouse service contract to generate warehouse service request"));
                contract = null;
            } else {
                serviceType = "Warehouse";
//                fill in all fields necessary for transportation contract
                partyAName = uamsb.getCompany(contract.getPartyA()).getName();
                partyBName = uamsb.getCompany(contract.getPartyB()).getName();
            }

        }
    }

    private void clearAllFields() {
        volume = null;
        serviceFulfillmentDate = null;
        serviceReceiveDate = null;
        warehouseOrderType = null;
        serviceDeliveryDate = null;
        productQuantityPerTEU = null;
        productId = null;
        amount = null;
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

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
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

    public Date getServiceDeliveryDate() {
        return serviceDeliveryDate;
    }

    public void setServiceDeliveryDate(Date serviceDeliveryDate) {
        this.serviceDeliveryDate = serviceDeliveryDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getProductQuantityPerTEU() {
        return productQuantityPerTEU;
    }

    public void setProductQuantityPerTEU(Integer productQuantityPerTEU) {
        this.productQuantityPerTEU = productQuantityPerTEU;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
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

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

}
