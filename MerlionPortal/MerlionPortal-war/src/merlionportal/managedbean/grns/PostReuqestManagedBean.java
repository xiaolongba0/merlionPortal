/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import entity.Contract;
import entity.ServicePO;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.grns.reverseauctionmodule.ReverseAuctionManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "postReuqestManagedBean")
@ViewScoped
public class PostReuqestManagedBean {

    @EJB
    private ReverseAuctionManagerSessionBean reverseSB;
    @EJB
    private SystemLogSessionBean systemLogSB;
    
    private Integer userId;
    private Integer companyId;
    private List<ServicePO> transList;
    private List<Contract> warehouseList;
    private int serviceType;
    private String orign;
    private String desti;
    private Integer volume;
    private Date deliveryDate;
    private String pickingPoint;
    private String shipto;
    private String description;
    private String currency;
    private Date expiryDate;
    private Date storageStart;
    private Date storageEnd;
    private Double totalSpace = 0.0;

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
        serviceType = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("serviceType");
        if (serviceType == 1) {
            transList = (List<ServicePO>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedT");
            orign = reverseSB.getORegion(transList.get(0));
            desti = reverseSB.getDRegion(transList.get(0));
            volume = 0;
            for (Object o : transList) {
                ServicePO p = (ServicePO) o;
                volume += p.getVolume();
            }
            deliveryDate = transList.get(0).getServiceDeliveryDate();
        }
        if (serviceType == 2) {
            warehouseList = (List<Contract>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedW");
            storageStart = warehouseList.get(0).getStartDate();
            storageEnd = warehouseList.get(0).getEndDate();
            for (Object o : warehouseList) {
                Contract myC = (Contract) o;
                totalSpace += myC.getAmountOfProduct().intValue() * myC.getSpacePerProduct();
            }
        }

    }

    public PostReuqestManagedBean() {
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

    public List<ServicePO> getTransList() {
        return transList;
    }

    public void setTransList(List<ServicePO> transList) {
        this.transList = transList;
    }

    public List<Contract> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<Contract> warehouseList) {
        this.warehouseList = warehouseList;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String viewCompanyName(Integer compId) {
        if (compId == null) {
            return "";
        }
        return reverseSB.getCompanyName(compId);
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

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPickingPoint() {
        return pickingPoint;
    }

    public void setPickingPoint(String pickingPoint) {
        this.pickingPoint = pickingPoint;
    }

    public String getShipto() {
        return shipto;
    }

    public void setShipto(String shipto) {
        this.shipto = shipto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getStorageStart() {
        return storageStart;
    }

    public void setStorageStart(Date storageStart) {
        this.storageStart = storageStart;
    }

    public Date getStorageEnd() {
        return storageEnd;
    }

    public void setStorageEnd(Date storageEnd) {
        this.storageEnd = storageEnd;
    }

    public Double getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(Double totalSpace) {
        this.totalSpace = totalSpace;
    }

    public String postTrequest() {
        if (transList == null || description == null || shipto == null || pickingPoint == null || deliveryDate == null || currency == null || expiryDate == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please fillin all filed."));
            return "post.xhtml";

        }
        System.out.println("orign destination" + orign + "   " + desti);
        System.out.println("Shio to pickingpoint " + shipto + "   " + pickingPoint);

        reverseSB.submitTPost(transList, description, shipto, pickingPoint, deliveryDate,
                currency, expiryDate, userId, volume);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Post Successful!"));
        systemLogSB.recordSystemLog(userId, "GRNS posted Transportation request.");
        return "aggregaterequests.xhtml?faces-redirect=true";

    }

    public String postWrequest() {
        if (warehouseList == null || description == null || storageStart == null || storageEnd == null || expiryDate == null || currency == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please fillin all filed."));
            return "post.xhtml";
        }
        reverseSB.submitWPost(warehouseList, description, storageStart,
                storageEnd, totalSpace, currency, expiryDate, userId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Post Successful!"));
        systemLogSB.recordSystemLog(userId, "GRNS posted Warehouse request.");
        return "aggregaterequests.xhtml?faces-redirect=true";
    }
}
