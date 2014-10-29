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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "AssetSchedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AssetSchedule.findAll", query = "SELECT a FROM AssetSchedule a"),
    @NamedQuery(name = "AssetSchedule.findByScheduleId", query = "SELECT a FROM AssetSchedule a WHERE a.scheduleId = :scheduleId"),
    @NamedQuery(name = "AssetSchedule.findByStartDate", query = "SELECT a FROM AssetSchedule a WHERE a.startDate = :startDate"),
    @NamedQuery(name = "AssetSchedule.findByEndDate", query = "SELECT a FROM AssetSchedule a WHERE a.endDate = :endDate")})
public class AssetSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "scheduleId")
    private Integer scheduleId;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @JoinColumn(name = "TransportationOperator", referencedColumnName = "operatorId")
    @ManyToOne(optional = false)
    private TransportationOperator transportationOperator;
    @JoinColumn(name = "TransporationAsset_assetId", referencedColumnName = "assetId")
    @ManyToOne(optional = false)
    private TransportationAsset transporationAssetassetId;

    public AssetSchedule() {
    }

    public AssetSchedule(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
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

    public TransportationOperator getTransportationOperator() {
        return transportationOperator;
    }

    public void setTransportationOperator(TransportationOperator transportationOperator) {
        this.transportationOperator = transportationOperator;
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
        hash += (scheduleId != null ? scheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssetSchedule)) {
            return false;
        }
        AssetSchedule other = (AssetSchedule) object;
        if ((this.scheduleId == null && other.scheduleId != null) || (this.scheduleId != null && !this.scheduleId.equals(other.scheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AssetSchedule[ scheduleId=" + scheduleId + " ]";
    }
    
}
