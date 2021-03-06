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
@Table(name = "StorageBin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StorageBin.findAll", query = "SELECT s FROM StorageBin s"),
    @NamedQuery(name = "StorageBin.findByStorageBinId", query = "SELECT s FROM StorageBin s WHERE s.storageBinId = :storageBinId"),
    @NamedQuery(name = "StorageBin.findByDescription", query = "SELECT s FROM StorageBin s WHERE s.description = :description"),
    @NamedQuery(name = "StorageBin.findByBinName", query = "SELECT s FROM StorageBin s WHERE s.binName = :binName"),
    @NamedQuery(name = "StorageBin.findByBinType", query = "SELECT s FROM StorageBin s WHERE s.binType = :binType"),
    @NamedQuery(name = "StorageBin.findByMaxQuantity", query = "SELECT s FROM StorageBin s WHERE s.maxQuantity = :maxQuantity"),
    @NamedQuery(name = "StorageBin.findByMaxWeight", query = "SELECT s FROM StorageBin s WHERE s.maxWeight = :maxWeight"),
    @NamedQuery(name = "StorageBin.findByInuseSpace", query = "SELECT s FROM StorageBin s WHERE s.inuseSpace = :inuseSpace"),
    @NamedQuery(name = "StorageBin.findByReservedSpace", query = "SELECT s FROM StorageBin s WHERE s.reservedSpace = :reservedSpace"),
    @NamedQuery(name = "StorageBin.findByAvailableSpace", query = "SELECT s FROM StorageBin s WHERE s.availableSpace = :availableSpace"),
    @NamedQuery(name = "StorageBin.findByRentedCompanyId", query = "SELECT s FROM StorageBin s WHERE s.rentedCompanyId = :rentedCompanyId"),
    @NamedQuery(name = "StorageBin.findByRented", query = "SELECT s FROM StorageBin s WHERE s.rented = :rented")})
public class StorageBin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "storageBinId")
    private Integer storageBinId;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @Size(max = 45)
    @Column(name = "binName")
    private String binName;
    @Size(max = 45)
    @Column(name = "binType")
    private String binType;
    @Column(name = "maxQuantity")
    private Integer maxQuantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "maxWeight")
    private Double maxWeight;
    @Column(name = "inuseSpace")
    private Integer inuseSpace;
    @Column(name = "reservedSpace")
    private Integer reservedSpace;
    @Column(name = "availableSpace")
    private Integer availableSpace;
    @Column(name = "rentedCompanyId")
    private Integer rentedCompanyId;
    @Column(name = "rented")
    private Boolean rented;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storageBin")
    private List<Stock> stockList;
    @JoinColumn(name = "warehouseZone", referencedColumnName = "warehouseZoneId")
    @ManyToOne(optional = false)
    private WarehouseZone warehouseZone;

    public StorageBin() {
    }

    public StorageBin(Integer storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Integer getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Integer storageBinId) {
        this.storageBinId = storageBinId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getInuseSpace() {
        return inuseSpace;
    }

    public void setInuseSpace(Integer inuseSpace) {
        this.inuseSpace = inuseSpace;
    }

    public Integer getReservedSpace() {
        return reservedSpace;
    }

    public void setReservedSpace(Integer reservedSpace) {
        this.reservedSpace = reservedSpace;
    }

    public Integer getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(Integer availableSpace) {
        this.availableSpace = availableSpace;
    }

    public Integer getRentedCompanyId() {
        return rentedCompanyId;
    }

    public void setRentedCompanyId(Integer rentedCompanyId) {
        this.rentedCompanyId = rentedCompanyId;
    }

    public Boolean getRented() {
        return rented;
    }

    public void setRented(Boolean rented) {
        this.rented = rented;
    }

    @XmlTransient
    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public WarehouseZone getWarehouseZone() {
        return warehouseZone;
    }

    public void setWarehouseZone(WarehouseZone warehouseZone) {
        this.warehouseZone = warehouseZone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storageBinId != null ? storageBinId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StorageBin)) {
            return false;
        }
        StorageBin other = (StorageBin) object;
        if ((this.storageBinId == null && other.storageBinId != null) || (this.storageBinId != null && !this.storageBinId.equals(other.storageBinId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StorageBin[ storageBinId=" + storageBinId + " ]";
    }
    
}
