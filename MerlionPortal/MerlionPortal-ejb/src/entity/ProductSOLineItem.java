/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "ProductSOLineItem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductSOLineItem.findAll", query = "SELECT p FROM ProductSOLineItem p"),
    @NamedQuery(name = "ProductSOLineItem.findByProductproductId", query = "SELECT p FROM ProductSOLineItem p WHERE p.productSOLineItemPK.productproductId = :productproductId"),
    @NamedQuery(name = "ProductSOLineItem.findByProductSOHeaderproductSOId", query = "SELECT p FROM ProductSOLineItem p WHERE p.productSOLineItemPK.productSOHeaderproductSOId = :productSOHeaderproductSOId"),
    @NamedQuery(name = "ProductSOLineItem.findByStatus", query = "SELECT p FROM ProductSOLineItem p WHERE p.status = :status"),
    @NamedQuery(name = "ProductSOLineItem.findByQuantity", query = "SELECT p FROM ProductSOLineItem p WHERE p.quantity = :quantity")})
public class ProductSOLineItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductSOLineItemPK productSOLineItemPK;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Column(name = "quantity")
    private Integer quantity;
    @JoinColumn(name = "ProductSOHeader_productSOId", referencedColumnName = "productSOId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductSOHeader productSOHeader;
    @JoinColumn(name = "Product_productId", referencedColumnName = "productId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public ProductSOLineItem() {
    }

    public ProductSOLineItem(ProductSOLineItemPK productSOLineItemPK) {
        this.productSOLineItemPK = productSOLineItemPK;
    }

    public ProductSOLineItem(int productproductId, int productSOHeaderproductSOId) {
        this.productSOLineItemPK = new ProductSOLineItemPK(productproductId, productSOHeaderproductSOId);
    }

    public ProductSOLineItemPK getProductSOLineItemPK() {
        return productSOLineItemPK;
    }

    public void setProductSOLineItemPK(ProductSOLineItemPK productSOLineItemPK) {
        this.productSOLineItemPK = productSOLineItemPK;
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

    public ProductSOHeader getProductSOHeader() {
        return productSOHeader;
    }

    public void setProductSOHeader(ProductSOHeader productSOHeader) {
        this.productSOHeader = productSOHeader;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productSOLineItemPK != null ? productSOLineItemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductSOLineItem)) {
            return false;
        }
        ProductSOLineItem other = (ProductSOLineItem) object;
        if ((this.productSOLineItemPK == null && other.productSOLineItemPK != null) || (this.productSOLineItemPK != null && !this.productSOLineItemPK.equals(other.productSOLineItemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductSOLineItem[ productSOLineItemPK=" + productSOLineItemPK + " ]";
    }
    
}
