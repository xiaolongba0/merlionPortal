/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import entity.SystemUser;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.CheckAccessRightBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Named(value = "userDashBoardBean")
@ViewScoped
public class UserDashBoardManagedBean {

    /**
     * Creates a new instance of UserDashBoardBean
     */
    private SystemUser loginedUser;

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    CheckAccessRightBean carb;

    public UserDashBoardManagedBean() {
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

    }

    public boolean canUseOES() {
        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canGeneratePO) || carb.userHasRight(loginedUser, Right.canGenerateSO)
                    || carb.userHasRight(loginedUser, Right.canGenerateQuotationAndProductContract) || carb.userHasRight(loginedUser, Right.canGenerateSalesReport)) {
                return true;
            }
        }
        return false;
    }

    public boolean canUseMRP() {
        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canUseForecast) || carb.userHasRight(loginedUser, Right.canManageProductAndComponent)
                    || carb.userHasRight(loginedUser, Right.canGenerateMRPList)) {
                return true;
            }
        }
        return false;
    }

    public boolean canUseCRMS() {
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

    public boolean canUseWMS() {
        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageWarehouse) || carb.userHasRight(loginedUser, Right.canManageStockAuditProcess)
                    || carb.userHasRight(loginedUser, Right.canManageStockTransportOrder) || carb.userHasRight(loginedUser, Right.canManageReceivingGoods)
                    || carb.userHasRight(loginedUser, Right.canManageOrderFulfillment)) {
                return true;
            }
        }
        return false;
    }

    public boolean canUseTMS() {
        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageTransportationAsset) || carb.userHasRight(loginedUser, Right.canManageTransportationOrder)
                    || carb.userHasRight(loginedUser, Right.canManageLog) || carb.userHasRight(loginedUser, Right.canManageAssetMaintenence)
                    || carb.userHasRight(loginedUser, Right.canUseHRFunction)) {
                return true;
            }
        }
        return false;

    }

    public boolean canUseGRNS() {
        if (loginedUser != null) {
            if (carb.userHasRight(loginedUser, Right.canManageBid) || carb.userHasRight(loginedUser, Right.canManagePost)) {
                return true;
            }
        }
        return false;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

}
