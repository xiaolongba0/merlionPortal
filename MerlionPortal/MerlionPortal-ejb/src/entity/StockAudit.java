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
@Table(name = "StockAudit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockAudit.findAll", query = "SELECT s FROM StockAudit s"),
    @NamedQuery(name = "StockAudit.findByStockAuditId", query = "SELECT s FROM StockAudit s WHERE s.stockAuditId = :stockAuditId"),
    @NamedQuery(name = "StockAudit.findBySupervisorId", query = "SELECT s FROM StockAudit s WHERE s.supervisorId = :supervisorId"),
    @NamedQuery(name = "StockAudit.findByStaffId", query = "SELECT s FROM StockAudit s WHERE s.staffId = :staffId"),
    @NamedQuery(name = "StockAudit.findByCreatedDate", query = "SELECT s FROM StockAudit s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "StockAudit.findByActualDate", query = "SELECT s FROM StockAudit s WHERE s.actualDate = :actualDate"),
    @NamedQuery(name = "StockAudit.findByPassQuantity", query = "SELECT s FROM StockAudit s WHERE s.passQuantity = :passQuantity"),
    @NamedQuery(name = "StockAudit.findByFailQuantity", query = "SELECT s FROM StockAudit s WHERE s.failQuantity = :failQuantity"),
    @NamedQuery(name = "StockAudit.findByActualQuantity", query = "SELECT s FROM StockAudit s WHERE s.actualQuantity = :actualQuantity"),
    @NamedQuery(name = "StockAudit.findByRemarks", query = "SELECT s FROM StockAudit s WHERE s.remarks = :remarks"),
    @NamedQuery(name = "StockAudit.findByStockAuditStatus", query = "SELECT s FROM StockAudit s WHERE s.stockAuditStatus = :stockAuditStatus"),
    @NamedQuery(name = "StockAudit.findByStorageBinId", query = "SELECT s FROM StockAudit s WHERE s.storageBinId = :storageBinId"),
    @NamedQuery(name = "StockAudit.findByExpectedQuantity", query = "SELECT s FROM StockAudit s WHERE s.expectedQuantity = :expectedQuantity")})
public class StockAudit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "stockAuditId")
    private Integer stockAuditId;
    @Column(name = "supervisorId")
    private Integer supervisorId;
    @Column(name = "staffId")
    private Integer staffId;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "actualDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualDate;
    @Column(name = "passQuantity")
    private Integer passQuantity;
    @Column(name = "failQuantity")
    private Integer failQuantity;
    @Column(name = "actualQuantity")
    private Integer actualQuantity;
    @Size(max = 255)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "stockAuditStatus")
    private Integer stockAuditStatus;
    @Column(name = "storageBinId")
    private Integer storageBinId;
    @Column(name = "expectedQuantity")
    private Integer expectedQuantity;

    public StockAudit() {
    }

    public StockAudit(Integer stockAuditId) {
        this.stockAuditId = stockAuditId;
    }

    public Integer getStockAuditId() {
        return stockAuditId;
    }

    public void setStockAuditId(Integer stockAuditId) {
        this.stockAuditId = stockAuditId;
    }

    public Integer getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Integer supervisorId) {
        this.supervisorId = supervisorId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public Integer getPassQuantity() {
        return passQuantity;
    }

    public void setPassQuantity(Integer passQuantity) {
        this.passQuantity = passQuantity;
    }

    public Integer getFailQuantity() {
        return failQuantity;
    }

    public void setFailQuantity(Integer failQuantity) {
        this.failQuantity = failQuantity;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStockAuditStatus() {
        return stockAuditStatus;
    }

    public void setStockAuditStatus(Integer stockAuditStatus) {
        this.stockAuditStatus = stockAuditStatus;
    }

    public Integer getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Integer storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Integer getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(Integer expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockAuditId != null ? stockAuditId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockAudit)) {
            return false;
        }
        StockAudit other = (StockAudit) object;
        if ((this.stockAuditId == null && other.stockAuditId != null) || (this.stockAuditId != null && !this.stockAuditId.equals(other.stockAuditId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StockAudit[ stockAuditId=" + stockAuditId + " ]";
    }
    
}
