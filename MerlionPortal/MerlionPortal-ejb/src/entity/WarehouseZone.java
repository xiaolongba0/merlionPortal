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
@Table(name = "WarehouseZone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WarehouseZone.findAll", query = "SELECT w FROM WarehouseZone w"),
    @NamedQuery(name = "WarehouseZone.findByWarehouseZoneId", query = "SELECT w FROM WarehouseZone w WHERE w.warehouseZoneId = :warehouseZoneId"),
    @NamedQuery(name = "WarehouseZone.findByName", query = "SELECT w FROM WarehouseZone w WHERE w.name = :name"),
    @NamedQuery(name = "WarehouseZone.findByDescription", query = "SELECT w FROM WarehouseZone w WHERE w.description = :description")})
public class WarehouseZone implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "warehouseZoneId")
    private Integer warehouseZoneId;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "warehouse", referencedColumnName = "warehouseId")
    @ManyToOne(optional = false)
    private Warehouse warehouse;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouseZone")
    private List<StorageBin> storageBinList;

    public WarehouseZone() {
    }

    public WarehouseZone(Integer warehouseZoneId) {
        this.warehouseZoneId = warehouseZoneId;
    }

    public Integer getWarehouseZoneId() {
        return warehouseZoneId;
    }

    public void setWarehouseZoneId(Integer warehouseZoneId) {
        this.warehouseZoneId = warehouseZoneId;
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

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @XmlTransient
    public List<StorageBin> getStorageBinList() {
        return storageBinList;
    }

    public void setStorageBinList(List<StorageBin> storageBinList) {
        this.storageBinList = storageBinList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (warehouseZoneId != null ? warehouseZoneId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WarehouseZone)) {
            return false;
        }
        WarehouseZone other = (WarehouseZone) object;
        if ((this.warehouseZoneId == null && other.warehouseZoneId != null) || (this.warehouseZoneId != null && !this.warehouseZoneId.equals(other.warehouseZoneId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.WarehouseZone[ warehouseZoneId=" + warehouseZoneId + " ]";
    }
    
}
