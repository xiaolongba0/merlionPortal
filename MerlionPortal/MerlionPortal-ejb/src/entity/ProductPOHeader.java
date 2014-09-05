/*
 * To change this template, choose Tools | Templates
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
 * @author MelodyPond_2
 */
@Entity
@Table(name = "productpoheader")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductPOHeader.findAll", query = "SELECT p FROM ProductPOHeader p"),
    @NamedQuery(name = "ProductPOHeader.findByProductPOId", query = "SELECT p FROM ProductPOHeader p WHERE p.productPOId = :productPOId"),
    @NamedQuery(name = "ProductPOHeader.findByCreatedDate", query = "SELECT p FROM ProductPOHeader p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "ProductPOHeader.findBySalesPersonId", query = "SELECT p FROM ProductPOHeader p WHERE p.salesPersonId = :salesPersonId"),
    @NamedQuery(name = "ProductPOHeader.findByPrice", query = "SELECT p FROM ProductPOHeader p WHERE p.price = :price"),
    @NamedQuery(name = "ProductPOHeader.findByStatus", query = "SELECT p FROM ProductPOHeader p WHERE p.status = :status"),
    @NamedQuery(name = "ProductPOHeader.findByShipTo", query = "SELECT p FROM ProductPOHeader p WHERE p.shipTo = :shipTo"),
    @NamedQuery(name = "ProductPOHeader.findByBillTo", query = "SELECT p FROM ProductPOHeader p WHERE p.billTo = :billTo"),
    @NamedQuery(name = "ProductPOHeader.findByContactPersonPhoneNumber", query = "SELECT p FROM ProductPOHeader p WHERE p.contactPersonPhoneNumber = :contactPersonPhoneNumber"),
    @NamedQuery(name = "ProductPOHeader.findByContactPersonName", query = "SELECT p FROM ProductPOHeader p WHERE p.contactPersonName = :contactPersonName"),
    @NamedQuery(name = "ProductPOHeader.findByClientSystemUsersystemUserId", query = "SELECT p FROM ProductPOHeader p WHERE p.clientSystemUsersystemUserId = :clientSystemUsersystemUserId")})
public class ProductPOHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "Client_SystemUser_systemUserId")
    private int clientSystemUsersystemUserId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productPOHeader")
    private List<ProductPOLineItem> productPOLineItemList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productPOHeaderproductPOId")
    private List<ProductSOHeader> productSOHeaderList;

    public ProductPOHeader() {
    }

    public ProductPOHeader(Integer productPOId) {
        this.productPOId = productPOId;
    }

    public ProductPOHeader(Integer productPOId, int clientSystemUsersystemUserId) {
        this.productPOId = productPOId;
        this.clientSystemUsersystemUserId = clientSystemUsersystemUserId;
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

    public int getClientSystemUsersystemUserId() {
        return clientSystemUsersystemUserId;
    }

    public void setClientSystemUsersystemUserId(int clientSystemUsersystemUserId) {
        this.clientSystemUsersystemUserId = clientSystemUsersystemUserId;
    }

    @XmlTransient
    public List<ProductPOLineItem> getProductPOLineItemList() {
        return productPOLineItemList;
    }

    public void setProductPOLineItemList(List<ProductPOLineItem> productPOLineItemList) {
        this.productPOLineItemList = productPOLineItemList;
    }

    @XmlTransient
    public List<ProductSOHeader> getProductSOHeaderList() {
        return productSOHeaderList;
    }

    public void setProductSOHeaderList(List<ProductSOHeader> productSOHeaderList) {
        this.productSOHeaderList = productSOHeaderList;
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
        if (!(object instanceof ProductPOHeader)) {
            return false;
        }
        ProductPOHeader other = (ProductPOHeader) object;
        if ((this.productPOId == null && other.productPOId != null) || (this.productPOId != null && !this.productPOId.equals(other.productPOId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductPOHeader[ productPOId=" + productPOId + " ]";
    }
    
}
