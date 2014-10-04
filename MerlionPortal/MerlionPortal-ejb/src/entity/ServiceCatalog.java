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
@Table(name = "ServiceCatalog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceCatalog.findAll", query = "SELECT s FROM ServiceCatalog s"),
    @NamedQuery(name = "ServiceCatalog.findByServiceCatalogId", query = "SELECT s FROM ServiceCatalog s WHERE s.serviceCatalogId = :serviceCatalogId"),
    @NamedQuery(name = "ServiceCatalog.findByServiceName", query = "SELECT s FROM ServiceCatalog s WHERE s.serviceName = :serviceName"),
    @NamedQuery(name = "ServiceCatalog.findByServiceDescription", query = "SELECT s FROM ServiceCatalog s WHERE s.serviceDescription = :serviceDescription"),
    @NamedQuery(name = "ServiceCatalog.findByPublicView", query = "SELECT s FROM ServiceCatalog s WHERE s.publicView = :publicView"),
    @NamedQuery(name = "ServiceCatalog.findByServiceType", query = "SELECT s FROM ServiceCatalog s WHERE s.serviceType = :serviceType"),
    @NamedQuery(name = "ServiceCatalog.findByPricePerTEU", query = "SELECT s FROM ServiceCatalog s WHERE s.pricePerTEU = :pricePerTEU"),
    @NamedQuery(name = "ServiceCatalog.findByCompanyId", query = "SELECT s FROM ServiceCatalog s WHERE s.companyId = :companyId"),
    @NamedQuery(name = "ServiceCatalog.findByVoid1", query = "SELECT s FROM ServiceCatalog s WHERE s.void1 = :void1"),
    @NamedQuery(name = "ServiceCatalog.findByCompanyName", query = "SELECT s FROM ServiceCatalog s WHERE s.companyName = :companyName")})
public class ServiceCatalog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "serviceCatalogId")
    private Integer serviceCatalogId;
    @Size(max = 255)
    @Column(name = "serviceName")
    private String serviceName;
    @Size(max = 1000)
    @Column(name = "serviceDescription")
    private String serviceDescription;
    @Column(name = "publicView")
    private Boolean publicView;
    @Size(max = 45)
    @Column(name = "serviceType")
    private String serviceType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pricePerTEU")
    private Double pricePerTEU;
    @Column(name = "companyId")
    private Integer companyId;
    @Column(name = "void")
    private Boolean void1;
    @Size(max = 45)
    @Column(name = "companyName")
    private String companyName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceCatalog")
    private List<ServiceQuotation> serviceQuotationList;

    public ServiceCatalog() {
    }

    public ServiceCatalog(Integer serviceCatalogId) {
        this.serviceCatalogId = serviceCatalogId;
    }

    public Integer getServiceCatalogId() {
        return serviceCatalogId;
    }

    public void setServiceCatalogId(Integer serviceCatalogId) {
        this.serviceCatalogId = serviceCatalogId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Boolean getPublicView() {
        return publicView;
    }

    public void setPublicView(Boolean publicView) {
        this.publicView = publicView;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getPricePerTEU() {
        return pricePerTEU;
    }

    public void setPricePerTEU(Double pricePerTEU) {
        this.pricePerTEU = pricePerTEU;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean getVoid1() {
        return void1;
    }

    public void setVoid1(Boolean void1) {
        this.void1 = void1;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
        hash += (serviceCatalogId != null ? serviceCatalogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceCatalog)) {
            return false;
        }
        ServiceCatalog other = (ServiceCatalog) object;
        if ((this.serviceCatalogId == null && other.serviceCatalogId != null) || (this.serviceCatalogId != null && !this.serviceCatalogId.equals(other.serviceCatalogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ServiceCatalog[ serviceCatalogId=" + serviceCatalogId + " ]";
    }
    
}
