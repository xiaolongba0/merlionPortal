/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oes;

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

    public List<String> getAllTypes() {
        List<String> types = new ArrayList();
        List<String> uniTypes = new ArrayList();
        Product p;
        for (Object o : this.displayAllProducts(1)) {
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
        Query q = em.createNamedQuery("Quotation.findByCompany").setParameter("company", companyId);
        for (Object o : q.getResultList()) {
            quotation = (Quotation) o;
            result.add(quotation);
        }
        return result;
    }

    public List<Quotation> viewAllRequestForQuotation(int companyId, int clientId) {
        List<Quotation> result = new ArrayList();
        Query q = em.createQuery("SELECT q FROM Quotation q WHERE q.company = :company ADN customerId := clientId");
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

    public void generateQuotation(Quotation myQuotation, String Description) {
        myQuotation.setDescription(Description);
        Date date = this.getCurrentTime();
        myQuotation.setCreateDate(date);
        myQuotation.setStatus(2);
        em.merge(myQuotation);

    }
    public Boolean checkPrice(Quotation myQuotation){
        List<QuotationLineItem> myLine;
        myLine=myQuotation.getQuotationLineItemList();
        for(Object o: myLine){
            QuotationLineItem q= (QuotationLineItem)o;
            if(q.getLineItemPrice()==null){
                return false;
            }
        }
        return true;
    }
    
    public void setLineItemPrice(QuotationLineItem myItem,Double myPrice){
        myItem.setLineItemPrice(myPrice);
        em.merge(myItem);
        
    }
    
    public void updateObject(){
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
                + "p.companyId =:companyId AND p.productId =:productId").getSingleResult();
        return result;
    }

    private Quotation findQuotation(int quotationId) {
        Quotation result = (Quotation) em.find(Quotation.class, quotationId);
        return result;
    }

}
