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
@Table(name = "ServiceQuotation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceQuotation.findAll", query = "SELECT s FROM ServiceQuotation s"),
    @NamedQuery(name = "ServiceQuotation.findByQuotationId", query = "SELECT s FROM ServiceQuotation s WHERE s.quotationId = :quotationId"),
    @NamedQuery(name = "ServiceQuotation.findByServiceType", query = "SELECT s FROM ServiceQuotation s WHERE s.serviceType = :serviceType"),
    @NamedQuery(name = "ServiceQuotation.findByCreatedDate", query = "SELECT s FROM ServiceQuotation s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "ServiceQuotation.findByStartDate", query = "SELECT s FROM ServiceQuotation s WHERE s.startDate = :startDate"),
    @NamedQuery(name = "ServiceQuotation.findByEndDate", query = "SELECT s FROM ServiceQuotation s WHERE s.endDate = :endDate"),
    @NamedQuery(name = "ServiceQuotation.findBySenderCompanyId", query = "SELECT s FROM ServiceQuotation s WHERE s.senderCompanyId = :senderCompanyId"),
    @NamedQuery(name = "ServiceQuotation.findByReceiverCompanyId", query = "SELECT s FROM ServiceQuotation s WHERE s.receiverCompanyId = :receiverCompanyId"),
    @NamedQuery(name = "ServiceQuotation.findByStatus", query = "SELECT s FROM ServiceQuotation s WHERE s.status = :status"),
    @NamedQuery(name = "ServiceQuotation.findByPrice", query = "SELECT s FROM ServiceQuotation s WHERE s.price = :price"),
    @NamedQuery(name = "ServiceQuotation.findByDestination", query = "SELECT s FROM ServiceQuotation s WHERE s.destination = :destination"),
    @NamedQuery(name = "ServiceQuotation.findByOrigin", query = "SELECT s FROM ServiceQuotation s WHERE s.origin = :origin"),
    @NamedQuery(name = "ServiceQuotation.findByDiscountRate", query = "SELECT s FROM ServiceQuotation s WHERE s.discountRate = :discountRate"),
    @NamedQuery(name = "ServiceQuotation.findByQuantityPerMonth", query = "SELECT s FROM ServiceQuotation s WHERE s.quantityPerMonth = :quantityPerMonth"),
    @NamedQuery(name = "ServiceQuotation.findByStorageBinId", query = "SELECT s FROM ServiceQuotation s WHERE s.storageBinId = :storageBinId"),
    @NamedQuery(name = "ServiceQuotation.findByStorageZoneId", query = "SELECT s FROM ServiceQuotation s WHERE s.storageZoneId = :storageZoneId"),
    @NamedQuery(name = "ServiceQuotation.findByWarehoueId", query = "SELECT s FROM ServiceQuotation s WHERE s.warehoueId = :warehoueId")})
public class ServiceQuotation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "quotationId")
    private Integer quotationId;
    @Size(max = 45)
    @Column(name = "serviceType")
    private String serviceType;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "senderCompanyId")
    private Integer senderCompanyId;
    @Column(name = "receiverCompanyId")
    private Integer receiverCompanyId;
    @Column(name = "status")
    private Integer status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Size(max = 255)
    @Column(name = "destination")
    private String destination;
    @Size(max = 255)
    @Column(name = "origin")
    private String origin;
    @Column(name = "discountRate")
    private Integer discountRate;
    @Column(name = "quantityPerMonth")
    private Integer quantityPerMonth;
    @Column(name = "storageBinId")
    private Integer storageBinId;
    @Column(name = "storageZoneId")
    private Integer storageZoneId;
    @Column(name = "warehoueId")
    private Integer warehoueId;
    @JoinColumn(name = "serviceCatalog", referencedColumnName = "serviceCatalogId")
    @ManyToOne(optional = false)
    private ServiceCatalog serviceCatalog;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceQuotation")
    private List<Contract> contractList;

    public ServiceQuotation() {
    }

    public ServiceQuotation(Integer quotationId) {
        this.quotationId = quotationId;
    }

    public Integer getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Integer quotationId) {
        this.quotationId = quotationId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public Integer getQuantityPerMonth() {
        return quantityPerMonth;
    }

    public void setQuantityPerMonth(Integer quantityPerMonth) {
        this.quantityPerMonth = quantityPerMonth;
    }

    public Integer getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Integer storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Integer getStorageZoneId() {
        return storageZoneId;
    }

    public void setStorageZoneId(Integer storageZoneId) {
        this.storageZoneId = storageZoneId;
    }

    public Integer getWarehoueId() {
        return warehoueId;
    }

    public void setWarehoueId(Integer warehoueId) {
        this.warehoueId = warehoueId;
    }

    public ServiceCatalog getServiceCatalog() {
        return serviceCatalog;
    }

    public void setServiceCatalog(ServiceCatalog serviceCatalog) {
        this.serviceCatalog = serviceCatalog;
    }

    @XmlTransient
    public List<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(List<Contract> contractList) {
        this.contractList = contractList;
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
        if (!(object instanceof ServiceQuotation)) {
            return false;
        }
        ServiceQuotation other = (ServiceQuotation) object;
        if ((this.quotationId == null && other.quotationId != null) || (this.quotationId != null && !this.quotationId.equals(other.quotationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ServiceQuotation[ quotationId=" + quotationId + " ]";
    }
    
}
