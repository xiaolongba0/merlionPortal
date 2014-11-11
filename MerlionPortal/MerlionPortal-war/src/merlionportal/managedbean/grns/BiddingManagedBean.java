/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

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
@Named(value = "biddingManagedBean")
@ViewScoped
public class BiddingManagedBean {

    @EJB
    private PostsManagerSessionBean postsSB;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private Integer userId;
    private Integer companyId;
    private Post mypost;
    private int serviceType;
    private Double bids;
    private String warehoueLocation;
    private String warehouseName;

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
        System.out.println("Init start===========");
        mypost = (Post) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedPost");
        if (mypost != null) {
            System.out.println("Not null");

            String serviceT = mypost.getServiceType();
            System.out.println("service Type" + serviceT);
            if (serviceT.equals("Transportation")) {
                serviceType = 1;
            }
            if (serviceT.equals("Warehouse")) {
                serviceType = 2;
            }
            if (serviceT.equals("Transportation Space")) {
                serviceType = 3;
            }
            if (serviceT.equals("Warehouse Space")) {
                serviceType = 4;
                warehoueLocation = postsSB.getWarehouseLocation(mypost.getWarehouseId());
                warehouseName = postsSB.getWarehouseName(mypost.getWarehouseId());
            }
        }
        System.out.println("My Post" + mypost);
    }

    public BiddingManagedBean() {
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

    public Post getMypost() {
        return mypost;
    }

    public void setMypost(Post mypost) {
        this.mypost = mypost;
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

    public String getWarehoueLocation() {
        return warehoueLocation;
    }

    public void setWarehoueLocation(String warehoueLocation) {
        this.warehoueLocation = warehoueLocation;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String bisPost() {

        if (bids == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please Enter Bids"));
            return "bidpost.xhtml";
        }
        postsSB.bidPost(bids, userId, mypost);
        systemLogSB.recordSystemLog(userId, "GRNS placed a bid.");
        return "viewallposts.xhtml?faces-redirect=true";
    }

}
