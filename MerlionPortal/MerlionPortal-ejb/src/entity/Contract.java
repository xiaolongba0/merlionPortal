/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
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
    @Size(max = 1000)
    @Column(name = "conditionText")
    private String conditionText;
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "startDate")
    private String startDate;
    @Size(max = 45)
    @Column(name = "endDate")
    private String endDate;
    @Size(max = 45)
    @Column(name = "createdDate")
    private String createdDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Size(max = 255)
    @Column(name = "origin")
    private String origin;
    @Size(max = 255)
    @Column(name = "destination")
    private String destination;
    @Column(name = "warehouseId")
    private Integer warehouseId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contract")
    private List<ServicePO> servicePOList;
    @JoinColumn(name = "serviceQuotation", referencedColumnName = "quotationId")
    @ManyToOne(optional = false)
    private ServiceQuotation serviceQuotation;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
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

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
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
