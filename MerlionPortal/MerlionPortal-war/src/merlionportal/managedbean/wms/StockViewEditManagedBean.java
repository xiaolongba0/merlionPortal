/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.Product;
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
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
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
    private UserAccountManagementSessionBean uamb;
    @EJB
    private ProductSessionBean psb;

    private List<Stock> stocks;
    private Integer productId;
    private Stock stock;
    private Integer radioValue;
    private Integer totalQuantity;
    
    private List<Product> productList;
    private Product product;
    
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
        productList = psb.getMyProducts(companyId);
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

    public void viewStocks() {
        System.out.println("===============================[In Managed Bean - view Stocks]");
        System.out.println("[In Managed Bean - getStocks] Product ID : " + productId);
        totalQuantity = 0;
        if (productId != null) {
            stocks = amsb.viewStock(productId);
            totalQuantity = amsb.countTotalStock(productId);
            if (stocks == null) {
                System.out.println("============== FAILED TO VIEW STOCK ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Stock.", ""));
            }
        }
    }

    // INTEGRATED WITH MRP
//    public void displayProducts() {
//
//        System.out.println("[IN MANAGED BEAN -- VIEW EDIT STOCK MB] ====================== displayproducts");
//        if (companyId != null) {
//            productList = psb.getMyProducts(companyId);
//        }
//    }

    public void deleteStock(Stock stock) {
        try {
            boolean result = amsb.deleteStock(stock.getStockId());
            if (result) {
                stocks.remove(stock);
                FacesMessage msg = new FacesMessage("Stock with ID = " + stock.getStockId() + " has sucessfully been deleted");
                FacesContext.getCurrentInstance().addMessage(null, msg);
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
        boolean result = amsb.editStock(stock.getName(), stock.getComments(), stock.getQuantity(), stock.getProductId(), stock.getStockId(), stock.getExpiryDate());
        if (result) {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
