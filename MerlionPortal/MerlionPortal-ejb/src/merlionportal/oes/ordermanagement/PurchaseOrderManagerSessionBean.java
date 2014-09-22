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

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class PurchaseOrderManagerSessionBean {

    @PersistenceContext
    private EntityManager em;

    private ProductOrder po;
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

    public ProductOrder createPO(String shipto,int companyId, int customerId, 
            int quotationId, String cName, String cNumber) {
        System.out.println("+++++++++++++++++++GeneratePO Start====================");
        po = new ProductOrder();
        Date date = new Date();
        po.setCreatedDate(date);
        List<ProductOrderLineItem> itemLiest = new ArrayList();
        po.setProductOrderLineItemList(itemLiest);
        po.setStatus(1);
        po.setShipTo(shipto);
        po.setCompanyId(companyId);
        po.setQuotationId(quotationId);
        po.setCreatorId(customerId);
        em.persist(po);
        em.flush();
        System.out.println("Finished generating PO  ********");
        return po;

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

    public void rejectPo(ProductOrder mPo) {
        mPo.setStatus(2);
    }

    public void generateSo(ProductOrder mpo) {
        mpo.setStatus(3);
    }

    public Boolean creaditCheck(int customerId) {
        SystemUser myCustomer = em.find(SystemUser.class, customerId);
        String myCredit = myCustomer.getCredit();
        if(myCredit.equalsIgnoreCase("fail")) {
            return false;
        }
        return true;
    }
    
    public Boolean checkAvailavility(){
        return true;
    }
    public String checkTimeToFulfill(){
        return "Need 5 days";
    }
    
    public List<String> getCustomerCompany(int userid){
        SystemUser cus=em.find(SystemUser.class, userid);
        Company com=cus.getCompanycompanyId();
        List<String> result=new ArrayList();
        result.add(com.getName());
        result.add(com.getAddress());
        result.add(com.getContactPersonName());
        result.add(com.getContactNumber());
        return result;
    }
    
    public List<ProductOrderLineItem> copyFromQuotation(int quotationId){
        List<ProductOrderLineItem> poList = new ArrayList();
        quotation=em.find(Quotation.class,quotationId);
        List<QuotationLineItem> qList=new ArrayList();
        qList = quotation.getQuotationLineItemList();
        for(Object o: qList){
            ProductOrderLineItem poLineItem = new ProductOrderLineItem();
            QuotationLineItem oLineItem=(QuotationLineItem)o;
            poLineItem.setPrice(oLineItem.getLineItemPrice());
            poLineItem.setProductproductId(oLineItem.getProductproductId());
            poLineItem.setStatus("valid");
            poList.add(poLineItem);
        }
            return poList;
        }
    
    public void saveOrder(ProductOrder mypo){
        mypo.setStatus(4);
        em.merge(mypo);
    }

}