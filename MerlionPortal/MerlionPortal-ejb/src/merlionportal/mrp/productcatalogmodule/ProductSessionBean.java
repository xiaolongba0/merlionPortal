package merlionportal.mrp.productcatalogmodule;

import entity.Product;
//import entity.SystemUser;
//import entity.Venue;
//import java.sql.Timestamp;
import java.util.List;
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

   /* public List<Event> getMyEvents(Long systemUserId) {
        SystemUser systemUser = getSystemUser(systemUserId);
        Query query = entityManager.createQuery("SELECT e FROM Event e WHERE e.systemUser = :inSytemUser");
        query.setParameter("inSytemUser", systemUser);
        return query.getResultList();
    }*/

        public Integer addNewProduct(String productName, String description, String productType, String currency, Double price/*, Integer companyId, Integer componentId*/) {

       // Company company = new Company(companyId);
        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setProductType(productType);
        product.setCurrency(currency);
        product.setPrice(price);
        
        //hard code company id
       // product.setCompanyId(12345);
       // product.setComponent(getComponent(componentId));
           // product.setCompany(company);
 

        entityManager.persist(product);
        entityManager.flush();

      // company.getProductList().add(product);
      //  entityManager.merge(company);
        return product.getProductId();

    }
}
