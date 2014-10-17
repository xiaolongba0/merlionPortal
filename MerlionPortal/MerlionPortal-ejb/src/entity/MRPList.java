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
import javax.persistence.JoinColumn;
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
@Table(name = "MRPList")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MRPList.findAll", query = "SELECT m FROM MRPList m"),
    @NamedQuery(name = "MRPList.findByMrpListId", query = "SELECT m FROM MRPList m WHERE m.mrpListId = :mrpListId"),
    @NamedQuery(name = "MRPList.findByMrpDate", query = "SELECT m FROM MRPList m WHERE m.mrpDate = :mrpDate"),
    @NamedQuery(name = "MRPList.findByProductId", query = "SELECT m FROM MRPList m WHERE m.productId = :productId")})
public class MRPList implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mrpListId")
    private Integer mrpListId;
    @Column(name = "mrpDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mrpDate;
    @Column(name = "productId")
    private Integer productId;
    @JoinColumn(name = "mrpListId", referencedColumnName = "mpsId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Mps mps;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mrpList")
    private List<Mrp> mrpList;

    public MRPList() {
    }

    public MRPList(Integer mrpListId) {
        this.mrpListId = mrpListId;
    }

    public Integer getMrpListId() {
        return mrpListId;
    }

    public void setMrpListId(Integer mrpListId) {
        this.mrpListId = mrpListId;
    }

    public Date getMrpDate() {
        return mrpDate;
    }

    public void setMrpDate(Date mrpDate) {
        this.mrpDate = mrpDate;
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
    public List<Mrp> getMrpList() {
        return mrpList;
    }

    public void setMrpList(List<Mrp> mrpList) {
        this.mrpList = mrpList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrpListId != null ? mrpListId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MRPList)) {
            return false;
        }
        MRPList other = (MRPList) object;
        if ((this.mrpListId == null && other.mrpListId != null) || (this.mrpListId != null && !this.mrpListId.equals(other.mrpListId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MRPList[ mrpListId=" + mrpListId + " ]";
    }
    
}
