/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "viewSalesOrderManagedBean")
@RequestScoped
public class ViewSalesOrderManagedBean {

    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrdrMB;
    @EJB
    private SystemAccessRightSessionBean systemAccessRightSB;
    private Integer companyId;
    private Integer userId;
    private List<ProductOrder> salesOrderList=new ArrayList();
    private ProductOrder filteredOrder;
    private ProductOrder selectedOrder;
    private List<String> statusList;

    public ViewSalesOrderManagedBean() {
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

    public boolean checkUserIsCustomer() {
        return systemAccessRightSB.checkOESCustomer(userId);
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

    public List<ProductOrder> getSalesOrderList() {
        if (this.checkUserIsCustomer()) {
            salesOrderList = purchaseOrdrMB.viewAllOrder(companyId, userId);
        } else {
            salesOrderList = purchaseOrdrMB.viewAllOrder(companyId);

        }
        return salesOrderList;
    }

    public void setSalesOrderList(List<ProductOrder> salesOrderList) {
        this.salesOrderList = salesOrderList;
    }

    public ProductOrder getFilteredOrder() {
        return filteredOrder;
    }

    public void setFilteredOrder(ProductOrder filteredOrder) {
        this.filteredOrder = filteredOrder;
    }

    public ProductOrder getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(ProductOrder selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public List<String> getStatusList() {
        statusList=purchaseOrdrMB.getAllOrderStatus();
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }
    
    public String myOrderStatus(ProductOrder myorder){
        String result=purchaseOrdrMB.getOrderStatus(myorder);
        return result;
    }
    
    public String viewOrderDetail(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("viewOrder", selectedOrder);
        
        return "orderdetail.xhtml";
    }
}
