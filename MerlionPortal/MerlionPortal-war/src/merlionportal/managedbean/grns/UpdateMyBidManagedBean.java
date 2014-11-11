/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import entity.Bid;
import entity.Post;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.grns.reverseauctionmodule.PostsManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "updateMyBidManagedBean")
@ViewScoped
public class UpdateMyBidManagedBean {

    @EJB
    private PostsManagerSessionBean postsSB;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private Integer userId;
    private Integer companyId;
    private Bid myBid;
    private Post myPost;
    private int serviceType;
    private Double bids;
    private Boolean updatable;

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
        updatable = true;

        myBid = (Bid) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedBid");
        myPost = myBid.getPost();
        String serviceT = myPost.getServiceType();
        if (serviceT.equals("Transportation")) {
            serviceType = 1;
        }
        if (serviceT.equals("Transportation Space")) {
            serviceType = 3;
        }
        if (serviceT.equals("Warehouse")) {
            serviceType = 2;
        }
        if (serviceT.equals("Warehouse Space")) {
            serviceType = 4;
        }

        System.out.println("=======" + serviceType);

        if (myPost.getStatus().equalsIgnoreCase("Expired")) {
            updatable = false;
        }
    }

    public UpdateMyBidManagedBean() {
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

    public Bid getMyBid() {
        return myBid;
    }

    public void setMyBid(Bid myBid) {
        this.myBid = myBid;
    }

    public Post getMyPost() {
        return myPost;
    }

    public void setMyPost(Post myPost) {
        this.myPost = myPost;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public Double getBids() {
        return bids;
    }

    public void setBids(Double bids) {
        this.bids = bids;
    }

    public Boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public void updateMyBid() {
        postsSB.updateMyBid(myBid, bids);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO!", " Bids Updated "));
        systemLogSB.recordSystemLog(userId, "GRNS updated bid.");
    }

    public String getWarehouName(Integer wId) {
        if (wId == null) {
            return "";
        }
        return postsSB.getWarehouseName(wId);
    }

    public String getWarehouseLocation(Integer wId) {
        if (wId == null) {
            return "";
        }
        return postsSB.getWarehouseLocation(wId);
    }
}
