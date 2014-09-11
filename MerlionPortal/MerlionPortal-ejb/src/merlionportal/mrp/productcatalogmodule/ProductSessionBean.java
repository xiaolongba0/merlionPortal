package merlionportal.mrp.productcatalogmodule;

import entity.Product;
//import entity.SystemUser;
//import entity.Venue;
//import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import util.exception.VenueConflictException;

@Stateless
@LocalBean
public class ProductSessionBean {

    @PersistenceContext
    private EntityManager entityManager;

 /*   public List<Venue> getAllVenues() {
        Query query = entityManager.createQuery("SELECT v FROM Venue v");
        return query.getResultList();
    }

   // private Venue getVenue(Long venueId) {
    //    Venue venue = entityManager.find(Venue.class, venueId);
   //     return venue;
   // }

    private SystemUser getSystemUser(Long systemUserId) {
        SystemUser systemUser = entityManager.find(SystemUser.class, systemUserId);
        return systemUser;
    }*/

    public List<Product> getAllProducts() {
        Query query = entityManager.createQuery("SELECT e FROM Product e");
        return query.getResultList();
    }

    
    // Notes: take in companyId automatically after Login, later change to getMyProducts(Integer companyId)
     public List<Product> getMyProducts(Integer companyId){
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyId);
        return query.getResultList();
    }
     
  
     
//Return a selected product for user to view
         public Product getAProduct(Integer companyID, Integer productID){
             
            Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
            query.setParameter("inCompanyId", companyID);
            List<Product> allmyproducts = query.getResultList();
       
         for(Product p: allmyproducts){
           if(Objects.equals(p.getProductId(), productID)){
               return p;
           }
       }
        return null;
    }
     
      
      
     public Boolean deleteProducts(Integer companyID, Integer productID){
       
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyID);
        List<Product> allmyproducts = query.getResultList();
       
         for(Product p: allmyproducts){
           if(Objects.equals(p.getProductId(), productID)){
               entityManager.remove(p);
               entityManager.flush(); 
               return true;
           }
       }
        return false;
     }
     

        public Integer addNewProduct(String productName, String description, String productType, String currency, Double price, Integer companyId/*, Integer companyId, Integer componentId*/) {

       // Company company = new Company(companyId);
        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setProductType(productType);
        product.setCurrency(currency);
        product.setPrice(price);
        product.setCompanyId(companyId);
        
       // product.setComponent(getComponent(componentId));
           // product.setCompany(company);
 

        entityManager.persist(product);
        entityManager.flush();

      // company.getProductList().add(product);
      //  entityManager.merge(company);
        return product.getProductId();

    }
        
        
        public Integer editProduct(String productName, String description, String productType, String currency, Double price, Integer companyID, Integer productID/*, Integer companyId, Integer componentId*/) {

        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyID);
        List<Product> allmyproducts = query.getResultList();
       Product pdt = new Product();
         for(Product p: allmyproducts){
           if(Objects.equals(p.getProductId(), productID)){
               pdt = p;
             }
       }
     
       // Company company = new Company(companyId);
        pdt.setProductName(productName);
        pdt.setDescription(description);
        pdt.setProductType(productType);
        pdt.setCurrency(currency);
        pdt.setPrice(price);
     
        
       // product.setComponent(getComponent(componentId));
           // product.setCompany(company);
 

        entityManager.merge(pdt);
        entityManager.flush();

      // company.getProductList().add(product);
      //  entityManager.merge(company);
        return pdt.getProductId();

    }
        
        

    public void deleteProducts(Integer companyId, Double productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
