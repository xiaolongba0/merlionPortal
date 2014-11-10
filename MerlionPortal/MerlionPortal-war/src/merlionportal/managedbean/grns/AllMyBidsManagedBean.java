/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import entity.Bid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.grns.reverseauctionmodule.PostsManagerSessionBean;
import merlionportal.grns.reverseauctionmodule.ReverseAuctionManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "allMyBidsManagedBean")
@ViewScoped
public class AllMyBidsManagedBean {

    @EJB
    private PostsManagerSessionBean postsSB;
    @EJB
    private ReverseAuctionManagerSessionBean reverseSB;

    private Integer userId;
    private Integer companyId;
    private Bid selectedBid;
    private List<Bid> allMyBid;
    private List<String> bidStatus;

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
        allMyBid = postsSB.viewAllMyBids(companyId);
        bidStatus = new ArrayList();
        bidStatus.add("Open");
        bidStatus.add("Successful");
        bidStatus.add("Rejected");
    }

    public AllMyBidsManagedBean() {
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

    public Bid getSelectedBid() {
        return selectedBid;
    }

    public void setSelectedBid(Bid selectedBid) {
        this.selectedBid = selectedBid;
    }

    public List<Bid> getAllMyBid() {
        return allMyBid;
    }

    public void setAllMyBid(List<Bid> allMyBid) {
        this.allMyBid = allMyBid;
    }

    public String viewCompanyName(Integer compId) {
        if (compId == null) {
            return "";
        }
        return reverseSB.getCompanyName(compId);
    }

    public String viewBidStatus(Bid aBid) {
        if (aBid == null) {
            return "";
        }
        if (aBid.getSelected() == null) {
            return "Open";
        } else if (aBid.getSelected() == true) {
            return "Successful";
        } else {
            return "Rejected";
        }
    }

    public List<String> getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(List<String> bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String updateMyBid() {
        if (selectedBid == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select One Post to bid"));
            return "allmybids.xhtml";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("selectedBid", selectedBid);
        return "viewmybidinfo.xhtml?faces-redirect=true";
    }

}
