/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "TransportationOperator")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransportationOperator.findAll", query = "SELECT t FROM TransportationOperator t"),
    @NamedQuery(name = "TransportationOperator.findByOperatorId", query = "SELECT t FROM TransportationOperator t WHERE t.operatorId = :operatorId"),
    @NamedQuery(name = "TransportationOperator.findByIsAvailable", query = "SELECT t FROM TransportationOperator t WHERE t.isAvailable = :isAvailable"),
    @NamedQuery(name = "TransportationOperator.findByOperatorStatus", query = "SELECT t FROM TransportationOperator t WHERE t.operatorStatus = :operatorStatus"),
    @NamedQuery(name = "TransportationOperator.findByOperatorType", query = "SELECT t FROM TransportationOperator t WHERE t.operatorType = :operatorType")})
public class TransportationOperator implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "operatorId")
    private Integer operatorId;
    @Column(name = "isAvailable")
    private Boolean isAvailable;
    @Size(max = 45)
    @Column(name = "operatorStatus")
    private String operatorStatus;
    @Size(max = 45)
    @Column(name = "operatorType")
    private String operatorType;
    @JoinColumn(name = "AssetSchedule_scheduleId", referencedColumnName = "scheduleId")
    @ManyToOne(optional = false)
    private AssetSchedule assetSchedulescheduleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transportationOperatoroperatorId")
    private List<OperatorSchedule> operatorScheduleList;

    public TransportationOperator() {
    }

    public TransportationOperator(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(String operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public AssetSchedule getAssetSchedulescheduleId() {
        return assetSchedulescheduleId;
    }

    public void setAssetSchedulescheduleId(AssetSchedule assetSchedulescheduleId) {
        this.assetSchedulescheduleId = assetSchedulescheduleId;
    }

    @XmlTransient
    public List<OperatorSchedule> getOperatorScheduleList() {
        return operatorScheduleList;
    }

    public void setOperatorScheduleList(List<OperatorSchedule> operatorScheduleList) {
        this.operatorScheduleList = operatorScheduleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (operatorId != null ? operatorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransportationOperator)) {
            return false;
        }
        TransportationOperator other = (TransportationOperator) object;
        if ((this.operatorId == null && other.operatorId != null) || (this.operatorId != null && !this.operatorId.equals(other.operatorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransportationOperator[ operatorId=" + operatorId + " ]";
    }
    
}
