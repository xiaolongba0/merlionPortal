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
import javax.persistence.ManyToMany;
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
@Table(name = "TransportationOrder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransportationOrder.findAll", query = "SELECT t FROM TransportationOrder t"),
    @NamedQuery(name = "TransportationOrder.findByTransportationOrderId", query = "SELECT t FROM TransportationOrder t WHERE t.transportationOrderId = :transportationOrderId"),
    @NamedQuery(name = "TransportationOrder.findByCompanyId", query = "SELECT t FROM TransportationOrder t WHERE t.companyId = :companyId"),
    @NamedQuery(name = "TransportationOrder.findByCreatorId", query = "SELECT t FROM TransportationOrder t WHERE t.creatorId = :creatorId"),
    @NamedQuery(name = "TransportationOrder.findByReferenceId", query = "SELECT t FROM TransportationOrder t WHERE t.referenceId = :referenceId"),
    @NamedQuery(name = "TransportationOrder.findByReferenceType", query = "SELECT t FROM TransportationOrder t WHERE t.referenceType = :referenceType"),
    @NamedQuery(name = "TransportationOrder.findByOrigin", query = "SELECT t FROM TransportationOrder t WHERE t.origin = :origin"),
    @NamedQuery(name = "TransportationOrder.findByDestination", query = "SELECT t FROM TransportationOrder t WHERE t.destination = :destination"),
    @NamedQuery(name = "TransportationOrder.findByTimeStart", query = "SELECT t FROM TransportationOrder t WHERE t.timeStart = :timeStart"),
    @NamedQuery(name = "TransportationOrder.findByTimeEnd", query = "SELECT t FROM TransportationOrder t WHERE t.timeEnd = :timeEnd"),
    @NamedQuery(name = "TransportationOrder.findByCargoType", query = "SELECT t FROM TransportationOrder t WHERE t.cargoType = :cargoType"),
    @NamedQuery(name = "TransportationOrder.findByCargoWeight", query = "SELECT t FROM TransportationOrder t WHERE t.cargoWeight = :cargoWeight")})
public class TransportationOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "transportationOrderId")
    private Integer transportationOrderId;
    @Column(name = "companyId")
    private Integer companyId;
    @Column(name = "creatorId")
    private Integer creatorId;
    @Column(name = "referenceId")
    private Integer referenceId;
    @Column(name = "referenceType")
    private Integer referenceType;
    @Size(max = 45)
    @Column(name = "origin")
    private String origin;
    @Size(max = 45)
    @Column(name = "destination")
    private String destination;
    @Column(name = "timeStart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStart;
    @Column(name = "timeEnd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEnd;
    @Size(max = 45)
    @Column(name = "cargoType")
    private String cargoType;
    @Column(name = "cargoWeight")
    private Integer cargoWeight;
    @ManyToMany(mappedBy = "transportationOrderList")
    private List<TransporationAsset> transporationAssetList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transportationOrdertransportationOrderId")
    private List<TransportationLog> transportationLogList;

    public TransportationOrder() {
    }

    public TransportationOrder(Integer transportationOrderId) {
        this.transportationOrderId = transportationOrderId;
    }

    public Integer getTransportationOrderId() {
        return transportationOrderId;
    }

    public void setTransportationOrderId(Integer transportationOrderId) {
        this.transportationOrderId = transportationOrderId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(Integer referenceType) {
        this.referenceType = referenceType;
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

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public Integer getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(Integer cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    @XmlTransient
    public List<TransporationAsset> getTransporationAssetList() {
        return transporationAssetList;
    }

    public void setTransporationAssetList(List<TransporationAsset> transporationAssetList) {
        this.transporationAssetList = transporationAssetList;
    }

    @XmlTransient
    public List<TransportationLog> getTransportationLogList() {
        return transportationLogList;
    }

    public void setTransportationLogList(List<TransportationLog> transportationLogList) {
        this.transportationLogList = transportationLogList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transportationOrderId != null ? transportationOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransportationOrder)) {
            return false;
        }
        TransportationOrder other = (TransportationOrder) object;
        if ((this.transportationOrderId == null && other.transportationOrderId != null) || (this.transportationOrderId != null && !this.transportationOrderId.equals(other.transportationOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransportationOrder[ transportationOrderId=" + transportationOrderId + " ]";
    }
    
}
