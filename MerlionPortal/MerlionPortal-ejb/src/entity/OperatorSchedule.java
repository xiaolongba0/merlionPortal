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
@Table(name = "OperatorSchedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperatorSchedule.findAll", query = "SELECT o FROM OperatorSchedule o"),
    @NamedQuery(name = "OperatorSchedule.findByOperatorScheduleId", query = "SELECT o FROM OperatorSchedule o WHERE o.operatorScheduleId = :operatorScheduleId"),
    @NamedQuery(name = "OperatorSchedule.findByStartDate", query = "SELECT o FROM OperatorSchedule o WHERE o.startDate = :startDate"),
    @NamedQuery(name = "OperatorSchedule.findByEndDate", query = "SELECT o FROM OperatorSchedule o WHERE o.endDate = :endDate")})
public class OperatorSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "operatorScheduleId")
    private Integer operatorScheduleId;
    @Size(max = 45)
    @Column(name = "startDate")
    private String startDate;
    @Size(max = 45)
    @Column(name = "endDate")
    private String endDate;
    @JoinColumn(name = "TransportationOperator_operatorId", referencedColumnName = "operatorId")
    @ManyToOne(optional = false)
    private TransportationOperator transportationOperatoroperatorId;

    public OperatorSchedule() {
    }

    public OperatorSchedule(Integer operatorScheduleId) {
        this.operatorScheduleId = operatorScheduleId;
    }

    public Integer getOperatorScheduleId() {
        return operatorScheduleId;
    }

    public void setOperatorScheduleId(Integer operatorScheduleId) {
        this.operatorScheduleId = operatorScheduleId;
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

    public TransportationOperator getTransportationOperatoroperatorId() {
        return transportationOperatoroperatorId;
    }

    public void setTransportationOperatoroperatorId(TransportationOperator transportationOperatoroperatorId) {
        this.transportationOperatoroperatorId = transportationOperatoroperatorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (operatorScheduleId != null ? operatorScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OperatorSchedule)) {
            return false;
        }
        OperatorSchedule other = (OperatorSchedule) object;
        if ((this.operatorScheduleId == null && other.operatorScheduleId != null) || (this.operatorScheduleId != null && !this.operatorScheduleId.equals(other.operatorScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OperatorSchedule[ operatorScheduleId=" + operatorScheduleId + " ]";
    }
    
}
