/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "Contract")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contract.findAll", query = "SELECT c FROM Contract c"),
    @NamedQuery(name = "Contract.findByContractId", query = "SELECT c FROM Contract c WHERE c.contractId = :contractId"),
    @NamedQuery(name = "Contract.findByServiceType", query = "SELECT c FROM Contract c WHERE c.serviceType = :serviceType"),
    @NamedQuery(name = "Contract.findByPartyA", query = "SELECT c FROM Contract c WHERE c.partyA = :partyA"),
    @NamedQuery(name = "Contract.findByPartyB", query = "SELECT c FROM Contract c WHERE c.partyB = :partyB"),
    @NamedQuery(name = "Contract.findByConditionText", query = "SELECT c FROM Contract c WHERE c.conditionText = :conditionText"),
    @NamedQuery(name = "Contract.findByStatus", query = "SELECT c FROM Contract c WHERE c.status = :status"),
    @NamedQuery(name = "Contract.findByStartDate", query = "SELECT c FROM Contract c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "Contract.findByEndDate", query = "SELECT c FROM Contract c WHERE c.endDate = :endDate"),
    @NamedQuery(name = "Contract.findByCreatedDate", query = "SELECT c FROM Contract c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "Contract.findByPrice", query = "SELECT c FROM Contract c WHERE c.price = :price"),
    @NamedQuery(name = "Contract.findByOrigin", query = "SELECT c FROM Contract c WHERE c.origin = :origin"),
    @NamedQuery(name = "Contract.findByDestination", query = "SELECT c FROM Contract c WHERE c.destination = :destination"),
    @NamedQuery(name = "Contract.findByContactPersonId", query = "SELECT c FROM Contract c WHERE c.contactPersonId = :contactPersonId"),
    @NamedQuery(name = "Contract.findByContactPersonName", query = "SELECT c FROM Contract c WHERE c.contactPersonName = :contactPersonName"),
    @NamedQuery(name = "Contract.findByContactPersonNumber", query = "SELECT c FROM Contract c WHERE c.contactPersonNumber = :contactPersonNumber"),
    @NamedQuery(name = "Contract.findByReasonModification", query = "SELECT c FROM Contract c WHERE c.reasonModification = :reasonModification"),
    @NamedQuery(name = "Contract.findByStorageType", query = "SELECT c FROM Contract c WHERE c.storageType = :storageType"),
    @NamedQuery(name = "Contract.findByWarehouseRental", query = "SELECT c FROM Contract c WHERE c.warehouseRental = :warehouseRental"),
    @NamedQuery(name = "Contract.findByAmountOfProduct", query = "SELECT c FROM Contract c WHERE c.amountOfProduct = :amountOfProduct"),
    @NamedQuery(name = "Contract.findBySpacePerProduct", query = "SELECT c FROM Contract c WHERE c.spacePerProduct = :spacePerProduct"),
    @NamedQuery(name = "Contract.findByWarehouseId", query = "SELECT c FROM Contract c WHERE c.warehouseId = :warehouseId")})
public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contractId")
    private Integer contractId;
    @Size(max = 45)
    @Column(name = "serviceType")
    private String serviceType;
    @Column(name = "partyA")
    private Integer partyA;
    @Column(name = "partyB")
    private Integer partyB;
    @Size(max = 9000)
    @Column(name = "conditionText")
    private String conditionText;
    @Column(name = "status")
    private Integer status;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Size(max = 255)
    @Column(name = "origin")
    private String origin;
    @Size(max = 255)
    @Column(name = "destination")
    private String destination;
    @Column(name = "contactPersonId")
    private Integer contactPersonId;
    @Size(max = 45)
    @Column(name = "contactPersonName")
    private String contactPersonName;
    @Size(max = 45)
    @Column(name = "contactPersonNumber")
    private String contactPersonNumber;
    @Size(max = 1000)
    @Column(name = "reasonModification")
    private String reasonModification;
    @Size(max = 100)
    @Column(name = "storageType")
    private String storageType;
    @Column(name = "warehouseRental")
    private Double warehouseRental;
    @Column(name = "amountOfProduct")
    private BigInteger amountOfProduct;
    @Column(name = "spacePerProduct")
    private Double spacePerProduct;
    @Column(name = "warehouseId")
    private Integer warehouseId;
    @JoinTable(name = "Contract_has_Post", joinColumns = {
        @JoinColumn(name = "contract", referencedColumnName = "contractId")}, inverseJoinColumns = {
        @JoinColumn(name = "post", referencedColumnName = "postId")})
    @ManyToMany
    private List<Post> postList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contract")
    private List<ServicePO> servicePOList;
    @JoinColumn(name = "serviceQuotation", referencedColumnName = "quotationId")
    @ManyToOne(optional = false)
    private ServiceQuotation serviceQuotation;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "contract")
    private SignedContract signedContract;

    public Contract() {
    }

    public Contract(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getPartyA() {
        return partyA;
    }

    public void setPartyA(Integer partyA) {
        this.partyA = partyA;
    }

    public Integer getPartyB() {
        return partyB;
    }

    public void setPartyB(Integer partyB) {
        this.partyB = partyB;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Integer contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonNumber() {
        return contactPersonNumber;
    }

    public void setContactPersonNumber(String contactPersonNumber) {
        this.contactPersonNumber = contactPersonNumber;
    }

    public String getReasonModification() {
        return reasonModification;
    }

    public void setReasonModification(String reasonModification) {
        this.reasonModification = reasonModification;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Double getWarehouseRental() {
        return warehouseRental;
    }

    public void setWarehouseRental(Double warehouseRental) {
        this.warehouseRental = warehouseRental;
    }

    public BigInteger getAmountOfProduct() {
        return amountOfProduct;
    }

    public void setAmountOfProduct(BigInteger amountOfProduct) {
        this.amountOfProduct = amountOfProduct;
    }

    public Double getSpacePerProduct() {
        return spacePerProduct;
    }

    public void setSpacePerProduct(Double spacePerProduct) {
        this.spacePerProduct = spacePerProduct;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @XmlTransient
    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    @XmlTransient
    public List<ServicePO> getServicePOList() {
        return servicePOList;
    }

    public void setServicePOList(List<ServicePO> servicePOList) {
        this.servicePOList = servicePOList;
    }

    public ServiceQuotation getServiceQuotation() {
        return serviceQuotation;
    }

    public void setServiceQuotation(ServiceQuotation serviceQuotation) {
        this.serviceQuotation = serviceQuotation;
    }

    public SignedContract getSignedContract() {
        return signedContract;
    }

    public void setSignedContract(SignedContract signedContract) {
        this.signedContract = signedContract;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contractId != null ? contractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contract)) {
            return false;
        }
        Contract other = (Contract) object;
        if ((this.contractId == null && other.contractId != null) || (this.contractId != null && !this.contractId.equals(other.contractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Contract[ contractId=" + contractId + " ]";
    }
    
}
