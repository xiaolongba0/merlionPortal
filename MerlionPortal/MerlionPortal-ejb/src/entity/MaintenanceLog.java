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
@Table(name = "MaintenanceLog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceLog.findAll", query = "SELECT m FROM MaintenanceLog m"),
    @NamedQuery(name = "MaintenanceLog.findByMaintenanceLogId", query = "SELECT m FROM MaintenanceLog m WHERE m.maintenanceLogId = :maintenanceLogId"),
    @NamedQuery(name = "MaintenanceLog.findByOperatorId", query = "SELECT m FROM MaintenanceLog m WHERE m.operatorId = :operatorId"),
    @NamedQuery(name = "MaintenanceLog.findByCost", query = "SELECT m FROM MaintenanceLog m WHERE m.cost = :cost"),
    @NamedQuery(name = "MaintenanceLog.findByDescription", query = "SELECT m FROM MaintenanceLog m WHERE m.description = :description"),
    @NamedQuery(name = "MaintenanceLog.findByStartDate", query = "SELECT m FROM MaintenanceLog m WHERE m.startDate = :startDate"),
    @NamedQuery(name = "MaintenanceLog.findByEndDate", query = "SELECT m FROM MaintenanceLog m WHERE m.endDate = :endDate"),
    @NamedQuery(name = "MaintenanceLog.findByScheduleId", query = "SELECT m FROM MaintenanceLog m WHERE m.scheduleId = :scheduleId")})
public class MaintenanceLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "maintenanceLogId")
    private Integer maintenanceLogId;
    @Column(name = "operatorId")
    private Integer operatorId;
    @Size(max = 45)
    @Column(name = "cost")
    private String cost;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "scheduleId")
    private Integer scheduleId;
    @JoinColumn(name = "TransporationAsset_assetId", referencedColumnName = "assetId")
    @ManyToOne(optional = false)
    private TransportationAsset transporationAssetassetId;

    public MaintenanceLog() {
    }

    public MaintenanceLog(Integer maintenanceLogId) {
        this.maintenanceLogId = maintenanceLogId;
    }

    public Integer getMaintenanceLogId() {
        return maintenanceLogId;
    }

    public void setMaintenanceLogId(Integer maintenanceLogId) {
        this.maintenanceLogId = maintenanceLogId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public TransportationAsset getTransporationAssetassetId() {
        return transporationAssetassetId;
    }

    public void setTransporationAssetassetId(TransportationAsset transporationAssetassetId) {
        this.transporationAssetassetId = transporationAssetassetId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maintenanceLogId != null ? maintenanceLogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaintenanceLog)) {
            return false;
        }
        MaintenanceLog other = (MaintenanceLog) object;
        if ((this.maintenanceLogId == null && other.maintenanceLogId != null) || (this.maintenanceLogId != null && !this.maintenanceLogId.equals(other.maintenanceLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MaintenanceLog[ maintenanceLogId=" + maintenanceLogId + " ]";
    }
    
}
