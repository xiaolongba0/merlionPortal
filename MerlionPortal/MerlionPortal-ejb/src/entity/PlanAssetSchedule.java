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
    @NamedQuery(name = "PlanAssetSchedule.findByAssetLoad", query = "SELECT p FROM PlanAssetSchedule p WHERE p.assetLoad = :assetLoad")})
public class PlanAssetSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "planScheduleId")
    private Integer planScheduleId;
    @Size(max = 45)
    @Column(name = "startDate")
    private String startDate;
    @Size(max = 45)
    @Column(name = "endDate")
    private String endDate;
    @Size(max = 45)
    @Column(name = "operatorId")
    private String operatorId;
    @Size(max = 45)
    @Column(name = "assetLoad")
    private String assetLoad;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getAssetLoad() {
        return assetLoad;
    }

    public void setAssetLoad(String assetLoad) {
        this.assetLoad = assetLoad;
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
