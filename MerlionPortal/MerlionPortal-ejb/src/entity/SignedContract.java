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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "SignedContract")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SignedContract.findAll", query = "SELECT s FROM SignedContract s"),
    @NamedQuery(name = "SignedContract.findBySignedContractId", query = "SELECT s FROM SignedContract s WHERE s.signedContractId = :signedContractId"),
    @NamedQuery(name = "SignedContract.findByUploadedDate", query = "SELECT s FROM SignedContract s WHERE s.uploadedDate = :uploadedDate")})
public class SignedContract implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "signedContractId")
    private Integer signedContractId;
    @Lob
    @Column(name = "pdf")
    private byte[] pdf;
    @Column(name = "uploadedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedDate;
    @JoinColumn(name = "signedContractId", referencedColumnName = "contractId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Contract contract;

    public SignedContract() {
    }

    public SignedContract(Integer signedContractId) {
        this.signedContractId = signedContractId;
    }

    public Integer getSignedContractId() {
        return signedContractId;
    }

    public void setSignedContractId(Integer signedContractId) {
        this.signedContractId = signedContractId;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public Date getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (signedContractId != null ? signedContractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SignedContract)) {
            return false;
        }
        SignedContract other = (SignedContract) object;
        if ((this.signedContractId == null && other.signedContractId != null) || (this.signedContractId != null && !this.signedContractId.equals(other.signedContractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SignedContract[ signedContractId=" + signedContractId + " ]";
    }
    
}
