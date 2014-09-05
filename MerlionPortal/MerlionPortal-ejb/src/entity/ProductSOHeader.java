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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "ProductSOHeader")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductSOHeader.findAll", query = "SELECT p FROM ProductSOHeader p"),
    @NamedQuery(name = "ProductSOHeader.findByProductSOId", query = "SELECT p FROM ProductSOHeader p WHERE p.productSOId = :productSOId"),
    @NamedQuery(name = "ProductSOHeader.findByCreatedDate", query = "SELECT p FROM ProductSOHeader p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "ProductSOHeader.findByPrice", query = "SELECT p FROM ProductSOHeader p WHERE p.price = :price"),
    @NamedQuery(name = "ProductSOHeader.findByStatus", query = "SELECT p FROM ProductSOHeader p WHERE p.status = :status"),
    @NamedQuery(name = "ProductSOHeader.findByShipTo", query = "SELECT p FROM ProductSOHeader p WHERE p.shipTo = :shipTo"),
    @NamedQuery(name = "ProductSOHeader.findByBillTo", query = "SELECT p FROM ProductSOHeader p WHERE p.billTo = :billTo"),
    @NamedQuery(name = "ProductSOHeader.findByContactPersonPhoneNumber", query = "SELECT p FROM ProductSOHeader p WHERE p.contactPersonPhoneNumber = :contactPersonPhoneNumber"),
    @NamedQuery(name = "ProductSOHeader.findByContactPersonName", query = "SELECT p FROM ProductSOHeader p WHERE p.contactPersonName = :contactPersonName"),
    @NamedQuery(name = "ProductSOHeader.findByText", query = "SELECT p FROM ProductSOHeader p WHERE p.text = :text"),
    @NamedQuery(name = "ProductSOHeader.findByStaffId", query = "SELECT p FROM ProductSOHeader p WHERE p.staffId = :staffId")})
public class ProductSOHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "productSOId")
    private Integer productSOId;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
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
    @Size(max = 45)
    @Column(name = "text")
    private String text;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "staffId")
    private String staffId;
    @JoinColumn(name = "ProductPOHeader_productPOId", referencedColumnName = "productPOId")
    @ManyToOne(optional = false)
    private ProductPOHeader productPOHeaderproductPOId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productSOHeader")
    private List<ProductSOLineItem> productSOLineItemList;

    public ProductSOHeader() {
    }

    public ProductSOHeader(Integer productSOId) {
        this.productSOId = productSOId;
    }

    public ProductSOHeader(Integer productSOId, String staffId) {
        this.productSOId = productSOId;
        this.staffId = staffId;
    }

    public Integer getProductSOId() {
        return productSOId;
    }

    public void setProductSOId(Integer productSOId) {
        this.productSOId = productSOId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public ProductPOHeader getProductPOHeaderproductPOId() {
        return productPOHeaderproductPOId;
    }

    public void setProductPOHeaderproductPOId(ProductPOHeader productPOHeaderproductPOId) {
        this.productPOHeaderproductPOId = productPOHeaderproductPOId;
    }

    @XmlTransient
    public List<ProductSOLineItem> getProductSOLineItemList() {
        return productSOLineItemList;
    }

    public void setProductSOLineItemList(List<ProductSOLineItem> productSOLineItemList) {
        this.productSOLineItemList = productSOLineItemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productSOId != null ? productSOId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductSOHeader)) {
            return false;
        }
        ProductSOHeader other = (ProductSOHeader) object;
        if ((this.productSOId == null && other.productSOId != null) || (this.productSOId != null && !this.productSOId.equals(other.productSOId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductSOHeader[ productSOId=" + productSOId + " ]";
    }
    
}
