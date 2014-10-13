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
@Table(name = "Location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
    @NamedQuery(name = "Location.findByLocationId", query = "SELECT l FROM Location l WHERE l.locationId = :locationId"),
    @NamedQuery(name = "Location.findByLocationName", query = "SELECT l FROM Location l WHERE l.locationName = :locationName"),
    @NamedQuery(name = "Location.findByLocationType", query = "SELECT l FROM Location l WHERE l.locationType = :locationType"),
    @NamedQuery(name = "Location.findByCompanyId", query = "SELECT l FROM Location l WHERE l.companyId = :companyId")})
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "locationId")
    private Integer locationId;
    @Size(max = 45)
    @Column(name = "locationName")
    private String locationName;
    @Size(max = 45)
    @Column(name = "locationType")
    private String locationType;
    @Column(name = "companyId")
    private Integer companyId;
    @JoinColumn(name = "nodeId", referencedColumnName = "nodeId")
    @ManyToOne(optional = false)
    private Node nodeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationlocationId")
    private List<TransportationAsset> transportationAssetList;

    public Location() {
    }

    public Location(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Node getNodeId() {
        return nodeId;
    }

    public void setNodeId(Node nodeId) {
        this.nodeId = nodeId;
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
        hash += (locationId != null ? locationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.locationId == null && other.locationId != null) || (this.locationId != null && !this.locationId.equals(other.locationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Location[ locationId=" + locationId + " ]";
    }
    
}
