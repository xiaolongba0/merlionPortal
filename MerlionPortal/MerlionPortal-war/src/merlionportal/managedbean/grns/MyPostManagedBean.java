/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import entity.Bid;
import entity.Post;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.grns.reverseauctionmodule.PostsManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "myPostManagedBean")
@ViewScoped
public class MyPostManagedBean {

    @EJB
    private PostsManagerSessionBean postsSB;

    private Integer userId;
    private Integer companyId;
    private Post myPost;
    private int serviceType;
    private Boolean needCancel;
    private Bid selectedBid;
    private Boolean openPost;
    
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
        needCancel = false;
        openPost = false;
        myPost = (Post) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedPost");
        myPost = (Post) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedPost");
        String serviceT = myPost.getServiceType();
        System.out.println("service Type" + serviceT);
        if (serviceT.equals("Transportation")) {
            serviceType = 1;
        }
        if (serviceT.equals("Warehouse")) {
            serviceType = 2;
        }
        Date todayDate = new Date();
        if ((!myPost.getExpireDate().after(todayDate))&&myPost.getStatus().equals("Valid")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "This Post is No Longer Valid Please Cancel"));
        }
        if(myPost.getStatus().equals("Valid")){
            needCancel=true;
        }
        if(myPost.getExpireDate().after(todayDate)&& myPost.getStatus().equals("Valid")){
            openPost=true;
        }
    }
    
    public void cancelThisRequest(){
        postsSB.cancelPost(myPost);
    }

    public MyPostManagedBean() {
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

    public Boolean isNeedCancel() {
        return needCancel;
    }

    public void setNeedCancel(Boolean needCancel) {
        this.needCancel = needCancel;
    }

    public Bid getSelectedBid() {
        return selectedBid;
    }

    public void setSelectedBid(Bid selectedBid) {
        this.selectedBid = selectedBid;
    }

    public Boolean isOpenPost() {
        return openPost;
    }

    public void setOpenPost(Boolean openPost) {
        this.openPost = openPost;
    }
    
    
}
