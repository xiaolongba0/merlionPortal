/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.mrp.pomodule;

import entity.Company;
import entity.Mrp;
import entity.Product;
import entity.ProductOrder;
import entity.ProductOrderLineItem;
import entity.SystemUser;
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
 * @author yao
 */
@Stateless
@LocalBean
public class PurchaseOrderSessionBean {

    
  @PersistenceContext
    private EntityManager entityManager;
  
  
      public List<ProductOrder> createPO(Integer productId, Integer companyId, SystemUser loginedUser, List<Mrp> mrps) {
        System.out.println("+++++++++++++++++++GeneratePO Start====================1");
        Product product = entityManager.find(Product.class, productId);
        Company company = entityManager.find(Company.class, companyId);
        List<ProductOrder> pos = new ArrayList<ProductOrder>();
        
        int size = product.getComponentList().size();
        
         for(int i=0; i<size; i++){
             if(mrps.get(i).getPlannedOrder1()  == 0){
                 
             }else{
        ProductOrder po = new ProductOrder();
        Date date = new Date();
        po.setCreatedDate(date);
        List<ProductOrderLineItem> itemList = new ArrayList();
        po.setProductOrderLineItemList(itemList);
        po.setStatus(1);
        po.setShipTo(company.getAddress()); //retrieve later
        System.out.println("----------supplier company id" + product.getComponentList().get(i).getSupplierCompanyId());
        po.setCompanyId(product.getComponentList().get(i).getSupplierCompanyId());//retrieve later, supplier's one
        po.setQuotationId(1); //retrieve later
        po.setCreatorId(loginedUser.getSystemUserId()); //retrieve later
        po.setContactPersonName(loginedUser.getFirstName()); //retrieve later
        po.setContactPersonPhoneNumber(loginedUser.getContactNumber());//retrieve later
        po.setBillTo(company.getAddress());
        po.setPrice(product.getComponentList().get(0).getCost());
        po.setSalesPersonId(product.getComponentList().get(0).getSupplierCompanyId());
        entityManager.persist(po);
        entityManager.flush();
        
       
        ProductOrderLineItem pLine = new ProductOrderLineItem();
        pLine.setStatus("Ready for delivery");
        pLine.setQuantity(mrps.get(i).getPlannedOrder1());
        pLine.setPrice(mrps.get(i).getPlannedOrder1() * product.getComponentList().get(0).getCost());
        pLine.setProductOrderproductPOId(po);
        
        //set the other company's product
        int tempId = product.getComponentList().get(i).getSupplierCompanyId();
     Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", tempId);
        List<Product> products = query.getResultList();
        
        Product supplierProduct = new Product();
         
        
        for(int j=0; j<products.size(); j++){
            if(products.get(j).getProductName().equals(product.getComponentList().get(i).getComponentName())){
                supplierProduct = entityManager.find(Product.class,products.get(j).getProductId());
            }
        }
   
        pLine.setProductproductId(supplierProduct);
        entityManager.persist(pLine);
        entityManager.flush();
        itemList.add(pLine);
        System.out.println("----test get product name:" + pLine.getProductproductId().getProductName());
         
         po.setProductOrderLineItemList(itemList);
         po.setPrice(mrps.get(i).getPlannedOrder1() * product.getComponentList().get(0).getCost());
         entityManager.merge(po);
         entityManager.flush();
         pos.add(po);
         }
         }
         
         System.out.println("+++++++++++++++++++GeneratePO Start====================3");
         
         
         
         
         return pos;

    }
      
      
       public List<ProductOrder> createPOBackorder(Integer productId, Integer companyId, SystemUser loginedUser, List<Mrp> mrps) {
        System.out.println("+++++++++++++++++++GeneratePO Start====================1");
        Product product = entityManager.find(Product.class, productId);
        Company company = entityManager.find(Company.class, companyId);
        List<ProductOrder> pos = new ArrayList<ProductOrder>();
        
        int size = product.getComponentList().size();
        
         for(int i=0; i<size; i++){
             if(mrps.get(i).getPlannedOrder1()  == 0){
                 
             }else{
        ProductOrder po = new ProductOrder();
        Date date = new Date();
        po.setCreatedDate(date);
        List<ProductOrderLineItem> itemList = new ArrayList();
        po.setProductOrderLineItemList(itemList);
        po.setStatus(1);
        po.setShipTo(company.getAddress()); //retrieve later
        System.out.println("----------supplier company id" + product.getComponentList().get(i).getSupplierCompanyId());
        po.setCompanyId(product.getComponentList().get(i).getSupplierCompanyId());//retrieve later, supplier's one
        po.setQuotationId(1); //retrieve later
        po.setCreatorId(loginedUser.getSystemUserId()); //retrieve later
        po.setContactPersonName(loginedUser.getFirstName()); //retrieve later
        po.setContactPersonPhoneNumber(loginedUser.getContactNumber());//retrieve later
        po.setBillTo(company.getAddress());
        po.setPrice(product.getComponentList().get(0).getCost());
        po.setSalesPersonId(product.getComponentList().get(0).getSupplierCompanyId());
        entityManager.persist(po);
        entityManager.flush();
        
       
        ProductOrderLineItem pLine = new ProductOrderLineItem();
        pLine.setStatus("Ready for delivery");
        pLine.setQuantity(mrps.get(i).getPlannedOrder1());
        pLine.setPrice(mrps.get(i).getPlannedOrder1() * product.getComponentList().get(0).getCost());
        pLine.setProductOrderproductPOId(po);
        
        //set the other company's product
        int tempId = product.getComponentList().get(i).getSupplierCompanyId();
     Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", tempId);
        List<Product> products = query.getResultList();
        
        Product supplierProduct = new Product();
         
        
        for(int j=0; j<products.size(); j++){
            if(products.get(j).getProductName().equals(product.getComponentList().get(i).getComponentName())){
                supplierProduct = entityManager.find(Product.class,products.get(j).getProductId());
            }
        }
   
        pLine.setProductproductId(supplierProduct);
        entityManager.persist(pLine);
        entityManager.flush();
        itemList.add(pLine);
        System.out.println("----test get product name:" + pLine.getProductproductId().getProductName());
         
         po.setProductOrderLineItemList(itemList);
         po.setPrice(mrps.get(i).getPlannedOrder1() * product.getComponentList().get(0).getCost());
         entityManager.merge(po);
         entityManager.flush();
         pos.add(po);
         }
         }
         
         System.out.println("+++++++++++++++++++GeneratePO Start====================3");
         
         
         
         
         return pos;

    }



}
