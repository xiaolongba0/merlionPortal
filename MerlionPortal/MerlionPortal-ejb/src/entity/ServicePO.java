/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "ServicePO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServicePO.findAll", query = "SELECT s FROM ServicePO s"),
    @NamedQuery(name = "ServicePO.findByServicePOId", query = "SELECT s FROM ServicePO s WHERE s.servicePOId = :servicePOId"),
    @NamedQuery(name = "ServicePO.findByServiceType", query = "SELECT s FROM ServicePO s WHERE s.serviceType = :serviceType"),
    @NamedQuery(name = "ServicePO.findByCreatedDate", query = "SELECT s FROM ServicePO s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "ServicePO.findByStatus", query = "SELECT s FROM ServicePO s WHERE s.status = :status"),
    @NamedQuery(name = "ServicePO.findByPrice", query = "SELECT s FROM ServicePO s WHERE s.price = :price"),
    @NamedQuery(name = "ServicePO.findByReceiverCompanyId", query = "SELECT s FROM ServicePO s WHERE s.receiverCompanyId = :receiverCompanyId"),
    @NamedQuery(name = "ServicePO.findBySenderCompanyId", query = "SELECT s FROM ServicePO s WHERE s.senderCompanyId = :senderCompanyId"),
    @NamedQuery(name = "ServicePO.findByCreatorId", query = "SELECT s FROM ServicePO s WHERE s.creatorId = :creatorId"),
    @NamedQuery(name = "ServicePO.findByVolume", query = "SELECT s FROM ServicePO s WHERE s.volume = :volume"),
    @NamedQuery(name = "ServicePO.findByServiceDeliveryDate", query = "SELECT s FROM ServicePO s WHERE s.serviceDeliveryDate = :serviceDeliveryDate"),
    @NamedQuery(name = "ServicePO.findByProductId", query = "SELECT s FROM ServicePO s WHERE s.productId = :productId"),
    @NamedQuery(name = "ServicePO.findByProductQuantityPerTEU", query = "SELECT s FROM ServicePO s WHERE s.productQuantityPerTEU = :productQuantityPerTEU"),
    @NamedQuery(name = "ServicePO.findByWarehousePOtype", query = "SELECT s FROM ServicePO s WHERE s.warehousePOtype = :warehousePOtype"),
    @NamedQuery(name = "ServicePO.findByServiceReceiveDate", query = "SELECT s FROM ServicePO s WHERE s.serviceReceiveDate = :serviceReceiveDate"),
    @NamedQuery(name = "ServicePO.findByServiceFulfilmentDate", query = "SELECT s FROM ServicePO s WHERE s.serviceFulfilmentDate = :serviceFulfilmentDate")})
public class ServicePO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "servicePOId")
    private Integer servicePOId;
    @Size(max = 45)
    @Column(name = "serviceType")
    private String serviceType;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "status")
    private Integer status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @Column(name = "receiverCompanyId")
    private Integer receiverCompanyId;
    @Column(name = "senderCompanyId")
    private Integer senderCompanyId;
    @Column(name = "creatorId")
    private Integer creatorId;
    @Column(name = "volume")
    private Integer volume;
    @Column(name = "serviceDeliveryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDeliveryDate;
    @Column(name = "productId")
    private Integer productId;
    @Column(name = "productQuantityPerTEU")
    private Integer productQuantityPerTEU;
    @Size(max = 45)
    @Column(name = "warehousePOtype")
    private String warehousePOtype;
    @Column(name = "serviceReceiveDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceReceiveDate;
    @Column(name = "serviceFulfilmentDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceFulfilmentDate;
    @JoinColumn(name = "contract", referencedColumnName = "contractId")
    @ManyToOne(optional = false)
    private Contract contract;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "servicePO")
    private ServiceInvoice serviceInvoice;

    public ServicePO() {
    }

    public ServicePO(Integer servicePOId) {
        this.servicePOId = servicePOId;
    }

    public Integer getServicePOId() {
        return servicePOId;
    }

    public void setServicePOId(Integer servicePOId) {
        this.servicePOId = servicePOId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getReceiverCompanyId() {
        return receiverCompanyId;
    }

    public void setReceiverCompanyId(Integer receiverCompanyId) {
        this.receiverCompanyId = receiverCompanyId;
    }

    public Integer getSenderCompanyId() {
        return senderCompanyId;
    }

    public void setSenderCompanyId(Integer senderCompanyId) {
        this.senderCompanyId = senderCompanyId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Date getServiceDeliveryDate() {
        return serviceDeliveryDate;
    }

    public void setServiceDeliveryDate(Date serviceDeliveryDate) {
        this.serviceDeliveryDate = serviceDeliveryDate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductQuantityPerTEU() {
        return productQuantityPerTEU;
    }

    public void setProductQuantityPerTEU(Integer productQuantityPerTEU) {
        this.productQuantityPerTEU = productQuantityPerTEU;
    }

    public String getWarehousePOtype() {
        return warehousePOtype;
    }

    public void setWarehousePOtype(String warehousePOtype) {
        this.warehousePOtype = warehousePOtype;
    }

    public Date getServiceReceiveDate() {
        return serviceReceiveDate;
    }

    public void setServiceReceiveDate(Date serviceReceiveDate) {
        this.serviceReceiveDate = serviceReceiveDate;
    }

    public Date getServiceFulfilmentDate() {
        return serviceFulfilmentDate;
    }

    public void setServiceFulfilmentDate(Date serviceFulfilmentDate) {
        this.serviceFulfilmentDate = serviceFulfilmentDate;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public ServiceInvoice getServiceInvoice() {
        return serviceInvoice;
    }

    public void setServiceInvoice(ServiceInvoice serviceInvoice) {
        this.serviceInvoice = serviceInvoice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servicePOId != null ? servicePOId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServicePO)) {
            return false;
        }
        ServicePO other = (ServicePO) object;
        if ((this.servicePOId == null && other.servicePOId != null) || (this.servicePOId != null && !this.servicePOId.equals(other.servicePOId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ServicePO[ servicePOId=" + servicePOId + " ]";
    }
    
}
