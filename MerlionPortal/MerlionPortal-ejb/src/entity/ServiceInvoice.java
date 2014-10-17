/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "ServiceInvoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceInvoice.findAll", query = "SELECT s FROM ServiceInvoice s"),
    @NamedQuery(name = "ServiceInvoice.findByInvoiceId", query = "SELECT s FROM ServiceInvoice s WHERE s.invoiceId = :invoiceId"),
    @NamedQuery(name = "ServiceInvoice.findByPrice", query = "SELECT s FROM ServiceInvoice s WHERE s.price = :price"),
    @NamedQuery(name = "ServiceInvoice.findBySenderCompanId", query = "SELECT s FROM ServiceInvoice s WHERE s.senderCompanId = :senderCompanId"),
    @NamedQuery(name = "ServiceInvoice.findByReceiverCompanyId", query = "SELECT s FROM ServiceInvoice s WHERE s.receiverCompanyId = :receiverCompanyId"),
    @NamedQuery(name = "ServiceInvoice.findByCreatorId", query = "SELECT s FROM ServiceInvoice s WHERE s.creatorId = :creatorId"),
    @NamedQuery(name = "ServiceInvoice.findByStatus", query = "SELECT s FROM ServiceInvoice s WHERE s.status = :status"),
    @NamedQuery(name = "ServiceInvoice.findByConditionText", query = "SELECT s FROM ServiceInvoice s WHERE s.conditionText = :conditionText"),
    @NamedQuery(name = "ServiceInvoice.findByCreateDate", query = "SELECT s FROM ServiceInvoice s WHERE s.createDate = :createDate")})
public class ServiceInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "invoiceId")
    private Integer invoiceId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Column(name = "senderCompanId")
    private Integer senderCompanId;
    @Column(name = "receiverCompanyId")
    private Integer receiverCompanyId;
    @Column(name = "creatorId")
    private Integer creatorId;
    @Column(name = "status")
    private Integer status;
    @Size(max = 1000)
    @Column(name = "conditionText")
    private String conditionText;
    @Column(name = "createDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JoinColumn(name = "invoiceId", referencedColumnName = "servicePOId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ServicePO servicePO;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "serviceInvoice")
    private Payment payment;

    public ServiceInvoice() {
    }

    public ServiceInvoice(Integer invoiceId) {
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

    public Integer getSenderCompanId() {
        return senderCompanId;
    }

    public void setSenderCompanId(Integer senderCompanId) {
        this.senderCompanId = senderCompanId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ServicePO getServicePO() {
        return servicePO;
    }

    public void setServicePO(ServicePO servicePO) {
        this.servicePO = servicePO;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
        if (!(object instanceof ServiceInvoice)) {
            return false;
        }
        ServiceInvoice other = (ServiceInvoice) object;
        if ((this.invoiceId == null && other.invoiceId != null) || (this.invoiceId != null && !this.invoiceId.equals(other.invoiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ServiceInvoice[ invoiceId=" + invoiceId + " ]";
    }
    
}
