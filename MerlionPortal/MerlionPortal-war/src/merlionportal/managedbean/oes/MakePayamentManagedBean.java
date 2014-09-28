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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.oes.ordermanagement.PaymentManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "makePayamentManagedBean")
@ViewScoped
public class MakePayamentManagedBean {

    @EJB
    private PaymentManagerSessionBean paymentMB;
    private Integer companyId;
    private Integer userId;
    private List<ProductOrder> unpaidOrderList;
    private ProductOrder selectedOrder;

    public MakePayamentManagedBean() {
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
        unpaidOrderList=paymentMB.getAllUnpaidOrder(userId);
    }

    public List<ProductOrder> getUnpaidOrderList() {
        return unpaidOrderList;
    }

    public void setUnpaidOrderList(List<ProductOrder> unpaidOrderList) {
        this.unpaidOrderList = unpaidOrderList;
    }

    public ProductOrder getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(ProductOrder selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public String processPayament() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("unpaidOrder", selectedOrder);

        return "recordpayment.xhtml?faces-redirect=true;";
    }

}
