/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.Stock;
import entity.StockAudit;
import entity.SystemUser;
import entity.Warehouse;
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
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.StockAuditSessionBean;
import org.primefaces.event.RowEditEvent;

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
    private SystemLogSessionBean systemLogSB;

    @EJB
    private AssetManagementSessionBean amsb;

    private Integer companyId;
    private SystemUser loginedUser;
    private Integer radioValue;
    private Integer userId;

    private List<StockAudit> stockAudits;
    private String auditStatus;
    private Date scheduledDate;
    private StockAudit audit;

    private Integer auditStockAuditId;
    private Integer auditStorageBinId;
    private Integer auditStaffId;
    private Date auditScheduledDate;
    private Integer auditExpectedQuantity;

    private List<Product> productList;
    private List<Warehouse> warehouses;
    private Integer warehouseId;
    private Integer storageBinId;

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
        warehouses = amsb.viewMyWarehouses(companyId);
    }

    public void getStockAuditInfo(Integer stockAuditId) {
        System.out.println("[In Managed Bean - getStockAuditInfo] ===============================: " + stockAuditId);
        audit = sasb.getStockAudit(stockAuditId);
        auditStockAuditId = audit.getStockAuditId();
        auditStorageBinId = audit.getStorageBinId();
        auditStaffId = audit.getStaffId();
        auditScheduledDate = audit.getCreatedDate();
        auditExpectedQuantity = audit.getExpectedQuantity();
            
}
    public void viewAllStockAudits() {
        System.out.println("[In Managed Bean - viewAllStockAudits] =============================== Warehouse ID : " + warehouseId);

        if (warehouseId != null) {
            stockAudits = sasb.viewStockAuditsForAWarehouse(warehouseId);
            systemLogSB.recordSystemLog(userId, "WMS view all stock audit");
            if (warehouseId == null) {
                System.out.println("============== FAILED TO VIEW STOCK Audits ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock Audit", ""));
            }
        }
    }
    
        public void viewDueStockAudits() {
        System.out.println("[In Managed Bean - viewDueStockAudits] =============================== Warehouse ID : " + warehouseId);

        if (warehouseId != null) {
            stockAudits = sasb.viewDueStockAuditsForAWarehouse(warehouseId);
            systemLogSB.recordSystemLog(userId, "WMS view due stock audit");
            if (warehouseId == null) {
                System.out.println("============== FAILED TO VIEW STOCK Audits ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock Audit", ""));
            }
        }
    }

    public String getAuditStatus(int stockAuditStatus) {
        if (stockAuditStatus == 0) {
            auditStatus = "Not Done";
        }
        if (stockAuditStatus == 1) {
            auditStatus = "Completed";
        }
        if (stockAuditStatus == 2) {
            auditStatus = "On Hold";
        }

        return auditStatus;
    }


    public void deleteStockAudit(StockAudit stockAudit) {
        try {
            System.out.println("In StockAuditViewEditManagedBean, delete stock ======================");
            boolean result = sasb.deleteStockAudit(stockAudit.getStockAuditId());
            if (result) {
                stockAudits.remove(stockAudit);
                systemLogSB.recordSystemLog(userId, "WMS delete stock audit");
                FacesMessage msg = new FacesMessage("Stock Audit with ID = " + stockAudit.getStockAuditId() + " has sucessfully been deleted");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ""));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        StockAudit stockAudit = new StockAudit();
        stockAudit = (StockAudit) event.getObject();
        System.out.println("[In Managed Bean - STOCK AUDIT ON ROW EDIT]=============================== ");
        boolean result = sasb.editStockAudit(stockAudit.getStockAuditId(),stockAudit.getStaffId(), stockAudit.getCreatedDate(),
                stockAudit.getStockAuditStatus(), stockAudit.getRemarks());
        if (result) {
            systemLogSB.recordSystemLog(userId, "WMS edit stock audit");
            FacesMessage msg = new FacesMessage("Stock Audit with ID = " + stockAudit.getStockAuditId() + " has sucessfully been edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Edit Cancelled"));
    }

    /**
     * Creates a new instance of StockAuditViewEditManagedBean
     */
    public StockAuditViewEditManagedBean() {
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

    public Integer getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Integer storageBinId) {
        this.storageBinId = storageBinId;
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

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public StockAudit getAudit() {
        return audit;
    }

    public void setAudit(StockAudit audit) {
        this.audit = audit;
    }

    public Integer getAuditStockAuditId() {
        return auditStockAuditId;
    }

    public void setAuditStockAuditId(Integer auditStockAuditId) {
        this.auditStockAuditId = auditStockAuditId;
    }

    public Integer getAuditStorageBinId() {
        return auditStorageBinId;
    }

    public void setAuditStorageBinId(Integer auditStorageBinId) {
        this.auditStorageBinId = auditStorageBinId;
    }

    public Integer getAuditStaffId() {
        return auditStaffId;
    }

    public void setAuditStaffId(Integer auditStaffId) {
        this.auditStaffId = auditStaffId;
    }

    public Date getAuditScheduledDate() {
        return auditScheduledDate;
    }

    public void setAuditScheduledDate(Date auditScheduledDate) {
        this.auditScheduledDate = auditScheduledDate;
    }

    public Integer getAuditExpectedQuantity() {
        return auditExpectedQuantity;
    }

    public void setAuditExpectedQuantity(Integer auditExpectedQuantity) {
        this.auditExpectedQuantity = auditExpectedQuantity;
    }

}
