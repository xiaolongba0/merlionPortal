/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import entity.Post;
import java.io.IOException;
import java.util.List;
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
@Named(value = "marketplaceManagedBean")
@ViewScoped
public class MarketplaceManagedBean {

    @EJB
    private PostsManagerSessionBean postsSB;

    private Integer userId;
    private Integer companyId;
    private List<Post> allTPosts;
    private List<Post> allWPosts;
    private List<Post> allSTPosts;
    private List<Post> allSWPosts;

    private List<Post> filteredPosts;
    private Post selectedPost;
    private Post selectedWPost;
    private Post selectedSTpost;
    private Post selectedSWpost;

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
        allSTPosts = postsSB.getAllSTPosts(companyId);
        allSWPosts = postsSB.getAllSWPosts(companyId);

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

    public Post getSelectedSTpost() {
        return selectedSTpost;
    }

    public void setSelectedSTpost(Post selectedSTpost) {
        this.selectedSTpost = selectedSTpost;
    }

    public Post getSelectedSWpost() {
        return selectedSWpost;
    }

    public void setSelectedSWpost(Post selectedSWpost) {
        this.selectedSWpost = selectedSWpost;
    }

    public String bidSelectedPost() {
        if (selectedPost == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select One Post to bid"));
        }

        System.out.println("bid selected");
        if (selectedPost != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedPost");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPost", selectedPost);
        }
        return "bidpost.xhtml?faces-redirect=true";
    }

    public String bidSelectedSTPost() {
        if (selectedSTpost == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select One Post to bid"));
        }

        System.out.println("bid selected");
        if (selectedSTpost != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedPost");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPost", selectedSTpost);
        }
        return "bidpost.xhtml?faces-redirect=true";
    }

    public String bidSelectedSWPost() {
        if (selectedSWpost == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select One Post to bid"));
        }

        System.out.println("bid selected");
        if (selectedSWpost != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedPost");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPost", selectedSWpost);
        }
        return "bidpost.xhtml?faces-redirect=true";
    }

    public String bidSelectedWPost() {
        if (selectedWPost == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select One Post to bid"));
        }

        System.out.println("bid selected");
        if (selectedWPost != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedPost");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPost", selectedWPost);
        }
        return "bidpost.xhtml?faces-redirect=true";
    }

    public String getWarehouseName(Integer warehouId) {
        if (warehouId == null) {
            return "";
        }
        return postsSB.getWarehouseLocation(warehouId);
    }

    public List<Post> getAllSTPosts() {
        return allSTPosts;
    }

    public void setAllSTPosts(List<Post> allSTPosts) {
        this.allSTPosts = allSTPosts;
    }

    public List<Post> getAllSWPosts() {
        return allSWPosts;
    }

    public void setAllSWPosts(List<Post> allSWPosts) {
        this.allSWPosts = allSWPosts;
    }

}
