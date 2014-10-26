/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.accountmanagementmodule;

import entity.Company;
import entity.CustomerAccount;
import entity.ServicePO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class KeyAccountManagementSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    public int createKeyAccount(Integer myCompanyId, Integer customerCompanyId, String text) {
//        Does not have such a customer
        Query q = em.createQuery("SELECT c FROM CustomerAccount c WHERE c.customerCompanyId = :customerCompanyId AND c.myCompanyId = :myCompanyId");
        q.setParameter("customerCompanyId", customerCompanyId);
        q.setParameter("myCompanyId", myCompanyId);

        if (q.getResultList().isEmpty()) {
            return -1;
        } else {
            CustomerAccount customerAcc = (CustomerAccount) q.getResultList().get(0);
            if (customerAcc.getKeyAccount()) {
                //This customer is alr a key account holder
                return 0;
            } else {
                //Success
                customerAcc.setKeyAccount(true);
                customerAcc.setRemarks(text);
                em.merge(customerAcc);
                em.flush();
                return 1;
            }
        }

    }

    public boolean updateKeyAccount(Integer myCompanyId, Integer customerCompanyId, String text) {
        Query q = em.createQuery("SELECT c FROM CustomerAccount c WHERE c.customerCompanyId = :customerCompanyId AND c.myCompanyId = :myCompanyId");
        q.setParameter("customerCompanyId", customerCompanyId);
        q.setParameter("myCompanyId", myCompanyId);

        CustomerAccount cust = (CustomerAccount) q.getSingleResult();
        cust.setRemarks(text);
        em.merge(cust);
        em.flush();
        return true;

    }

    public boolean deleteKeyAccount(Integer myCompanyId, Integer customerCompanyId) {
        Query q = em.createQuery("SELECT c FROM CustomerAccount c WHERE c.customerCompanyId = :customerCompanyId AND c.myCompanyId = :myCompanyId");
        q.setParameter("customerCompanyId", customerCompanyId);
        q.setParameter("myCompanyId", myCompanyId);

        CustomerAccount cust = (CustomerAccount) q.getSingleResult();
        cust.setKeyAccount(false);
        cust.setRemarks("");

        em.merge(cust);
        em.flush();
        return true;

    }

    public List<Double> calculateOrderAmountForAllCompanyies(Date startDate, Date endDate, Integer myCompId) {
        Query q = em.createNamedQuery("ServicePO.findByReceiverCompanyId").setParameter("receiverCompanyId", myCompId);
        q.getResultList();

        System.out.println("StarDate: " +startDate + " End Date: " + endDate);
        if (!q.getResultList().isEmpty()) {
            List<ServicePO> servicePOList = (List<ServicePO>) q.getResultList();
            //Keep a list of customer company id
            List<Integer> companyList = new ArrayList<>();
            //Keep a list of customer order amount sum
            List<Double> amtList = new ArrayList<>();

            for (ServicePO po : servicePOList) {
                //Validate the date and status of order
                if (po.getCreatedDate().after(startDate) && po.getCreatedDate().before(endDate) && po.getStatus() != 2 && po.getStatus() != 4) {
                    //Get the customer id of this order
                    Integer customerCompanyId = po.getSenderCompanyId();

                    //If can find this id in customer id list
                    if (companyList.contains(customerCompanyId)) {
                        //index of the found customer id 
                        int index = companyList.indexOf(customerCompanyId);
                        //Add order amt in the right position
                        amtList.set(index, amtList.get(index) + po.getPrice());

                    } else {
                        //cannot find list in the customer id list
                        companyList.add(customerCompanyId);
                        amtList.add(po.getPrice());
                    }
                }
            }
            return amtList;

        } else {
            return null;
        }
    }

    public List<String> produceCompanyList(Date startDate, Date endDate, Integer myCompId) {
        Query q = em.createNamedQuery("ServicePO.findByReceiverCompanyId").setParameter("receiverCompanyId", myCompId);
        q.getResultList();

        if (!q.getResultList().isEmpty()) {
            List<ServicePO> servicePOList = (List<ServicePO>) q.getResultList();
            //Keep a list of customer company id
            List<Integer> companyList = new ArrayList<>();
            //Keep a list of customer order amount sum
            List<String> companyNameList = new ArrayList<>();

            for (ServicePO po : servicePOList) {
                //Validate the date and status of order
                if (po.getCreatedDate().after(startDate) && po.getCreatedDate().before(endDate) && po.getStatus() != 2 && po.getStatus() != 4) {
                    //Get the customer id of this order
                    Integer customerCompanyId = po.getSenderCompanyId();

                    //If can find this id in customer id list
                    if (companyList.contains(customerCompanyId)) {

                    } else {
                        companyList.add(customerCompanyId);
                    }
                }
            }

            for (Integer customerCompanyid : companyList) {
                Company company = em.find(Company.class, customerCompanyid);
                companyNameList.add(company.getName() + " ID: " + customerCompanyid);
            }
            return companyNameList;

        } else {
            return null;
        }
    }

    public CustomerAccount searchAValidCustomer(Integer customerId, Integer myCompanyId) {
        Query q = em.createQuery("SELECT c FROM CustomerAccount c WHERE c.customerCompanyId = :customerCompanyId AND c.myCompanyId = :myCompanyId");
        q.setParameter("customerCompanyId", customerId);
        q.setParameter("myCompanyId", myCompanyId);

        if (!q.getResultList().isEmpty()) {
            CustomerAccount customerAccount = (CustomerAccount) q.getSingleResult();

            return customerAccount;

        }
        return null;
    }
    
    public List<CustomerAccount> viewAllKeyAccounts(Integer myCompanyId){
        Query q = em.createQuery("SELECT c FROM CustomerAccount c WHERE c.myCompanyId = :myCompanyId AND c.keyAccount = :keyAccount");
        q.setParameter("keyAccount", true);
        q.setParameter("myCompanyId", myCompanyId);
        return (List<CustomerAccount>)q.getResultList();
    }
}
