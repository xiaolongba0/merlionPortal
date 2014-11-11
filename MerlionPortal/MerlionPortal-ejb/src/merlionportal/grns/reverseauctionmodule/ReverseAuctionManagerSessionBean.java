/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.grns.reverseauctionmodule;

import entity.Bid;
import entity.Company;
import entity.Contract;
import entity.Post;
import entity.ServicePO;
import entity.SystemUser;
import entity.Warehouse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

public class ReverseAuctionManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public ReverseAuctionManagerSessionBean() {
    }

    public List<ServicePO> getAllService(Integer companyId, String serviceType, Integer status) {
        Query q = em.createQuery("SELECT s FROM ServicePO s WHERE s.receiverCompanyId = :receiverCompanyId AND s.serviceType = :serviceType");
        q.setParameter("receiverCompanyId", companyId);
        q.setParameter("serviceType", "Transportation");
        List<ServicePO> returnedList = new ArrayList<>();
        for (Object o : q.getResultList()) {
            ServicePO po = (ServicePO) o;
            if (po.getStatus() == (int) status && (po.getPostList() == null || po.getPostList().isEmpty())) {
                returnedList.add(po);
            }
        }
        return returnedList;

    }

    public List<Contract> getAllContract(Integer companyId, String serviceType, Integer status) {
        Query q = em.createQuery("SELECT c FROM Contract c WHERE c.partyA = :partyA AND c.status=:status AND c.serviceType=:serviceType");
        q.setParameter("serviceType", serviceType);
        q.setParameter("status", status);
        q.setParameter("partyA", companyId);
        List<Contract> returnedList = new ArrayList<>();

        for (Object o : q.getResultList()) {
            Contract po = (Contract) o;
            if (po.getStatus() == (int) status && (po.getPostList() == null || po.getPostList().isEmpty())) {
                returnedList.add(po);
            }
        }
        return returnedList;
    }

    public Boolean compareAddress(ServicePO first, ServicePO second) {
        Contract firCon = first.getContract();
        Contract secCon = second.getContract();
        String des1 = firCon.getDestination();
        String des2 = secCon.getDestination();
        String ori1 = firCon.getOrigin();
        String ori2 = secCon.getOrigin();

        String[] region1 = ori1.split("\\^");
        String[] region2 = ori2.split("\\^");
        if (first.getServiceDeliveryDate().equals(second.getServiceDeliveryDate())) {
            if (des1.equalsIgnoreCase(des2)) {
                if (region1[3].equalsIgnoreCase(region2[3])) {
                    if (region1[2].equalsIgnoreCase(region2[2])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Boolean checkTAggregatability(List<ServicePO> myList) {
        ServicePO firstPO = myList.get(0);
        for (Object o : myList) {
            ServicePO p = (ServicePO) o;
            if (!this.compareAddress(firstPO, p) || !firstPO.getServiceDeliveryDate().equals(p.getServiceDeliveryDate())) {
                return false;
            }

        }
        return true;
    }

    public Boolean checkWAggregatability(List<Contract> myList) {
        Contract firstCon = myList.get(0);
        for (Object o : myList) {
            Contract p = (Contract) o;
            if (!firstCon.getStartDate().equals(p.getStartDate()) || !firstCon.getEndDate().equals(p.getEndDate())) {
                return false;
            } else if (!firstCon.getServiceQuotation().getServiceCatalog().getServiceCatalogId().equals(p.getServiceQuotation().getServiceCatalog().getServiceCatalogId())) {
                return false;
            }
        }
        return true;
    }

    public List<String> getAllOrigionRegion(List<ServicePO> myList) {
        List<String> results = new ArrayList();
        List<String> uniResults = new ArrayList();
        ServicePO myPO;
        for (Object o : myList) {
            myPO = (ServicePO) o;
            Contract contract = myPO.getContract();
            String origion = contract.getOrigin();
            String[] region1 = origion.split("\\^");
            String region2 = region1[2] + "," + region1[3];
            results.add(region2);

        }
        Set<String> uniqueGas = new HashSet(results);
        uniResults.addAll(uniqueGas);
        return uniResults;
    }

    public List<String> getAllDestinationRegion(List<ServicePO> myList) {
        List<String> results = new ArrayList();
        List<String> uniResults = new ArrayList();
        ServicePO myPO;
        for (Object o : myList) {
            myPO = (ServicePO) o;
            Contract contract = myPO.getContract();
            results.add(contract.getDestination());

        }
        Set<String> uniqueGas = new HashSet(results);
        uniResults.addAll(uniqueGas);
        return uniResults;
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public String getCompanyName(Integer companyId) {
        Company mycom = em.find(Company.class, companyId);
        String result = mycom.getName();
        return result;
    }

    public String getORegion(ServicePO mypo) {
        if (mypo == null) {
            return "";
        }
        Contract mycon = mypo.getContract();
        String origin = mycon.getOrigin();
        System.out.println("origin *************" + origin);
        String[] region1 = origin.split("\\^");
        System.out.println("000000000000000000000" + region1[0]);
        String region2 = region1[2] + "," + region1[3];
        return region2;
    }

    public String getDRegion(ServicePO mypo) {
        if (mypo == null) {
            return "";
        }
        Contract mycon = mypo.getContract();
        String origion = mycon.getDestination();
        String[] region1 = origion.split("\\^");
        String region2 = region1[2] + "," + region1[3];
        return region2;
    }

    public void submitTPost(List<ServicePO> tList, String description, String destination, String origin,
            Date deliveryDate, String currency,
            Date expireDate, Integer systemU, Integer totalVolum) {
        List<Bid> bidList = new ArrayList();
        SystemUser systemUser = em.find(SystemUser.class, systemU);
        List<Post> myPostlist = new ArrayList();
        if (systemUser.getPostList() == null) {
            systemUser.setPostList(myPostlist);
        }
        Post newPost = new Post();
        newPost.setServiceType("Transportation");
        newPost.setDescription(description);
        newPost.setCurrency(currency);
        newPost.setDeliveryDate(deliveryDate);
        newPost.setDestination(destination);
        newPost.setOrigin(origin);
        Date todayDate = new Date();
        newPost.setPostDate(todayDate);
        newPost.setQuantityInTEU(totalVolum);
        newPost.setStatus("Valid");
        newPost.setExpireDate(expireDate);
        newPost.setSystemUser(systemUser);
        newPost.setServicePOList(tList);
        newPost.setBidList(bidList);
        em.persist(newPost);
        for (Object o : tList) {
            ServicePO p = (ServicePO) o;
            p.getPostList().add(newPost);
            em.merge(p);
        }
        systemUser.getPostList().add(newPost);
        em.merge(systemUser);
        em.flush();
    }
public void postTPost(String description, String destination, String origin,
            Date deliveryDate, String currency,
            Date expireDate, Integer systemU, Integer totalVolum) {
        List<Bid> bidList = new ArrayList();
        SystemUser systemUser = em.find(SystemUser.class, systemU);
        List<Post> myPostlist = new ArrayList();
        if (systemUser.getPostList() == null) {
            systemUser.setPostList(myPostlist);
        }
        Post newPost = new Post();
        newPost.setServiceType("Transportation Space");
        newPost.setDescription(description);
        newPost.setCurrency(currency);
        newPost.setDeliveryDate(deliveryDate);
        newPost.setDestination(destination);
        newPost.setOrigin(origin);
        Date todayDate = new Date();
        newPost.setPostDate(todayDate);
        newPost.setQuantityInTEU(totalVolum);
        newPost.setStatus("Valid");
        newPost.setExpireDate(expireDate);
        newPost.setSystemUser(systemUser);
        newPost.setBidList(bidList);
        em.persist(newPost);
        systemUser.getPostList().add(newPost);
        em.merge(systemUser);
        em.flush();
    }
    public void submitWPost(List<Contract> wList, String description, Date sStart, Date sEnd,
            Double wareSpace, String currency, Date expireDate, Integer sysUser) {
        SystemUser systemUser = em.find(SystemUser.class, sysUser);
        List<Post> myPostlist = new ArrayList();
        if (systemUser.getPostList() == null) {
            systemUser.setPostList(myPostlist);
        }
        Post newPost = new Post();
        List<Bid> bidList = new ArrayList();
        newPost.setServiceType("Warehouse");
        newPost.setDescription(description);
        newPost.setCurrency(currency);
        Date todayDate = new Date();
        newPost.setPostDate(todayDate);
        newPost.setStorageStartDate(sStart);
        newPost.setStorageEndDate(sEnd);
        newPost.setWarehouseSpace(wareSpace);
        newPost.setStatus("Valid");
        newPost.setExpireDate(expireDate);
        newPost.setContractList(wList);
        newPost.setSystemUser(systemUser);
        newPost.setBidList(bidList);
        em.persist(newPost);
        for (Object o : wList) {
            Contract p = (Contract) o;
            p.getPostList().add(newPost);
            em.merge(p);
        }
        systemUser.getPostList().add(newPost);
        em.merge(systemUser);
        em.flush();

    }
    public void postWPost(Integer warehouId,String description, Date sStart, Date sEnd,
            Double wareSpace, String currency, Date expireDate, Integer sysUser) {
        SystemUser systemUser = em.find(SystemUser.class, sysUser);
        List<Post> myPostlist = new ArrayList();
        if (systemUser.getPostList() == null) {
            systemUser.setPostList(myPostlist);
        }
        Post newPost = new Post();
        List<Bid> bidList = new ArrayList();
        newPost.setServiceType("Warehouse Space");
        newPost.setDescription(description);
        newPost.setCurrency(currency);
        Date todayDate = new Date();
        newPost.setPostDate(todayDate);
        newPost.setStorageStartDate(sStart);
        newPost.setStorageEndDate(sEnd);
        newPost.setWarehouseSpace(wareSpace);
        newPost.setStatus("Valid");
        newPost.setExpireDate(expireDate);
        newPost.setSystemUser(systemUser);
        newPost.setBidList(bidList);
        em.persist(newPost);
        systemUser.getPostList().add(newPost);
        em.merge(systemUser);
        em.flush();

    }

    public Boolean checkValidWarehouse(Integer wId, Integer companyId) {
        Warehouse myw = em.find(Warehouse.class, wId);
        if (myw.getCompanyId() == companyId) {
            return true;
        }
        return false;
    }

    public String getWarehouseName(Integer wId) {
        Warehouse myw = em.find(Warehouse.class, wId);
        return myw.getName();
    }
    public String getWarehouseLocation(Integer wId) {
        Warehouse myw = em.find(Warehouse.class, wId);
        return myw.getStreet()+","+myw.getCity()+","+myw.getCountry();
    }

}
