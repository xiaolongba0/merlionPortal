/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceInvoice;
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
import merlionportal.crms.ordermanagementmodule.POProcessingManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewInvoiceDetailManagedBean")
@ViewScoped
public class ViewInvoiceDetailManagedBean {

    /**
     * Creates a new instance of ViewInvoiceDetailManagedBean
     */
    private Integer companyId;
    private Integer userId;

    @EJB
    UserAccountManagementSessionBean userAccountSB;
    @EJB
    POProcessingManagementSessionBean poProcessingSB;

    private ServiceInvoice selectedInvoice;
    private String status;
    private String senderCompanyName;
    private String receiverCompanyName;
    private String orderstatus;

    private int method;
    private Date receivedDate;
    private String accountInfo;
    private BigInteger creditCardNo;
    private Double amount;
    private Integer swiftcode;
    private BigInteger checkNumber;

    private String methodText;

    public ViewInvoiceDetailManagedBean() {
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
        selectedInvoice = (ServiceInvoice) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedInvoice");
        if (selectedInvoice != null) {
            this.getStatusText(selectedInvoice.getStatus());
            this.getOrderStatusText(selectedInvoice.getServicePO().getStatus());
            senderCompanyName = userAccountSB.getCompany(selectedInvoice.getSenderCompanId()).getName();
            receiverCompanyName = userAccountSB.getCompany(selectedInvoice.getReceiverCompanyId()).getName();
        }

    }

    public void updatePaymentStatus() {
        int result = poProcessingSB.updatePaymentStatus(selectedInvoice.getInvoiceId());
        if (result == 1) {
            this.getStatusText(3);
            this.getOrderStatusText(8);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status updated", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void recordPaymentInfo() {
        boolean result = poProcessingSB.recordPaymentInfo(selectedInvoice.getInvoiceId(), method, receivedDate, accountInfo, creditCardNo, amount, swiftcode, checkNumber);
        if (result) {
            this.getStatusText(2);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Information Recorded", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public void onMethodChange() {
        System.out.println("Method: " + method);
    }

    private void getStatusText(int statusNumber) {

        if (statusNumber == 1) {
            status = "Invoiced";
        }
        if (statusNumber == 2) {
            status = "Payment information recorded";
        }
        if (statusNumber == 3) {
            status = "Paid";
        }
    }

    private void getOrderStatusText(int statusNumber) {

        if (statusNumber == 1) {
            orderstatus = "PO Waiting to be processed";
        }
        if (statusNumber == 2) {
            orderstatus = "PO Deleted";
        }
        if (statusNumber == 3) {
            orderstatus = "PO Hold";
        }
        if (statusNumber == 4) {
            orderstatus = "PO Rejected";
        }
        if (statusNumber == 5) {
            orderstatus = "SO Waiting for fulfillment";
        }
        if (statusNumber == 6) {
            orderstatus = "SO Fulfilled";
        }
        if (statusNumber == 7) {
            orderstatus = "SO Invoiced";
        }
        if (statusNumber == 8) {
            orderstatus = "SO Closed";
        }
        if (statusNumber == 9) {
            orderstatus = "Transportation SO in transit";
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

    public ServiceInvoice getSelectedInvoice() {
        return selectedInvoice;
    }

    public void setSelectedInvoice(ServiceInvoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
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

    public BigInteger getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(BigInteger creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public BigInteger getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(BigInteger checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getMethodText() {
        if (selectedInvoice.getPayment().getMethod() == 1) {
            methodText = "Telegraphic Transfer";
        }
        if (selectedInvoice.getPayment().getMethod() == 2) {
            methodText = "Credit Card";
        }
        if (selectedInvoice.getPayment().getMethod() == 3) {
            methodText = "Paper Check";
        }

        return methodText;
    }

    public void setMethodText(String methodText) {
        this.methodText = methodText;
    }

}
