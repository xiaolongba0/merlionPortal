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
@Table(name = "CustomerAccount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerAccount.findAll", query = "SELECT c FROM CustomerAccount c"),
    @NamedQuery(name = "CustomerAccount.findByCustomerAccountId", query = "SELECT c FROM CustomerAccount c WHERE c.customerAccountId = :customerAccountId"),
    @NamedQuery(name = "CustomerAccount.findByCustomerCompanyId", query = "SELECT c FROM CustomerAccount c WHERE c.customerCompanyId = :customerCompanyId"),
    @NamedQuery(name = "CustomerAccount.findByKeyAccount", query = "SELECT c FROM CustomerAccount c WHERE c.keyAccount = :keyAccount"),
    @NamedQuery(name = "CustomerAccount.findByRemarks", query = "SELECT c FROM CustomerAccount c WHERE c.remarks = :remarks"),
    @NamedQuery(name = "CustomerAccount.findByMyCompanyId", query = "SELECT c FROM CustomerAccount c WHERE c.myCompanyId = :myCompanyId")})
public class CustomerAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "customerAccountId")
    private Integer customerAccountId;
    @Column(name = "customerCompanyId")
    private Integer customerCompanyId;
    @Column(name = "keyAccount")
    private Boolean keyAccount;
    @Size(max = 2000)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "myCompanyId")
    private Integer myCompanyId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerAccount")
    private List<ServiceQuotation> serviceQuotationList;

    public CustomerAccount() {
    }

    public CustomerAccount(Integer customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public Integer getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(Integer customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public Integer getCustomerCompanyId() {
        return customerCompanyId;
    }

    public void setCustomerCompanyId(Integer customerCompanyId) {
        this.customerCompanyId = customerCompanyId;
    }

    public Boolean getKeyAccount() {
        return keyAccount;
    }

    public void setKeyAccount(Boolean keyAccount) {
        this.keyAccount = keyAccount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getMyCompanyId() {
        return myCompanyId;
    }

    public void setMyCompanyId(Integer myCompanyId) {
        this.myCompanyId = myCompanyId;
    }

    @XmlTransient
    public List<ServiceQuotation> getServiceQuotationList() {
        return serviceQuotationList;
    }

    public void setServiceQuotationList(List<ServiceQuotation> serviceQuotationList) {
        this.serviceQuotationList = serviceQuotationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerAccountId != null ? customerAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerAccount)) {
            return false;
        }
        CustomerAccount other = (CustomerAccount) object;
        if ((this.customerAccountId == null && other.customerAccountId != null) || (this.customerAccountId != null && !this.customerAccountId.equals(other.customerAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerAccount[ customerAccountId=" + customerAccountId + " ]";
    }
    
}
