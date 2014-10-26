/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.StockAudit;
import entity.SystemUser;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.StockAuditSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "stockAuditProcessManagedBean")
@SessionScoped
public class StockAuditProcessManagedBean implements Serializable{

    @EJB
    private UserAccountManagementSessionBean uamb;

    @EJB
    private StockAuditSessionBean sasb;

    @EJB
    private ProductSessionBean psb;

    @EJB
    private SystemLogSessionBean systemLogSB;

    private Integer companyId;
    private SystemUser loginedUser;
    private Integer userId;

    private StockAudit audit;
    private Integer passQuantity;
    private Integer failQuantity;
    private String remarks;

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
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

    public void getStockAuditInfo(Integer stockAuditId) {
        System.out.println("[In Managed Bean - getStockAuditInfo] ===============================: " + stockAuditId);
        audit = sasb.getStockAudit(stockAuditId);
        System.out.println("[In Managed Bean - getStockAuditInfo] ===============================: " + audit.getCreatedDate());
    }

        public void carryOutStockAudit() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK AUDIT MB] ====================== Carry Out Stock Audit: " + audit.getStockAuditId());
        // stock audit completed
        audit.setStockAuditStatus(1);
        audit.setActualQuantity(audit.getPassQuantity() + audit.getFailQuantity());
        boolean result = sasb.carryOutStockAudit(audit.getStockAuditId(),audit.getStockAuditStatus(), audit.getPassQuantity(), audit.getFailQuantity(), 
                audit.getActualQuantity(), audit.getRemarks());
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! Stock Audit Completed!", ""));
            systemLogSB.recordSystemLog(userId, "WMS carry out stock audit");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ""));
        }
    }
    /**
     * Creates a new instance of StockAuditProcessManagedBean
     */
    public StockAuditProcessManagedBean() {
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public StockAudit getAudit() {
        return audit;
    }

    public void setAudit(StockAudit audit) {
        this.audit = audit;
    }

}
