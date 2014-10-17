/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "Payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findByPaymentId", query = "SELECT p FROM Payment p WHERE p.paymentId = :paymentId"),
    @NamedQuery(name = "Payment.findByMethod", query = "SELECT p FROM Payment p WHERE p.method = :method"),
    @NamedQuery(name = "Payment.findByReceiveDate", query = "SELECT p FROM Payment p WHERE p.receiveDate = :receiveDate"),
    @NamedQuery(name = "Payment.findByCreatedDate", query = "SELECT p FROM Payment p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "Payment.findByAccountInfo", query = "SELECT p FROM Payment p WHERE p.accountInfo = :accountInfo"),
    @NamedQuery(name = "Payment.findByCreditCardNo", query = "SELECT p FROM Payment p WHERE p.creditCardNo = :creditCardNo"),
    @NamedQuery(name = "Payment.findByAmount", query = "SELECT p FROM Payment p WHERE p.amount = :amount"),
    @NamedQuery(name = "Payment.findBySwiftCode", query = "SELECT p FROM Payment p WHERE p.swiftCode = :swiftCode"),
    @NamedQuery(name = "Payment.findByCheckNumber", query = "SELECT p FROM Payment p WHERE p.checkNumber = :checkNumber")})
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "paymentId")
    private Integer paymentId;
    @Column(name = "method")
    private Integer method;
    @Column(name = "receiveDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveDate;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Size(max = 255)
    @Column(name = "accountInfo")
    private String accountInfo;
    @Size(max = 45)
    @Column(name = "creditCardNo")
    private String creditCardNo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Double amount;
    @Column(name = "swiftCode")
    private Integer swiftCode;
    @Column(name = "checkNumber")
    private Integer checkNumber;
    @JoinColumn(name = "paymentId", referencedColumnName = "invoiceId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ServiceInvoice serviceInvoice;

    public Payment() {
    }

    public Payment(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(Integer swiftCode) {
        this.swiftCode = swiftCode;
    }

    public Integer getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(Integer checkNumber) {
        this.checkNumber = checkNumber;
    }

    public ServiceInvoice getServiceInvoice() {
        return serviceInvoice;
    }

    public void setServiceInvoice(ServiceInvoice serviceInvoice) {
        this.serviceInvoice = serviceInvoice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentId != null ? paymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.paymentId == null && other.paymentId != null) || (this.paymentId != null && !this.paymentId.equals(other.paymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Payment[ paymentId=" + paymentId + " ]";
    }
    
}
