/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "retrieveSavedOrder")
@RequestScoped
public class RetrieveSavedOrderManagedBean {

    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrderMB;

    private Integer companyId;
    private Integer userId;
    private List<ProductOrder> savedOrderList;
    private ProductOrder selectedOrder;
    private List<ProductOrder> productOrderList;
    

   
    @EJB
    private SystemAccessRightSessionBean systemAccessRightSB;

    public RetrieveSavedOrderManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

            if (userId != null) {
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

    public List<ProductOrder> getSavedOrderList() {
        savedOrderList = purchaseOrderMB.viewAllProductOrder(14, companyId, userId);
        return savedOrderList;
    }

    public List<ProductOrder> getProductOrderList() {
        if (systemAccessRightSB.checkOESGeneratePO(userId)) {
            productOrderList = purchaseOrderMB.viewAllProductOrder(1, companyId, userId);
        } else {
            productOrderList = purchaseOrderMB.viewAllProductOrder(1, companyId);

        }
        return productOrderList;
    }

    public boolean checkOESSales(){
        return systemAccessRightSB.checkOESGenerateSO(userId);
    }

    public void setSavedOrderList(List<ProductOrder> savedOrderList) {
        this.savedOrderList = savedOrderList;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ProductOrder getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(ProductOrder selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public String editOrder() {

        if (selectedOrder == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one order ."));
            return "retrievesavedorder.xhtml";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("savedOrder", selectedOrder);
        System.out.println("this line will executed");
        System.out.println("this line  executed  finished");
        return "editsavedorder.xhtml?faces-redirect=true";

    }

    public String createSo() {
        if (selectedOrder == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one order ."));
            return "viewallpo.xhtml";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("selectedOrder", selectedOrder);
        System.out.println("this line will executed");
        System.out.println("this line  executed  finished");
        selectedOrder = null;
        return "createso.xhtml?faces-redirect=true";
    }

}
