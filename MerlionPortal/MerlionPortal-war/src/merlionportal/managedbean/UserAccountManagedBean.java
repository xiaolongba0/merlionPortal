/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean;

import entity.SystemUser;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import merlionportal.ci.administrationmodule.CheckAccessRightBean;
import merlionportal.ci.administrationmodule.UserAccountManagementBean;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Named(value = "userAccountManagedBean")
@ViewScoped
public class UserAccountManagedBean {

    /**
     * Creates a new instance of UserAccountManagedBean
     */
    @EJB
    private UserAccountManagementBean uamb;

    @EJB
    private CheckAccessRightBean carb;

    private FacesContext ctx;

    @PersistenceContext
    EntityManager em;

    private Integer userId;
    private Integer companyId;
    private String companyName;
    private String companyAddress;
    private String contactNumber;
    private String contactPersonName;
    private String emailAddress;
    private String description;
    private Integer package1;

    private String firstNameNewUser;
    private String lastNameNewUser;
    private String emailAddressNewUser;
    private String passwordNewUser;
    private String postalAddressNewUser;
    private String contactNumberNewUser;
    private String salutionNewUser;
    private String userTypeNewUser;
    private Integer companyIdNewUser;

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

    public UserAccountManagedBean() {
    }

    public void createSystemAdmin(ActionEvent event) {

        Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        SystemUser operator = em.find(SystemUser.class, operatorId);
        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {
                int result = uamb.createCompanySystemAdminUser(firstNameNewUser, lastNameNewUser, emailAddressNewUser, passwordNewUser, postalAddressNewUser, contactNumberNewUser, salutionNewUser, userTypeNewUser, companyIdNewUser);
            } else {
                System.out.println("Access Denied");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action!"));
            }

        }

    }

    public void createSystemAdminRole(ActionEvent event) {
        Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        SystemUser operator = em.find(SystemUser.class, operatorId);

        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {
                int result = uamb.createSystemAdminRole(companyId);
            } else {
                System.out.println("Access Denied");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Access Denied."));
            }

        }
    }

    public void registerNewCompany(ActionEvent event) {

        int result = uamb.registerNewCompany(companyName, emailAddress, contactNumber, contactPersonName, emailAddress, description, package1);

    }

    public void createRole(ActionEvent event) {
        Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        SystemUser operator = em.find(SystemUser.class, operatorId);

        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {
                Integer company = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
                int result = uamb.createCompanyRole(company, roleName, description, canGeneratePO, canGenerateSO, canGenerateQuotationAndProductContract, canGenerateSalesReport, canManageUser, canUseForecast, canManageProductAndComponent, canGenerateMRPList, canGenerateServicePO, canUpdateCustomerCredit, canGenerateServiceSO, canGenerateQuotationRequest, canManageServiceCatalog, canGenerateServiceQuotationAndContract, canManageKeyAccount, canManageTransportationAsset, canManageTransportationOrder, canManageLocation, canManageAssetType, canUseHRFunction, canManageWarehouse, canManageStockAuditProcess, canManageStockTransportOrder, canManageReceivingGoods, canManageOrderFulfillment, canManageBid, canManagePost);
            }
                
        }
        else{
            System.out.println("Access Denied");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Access Denied."));
        }    
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPackage1() {
        return package1;
    }

    public void setPackage1(Integer package1) {
        this.package1 = package1;
    }

    public String getFirstNameNewUser() {
        return firstNameNewUser;
    }

    public void setFirstNameNewUser(String firstNameNewUser) {
        this.firstNameNewUser = firstNameNewUser;
    }

    public String getLastNameNewUser() {
        return lastNameNewUser;
    }

    public void setLastNameNewUser(String lastNameNewUser) {
        this.lastNameNewUser = lastNameNewUser;
    }

    public String getEmailAddressNewUser() {
        return emailAddressNewUser;
    }

    public void setEmailAddressNewUser(String emailAddressNewUser) {
        this.emailAddressNewUser = emailAddressNewUser;
    }

    public String getPasswordNewUser() {
        return passwordNewUser;
    }

    public void setPasswordNewUser(String passwordNewUser) {
        this.passwordNewUser = passwordNewUser;
    }

    public String getPostalAddressNewUser() {
        return postalAddressNewUser;
    }

    public void setPostalAddressNewUser(String postalAddressNewUser) {
        this.postalAddressNewUser = postalAddressNewUser;
    }

    public String getContactNumberNewUser() {
        return contactNumberNewUser;
    }

    public void setContactNumberNewUser(String contactNumberNewUser) {
        this.contactNumberNewUser = contactNumberNewUser;
    }

    public String getSalutionNewUser() {
        return salutionNewUser;
    }

    public void setSalutionNewUser(String salutionNewUser) {
        this.salutionNewUser = salutionNewUser;
    }

    public String getUserTypeNewUser() {
        return userTypeNewUser;
    }

    public void setUserTypeNewUser(String userTypeNewUser) {
        this.userTypeNewUser = userTypeNewUser;
    }

    public Integer getCompanyIdNewUser() {
        return companyIdNewUser;
    }

    public void setCompanyIdNewUser(Integer companyIdNewUser) {
        this.companyIdNewUser = companyIdNewUser;
    }

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

}
