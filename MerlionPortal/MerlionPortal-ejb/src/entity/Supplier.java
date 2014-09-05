/*
 * To change this template, choose Tools | Templates
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
 * @author MelodyPond_2
 */
@Entity
@Table(name = "supplier")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findBySupplierCompanyId", query = "SELECT s FROM Supplier s WHERE s.supplierCompanyId = :supplierCompanyId"),
    @NamedQuery(name = "Supplier.findByContactPerson", query = "SELECT s FROM Supplier s WHERE s.contactPerson = :contactPerson"),
    @NamedQuery(name = "Supplier.findByContactNumber", query = "SELECT s FROM Supplier s WHERE s.contactNumber = :contactNumber"),
    @NamedQuery(name = "Supplier.findByDescription", query = "SELECT s FROM Supplier s WHERE s.description = :description"),
    @NamedQuery(name = "Supplier.findByContactEmail", query = "SELECT s FROM Supplier s WHERE s.contactEmail = :contactEmail")})
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "supplierCompanyId")
    private Integer supplierCompanyId;
    @Size(max = 45)
    @Column(name = "contactPerson")
    private String contactPerson;
    @Size(max = 45)
    @Column(name = "contactNumber")
    private String contactNumber;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @Size(max = 45)
    @Column(name = "contactEmail")
    private String contactEmail;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "suppliersupplierCompanyId")
    private List<Component> componentList;

    public Supplier() {
    }

    public Supplier(Integer supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public Integer getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Integer supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @XmlTransient
    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supplierCompanyId != null ? supplierCompanyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.supplierCompanyId == null && other.supplierCompanyId != null) || (this.supplierCompanyId != null && !this.supplierCompanyId.equals(other.supplierCompanyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Supplier[ supplierCompanyId=" + supplierCompanyId + " ]";
    }
    
}
