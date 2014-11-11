package merlionportal.managedbean.mrp2;

import entity.Product;
import entity.SystemUser;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.backordermodule.BackorderSessionBean;
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

    

    private Integer companyId;
    private Integer productId;
    private Product product;
    private List<Product> products;
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
        return "forecastviewhistory?faces-redirect=true";
    }

    public String proceedGetUserInput() {
        return "forecastgetperiodicity?faces-redirect=true";
    }

    public String stopForecasting() {
        return "forecast?faces-redirect=true";
    }

    public String proceedViewResult() {
        return ("forecastResult");
    }

    public String goBackForecastParameter() {
        return "forecastgetperiodicity?faces-redirect=true";
    }

    public String proceedMPS() {
        return "mps?faces-redirect=true";
    }
    
    public String backToHistory() {
        return "forecastviewhistory?faces-redirect=true";
    }
    
    

}
