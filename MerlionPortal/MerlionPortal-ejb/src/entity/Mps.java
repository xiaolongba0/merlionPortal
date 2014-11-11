/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "MPS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mps.findAll", query = "SELECT m FROM Mps m"),
    @NamedQuery(name = "Mps.findByMpsId", query = "SELECT m FROM Mps m WHERE m.mpsId = :mpsId"),
    @NamedQuery(name = "Mps.findByBuffer", query = "SELECT m FROM Mps m WHERE m.buffer = :buffer"),
    @NamedQuery(name = "Mps.findByDemand", query = "SELECT m FROM Mps m WHERE m.demand = :demand")})
public class Mps implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "mpsId")
    private Integer mpsId;
    @Column(name = "buffer")
    private Integer buffer;
    @Column(name = "demand")
    private Integer demand;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "mps")
    private MRPList mRPList;
    @JoinColumn(name = "mpsId", referencedColumnName = "forecastResultId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ForecastResult forecastResult;

    public Mps() {
    }

    public Mps(Integer mpsId) {
        this.mpsId = mpsId;
    }

    public Integer getMpsId() {
        return mpsId;
    }

    public void setMpsId(Integer mpsId) {
        this.mpsId = mpsId;
    }

    public Integer getBuffer() {
        return buffer;
    }

    public void setBuffer(Integer buffer) {
        this.buffer = buffer;
    }

    public Integer getDemand() {
        return demand;
    }

    public void setDemand(Integer demand) {
        this.demand = demand;
    }

    public MRPList getMRPList() {
        return mRPList;
    }

    public void setMRPList(MRPList mRPList) {
        this.mRPList = mRPList;
    }

    public ForecastResult getForecastResult() {
        return forecastResult;
    }

    public void setForecastResult(ForecastResult forecastResult) {
        this.forecastResult = forecastResult;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpsId != null ? mpsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mps)) {
            return false;
        }
        Mps other = (Mps) object;
        if ((this.mpsId == null && other.mpsId != null) || (this.mpsId != null && !this.mpsId.equals(other.mpsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Mps[ mpsId=" + mpsId + " ]";
    }
    
}
