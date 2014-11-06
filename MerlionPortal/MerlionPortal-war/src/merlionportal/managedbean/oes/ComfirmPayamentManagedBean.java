/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductInvoice;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.oes.ordermanagement.CommonFunctionSessionBean;
import merlionportal.oes.ordermanagement.PaymentManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "comfirmPayament")
@Dependent
public class ComfirmPayamentManagedBean {

    @EJB
    private CommonFunctionSessionBean commSB;
    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private PaymentManagerSessionBean paymentMB;
    @EJB
    private SystemAccessRightSessionBean accessRight;

    private Integer companyId;
    private Integer userId;
    private ProductInvoice unpaidOrder;
    private String billTo;
    private List<String> maymentMethods;
    private Integer method;
    private Integer swiftcode;
    private String accountInfo;
    private BigInteger creditCardNo;
    private Double amount;
    private BigInteger checkNumber;
    private List<String> orderInfor = new ArrayList();

    public ComfirmPayamentManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

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
        unpaidOrder = (ProductInvoice) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("unpaidOrder");
        orderInfor = paymentMB.findServiceOrder(unpaidOrder.getSalesOrderId());
        maymentMethods = paymentMB.allPaymentMethods();
    }

    public void confirmPayment() {
        int result = paymentMB.updatePaymentStatus(unpaidOrder);
        if (result == 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status updated", ""));
            systemLogSB.recordSystemLog(userId, "OES updated payment status");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong."));
        }
    }

    public String getCustomerName(int cId) {
        return commSB.viewCustomerName(cId);
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

    public ProductInvoice getUnpaidOrder() {
        return unpaidOrder;
    }

    public void setUnpaidOrder(ProductInvoice unpaidOrder) {
        this.unpaidOrder = unpaidOrder;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public List<String> getMaymentMethods() {
        return maymentMethods;
    }

    public void setMaymentMethods(List<String> maymentMethods) {
        this.maymentMethods = maymentMethods;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Integer getSwiftcode() {
        return swiftcode;
    }

    public void setSwiftcode(Integer swiftcode) {
        this.swiftcode = swiftcode;
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

    public BigInteger getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(BigInteger checkNumber) {
        this.checkNumber = checkNumber;
    }

    public List<String> getOrderInfor() {
        return orderInfor;
    }

    public void setOrderInfor(List<String> orderInfor) {
        this.orderInfor = orderInfor;
    }

}
