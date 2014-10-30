/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.ProductOrder;
import entity.SystemUser;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.pomodule.PurchaseOrderSessionBean;

/**
 *
 * @author yao
 */
@ViewScoped
@ManagedBean(name = "viewNotificationManagedBean")
public class ViewNotificationManagedBean {

    /**
     * Creates a new instance of ProductViewEditManagedBean
     */
    @EJB
    private PurchaseOrderSessionBean purchaseOrderSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    
    private Integer companyId;


    private SystemUser loginedUser;
    private List<ProductOrder> pos;

    public ViewNotificationManagedBean() {
    }

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
        pos = purchaseOrderSessionBean.getAllMyPOs(companyId);
    }

   public List<ProductOrder> getProductOrders() {
      return pos;
    }
    
   
}

