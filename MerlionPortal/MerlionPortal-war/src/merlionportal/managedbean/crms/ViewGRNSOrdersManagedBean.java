/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.GrnsServiceOrder;
import entity.ServicePO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.crms.ordermanagementmodule.GRNSOrderSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewGRNSOrdersManagedBean")
@ViewScoped
public class ViewGRNSOrdersManagedBean {

    /**
     * Creates a new instance of ViewGRNSOrdersManagedBean
     */
    private Integer companyId;
    private Integer userId;

    private List<GrnsServiceOrder> sentOrders;
    private List<GrnsServiceOrder> ReceivedOrders;

    private List<ServicePO> filteredServicePO;

    private ServicePO selectedSentOrder;
    private ServicePO selectedReceivedOrder;

    private List<String> status;

    @EJB
    GRNSOrderSessionBean grnsSB;

    public ViewGRNSOrdersManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
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
        sentOrders = grnsSB.viewAllSentOrders(companyId);
        ReceivedOrders = grnsSB.viewAllReceivedOrders(companyId);
        status = new ArrayList<>();
        status.add("Confirmed");
        status.add("Paid");
        status.add("Processed");

    }

    public String viewSentOrderDetail() {
        if (selectedSentOrder != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedGRNSOrder");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedGRNSOrder", selectedSentOrder);
            return "viewgrnsorderdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select an order to view!", ""));
            return null;
        }
    }
    public String viewReceivedOrderDetail() {
        if (selectedReceivedOrder != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedGRNSOrder");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedGRNSOrder", selectedReceivedOrder);
            return "viewgrnsorderdetail.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select an order to view!", ""));
            return null;
        }
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

    public List<GrnsServiceOrder> getSentOrders() {
        return sentOrders;
    }

    public void setSentOrders(List<GrnsServiceOrder> sentOrders) {
        this.sentOrders = sentOrders;
    }

    public List<GrnsServiceOrder> getReceivedOrders() {
        return ReceivedOrders;
    }

    public void setReceivedOrders(List<GrnsServiceOrder> ReceivedOrders) {
        this.ReceivedOrders = ReceivedOrders;
    }

    public List<ServicePO> getFilteredServicePO() {
        return filteredServicePO;
    }

    public void setFilteredServicePO(List<ServicePO> filteredServicePO) {
        this.filteredServicePO = filteredServicePO;
    }

    public ServicePO getSelectedSentOrder() {
        return selectedSentOrder;
    }

    public void setSelectedSentOrder(ServicePO selectedSentOrder) {
        this.selectedSentOrder = selectedSentOrder;
    }

    public ServicePO getSelectedReceivedOrder() {
        return selectedReceivedOrder;
    }

    public void setSelectedReceivedOrder(ServicePO selectedReceivedOrder) {
        this.selectedReceivedOrder = selectedReceivedOrder;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
    

}
