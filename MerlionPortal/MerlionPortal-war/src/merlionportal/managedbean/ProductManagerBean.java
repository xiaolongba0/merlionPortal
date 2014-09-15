package merlionportal.managedbean;

import entity.Product;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import javax.faces.event.ActionEvent;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;


@Named(value = "productManagerBean")
@RequestScoped
public class ProductManagerBean {

    @EJB
    private ProductSessionBean productSessionBean;
    private String productName;
    private String description;
    private String productType;
    private String currency;
    private Double price;
    private Integer companyId = 12345;
    private Double productId;
    private Product productTemp;
    
  /*  private String componentName;
    private String componentDescription;
    private Integer componentQuantity;
    private String componentCurrency;
    private Double componentCost;
    private Integer componentLeadTime;*/


    private String productNameLength;
    private String statusMessage;
    private Integer newProductId;
 //   private Integer newComponentId;
    
    //private String types = "Manufacturing" + "Non-manufacturing";

    public ProductManagerBean() {
        productNameLength = "Current product name length is less than 4.";
    }

    public Double getProductId() {
        return productId;
    }

    public void setProductId(Double productId) {
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
    
 /*  Used for 
    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }*/
    
    /*    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
    
        public String getComponentDescription(){
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
    
    */
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

      //  Integer venueId = Integer.valueOf(venue);
        //  Integer systemUserId = Integer.valueOf(systemUser);
        try {
            //companyId =(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            newProductId = productSessionBean.addNewProduct(productName, description, productType, currency, price, companyId);
            statusMessage = "New Product Saved Successfully";
     //   } catch (VenueConflictException vex) {
            //      statusMessage = "Venue Conflict Exception";
            //       newProductId = -1L;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
    public void viewAProduct() {
//        companyId=(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
        try {
            int pdtID = productId.intValue();
            productTemp = productSessionBean.getAProduct(companyId, pdtID);
            productId = productTemp.getProductId().doubleValue();
            productName = productTemp.getProductName();
            description = productTemp.getDescription();
            productType = productTemp.getProductType();
            currency = productTemp.getCurrency();
            price = productTemp.getPrice();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
        ////////////////////////////////////////////////////////////////editing
  /*      public void editProduct(ActionEvent product) {
        try {
            int pdtID = productId.intValue();
            productSessionBean.editProduct(productName, description, productType, currency, price, companyId, pdtID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }  */
    

    public void doAjaxCountProductNameLength() {
        if (productName != null) {
            productNameLength = Integer.toString(productName.length());
        } else {
            productNameLength = "0";
        }
        productNameLength = "Current product name length is " + productNameLength;
    }
}
