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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "TransporationAsset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransporationAsset.findAll", query = "SELECT t FROM TransporationAsset t"),
    @NamedQuery(name = "TransporationAsset.findByAssetId", query = "SELECT t FROM TransporationAsset t WHERE t.assetId = :assetId"),
    @NamedQuery(name = "TransporationAsset.findByCapacity", query = "SELECT t FROM TransporationAsset t WHERE t.capacity = :capacity"),
    @NamedQuery(name = "TransporationAsset.findByAssetType", query = "SELECT t FROM TransporationAsset t WHERE t.assetType = :assetType"),
    @NamedQuery(name = "TransporationAsset.findByPrice", query = "SELECT t FROM TransporationAsset t WHERE t.price = :price"),
    @NamedQuery(name = "TransporationAsset.findBySpeed", query = "SELECT t FROM TransporationAsset t WHERE t.speed = :speed"),
    @NamedQuery(name = "TransporationAsset.findByStatus", query = "SELECT t FROM TransporationAsset t WHERE t.status = :status"),
    @NamedQuery(name = "TransporationAsset.findByIsAvailable", query = "SELECT t FROM TransporationAsset t WHERE t.isAvailable = :isAvailable"),
    @NamedQuery(name = "TransporationAsset.findByIsMaintain", query = "SELECT t FROM TransporationAsset t WHERE t.isMaintain = :isMaintain"),
    @NamedQuery(name = "TransporationAsset.findByRouteId", query = "SELECT t FROM TransporationAsset t WHERE t.routeId = :routeId")})
public class TransporationAsset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "assetId")
    private Integer assetId;
    @Size(max = 45)
    @Column(name = "capacity")
    private String capacity;
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
    @JoinTable(name = "TransporationAsset_has_TransportationOrder", joinColumns = {
        @JoinColumn(name = "TransporationAsset_assetId", referencedColumnName = "assetId")}, inverseJoinColumns = {
        @JoinColumn(name = "TransportationOrder_transportationOrderId", referencedColumnName = "transportationOrderId")})
    @ManyToMany
    private List<TransportationOrder> transportationOrderList;
    @ManyToMany(mappedBy = "transporationAssetList")
    private List<Route> routeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transporationAssetassetId")
    private List<AssetSchedule> assetScheduleList;
    @JoinColumn(name = "Location_locationId", referencedColumnName = "locationId")
    @ManyToOne(optional = false)
    private Location locationlocationId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transporationAssetassetId")
    private List<MaintenanceLog> maintenanceLogList;

    public TransporationAsset() {
    }

    public TransporationAsset(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
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

    @XmlTransient
    public List<TransportationOrder> getTransportationOrderList() {
        return transportationOrderList;
    }

    public void setTransportationOrderList(List<TransportationOrder> transportationOrderList) {
        this.transportationOrderList = transportationOrderList;
    }

    @XmlTransient
    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    @XmlTransient
    public List<AssetSchedule> getAssetScheduleList() {
        return assetScheduleList;
    }

    public void setAssetScheduleList(List<AssetSchedule> assetScheduleList) {
        this.assetScheduleList = assetScheduleList;
    }

    public Location getLocationlocationId() {
        return locationlocationId;
    }

    public void setLocationlocationId(Location locationlocationId) {
        this.locationlocationId = locationlocationId;
    }

    @XmlTransient
    public List<MaintenanceLog> getMaintenanceLogList() {
        return maintenanceLogList;
    }

    public void setMaintenanceLogList(List<MaintenanceLog> maintenanceLogList) {
        this.maintenanceLogList = maintenanceLogList;
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
        if (!(object instanceof TransporationAsset)) {
            return false;
        }
        TransporationAsset other = (TransporationAsset) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransporationAsset[ assetId=" + assetId + " ]";
    }
    
}
