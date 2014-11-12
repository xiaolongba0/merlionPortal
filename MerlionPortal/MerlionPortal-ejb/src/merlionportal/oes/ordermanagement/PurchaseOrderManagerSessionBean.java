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
        mypo.setPrice(mypo.getPrice() + pLine.getQuantity() * pLine.getPrice());
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
        List<ProductOrderLineItem> myList = new ArrayList();
        mpo.setStatus(2);
        mpo.setSalesPersonId(operator);
        int cId = mpo.getCreatorId();
        SystemUser myCustomer = em.find(SystemUser.class, cId);
        int credit = myCustomer.getCreditLimit() - mpo.getPrice().intValue();
        myCustomer.setCreditLimit(credit);
        em.merge(myCustomer);
        em.merge(mpo);
        em.flush();
    }

    public Boolean checkCredit(int cutomerId, ProductOrder mpo) {
        Boolean result = true;
        SystemUser customer = em.find(SystemUser.class, cutomerId);
        int credit = customer.getCreditLimit();
        if (mpo.getPrice() > credit) {
            result = false;
        }
        return result;
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

    public String getMyQuotationCurrency(Integer quotationId) {
        if(quotationId==null){
            return "";
        }
        quotation = em.find(Quotation.class, quotationId);
        return quotation.getCurrency();
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
        mypo.setStatus(14);
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

    public Boolean checkQuotationValidity(int quoationId) {
        Boolean result;
        Quotation myQuotation = em.find(Quotation.class, quoationId);
        if (myQuotation == null) {
            System.out.println("myQuotation is null");
            return false;
        } else if (myQuotation.getStatus() == 3) {
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
        return result;
    }

    public String viewMyOrderStatus(int status) {
        String result;
        if (status == 1) {
            result = "PO, Waiting for process";
        } else if (status == 2) {
            result = "SO, Waiting for fulfillment";
        } else if (status == 3) {
            result = "Product Shipped";
        } else if (status == 4) {
            result = "Order closed";
        } else if (status == 5) {
            result = "Order invoiced";
        } else if (status == 6) {
            result = "Rejected Wrong product";
        } else if (status == 7) {
            result = "Rejected, Wrong product quantity";
        } else if (status == 8) {
            result = "Rejected, Wrong price";
        } else if (status == 9) {
            result = "Rejected, Wrong ship to address";
        } else if (status == 10) {
            result = "Rejected, Wrong contact person";
        } else if (status == 11) {
            result = "Rejected, Credit check fail";
        } else if (status == 12) {
            result = "Rejected, Others please contact sales for more information";
        } else if (status == 13) {
            result = "Rejected,Unable to fulfill this order";
        } else if (status == 14) {
            result = "Saved";
        } else {
            result = "Rejected, Customer request for cancelation";
        }
        return result;
    }

    public String getOrderStatus(ProductOrder myOrder) {
        String result;
        if (myOrder == null) {
            return " ";
        } else {
            int status = myOrder.getStatus();
            if (status == 1) {
                result = "PO, Waiting for process";
            } else if (status == 2) {
                result = "SO, Waiting for fulfillment";
            } else if (status == 3) {
                result = "Product Shipped";
            } else if (status == 4) {
                result = "Order closed";
            } else if (status == 5) {
                result = "Order invoiced";
            } else if (status == 6) {
                result = "Request for return, waiting for approval";
            } else if (status == 14) {
                result = "Saved";
            } else {
                result = "Rejected";
            }
        }
        return result;
    }

}
