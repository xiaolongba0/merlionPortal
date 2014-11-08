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
@Table(name = "GrnsServiceOrder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrnsServiceOrder.findAll", query = "SELECT g FROM GrnsServiceOrder g"),
    @NamedQuery(name = "GrnsServiceOrder.findByOrderId", query = "SELECT g FROM GrnsServiceOrder g WHERE g.orderId = :orderId"),
    @NamedQuery(name = "GrnsServiceOrder.findByCreateDate", query = "SELECT g FROM GrnsServiceOrder g WHERE g.createDate = :createDate"),
    @NamedQuery(name = "GrnsServiceOrder.findByServiceType", query = "SELECT g FROM GrnsServiceOrder g WHERE g.serviceType = :serviceType"),
    @NamedQuery(name = "GrnsServiceOrder.findByServiceRequester", query = "SELECT g FROM GrnsServiceOrder g WHERE g.serviceRequester = :serviceRequester"),
    @NamedQuery(name = "GrnsServiceOrder.findByServiceProvider", query = "SELECT g FROM GrnsServiceOrder g WHERE g.serviceProvider = :serviceProvider"),
    @NamedQuery(name = "GrnsServiceOrder.findByDestination", query = "SELECT g FROM GrnsServiceOrder g WHERE g.destination = :destination"),
    @NamedQuery(name = "GrnsServiceOrder.findByOrigin", query = "SELECT g FROM GrnsServiceOrder g WHERE g.origin = :origin"),
    @NamedQuery(name = "GrnsServiceOrder.findByStorageStartDate", query = "SELECT g FROM GrnsServiceOrder g WHERE g.storageStartDate = :storageStartDate"),
    @NamedQuery(name = "GrnsServiceOrder.findByStorageEndDate", query = "SELECT g FROM GrnsServiceOrder g WHERE g.storageEndDate = :storageEndDate"),
    @NamedQuery(name = "GrnsServiceOrder.findByQuantityInTEU", query = "SELECT g FROM GrnsServiceOrder g WHERE g.quantityInTEU = :quantityInTEU"),
    @NamedQuery(name = "GrnsServiceOrder.findByStorageType", query = "SELECT g FROM GrnsServiceOrder g WHERE g.storageType = :storageType"),
    @NamedQuery(name = "GrnsServiceOrder.findByDeliveryDate", query = "SELECT g FROM GrnsServiceOrder g WHERE g.deliveryDate = :deliveryDate"),
    @NamedQuery(name = "GrnsServiceOrder.findByWarehouseSpace", query = "SELECT g FROM GrnsServiceOrder g WHERE g.warehouseSpace = :warehouseSpace"),
    @NamedQuery(name = "GrnsServiceOrder.findByDescription", query = "SELECT g FROM GrnsServiceOrder g WHERE g.description = :description"),
    @NamedQuery(name = "GrnsServiceOrder.findByPrice", query = "SELECT g FROM GrnsServiceOrder g WHERE g.price = :price"),
    @NamedQuery(name = "GrnsServiceOrder.findByCurrency", query = "SELECT g FROM GrnsServiceOrder g WHERE g.currency = :currency"),
    @NamedQuery(name = "GrnsServiceOrder.findByStatus", query = "SELECT g FROM GrnsServiceOrder g WHERE g.status = :status")})
public class GrnsServiceOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "orderId")
    private Integer orderId;
    @Column(name = "createDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 45)
    @Column(name = "serviceType")
    private String serviceType;
    @Column(name = "serviceRequester")
    private Integer serviceRequester;
    @Column(name = "serviceProvider")
    private Integer serviceProvider;
    @Size(max = 200)
    @Column(name = "destination")
    private String destination;
    @Size(max = 200)
    @Column(name = "origin")
    private String origin;
    @Column(name = "storageStartDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date storageStartDate;
    @Column(name = "storageEndDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date storageEndDate;
    @Column(name = "quantityInTEU")
    private Integer quantityInTEU;
    @Size(max = 45)
    @Column(name = "storageType")
    private String storageType;
    @Column(name = "deliveryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "warehouseSpace")
    private Double warehouseSpace;
    @Size(max = 2000)
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
    @Size(max = 200)
    @Column(name = "currency")
    private String currency;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "orderId", referencedColumnName = "postId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Post post;

    public GrnsServiceOrder() {
    }

    public GrnsServiceOrder(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getServiceRequester() {
        return serviceRequester;
    }

    public void setServiceRequester(Integer serviceRequester) {
        this.serviceRequester = serviceRequester;
    }

    public Integer getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(Integer serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getStorageStartDate() {
        return storageStartDate;
    }

    public void setStorageStartDate(Date storageStartDate) {
        this.storageStartDate = storageStartDate;
    }

    public Date getStorageEndDate() {
        return storageEndDate;
    }

    public void setStorageEndDate(Date storageEndDate) {
        this.storageEndDate = storageEndDate;
    }

    public Integer getQuantityInTEU() {
        return quantityInTEU;
    }

    public void setQuantityInTEU(Integer quantityInTEU) {
        this.quantityInTEU = quantityInTEU;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Double getWarehouseSpace() {
        return warehouseSpace;
    }

    public void setWarehouseSpace(Double warehouseSpace) {
        this.warehouseSpace = warehouseSpace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrnsServiceOrder)) {
            return false;
        }
        GrnsServiceOrder other = (GrnsServiceOrder) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GrnsServiceOrder[ orderId=" + orderId + " ]";
    }
    
}
