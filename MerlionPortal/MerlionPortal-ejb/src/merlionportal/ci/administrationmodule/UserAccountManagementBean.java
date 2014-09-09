/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.administrationmodule;

import entity.Company;
import entity.SystemUser;
import entity.UserRole;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class UserAccountManagementBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //Used by platform superuser
    @PersistenceContext
    EntityManager em;

    private SystemUser operator;

    //Used by super user
    //if operator is null return 0, if access denied return -1, sucess return 1
    public int createSystemAdminRole(Integer companyId) {

        UserRole superRole = new UserRole();
        superRole.setRoleName("systemadmin");

        superRole.setCanManageUser(true);
        Company company = em.find(Company.class, companyId);
        if (company != null) {

            List<Company> companies = new ArrayList();
            companies.add(company);
            superRole.setCompanyList(companies);
            superRole.setDescription("systemadmin for company " + company.getName());
            em.persist(superRole);
            em.flush();

            List<UserRole> roles = new ArrayList();
            roles.add(superRole);
            company.setUserRoleList(roles);

            em.persist(superRole);
            em.flush();
            em.merge(company);
            return 1;
        } else {
            System.out.println("Company is null");
            return 0;
        }

    }

/// Used by anyone
    public int registerNewCompany(String name, String address, String contactNumber, String contactPersonName, String emailAddress, String description, Integer package1) {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        company.setContactNumber(contactNumber);
        company.setContactPersonName(contactPersonName);
        company.setEmailAddress(emailAddress);
        company.setDescription(description);
        company.setPackage1(package1);

        em.persist(company);
        return 1;
    }

    //Used by company system Admin
    public int createCompanySystemAdminUser(String firstName, String lastName, String emailAddress, String password, String postalAddress,
            String contactNumber, String salution, String userType, Integer companyId) {
        SystemUser user = new SystemUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailAddress(emailAddress);
        user.setPassword(password);
        user.setPostalAddress(postalAddress);
        user.setContactNumber(contactNumber);
        user.setSalution(salution);
        user.setUserType(userType);

        Company company = getCompany(companyId);
        if (company != null) {
            user.setCompanycompanyId(company);
        } else {
            return 0;
        }

        List<UserRole> roles = new ArrayList<>();
        UserRole role = em.find(UserRole.class, "2");
        roles.add(role);
        user.setUserRoleList(roles);

        em.persist(user);
        em.flush();
        em.merge(company);
        em.merge(role);
        return 1;

    }

    public int createCompanyRole(Integer companyId, String roleName, String description, boolean canGeneratePO, boolean canGenerateSO, boolean canGenerateQuotationAndProductContract, boolean canGenerateSalesReport,
            boolean canManageUser, boolean canUseForecast, boolean canManageProductAndComponent, boolean canGenerateMRPList, boolean canGenerateServicePO, boolean canUpdateCustomerCredit, boolean canGenerateServiceSO,
            boolean canGenerateQuotationRequest, boolean canManageServiceCatalog, boolean canGenerateServiceQuotationAndContract, boolean canManageKeyAccount,
            boolean canManageTransportationAsset, boolean canManageTransportationOrder, boolean canManageLocation, boolean canManageAssetType, boolean canUseHRFunction,
            boolean canManageWarehouse, boolean canManageStockAuditProcess, boolean canManageStockTransportOrder, boolean canManageReceivingGoods,
            boolean canManageOrderFulfillment, boolean canManageBid, boolean canManagePost) {

        UserRole companyRole = new UserRole();
        companyRole.setRoleName(roleName);
        companyRole.setDescription(description);

        companyRole.setCanGeneratePO(canGeneratePO);
        companyRole.setCanGenerateSO(canGenerateSO);
        companyRole.setCanGenerateQuotationAndProductContract(canGenerateQuotationAndProductContract);
        companyRole.setCanGenerateSalesReport(canGenerateSalesReport);

        companyRole.setCanManageUser(canManageUser);

        companyRole.setCanUseForecast(canUseForecast);
        companyRole.setCanManageProductAndComponent(canManageProductAndComponent);
        companyRole.setCanGenerateMRPList(canGenerateMRPList);

        companyRole.setCanGenerateServicePO(canGenerateServicePO);
        companyRole.setCanUpdateCustomerCredit(canUpdateCustomerCredit);
        companyRole.setCanGenerateServiceSO(canGenerateServiceSO);
        companyRole.setCanGenerateQuotationRequest(canGenerateQuotationRequest);
        companyRole.setCanManageServiceCatalog(canManageServiceCatalog);
        companyRole.setCanGenerateServiceQuotationAndContract(canGenerateServiceQuotationAndContract);
        companyRole.setCanManageKeyAccount(canManageKeyAccount);

        companyRole.setCanManageTransportationAsset(canManageTransportationAsset);
        companyRole.setCanManageTransportationOrder(canManageTransportationOrder);
        companyRole.setCanManageLocation(canManageLocation);
        companyRole.setCanManageAssetType(canManageAssetType);
        companyRole.setCanUseHRFunction(canUseHRFunction);

        companyRole.setCanManageWarehouse(canManageWarehouse);
        companyRole.setCanManageStockAuditProcess(canManageStockAuditProcess);
        companyRole.setCanManageStockTransportOrder(canManageStockTransportOrder);
        companyRole.setCanManageReceivingGoods(canManageReceivingGoods);
        companyRole.setCanManageOrderFulfillment(canManageOrderFulfillment);

        companyRole.setCanManageBid(canManageBid);
        companyRole.setCanManagePost(canManagePost);

        List<Company> companies = new ArrayList<>();
        Company company = getCompany(companyId);
        if (company != null) {
            companies.add(company);
            companyRole.setCompanyList(companies);

            List<UserRole> roles = new ArrayList<>();
            roles.add(companyRole);

            company.setUserRoleList(roles);

            em.persist(companyRole);
            em.flush();
            em.merge(company);

            return 1;
        } else {
            return 0;
        }
    }

    private SystemUser getOperator(Integer operatorId) {
        return em.find(SystemUser.class, operatorId);
    }

    private Company getCompany(Integer companyId) {
        return em.find(Company.class, companyId);
    }

    public int createCompanySystemUser() {
        return 0;

    }

    public int assignRoleToCompanySystemUser() {
        return 0;

    }

    public int unlockUser(int systemAdminId) {
        return 0;

    }

    public int activateUser(int systemAdminId) {
        return 0;

    }

    public int changePasswordUponLogin(int systemAdminId) {
        return 0;

    }

}
