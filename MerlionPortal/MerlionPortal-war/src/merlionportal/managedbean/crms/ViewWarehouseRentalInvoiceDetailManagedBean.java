/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

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
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.ordermanagementmodule.GRNSOrderSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewWarehouseRentalInvoiceDetailMD")
@ViewScoped
public class ViewWarehouseRentalInvoiceDetailManagedBean {

    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    SystemLogSessionBean logSB;
    @EJB
    GRNSOrderSessionBean grnsSB;

    private Integer userId;
    private Integer companyId;
    private OtherInvoice invoice;

    private String senderCompanyName;
    private String receiverCompanyName;
    private String status;

    private String method;
    private Date receivedDate;
    private String accountInfo;
    private BigInteger creditCardNo;
    private Integer swiftcode;
    private BigInteger checkNumber;

    private Double price;

    public ViewWarehouseRentalInvoiceDetailManagedBean() {
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
        invoice = (OtherInvoice) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedWarehouseRentalInvoice");
        if (invoice != null) {
            senderCompanyName = userAccountSB.getCompany(invoice.getSenderCompanyId()).getName();
            receiverCompanyName = userAccountSB.getCompany(invoice.getReceiverCompanyId()).getName();
            price = invoice.getPrice();
            status = invoice.getStatus();

        }

    }

    public void recordPaymentInfo() {
        boolean result = grnsSB.recordPaymentInformation(invoice.getInvoiceId(), method, receivedDate, accountInfo, creditCardNo, swiftcode, checkNumber);
        if (result) {
            status = "Payment Info Recorded";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Information is recorded", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));

        }
    }

    public void updatePaymentStatus() {
        boolean result = grnsSB.updateContractPaymentStatus(invoice.getInvoiceId());
        if (result) {
            status = "Paid";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status updated", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong!", ""));

        }
    }

    public void onMethodChange() {
        System.out.println("method is " + method);
        System.out.println("method is " + method);
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

    public OtherInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(OtherInvoice invoice) {
        this.invoice = invoice;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
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

}
