/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MelodyPond_2
 */
@Embeddable
public class ProductPOLineItemPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "Product_productId")
    private int productproductId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ProductPOHeader_productPOId")
    private int productPOHeaderproductPOId;

    public ProductPOLineItemPK() {
    }

    public ProductPOLineItemPK(int productproductId, int productPOHeaderproductPOId) {
        this.productproductId = productproductId;
        this.productPOHeaderproductPOId = productPOHeaderproductPOId;
    }

    public int getProductproductId() {
        return productproductId;
    }

    public void setProductproductId(int productproductId) {
        this.productproductId = productproductId;
    }

    public int getProductPOHeaderproductPOId() {
        return productPOHeaderproductPOId;
    }

    public void setProductPOHeaderproductPOId(int productPOHeaderproductPOId) {
        this.productPOHeaderproductPOId = productPOHeaderproductPOId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productproductId;
        hash += (int) productPOHeaderproductPOId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductPOLineItemPK)) {
            return false;
        }
        ProductPOLineItemPK other = (ProductPOLineItemPK) object;
        if (this.productproductId != other.productproductId) {
            return false;
        }
        if (this.productPOHeaderproductPOId != other.productPOHeaderproductPOId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductPOLineItemPK[ productproductId=" + productproductId + ", productPOHeaderproductPOId=" + productPOHeaderproductPOId + " ]";
    }
    
}
