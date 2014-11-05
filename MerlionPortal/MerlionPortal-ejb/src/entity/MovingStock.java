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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "MovingStock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovingStock.findAll", query = "SELECT m FROM MovingStock m"),
    @NamedQuery(name = "MovingStock.findByMovingStockId", query = "SELECT m FROM MovingStock m WHERE m.movingStockId = :movingStockId"),
    @NamedQuery(name = "MovingStock.findByMovingQuantity", query = "SELECT m FROM MovingStock m WHERE m.movingQuantity = :movingQuantity"),
    @NamedQuery(name = "MovingStock.findBySourceStorageBinId", query = "SELECT m FROM MovingStock m WHERE m.sourceStorageBinId = :sourceStorageBinId"),
    @NamedQuery(name = "MovingStock.findByDestStorageBinId", query = "SELECT m FROM MovingStock m WHERE m.destStorageBinId = :destStorageBinId"),
    @NamedQuery(name = "MovingStock.findByCreatedDate", query = "SELECT m FROM MovingStock m WHERE m.createdDate = :createdDate"),
    @NamedQuery(name = "MovingStock.findByStockName", query = "SELECT m FROM MovingStock m WHERE m.stockName = :stockName"),
    @NamedQuery(name = "MovingStock.findByExpiryDate", query = "SELECT m FROM MovingStock m WHERE m.expiryDate = :expiryDate")})
public class MovingStock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "movingStockId")
    private Integer movingStockId;
    @Column(name = "movingQuantity")
    private Integer movingQuantity;
    @Column(name = "sourceStorageBinId")
    private Integer sourceStorageBinId;
    @Column(name = "destStorageBinId")
    private Integer destStorageBinId;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Size(max = 100)
    @Column(name = "stockName")
    private String stockName;
    @Column(name = "expiryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @JoinColumn(name = "transportOrder", referencedColumnName = "transportOrderId")
    @ManyToOne(optional = false)
    private TransportOrder transportOrder;

    public MovingStock() {
    }

    public MovingStock(Integer movingStockId) {
        this.movingStockId = movingStockId;
    }

    public Integer getMovingStockId() {
        return movingStockId;
    }

    public void setMovingStockId(Integer movingStockId) {
        this.movingStockId = movingStockId;
    }

    public Integer getMovingQuantity() {
        return movingQuantity;
    }

    public void setMovingQuantity(Integer movingQuantity) {
        this.movingQuantity = movingQuantity;
    }

    public Integer getSourceStorageBinId() {
        return sourceStorageBinId;
    }

    public void setSourceStorageBinId(Integer sourceStorageBinId) {
        this.sourceStorageBinId = sourceStorageBinId;
    }

    public Integer getDestStorageBinId() {
        return destStorageBinId;
    }

    public void setDestStorageBinId(Integer destStorageBinId) {
        this.destStorageBinId = destStorageBinId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movingStockId != null ? movingStockId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovingStock)) {
            return false;
        }
        MovingStock other = (MovingStock) object;
        if ((this.movingStockId == null && other.movingStockId != null) || (this.movingStockId != null && !this.movingStockId.equals(other.movingStockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MovingStock[ movingStockId=" + movingStockId + " ]";
    }
    
}
