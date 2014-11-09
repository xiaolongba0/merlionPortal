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
@Named(value = "postManagedBean")
@ViewScoped
public class PostManagedBean {

    @EJB
    private PostsManagerSessionBean postsSB;

    private Integer userId;
    private Integer companyId;
    private List<Post> allTPosts;
    private List<Post> allWPosts;
    private List<Post> pastTposts;
    private List<Post> pastWposts;
    private Post selectedPost;
    private Post selectedWPost;
    private Post pastT;
    private Post pastW;

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

        allTPosts = postsSB.getMyActiveTPosts(companyId);
        allWPosts = postsSB.getMyActiveWPosts(companyId);
        pastTposts = postsSB.getMyHistoricalTPosts(companyId);
        pastWposts = postsSB.getMyHistoricalWPosts(companyId);
    }

    public PostManagedBean() {
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

    public Post getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }

    public Post getSelectedWPost() {
        return selectedWPost;
    }

    public void setSelectedWPost(Post selectedWPost) {
        this.selectedWPost = selectedWPost;
    }

    public List<Post> getPastTposts() {
        return pastTposts;
    }

    public void setPastTposts(List<Post> pastTposts) {
        this.pastTposts = pastTposts;
    }

    public List<Post> getPastWposts() {
        return pastWposts;
    }

    public void setPastWposts(List<Post> pastWposts) {
        this.pastWposts = pastWposts;
    }

    public Post getPastT() {
        return pastT;
    }

    public void setPastT(Post pastT) {
        this.pastT = pastT;
    }

    public Post getPastW() {
        return pastW;
    }

    public void setPastW(Post pastW) {
        this.pastW = pastW;
    }

    public String viwPost() {
        if (selectedPost == null && selectedWPost == null && pastT == null && pastW == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select One Post to View"));
            return "allmyposts.xhtml";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        if (selectedPost != null) {
            sessionMap.put("selectedPost", selectedPost);
        }
        if (selectedWPost != null) {
            sessionMap.put("selectedPost", selectedWPost);
        }
        if (pastT != null) {
            sessionMap.put("selectedPost", pastT);
        }
        if (pastW != null) {
            sessionMap.put("selectedPost", pastW);
        }
        return "viewmypost.xhtml?faces-redirect=true";
    }
    
}
