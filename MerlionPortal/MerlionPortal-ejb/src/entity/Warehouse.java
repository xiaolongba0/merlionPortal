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
@Table(name = "Warehouse")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Warehouse.findAll", query = "SELECT w FROM Warehouse w"),
    @NamedQuery(name = "Warehouse.findByWarehouseId", query = "SELECT w FROM Warehouse w WHERE w.warehouseId = :warehouseId"),
    @NamedQuery(name = "Warehouse.findByName", query = "SELECT w FROM Warehouse w WHERE w.name = :name"),
    @NamedQuery(name = "Warehouse.findByCountry", query = "SELECT w FROM Warehouse w WHERE w.country = :country"),
    @NamedQuery(name = "Warehouse.findByCity", query = "SELECT w FROM Warehouse w WHERE w.city = :city"),
    @NamedQuery(name = "Warehouse.findByStreet", query = "SELECT w FROM Warehouse w WHERE w.street = :street"),
    @NamedQuery(name = "Warehouse.findByBlock", query = "SELECT w FROM Warehouse w WHERE w.block = :block"),
    @NamedQuery(name = "Warehouse.findByZipcode", query = "SELECT w FROM Warehouse w WHERE w.zipcode = :zipcode")})
public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "warehouseId")
    private Integer warehouseId;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "country")
    private String country;
    @Size(max = 45)
    @Column(name = "city")
    private String city;
    @Size(max = 45)
    @Column(name = "street")
    private String street;
    @Size(max = 45)
    @Column(name = "block")
    private String block;
    @Column(name = "zipcode")
    private Integer zipcode;
    @JoinColumn(name = "Company_companyId", referencedColumnName = "companyId")
    @ManyToOne(optional = false)
    private Company companycompanyId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehousewarehouseId")
    private List<StorageType> storageTypeList;

    public Warehouse() {
    }

    public Warehouse(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Company getCompanycompanyId() {
        return companycompanyId;
    }

    public void setCompanycompanyId(Company companycompanyId) {
        this.companycompanyId = companycompanyId;
    }

    @XmlTransient
    public List<StorageType> getStorageTypeList() {
        return storageTypeList;
    }

    public void setStorageTypeList(List<StorageType> storageTypeList) {
        this.storageTypeList = storageTypeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (warehouseId != null ? warehouseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Warehouse)) {
            return false;
        }
        Warehouse other = (Warehouse) object;
        if ((this.warehouseId == null && other.warehouseId != null) || (this.warehouseId != null && !this.warehouseId.equals(other.warehouseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Warehouse[ warehouseId=" + warehouseId + " ]";
    }
    
}
