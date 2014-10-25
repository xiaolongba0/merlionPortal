/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.Product;
import entity.SystemUser;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author yao
 */
@ViewScoped
@ManagedBean(name = "productViewEditManagedBean")
public class ProductViewEditManagedBean {

    /**
     * Creates a new instance of ProductViewEditManagedBean
     */
    @EJB
    private ProductSessionBean productSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    private Integer companyId;

    private List<Product> products;

    private String productName;
    private String description;
    private String category;
    private String productType;
    private String currency;
    private Double price;
    private Integer productId;
    private Product product;
    private final static String[] productTypes;
    private List<String> currencies;
    private final static String[] categories;

    private SystemUser loginedUser;

    public ProductViewEditManagedBean() {
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

        products = productSessionBean.getMyProducts(companyId);
    }

    static {
        productTypes = new String[2];
        productTypes[0] = "Manufacturing";
        productTypes[1] = "Non-Manufacturing";

        /*  currencies = new String[20];
         currencies[0] = "US Dollar (USD)";
         currencies[1] = "European Euro (EUR)";
         currencies[2] = "Japan Yen (JPY)";
         currencies[3] = "Pound Sterling (GBP)";
         currencies[4] = "Australian Dollar (AUD)";
         currencies[5] = "Swiss Franc (CHF)";
         currencies[6] = "Canadian Dollar";
         currencies[7] = "Hong Kong Dollar (HKD)";
         currencies[8] = "Swedish Krona (SEK)";
         currencies[9] = "New Zealand Dollar (NZD)";
         currencies[10] = "South African Rand (ZAR)";
         currencies[11] = "Russian Ruble (RUB)";
         currencies[12] = "Indian Rupee (INR)";
         currencies[13] = "Singapore Dollar (SGD)";
         currencies[14] = "Bulgarian Lev (BGN)";
         currencies[15] = "Chinese Yuan Renminbi (CNY)";
         currencies[16] = "Thailand Baht (THB)";
         currencies[17] = "Hungary Forint(HUF)";
         currencies[18] = "Norwegian Krone (NOK)";
         currencies[19] = "Mexican Peso (MXN)";  */
        categories = new String[13];
        categories[0] = "Food - Fresh Products";
        categories[1] = "Food - Frozen Products";
        categories[2] = "Food - Normal Products";
        categories[3] = "Beverages";
        categories[4] = "Retail and Apparel";
        categories[5] = "Automotive";
        categories[6] = "Household Products";
        categories[7] = "Children Products";
        categories[8] = "Personal Care Products";
        categories[9] = "Electronics";
        categories[10] = "Semi-Finished Products";
        categories[11] = "Raw Materials";
        categories[12] = "Others";

    }

    public String[] getProductTypes() {
        return productTypes;
    }

    public List<String> getCurrencies() {
        this.currencies = productSessionBean.getListCurrency();
        return currencies;
    }

    public String[] getCategories() {
        return categories;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

   public String getProductName() {
        System.out.println("SEE if get productName");
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Product> getProducts() {
        products = productSessionBean.getAllProducts();
        return products;
    }

    public List<Product> getMyCompanyProducts() {
        return products;
    }

    //ok. But see if user input (in xhtml) can be in Integer type directly.
    public void deleteProduct(Product product) {
        try {
            productId = product.getProductId();
            productSessionBean.deleteProducts(companyId, productId);
            products.remove(product);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {

        product = new Product();
        product = (Product) event.getObject();
        System.err.println("product.getProductName(): " + product.getProductName());
        productSessionBean.editProduct(product.getProductName(), product.getDescription(), product.getCategory(), product.getProductType(), product.getCurrency(), product.getPrice(), companyId, product.getProductId());
        FacesMessage msg = new FacesMessage("Product Edited", String.valueOf(product.getProductId()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
