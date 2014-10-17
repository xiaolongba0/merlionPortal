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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "ForecastResult")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ForecastResult.findAll", query = "SELECT f FROM ForecastResult f"),
    @NamedQuery(name = "ForecastResult.findByForecastResultId", query = "SELECT f FROM ForecastResult f WHERE f.forecastResultId = :forecastResultId"),
    @NamedQuery(name = "ForecastResult.findByForecastResultDate", query = "SELECT f FROM ForecastResult f WHERE f.forecastResultDate = :forecastResultDate"),
    @NamedQuery(name = "ForecastResult.findByPeriodicity", query = "SELECT f FROM ForecastResult f WHERE f.periodicity = :periodicity"),
    @NamedQuery(name = "ForecastResult.findByFluctuation", query = "SELECT f FROM ForecastResult f WHERE f.fluctuation = :fluctuation"),
    @NamedQuery(name = "ForecastResult.findByProductId", query = "SELECT f FROM ForecastResult f WHERE f.productId = :productId")})
public class ForecastResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "forecastResultId")
    private Integer forecastResultId;
    @Column(name = "forecastResultDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date forecastResultDate;
    @Column(name = "periodicity")
    private Integer periodicity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fluctuation")
    private Double fluctuation;
    @Column(name = "productId")
    private Integer productId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "forecastResult")
    private Mps mps;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "forecastResult")
    private List<MonthForecastResult> monthForecastResultList;

    public ForecastResult() {
    }

    public ForecastResult(Integer forecastResultId) {
        this.forecastResultId = forecastResultId;
    }

    public Integer getForecastResultId() {
        return forecastResultId;
    }

    public void setForecastResultId(Integer forecastResultId) {
        this.forecastResultId = forecastResultId;
    }

    public Date getForecastResultDate() {
        return forecastResultDate;
    }

    public void setForecastResultDate(Date forecastResultDate) {
        this.forecastResultDate = forecastResultDate;
    }

    public Integer getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Integer periodicity) {
        this.periodicity = periodicity;
    }

    public Double getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(Double fluctuation) {
        this.fluctuation = fluctuation;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Mps getMps() {
        return mps;
    }

    public void setMps(Mps mps) {
        this.mps = mps;
    }

    @XmlTransient
    public List<MonthForecastResult> getMonthForecastResultList() {
        return monthForecastResultList;
    }

    public void setMonthForecastResultList(List<MonthForecastResult> monthForecastResultList) {
        this.monthForecastResultList = monthForecastResultList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (forecastResultId != null ? forecastResultId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ForecastResult)) {
            return false;
        }
        ForecastResult other = (ForecastResult) object;
        if ((this.forecastResultId == null && other.forecastResultId != null) || (this.forecastResultId != null && !this.forecastResultId.equals(other.forecastResultId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ForecastResult[ forecastResultId=" + forecastResultId + " ]";
    }
    
}
