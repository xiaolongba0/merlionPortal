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
import javax.persistence.OneToOne;
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
@Table(name = "Post")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"),
    @NamedQuery(name = "Post.findByPostId", query = "SELECT p FROM Post p WHERE p.postId = :postId"),
    @NamedQuery(name = "Post.findByServiceType", query = "SELECT p FROM Post p WHERE p.serviceType = :serviceType"),
    @NamedQuery(name = "Post.findByDescription", query = "SELECT p FROM Post p WHERE p.description = :description"),
    @NamedQuery(name = "Post.findByPostDate", query = "SELECT p FROM Post p WHERE p.postDate = :postDate"),
    @NamedQuery(name = "Post.findByDestination", query = "SELECT p FROM Post p WHERE p.destination = :destination"),
    @NamedQuery(name = "Post.findByOrigin", query = "SELECT p FROM Post p WHERE p.origin = :origin"),
    @NamedQuery(name = "Post.findByStorageStartDate", query = "SELECT p FROM Post p WHERE p.storageStartDate = :storageStartDate"),
    @NamedQuery(name = "Post.findByStorageEndDate", query = "SELECT p FROM Post p WHERE p.storageEndDate = :storageEndDate"),
    @NamedQuery(name = "Post.findByQuantityInTEU", query = "SELECT p FROM Post p WHERE p.quantityInTEU = :quantityInTEU"),
    @NamedQuery(name = "Post.findByDeliveryDate", query = "SELECT p FROM Post p WHERE p.deliveryDate = :deliveryDate"),
    @NamedQuery(name = "Post.findByWarehouseId", query = "SELECT p FROM Post p WHERE p.warehouseId = :warehouseId"),
    @NamedQuery(name = "Post.findByWarehouseSpace", query = "SELECT p FROM Post p WHERE p.warehouseSpace = :warehouseSpace")})
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "postId")
    private Integer postId;
    @Size(max = 45)
    @Column(name = "serviceType")
    private String serviceType;
    @Size(max = 2000)
    @Column(name = "description")
    private String description;
    @Column(name = "postDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;
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
    @Column(name = "deliveryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @Column(name = "warehouseId")
    private Integer warehouseId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "warehouseSpace")
    private Double warehouseSpace;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Bid> bidList;
    @JoinColumn(name = "systemUser", referencedColumnName = "systemUserId")
    @ManyToOne(optional = false)
    private SystemUser systemUser;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "post")
    private GrnsServiceOrder grnsServiceOrder;

    public Post() {
    }

    public Post(Integer postId) {
        this.postId = postId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Double getWarehouseSpace() {
        return warehouseSpace;
    }

    public void setWarehouseSpace(Double warehouseSpace) {
        this.warehouseSpace = warehouseSpace;
    }

    @XmlTransient
    public List<Bid> getBidList() {
        return bidList;
    }

    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public GrnsServiceOrder getGrnsServiceOrder() {
        return grnsServiceOrder;
    }

    public void setGrnsServiceOrder(GrnsServiceOrder grnsServiceOrder) {
        this.grnsServiceOrder = grnsServiceOrder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (postId != null ? postId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.postId == null && other.postId != null) || (this.postId != null && !this.postId.equals(other.postId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Post[ postId=" + postId + " ]";
    }
    
}
