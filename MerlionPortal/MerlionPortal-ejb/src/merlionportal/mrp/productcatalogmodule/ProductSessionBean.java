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
 //   private ArrayList<Component> componentList;
    private Product product;

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
    public List<Product> getMyProducts(Integer companyId) {
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyId);
        return query.getResultList();
    }

//Return a selected product for user to view
    public Product getAProduct(Integer companyID, Integer productID) {

        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyID);
        List<Product> allmyproducts = query.getResultList();

        for (Product p : allmyproducts) {
            if (Objects.equals(p.getProductId(), productID)) {
                return p;
            }
        }
        return null;
    }

    public Boolean deleteProducts(Integer companyID, Integer productID) {

        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyID);
        List<Product> allmyproducts = query.getResultList();

        for (Product p : allmyproducts) {
            if (Objects.equals(p.getProductId(), productID)) {
                entityManager.remove(p);
                entityManager.flush();
                return true;
            }
        }
        return false;
    }

    public Integer addNewProduct(String productName, String description, String category, String productType, String currency, Double price, Integer companyId) {

        // Company company = new Company(companyId);
        product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
         product.setCategory(category);
        product.setProductType(productType);
        product.setCurrency(currency);
        product.setPrice(price);
        product.setCompanyId(companyId);

        ////editing...on 14 Sep 2014
    //    if (productType.equals("Manufacturing")) {
     //       componentList = new ArrayList<Component>();
     //   }

        // product.setComponent(getComponent(componentId));
        // product.setCompany(company);
        entityManager.persist(product);
        entityManager.flush();

        // company.getProductList().add(product);
        //  entityManager.merge(company);
        return product.getProductId();

    }

    ////editing...on 14 Sep 2014
    /*
    public Integer addNewComponent(String componentName, String description, Integer quantity, String currency, Double cost, Integer leadTime, Integer companyId, Integer productId) {
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");

        query.setParameter("inCompanyId", companyId);
        List<Product> allmyproducts = query.getResultList();
        Product pdt = new Product();
        for (Product p : allmyproducts) {
            if (Objects.equals(p.getProductId(), productId)) {
                pdt = p;
            }
        }
        // Company company = new Company(companyId);
        Component component = new Component();
        component.setDescription(description);
        component.setQuantity(quantity);
        component.setCurrency(currency);
        component.setCost(cost);
        component.setLeadTime(leadTime);
        component.setProductproductId(product);

        componentList.add(component);
        product.setComponentList(componentList);

//debug....use merge?
        entityManager.merge(pdt);
        entityManager.flush();

        return pdt.getProductId();

    }
    
    */

    public Integer editProduct(String productName, String description, String category, String productType, String currency, Double price, Integer companyID, Integer productID/*, Integer companyId, Integer componentId*/) {

        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyID);
        List<Product> allmyproducts = query.getResultList();
        Product pdt = new Product();
        for (Product p : allmyproducts) {
            if (Objects.equals(p.getProductId(), productID)) {
                pdt = p;
            }
        }

        // Company company = new Company(companyId);
        pdt.setProductName(productName);
        pdt.setDescription(description);
        pdt.setCategory(category);
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
