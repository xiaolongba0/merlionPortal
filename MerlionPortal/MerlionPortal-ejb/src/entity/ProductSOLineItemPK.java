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
public class ProductSOLineItemPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "Product_productId")
    private int productproductId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ProductSOHeader_productSOId")
    private int productSOHeaderproductSOId;

    public ProductSOLineItemPK() {
    }

    public ProductSOLineItemPK(int productproductId, int productSOHeaderproductSOId) {
        this.productproductId = productproductId;
        this.productSOHeaderproductSOId = productSOHeaderproductSOId;
    }

    public int getProductproductId() {
        return productproductId;
    }

    public void setProductproductId(int productproductId) {
        this.productproductId = productproductId;
    }

    public int getProductSOHeaderproductSOId() {
        return productSOHeaderproductSOId;
    }

    public void setProductSOHeaderproductSOId(int productSOHeaderproductSOId) {
        this.productSOHeaderproductSOId = productSOHeaderproductSOId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productproductId;
        hash += (int) productSOHeaderproductSOId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductSOLineItemPK)) {
            return false;
        }
        ProductSOLineItemPK other = (ProductSOLineItemPK) object;
        if (this.productproductId != other.productproductId) {
            return false;
        }
        if (this.productSOHeaderproductSOId != other.productSOHeaderproductSOId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductSOLineItemPK[ productproductId=" + productproductId + ", productSOHeaderproductSOId=" + productSOHeaderproductSOId + " ]";
    }
    
}
