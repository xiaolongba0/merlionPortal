/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "UserRole")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u"),
    @NamedQuery(name = "UserRole.findByUserRoleId", query = "SELECT u FROM UserRole u WHERE u.userRoleId = :userRoleId"),
    @NamedQuery(name = "UserRole.findByRoleName", query = "SELECT u FROM UserRole u WHERE u.roleName = :roleName"),
    @NamedQuery(name = "UserRole.findByDescription", query = "SELECT u FROM UserRole u WHERE u.description = :description"),
    @NamedQuery(name = "UserRole.findByCanGeneratePO", query = "SELECT u FROM UserRole u WHERE u.canGeneratePO = :canGeneratePO"),
    @NamedQuery(name = "UserRole.findByCanGenerateSO", query = "SELECT u FROM UserRole u WHERE u.canGenerateSO = :canGenerateSO"),
    @NamedQuery(name = "UserRole.findByCanGenerateQuotationAndProductContract", query = "SELECT u FROM UserRole u WHERE u.canGenerateQuotationAndProductContract = :canGenerateQuotationAndProductContract"),
    @NamedQuery(name = "UserRole.findByCanGenerateSalesReport", query = "SELECT u FROM UserRole u WHERE u.canGenerateSalesReport = :canGenerateSalesReport"),
    @NamedQuery(name = "UserRole.findByCanManageUser", query = "SELECT u FROM UserRole u WHERE u.canManageUser = :canManageUser"),
    @NamedQuery(name = "UserRole.findByCanUseForecast", query = "SELECT u FROM UserRole u WHERE u.canUseForecast = :canUseForecast"),
    @NamedQuery(name = "UserRole.findByCanManageProductAndComponent", query = "SELECT u FROM UserRole u WHERE u.canManageProductAndComponent = :canManageProductAndComponent"),
    @NamedQuery(name = "UserRole.findByCanGenerateMRPList", query = "SELECT u FROM UserRole u WHERE u.canGenerateMRPList = :canGenerateMRPList"),
    @NamedQuery(name = "UserRole.findByCanGenerateServicePO", query = "SELECT u FROM UserRole u WHERE u.canGenerateServicePO = :canGenerateServicePO"),
    @NamedQuery(name = "UserRole.findByCanUpdateCustomerCredit", query = "SELECT u FROM UserRole u WHERE u.canUpdateCustomerCredit = :canUpdateCustomerCredit"),
    @NamedQuery(name = "UserRole.findByCanGenerateServiceSO", query = "SELECT u FROM UserRole u WHERE u.canGenerateServiceSO = :canGenerateServiceSO"),
    @NamedQuery(name = "UserRole.findByCanGenerateQuotationRequest", query = "SELECT u FROM UserRole u WHERE u.canGenerateQuotationRequest = :canGenerateQuotationRequest"),
    @NamedQuery(name = "UserRole.findByCanManageServiceCatalog", query = "SELECT u FROM UserRole u WHERE u.canManageServiceCatalog = :canManageServiceCatalog"),
    @NamedQuery(name = "UserRole.findByCanGenerateServiceQuotationAndContract", query = "SELECT u FROM UserRole u WHERE u.canGenerateServiceQuotationAndContract = :canGenerateServiceQuotationAndContract"),
    @NamedQuery(name = "UserRole.findByCanManageTransportationAsset", query = "SELECT u FROM UserRole u WHERE u.canManageTransportationAsset = :canManageTransportationAsset"),
    @NamedQuery(name = "UserRole.findByCanManageTransportationOrder", query = "SELECT u FROM UserRole u WHERE u.canManageTransportationOrder = :canManageTransportationOrder"),
    @NamedQuery(name = "UserRole.findByCanManageLog", query = "SELECT u FROM UserRole u WHERE u.canManageLog = :canManageLog"),
    @NamedQuery(name = "UserRole.findByCanManageAssetMaintenence", query = "SELECT u FROM UserRole u WHERE u.canManageAssetMaintenence = :canManageAssetMaintenence"),
    @NamedQuery(name = "UserRole.findByCanUseHRFunction", query = "SELECT u FROM UserRole u WHERE u.canUseHRFunction = :canUseHRFunction"),
    @NamedQuery(name = "UserRole.findByCanManageWarehouse", query = "SELECT u FROM UserRole u WHERE u.canManageWarehouse = :canManageWarehouse"),
    @NamedQuery(name = "UserRole.findByCanManageStockAuditProcess", query = "SELECT u FROM UserRole u WHERE u.canManageStockAuditProcess = :canManageStockAuditProcess"),
    @NamedQuery(name = "UserRole.findByCanManageStockTransportOrder", query = "SELECT u FROM UserRole u WHERE u.canManageStockTransportOrder = :canManageStockTransportOrder"),
    @NamedQuery(name = "UserRole.findByCanManageReceivingGoods", query = "SELECT u FROM UserRole u WHERE u.canManageReceivingGoods = :canManageReceivingGoods"),
    @NamedQuery(name = "UserRole.findByCanManageOrderFulfillment", query = "SELECT u FROM UserRole u WHERE u.canManageOrderFulfillment = :canManageOrderFulfillment"),
    @NamedQuery(name = "UserRole.findByCanManageKeyAccount", query = "SELECT u FROM UserRole u WHERE u.canManageKeyAccount = :canManageKeyAccount"),
    @NamedQuery(name = "UserRole.findByCanManageBid", query = "SELECT u FROM UserRole u WHERE u.canManageBid = :canManageBid"),
    @NamedQuery(name = "UserRole.findByCanManagePost", query = "SELECT u FROM UserRole u WHERE u.canManagePost = :canManagePost")})
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "userRoleId")
    private Integer userRoleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "roleName")
    private String roleName;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGeneratePO")
    private boolean canGeneratePO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateSO")
    private boolean canGenerateSO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateQuotationAndProductContract")
    private boolean canGenerateQuotationAndProductContract;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateSalesReport")
    private boolean canGenerateSalesReport;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageUser")
    private boolean canManageUser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canUseForecast")
    private boolean canUseForecast;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageProductAndComponent")
    private boolean canManageProductAndComponent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateMRPList")
    private boolean canGenerateMRPList;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateServicePO")
    private boolean canGenerateServicePO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canUpdateCustomerCredit")
    private boolean canUpdateCustomerCredit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateServiceSO")
    private boolean canGenerateServiceSO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateQuotationRequest")
    private boolean canGenerateQuotationRequest;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageServiceCatalog")
    private boolean canManageServiceCatalog;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canGenerateServiceQuotationAndContract")
    private boolean canGenerateServiceQuotationAndContract;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageTransportationAsset")
    private boolean canManageTransportationAsset;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageTransportationOrder")
    private boolean canManageTransportationOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageLog")
    private boolean canManageLog;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageAssetMaintenence")
    private boolean canManageAssetMaintenence;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canUseHRFunction")
    private boolean canUseHRFunction;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageWarehouse")
    private boolean canManageWarehouse;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageStockAuditProcess")
    private boolean canManageStockAuditProcess;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageStockTransportOrder")
    private boolean canManageStockTransportOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageReceivingGoods")
    private boolean canManageReceivingGoods;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageOrderFulfillment")
    private boolean canManageOrderFulfillment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageKeyAccount")
    private boolean canManageKeyAccount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManageBid")
    private boolean canManageBid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canManagePost")
    private boolean canManagePost;
    @ManyToMany(mappedBy = "userRoleList")
    private List<SystemUser> systemUserList;
    @JoinColumn(name = "company", referencedColumnName = "companyId")
    @ManyToOne(optional = false)
    private Company company;

    public UserRole() {
    }

    public UserRole(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public UserRole(Integer userRoleId, String roleName, boolean canGeneratePO, boolean canGenerateSO, boolean canGenerateQuotationAndProductContract, boolean canGenerateSalesReport, boolean canManageUser, boolean canUseForecast, boolean canManageProductAndComponent, boolean canGenerateMRPList, boolean canGenerateServicePO, boolean canUpdateCustomerCredit, boolean canGenerateServiceSO, boolean canGenerateQuotationRequest, boolean canManageServiceCatalog, boolean canGenerateServiceQuotationAndContract, boolean canManageTransportationAsset, boolean canManageTransportationOrder, boolean canManageLog, boolean canManageAssetMaintenence, boolean canUseHRFunction, boolean canManageWarehouse, boolean canManageStockAuditProcess, boolean canManageStockTransportOrder, boolean canManageReceivingGoods, boolean canManageOrderFulfillment, boolean canManageKeyAccount, boolean canManageBid, boolean canManagePost) {
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.canGeneratePO = canGeneratePO;
        this.canGenerateSO = canGenerateSO;
        this.canGenerateQuotationAndProductContract = canGenerateQuotationAndProductContract;
        this.canGenerateSalesReport = canGenerateSalesReport;
        this.canManageUser = canManageUser;
        this.canUseForecast = canUseForecast;
        this.canManageProductAndComponent = canManageProductAndComponent;
        this.canGenerateMRPList = canGenerateMRPList;
        this.canGenerateServicePO = canGenerateServicePO;
        this.canUpdateCustomerCredit = canUpdateCustomerCredit;
        this.canGenerateServiceSO = canGenerateServiceSO;
        this.canGenerateQuotationRequest = canGenerateQuotationRequest;
        this.canManageServiceCatalog = canManageServiceCatalog;
        this.canGenerateServiceQuotationAndContract = canGenerateServiceQuotationAndContract;
        this.canManageTransportationAsset = canManageTransportationAsset;
        this.canManageTransportationOrder = canManageTransportationOrder;
        this.canManageLog = canManageLog;
        this.canManageAssetMaintenence = canManageAssetMaintenence;
        this.canUseHRFunction = canUseHRFunction;
        this.canManageWarehouse = canManageWarehouse;
        this.canManageStockAuditProcess = canManageStockAuditProcess;
        this.canManageStockTransportOrder = canManageStockTransportOrder;
        this.canManageReceivingGoods = canManageReceivingGoods;
        this.canManageOrderFulfillment = canManageOrderFulfillment;
        this.canManageKeyAccount = canManageKeyAccount;
        this.canManageBid = canManageBid;
        this.canManagePost = canManagePost;
    }

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getCanGeneratePO() {
        return canGeneratePO;
    }

    public void setCanGeneratePO(boolean canGeneratePO) {
        this.canGeneratePO = canGeneratePO;
    }

    public boolean getCanGenerateSO() {
        return canGenerateSO;
    }

    public void setCanGenerateSO(boolean canGenerateSO) {
        this.canGenerateSO = canGenerateSO;
    }

    public boolean getCanGenerateQuotationAndProductContract() {
        return canGenerateQuotationAndProductContract;
    }

    public void setCanGenerateQuotationAndProductContract(boolean canGenerateQuotationAndProductContract) {
        this.canGenerateQuotationAndProductContract = canGenerateQuotationAndProductContract;
    }

    public boolean getCanGenerateSalesReport() {
        return canGenerateSalesReport;
    }

    public void setCanGenerateSalesReport(boolean canGenerateSalesReport) {
        this.canGenerateSalesReport = canGenerateSalesReport;
    }

    public boolean getCanManageUser() {
        return canManageUser;
    }

    public void setCanManageUser(boolean canManageUser) {
        this.canManageUser = canManageUser;
    }

    public boolean getCanUseForecast() {
        return canUseForecast;
    }

    public void setCanUseForecast(boolean canUseForecast) {
        this.canUseForecast = canUseForecast;
    }

    public boolean getCanManageProductAndComponent() {
        return canManageProductAndComponent;
    }

    public void setCanManageProductAndComponent(boolean canManageProductAndComponent) {
        this.canManageProductAndComponent = canManageProductAndComponent;
    }

    public boolean getCanGenerateMRPList() {
        return canGenerateMRPList;
    }

    public void setCanGenerateMRPList(boolean canGenerateMRPList) {
        this.canGenerateMRPList = canGenerateMRPList;
    }

    public boolean getCanGenerateServicePO() {
        return canGenerateServicePO;
    }

    public void setCanGenerateServicePO(boolean canGenerateServicePO) {
        this.canGenerateServicePO = canGenerateServicePO;
    }

    public boolean getCanUpdateCustomerCredit() {
        return canUpdateCustomerCredit;
    }

    public void setCanUpdateCustomerCredit(boolean canUpdateCustomerCredit) {
        this.canUpdateCustomerCredit = canUpdateCustomerCredit;
    }

    public boolean getCanGenerateServiceSO() {
        return canGenerateServiceSO;
    }

    public void setCanGenerateServiceSO(boolean canGenerateServiceSO) {
        this.canGenerateServiceSO = canGenerateServiceSO;
    }

    public boolean getCanGenerateQuotationRequest() {
        return canGenerateQuotationRequest;
    }

    public void setCanGenerateQuotationRequest(boolean canGenerateQuotationRequest) {
        this.canGenerateQuotationRequest = canGenerateQuotationRequest;
    }

    public boolean getCanManageServiceCatalog() {
        return canManageServiceCatalog;
    }

    public void setCanManageServiceCatalog(boolean canManageServiceCatalog) {
        this.canManageServiceCatalog = canManageServiceCatalog;
    }

    public boolean getCanGenerateServiceQuotationAndContract() {
        return canGenerateServiceQuotationAndContract;
    }

    public void setCanGenerateServiceQuotationAndContract(boolean canGenerateServiceQuotationAndContract) {
        this.canGenerateServiceQuotationAndContract = canGenerateServiceQuotationAndContract;
    }

    public boolean getCanManageTransportationAsset() {
        return canManageTransportationAsset;
    }

    public void setCanManageTransportationAsset(boolean canManageTransportationAsset) {
        this.canManageTransportationAsset = canManageTransportationAsset;
    }

    public boolean getCanManageTransportationOrder() {
        return canManageTransportationOrder;
    }

    public void setCanManageTransportationOrder(boolean canManageTransportationOrder) {
        this.canManageTransportationOrder = canManageTransportationOrder;
    }

    public boolean getCanManageLog() {
        return canManageLog;
    }

    public void setCanManageLog(boolean canManageLog) {
        this.canManageLog = canManageLog;
    }

    public boolean getCanManageAssetMaintenence() {
        return canManageAssetMaintenence;
    }

    public void setCanManageAssetMaintenence(boolean canManageAssetMaintenence) {
        this.canManageAssetMaintenence = canManageAssetMaintenence;
    }

    public boolean getCanUseHRFunction() {
        return canUseHRFunction;
    }

    public void setCanUseHRFunction(boolean canUseHRFunction) {
        this.canUseHRFunction = canUseHRFunction;
    }

    public boolean getCanManageWarehouse() {
        return canManageWarehouse;
    }

    public void setCanManageWarehouse(boolean canManageWarehouse) {
        this.canManageWarehouse = canManageWarehouse;
    }

    public boolean getCanManageStockAuditProcess() {
        return canManageStockAuditProcess;
    }

    public void setCanManageStockAuditProcess(boolean canManageStockAuditProcess) {
        this.canManageStockAuditProcess = canManageStockAuditProcess;
    }

    public boolean getCanManageStockTransportOrder() {
        return canManageStockTransportOrder;
    }

    public void setCanManageStockTransportOrder(boolean canManageStockTransportOrder) {
        this.canManageStockTransportOrder = canManageStockTransportOrder;
    }

    public boolean getCanManageReceivingGoods() {
        return canManageReceivingGoods;
    }

    public void setCanManageReceivingGoods(boolean canManageReceivingGoods) {
        this.canManageReceivingGoods = canManageReceivingGoods;
    }

    public boolean getCanManageOrderFulfillment() {
        return canManageOrderFulfillment;
    }

    public void setCanManageOrderFulfillment(boolean canManageOrderFulfillment) {
        this.canManageOrderFulfillment = canManageOrderFulfillment;
    }

    public boolean getCanManageKeyAccount() {
        return canManageKeyAccount;
    }

    public void setCanManageKeyAccount(boolean canManageKeyAccount) {
        this.canManageKeyAccount = canManageKeyAccount;
    }

    public boolean getCanManageBid() {
        return canManageBid;
    }

    public void setCanManageBid(boolean canManageBid) {
        this.canManageBid = canManageBid;
    }

    public boolean getCanManagePost() {
        return canManagePost;
    }

    public void setCanManagePost(boolean canManagePost) {
        this.canManagePost = canManagePost;
    }

    @XmlTransient
    public List<SystemUser> getSystemUserList() {
        return systemUserList;
    }

    public void setSystemUserList(List<SystemUser> systemUserList) {
        this.systemUserList = systemUserList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userRoleId != null ? userRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRole)) {
            return false;
        }
        UserRole other = (UserRole) object;
        if ((this.userRoleId == null && other.userRoleId != null) || (this.userRoleId != null && !this.userRoleId.equals(other.userRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UserRole[ userRoleId=" + userRoleId + " ]";
    }
    
}
