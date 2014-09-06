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
@Table(name = "Component")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Component.findAll", query = "SELECT c FROM Component c"),
    @NamedQuery(name = "Component.findByComponentId", query = "SELECT c FROM Component c WHERE c.componentId = :componentId"),
    @NamedQuery(name = "Component.findByComponentName", query = "SELECT c FROM Component c WHERE c.componentName = :componentName"),
    @NamedQuery(name = "Component.findByDescription", query = "SELECT c FROM Component c WHERE c.description = :description"),
    @NamedQuery(name = "Component.findByCost", query = "SELECT c FROM Component c WHERE c.cost = :cost"),
    @NamedQuery(name = "Component.findByCurrency", query = "SELECT c FROM Component c WHERE c.currency = :currency"),
    @NamedQuery(name = "Component.findByQuantity", query = "SELECT c FROM Component c WHERE c.quantity = :quantity"),
    @NamedQuery(name = "Component.findByLeadTime", query = "SELECT c FROM Component c WHERE c.leadTime = :leadTime")})
public class Component implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "componentId")
    private Integer componentId;
    @Size(max = 45)
    @Column(name = "componentName")
    private String componentName;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private Double cost;
    @Size(max = 45)
    @Column(name = "currency")
    private String currency;
    @Column(name = "Quantity")
    private Integer quantity;
    @Column(name = "leadTime")
    private Integer leadTime;
    @JoinColumn(name = "Supplier_supplierCompanyId", referencedColumnName = "supplierCompanyId")
    @ManyToOne(optional = false)
    private Supplier suppliersupplierCompanyId;
    @JoinColumn(name = "Product_productId", referencedColumnName = "productId")
    @ManyToOne(optional = false)
    private Product productproductId;

    public Component() {
    }

    public Component(Integer componentId) {
        this.componentId = componentId;
    }

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Supplier getSuppliersupplierCompanyId() {
        return suppliersupplierCompanyId;
    }

    public void setSuppliersupplierCompanyId(Supplier suppliersupplierCompanyId) {
        this.suppliersupplierCompanyId = suppliersupplierCompanyId;
    }

    public Product getProductproductId() {
        return productproductId;
    }

    public void setProductproductId(Product productproductId) {
        this.productproductId = productproductId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentId != null ? componentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Component)) {
            return false;
        }
        Component other = (Component) object;
        if ((this.componentId == null && other.componentId != null) || (this.componentId != null && !this.componentId.equals(other.componentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Component[ componentId=" + componentId + " ]";
    }
    
}
