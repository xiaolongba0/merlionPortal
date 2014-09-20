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
@Table(name = "TransportationLog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransportationLog.findAll", query = "SELECT t FROM TransportationLog t"),
    @NamedQuery(name = "TransportationLog.findByLogId", query = "SELECT t FROM TransportationLog t WHERE t.logId = :logId"),
    @NamedQuery(name = "TransportationLog.findByAction", query = "SELECT t FROM TransportationLog t WHERE t.action = :action"),
    @NamedQuery(name = "TransportationLog.findByActionMessage", query = "SELECT t FROM TransportationLog t WHERE t.actionMessage = :actionMessage"),
    @NamedQuery(name = "TransportationLog.findByTimeStamp", query = "SELECT t FROM TransportationLog t WHERE t.timeStamp = :timeStamp"),
    @NamedQuery(name = "TransportationLog.findByOperatorId", query = "SELECT t FROM TransportationLog t WHERE t.operatorId = :operatorId")})
public class TransportationLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "logId")
    private Integer logId;
    @Size(max = 255)
    @Column(name = "action")
    private String action;
    @Size(max = 255)
    @Column(name = "actionMessage")
    private String actionMessage;
    @Column(name = "timeStamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;
    @Column(name = "operatorId")
    private Integer operatorId;
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

    public String getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
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
