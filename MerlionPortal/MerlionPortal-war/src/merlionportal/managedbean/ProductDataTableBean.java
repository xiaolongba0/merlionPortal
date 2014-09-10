package merlionportal.managedbean;

import entity.Product;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;

@Named(value = "productDataTableBean")
@RequestScoped
public class ProductDataTableBean {

    @EJB
    private ProductSessionBean productSessionBean;
    private Integer companyId = 12345;

    private List<Product> products;

    public ProductDataTableBean() {
    }

    public List<Product> getProducts() {
        products = productSessionBean.getAllProducts();
        return products;
    }
    
    public List<Product> getMyCompanyProducts() {
//        companyId=(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

         products = productSessionBean.getMyProducts(companyId);
        return products;
    }  
    
}
