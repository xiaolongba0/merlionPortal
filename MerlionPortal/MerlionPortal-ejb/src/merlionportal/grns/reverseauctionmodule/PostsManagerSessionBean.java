/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.grns.reverseauctionmodule;

import entity.Bid;
import entity.GrnsServiceOrder;
import entity.Post;
import entity.SystemUser;
import entity.Warehouse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import merlionportal.crms.ordermanagementmodule.GRNSOrderSessionBean;

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class PostsManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    @EJB
    GRNSOrderSessionBean grnsSB;

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
            if (companyId != p.getSystemUser().getCompanycompanyId().getCompanyId() && p.getServiceType().equals("Transportation")) {
                if (this.checkPostValidity(p)) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public List<Post> getAllSTPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            //rmb to change to companyId!=p.getSystemUser..... !!!!
            if (companyId != p.getSystemUser().getCompanycompanyId().getCompanyId() && p.getServiceType().equals("Transportation Space")) {
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
            if (companyId != p.getSystemUser().getCompanycompanyId().getCompanyId() && p.getServiceType().equals("Warehouse")) {
                if (this.checkPostValidity(p)) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public List<Post> getAllSWPosts(int companyId) {
        List<Post> result = new ArrayList();
        Query q = em.createQuery("SELECT p FROM Post p");
        for (Object o : q.getResultList()) {
            Post p = (Post) o;
            //rmb to change to companyId!=p.getSystemUser..... !!!!!!
            if (companyId != p.getSystemUser().getCompanycompanyId().getCompanyId() && p.getServiceType().equals("Warehouse Space")) {
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
                    && p.getStatus().equalsIgnoreCase("Valid")
                    && (p.getServiceType().equals("Transportation") || p.getServiceType().equals("Transportation Space"))) {
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
                    && p.getStatus().equalsIgnoreCase("Valid")
                    && (p.getServiceType().equals("Warehouse") || p.getServiceType().equals("Warehouse Space"))) {
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
                    && p.getStatus().equalsIgnoreCase("Expired")
                    && (p.getServiceType().equals("Warehouse") || p.getServiceType().equals("Warehouse Space"))) {
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
                    && p.getStatus().equalsIgnoreCase("Expired")
                    && (p.getServiceType().equals("Transportation") || p.getServiceType().equals("Transportation Space"))) {
                result.add(p);
            }
        }
        return result;
    }

    public Boolean checkPostValidity(Post myPost) {
        Date todayDate = new Date();
        if(myPost.getStatus().equalsIgnoreCase("Expired")||myPost.getExpireDate().before(todayDate)){
            return false;
        }
        return true;
    }

    public void bidPost(Double amount, Integer userId, Post post) {

        Bid newBid = new Bid();
        SystemUser myUser = em.find(SystemUser.class, userId);
        if (myUser.getBidList() == null) {
            List<Bid> myBidslist = new ArrayList();
            myUser.setBidList(myBidslist);
        }
        newBid.setAmount(amount);
        newBid.setSystemUser(myUser);
        newBid.setPost(post);
        em.persist(newBid);
        post.getBidList().add(newBid);
        myUser.getBidList().add(newBid);
        em.merge(post);
    }

    public void cancelPost(Post myPost) {
        myPost.setStatus("Expired");
        em.merge(myPost);
    }

    public void acceptBidConvertToGrnsServiceOrder(Post myPost, Bid bid) {
        GrnsServiceOrder grnsOrder = new GrnsServiceOrder();
        grnsOrder.setOrderId(myPost.getPostId());
        grnsOrder.setCreateDate(new Date());
        grnsOrder.setCurrency(myPost.getCurrency());
        grnsOrder.setServiceType(myPost.getServiceType());
        grnsOrder.setPrice(bid.getAmount());
        grnsOrder.setStatus("Confirmed");
        grnsOrder.setDescription(myPost.getDescription());

        if (myPost.getServicePOList().isEmpty() && myPost.getContractList().isEmpty()) {
            grnsOrder.setServiceProvider(myPost.getSystemUser().getCompanycompanyId().getCompanyId());
            grnsOrder.setServiceRequester(bid.getSystemUser().getCompanycompanyId().getCompanyId());
            grnsSB.createGRNSOrderInvoice(myPost.getPostId(),myPost.getSystemUser().getSystemUserId() , myPost.getSystemUser().getCompanycompanyId().getCompanyId(), bid.getSystemUser().getCompanycompanyId().getCompanyId(), bid.getAmount());

        } else {
            grnsOrder.setServiceProvider(bid.getSystemUser().getCompanycompanyId().getCompanyId());
            grnsOrder.setServiceRequester(myPost.getSystemUser().getCompanycompanyId().getCompanyId());
            grnsSB.createGRNSOrderInvoice(myPost.getPostId(), bid.getSystemUser().getSystemUserId(), myPost.getSystemUser().getCompanycompanyId().getCompanyId(), bid.getSystemUser().getCompanycompanyId().getCompanyId(), bid.getAmount());

        }

        if (grnsOrder.getServiceType().equals("Transportation")) {
            grnsOrder.setDeliveryDate(myPost.getDeliveryDate());
            grnsOrder.setOrigin(myPost.getOrigin());
            grnsOrder.setDestination(myPost.getDestination());
            grnsOrder.setQuantityInTEU(myPost.getQuantityInTEU());
        }
        if (grnsOrder.getServiceType().equals("Warehouse")) {
            grnsOrder.setStorageStartDate(myPost.getStorageStartDate());
            grnsOrder.setStorageEndDate(myPost.getStorageEndDate());
            grnsOrder.setWarehouseSpace(myPost.getWarehouseSpace());
        }

        em.persist(grnsOrder);
        em.flush();

    }

    public List<Bid> viewAllMyBids(int companyId) {
        List<Bid> result = new ArrayList();
        Query q = em.createQuery("SELECT b FROM Bid b");
        for (Object o : q.getResultList()) {
            Bid myBid = (Bid) o;
            if (myBid.getSystemUser().getCompanycompanyId().getCompanyId() == companyId) {
                result.add(myBid);
            }
        }
        return result;
    }

    public void updateMyBid(Bid myBid, Double newprice) {
        myBid.setAmount(newprice);
        em.merge(myBid);
        em.flush();
    }

    public String getWarehouseLocation(Integer wId) {
        Warehouse myw = em.find(Warehouse.class, wId);
        return myw.getStreet() + "," + myw.getCity() + "," + myw.getCountry();
    }

    public String getWarehouseName(Integer wId) {
        Warehouse myw = em.find(Warehouse.class, wId);
        return myw.getName();
    }
}
