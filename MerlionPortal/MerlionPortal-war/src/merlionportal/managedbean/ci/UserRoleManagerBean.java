/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import entity.Company;
import entity.SystemUser;
import entity.UserRole;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.GetCompanySessionBean;
import merlionportal.ci.administrationmodule.RoleManagementBean;
import merlionportal.ci.administrationmodule.UserAccountManagementBean;

/**
 *
 * @author manliqi
 */
@ManagedBean(name = "createRole")
@ViewScoped
public class UserRoleManagerBean implements Serializable {

    @EJB
    private RoleManagementBean rmb;

    @EJB
    UserAccountManagementBean uamb;
    @EJB
    GetCompanySessionBean gcsb;

    private SystemUser loginedUser;

    private List<Company> companys;
    private Integer selectCompanyId;

    private String roleName;
    private String roleDescription;
    private boolean canGeneratePO;
    private boolean canGenerateSO;
    private boolean canGenerateQuotationAndProductContract;
    private boolean canGenerateSalesReport;
    private boolean canManageUser;
    private boolean canUseForecast;
    private boolean canManageProductAndComponent;
    private boolean canGenerateMRPList;
    private boolean canGenerateServicePO;
    private boolean canUpdateCustomerCredit;
    private boolean canGenerateServiceSO;
    private boolean canGenerateQuotationRequest;
    private boolean canManageServiceCatalog;
    private boolean canGenerateServiceQuotationAndContract;
    private boolean canManageKeyAccount;
    private boolean canManageTransportationAsset;
    private boolean canManageTransportationOrder;
    private boolean canManageLocation;
    private boolean canManageAssetType;
    private boolean canUseHRFunction;
    private boolean canManageWarehouse;
    private boolean canManageStockAuditProcess;
    private boolean canManageStockTransportOrder;
    private boolean canManageReceivingGoods;
    private boolean canManageOrderFulfillment;
    private boolean canManageBid;
    private boolean canManagePost;

    private List<UserRole> roles;

    public UserRoleManagerBean() {
        canGeneratePO = false;
        canGenerateSO = false;
        canGenerateQuotationAndProductContract = false;
        canGenerateSalesReport = false;
        canManageUser = false;
        canUseForecast = false;
        canManageProductAndComponent = false;
        canGenerateMRPList = false;
        canGenerateServicePO = false;
        canUpdateCustomerCredit = false;
        canGenerateServiceSO = false;
        canGenerateQuotationRequest = false;
        canManageServiceCatalog = false;
        canGenerateServiceQuotationAndContract = false;
        canManageKeyAccount = false;
        canManageTransportationAsset = false;
        canManageTransportationOrder = false;
        canManageLocation = false;
        canManageAssetType = false;
        canUseHRFunction = false;
        canManageWarehouse = false;
        canManageStockAuditProcess = false;
        canManageStockTransportOrder = false;
        canManageReceivingGoods = false;
        canManageOrderFulfillment = false;
        canManageBid = false;
        canManagePost = false;
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            if (loginedUser != null) {
                redirect = false;
            }
        }
        if (redirect) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        companys = (List<Company>) gcsb.getCompanies();
    }

    public void createRole(ActionEvent event) {
        Integer operatorId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        Integer companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
        if (rmb.isSuperUser(operatorId)) {
            companyId = selectCompanyId;
        }
        int result = rmb.createCompanyRole(operatorId, companyId, roleName, roleDescription, canGeneratePO, canGenerateSO, canGenerateQuotationAndProductContract, canGenerateSalesReport, canManageUser, canUseForecast, canManageProductAndComponent, canGenerateMRPList, canGenerateServicePO, canUpdateCustomerCredit, canGenerateServiceSO, canGenerateQuotationRequest, canManageServiceCatalog, canGenerateServiceQuotationAndContract, canManageKeyAccount, canManageTransportationAsset, canManageTransportationOrder, canManageLocation, canManageAssetType, canUseHRFunction, canManageWarehouse, canManageStockAuditProcess, canManageStockTransportOrder, canManageReceivingGoods, canManageOrderFulfillment, canManageBid, canManagePost);
        if (result == 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Role Created!", "You can now assign users to this role"));

        } else if (result == -1) {

            System.out.println("Access Denied");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action!"));

        } else {
//      redirect to login page
        }

    }

    public List<UserRole> getRoles() {
        //Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        //Integer company = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
        Integer operatorId = 3;
        Integer companyId = 2;

        return rmb.viewAllRoles(operatorId, companyId);

    }

    //    <editor-fold defaultstate="collapsed" desc="getters and setters">
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public boolean isCanGeneratePO() {
        return canGeneratePO;
    }

    public void setCanGeneratePO(boolean canGeneratePO) {
        this.canGeneratePO = canGeneratePO;
    }

    public boolean isCanGenerateSO() {
        return canGenerateSO;
    }

    public void setCanGenerateSO(boolean canGenerateSO) {
        this.canGenerateSO = canGenerateSO;
    }

    public boolean isCanGenerateQuotationAndProductContract() {
        return canGenerateQuotationAndProductContract;
    }

    public void setCanGenerateQuotationAndProductContract(boolean canGenerateQuotationAndProductContract) {
        this.canGenerateQuotationAndProductContract = canGenerateQuotationAndProductContract;
    }

    public boolean isCanGenerateSalesReport() {
        return canGenerateSalesReport;
    }

    public void setCanGenerateSalesReport(boolean canGenerateSalesReport) {
        this.canGenerateSalesReport = canGenerateSalesReport;
    }

    public boolean isCanManageUser() {
        return canManageUser;
    }

    public void setCanManageUser(boolean canManageUser) {
        this.canManageUser = canManageUser;
    }

    public boolean isCanUseForecast() {
        return canUseForecast;
    }

    public void setCanUseForecast(boolean canUseForecast) {
        this.canUseForecast = canUseForecast;
    }

    public boolean isCanManageProductAndComponent() {
        return canManageProductAndComponent;
    }

    public void setCanManageProductAndComponent(boolean canManageProductAndComponent) {
        this.canManageProductAndComponent = canManageProductAndComponent;
    }

    public boolean isCanGenerateMRPList() {
        return canGenerateMRPList;
    }

    public void setCanGenerateMRPList(boolean canGenerateMRPList) {
        this.canGenerateMRPList = canGenerateMRPList;
    }

    public boolean isCanGenerateServicePO() {
        return canGenerateServicePO;
    }

    public void setCanGenerateServicePO(boolean canGenerateServicePO) {
        this.canGenerateServicePO = canGenerateServicePO;
    }

    public boolean isCanUpdateCustomerCredit() {
        return canUpdateCustomerCredit;
    }

    public void setCanUpdateCustomerCredit(boolean canUpdateCustomerCredit) {
        this.canUpdateCustomerCredit = canUpdateCustomerCredit;
    }

    public boolean isCanGenerateServiceSO() {
        return canGenerateServiceSO;
    }

    public void setCanGenerateServiceSO(boolean canGenerateServiceSO) {
        this.canGenerateServiceSO = canGenerateServiceSO;
    }

    public boolean isCanGenerateQuotationRequest() {
        return canGenerateQuotationRequest;
    }

    public void setCanGenerateQuotationRequest(boolean canGenerateQuotationRequest) {
        this.canGenerateQuotationRequest = canGenerateQuotationRequest;
    }

    public boolean isCanManageServiceCatalog() {
        return canManageServiceCatalog;
    }

    public void setCanManageServiceCatalog(boolean canManageServiceCatalog) {
        this.canManageServiceCatalog = canManageServiceCatalog;
    }

    public boolean isCanGenerateServiceQuotationAndContract() {
        return canGenerateServiceQuotationAndContract;
    }

    public void setCanGenerateServiceQuotationAndContract(boolean canGenerateServiceQuotationAndContract) {
        this.canGenerateServiceQuotationAndContract = canGenerateServiceQuotationAndContract;
    }

    public boolean isCanManageKeyAccount() {
        return canManageKeyAccount;
    }

    public void setCanManageKeyAccount(boolean canManageKeyAccount) {
        this.canManageKeyAccount = canManageKeyAccount;
    }

    public boolean isCanManageTransportationAsset() {
        return canManageTransportationAsset;
    }

    public void setCanManageTransportationAsset(boolean canManageTransportationAsset) {
        this.canManageTransportationAsset = canManageTransportationAsset;
    }

    public boolean isCanManageTransportationOrder() {
        return canManageTransportationOrder;
    }

    public void setCanManageTransportationOrder(boolean canManageTransportationOrder) {
        this.canManageTransportationOrder = canManageTransportationOrder;
    }

    public boolean isCanManageLocation() {
        return canManageLocation;
    }

    public void setCanManageLocation(boolean canManageLocation) {
        this.canManageLocation = canManageLocation;
    }

    public boolean isCanManageAssetType() {
        return canManageAssetType;
    }

    public void setCanManageAssetType(boolean canManageAssetType) {
        this.canManageAssetType = canManageAssetType;
    }

    public boolean isCanUseHRFunction() {
        return canUseHRFunction;
    }

    public void setCanUseHRFunction(boolean canUseHRFunction) {
        this.canUseHRFunction = canUseHRFunction;
    }

    public boolean isCanManageWarehouse() {
        return canManageWarehouse;
    }

    public void setCanManageWarehouse(boolean canManageWarehouse) {
        this.canManageWarehouse = canManageWarehouse;
    }

    public boolean isCanManageStockAuditProcess() {
        return canManageStockAuditProcess;
    }

    public void setCanManageStockAuditProcess(boolean canManageStockAuditProcess) {
        this.canManageStockAuditProcess = canManageStockAuditProcess;
    }

    public boolean isCanManageStockTransportOrder() {
        return canManageStockTransportOrder;
    }

    public void setCanManageStockTransportOrder(boolean canManageStockTransportOrder) {
        this.canManageStockTransportOrder = canManageStockTransportOrder;
    }

    public boolean isCanManageReceivingGoods() {
        return canManageReceivingGoods;
    }

    public void setCanManageReceivingGoods(boolean canManageReceivingGoods) {
        this.canManageReceivingGoods = canManageReceivingGoods;
    }

    public boolean isCanManageOrderFulfillment() {
        return canManageOrderFulfillment;
    }

    public void setCanManageOrderFulfillment(boolean canManageOrderFulfillment) {
        this.canManageOrderFulfillment = canManageOrderFulfillment;
    }

    public boolean isCanManageBid() {
        return canManageBid;
    }

    public void setCanManageBid(boolean canManageBid) {
        this.canManageBid = canManageBid;
    }

    public boolean isCanManagePost() {
        return canManagePost;
    }

    public void setCanManagePost(boolean canManagePost) {
        this.canManagePost = canManagePost;
    }

    //</editor-fold>
    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public List<Company> getCompanys() {
        return companys;
    }

    public void setCompanys(List<Company> companys) {
        this.companys = companys;
    }

    public Integer getSelectCompanyId() {
        return selectCompanyId;
    }

    public void setSelectCompanyId(Integer selectCompanyId) {
        this.selectCompanyId = selectCompanyId;
    }

    

    

}
