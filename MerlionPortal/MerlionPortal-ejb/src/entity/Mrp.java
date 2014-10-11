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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "MRP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mrp.findAll", query = "SELECT m FROM Mrp m"),
    @NamedQuery(name = "Mrp.findByMrpId", query = "SELECT m FROM Mrp m WHERE m.mrpId = :mrpId"),
    @NamedQuery(name = "Mrp.findByGrossReq1", query = "SELECT m FROM Mrp m WHERE m.grossReq1 = :grossReq1"),
    @NamedQuery(name = "Mrp.findByGrossReq2", query = "SELECT m FROM Mrp m WHERE m.grossReq2 = :grossReq2"),
    @NamedQuery(name = "Mrp.findByGrossReq3", query = "SELECT m FROM Mrp m WHERE m.grossReq3 = :grossReq3"),
    @NamedQuery(name = "Mrp.findByGrossReq4", query = "SELECT m FROM Mrp m WHERE m.grossReq4 = :grossReq4"),
    @NamedQuery(name = "Mrp.findByGrossReq5", query = "SELECT m FROM Mrp m WHERE m.grossReq5 = :grossReq5"),
    @NamedQuery(name = "Mrp.findByScheduledRec1", query = "SELECT m FROM Mrp m WHERE m.scheduledRec1 = :scheduledRec1"),
    @NamedQuery(name = "Mrp.findByScheduledRec2", query = "SELECT m FROM Mrp m WHERE m.scheduledRec2 = :scheduledRec2"),
    @NamedQuery(name = "Mrp.findByScheduledRec3", query = "SELECT m FROM Mrp m WHERE m.scheduledRec3 = :scheduledRec3"),
    @NamedQuery(name = "Mrp.findByScheduledRec4", query = "SELECT m FROM Mrp m WHERE m.scheduledRec4 = :scheduledRec4"),
    @NamedQuery(name = "Mrp.findByScheduledRec5", query = "SELECT m FROM Mrp m WHERE m.scheduledRec5 = :scheduledRec5"),
    @NamedQuery(name = "Mrp.findByPlannedRec1", query = "SELECT m FROM Mrp m WHERE m.plannedRec1 = :plannedRec1"),
    @NamedQuery(name = "Mrp.findByPlannedRec2", query = "SELECT m FROM Mrp m WHERE m.plannedRec2 = :plannedRec2"),
    @NamedQuery(name = "Mrp.findByPlannedRec3", query = "SELECT m FROM Mrp m WHERE m.plannedRec3 = :plannedRec3"),
    @NamedQuery(name = "Mrp.findByPlannedRec4", query = "SELECT m FROM Mrp m WHERE m.plannedRec4 = :plannedRec4"),
    @NamedQuery(name = "Mrp.findByPlannedRec5", query = "SELECT m FROM Mrp m WHERE m.plannedRec5 = :plannedRec5"),
    @NamedQuery(name = "Mrp.findByOnHand1", query = "SELECT m FROM Mrp m WHERE m.onHand1 = :onHand1"),
    @NamedQuery(name = "Mrp.findByOnHand2", query = "SELECT m FROM Mrp m WHERE m.onHand2 = :onHand2"),
    @NamedQuery(name = "Mrp.findByOnHand3", query = "SELECT m FROM Mrp m WHERE m.onHand3 = :onHand3"),
    @NamedQuery(name = "Mrp.findByOnHand4", query = "SELECT m FROM Mrp m WHERE m.onHand4 = :onHand4"),
    @NamedQuery(name = "Mrp.findByOnHand5", query = "SELECT m FROM Mrp m WHERE m.onHand5 = :onHand5"),
    @NamedQuery(name = "Mrp.findByPlannedOrder1", query = "SELECT m FROM Mrp m WHERE m.plannedOrder1 = :plannedOrder1"),
    @NamedQuery(name = "Mrp.findByPlannedOrder2", query = "SELECT m FROM Mrp m WHERE m.plannedOrder2 = :plannedOrder2"),
    @NamedQuery(name = "Mrp.findByPlannedOrder3", query = "SELECT m FROM Mrp m WHERE m.plannedOrder3 = :plannedOrder3"),
    @NamedQuery(name = "Mrp.findByPlannedOrder4", query = "SELECT m FROM Mrp m WHERE m.plannedOrder4 = :plannedOrder4"),
    @NamedQuery(name = "Mrp.findByPlannedOrder5", query = "SELECT m FROM Mrp m WHERE m.plannedOrder5 = :plannedOrder5"),
    @NamedQuery(name = "Mrp.findByLeadTime", query = "SELECT m FROM Mrp m WHERE m.leadTime = :leadTime")})
public class Mrp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mrpId")
    private Integer mrpId;
    @Column(name = "grossReq1")
    private Integer grossReq1;
    @Column(name = "grossReq2")
    private Integer grossReq2;
    @Column(name = "grossReq3")
    private Integer grossReq3;
    @Column(name = "grossReq4")
    private Integer grossReq4;
    @Column(name = "grossReq5")
    private Integer grossReq5;
    @Column(name = "scheduledRec1")
    private Integer scheduledRec1;
    @Column(name = "scheduledRec2")
    private Integer scheduledRec2;
    @Column(name = "scheduledRec3")
    private Integer scheduledRec3;
    @Column(name = "scheduledRec4")
    private Integer scheduledRec4;
    @Column(name = "scheduledRec5")
    private Integer scheduledRec5;
    @Column(name = "plannedRec1")
    private Integer plannedRec1;
    @Column(name = "plannedRec2")
    private Integer plannedRec2;
    @Column(name = "plannedRec3")
    private Integer plannedRec3;
    @Column(name = "plannedRec4")
    private Integer plannedRec4;
    @Column(name = "plannedRec5")
    private Integer plannedRec5;
    @Column(name = "onHand1")
    private Integer onHand1;
    @Column(name = "onHand2")
    private Integer onHand2;
    @Column(name = "onHand3")
    private Integer onHand3;
    @Column(name = "onHand4")
    private Integer onHand4;
    @Column(name = "onHand5")
    private Integer onHand5;
    @Column(name = "plannedOrder1")
    private Integer plannedOrder1;
    @Column(name = "plannedOrder2")
    private Integer plannedOrder2;
    @Column(name = "plannedOrder3")
    private Integer plannedOrder3;
    @Column(name = "plannedOrder4")
    private Integer plannedOrder4;
    @Column(name = "plannedOrder5")
    private Integer plannedOrder5;
    @Column(name = "leadTime")
    private Integer leadTime;
    @JoinColumn(name = "mrpList", referencedColumnName = "mrpListId")
    @ManyToOne(optional = false)
    private MRPList mrpList;

    public Mrp() {
    }

    public Mrp(Integer mrpId) {
        this.mrpId = mrpId;
    }

    public Integer getMrpId() {
        return mrpId;
    }

    public void setMrpId(Integer mrpId) {
        this.mrpId = mrpId;
    }

    public Integer getGrossReq1() {
        return grossReq1;
    }

    public void setGrossReq1(Integer grossReq1) {
        this.grossReq1 = grossReq1;
    }

    public Integer getGrossReq2() {
        return grossReq2;
    }

    public void setGrossReq2(Integer grossReq2) {
        this.grossReq2 = grossReq2;
    }

    public Integer getGrossReq3() {
        return grossReq3;
    }

    public void setGrossReq3(Integer grossReq3) {
        this.grossReq3 = grossReq3;
    }

    public Integer getGrossReq4() {
        return grossReq4;
    }

    public void setGrossReq4(Integer grossReq4) {
        this.grossReq4 = grossReq4;
    }

    public Integer getGrossReq5() {
        return grossReq5;
    }

    public void setGrossReq5(Integer grossReq5) {
        this.grossReq5 = grossReq5;
    }

    public Integer getScheduledRec1() {
        return scheduledRec1;
    }

    public void setScheduledRec1(Integer scheduledRec1) {
        this.scheduledRec1 = scheduledRec1;
    }

    public Integer getScheduledRec2() {
        return scheduledRec2;
    }

    public void setScheduledRec2(Integer scheduledRec2) {
        this.scheduledRec2 = scheduledRec2;
    }

    public Integer getScheduledRec3() {
        return scheduledRec3;
    }

    public void setScheduledRec3(Integer scheduledRec3) {
        this.scheduledRec3 = scheduledRec3;
    }

    public Integer getScheduledRec4() {
        return scheduledRec4;
    }

    public void setScheduledRec4(Integer scheduledRec4) {
        this.scheduledRec4 = scheduledRec4;
    }

    public Integer getScheduledRec5() {
        return scheduledRec5;
    }

    public void setScheduledRec5(Integer scheduledRec5) {
        this.scheduledRec5 = scheduledRec5;
    }

    public Integer getPlannedRec1() {
        return plannedRec1;
    }

    public void setPlannedRec1(Integer plannedRec1) {
        this.plannedRec1 = plannedRec1;
    }

    public Integer getPlannedRec2() {
        return plannedRec2;
    }

    public void setPlannedRec2(Integer plannedRec2) {
        this.plannedRec2 = plannedRec2;
    }

    public Integer getPlannedRec3() {
        return plannedRec3;
    }

    public void setPlannedRec3(Integer plannedRec3) {
        this.plannedRec3 = plannedRec3;
    }

    public Integer getPlannedRec4() {
        return plannedRec4;
    }

    public void setPlannedRec4(Integer plannedRec4) {
        this.plannedRec4 = plannedRec4;
    }

    public Integer getPlannedRec5() {
        return plannedRec5;
    }

    public void setPlannedRec5(Integer plannedRec5) {
        this.plannedRec5 = plannedRec5;
    }

    public Integer getOnHand1() {
        return onHand1;
    }

    public void setOnHand1(Integer onHand1) {
        this.onHand1 = onHand1;
    }

    public Integer getOnHand2() {
        return onHand2;
    }

    public void setOnHand2(Integer onHand2) {
        this.onHand2 = onHand2;
    }

    public Integer getOnHand3() {
        return onHand3;
    }

    public void setOnHand3(Integer onHand3) {
        this.onHand3 = onHand3;
    }

    public Integer getOnHand4() {
        return onHand4;
    }

    public void setOnHand4(Integer onHand4) {
        this.onHand4 = onHand4;
    }

    public Integer getOnHand5() {
        return onHand5;
    }

    public void setOnHand5(Integer onHand5) {
        this.onHand5 = onHand5;
    }

    public Integer getPlannedOrder1() {
        return plannedOrder1;
    }

    public void setPlannedOrder1(Integer plannedOrder1) {
        this.plannedOrder1 = plannedOrder1;
    }

    public Integer getPlannedOrder2() {
        return plannedOrder2;
    }

    public void setPlannedOrder2(Integer plannedOrder2) {
        this.plannedOrder2 = plannedOrder2;
    }

    public Integer getPlannedOrder3() {
        return plannedOrder3;
    }

    public void setPlannedOrder3(Integer plannedOrder3) {
        this.plannedOrder3 = plannedOrder3;
    }

    public Integer getPlannedOrder4() {
        return plannedOrder4;
    }

    public void setPlannedOrder4(Integer plannedOrder4) {
        this.plannedOrder4 = plannedOrder4;
    }

    public Integer getPlannedOrder5() {
        return plannedOrder5;
    }

    public void setPlannedOrder5(Integer plannedOrder5) {
        this.plannedOrder5 = plannedOrder5;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public MRPList getMrpList() {
        return mrpList;
    }

    public void setMrpList(MRPList mrpList) {
        this.mrpList = mrpList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrpId != null ? mrpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mrp)) {
            return false;
        }
        Mrp other = (Mrp) object;
        if ((this.mrpId == null && other.mrpId != null) || (this.mrpId != null && !this.mrpId.equals(other.mrpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Mrp[ mrpId=" + mrpId + " ]";
    }
    
}
