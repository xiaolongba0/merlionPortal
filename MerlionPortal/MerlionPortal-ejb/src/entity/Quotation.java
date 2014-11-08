/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "Quotation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quotation.findAll", query = "SELECT q FROM Quotation q"),
    @NamedQuery(name = "Quotation.findByQuotationId", query = "SELECT q FROM Quotation q WHERE q.quotationId = :quotationId"),
    @NamedQuery(name = "Quotation.findByDescription", query = "SELECT q FROM Quotation q WHERE q.description = :description"),
    @NamedQuery(name = "Quotation.findByCustomerId", query = "SELECT q FROM Quotation q WHERE q.customerId = :customerId"),
    @NamedQuery(name = "Quotation.findByStatus", query = "SELECT q FROM Quotation q WHERE q.status = :status"),
    @NamedQuery(name = "Quotation.findByCreateDate", query = "SELECT q FROM Quotation q WHERE q.createDate = :createDate"),
    @NamedQuery(name = "Quotation.findByCompany", query = "SELECT q FROM Quotation q WHERE q.company = :company"),
    @NamedQuery(name = "Quotation.findByCurrency", query = "SELECT q FROM Quotation q WHERE q.currency = :currency")})
public class Quotation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "quotationId")
    private Integer quotationId;
    @Size(max = 5000)
    @Column(name = "description")
    private String description;
    @Column(name = "customerId")
    private Integer customerId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "createDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "company")
    private Integer company;
    @Size(max = 200)
    @Column(name = "currency")
    private String currency;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotationquotationId")
    private List<QuotationLineItem> quotationLineItemList;

    public Quotation() {
    }

    public Quotation(Integer quotationId) {
        this.quotationId = quotationId;
    }

    public Integer getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Integer quotationId) {
        this.quotationId = quotationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @XmlTransient
    public List<QuotationLineItem> getQuotationLineItemList() {
        return quotationLineItemList;
    }

    public void setQuotationLineItemList(List<QuotationLineItem> quotationLineItemList) {
        this.quotationLineItemList = quotationLineItemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (quotationId != null ? quotationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quotation)) {
            return false;
        }
        Quotation other = (Quotation) object;
        if ((this.quotationId == null && other.quotationId != null) || (this.quotationId != null && !this.quotationId.equals(other.quotationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Quotation[ quotationId=" + quotationId + " ]";
    }
    
}
