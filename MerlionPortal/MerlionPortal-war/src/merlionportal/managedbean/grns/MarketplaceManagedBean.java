/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import entity.Post;
import java.io.IOException;
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

/**
 *
 * @author mac
 */
@Named(value = "marketplaceManagedBean")
@ViewScoped
public class MarketplaceManagedBean {

    @EJB
    private PostsManagerSessionBean postsSB;

    private Integer userId;
    private Integer companyId;
    private List<Post> allTPosts;
    private List<Post> allWPosts;
    private Post selectedPost;
    private List<Post> filteredPosts;
    private Post selectedWPost;

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
        allTPosts = postsSB.getAllTPosts(companyId);
        allWPosts = postsSB.getAllWPosts(companyId);
    }

    public MarketplaceManagedBean() {
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

    public Post getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }

    public List<Post> getFilteredPosts() {
        return filteredPosts;
    }

    public void setFilteredPosts(List<Post> filteredPosts) {
        this.filteredPosts = filteredPosts;
    }

    public List<Post> getAllTPosts() {
        return allTPosts;
    }

    public void setAllTPosts(List<Post> allTPosts) {
        this.allTPosts = allTPosts;
    }

    public List<Post> getAllWPosts() {
        return allWPosts;
    }

    public void setAllWPosts(List<Post> allWPosts) {
        this.allWPosts = allWPosts;
    }

    public Post getSelectedWPost() {
        return selectedWPost;
    }

    public void setSelectedWPost(Post selectedWPost) {
        this.selectedWPost = selectedWPost;
    }

    public String bidSelectedPost() {
        if (selectedPost == null && selectedWPost==null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select One Post to bid"));
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        System.out.println("bid selected");
        System.out.println("================" + selectedPost.getDescription());
        if (selectedPost != null) {
            sessionMap.put("selectedPost", selectedPost);
        }
        if (selectedWPost != null) {
            sessionMap.put("selectedPost", selectedWPost);
        }
        return "bidpost.xhtml?faces-redirect=true";
    }
}