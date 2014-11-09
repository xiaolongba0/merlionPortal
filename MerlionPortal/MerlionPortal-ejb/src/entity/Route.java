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
@Table(name = "Route")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Route.findAll", query = "SELECT r FROM Route r"),
    @NamedQuery(name = "Route.findByRouteId", query = "SELECT r FROM Route r WHERE r.routeId = :routeId"),
    @NamedQuery(name = "Route.findByRouteType", query = "SELECT r FROM Route r WHERE r.routeType = :routeType"),
    @NamedQuery(name = "Route.findByOrigin", query = "SELECT r FROM Route r WHERE r.origin = :origin"),
    @NamedQuery(name = "Route.findByDestination", query = "SELECT r FROM Route r WHERE r.destination = :destination"),
    @NamedQuery(name = "Route.findByDistance", query = "SELECT r FROM Route r WHERE r.distance = :distance"),
    @NamedQuery(name = "Route.findByEndNodeId", query = "SELECT r FROM Route r WHERE r.endNodeId = :endNodeId")})
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
    @Column(name = "endNodeId")
    private Integer endNodeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "route")
    private List<AssetSchedule> assetScheduleList;
    @JoinColumn(name = "startNodeId", referencedColumnName = "nodeId")
    @ManyToOne(optional = false)
    private Node startNodeId;

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

    public Integer getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(Integer endNodeId) {
        this.endNodeId = endNodeId;
    }

    @XmlTransient
    public List<AssetSchedule> getAssetScheduleList() {
        return assetScheduleList;
    }

    public void setAssetScheduleList(List<AssetSchedule> assetScheduleList) {
        this.assetScheduleList = assetScheduleList;
    }

    public Node getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(Node startNodeId) {
        this.startNodeId = startNodeId;
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
