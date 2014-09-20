/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "Route")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Route.findAll", query = "SELECT r FROM Route r"),
    @NamedQuery(name = "Route.findByRouteId", query = "SELECT r FROM Route r WHERE r.routeId = :routeId"),
    @NamedQuery(name = "Route.findByRouteType", query = "SELECT r FROM Route r WHERE r.routeType = :routeType"),
    @NamedQuery(name = "Route.findByOrigin", query = "SELECT r FROM Route r WHERE r.origin = :origin"),
    @NamedQuery(name = "Route.findByDestination", query = "SELECT r FROM Route r WHERE r.destination = :destination"),
    @NamedQuery(name = "Route.findByDistance", query = "SELECT r FROM Route r WHERE r.distance = :distance")})
public class Route implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "routeId")
    private Integer routeId;
    @Size(max = 45)
    @Column(name = "routeType")
    private String routeType;
    @Size(max = 45)
    @Column(name = "origin")
    private String origin;
    @Size(max = 45)
    @Column(name = "destination")
    private String destination;
    @Column(name = "distance")
    private Integer distance;
    @JoinTable(name = "TransporationAsset_has_Route", joinColumns = {
        @JoinColumn(name = "Route_routeId", referencedColumnName = "routeId")}, inverseJoinColumns = {
        @JoinColumn(name = "TransporationAsset_assetId", referencedColumnName = "assetId")})
    @ManyToMany
    private List<TransportationAsset> transportationAssetList;

    public Route() {
    }

    public Route(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @XmlTransient
    public List<TransportationAsset> getTransportationAssetList() {
        return transportationAssetList;
    }

    public void setTransportationAssetList(List<TransportationAsset> transportationAssetList) {
        this.transportationAssetList = transportationAssetList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (routeId != null ? routeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Route)) {
            return false;
        }
        Route other = (Route) object;
        if ((this.routeId == null && other.routeId != null) || (this.routeId != null && !this.routeId.equals(other.routeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Route[ routeId=" + routeId + " ]";
    }
    
}
