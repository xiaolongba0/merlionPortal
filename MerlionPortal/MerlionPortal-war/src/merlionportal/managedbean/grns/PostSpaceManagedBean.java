/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.grns.reverseauctionmodule.ReverseAuctionManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "postSpaceManagedBean")
@ViewScoped
public class PostSpaceManagedBean {

    @EJB
    private ReverseAuctionManagerSessionBean reverseSB;

    private Integer userId;
    private Integer companyId;
    private Date startDate;
    private Date endDate;
    private Date deliveryDate;
    private Integer volume;
    private String orign;
    private String desti;
    private Integer warehouseId;
    private Double totalSpace;
    private String currency;
    private Date expireDate;
    private Integer serviceType;
    private String warehouseLocation = "";
    private String warehouseName = "";
    private String description;
    private Boolean summitable;

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
        serviceType = 1;
        summitable = true;
    }

    public PostSpaceManagedBean() {
    }

    public String viewCompanyName(Integer compId) {
        if (compId == null) {
            return "";
        }
        return reverseSB.getCompanyName(compId);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getOrign() {
        return orign;
    }

    public void setOrign(String orign) {
        this.orign = orign;
    }

    public String getDesti() {
        return desti;
    }

    public void setDesti(String desti) {
        this.desti = desti;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Double getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(Double totalSpace) {
        this.totalSpace = totalSpace;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void updateWinfo() {
        serviceType = 2;
        System.out.println("UPDATE WARE HOUSE INFO!!!!!!---------------START");
        if (warehouseId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please Enter Warehouse Id."));

        } else {
            if (!reverseSB.checkValidWarehouse(warehouseId, companyId)) {
                warehouseId=null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Invalid Warehouse Id please re-selecte."));
            } else {
                warehouseName = reverseSB.getWarehouseName(warehouseId);
                warehouseLocation = reverseSB.getWarehouseLocation(warehouseId);
            }
        }
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public void postRequest() {
        if (serviceType == 1) {

            reverseSB.postTPost(description, desti, orign, deliveryDate, currency, expireDate, userId, volume);
            summitable = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Post successful"));

        }
        if (serviceType == 2) {
            reverseSB.postWPost(warehouseId, description, startDate, endDate, totalSpace, currency, expireDate, userId);
            summitable = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Post successful"));

        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isSummitable() {
        return summitable;
    }

    public void setSummitable(Boolean summitable) {
        this.summitable = summitable;
    }

}
