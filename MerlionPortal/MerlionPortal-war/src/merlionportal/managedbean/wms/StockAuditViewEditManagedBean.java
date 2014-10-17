/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.StockAudit;
import entity.SystemUser;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import merlionportal.wms.warehousemanagementmodule.StockAuditSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "stockAuditViewEditManagedBean")
@ViewScoped
public class StockAuditViewEditManagedBean {

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
    private Integer radioValue;
    private Integer userId;

    private List<StockAudit> stockAudits;
    private Date scheduledDate;

    private List<Product> productList;
    private Integer productId;

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
        productList = psb.getMyProducts(companyId);
    }

    public void viewStockAudits() {
        System.out.println("===============================[In Managed Bean - view StockAudits]");
        System.out.println("[In Managed Bean - getStocks] Product ID : " + productId);

        if (productId != null) {
            stockAudits = sasb.viewStockAudits(productId);
            systemLogSB.recordSystemLog(userId, "WMS view stock audit");
            if (stockAudits == null) {
                System.out.println("============== FAILED TO VIEW STOCK Audits ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock Audit", ""));
            }
        }
    }

    /**
     * Creates a new instance of StockAuditViewEditManagedBean
     */
    public StockAuditViewEditManagedBean() {
    }

    public ProductSessionBean getPsb() {
        return psb;
    }

    public void setPsb(ProductSessionBean psb) {
        this.psb = psb;
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

    public Integer getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(Integer radioValue) {
        this.radioValue = radioValue;
    }

    public List<StockAudit> getStockAudits() {
        return stockAudits;
    }

    public void setStockAudits(List<StockAudit> stockAudits) {
        this.stockAudits = stockAudits;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
