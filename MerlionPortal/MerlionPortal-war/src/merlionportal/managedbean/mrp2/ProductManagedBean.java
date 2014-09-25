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
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

@Named(value = "productManagedBean")
@ViewScoped
public class ProductManagedBean {

    @EJB
    private ProductSessionBean productSessionBean;
    
    @EJB
    UserAccountManagementSessionBean uamb;
    
    private String productName;
    private String description;
    private String category;
    private String productType;
    private String currency;
    private Double price;
    private Integer companyId;

    private Integer productId;
    private Product productTemp;
private Product product;
    private List<Product> products;

    private String componentName;
    private String componentDescription;
    private Integer componentQuantity;
    private String componentCurrency;
    private Double componentCost;
    private Integer componentLeadTime;

    private Integer supplierCompanyId;
    private String supplierContactPerson;
    private String supplierContactNumber;
    private String supplierContactEmail;

    private String productNameLength;
    private String statusMessage;
    private Integer newProductId;
    private Integer newComponentId;
 //   private Integer newComponentId;
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
        
        products = productSessionBean.getMyProducts(companyId);
}
    

    //private String types = "Manufacturing" + "Non-manufacturing";
    public ProductManagedBean() {
        productNameLength = "Current product name length is less than 4.";
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
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

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentDescription() {
        return componentDescription;
    }

    public void setComponentDescription(String componentDescription) {
        this.componentDescription = componentDescription;
    }

    public Integer getComponentQuantity() {
        return componentQuantity;
    }

    public void setComponentQuantity(Integer componentQuantity) {
        this.componentQuantity = componentQuantity;
    }

    public String getComponentCurrency() {
        return componentCurrency;
    }

    public void setComponentCurrency(String componentCurrency) {
        this.componentCurrency = componentCurrency;
    }

    public Double getComponentCost() {
        return componentCost;
    }

    public void setComponentCost(Double componentCost) {
        this.componentCost = componentCost;
    }

    public Integer getComponentLeadTime() {
        return componentLeadTime;
    }

    public void setComponentLeadTime(Integer componentLeadTime) {
        this.componentLeadTime = componentLeadTime;
    }

    public Integer getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Integer supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierContactEmail() {
        return supplierContactEmail;
    }

    public void setSupplierContactEmail(String supplierContactEmail) {
        this.supplierContactEmail = supplierContactEmail;
    }

    public String getSupplierContactPerson() {
        return supplierContactPerson;
    }

    public void setSupplierContactPerson(String supplierContactPerson) {
        this.supplierContactPerson = supplierContactPerson;
    }

    public String getSupplierContactNumber() {
        return supplierContactNumber;
    }

    public void setSupplierContactNumber(String supplierContactNumber) {
        this.supplierContactNumber = supplierContactNumber;
    }

   

    public void setProductNameLength(String productNameLength) {
        this.productNameLength = productNameLength;
    }

    public String getProductNameLength() {
        return productNameLength;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getNewProductId() {
        return newProductId;
    }

    public void setNewProductId(Integer newProductId) {
        this.newProductId = newProductId;
    }

    public List<Product> getProducts() {
        return products;
    }
    
     public void setProducts(List<Product> products) {
        this.products = products;
    }
     
         public Product getProduct() {
        return product;
    }
    
     public void setProduct(Product product) {
        this.product = product;
    }

    //ok. But see if user input (in xhtml) can be in Integer type directly.
    public void deleteProduct(ActionEvent product) {
        try {
            
            int pdtID = productId.intValue();
            productSessionBean.deleteProducts(companyId, pdtID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveNewProduct(ActionEvent product) {
        try {
            newProductId = productSessionBean.addNewProduct(productName, description, category, productType, currency, price, companyId);
            statusMessage = "New Product Saved Successfully";
            this.clearAllProductFields();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
     public void saveNewComponent(ActionEvent component) {

        try {
            int pdtTempId = productId.intValue();
            newComponentId = productSessionBean.addNewComponent(componentName, componentDescription, componentQuantity, componentCurrency, componentCost, componentLeadTime, supplierCompanyId, supplierContactPerson, supplierContactNumber, supplierContactEmail, companyId, pdtTempId);
            statusMessage = "New Component Saved Successfully";
            this.clearAllComponentFields();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void clearAllProductFields() {
        productName = null;
        description = null;
        category = null;
        productType = null;
        currency = null;
        price = null;
    }
    
    private void clearAllComponentFields() {
        productId = null;
        componentName = null;
        componentDescription = null;
        componentQuantity = null;
        componentCurrency = null;
        componentCost = null;
        componentLeadTime = null;
        supplierCompanyId = null;
        supplierContactPerson = null;
        supplierContactNumber = null;
        supplierContactEmail = null;
 }

    /* do it later
     public void saveNewComponent(ActionEvent product) {

     //  Integer venueId = Integer.valueOf(venue);
     //  Integer systemUserId = Integer.valueOf(systemUser);
     try {
     //companyId =(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
        
       
     newComponentId = productSessionBean.addNewComponent(componentName, componentDescription, componentQuantity, componentCurrency, componentCost, componentLeadTime, companyId, newProductId);
     statusMessage = "New Component Saved Successfully";
     //   } catch (VenueConflictException vex) {
     //      statusMessage = "Venue Conflict Exception";
     //       newProductId = -1L;
     } catch (Exception ex) {
     ex.printStackTrace();
     }
     }
     */

    /*  ////////////////////////////////////////////////////////////////
     public Product getAProduct() {
     //        companyId=(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
     int pdtID = productId.intValue();
     productTemp = productSessionBean.getAProduct(companyId, pdtID);
     return productTemp;
     } */
    
    //change later
    public void viewAProduct() {
        try {
            int pdtID = productId.intValue();
            productTemp = productSessionBean.getAProduct(companyId, pdtID);
            productId = productTemp.getProductId();
            productName = productTemp.getProductName();
            description = productTemp.getDescription();
            category = productTemp.getCategory();
            productType = productTemp.getProductType();
            currency = productTemp.getCurrency();
            price = productTemp.getPrice();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Product Edited");
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

    public void doAjaxCountProductNameLength() {
        if (productName != null) {
            productNameLength = Integer.toString(productName.length());
        } else {
            productNameLength = "0";
        }
        productNameLength = "Current product name length is " + productNameLength;
    }
}
