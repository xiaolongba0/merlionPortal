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
    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
//***********************************
//***             OES             ***
//***                             ***
//***********************************

//    OES System
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
// OES Rights
    public boolean checkOESGeneratePO(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGeneratePO)) {
                return true;
            }
        }
        return false;

    }
    public boolean checkOESGenerateQuotation(Integer userId){
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGenerateQuotationAndProductContract)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkOESGenerateSO(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGenerateSO)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkOESReport(Integer userId){
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGenerateSalesReport)) {
                return true;
            }
        }
        return false;
    }
    
    
//***********************************
//***             MRP             ***
//***                             ***
//***********************************    
//    MRP System
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
    
//    MRP Rights check
    public boolean checkMRPUseForecast(Integer userId){
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canUseForecast)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkMRPManageProduct(Integer userId){
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageProductAndComponent)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkMRPGenerateMRPList(Integer userId){
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGenerateMRPList)) {
                return true;
            }
        }
        return false;
    }

    
    
//***********************************
//***            CRMS             ***
//***                             ***
//***********************************
//    CRMS System
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
    
    
    
//***********************************
//***             WMS             ***
//***                             ***
//***********************************
//  WMS System
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
    
    public boolean checkWMSManageWarehouse(Integer userId){
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageWarehouse)) {
                return true;
            }
        }
        return false;
    }
    
//***********************************
//***             TMS             ***
//***                             ***
//***********************************
//TMS System
    public boolean canUseTMS(Integer userId) {
        SystemUser loginedUser = getUser(userId);

        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageTransportationAsset) || carb.userHasRight(loginedUser, Right.canManageTransportationOrder)
                    || carb.userHasRight(loginedUser, Right.canManageLog) || carb.userHasRight(loginedUser, Right.canManageAssetMaintenence)
                    || carb.userHasRight(loginedUser, Right.canUseHRFunction)) {
                return true;
            }
        }
        return false;

    }
    
    
    
//***********************************
//***             GRNS            ***
//***                             ***
//***********************************
//GRNS System
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
