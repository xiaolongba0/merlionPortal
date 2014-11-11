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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "TransportationAsset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransportationAsset.findAll", query = "SELECT t FROM TransportationAsset t"),
    @NamedQuery(name = "TransportationAsset.findByAssetId", query = "SELECT t FROM TransportationAsset t WHERE t.assetId = :assetId"),
    @NamedQuery(name = "TransportationAsset.findByCapacity", query = "SELECT t FROM TransportationAsset t WHERE t.capacity = :capacity"),
    @NamedQuery(name = "TransportationAsset.findByAssetType", query = "SELECT t FROM TransportationAsset t WHERE t.assetType = :assetType"),
    @NamedQuery(name = "TransportationAsset.findByPrice", query = "SELECT t FROM TransportationAsset t WHERE t.price = :price"),
    @NamedQuery(name = "TransportationAsset.findBySpeed", query = "SELECT t FROM TransportationAsset t WHERE t.speed = :speed"),
    @NamedQuery(name = "TransportationAsset.findByStatus", query = "SELECT t FROM TransportationAsset t WHERE t.status = :status"),
    @NamedQuery(name = "TransportationAsset.findByIsAvailable", query = "SELECT t FROM TransportationAsset t WHERE t.isAvailable = :isAvailable"),
    @NamedQuery(name = "TransportationAsset.findByIsMaintain", query = "SELECT t FROM TransportationAsset t WHERE t.isMaintain = :isMaintain"),
    @NamedQuery(name = "TransportationAsset.findByRouteId", query = "SELECT t FROM TransportationAsset t WHERE t.routeId = :routeId"),
    @NamedQuery(name = "TransportationAsset.findByAssetLoad", query = "SELECT t FROM TransportationAsset t WHERE t.assetLoad = :assetLoad")})
public class TransportationAsset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "assetId")
    private Integer assetId;
    @Column(name = "capacity")
    private Integer capacity;
    @Size(max = 45)
    @Column(name = "assetType")
    private String assetType;
    @Column(name = "price")
    private Integer price;
    @Column(name = "speed")
    private Integer speed;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Column(name = "isAvailable")
    private Boolean isAvailable;
    @Column(name = "isMaintain")
    private Boolean isMaintain;
    @Column(name = "routeId")
    private Integer routeId;
    @Column(name = "assetLoad")
    private Integer assetLoad;
    @JoinTable(name = "TransporationAsset_has_TransportationOrder", joinColumns = {
        @JoinColumn(name = "TransporationAsset_assetId", referencedColumnName = "assetId")}, inverseJoinColumns = {
        @JoinColumn(name = "TransportationOrder_transportationOrderId", referencedColumnName = "transportationOrderId")})
    @ManyToMany
    private List<TransportationOrder> transportationOrderList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transportationAsset")
    private List<PlanAssetSchedule> planAssetScheduleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transporationAssetassetId")
    private List<AssetSchedule> assetScheduleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transporationAssetassetId")
    private List<MaintenanceLog> maintenanceLogList;
    @JoinColumn(name = "Location_locationId", referencedColumnName = "locationId")
    @ManyToOne(optional = false)
    private Location locationlocationId;

    public TransportationAsset() {
    }

    public TransportationAsset(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getIsMaintain() {
        return isMaintain;
    }

    public void setIsMaintain(Boolean isMaintain) {
        this.isMaintain = isMaintain;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getAssetLoad() {
        return assetLoad;
    }

    public void setAssetLoad(Integer assetLoad) {
        this.assetLoad = assetLoad;
    }

    @XmlTransient
    public List<TransportationOrder> getTransportationOrderList() {
        return transportationOrderList;
    }

    public void setTransportationOrderList(List<TransportationOrder> transportationOrderList) {
        this.transportationOrderList = transportationOrderList;
    }

    @XmlTransient
    public List<PlanAssetSchedule> getPlanAssetScheduleList() {
        return planAssetScheduleList;
    }

    public void setPlanAssetScheduleList(List<PlanAssetSchedule> planAssetScheduleList) {
        this.planAssetScheduleList = planAssetScheduleList;
    }

    @XmlTransient
    public List<AssetSchedule> getAssetScheduleList() {
        return assetScheduleList;
    }

    public void setAssetScheduleList(List<AssetSchedule> assetScheduleList) {
        this.assetScheduleList = assetScheduleList;
    }

    @XmlTransient
    public List<MaintenanceLog> getMaintenanceLogList() {
        return maintenanceLogList;
    }

    public void setMaintenanceLogList(List<MaintenanceLog> maintenanceLogList) {
        this.maintenanceLogList = maintenanceLogList;
    }

    public Location getLocationlocationId() {
        return locationlocationId;
    }

    public void setLocationlocationId(Location locationlocationId) {
        this.locationlocationId = locationlocationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetId != null ? assetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransportationAsset)) {
            return false;
        }
        TransportationAsset other = (TransportationAsset) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransportationAsset[ assetId=" + assetId + " ]";
    }
    
}
