/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.Mrp;
import entity.ProductOrder;
import entity.ProductOrderLineItem;
import entity.SystemUser;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.pomodule.PurchaseOrderSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "mrpPurchaseOrderManagedBean")
@ViewScoped
public class MrpPurchaseOrderManagedBean {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    PurchaseOrderSessionBean productOrderSessionBean;

    private SystemUser loginedUser;
    Integer companyId;
    Integer productId;

    List<ProductOrderLineItem> lineItemList;
    List<ProductOrder> pos;
    List<Mrp> mrps;

    
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
         productId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productId");
          mrps = (List<Mrp>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mrps");
         createPO();

    }

    public MrpPurchaseOrderManagedBean() {
    }
    
    public void createPO(){
        
        pos = productOrderSessionBean.createPO(productId, companyId, loginedUser, mrps);
        
    }

        public List<ProductOrderLineItem> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<ProductOrderLineItem> lineItemList) {
        this.lineItemList = lineItemList;
    }
    
        public List<ProductOrder> getPos() {
        return pos;
    }

    public void setPos(List<ProductOrder> pos) {
        this.pos = pos;
    }
  public String backToMrpHome() {
        return ("mrp");
    }
      
      public String cancelPO(){
          return ("mrp");
      }
    
}