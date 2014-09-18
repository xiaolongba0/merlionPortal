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
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class RoleManagementSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    @EJB
    private CheckAccessRightBean carb;

    public boolean isSuperUser(int userid) {
        SystemUser user = em.find(SystemUser.class, userid);
        if (user != null) {
            if (user.getUserType() != null) {
                if (user.getUserType().equals("superuser")) {
                    return true;
                }
            }
        }

        return false;
    }

    public int createCompanyRole(Integer operatorId, Integer companyId, String roleName, String description, boolean canGeneratePO, boolean canGenerateSO, boolean canGenerateQuotationAndProductContract, boolean canGenerateSalesReport,
            boolean canManageUser, boolean canUseForecast, boolean canManageProductAndComponent, boolean canGenerateMRPList, boolean canGenerateServicePO, boolean canUpdateCustomerCredit, boolean canGenerateServiceSO,
            boolean canGenerateQuotationRequest, boolean canManageServiceCatalog, boolean canGenerateServiceQuotationAndContract, boolean canManageKeyAccount,
            boolean canManageTransportationAsset, boolean canManageTransportationOrder, boolean canManageLocation, boolean canManageAssetType, boolean canUseHRFunction,
            boolean canManageWarehouse, boolean canManageStockAuditProcess, boolean canManageStockTransportOrder, boolean canManageReceivingGoods,
            boolean canManageOrderFulfillment, boolean canManageBid, boolean canManagePost) {

        SystemUser operator = em.find(SystemUser.class, operatorId);
        boolean canRun = false;

        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }
            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }

            if (canRun) {
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
                //Add a list of user but empty
                List<SystemUser> userList = new ArrayList<>();

                //Add a list of company
                companyRole.setSystemUserList(userList);

                List<Company> companies = new ArrayList<>();
                Company company = getCompany(companyId);
                if (company != null) {
                    companies.add(company);
                    companyRole.setCompanyList(companies);

                    List<UserRole> roles = new ArrayList<>();
                    roles.add(companyRole);

                    company.getUserRoleList().add(companyRole);

                    em.persist(companyRole);
                    em.flush();
                    em.merge(company);

                    return 1;
                } else {
                    return 0;

                }
            } else {
                //Access Denied
                return -1;
            }

        } else {
            return 0;
        }

    }
//Return -2 if cannot delete

    public int deleteCompanyRole(int operatorId, int roleId) {
        SystemUser operator = em.find(SystemUser.class, operatorId);
        boolean canRun = false;

        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }
            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }
            if (canRun) {
                UserRole userRole = em.find(UserRole.class, roleId);
                if (userRole != null) {
                    if (userRole.getSystemUserList() != null) {
                        System.out.println("There are users in this role");
                        return -2;
                    } else {
                        for (Object o : userRole.getCompanyList()) {
                            Company company = (Company) o;
                            if (company.getUserRoleList().contains(userRole)) {
                                company.getUserRoleList().remove(userRole);
                                em.merge(company);
                            }
                        }
                        em.remove(userRole);
                        em.flush();
                        return 1;
                    }
                }
            }
            System.out.println("Access Denied");

            return -1;
        } else {
            System.out.println("Operator is null");
            return 0;
        }
    }

    public int updateRole(int operatorId, UserRole changedRole) {
        SystemUser operator = em.find(SystemUser.class, operatorId);
        boolean canRun = false;

        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }
            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }
            if (canRun) {
                int roleId = changedRole.getUserRoleId();

                UserRole userRole = em.find(UserRole.class, roleId);
                userRole.setRoleName(changedRole.getRoleName());

                userRole.setRoleName(changedRole.getRoleName());
                userRole.setDescription(changedRole.getDescription());

                userRole.setCanGeneratePO(changedRole.getCanGeneratePO());
                userRole.setCanGenerateSO(changedRole.getCanGenerateSO());
                userRole.setCanGenerateQuotationAndProductContract(changedRole.getCanGenerateQuotationAndProductContract());
                userRole.setCanGenerateSalesReport(changedRole.getCanGenerateSalesReport());

                userRole.setCanManageUser(changedRole.getCanManageUser());

                userRole.setCanUseForecast(changedRole.getCanUseForecast());
                userRole.setCanManageProductAndComponent(changedRole.getCanManageProductAndComponent());
                userRole.setCanGenerateMRPList(changedRole.getCanGenerateMRPList());

                userRole.setCanGenerateServicePO(changedRole.getCanGenerateServicePO());
                userRole.setCanUpdateCustomerCredit(changedRole.getCanUpdateCustomerCredit());
                userRole.setCanGenerateServiceSO(changedRole.getCanGenerateServiceSO());
                userRole.setCanGenerateQuotationRequest(changedRole.getCanGenerateQuotationRequest());
                userRole.setCanManageServiceCatalog(changedRole.getCanManageServiceCatalog());
                userRole.setCanGenerateServiceQuotationAndContract(changedRole.getCanGenerateServiceQuotationAndContract());
                userRole.setCanManageKeyAccount(changedRole.getCanManageKeyAccount());

                userRole.setCanManageTransportationAsset(changedRole.getCanManageTransportationAsset());
                userRole.setCanManageTransportationOrder(changedRole.getCanManageTransportationOrder());
                userRole.setCanManageLocation(changedRole.getCanManageLocation());
                userRole.setCanManageAssetType(changedRole.getCanManageAssetType());
                userRole.setCanUseHRFunction(changedRole.getCanUseHRFunction());

                userRole.setCanManageWarehouse(changedRole.getCanManageWarehouse());
                userRole.setCanManageStockAuditProcess(changedRole.getCanManageStockAuditProcess());
                userRole.setCanManageStockTransportOrder(changedRole.getCanManageStockTransportOrder());
                userRole.setCanManageReceivingGoods(changedRole.getCanManageReceivingGoods());
                userRole.setCanManageOrderFulfillment(changedRole.getCanManageOrderFulfillment());

                userRole.setCanManageBid(changedRole.getCanManageBid());
                userRole.setCanManagePost(changedRole.getCanManagePost());

                em.merge(userRole);
                return 1;
            }
            System.out.println("Access Denied");
            return -1;

        }
        System.out.println("Operator is null");
        return 0;
    }

    private SystemUser getOperator(Integer operatorId) {
        return em.find(SystemUser.class, operatorId);
    }

    private Company getCompany(Integer companyId) {
        return em.find(Company.class, companyId);
    }
}
