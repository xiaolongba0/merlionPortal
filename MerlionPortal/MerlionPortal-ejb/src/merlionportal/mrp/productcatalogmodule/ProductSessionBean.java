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
    private Product product;

    public Boolean ifCanAdd(String productName) {
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.productName = :inProductName");
        query.setParameter("inProductName", productName);
        List<Product> productTemp = query.getResultList();
        if (productTemp.isEmpty()) {
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
        List<Component> componentss;
        for (Product p : allmyproducts) {
            if (Objects.equals(p.getProductId(), productID)) {
                componentss = p.getComponentList();
                entityManager.remove(p);
                entityManager.flush();
                for (Component c : componentss) {
                    entityManager.remove(c);
                    entityManager.flush();
                }
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    public Boolean deleteComponents(Integer componentID) {

        Component componentt = entityManager.find(Component.class, componentID);
        Product productt = componentt.getProductproductId();
        productt.getComponentList().remove(componentt);
        entityManager.merge(productt);
        entityManager.flush();

        return true;
    }

    public Integer addNewProduct(String productName, String description, String category, String productType, String currency, Double price, Integer companyId) {

        product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setCategory(category);
        product.setProductType(productType);
        product.setCurrency(currency);
        product.setPrice(price);
        product.setCompanyId(companyId);

        componentList = new ArrayList<Component>();
        product.setComponentList(componentList);

        entityManager.persist(product);
        entityManager.flush();

        return product.getProductId();

    }

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

        entityManager.persist(component);
        entityManager.flush();

        pdt.getComponentList().add(component);
        entityManager.merge(pdt);
        entityManager.flush();

        return component.getComponentId();

    }

    public Integer editProduct(String productName, String description, String category, String productType, String currency, Double price, Integer companyID, Integer productID) {

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

    //Edit a component
    public Integer editComponent(String componentName, String description, Double cost, String currency, Integer quantity, Integer leadTime, int supplierCompanyId, String contactPerson, String contactNumber, String contactEmail, Integer companyID, Integer productID, Integer componentID) {

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

        entityManager.merge(component);
        entityManager.flush();

        return component.getComponentId();
    }

    public void deleteProducts(Integer companyId, Double productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
