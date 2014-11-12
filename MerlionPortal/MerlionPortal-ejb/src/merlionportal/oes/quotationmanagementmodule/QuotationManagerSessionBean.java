/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.oes.quotationmanagementmodule;

import entity.Product;
import entity.Quotation;
import entity.QuotationLineItem;
import entity.SystemUser;
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
 * @author LI XIAMENG
 */
@Stateless
@LocalBean
public class QuotationManagerSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    
    private Product product;
    private Quotation quotation;
    private QuotationLineItem lineItem;
    
    public QuotationManagerSessionBean() {
    }
    public List<Product> displayAllProducts(int companyId) {
        List<Product> allProduct = new ArrayList<>();
        Query q = em.createNamedQuery("Product.findByCompanyId").setParameter("companyId", companyId);
        for (Object o : q.getResultList()) {
            product = (Product) o;
            allProduct.add(product);
        }

        return allProduct;
    }

    public List<String> getAllTypes(int companyId) {
        List<String> types = new ArrayList();
        List<String> uniTypes = new ArrayList();
        Product p;
        for (Object o : this.displayAllProducts(companyId)) {
            p = (Product) o;
            types.add(p.getCategory());

        }
        Set<String> uniqueGas = new HashSet(types);
        uniTypes.addAll(uniqueGas);

        return uniTypes;

    }

    public Quotation requestForQuotation(int companyId, int customerId) {
        System.out.println("GeneratedRequest Start====================");

        quotation = new Quotation();
        quotation.setCompany(companyId);
        quotation.setCustomerId(customerId);
        quotation.setStatus(1);//stage 1 request for quoation;
        List<QuotationLineItem> itemList = new ArrayList();//the list is empty
        quotation.setQuotationLineItemList(itemList);
        //quotation.setDescription(description);
        Date date = new Date();
        // Timestamp currentDate = new Timestamp(date.getTime());
        quotation.setCreateDate(date);
        em.persist(quotation);
        System.out.println("Finished generating request for quotation ********");
        return quotation;

    }

    public Date getCurrentTime() {
        Date date = new Date();
        return date;
    }

    public void createLineItem(int companyId, Product product, Quotation myQuotation) {

        lineItem = new QuotationLineItem();
        lineItem.setProductproductId(product);
        lineItem.setQuotationquotationId(myQuotation);
        em.merge(myQuotation);
        myQuotation.getQuotationLineItemList().add(lineItem);
        em.persist(lineItem);
        em.merge(myQuotation);
    }

    public List<Quotation> viewAllRequestForQuotation(int companyId) {
        List<Quotation> result = new ArrayList();
        Query q = em.createQuery("SELECT q FROM Quotation q WHERE q.company = :companyId AND q.status = :mystatus").setParameter("companyId", companyId);
        q.setParameter("mystatus", 1);
        for (Object o : q.getResultList()) {
            quotation = (Quotation) o;
            result.add(quotation);
        }
        return result;
    }

    public List<Quotation> viewAllRequestForQuotation(int companyId, int clientId) {
        List<Quotation> result = new ArrayList();
        Query q = em.createQuery("SELECT q FROM Quotation q WHERE q.company = :companyId AND q.customerId = :clientId AND q.status = :mystatus ").setParameter("clientId", clientId);
        q.setParameter("companyId", companyId);
        q.setParameter("mystatus", 1);

        for (Object o : q.getResultList()) {
            quotation = (Quotation) o;
            result.add(quotation);
        }
        return result;
    }

    public SystemUser findCustomer(int customerId) {
        SystemUser customer = (SystemUser) em.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", customerId).getSingleResult();
        return customer;
    }

    public Boolean deleteRequest(int quotationId) {
        quotation = this.findQuotation(quotationId);
        em.remove(quotation);
        return true;

    }

    public void generateQuotation(Quotation myQuotation, String Description,String currency) {
        myQuotation.setDescription(Description);
        Date date = this.getCurrentTime();
        myQuotation.setCreateDate(date);
        myQuotation.setCurrency(currency);
        myQuotation.setStatus(2);
        em.merge(myQuotation);
        em.flush();

    }

    public Boolean checkPrice(Quotation myQuotation) {
        List<QuotationLineItem> myLine;
        myLine = myQuotation.getQuotationLineItemList();
        for (Object o : myLine) {
            QuotationLineItem q = (QuotationLineItem) o;
            if (q.getLineItemPrice() == null) {
                return false;
            }
        }
        return true;
    }

    public void setLineItemPrice(Quotation myQuotation, QuotationLineItem myItem, Double myPrice) {
        myItem.setLineItemPrice(myPrice);
        em.merge(myQuotation);
        em.flush();

    }

    public List<Quotation> viewAllProQuotation(int companyId) {
        List<Quotation> result = new ArrayList();
        Query q = em.createQuery("SELECT q FROM Quotation q WHERE q.company = :companyId").setParameter("companyId", companyId);
        for (Object o : q.getResultList()) {
            quotation = (Quotation) o;
            if (quotation.getStatus() != 1) {
                result.add(quotation);
            }
        }
        return result;
    }

    public List<Quotation> viewAllProQuotation(int companyId, int clientId) {
        List<Quotation> result = new ArrayList();
        Query q = em.createQuery("SELECT q FROM Quotation q WHERE q.company = :companyId AND q.customerId = :clientId ").setParameter("companyId", companyId);
        q.setParameter("clientId", clientId);
        for (Object o : q.getResultList()) {
            quotation = (Quotation) o;
            if (quotation.getStatus() != 1) {
                result.add(quotation);
            }
        }
        return result;
    }

    public List<String> getAllStatus() {
        List<String> result = new ArrayList();
        result.add("Waiting for acception");
        result.add("Valid");
        result.add("Rejected");
        result.add("Canceled");
        return result;

    }

    public void acceptQuotation(Quotation selectedQuotation) {
        selectedQuotation.setStatus(3);
        em.merge(selectedQuotation);
        em.flush();
    }

    public void rejectQuotation(Quotation selectedQuotation) {
        selectedQuotation.setStatus(4);
        em.merge(selectedQuotation);
        em.flush();
    }

    public void cancelQuotation(Quotation selectedQuotation) {
        selectedQuotation.setStatus(5);
        em.merge(selectedQuotation);
        em.flush();
    }


    //======================= Private Functions=================================
    private Boolean checkContains(String mainString, String subString) {
        Boolean result = false;
        result = mainString.toLowerCase().contains(subString.toLowerCase());
        return result;
    }

    private Product findProduct(int companyId, int productId) {
        Product result = (Product) em.createQuery("SELECT p FROM Product p WHERE "
                + "p.companyId =:companyId AND p.productId =:productId").setParameter("productId", productId).getSingleResult();
        return result;
    }

    private Quotation findQuotation(int quotationId) {
        Quotation result = (Quotation) em.find(Quotation.class, quotationId);
        return result;
    }
    
}
