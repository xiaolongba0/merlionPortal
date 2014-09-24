/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.administrationmodule;

import entity.SystemUser;
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
public class SystemAccessRightSessionBean {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    CheckAccessRightBean carb;
    @PersistenceContext
    EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
//    OES
    public boolean canUseOES(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGeneratePO) || carb.userHasRight(loginedUser, Right.canGenerateSO)
                    || carb.userHasRight(loginedUser, Right.canGenerateQuotationAndProductContract) || carb.userHasRight(loginedUser, Right.canGenerateSalesReport)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkOESCustomer(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGeneratePO)) {
                return true;
            }
        }
        return false;

    }
    public boolean checkOESOrderProcessing(Integer userId){
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGenerateQuotationAndProductContract)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkOESSales(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGenerateSO)) {
                return true;
            }
        }
        return false;
    }

    
    
    
//    MRP
    public boolean canUseMRP(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canUseForecast) || carb.userHasRight(loginedUser, Right.canManageProductAndComponent)
                    || carb.userHasRight(loginedUser, Right.canGenerateMRPList)) {
                return true;
            }
        }
        return false;
    }

//    CRMS
    public boolean canUseCRMS(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGenerateServicePO) || carb.userHasRight(loginedUser, Right.canUpdateCustomerCredit)
                    || carb.userHasRight(loginedUser, Right.canGenerateServiceSO) || carb.userHasRight(loginedUser, Right.canGenerateQuotationRequest)
                    || carb.userHasRight(loginedUser, Right.canManageServiceCatalog) || carb.userHasRight(loginedUser, Right.canGenerateServiceQuotationAndContract)
                    || carb.userHasRight(loginedUser, Right.canManageKeyAccount)) {
                return true;
            }
        }
        return false;

    }
//  WMS
    public boolean canUseWMS(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageWarehouse) || carb.userHasRight(loginedUser, Right.canManageStockAuditProcess)
                    || carb.userHasRight(loginedUser, Right.canManageStockTransportOrder) || carb.userHasRight(loginedUser, Right.canManageReceivingGoods)
                    || carb.userHasRight(loginedUser, Right.canManageOrderFulfillment)) {
                return true;
            }
        }
        return false;
    }
//TMS
    public boolean canUseTMS(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageTransportationAsset) || carb.userHasRight(loginedUser, Right.canManageTransportationOrder)
                    || carb.userHasRight(loginedUser, Right.canManageLocation) || carb.userHasRight(loginedUser, Right.canManageAssetType)
                    || carb.userHasRight(loginedUser, Right.canUseHRFunction)) {
                return true;
            }
        }
        return false;

    }
//GRNS
    public boolean canUseGRNS(Integer userId) {
        SystemUser loginedUser = getUser(userId);
        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageBid) || carb.userHasRight(loginedUser, Right.canManagePost)) {
                return true;
            }
        }
        return false;
    }

    private SystemUser getUser(Integer userId) {
        return em.find(SystemUser.class, userId);

    }
}
