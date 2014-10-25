/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
import entity.StorageBin;
import entity.SystemUser;
import entity.Warehouse;
import entity.WarehouseZone;
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
import merlionportal.wms.mobilitymanagementmodule.ReceivingPutawaySessionBean;

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
    private ReceivingPutawaySessionBean rpsb;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private ProductSessionBean psb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer userId;
    private Integer radioValue;

    private Integer warehouseId;
    private List<Warehouse> warehouses;

    private Integer warehouseZoneId;
    private List<WarehouseZone> warehouseZones;

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
        productList = psb.getMyProducts(companyId);
    }

    public void addStock() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK MB] ====================== add stock, Storage Bin ID: " + storageBinId);

        boolean result = rpsb.addStock(stockName, comments, quantity, productId, storageBinId, expiryDate);
        if (result) {
            clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success! Stock Added!", "New Stock Added!"));
            systemLogSB.recordSystemLog(userId, "WMS add stock");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Please check if Bin has reached its max quantity", "Something went wrong."));
        }
    }

    public void onChangeWarehouse() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK MB] ====================== onChangeWarehouse, Warehouse ID: " + warehouseId);
        if (warehouseId != null) {
            warehouseZones = amsb.viewWarehouseZoneForAWarehouse(warehouseId);
        }
    }

    public void onChangeWarehouseZone() {
        System.out.println("[IN MANAGED BEAN -- Create STOCK MB] ====================== onChangeWarehouseZone");
        if (warehouseId != null & warehouseZoneId != null) {
            storageBins = amsb.viewStorageBinForWarehouseZone(warehouseZoneId);
        }
    }

//    public void displayProducts() {
//
//        System.out.println("[IN MANAGED BEAN -- Create STOCK MB]  ====================== displayproducts");
//        if (companyId != null) {
//            productList = psb.getMyProducts(companyId);
//        }
//    }

    private void clearAllFields() {
        warehouseId = null;
        warehouseZoneId = null;
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

    public Integer getWarehouseZoneId() {
        return warehouseZoneId;
    }

    public void setWarehouseZoneId(Integer warehouseZoneId) {
        this.warehouseZoneId = warehouseZoneId;
    }

    public List<WarehouseZone> getWarehouseZones() {
        return warehouseZones;
    }

    public void setWarehouseZones(List<WarehouseZone> warehouseZones) {
        this.warehouseZones = warehouseZones;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
