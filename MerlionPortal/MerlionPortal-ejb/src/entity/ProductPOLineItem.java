/*
 * To change this template, choose Tools | Templates
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MelodyPond_2
 */
@Entity
@Table(name = "productpolineitem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductPOLineItem.findAll", query = "SELECT p FROM ProductPOLineItem p"),
    @NamedQuery(name = "ProductPOLineItem.findByProductproductId", query = "SELECT p FROM ProductPOLineItem p WHERE p.productPOLineItemPK.productproductId = :productproductId"),
    @NamedQuery(name = "ProductPOLineItem.findByProductPOHeaderproductPOId", query = "SELECT p FROM ProductPOLineItem p WHERE p.productPOLineItemPK.productPOHeaderproductPOId = :productPOHeaderproductPOId"),
    @NamedQuery(name = "ProductPOLineItem.findByQuantity", query = "SELECT p FROM ProductPOLineItem p WHERE p.quantity = :quantity")})
public class ProductPOLineItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductPOLineItemPK productPOLineItemPK;
    @Column(name = "quantity")
    private Integer quantity;
    @JoinColumn(name = "ProductPOHeader_productPOId", referencedColumnName = "productPOId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductPOHeader productPOHeader;
    @JoinColumn(name = "Product_productId", referencedColumnName = "productId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public ProductPOLineItem() {
    }

    public ProductPOLineItem(ProductPOLineItemPK productPOLineItemPK) {
        this.productPOLineItemPK = productPOLineItemPK;
    }

    public ProductPOLineItem(int productproductId, int productPOHeaderproductPOId) {
        this.productPOLineItemPK = new ProductPOLineItemPK(productproductId, productPOHeaderproductPOId);
    }

    public ProductPOLineItemPK getProductPOLineItemPK() {
        return productPOLineItemPK;
    }

    public void setProductPOLineItemPK(ProductPOLineItemPK productPOLineItemPK) {
        this.productPOLineItemPK = productPOLineItemPK;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductPOHeader getProductPOHeader() {
        return productPOHeader;
    }

    public void setProductPOHeader(ProductPOHeader productPOHeader) {
        this.productPOHeader = productPOHeader;
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
        hash += (productPOLineItemPK != null ? productPOLineItemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductPOLineItem)) {
            return false;
        }
        ProductPOLineItem other = (ProductPOLineItem) object;
        if ((this.productPOLineItemPK == null && other.productPOLineItemPK != null) || (this.productPOLineItemPK != null && !this.productPOLineItemPK.equals(other.productPOLineItemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductPOLineItem[ productPOLineItemPK=" + productPOLineItemPK + " ]";
    }
    
}
