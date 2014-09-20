package merlionportal.mrp.productcatalogmodule;

import entity.Component;
import entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class ProductSessionBean {

    @PersistenceContext
    private EntityManager entityManager;
    private ArrayList<Component> componentList; //componentList refers to BOM of a product
    //  private ArrayList<Component> components; //components refers to list of components that a supplier offers
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
    
    public Boolean ifCanAdd(String productName){
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.productName = :inProductName");
        query.setParameter("inProductName", productName);
        List<Product> productTemp = query.getResultList();
        if(productTemp.isEmpty()){
            return true;
        }
        
        return false;
    }
    
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

    // Notes: take in companyId automatically after Login, later change to getMyProducts(Integer companyId)
    public List<Component> getComponentsForAProduct(Integer companyId, Integer productId) {
        Product productTemp = entityManager.find(Product.class, productId);
        return productTemp.getComponentList();
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

        componentList = new ArrayList<Component>();
        // product.setComponent(getComponent(componentId));
        // product.setCompany(company);

        product.setComponentList(componentList);

        entityManager.persist(product);
        entityManager.flush();

        // company.getProductList().add(product);
        //  entityManager.merge(company);
        return product.getProductId();

    }

    ////when adding a component, adding in supplier's details too
    //After suppliers account can be set up. this function searches through supplier's company Id, then set the id as the same as supplier's company Id
    ////do a actionlistener after product
    public Integer addNewComponent(String componentName, String description, Integer quantity, String currency, Double cost, Integer leadTime, int supplierCompanyId, String contactPerson, String contactNumber, String contactEmail, Integer companyId, Integer productId) {

        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");

        query.setParameter("inCompanyId", companyId);
        List<Product> allmyproducts = query.getResultList();
        Product pdt = new Product();
        for (Product p : allmyproducts) {
            if (Objects.equals(p.getProductId(), productId)) {
                pdt = p;
            }
        }
        Component component = new Component();
        component.setComponentName(componentName);
        component.setDescription(description);
        component.setQuantity(quantity);
        component.setCurrency(currency);
        component.setCost(cost);
        component.setLeadTime(leadTime);

        component.setSupplierCompanyId(supplierCompanyId);

        component.setSupplierContactPerson(contactPerson);
        component.setSupplierContactEmail(contactNumber);
        component.setSupplierContactNumber(contactEmail);
        component.setProductproductId(pdt);

        ///check later (19 sep)
        entityManager.persist(component);
        entityManager.flush();

        pdt.getComponentList().add(component);
        entityManager.merge(pdt);
        entityManager.flush();

        return component.getComponentId();

    }

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

        pdt.setProductName(productName);
        pdt.setDescription(description);
        pdt.setCategory(category);
        pdt.setProductType(productType);
        pdt.setCurrency(currency);
        pdt.setPrice(price);

  
        entityManager.merge(pdt);
        entityManager.flush();

        return pdt.getProductId();

    }

    ////Editting
    public Integer editComponent(String componentName, String description, Double cost, String currency, Integer quantity, Integer leadTime, int supplierCompanyId, String contactPerson, String contactNumber, String contactEmail,Integer companyID, Integer productID, Integer componentID) {

        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyID);
        List<Product> allmyproducts = query.getResultList();
        Product pdt = new Product();
        for (Product p : allmyproducts) {
            if (Objects.equals(p.getProductId(), productID)) {
                pdt = p;
            }
        }

        Component component = entityManager.find(Component.class, componentID);

        component.setComponentName(componentName);
        component.setDescription(description);
        component.setCost(cost);
        component.setCurrency(currency);
        component.setQuantity(quantity);
        component.setLeadTime(leadTime);
      
        component.setSupplierCompanyId(supplierCompanyId);

        component.setSupplierContactPerson(contactPerson);
        component.setSupplierContactEmail(contactNumber);
        component.setSupplierContactNumber(contactEmail);
        
        /*   component.getSuppliersupplierCompanyId().setContactPerson(contactPerson);
         component.getSuppliersupplierCompanyId().setContactNumber(contactNumber);
         component.getSuppliersupplierCompanyId().setDescription(supplierDescription);
         component.getSuppliersupplierCompanyId().setContactEmail(contactEmail);*/

        // product.setComponent(getComponent(componentId));
        // product.setCompany(company);
        entityManager.merge(component);
        entityManager.flush();

        // company.getProductList().add(product);
        //  entityManager.merge(company);
        return component.getComponentId();

    }

    public void deleteProducts(Integer companyId, Double productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
