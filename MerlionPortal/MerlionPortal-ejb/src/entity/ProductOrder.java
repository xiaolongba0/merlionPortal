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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ProductOrder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductOrder.findAll", query = "SELECT p FROM ProductOrder p"),
    @NamedQuery(name = "ProductOrder.findByProductPOId", query = "SELECT p FROM ProductOrder p WHERE p.productPOId = :productPOId"),
    @NamedQuery(name = "ProductOrder.findByCreatedDate", query = "SELECT p FROM ProductOrder p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "ProductOrder.findBySalesPersonId", query = "SELECT p FROM ProductOrder p WHERE p.salesPersonId = :salesPersonId"),
    @NamedQuery(name = "ProductOrder.findByPrice", query = "SELECT p FROM ProductOrder p WHERE p.price = :price"),
    @NamedQuery(name = "ProductOrder.findByStatus", query = "SELECT p FROM ProductOrder p WHERE p.status = :status"),
    @NamedQuery(name = "ProductOrder.findByShipTo", query = "SELECT p FROM ProductOrder p WHERE p.shipTo = :shipTo"),
    @NamedQuery(name = "ProductOrder.findByBillTo", query = "SELECT p FROM ProductOrder p WHERE p.billTo = :billTo"),
    @NamedQuery(name = "ProductOrder.findByContactPersonPhoneNumber", query = "SELECT p FROM ProductOrder p WHERE p.contactPersonPhoneNumber = :contactPersonPhoneNumber"),
    @NamedQuery(name = "ProductOrder.findByContactPersonName", query = "SELECT p FROM ProductOrder p WHERE p.contactPersonName = :contactPersonName"),
    @NamedQuery(name = "ProductOrder.findByCreatorId", query = "SELECT p FROM ProductOrder p WHERE p.creatorId = :creatorId")})
public class ProductOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "productPOId")
    private Integer productPOId;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Size(max = 45)
    @Column(name = "salesPersonId")
    private String salesPersonId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "shipTo")
    private String shipTo;
    @Size(max = 45)
    @Column(name = "billTo")
    private String billTo;
    @Size(max = 45)
    @Column(name = "contactPersonPhoneNumber")
    private String contactPersonPhoneNumber;
    @Size(max = 45)
    @Column(name = "contactPersonName")
    private String contactPersonName;
    @Column(name = "creatorId")
    private Integer creatorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productOrderproductPOId")
    private List<ProductOrderLineItem> productOrderLineItemList;
    @JoinColumn(name = "systemUserId", referencedColumnName = "systemUserId")
    @ManyToOne(optional = false)
    private SystemUser systemUserId;

    public ProductOrder() {
    }

    public ProductOrder(Integer productPOId) {
        this.productPOId = productPOId;
    }

    public Integer getProductPOId() {
        return productPOId;
    }

    public void setProductPOId(Integer productPOId) {
        this.productPOId = productPOId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getSalesPersonId() {
        return salesPersonId;
    }

    public void setSalesPersonId(String salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getContactPersonPhoneNumber() {
        return contactPersonPhoneNumber;
    }

    public void setContactPersonPhoneNumber(String contactPersonPhoneNumber) {
        this.contactPersonPhoneNumber = contactPersonPhoneNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @XmlTransient
    public List<ProductOrderLineItem> getProductOrderLineItemList() {
        return productOrderLineItemList;
    }

    public void setProductOrderLineItemList(List<ProductOrderLineItem> productOrderLineItemList) {
        this.productOrderLineItemList = productOrderLineItemList;
    }

    public SystemUser getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(SystemUser systemUserId) {
        this.systemUserId = systemUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productPOId != null ? productPOId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductOrder)) {
            return false;
        }
        ProductOrder other = (ProductOrder) object;
        if ((this.productPOId == null && other.productPOId != null) || (this.productPOId != null && !this.productPOId.equals(other.productPOId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductOrder[ productPOId=" + productPOId + " ]";
    }
    
}
