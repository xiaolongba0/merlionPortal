/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "Estimation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estimation.findAll", query = "SELECT e FROM Estimation e"),
    @NamedQuery(name = "Estimation.findByEstId", query = "SELECT e FROM Estimation e WHERE e.estId = :estId"),
    @NamedQuery(name = "Estimation.findByType", query = "SELECT e FROM Estimation e WHERE e.type = :type"),
    @NamedQuery(name = "Estimation.findByOrigin", query = "SELECT e FROM Estimation e WHERE e.origin = :origin"),
    @NamedQuery(name = "Estimation.findByDestination", query = "SELECT e FROM Estimation e WHERE e.destination = :destination"),
    @NamedQuery(name = "Estimation.findByCost", query = "SELECT e FROM Estimation e WHERE e.cost = :cost"),
    @NamedQuery(name = "Estimation.findByTotalLoad", query = "SELECT e FROM Estimation e WHERE e.totalLoad = :totalLoad"),
    @NamedQuery(name = "Estimation.findByEndDate", query = "SELECT e FROM Estimation e WHERE e.endDate = :endDate"),
    @NamedQuery(name = "Estimation.findByStartDate", query = "SELECT e FROM Estimation e WHERE e.startDate = :startDate"),
    @NamedQuery(name = "Estimation.findByCompanyId", query = "SELECT e FROM Estimation e WHERE e.companyId = :companyId")})
public class Estimation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "estId")
    private Integer estId;
    @Size(max = 45)
    @Column(name = "type")
    private String type;
    @Size(max = 255)
    @Column(name = "origin")
    private String origin;
    @Size(max = 255)
    @Column(name = "destination")
    private String destination;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private Double cost;
    @Column(name = "totalLoad")
    private Integer totalLoad;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "companyId")
    private Integer companyId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estimation")
    private List<PlanAssetSchedule> planAssetScheduleList;

    public Estimation() {
    }

    public Estimation(Integer estId) {
        this.estId = estId;
    }

    public Integer getEstId() {
        return estId;
    }

    public void setEstId(Integer estId) {
        this.estId = estId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getTotalLoad() {
        return totalLoad;
    }

    public void setTotalLoad(Integer totalLoad) {
        this.totalLoad = totalLoad;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @XmlTransient
    public List<PlanAssetSchedule> getPlanAssetScheduleList() {
        return planAssetScheduleList;
    }

    public void setPlanAssetScheduleList(List<PlanAssetSchedule> planAssetScheduleList) {
        this.planAssetScheduleList = planAssetScheduleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estId != null ? estId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estimation)) {
            return false;
        }
        Estimation other = (Estimation) object;
        if ((this.estId == null && other.estId != null) || (this.estId != null && !this.estId.equals(other.estId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Estimation[ estId=" + estId + " ]";
    }
    
}
