/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "OtherInvoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtherInvoice.findAll", query = "SELECT o FROM OtherInvoice o"),
    @NamedQuery(name = "OtherInvoice.findByInvoiceId", query = "SELECT o FROM OtherInvoice o WHERE o.invoiceId = :invoiceId"),
    @NamedQuery(name = "OtherInvoice.findByPrice", query = "SELECT o FROM OtherInvoice o WHERE o.price = :price"),
    @NamedQuery(name = "OtherInvoice.findBySenderCompanyId", query = "SELECT o FROM OtherInvoice o WHERE o.senderCompanyId = :senderCompanyId"),
    @NamedQuery(name = "OtherInvoice.findByReceiverCompanyId", query = "SELECT o FROM OtherInvoice o WHERE o.receiverCompanyId = :receiverCompanyId"),
    @NamedQuery(name = "OtherInvoice.findByCreatorId", query = "SELECT o FROM OtherInvoice o WHERE o.creatorId = :creatorId"),
    @NamedQuery(name = "OtherInvoice.findByStatus", query = "SELECT o FROM OtherInvoice o WHERE o.status = :status"),
    @NamedQuery(name = "OtherInvoice.findByConditionText", query = "SELECT o FROM OtherInvoice o WHERE o.conditionText = :conditionText"),
    @NamedQuery(name = "OtherInvoice.findByCreatedDate", query = "SELECT o FROM OtherInvoice o WHERE o.createdDate = :createdDate"),
    @NamedQuery(name = "OtherInvoice.findByMethod", query = "SELECT o FROM OtherInvoice o WHERE o.method = :method"),
    @NamedQuery(name = "OtherInvoice.findByReceiveDate", query = "SELECT o FROM OtherInvoice o WHERE o.receiveDate = :receiveDate"),
    @NamedQuery(name = "OtherInvoice.findByAccountInfo", query = "SELECT o FROM OtherInvoice o WHERE o.accountInfo = :accountInfo"),
    @NamedQuery(name = "OtherInvoice.findByCreditCardNo", query = "SELECT o FROM OtherInvoice o WHERE o.creditCardNo = :creditCardNo"),
    @NamedQuery(name = "OtherInvoice.findBySwiftcode", query = "SELECT o FROM OtherInvoice o WHERE o.swiftcode = :swiftcode"),
    @NamedQuery(name = "OtherInvoice.findByCheckNumber", query = "SELECT o FROM OtherInvoice o WHERE o.checkNumber = :checkNumber"),
    @NamedQuery(name = "OtherInvoice.findByContractId", query = "SELECT o FROM OtherInvoice o WHERE o.contractId = :contractId"),
    @NamedQuery(name = "OtherInvoice.findByGrnsOrderId", query = "SELECT o FROM OtherInvoice o WHERE o.grnsOrderId = :grnsOrderId"),
    @NamedQuery(name = "OtherInvoice.findByGrnsOrder", query = "SELECT o FROM OtherInvoice o WHERE o.grnsOrder = :grnsOrder")})
public class OtherInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "invoiceId")
    private Integer invoiceId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Column(name = "senderCompanyId")
    private Integer senderCompanyId;
    @Column(name = "receiverCompanyId")
    private Integer receiverCompanyId;
    @Column(name = "creatorId")
    private Integer creatorId;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Size(max = 2000)
    @Column(name = "conditionText")
    private String conditionText;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Size(max = 45)
    @Column(name = "method")
    private String method;
    @Column(name = "receiveDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveDate;
    @Size(max = 255)
    @Column(name = "accountInfo")
    private String accountInfo;
    @Column(name = "creditCardNo")
    private BigInteger creditCardNo;
    @Column(name = "swiftcode")
    private Integer swiftcode;
    @Column(name = "checkNumber")
    private BigInteger checkNumber;
    @Column(name = "contractId")
    private Integer contractId;
    @Column(name = "grnsOrderId")
    private Integer grnsOrderId;
    @Column(name = "grnsOrder")
    private Boolean grnsOrder;

    public OtherInvoice() {
    }

    public OtherInvoice(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSenderCompanyId() {
        return senderCompanyId;
    }

    public void setSenderCompanyId(Integer senderCompanyId) {
        this.senderCompanyId = senderCompanyId;
    }

    public Integer getReceiverCompanyId() {
        return receiverCompanyId;
    }

    public void setReceiverCompanyId(Integer receiverCompanyId) {
        this.receiverCompanyId = receiverCompanyId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
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

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getGrnsOrderId() {
        return grnsOrderId;
    }

    public void setGrnsOrderId(Integer grnsOrderId) {
        this.grnsOrderId = grnsOrderId;
    }

    public Boolean getGrnsOrder() {
        return grnsOrder;
    }

    public void setGrnsOrder(Boolean grnsOrder) {
        this.grnsOrder = grnsOrder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceId != null ? invoiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtherInvoice)) {
            return false;
        }
        OtherInvoice other = (OtherInvoice) object;
        if ((this.invoiceId == null && other.invoiceId != null) || (this.invoiceId != null && !this.invoiceId.equals(other.invoiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OtherInvoice[ invoiceId=" + invoiceId + " ]";
    }
    
}
