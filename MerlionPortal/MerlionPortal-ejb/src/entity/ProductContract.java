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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "ProductContract")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductContract.findAll", query = "SELECT p FROM ProductContract p"),
    @NamedQuery(name = "ProductContract.findByProductContractId", query = "SELECT p FROM ProductContract p WHERE p.productContractId = :productContractId"),
    @NamedQuery(name = "ProductContract.findByContractTerm", query = "SELECT p FROM ProductContract p WHERE p.contractTerm = :contractTerm"),
    @NamedQuery(name = "ProductContract.findByStatus", query = "SELECT p FROM ProductContract p WHERE p.status = :status"),
    @NamedQuery(name = "ProductContract.findByValidity", query = "SELECT p FROM ProductContract p WHERE p.validity = :validity"),
    @NamedQuery(name = "ProductContract.findByCreateDate", query = "SELECT p FROM ProductContract p WHERE p.createDate = :createDate"),
    @NamedQuery(name = "ProductContract.findByCreatorId", query = "SELECT p FROM ProductContract p WHERE p.creatorId = :creatorId"),
    @NamedQuery(name = "ProductContract.findByDiscountRate", query = "SELECT p FROM ProductContract p WHERE p.discountRate = :discountRate"),
    @NamedQuery(name = "ProductContract.findByClientId", query = "SELECT p FROM ProductContract p WHERE p.clientId = :clientId"),
    @NamedQuery(name = "ProductContract.findByQuotationId", query = "SELECT p FROM ProductContract p WHERE p.quotationId = :quotationId")})
public class ProductContract implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "productContractId")
    private Integer productContractId;
    @Size(max = 255)
    @Column(name = "contractTerm")
    private String contractTerm;
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "validity")
    private String validity;
    @Column(name = "createDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 45)
    @Column(name = "creatorId")
    private String creatorId;
    @Size(max = 255)
    @Column(name = "discountRate")
    private String discountRate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clientId")
    private int clientId;
    @Lob
    @Column(name = "signedContract")
    private byte[] signedContract;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quotationId")
    private int quotationId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productContractproductContractId")
    private List<Location> locationList;
    @JoinColumn(name = "companyId", referencedColumnName = "companyId")
    @ManyToOne(optional = false)
    private Company companyId;

    public ProductContract() {
    }

    public ProductContract(Integer productContractId) {
        this.productContractId = productContractId;
    }

    public ProductContract(Integer productContractId, int clientId, int quotationId) {
        this.productContractId = productContractId;
        this.clientId = clientId;
        this.quotationId = quotationId;
    }

    public Integer getProductContractId() {
        return productContractId;
    }

    public void setProductContractId(Integer productContractId) {
        this.productContractId = productContractId;
    }

    public String getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public byte[] getSignedContract() {
        return signedContract;
    }

    public void setSignedContract(byte[] signedContract) {
        this.signedContract = signedContract;
    }

    public int getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(int quotationId) {
        this.quotationId = quotationId;
    }

    @XmlTransient
    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productContractId != null ? productContractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductContract)) {
            return false;
        }
        ProductContract other = (ProductContract) object;
        if ((this.productContractId == null && other.productContractId != null) || (this.productContractId != null && !this.productContractId.equals(other.productContractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductContract[ productContractId=" + productContractId + " ]";
    }
    
}
