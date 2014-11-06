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
import entity.Quotation;
import entity.QuotationLineItem;
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
        System.out.println("FY 6NOV productid: " + product.getProductName());
        Company company = entityManager.find(Company.class, companyId);
        System.out.println("FY 6NOV companyid: " + company.getName());
        List<ProductOrder> pos = new ArrayList<ProductOrder>();

        int size = product.getComponentList().size();
        System.out.println("FY 6NOV size: " + size);

        for (int i = 0; i < size; i++) {
            if (mrps.get(i).getPlannedOrder1() == 0) {

            } else {
                System.out.println("FY 6NOV loop how many times");
                ProductOrder po = new ProductOrder();
                Date date = new Date();
                po.setCreatedDate(date);
                List<ProductOrderLineItem> itemList = new ArrayList();
                po.setProductOrderLineItemList(itemList);
                po.setStatus(1);
                po.setShipTo(company.getAddress()); //retrieve later
                System.out.println("----------supplier company id" + product.getComponentList().get(i).getSupplierCompanyId());
                po.setCompanyId(product.getComponentList().get(i).getSupplierCompanyId());//retrieve later, supplier's one
                System.out.println("FY 6NOV supplier company id: " + product.getComponentList().get(i).getSupplierCompanyId());
                po.setQuotationId(1); //retrieve later
                po.setCreatorId(loginedUser.getSystemUserId()); //retrieve later
                po.setContactPersonName(loginedUser.getFirstName()); //retrieve later
                po.setContactPersonPhoneNumber(loginedUser.getContactNumber());//retrieve later
                po.setBillTo(company.getAddress());
                po.setPrice(product.getComponentList().get(i).getCost());
                System.out.println("FY 6NOV supplier company id: " + product.getComponentList().get(i).getCost());
                po.setSalesPersonId(product.getComponentList().get(i).getSupplierCompanyId());
                System.out.println("FY 6NOV sales person id: " + product.getComponentList().get(i).getSupplierCompanyId());
                entityManager.persist(po);
                entityManager.flush();

                ProductOrderLineItem pLine = new ProductOrderLineItem();
                pLine.setStatus("Ready for delivery");
                pLine.setQuantity(mrps.get(i).getPlannedOrder1());
                System.out.println("FY 6NOV quantity: " + mrps.get(i).getPlannedOrder1());
                pLine.setPrice(mrps.get(i).getPlannedOrder1() * product.getComponentList().get(i).getCost());
                System.out.println("FY 6NOV price: " + mrps.get(i).getPlannedOrder1() * product.getComponentList().get(i).getCost());
                pLine.setProductOrderproductPOId(po);

                //set the other company's product
                int tempId = product.getComponentList().get(i).getSupplierCompanyId();
                Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
                query.setParameter("inCompanyId", tempId);
                List<Product> products = query.getResultList();

                Product supplierProduct = new Product();

                for (int j = 0; j < products.size(); j++) {
                    if (products.get(j).getProductName().equals(product.getComponentList().get(i).getComponentName())) {
                        supplierProduct = entityManager.find(Product.class, products.get(j).getProductId());
                    }
                }
                /*        Product supplierProduct = new Product();
                 Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.productName = :inProductName");
                 query.setParameter("inProductName", product.getComponentList().get(i).getComponentName());
                 supplierProduct = (Product)query.getSingleResult();*/

                System.out.println("FY 6NOV supplier company name: " + supplierProduct.getProductName());
                pLine.setProductproductId(supplierProduct);
                entityManager.persist(pLine);
                //  entityManager.flush();
                itemList.add(pLine);
                System.out.println("FY 6NOV test get product name:" + pLine.getProductproductId().getProductName());

                po.getProductOrderLineItemList().add(pLine);
                entityManager.merge(po);
                entityManager.flush();
                pos.add(po);
            }
        }

        System.out.println("+++++++++++++++++++GeneratePO End====================3");

        return pos;

        /*System.out.println("+++++++++++++++++++GeneratePO Start====================1");
         Product product = entityManager.find(Product.class, productId);
         Company company = entityManager.find(Company.class, companyId);
         List<ProductOrder> pos = new ArrayList<ProductOrder>();

         int size = product.getComponentList().size();

         for (int i = 0; i < size; i++) {
         if (mrps.get(i).getPlannedOrder1() == 0) {

         } else {
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
         po.setPrice(product.getComponentList().get(i).getCost());
         po.setSalesPersonId(product.getComponentList().get(i).getSupplierCompanyId());
         entityManager.persist(po);
         entityManager.flush();

         ProductOrderLineItem pLine = new ProductOrderLineItem();
         pLine.setStatus("Ready for delivery");
         pLine.setQuantity(mrps.get(i).getPlannedOrder1());
         pLine.setPrice(mrps.get(i).getPlannedOrder1() * product.getComponentList().get(i).getCost());
         pLine.setProductOrderproductPOId(po);

         //set the other company's product
         int tempId = product.getComponentList().get(i).getSupplierCompanyId();
         Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
         query.setParameter("inCompanyId", tempId);
         List<Product> products = query.getResultList();

         Product supplierProduct = new Product();

         for (int j = 0; j < products.size(); j++) {
         if (products.get(j).getProductName().equals(product.getComponentList().get(i).getComponentName())) {
         supplierProduct = entityManager.find(Product.class, products.get(j).getProductId());
         }
         }

         pLine.setProductproductId(supplierProduct);
                
         itemList.add(pLine);

         po.setProductOrderLineItemList(itemList);
         po.setPrice(mrps.get(i).getPlannedOrder1() * product.getComponentList().get(i).getCost());
         entityManager.merge(po);
         entityManager.flush();
         pos.add(po);
         }
         }

         System.out.println("+++++++++++++++++++GeneratePO Start====================3");

         return pos;*/
    }

    public List<ProductOrder> getAllMyPOs(Integer companyId) {
        Company company = entityManager.find(Company.class, companyId);
        String addressTemp = company.getAddress();

        Query query = entityManager.createQuery("SELECT o FROM ProductOrder o WHERE o.billTo = :inBillTo");
        query.setParameter("inBillTo", addressTemp);
        return query.getResultList();
        //***Handle date

    }

    public Integer checkValidAccessUser(String userIDTemp, String password, Integer poReference) {
        ProductOrder productOrder = new ProductOrder();
        productOrder = entityManager.find(ProductOrder.class, poReference);
        List<ProductOrderLineItem> productOrderLineItemList = productOrder.getProductOrderLineItemList();
        int sizePOItems = productOrderLineItemList.size();


        Query query = entityManager.createQuery("SELECT q FROM Quotation q");
        List<Quotation> quotations = query.getResultList();
        int size = quotations.size();
        List<QuotationLineItem> quotationLineItems = new ArrayList<QuotationLineItem>();
        int size1;
        Quotation reqiredQuotation = new Quotation();

        //for each quotation
        for (int i = 0; i < size; i++) {
            //for each quotation item
            quotationLineItems = quotations.get(i).getQuotationLineItemList();
            size1 = quotationLineItems.size();
            //handle if two size are diff, just pass
            int count = 0;
            if (sizePOItems == size1) {
                for (int j = 0; j < size1; j++) {
                    for (int k = 0; k < size1; k++) {
                        if (quotationLineItems.get(j).getProductproductId().getProductName().equals(productOrderLineItemList.get(k).getProductproductId().getProductName())) {
                            count++;
                        }
                    }
                }

                if (count == size1) {
                    reqiredQuotation = quotations.get(i);
                    i = size;//end the loop
                }
            }
         }
        System.out.println("Quotation customer user id:" + reqiredQuotation.getCustomerId());

     
        //  Query q = entityManager.createQuery("SELECT s FROM SystemUser s WHERE s.emailAddress = :InEmailAddress AND s.password = :InPassword").setParameter("InEmailAddress", userIDTemp); q.setParameter("InPassword", password);
        Query q = entityManager.createQuery("SELECT s FROM SystemUser s WHERE s.emailAddress = :InEmailAddress");
        q.setParameter("InEmailAddress", userIDTemp);
        List<SystemUser> sysUsers = new ArrayList();
        SystemUser sysUser = new SystemUser();
        sysUsers = q.getResultList();
        if (sysUsers.isEmpty()) {
            return null;
        } else {
            sysUser = sysUsers.get(0);
        }
        System.out.println("sys user user id:" + sysUser.getSystemUserId());
        if(reqiredQuotation.getCustomerId().equals(sysUser.getSystemUserId())){
            return sysUser.getSystemUserId();
        }
        
        
        return null;
    }

    public List<ProductOrder> createPOBackorder(Integer productId, Integer companyId, SystemUser loginedUser, List<Mrp> mrps) {
        System.out.println("+++++++++++++++++++GeneratePO Start====================1");
        Product product = entityManager.find(Product.class, productId);
        Company company = entityManager.find(Company.class, companyId);
        List<ProductOrder> pos = new ArrayList<ProductOrder>();

        int size = product.getComponentList().size();

        for (int i = 0; i < size; i++) {
            if (mrps.get(i).getPlannedOrder1() == 0) {

            } else {
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

                for (int j = 0; j < products.size(); j++) {
                    if (products.get(j).getProductName().equals(product.getComponentList().get(i).getComponentName())) {
                        supplierProduct = entityManager.find(Product.class, products.get(j).getProductId());
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
