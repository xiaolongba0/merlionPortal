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
@Table(name = "Location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
    @NamedQuery(name = "Location.findByLocationId", query = "SELECT l FROM Location l WHERE l.locationId = :locationId"),
    @NamedQuery(name = "Location.findByLocationName", query = "SELECT l FROM Location l WHERE l.locationName = :locationName"),
    @NamedQuery(name = "Location.findByLocationPosition", query = "SELECT l FROM Location l WHERE l.locationPosition = :locationPosition")})
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "locationId")
    private Integer locationId;
    @Size(max = 45)
    @Column(name = "locationName")
    private String locationName;
    @Size(max = 255)
    @Column(name = "locationPosition")
    private String locationPosition;
    @JoinColumn(name = "ProductContract_productContractId", referencedColumnName = "productContractId")
    @ManyToOne(optional = false)
    private ProductContract productContractproductContractId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationlocationId")
    private List<TransporationAsset> transporationAssetList;

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

    public String getLocationPosition() {
        return locationPosition;
    }

    public void setLocationPosition(String locationPosition) {
        this.locationPosition = locationPosition;
    }

    public ProductContract getProductContractproductContractId() {
        return productContractproductContractId;
    }

    public void setProductContractproductContractId(ProductContract productContractproductContractId) {
        this.productContractproductContractId = productContractproductContractId;
    }

    @XmlTransient
    public List<TransporationAsset> getTransporationAssetList() {
        return transporationAssetList;
    }

    public void setTransporationAssetList(List<TransporationAsset> transporationAssetList) {
        this.transporationAssetList = transporationAssetList;
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
