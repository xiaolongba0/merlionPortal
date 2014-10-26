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
@Table(name = "MovingStock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovingStock.findAll", query = "SELECT m FROM MovingStock m"),
    @NamedQuery(name = "MovingStock.findByMovingStockId", query = "SELECT m FROM MovingStock m WHERE m.movingStockId = :movingStockId"),
    @NamedQuery(name = "MovingStock.findByMovingStock", query = "SELECT m FROM MovingStock m WHERE m.movingStock = :movingStock"),
    @NamedQuery(name = "MovingStock.findBySourceStorageBinId", query = "SELECT m FROM MovingStock m WHERE m.sourceStorageBinId = :sourceStorageBinId")})
public class MovingStock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "movingStockId")
    private Integer movingStockId;
    @Column(name = "movingStock")
    private Integer movingStock;
    @Column(name = "sourceStorageBinId")
    private Integer sourceStorageBinId;
    @JoinColumn(name = "transportOrder", referencedColumnName = "transportOrderId")
    @ManyToOne(optional = false)
    private TransportOrder transportOrder;
    @JoinColumn(name = "stock", referencedColumnName = "stockId")
    @ManyToOne(optional = false)
    private Stock stock;

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

    public Integer getMovingStock() {
        return movingStock;
    }

    public void setMovingStock(Integer movingStock) {
        this.movingStock = movingStock;
    }

    public Integer getSourceStorageBinId() {
        return sourceStorageBinId;
    }

    public void setSourceStorageBinId(Integer sourceStorageBinId) {
        this.sourceStorageBinId = sourceStorageBinId;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
