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
@Table(name = "StorageLocation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StorageLocation.findAll", query = "SELECT s FROM StorageLocation s"),
    @NamedQuery(name = "StorageLocation.findByStorageLocationId", query = "SELECT s FROM StorageLocation s WHERE s.storageLocationId = :storageLocationId"),
    @NamedQuery(name = "StorageLocation.findByName", query = "SELECT s FROM StorageLocation s WHERE s.name = :name"),
    @NamedQuery(name = "StorageLocation.findByDescription", query = "SELECT s FROM StorageLocation s WHERE s.description = :description")})
public class StorageLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "storageLocationId")
    private Integer storageLocationId;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storageLocationstorageLocationId")
    private List<StorageBin> storageBinList;
    @JoinColumn(name = "Warehouse_warehouseId", referencedColumnName = "warehouseId")
    @ManyToOne(optional = false)
    private Warehouse warehousewarehouseId;

    public StorageLocation() {
    }

    public StorageLocation(Integer storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public Integer getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(Integer storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<StorageBin> getStorageBinList() {
        return storageBinList;
    }

    public void setStorageBinList(List<StorageBin> storageBinList) {
        this.storageBinList = storageBinList;
    }

    public Warehouse getWarehousewarehouseId() {
        return warehousewarehouseId;
    }

    public void setWarehousewarehouseId(Warehouse warehousewarehouseId) {
        this.warehousewarehouseId = warehousewarehouseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storageLocationId != null ? storageLocationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StorageLocation)) {
            return false;
        }
        StorageLocation other = (StorageLocation) object;
        if ((this.storageLocationId == null && other.storageLocationId != null) || (this.storageLocationId != null && !this.storageLocationId.equals(other.storageLocationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StorageLocation[ storageLocationId=" + storageLocationId + " ]";
    }
    
}
