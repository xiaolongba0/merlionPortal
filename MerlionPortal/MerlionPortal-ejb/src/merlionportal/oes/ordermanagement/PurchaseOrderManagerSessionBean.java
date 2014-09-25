/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.ordermanagement;

import entity.Company;
import entity.ProductOrder;
import entity.ProductOrderLineItem;
import entity.Quotation;
import entity.QuotationLineItem;
import entity.SystemUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class PurchaseOrderManagerSessionBean {

    @PersistenceContext
    private EntityManager em;

    private ProductOrderLineItem pLineItem;
    private Quotation quotation;

    public PurchaseOrderManagerSessionBean() {
    }

    public Quotation searchQuotation(int quotationId, int customerId) {
        quotation = em.find(Quotation.class, quotationId);
        int cId = quotation.getCustomerId();
        if (cId == customerId) {
            return quotation;
        }
        return null;
    }

    public Integer createPO(String shipto, int companyId, int customerId, int quotationId, String cName, String cNumber) {
        System.out.println("+++++++++++++++++++GeneratePO Start====================");
        ProductOrder po = new ProductOrder();
        Date date = new Date();
        po.setCreatedDate(date);
        List<ProductOrderLineItem> itemLiest = new ArrayList();
        po.setProductOrderLineItemList(itemLiest);
        po.setStatus(1);
        po.setShipTo(shipto);
        po.setCompanyId(companyId);
        po.setQuotationId(quotationId);
        po.setCreatorId(customerId);
        po.setContactPersonName(cName);
        po.setContactPersonPhoneNumber(cNumber);
        po.setBillTo("");
        po.setPrice(0.0);
        po.setSalesPersonId(0);
        em.persist(po);
        System.out.println("+++++++++++++++++++GeneratePO Start=====3333===============");
        em.flush();
        return po.getProductPOId();

    }

    public void createProductList(ProductOrderLineItem pLine, ProductOrder mypo) {
        pLine.setProductOrderproductPOId(mypo);
        em.persist(pLine);
        mypo.getProductOrderLineItemList().add(pLine);
        em.merge(mypo);
        em.flush();
    }

    public void rejectLineItem(ProductOrderLineItem poLine, String reason) {
        poLine.setStatus(reason);
        poLine.setPrice(0.0);
        em.merge(poLine);
    }

    public void rejectPo(int operator, ProductOrder mPo, int reason) {
        mPo.setStatus(reason);
        mPo.setSalesPersonId(operator);
        em.merge(mPo);
    }

    public void generateSo(int operator, ProductOrder mpo) {
        mpo.setStatus(3);
        mpo.setSalesPersonId(operator);
        em.merge(mpo);
    }

    public Boolean creaditCheck(int customerId) {
        SystemUser myCustomer = em.find(SystemUser.class, customerId);
        String myCredit = myCustomer.getCredit();
        if (myCredit.equalsIgnoreCase("fail")) {
            return false;
        }
        return true;
    }

    public Boolean checkAvailavility() {
        return true;
    }

    public String checkTimeToFulfill() {
        return "Need 5 days";
    }

    public List<String> getCustomerCompany(int userid) {
        SystemUser cus = em.find(SystemUser.class, userid);
        Company com = cus.getCompanycompanyId();
        List<String> result = new ArrayList();
        result.add(com.getName());
        result.add(com.getAddress());
        result.add(com.getContactPersonName());
        result.add(com.getContactNumber());
        return result;
    }

    public List<ProductOrderLineItem> copyFromQuotation(int quotationId) {
        List<ProductOrderLineItem> poList = new ArrayList();
        quotation = em.find(Quotation.class, quotationId);
        List<QuotationLineItem> qList = new ArrayList();
        qList = quotation.getQuotationLineItemList();
        for (Object o : qList) {
            ProductOrderLineItem poLineItem = new ProductOrderLineItem();
            QuotationLineItem oLineItem = (QuotationLineItem) o;
            poLineItem.setPrice(oLineItem.getLineItemPrice());
            poLineItem.setProductproductId(oLineItem.getProductproductId());
            poLineItem.setStatus("valid");
            poList.add(poLineItem);
        }
        return poList;
    }

    public void saveOrder(ProductOrder mypo) {
        mypo.setStatus(4);
        em.merge(mypo);
    }

    public List<ProductOrder> viewAllProductOrder(int status, int companyId) {
        List<ProductOrder> resultList = new ArrayList();
        Query q = em.createQuery("SELECT q FROM ProductOrder q WHERE q.companyId = :companyId AND q.status = :mystatus").setParameter("companyId", companyId);
        q.setParameter("mystatus", status);
        for (Object o : q.getResultList()) {
            ProductOrder pro = (ProductOrder) o;
            resultList.add(pro);
        }
        return resultList;
    }

    public List<ProductOrder> viewAllProductOrder(int status, int companyId, int customerId) {
        List<ProductOrder> resultList = new ArrayList();
        Query q = em.createQuery("SELECT q FROM ProductOrder q WHERE q.companyId = :companyId AND q.status = :mystatus AND q.creatorId = :clientId").setParameter("companyId", companyId);
        q.setParameter("mystatus", status);
        q.setParameter("clientId", customerId);
        for (Object o : q.getResultList()) {
            ProductOrder pro = (ProductOrder) o;
            resultList.add(pro);
        }
        return resultList;
    }

    public List<ProductOrder> viewAllOrder(int companyId, int customerId) {
        List<ProductOrder> resultList = new ArrayList();
        Query q = em.createQuery("SELECT q FROM ProductOrder q WHERE q.companyId = :companyId AND q.creatorId = :clientId").setParameter("companyId", companyId);
        q.setParameter("clientId", customerId);
        for (Object o : q.getResultList()) {
            ProductOrder pro = (ProductOrder) o;
            resultList.add(pro);
        }
        return resultList;
    }

    public List<ProductOrder> viewAllOrder(int companyId) {
        List<ProductOrder> resultList = new ArrayList();
        Query q = em.createQuery("SELECT q FROM ProductOrder q WHERE q.companyId = :companyId").setParameter("companyId", companyId);

        for (Object o : q.getResultList()) {
            ProductOrder pro = (ProductOrder) o;
            resultList.add(pro);
        }
        return resultList;
    }

    public ProductOrder retrieveProductOrder(Integer poId) {
        ProductOrder po = new ProductOrder();
        po = em.find(ProductOrder.class, poId);

        return po;
    }

    public Boolean checkCredit(int cutomerId) {
        SystemUser customer = em.find(SystemUser.class, cutomerId);
        String credit = customer.getCredit();
        if (credit.equals("pass")) {
            return true;
        }
        return false;
    }

    public List<String> getAllOrderStatus() {
        List<String> result = new ArrayList();
        result.add("PO, Waiting for process");
        result.add("SO, Waiting for fulfillment");
        result.add("Product Shipped");
        result.add("Order invoiced");
        result.add("Order closed");
        result.add("Request for return, waiting for approval");
        result.add("Rejected");
//        result.add("Rejected, Wrong product");
//        result.add("Rejected, Wrong product quantity");
//        result.add("Rejected, Wrong price ");
//        result.add("Rejected, Wrong ship to address");
//        result.add("Rejected, Wrong contact person");
//        result.add("Rejected, Credit check fail");
//        result.add("Rejected, Others please contact sales for more information");
//        result.add("Rejected, Customer request for cancelation");
//        result.add("Rejected, Unable to fulfill this order");
        return result;
    }

    public String getOrderStatus(ProductOrder myOrder) {
        String result;
        int status = myOrder.getStatus();
        if (status == 1) {
            result = "PO, Waiting for process";
        }
        else if (status == 2) {
            result = "SO, Waiting for fulfillment";
        }
        else if (status == 3) {
            result = "Product Shipped";
        }
        else if (status == 4) {
            result = "Order closed";
        }
        else if (status == 5) {
            result = "Order invoiced";
        }
        else if (status == 6) {
            result = "Request for return, waiting for approval";
        } else {
            result = "Rejected";
        }
        return result;
    }

}
