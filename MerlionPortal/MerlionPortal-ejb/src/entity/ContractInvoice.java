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
@Table(name = "ContractInvoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContractInvoice.findAll", query = "SELECT c FROM ContractInvoice c"),
    @NamedQuery(name = "ContractInvoice.findByContractInvoiceId", query = "SELECT c FROM ContractInvoice c WHERE c.contractInvoiceId = :contractInvoiceId"),
    @NamedQuery(name = "ContractInvoice.findByPrice", query = "SELECT c FROM ContractInvoice c WHERE c.price = :price"),
    @NamedQuery(name = "ContractInvoice.findBySenderCompanyId", query = "SELECT c FROM ContractInvoice c WHERE c.senderCompanyId = :senderCompanyId"),
    @NamedQuery(name = "ContractInvoice.findByReceiverCompanyId", query = "SELECT c FROM ContractInvoice c WHERE c.receiverCompanyId = :receiverCompanyId"),
    @NamedQuery(name = "ContractInvoice.findByCreatorId", query = "SELECT c FROM ContractInvoice c WHERE c.creatorId = :creatorId"),
    @NamedQuery(name = "ContractInvoice.findByStatus", query = "SELECT c FROM ContractInvoice c WHERE c.status = :status"),
    @NamedQuery(name = "ContractInvoice.findByConditionText", query = "SELECT c FROM ContractInvoice c WHERE c.conditionText = :conditionText"),
    @NamedQuery(name = "ContractInvoice.findByCreatedDate", query = "SELECT c FROM ContractInvoice c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "ContractInvoice.findByMethod", query = "SELECT c FROM ContractInvoice c WHERE c.method = :method"),
    @NamedQuery(name = "ContractInvoice.findByReceiveDate", query = "SELECT c FROM ContractInvoice c WHERE c.receiveDate = :receiveDate"),
    @NamedQuery(name = "ContractInvoice.findByAccountInfo", query = "SELECT c FROM ContractInvoice c WHERE c.accountInfo = :accountInfo"),
    @NamedQuery(name = "ContractInvoice.findByCreditCardNo", query = "SELECT c FROM ContractInvoice c WHERE c.creditCardNo = :creditCardNo"),
    @NamedQuery(name = "ContractInvoice.findBySwiftcode", query = "SELECT c FROM ContractInvoice c WHERE c.swiftcode = :swiftcode"),
    @NamedQuery(name = "ContractInvoice.findByCheckNumber", query = "SELECT c FROM ContractInvoice c WHERE c.checkNumber = :checkNumber"),
    @NamedQuery(name = "ContractInvoice.findByContractId", query = "SELECT c FROM ContractInvoice c WHERE c.contractId = :contractId")})
public class ContractInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contractInvoiceId")
    private Integer contractInvoiceId;
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
    @Column(name = "accountInfo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accountInfo;
    @Column(name = "creditCardNo")
    private BigInteger creditCardNo;
    @Column(name = "swiftcode")
    private Integer swiftcode;
    @Column(name = "checkNumber")
    private BigInteger checkNumber;
    @Column(name = "contractId")
    private Integer contractId;

    public ContractInvoice() {
    }

    public ContractInvoice(Integer contractInvoiceId) {
        this.contractInvoiceId = contractInvoiceId;
    }

    public Integer getContractInvoiceId() {
        return contractInvoiceId;
    }

    public void setContractInvoiceId(Integer contractInvoiceId) {
        this.contractInvoiceId = contractInvoiceId;
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

    public Date getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(Date accountInfo) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contractInvoiceId != null ? contractInvoiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContractInvoice)) {
            return false;
        }
        ContractInvoice other = (ContractInvoice) object;
        if ((this.contractInvoiceId == null && other.contractInvoiceId != null) || (this.contractInvoiceId != null && !this.contractInvoiceId.equals(other.contractInvoiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ContractInvoice[ contractInvoiceId=" + contractInvoiceId + " ]";
    }
    
}
