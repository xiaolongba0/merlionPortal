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
@Table(name = "ProductOrderLineItem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductOrderLineItem.findAll", query = "SELECT p FROM ProductOrderLineItem p"),
    @NamedQuery(name = "ProductOrderLineItem.findByLine", query = "SELECT p FROM ProductOrderLineItem p WHERE p.line = :line"),
    @NamedQuery(name = "ProductOrderLineItem.findByStatus", query = "SELECT p FROM ProductOrderLineItem p WHERE p.status = :status"),
    @NamedQuery(name = "ProductOrderLineItem.findByQuantity", query = "SELECT p FROM ProductOrderLineItem p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "ProductOrderLineItem.findByPrice", query = "SELECT p FROM ProductOrderLineItem p WHERE p.price = :price")})
public class ProductOrderLineItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "line")
    private Integer line;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Column(name = "quantity")
    private Integer quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @JoinColumn(name = "ProductOrder_productPOId", referencedColumnName = "productPOId")
    @ManyToOne(optional = false)
    private ProductOrder productOrderproductPOId;
    @JoinColumn(name = "Product_productId", referencedColumnName = "productId")
    @ManyToOne(optional = false)
    private Product productproductId;

    public ProductOrderLineItem() {
    }

    public ProductOrderLineItem(Integer line) {
        this.line = line;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductOrder getProductOrderproductPOId() {
        return productOrderproductPOId;
    }

    public void setProductOrderproductPOId(ProductOrder productOrderproductPOId) {
        this.productOrderproductPOId = productOrderproductPOId;
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
        hash += (line != null ? line.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductOrderLineItem)) {
            return false;
        }
        ProductOrderLineItem other = (ProductOrderLineItem) object;
        if ((this.line == null && other.line != null) || (this.line != null && !this.line.equals(other.line))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductOrderLineItem[ line=" + line + " ]";
    }
    
}
