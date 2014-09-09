package merlionportal.managedbean;

import entity.Product;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;

@Named(value = "productDataTableBean")
@RequestScoped
public class ProductDataTableBean {

    @EJB
    private ProductSessionBean productSessionBean;

    public ProductDataTableBean() {
    }

    public List<Product> getProducts() {
        return productSessionBean.getAllProducts();
    }
}
