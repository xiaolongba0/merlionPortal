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
@Table(name = "ProductInvoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductInvoice.findAll", query = "SELECT p FROM ProductInvoice p"),
    @NamedQuery(name = "ProductInvoice.findByInvoiceId", query = "SELECT p FROM ProductInvoice p WHERE p.invoiceId = :invoiceId"),
    @NamedQuery(name = "ProductInvoice.findBySalesOrderId", query = "SELECT p FROM ProductInvoice p WHERE p.salesOrderId = :salesOrderId"),
    @NamedQuery(name = "ProductInvoice.findByTotalPrice", query = "SELECT p FROM ProductInvoice p WHERE p.totalPrice = :totalPrice"),
    @NamedQuery(name = "ProductInvoice.findByCustomerId", query = "SELECT p FROM ProductInvoice p WHERE p.customerId = :customerId"),
    @NamedQuery(name = "ProductInvoice.findByStatus", query = "SELECT p FROM ProductInvoice p WHERE p.status = :status"),
    @NamedQuery(name = "ProductInvoice.findByConditionText", query = "SELECT p FROM ProductInvoice p WHERE p.conditionText = :conditionText")})
public class ProductInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "invoiceId")
    private Integer invoiceId;
    @Column(name = "salesOrderId")
    private Integer salesOrderId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalPrice")
    private Double totalPrice;
    @Column(name = "customerId")
    private Integer customerId;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "conditionText")
    private String conditionText;

    public ProductInvoice() {
    }

    public ProductInvoice(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(Integer salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceId != null ? invoiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductInvoice)) {
            return false;
        }
        ProductInvoice other = (ProductInvoice) object;
        if ((this.invoiceId == null && other.invoiceId != null) || (this.invoiceId != null && !this.invoiceId.equals(other.invoiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductInvoice[ invoiceId=" + invoiceId + " ]";
    }
    
}
