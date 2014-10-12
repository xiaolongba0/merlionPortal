/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.StorageBin;
import entity.StorageType;
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
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author YunWei
 */
@Named(value = "createStockManagedBean")
@ViewScoped
public class CreateStockManagedBean {

    /**
     * Creates a new instance of CreateStockManagedBean
     */
    @EJB
    private AssetManagementSessionBean amsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private ProductSessionBean psb;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer radioValue;

    private Integer warehouseId;
    private List<Warehouse> warehouses;

    private Integer storageTypeId;
    private List<StorageType> storageTypes;

    private Integer storageBinId;
    private List<String> listStorageBinType;
    private List<StorageBin> storageBins;

    private String stockName;
    private String comments;
    private Integer quantity;
    private Date expiryDate;
    
    private Integer productId;  
    private List<Product> productList;
    private Product product;

    public CreateStockManagedBean() {
    }

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
        warehouses = amsb.viewMyWarehouses(companyId);
        productList = psb.getMyProducts(companyId);
    }

    public void addStock() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK MB] ====================== add stock, Storage Bin ID: " + storageBinId);

        boolean result = amsb.addStock(stockName, comments, quantity, productId, storageBinId, expiryDate);
        if (result) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! Stock Added!", "New Stock Added!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Please check if Bin has reached its max quantity", "Something went wrong."));
        }
    }

    public void onChangeWarehouse() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK MB] ====================== onChangeWarehouse, Warehouse ID: " + warehouseId);
        if (warehouseId != null) {
            storageTypes = amsb.viewStorageTypesForAWarehouse(warehouseId);
        }
    }

    public void onChangeStorageType() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK MB] ====================== onChangeStorageType");
        if (warehouseId != null & storageTypeId != null) {
            storageBins = amsb.viewStorageBinForStorageType(storageTypeId);
        }
    }

    public void displayProducts() {

        System.out.println("[IN MANAGED BEAN -- Create STOCK MB]  ====================== displayproducts");
        if (companyId != null) {
            productList = psb.getMyProducts(companyId);
        }
    }

    private void clearAllFields() {
        warehouseId = null;
        storageTypeId = null;
        storageBinId = null;
        stockName = null;
        comments = null;
        quantity = null;
        productId = null;
        expiryDate = null;
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

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        System.out.println("[IN MANAGED BEAN -- Create STOCK MB] ======= Checking warehouse ID SET: " + warehouseId);
        this.warehouseId = warehouseId;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Integer getStorageTypeId() {
        return storageTypeId;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public List<StorageType> getStorageTypes() {
        return storageTypes;
    }

    public void setStorageTypes(List<StorageType> storageTypes) {
        this.storageTypes = storageTypes;
    }

    public Integer getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Integer storageBinId) {
        this.storageBinId = storageBinId;
    }

    public List<String> getListStorageBinType() {
        return listStorageBinType;
    }

    public void setListStorageBinType(List<String> listStorageBinType) {
        this.listStorageBinType = listStorageBinType;
    }

    public List<StorageBin> getStorageBins() {
        return storageBins;
    }

    public void setStorageBins(List<StorageBin> storageBins) {
        this.storageBins = storageBins;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Integer getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(Integer radioValue) {
        this.radioValue = radioValue;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}
