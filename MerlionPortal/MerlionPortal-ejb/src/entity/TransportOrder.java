/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "TransportOrder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransportOrder.findAll", query = "SELECT t FROM TransportOrder t"),
    @NamedQuery(name = "TransportOrder.findByTransportOrderId", query = "SELECT t FROM TransportOrder t WHERE t.transportOrderId = :transportOrderId"),
    @NamedQuery(name = "TransportOrder.findByTransportDate", query = "SELECT t FROM TransportOrder t WHERE t.transportDate = :transportDate"),
    @NamedQuery(name = "TransportOrder.findByStaffId", query = "SELECT t FROM TransportOrder t WHERE t.staffId = :staffId"),
    @NamedQuery(name = "TransportOrder.findByStatus", query = "SELECT t FROM TransportOrder t WHERE t.status = :status"),
    @NamedQuery(name = "TransportOrder.findByProductId", query = "SELECT t FROM TransportOrder t WHERE t.productId = :productId"),
    @NamedQuery(name = "TransportOrder.findByTotalQuantity", query = "SELECT t FROM TransportOrder t WHERE t.totalQuantity = :totalQuantity"),
    @NamedQuery(name = "TransportOrder.findBySourceWarehouseId", query = "SELECT t FROM TransportOrder t WHERE t.sourceWarehouseId = :sourceWarehouseId"),
    @NamedQuery(name = "TransportOrder.findByDestWarehouseId", query = "SELECT t FROM TransportOrder t WHERE t.destWarehouseId = :destWarehouseId")})
public class TransportOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transportOrderId")
    private Integer transportOrderId;
    @Column(name = "transportDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transportDate;
    @Column(name = "staffId")
    private Integer staffId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "productId")
    private Integer productId;
    @Column(name = "totalQuantity")
    private Integer totalQuantity;
    @Column(name = "sourceWarehouseId")
    private Integer sourceWarehouseId;
    @Column(name = "destWarehouseId")
    private Integer destWarehouseId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transportOrder")
    private List<MovingStock> movingStockList;

    public TransportOrder() {
    }

    public TransportOrder(Integer transportOrderId) {
        this.transportOrderId = transportOrderId;
    }

    public Integer getTransportOrderId() {
        return transportOrderId;
    }

    public void setTransportOrderId(Integer transportOrderId) {
        this.transportOrderId = transportOrderId;
    }

    public Date getTransportDate() {
        return transportDate;
    }

    public void setTransportDate(Date transportDate) {
        this.transportDate = transportDate;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getSourceWarehouseId() {
        return sourceWarehouseId;
    }

    public void setSourceWarehouseId(Integer sourceWarehouseId) {
        this.sourceWarehouseId = sourceWarehouseId;
    }

    public Integer getDestWarehouseId() {
        return destWarehouseId;
    }

    public void setDestWarehouseId(Integer destWarehouseId) {
        this.destWarehouseId = destWarehouseId;
    }

    @XmlTransient
    public List<MovingStock> getMovingStockList() {
        return movingStockList;
    }

    public void setMovingStockList(List<MovingStock> movingStockList) {
        this.movingStockList = movingStockList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transportOrderId != null ? transportOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransportOrder)) {
            return false;
        }
        TransportOrder other = (TransportOrder) object;
        if ((this.transportOrderId == null && other.transportOrderId != null) || (this.transportOrderId != null && !this.transportOrderId.equals(other.transportOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransportOrder[ transportOrderId=" + transportOrderId + " ]";
    }
    
}
