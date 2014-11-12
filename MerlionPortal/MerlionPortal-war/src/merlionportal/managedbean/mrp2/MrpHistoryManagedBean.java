/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.MRPList;
import entity.Mrp;
import entity.Product;
import entity.SystemUser;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.forecastingmodule.ForecastSessionBean;
import merlionportal.mrp.materialrequirementmodule.MrpHistorySessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "mrpHistoryManagedBean")
@ViewScoped
public class MrpHistoryManagedBean implements Serializable {

    @EJB
    ForecastSessionBean forecastSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    private ProductSessionBean psb;
    @EJB
    private MrpHistorySessionBean mhmb;

    private Integer companyId;
    private Integer productId;
    private Product product;
    private List<Product> products;
    private SystemUser loginedUser;
    private Integer mrplistId;
    private List<MRPList> mrplists;
    private List<Mrp> mrps;

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
        products = psb.getMyProducts(companyId);
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductId() {
        return productId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getMrplistId() {
        return mrplistId;
    }

    public void setMrplistId(Integer mrplistId) {
        this.mrplistId = mrplistId;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mrplistId", mrplistId);
    }

    public List<MRPList> getMrplists() {
        return mrplists;
    }

    public void setMrplists(List<MRPList> mrplists) {
        this.mrplists = mrplists;
    }

    public List<Mrp> getMrps() {
        return mrps;
    }

    public void setMrps(List<Mrp> mrps) {
        this.mrps = mrps;
    }

    public void onChangeProduct() {
        if (productId != null) {
            mrplists = mhmb.viewMrpLists(productId);
        }
    }

    public void onChangeMRP() {
        if (mrplistId != null) {
            mrps = mhmb.viewMrps(mrplistId);
            System.out.println("mrps:!!!!!" + mrps.get(1).getOnHand0());
        }
    }


    /*   public List<MRPList> getMrplists() {
     products = forecastSessionBean.getMyProducts(companyId);
     return products;
     }*/
}
