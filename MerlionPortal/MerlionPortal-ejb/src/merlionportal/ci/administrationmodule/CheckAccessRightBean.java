package merlionportal.ci.administrationmodule;

import entity.SystemUser;
import entity.UserRole;
import java.io.Serializable;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class CheckAccessRightBean implements Serializable {

    
    public boolean userHasRight(SystemUser user, int right) {
        List<UserRole> roles = user.getUserRoleList();
        if (roles != null) {
            for (UserRole role : roles) {
                if (roleHasRight(role, right)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean roleHasRight(UserRole role, int right) {

        boolean hasRight = false;

        switch (right) {
            case Right.canGeneratePO:
                if (role.getCanGeneratePO()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateSO:
                if (role.getCanGenerateSO()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateQuotationAndProductContract:
                if (role.getCanGenerateQuotationAndProductContract()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateSalesReport:
                if (role.getCanGenerateSalesReport()) {
                    hasRight = true;
                }
                break;
            case Right.canManageUser:
                if (role.getCanManageUser()) {
                    hasRight = true;
                }
                break;
            case Right.canUseForecast:
                if (role.getCanUseForecast()) {
                    hasRight = true;
                }
                break;
            case Right.canManageProductAndComponent:
                if (role.getCanManageProductAndComponent()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateMRPList:
                if (role.getCanGenerateMRPList()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateServicePO:
                if (role.getCanGenerateServicePO()) {
                    hasRight = true;
                }
                break;
            case Right.canUpdateCustomerCredit:
                if (role.getCanUpdateCustomerCredit()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateServiceSO:
                if (role.getCanGenerateServiceSO()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateQuotationRequest:
                if (role.getCanGenerateQuotationRequest()) {
                    hasRight = true;
                }
                break;

            case Right.canManageServiceCatalog:
                if (role.getCanManageServiceCatalog()) {
                    hasRight = true;
                }
                break;
            case Right.canGenerateServiceQuotationAndContract:
                if (role.getCanGenerateServiceQuotationAndContract()) {
                    hasRight = true;
                }
                break;
            case Right.canManageKeyAccount:
                if (role.getCanManageKeyAccount()) {
                    hasRight = true;
                }
                break;
            case Right.canManageTransportationAsset:
                if (role.getCanManageTransportationAsset()) {
                    hasRight = true;
                }
                break;
            case Right.canManageTransportationOrder:
                if (role.getCanManageTransportationOrder()) {
                    hasRight = true;
                }
                break;
            case Right.canManageLog:
                if (role.getCanManageLog() ) {
                    hasRight = true;
                }
                break;
            case Right.canManageAssetMaintenence:
                if (role.getCanManageAssetMaintenence()) {
                    hasRight = true;
                }
                break;
            case Right.canUseHRFunction:
                if (role.getCanUseHRFunction()) {
                    hasRight = true;
                }
                break;
            case Right.canManageWarehouse:
                if (role.getCanManageWarehouse()) {
                    hasRight = true;
                }
                break;
            case Right.canManageStockAuditProcess:
                if (role.getCanManageStockAuditProcess()) {
                    hasRight = true;
                }
                break;
            case Right.canManageStockTransportOrder:
                if (role.getCanManageStockTransportOrder()) {
                    hasRight = true;
                }
                break;
            case Right.canManageReceivingGoods:
                if (role.getCanManageReceivingGoods()) {
                    hasRight = true;
                }
                break;
            case Right.canManageOrderFulfillment:
                if (role.getCanManageOrderFulfillment()) {
                    hasRight = true;
                }
                break;
            case Right.canManageBid:
                if (role.getCanManageBid()) {
                    hasRight = true;
                }
                break;
            case Right.canManagePost:
                if (role.getCanManagePost()) {
                    hasRight = true;
                }
                break;

            default:
                hasRight = false;
                break;
        }
        return hasRight;
    }

    public void roleToggleRight(UserRole role, int right, boolean approve) {
        switch (right) {
            case Right.canGeneratePO:
                role.setCanGeneratePO(approve);
                break;
            case Right.canGenerateSO:
                role.setCanGenerateSO(approve);
                break;
            case Right.canGenerateQuotationAndProductContract:
                role.setCanGenerateQuotationAndProductContract(approve);
                break;
            case Right.canGenerateSalesReport:
                role.setCanGenerateSalesReport(approve);
                break;
            case Right.canManageUser:
                role.setCanManageUser(approve);
                break;
            case Right.canUseForecast:
                role.setCanUseForecast(approve);
                break;
            case Right.canManageProductAndComponent:
                role.setCanManageProductAndComponent(approve);
                break;
            case Right.canGenerateMRPList:
                role.setCanGenerateMRPList(approve);
                break;
            case Right.canGenerateServicePO:
                role.setCanGenerateServicePO(approve);
                break;
            case Right.canUpdateCustomerCredit:
                role.setCanUpdateCustomerCredit(approve);
                break;
            case Right.canGenerateServiceSO:
                role.setCanGenerateServiceSO(approve);
                break;
            case Right.canGenerateQuotationRequest:
                role.setCanGenerateQuotationRequest(approve);
                break;

            case Right.canManageServiceCatalog:
                role.setCanManageServiceCatalog(approve);
                break;
            case Right.canGenerateServiceQuotationAndContract:
                role.setCanGenerateServiceQuotationAndContract(approve);
                break;
            case Right.canManageKeyAccount:
                role.setCanManageKeyAccount(approve);
                break;
            case Right.canManageTransportationAsset:
                role.setCanManageTransportationAsset(approve);
                break;
            case Right.canManageTransportationOrder:
                role.setCanManageTransportationOrder(approve);
                break;
            case Right.canManageLog:
                role.setCanManageLog(approve);
                break;
            case Right.canManageAssetMaintenence:
                role.setCanManageAssetMaintenence(approve);
                break;
            case Right.canUseHRFunction:
                role.setCanUseHRFunction(approve);
                break;
            case Right.canManageWarehouse:
                role.setCanManageWarehouse(approve);
                break;
            case Right.canManageStockAuditProcess:
                role.setCanManageStockAuditProcess(approve);
                break;
            case Right.canManageStockTransportOrder:
                role.setCanManageStockTransportOrder(approve);
                break;
            case Right.canManageReceivingGoods:
                role.setCanManageReceivingGoods(approve);
                break;
            case Right.canManageOrderFulfillment:
                role.setCanManageOrderFulfillment(approve);
                break;
            case Right.canManageBid:
                role.setCanManageBid(approve);
                break;
            case Right.canManagePost:
                role.setCanManagePost(approve);
                break;
        
        }
    }
}
