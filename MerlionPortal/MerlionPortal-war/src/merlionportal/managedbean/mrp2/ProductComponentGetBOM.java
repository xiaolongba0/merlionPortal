package merlionportal.managedbean.mrp2;

import entity.Product;
import java.util.List;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.forecastingmodule.ForecastSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "productComponentGetBOM")
@ViewScoped
public class ProductComponentGetBOM implements Serializable {

    @EJB
    ForecastSessionBean forecastSessionBean;
    private Integer companyId = 12345;
    private Integer productId;
    private Product product;
    private List<Product> products;

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
    
           public String proceedViewBOM() {
        return ("viewbomforaproduct");
    }

}

