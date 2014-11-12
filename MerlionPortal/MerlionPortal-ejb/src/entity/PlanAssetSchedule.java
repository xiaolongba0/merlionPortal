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
@Table(name = "PlanAssetSchedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanAssetSchedule.findAll", query = "SELECT p FROM PlanAssetSchedule p"),
    @NamedQuery(name = "PlanAssetSchedule.findByPlanScheduleId", query = "SELECT p FROM PlanAssetSchedule p WHERE p.planScheduleId = :planScheduleId"),
    @NamedQuery(name = "PlanAssetSchedule.findByStartDate", query = "SELECT p FROM PlanAssetSchedule p WHERE p.startDate = :startDate"),
    @NamedQuery(name = "PlanAssetSchedule.findByEndDate", query = "SELECT p FROM PlanAssetSchedule p WHERE p.endDate = :endDate"),
    @NamedQuery(name = "PlanAssetSchedule.findByOperatorId", query = "SELECT p FROM PlanAssetSchedule p WHERE p.operatorId = :operatorId"),
    @NamedQuery(name = "PlanAssetSchedule.findByAssetLoad", query = "SELECT p FROM PlanAssetSchedule p WHERE p.assetLoad = :assetLoad"),
    @NamedQuery(name = "PlanAssetSchedule.findByOrigin", query = "SELECT p FROM PlanAssetSchedule p WHERE p.origin = :origin"),
    @NamedQuery(name = "PlanAssetSchedule.findByDestination", query = "SELECT p FROM PlanAssetSchedule p WHERE p.destination = :destination")})
public class PlanAssetSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "planScheduleId")
    private Integer planScheduleId;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "operatorId")
    private Integer operatorId;
    @Column(name = "assetLoad")
    private Integer assetLoad;
    @Size(max = 255)
    @Column(name = "origin")
    private String origin;
    @Size(max = 255)
    @Column(name = "destination")
    private String destination;
    @JoinColumn(name = "transportationAsset", referencedColumnName = "assetId")
    @ManyToOne(optional = false)
    private TransportationAsset transportationAsset;

    public PlanAssetSchedule() {
    }

    public PlanAssetSchedule(Integer planScheduleId) {
        this.planScheduleId = planScheduleId;
    }

    public Integer getPlanScheduleId() {
        return planScheduleId;
    }

    public void setPlanScheduleId(Integer planScheduleId) {
        this.planScheduleId = planScheduleId;
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

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getAssetLoad() {
        return assetLoad;
    }

    public void setAssetLoad(Integer assetLoad) {
        this.assetLoad = assetLoad;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public TransportationAsset getTransportationAsset() {
        return transportationAsset;
    }

    public void setTransportationAsset(TransportationAsset transportationAsset) {
        this.transportationAsset = transportationAsset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planScheduleId != null ? planScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanAssetSchedule)) {
            return false;
        }
        PlanAssetSchedule other = (PlanAssetSchedule) object;
        if ((this.planScheduleId == null && other.planScheduleId != null) || (this.planScheduleId != null && !this.planScheduleId.equals(other.planScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PlanAssetSchedule[ planScheduleId=" + planScheduleId + " ]";
    }
    
}
