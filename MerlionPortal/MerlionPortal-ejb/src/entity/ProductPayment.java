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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "ProductPayment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductPayment.findAll", query = "SELECT p FROM ProductPayment p"),
    @NamedQuery(name = "ProductPayment.findByPaymentId", query = "SELECT p FROM ProductPayment p WHERE p.paymentId = :paymentId"),
    @NamedQuery(name = "ProductPayment.findByMethod", query = "SELECT p FROM ProductPayment p WHERE p.method = :method"),
    @NamedQuery(name = "ProductPayment.findByReceivedDate", query = "SELECT p FROM ProductPayment p WHERE p.receivedDate = :receivedDate"),
    @NamedQuery(name = "ProductPayment.findByCreatedDate", query = "SELECT p FROM ProductPayment p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "ProductPayment.findByAccountInfo", query = "SELECT p FROM ProductPayment p WHERE p.accountInfo = :accountInfo"),
    @NamedQuery(name = "ProductPayment.findByCreditCardNo", query = "SELECT p FROM ProductPayment p WHERE p.creditCardNo = :creditCardNo"),
    @NamedQuery(name = "ProductPayment.findByAmount", query = "SELECT p FROM ProductPayment p WHERE p.amount = :amount"),
    @NamedQuery(name = "ProductPayment.findBySwiftCode", query = "SELECT p FROM ProductPayment p WHERE p.swiftCode = :swiftCode")})
public class ProductPayment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "paymentId")
    private Integer paymentId;
    @Column(name = "method")
    private Integer method;
    @Column(name = "receivedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;
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
    @JoinColumn(name = "invoice", referencedColumnName = "invoiceId")
    @ManyToOne(optional = false)
    private ProductInvoice invoice;

    public ProductPayment() {
    }

    public ProductPayment(Integer paymentId) {
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

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
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

    public ProductInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(ProductInvoice invoice) {
        this.invoice = invoice;
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
        if (!(object instanceof ProductPayment)) {
            return false;
        }
        ProductPayment other = (ProductPayment) object;
        if ((this.paymentId == null && other.paymentId != null) || (this.paymentId != null && !this.paymentId.equals(other.paymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductPayment[ paymentId=" + paymentId + " ]";
    }
    
}
