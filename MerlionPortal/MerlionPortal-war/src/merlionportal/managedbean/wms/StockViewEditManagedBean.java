/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Stock;
import entity.StorageBin;
import entity.StorageType;
import entity.SystemUser;
import entity.Warehouse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

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
    private UserAccountManagementSessionBean uamb;

    private List<Warehouse> warehouses;
    private Integer warehouseId;
    private List<StorageType> storagetypes;
    private Integer storageTypeId;
    private List<StorageBin> bins;
    private Integer storageBinId;

    private List<Stock> stocks;
    private Integer productId;
    private Stock stock;
    private Integer radioValue;
    private List<String> productIdList;
    private Integer totalQuantity;

    private Integer companyId;
    private SystemUser loginedUser;

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
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

    /**
     * Creates a new instance of StockViewEditManagedBean
     */
    public StockViewEditManagedBean() {
    }

    public void countTotalQuantity() {
        List<Integer> productIdList = new ArrayList<>();
        Stock tempStock = null;

        for (Object o : stocks) {
            tempStock = (Stock) o;
            totalQuantity = totalQuantity + tempStock.getQuantity();
            System.out.println("Stock: " + tempStock + "Quantity: " + tempStock.getQuantity());
        }

        System.out.println("In countTotalQuantity, TotalQuantity ============================= : " + totalQuantity);
    }

    // TO BE EDITED AND INTEGRATED WITH MRP
    public List<String> getProductIdList() {
        productIdList = amsb.listProductId(companyId);
        System.out.println("[In WAR FILE - get PRODUCTID LIST]" + productIdList);
        return productIdList;
    }

    public void setProductIdList(List<String> productIdList) {
        this.productIdList = productIdList;
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
        System.out.println("===============================[In Managed Bean - getStocks]");
        System.out.println("[In Managed Bean - getStocks] Product ID : " + productId);
        totalQuantity = 0;
        if (productId != null) {
            stocks = amsb.viewStock(productId);
            totalQuantity = amsb.countTotalStock(productId);
            if (stocks == null) {
                System.out.println("============== FAILED TO VIEW STOCK ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock.", ""));
            }
        return stocks;
        }
        return null;
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

}
