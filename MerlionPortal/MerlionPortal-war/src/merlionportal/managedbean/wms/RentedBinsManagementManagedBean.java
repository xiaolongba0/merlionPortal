/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.Stock;
import entity.StorageBin;
import entity.SystemUser;
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
import merlionportal.wms.mobilitymanagementmodule.OrderFulfillmentSessionBean;
import merlionportal.wms.mobilitymanagementmodule.ReceivingPutawaySessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "rentedBinsManagementManagedBean")
@ViewScoped
public class RentedBinsManagementManagedBean {

    @EJB
    private AssetManagementSessionBean amsb;
    @EJB
    private ReceivingPutawaySessionBean rpsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private ProductSessionBean psb;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer userId;
    private Integer radioValue;

    private Integer productId;
    private List<Product> productList;
    private Product product;

    private List<StorageBin> bins;
    private List<Stock> stocks;
    private Integer totalQuantity;

    /**
     * Creates a new instance of RentedBinsManagementManagedBean
     */
    public RentedBinsManagementManagedBean() {
    }

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
        bins = amsb.viewAllRentedBins(companyId);
    }

    public void viewStocksInRentedBins() {
        System.out.println("===============================[In Managed Bean - viewStocksInRentedBins]");
        System.out.println("[In Managed Bean - getStocks] Product ID : " + productId);
        totalQuantity = 0;
        if (productId != null) {
            stocks = rpsb.viewAllStockInRentedBin(companyId, productId);
            totalQuantity = rpsb.countAvailbleStockInAllRentedBin(companyId, productId);
            System.out.println("TOTAL QUANTITY IN viewStocksInRentedBins = " + totalQuantity);
            systemLogSB.recordSystemLog(userId, "WMS view stocks");
            if (stocks == null) {
                System.out.println("============== FAILED TO VIEW STOCK ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock.", ""));
            }
        }
    }
    

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(Integer radioValue) {
        this.radioValue = radioValue;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<StorageBin> getBins() {
        return bins;
    }

    public void setBins(List<StorageBin> bins) {
        this.bins = bins;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

}
