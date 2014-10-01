package merlionportal.managedbean.mrp2;

import entity.Product;
import entity.SystemUser;
import java.io.IOException;
import java.util.List;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.forecastingmodule.ForecastSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "forecastManagedBean")
@ViewScoped
public class ForecastManagedBean implements Serializable {

    @EJB
    ForecastSessionBean forecastSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;

    Integer companyId;
    Integer productId;
    Product product;
    List<Product> products;
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
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productId", productId);
        
        System.out.println("!!!!!!!!!!!!!!!!forecastManagedBean: setProductId" + productId);
 }

    public List<Product> getProducts() {
        products = forecastSessionBean.getMyProducts(companyId);
        return products;
    }

    public List<Product> getMyCompanyProducts() {
//        companyId=(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
        return products;
    }

    public String navigator() {
        return ("forecastviewhistory");
    }

    public String proceedGetUserInput() {
        return ("forecastgetperiodicity");
    }

    public String stopForecasting() {
        return ("forecast");
    }

    public String proceedViewResult() {
        return ("forecastResult");
    }

    public String goBackForecastParameter() {
        return ("forecastgetperiodicity");
    }

    public String proceedMPS() {
        return ("mps");
    }
    
    public String backToHistory() {
        return ("forecastviewhistory");
    }
    
    

}
