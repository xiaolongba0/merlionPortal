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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "QuotationLineItem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuotationLineItem.findAll", query = "SELECT q FROM QuotationLineItem q"),
    @NamedQuery(name = "QuotationLineItem.findByLine", query = "SELECT q FROM QuotationLineItem q WHERE q.line = :line"),
    @NamedQuery(name = "QuotationLineItem.findByLineItemPrice", query = "SELECT q FROM QuotationLineItem q WHERE q.lineItemPrice = :lineItemPrice")})
public class QuotationLineItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "line")
    private Integer line;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "lineItemPrice")
    private Double lineItemPrice;
    @JoinColumn(name = "Quotation_quotationId", referencedColumnName = "quotationId")
    @ManyToOne(optional = false)
    private Quotation quotationquotationId;
    @JoinColumn(name = "Product_productId", referencedColumnName = "productId")
    @ManyToOne(optional = false)
    private Product productproductId;

    public QuotationLineItem() {
    }

    public QuotationLineItem(Integer line) {
        this.line = line;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Double getLineItemPrice() {
        return lineItemPrice;
    }

    public void setLineItemPrice(Double lineItemPrice) {
        this.lineItemPrice = lineItemPrice;
    }

    public Quotation getQuotationquotationId() {
        return quotationquotationId;
    }

    public void setQuotationquotationId(Quotation quotationquotationId) {
        this.quotationquotationId = quotationquotationId;
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
        if (!(object instanceof QuotationLineItem)) {
            return false;
        }
        QuotationLineItem other = (QuotationLineItem) object;
        if ((this.line == null && other.line != null) || (this.line != null && !this.line.equals(other.line))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.QuotationLineItem[ line=" + line + " ]";
    }
    
}
