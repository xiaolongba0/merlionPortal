/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

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
    @NamedQuery(name = "StorageBin.findByMaxWeight", query = "SELECT s FROM StorageBin s WHERE s.maxWeight = :maxWeight")})
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
    @JoinColumn(name = "StorageType_storageTypeId", referencedColumnName = "storageTypeId")
    @ManyToOne(optional = false)
    private StorageType storageTypestorageTypeId;

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

    public StorageType getStorageTypestorageTypeId() {
        return storageTypestorageTypeId;
    }

    public void setStorageTypestorageTypeId(StorageType storageTypestorageTypeId) {
        this.storageTypestorageTypeId = storageTypestorageTypeId;
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
