/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.grns.reverseauctionmodule;

import entity.Bid;
import entity.Post;
import entity.SystemUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class PostsManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public PostsManagerSessionBean() {
    }

    public List<Post> getAllTPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            //rmb to change to companyId!=p.getSystemUser..... !!!!
            if (companyId == p.getSystemUser().getCompanycompanyId().getCompanyId() && p.getServiceType().equals("Transportation")) {
                if (this.checkPostValidity(p)) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public List<Post> getAllWPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            //rmb to change to companyId!=p.getSystemUser..... !!!!!!
            if (companyId == p.getSystemUser().getCompanycompanyId().getCompanyId() && p.getServiceType().equals("Warehouse")) {
                if (this.checkPostValidity(p)) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public List<Post> getMyActiveTPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            if (companyId == p.getSystemUser().getCompanycompanyId().getCompanyId()
                    && p.getStatus().equalsIgnoreCase("Valid")&& p.getServiceType().equals("Transportation")) {
                result.add(p);
            }
        }
        return result;
    }
    
    public List<Post> getMyActiveWPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            if (companyId == p.getSystemUser().getCompanycompanyId().getCompanyId()
                    && p.getStatus().equalsIgnoreCase("Valid")&& p.getServiceType().equals("Warehouse")) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Post> getMyHistoricalWPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            if (companyId == p.getSystemUser().getCompanycompanyId().getCompanyId()
                    && p.getStatus().equalsIgnoreCase("Expired")&& p.getServiceType().equals("Warehouse")) {
                result.add(p);
            }
        }
        return result;
    }
    
    public List<Post> getMyHistoricalTPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            if (companyId == p.getSystemUser().getCompanycompanyId().getCompanyId()
                    && p.getStatus().equalsIgnoreCase("Expired")&& p.getServiceType().equals("Transportation")) {
                result.add(p);
            }
        }
        return result;
    }

    public Boolean checkPostValidity(Post myPost) {
        Date todayDate = new Date();
        return !myPost.getExpireDate().before(todayDate);
    }
    
    public void bidPost(Double amount, Integer userId, Post post){
        Bid newBid = new Bid();
        SystemUser myUser=em.find(SystemUser.class, userId);
        newBid.setAmount(amount);
        newBid.setSystemUser(myUser);
        newBid.setPost(post);
        newBid.setSelected(false);
        em.persist(newBid);
        post.getBidList().add(newBid);
        em.merge(post);       
    }
    
    public void cancelPost(Post myPost){
        myPost.setStatus("Expired");
        em.merge(myPost);
    }
}
