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
@Table(name = "MonthForecastResult")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MonthForecastResult.findAll", query = "SELECT m FROM MonthForecastResult m"),
    @NamedQuery(name = "MonthForecastResult.findByMonthForecastResultId", query = "SELECT m FROM MonthForecastResult m WHERE m.monthForecastResultId = :monthForecastResultId"),
    @NamedQuery(name = "MonthForecastResult.findByMonthName", query = "SELECT m FROM MonthForecastResult m WHERE m.monthName = :monthName"),
    @NamedQuery(name = "MonthForecastResult.findByForecastedQuantity", query = "SELECT m FROM MonthForecastResult m WHERE m.forecastedQuantity = :forecastedQuantity")})
public class MonthForecastResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "monthForecastResultId")
    private Integer monthForecastResultId;
    @Size(max = 45)
    @Column(name = "monthName")
    private String monthName;
    @Column(name = "forecastedQuantity")
    private Integer forecastedQuantity;
    @JoinColumn(name = "forecastResult", referencedColumnName = "forecastResultId")
    @ManyToOne(optional = false)
    private ForecastResult forecastResult;

    public MonthForecastResult() {
    }

    public MonthForecastResult(Integer monthForecastResultId) {
        this.monthForecastResultId = monthForecastResultId;
    }

    public Integer getMonthForecastResultId() {
        return monthForecastResultId;
    }

    public void setMonthForecastResultId(Integer monthForecastResultId) {
        this.monthForecastResultId = monthForecastResultId;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Integer getForecastedQuantity() {
        return forecastedQuantity;
    }

    public void setForecastedQuantity(Integer forecastedQuantity) {
        this.forecastedQuantity = forecastedQuantity;
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
        hash += (monthForecastResultId != null ? monthForecastResultId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthForecastResult)) {
            return false;
        }
        MonthForecastResult other = (MonthForecastResult) object;
        if ((this.monthForecastResultId == null && other.monthForecastResultId != null) || (this.monthForecastResultId != null && !this.monthForecastResultId.equals(other.monthForecastResultId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MonthForecastResult[ monthForecastResultId=" + monthForecastResultId + " ]";
    }
    
}
