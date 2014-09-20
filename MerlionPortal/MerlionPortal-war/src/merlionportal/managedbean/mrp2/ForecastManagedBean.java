
package merlionportal.managedbean.mrp2;

import entity.Product;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.mrp.forecastingmodule.ForecastSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "forecastManagedBean")
@ViewScoped
public class ForecastManagedBean {
    @EJB
    ForecastSessionBean forecastSessionBean;
    
    Integer companyId = 12345;
    Integer productId;
    Product product;
    List<Product> products;
    
    public ForecastManagedBean() {
    }
    
  //    @PostConstruct
  //  public void init() {
   //     products = forecastSessionBean.getMyProducts(companyId);
  //  }
    
    //Show purchasing histroy on an monthly basis
    //hard code the data
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
