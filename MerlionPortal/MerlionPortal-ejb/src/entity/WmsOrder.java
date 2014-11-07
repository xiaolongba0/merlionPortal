/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "WmsOrder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WmsOrder.findAll", query = "SELECT w FROM WmsOrder w"),
    @NamedQuery(name = "WmsOrder.findByOrderId", query = "SELECT w FROM WmsOrder w WHERE w.orderId = :orderId"),
    @NamedQuery(name = "WmsOrder.findByOrderType", query = "SELECT w FROM WmsOrder w WHERE w.orderType = :orderType"),
    @NamedQuery(name = "WmsOrder.findByFulfillmentDate", query = "SELECT w FROM WmsOrder w WHERE w.fulfillmentDate = :fulfillmentDate"),
    @NamedQuery(name = "WmsOrder.findByReceiveDate", query = "SELECT w FROM WmsOrder w WHERE w.receiveDate = :receiveDate"),
    @NamedQuery(name = "WmsOrder.findByQuantity", query = "SELECT w FROM WmsOrder w WHERE w.quantity = :quantity"),
    @NamedQuery(name = "WmsOrder.findByProductId", query = "SELECT w FROM WmsOrder w WHERE w.productId = :productId"),
    @NamedQuery(name = "WmsOrder.findByMyCompanyId", query = "SELECT w FROM WmsOrder w WHERE w.myCompanyId = :myCompanyId"),
    @NamedQuery(name = "WmsOrder.findByServicePOId", query = "SELECT w FROM WmsOrder w WHERE w.servicePOId = :servicePOId"),
    @NamedQuery(name = "WmsOrder.findByInternalOrder", query = "SELECT w FROM WmsOrder w WHERE w.internalOrder = :internalOrder"),
    @NamedQuery(name = "WmsOrder.findByWarehouseId", query = "SELECT w FROM WmsOrder w WHERE w.warehouseId = :warehouseId")})
public class WmsOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "orderId")
    private Integer orderId;
    @Size(max = 45)
    @Column(name = "orderType")
    private String orderType;
    @Column(name = "fulfillmentDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fulfillmentDate;
    @Column(name = "receiveDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveDate;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "productId")
    private Integer productId;
    @Column(name = "myCompanyId")
    private Integer myCompanyId;
    @Column(name = "servicePOId")
    private Integer servicePOId;
    @Column(name = "internalOrder")
    private Boolean internalOrder;
    @Column(name = "warehouseId")
    private Integer warehouseId;

    public WmsOrder() {
    }

    public WmsOrder(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getFulfillmentDate() {
        return fulfillmentDate;
    }

    public void setFulfillmentDate(Date fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getMyCompanyId() {
        return myCompanyId;
    }

    public void setMyCompanyId(Integer myCompanyId) {
        this.myCompanyId = myCompanyId;
    }

    public Integer getServicePOId() {
        return servicePOId;
    }

    public void setServicePOId(Integer servicePOId) {
        this.servicePOId = servicePOId;
    }

    public Boolean getInternalOrder() {
        return internalOrder;
    }

    public void setInternalOrder(Boolean internalOrder) {
        this.internalOrder = internalOrder;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WmsOrder)) {
            return false;
        }
        WmsOrder other = (WmsOrder) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.WmsOrder[ orderId=" + orderId + " ]";
    }
    
}
