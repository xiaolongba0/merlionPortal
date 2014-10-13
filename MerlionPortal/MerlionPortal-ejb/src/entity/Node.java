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
@Table(name = "Node")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Node.findAll", query = "SELECT n FROM Node n"),
    @NamedQuery(name = "Node.findByNodeId", query = "SELECT n FROM Node n WHERE n.nodeId = :nodeId"),
    @NamedQuery(name = "Node.findByLocationName", query = "SELECT n FROM Node n WHERE n.locationName = :locationName"),
    @NamedQuery(name = "Node.findByLocationType", query = "SELECT n FROM Node n WHERE n.locationType = :locationType")})
public class Node implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nodeId")
    private Integer nodeId;
    @Size(max = 255)
    @Column(name = "locationName")
    private String locationName;
    @Size(max = 45)
    @Column(name = "locationType")
    private String locationType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "startNodeId")
    private List<Route> routeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nodeId")
    private List<Location> locationList;

    public Node() {
    }

    public Node(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @XmlTransient
    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    @XmlTransient
    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nodeId != null ? nodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Node)) {
            return false;
        }
        Node other = (Node) object;
        if ((this.nodeId == null && other.nodeId != null) || (this.nodeId != null && !this.nodeId.equals(other.nodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Node[ nodeId=" + nodeId + " ]";
    }
    
}
