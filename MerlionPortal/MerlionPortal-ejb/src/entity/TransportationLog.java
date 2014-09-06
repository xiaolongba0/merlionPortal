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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "TransportationLog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransportationLog.findAll", query = "SELECT t FROM TransportationLog t"),
    @NamedQuery(name = "TransportationLog.findByLogId", query = "SELECT t FROM TransportationLog t WHERE t.logId = :logId"),
    @NamedQuery(name = "TransportationLog.findByAction", query = "SELECT t FROM TransportationLog t WHERE t.action = :action"),
    @NamedQuery(name = "TransportationLog.findByTimeStamp", query = "SELECT t FROM TransportationLog t WHERE t.timeStamp = :timeStamp")})
public class TransportationLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "logId")
    private Integer logId;
    @Size(max = 255)
    @Column(name = "action")
    private String action;
    @Column(name = "timeStamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;
    @JoinColumn(name = "TransportationOrder_transportationOrderId", referencedColumnName = "transportationOrderId")
    @ManyToOne(optional = false)
    private TransportationOrder transportationOrdertransportationOrderId;

    public TransportationLog() {
    }

    public TransportationLog(Integer logId) {
        this.logId = logId;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TransportationOrder getTransportationOrdertransportationOrderId() {
        return transportationOrdertransportationOrderId;
    }

    public void setTransportationOrdertransportationOrderId(TransportationOrder transportationOrdertransportationOrderId) {
        this.transportationOrdertransportationOrderId = transportationOrdertransportationOrderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransportationLog)) {
            return false;
        }
        TransportationLog other = (TransportationLog) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransportationLog[ logId=" + logId + " ]";
    }
    
}
