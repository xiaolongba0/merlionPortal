/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oes;

import entity.Product;
import entity.Quotation;
import entity.QuotationLineItem;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

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

    public Product searchForProductById(int productId, int companyId) {
        Query q = em.createQuery("SELECT p FROM Product p WHERE "
                + "p.productId = :productId AND p.companyId = :companyId");
        product = (Product) q.getSingleResult();
        return product;
    }

    public List<Product> searchProductByName(int companyId, String productName) {
        List<Product> result = new ArrayList();
        List<Product> allProduct = this.displayAllProducts(companyId);
        for (Object o : allProduct) {
            Product p = (Product) o;
            if (this.checkContains(p.getProductName(), productName)) {
                result.add(p);
            }
        }
        return result;
    }

    public void requestForQuotation(int companyId, int customerId, String description) {
       
            quotation = new Quotation();
            quotation.setCompany(companyId);
            quotation.setCustomerId(customerId);
            quotation.setStatus(1);//stage 1 request for quoation;
             List<QuotationLineItem> itemList=new ArrayList();//the list is empty
            quotation.setQuotationLineItemList(itemList);
            quotation.setDescription(description);
            Calendar cal = Calendar.getInstance();
            Timestamp currentDate = (Timestamp) cal.getTime();
            quotation.setCreateDate(currentDate);
            em.persist(quotation);
        
    }

    public Boolean createLineItem(int companyId,int productId, Quotation myQuotation) {

        lineItem = new QuotationLineItem();
        product = this.findProduct(companyId,productId);
        lineItem.setProductproductId(product);
        lineItem.setQuotationquotationId(myQuotation);
        em.merge(myQuotation);
        myQuotation.getQuotationLineItemList().add(lineItem);
        em.persist(lineItem);
        em.persist(myQuotation);
        return true;
    }
    
    public List<Quotation> viewAllRequestForQuotation(int companyId){
       List<Quotation> result= new ArrayList();
       Query q=em.createQuery("SELECT q FROM Quotation q WHERE q.company = :companyId AND q.status =:2");
       for(Object o: q.getResultList()){
           quotation = (Quotation)o;
           result.add(quotation);
       }
       return result;
    }
        public List<Quotation> viewAllRequestForQuotation(int companyId,int clientId){
       List<Quotation> result= new ArrayList();
       Query q=em.createQuery("SELECT q FROM Quotation q WHERE q.company = :companyId "
               + "AND q.status =:2 AND q.customerId =:clientId");
       for(Object o: q.getResultList()){
           quotation = (Quotation)o;
           result.add(quotation);
       }
       return result;
    }
    
    
    public Boolean deleteRequest(int quotationId){
        quotation=this.findQuotation(quotationId);
        if(quotation!=null){
        em.remove(this.findQuotation(quotationId));
        return true;
        }
        return false;
    }


    //======================= Private Functions=================================

    private Boolean checkContains(String mainString, String subString) {
        Boolean result = false;
        result = mainString.toLowerCase().contains(subString.toLowerCase());
        return result;
    }

    private Product findProduct(int companyId, int productId) {
         Product result = (Product) em.createQuery("SELECT p FROM Product p WHERE "
                + "p.companyId =:companyId AND p.productId =:productId").getSingleResult();
        return result;
    }
    
    private Quotation findQuotation(int quotationId){
        Quotation result = (Quotation)em.find(Quotation.class, quotationId);
        return result;
    }

}
