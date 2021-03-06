/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.Stock;
import entity.SystemUser;
import entity.Warehouse;
import java.io.IOException;
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
import merlionportal.wms.mobilitymanagementmodule.ReceivingPutawaySessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author YunWei
 */
@Named(value = "stockViewEditManagedBean")
@ViewScoped
public class StockViewEditManagedBean {

    @EJB
    private AssetManagementSessionBean amsb;
    @EJB
    private ReceivingPutawaySessionBean rpsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private ProductSessionBean psb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private List<Stock> stocks;
    private Integer productId;
    private Integer productId2;
    private Integer productId3;
    private Stock stock;
    private Integer radioValue;
    private Integer radioValue2;
    private Integer totalQuantity;

    private List<Product> productList;

    private Integer warehouseId;
    private List<Warehouse> warehouses;

    private Integer companyId;
    private SystemUser loginedUser;
    private Integer userId;

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
        warehouses = amsb.viewMyWarehouses(companyId);
    }

    /**
     * Creates a new instance of StockViewEditManagedBean
     */
    public StockViewEditManagedBean() {
    }

    public void viewStocksForAWarehouse() {
        System.out.println("===============================[In Managed Bean -  viewStocksForAWarehouse]");
        totalQuantity = 0;
        if (productId != null) {
            stocks = rpsb.getWarehouseStock(warehouseId, productId);
            totalQuantity = rpsb.countAvailbleStockInWarehouse(warehouseId, productId);
            System.out.println("TOTAL QUANTITY IN VIEW ALL STOCKS = " + totalQuantity);
//            systemLogSB.recordSystemLog(userId, "WMS view stocks for a warehouse");
            if (stocks == null) {
                System.out.println("============== FAILED TO VIEW STOCK ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock.", ""));
            }
        }
    }
    
        public void viewAllStocks() {
        System.out.println("===============================[In Managed Bean - view Stocks]");
        System.out.println("[In Managed Bean - getStocks] Product ID : " + productId2);
        totalQuantity = 0;
        if (productId2 != null) {
            stocks = rpsb.viewAllStocks(companyId, productId2);
            totalQuantity = rpsb.countAvailbleStocksInCompany(companyId, productId2);
            System.out.println("TOTAL QUANTITY IN VIEW ALL STOCKS = " + totalQuantity);
//            systemLogSB.recordSystemLog(userId, "WMS view stocks");
            if (stocks == null) {
                System.out.println("============== FAILED TO VIEW STOCK ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock.", ""));
            }
        }
    }

    public void viewClientStockInRentedBin() {
        System.out.println("===============================[In Managed Bean -   viewClientStockInRentedBin");
        totalQuantity = 0;
        if (productId3 != null) {
            stocks = rpsb.viewClientStockInRentedBin(companyId, productId3);
//            systemLogSB.recordSystemLog(userId, "WMS view client stock in my warehouses");
            if (stocks == null) {
                System.out.println("============== FAILED TO VIEW STOCK ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock.", ""));
            }
        }
    }

    public void deleteStock(Stock stock) {
        try {
            boolean result = rpsb.deleteStock(stock.getStockId());
            if (result) {
                stocks.remove(stock);
                systemLogSB.recordSystemLog(userId, "WMS delete stock");
                FacesMessage msg = new FacesMessage("Stock with ID = " + stock.getStockId() + " has sucessfully been deleted");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                totalQuantity = totalQuantity - stock.getQuantity();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ""));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        stock = new Stock();
        stock = (Stock) event.getObject();
        System.out.println("[In Managed Bean - STOCK ON ROW EDIT]===============================: " + stock.getQuantity());
        boolean result = rpsb.editStock(stock.getName(), stock.getComments(), stock.getStockId(), stock.getExpiryDate(), stock.getCreatedDate());
        if (result) {
            systemLogSB.recordSystemLog(userId, "WMS edit stock");
            FacesMessage msg = new FacesMessage("Stock with ID = " + stock.getStockId() + " has sucessfully been edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Edit Cancelled"));
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public Integer getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(Integer radioValue) {
        this.radioValue = radioValue;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Integer getProductId2() {
        return productId2;
    }

    public void setProductId2(Integer productId2) {
        this.productId2 = productId2;
    }

    public Integer getProductId3() {
        return productId3;
    }

    public void setProductId3(Integer productId3) {
        this.productId3 = productId3;
    }

    public Integer getRadioValue2() {
        return radioValue2;
    }

    public void setRadioValue2(Integer radioValue2) {
        this.radioValue2 = radioValue2;
    }

}
