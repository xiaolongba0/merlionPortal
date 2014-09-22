
package merlionportal.managedbean.mrp2;

import entity.Product;
import java.util.List;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.mrp.forecastingmodule.ForecastSessionBean;
 
/**
 *
 * @author yao
 */
@Named(value = "forecastManagedBean")
@ViewScoped
public class ForecastManagedBean implements Serializable{
    @EJB
    ForecastSessionBean forecastSessionBean;
    
    Integer companyId = 12345;
    Integer productId;
    Product product;
    List<Product> products;
   
        public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
     public List<Product> getProducts() {
        products = forecastSessionBean.getMyProducts(companyId);
        return products;
    }

    public List<Product> getMyCompanyProducts() {
//        companyId=(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

        return products;
    }
    
    
 
    
}