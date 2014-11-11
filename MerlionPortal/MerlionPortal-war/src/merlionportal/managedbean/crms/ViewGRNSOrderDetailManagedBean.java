/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.GrnsServiceOrder;
import entity.OtherInvoice;
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
import merlionportal.crms.ordermanagementmodule.GRNSOrderSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewGRNSOrderDetailMB")
@ViewScoped
public class ViewGRNSOrderDetailManagedBean {

    /**
     * Creates a new instance of ViewGRNSOrderDetailManagedBean
     */
    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    GRNSOrderSessionBean grnsSB;
    private Integer userId;
    private Integer companyId;
    private GrnsServiceOrder order;

    private String requesterCompanyName;
    private String providerCompanyName;

    private int method;
    private Date receivedDate;
    private String accountInfo;
    private BigInteger creditCardNo;
    private Double amount;
    private Integer swiftcode;
    private BigInteger checkNumber;
    private OtherInvoice invoice;

    private Double price;

    public ViewGRNSOrderDetailManagedBean() {
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
        order = (GrnsServiceOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedGRNSOrder");
        if (order != null) {
            requesterCompanyName = userAccountSB.getCompany(order.getServiceRequester()).getName();
            providerCompanyName = userAccountSB.getCompany(order.getServiceProvider()).getName();

            price = order.getPrice();
            invoice = grnsSB.getOtherInvoiceFromOrder(order.getOrderId());
        }

    }

    public void recordPaymentInfo() {
        int invoiceId = grnsSB.getOtherInvoiceIdFromOrder(order.getOrderId());
        boolean result = grnsSB.recordPaymentInformation(invoiceId, method, receivedDate, accountInfo, creditCardNo, amount, swiftcode, checkNumber);
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Information if recorded", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));

        }
    }

    public void updatePaymentStatus() {
        int invoiceId = grnsSB.getOtherInvoiceIdFromOrder(order.getOrderId());
        boolean result = grnsSB.updateGRNSPaymentStatus(invoiceId);
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status updated", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));

        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public GrnsServiceOrder getOrder() {
        return order;
    }

    public void setOrder(GrnsServiceOrder order) {
        this.order = order;
    }

    public String getRequesterCompanyName() {
        return requesterCompanyName;
    }

    public void setRequesterCompanyName(String requesterCompanyName) {
        this.requesterCompanyName = requesterCompanyName;
    }

    public String getProviderCompanyName() {
        return providerCompanyName;
    }

    public void setProviderCompanyName(String providerCompanyName) {
        this.providerCompanyName = providerCompanyName;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public BigInteger getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(BigInteger creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getSwiftcode() {
        return swiftcode;
    }

    public void setSwiftcode(Integer swiftcode) {
        this.swiftcode = swiftcode;
    }

    public BigInteger getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(BigInteger checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public OtherInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(OtherInvoice invoice) {
        this.invoice = invoice;
    }

}
