package merlionportal.managedbean;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;

//import util.exception.VenueConflictException;

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
    
   // private String venue;
   // private String systemUser;
    private String productNameLength;
    private String statusMessage;
    private Integer newProductId;

    public ProductManagerBean() {
        productNameLength = "Current product name length is less than 4.";
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

 /*   public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getSystemUser() {
        return systemUser;
    }*/

 //   public void setSystemUser(String systemUser) {
 //       this.systemUser = systemUser;
  //  }

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

    public void saveNewProduct(ActionEvent product) {
 
      //  Integer venueId = Integer.valueOf(venue);
      //  Integer systemUserId = Integer.valueOf(systemUser);
        try {
            newProductId = productSessionBean.addNewProduct(productName, description, productType, currency, price /*, venueId, systemUserId*/);
            statusMessage = "New Product Saved Successfully";
     //   } catch (VenueConflictException vex) {
      //      statusMessage = "Venue Conflict Exception";
     //       newProductId = -1L;
        } catch (Exception ex) {
            ex.printStackTrace();
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
