/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
import entity.Quotation;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import merlionportal.oes.ordermanagement.EditSavedOrderSessionBean;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mac
 */
@Named(value = "editSavedOrder")
@RequestScoped
public class EditSavedOrderManagedBean {
    
    @EJB
    private PurchaseOrderManagerSessionBean purchaseOrderMB;
    @EJB
    private EditSavedOrderSessionBean editSavedOrderMB;
    
    private Integer companyId;
    private Integer userId;
    private ProductOrder myOrder;
    private List<String> customerInfor;
    private String contactPerson;
    private String contactNumber;
    private String shipto;
    private List<ProductOrderLineItem> itemList;
    private int qutatuonId;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String country;
    private String postalCode;
    
    public EditSavedOrderManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            
            if (userId != null) {
                redirect = false;
            }
        }
        if (redirect) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Price Edited", ((ProductOrderLineItem) event.getObject()).getQuantity().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        ProductOrderLineItem testout = (ProductOrderLineItem) event.getObject();
        System.out.println("Set line item price" + testout.getQuantity());
    }
    
    public void onRowCancel(RowEditEvent event) {
//        QuotationLineItem myLine = (QuotationLineItem) event.getObject();
//        quotationMB.setLineItemPrice(selectedRequest, myLine, null);
        FacesMessage msg = new FacesMessage("Price Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
    }
    
    public void searchForQuotation() {
        itemList = purchaseOrderMB.copyFromQuotation(qutatuonId);
        
    }
    
    public void submitOrder() {
        Double price = 0.0;
        myOrder.setContactPersonName(contactPerson);
        myOrder.setContactPersonPhoneNumber(contactPerson);
        myOrder.setShipTo(shipto);
        myOrder.setCreatorId(userId);
        myOrder.setStatus(1);
        myOrder.setQuotationId(qutatuonId);
        Date date = new Date();
        myOrder.setCreatedDate(date);
        if (myOrder.getProductOrderLineItemList().isEmpty()) {
            for (Object o : itemList) {
                ProductOrderLineItem pLine = (ProductOrderLineItem) o;
                purchaseOrderMB.createProductList(pLine, myOrder);
            }
        } else {
            for (Object o : itemList) {
                ProductOrderLineItem pLine = (ProductOrderLineItem) o;
                editSavedOrderMB.updatePoLineItem(pLine);
            }
            
        }
        for (Object o : itemList) {
            ProductOrderLineItem pLine = (ProductOrderLineItem) o;
            price = +pLine.getPrice();
            
        }
        myOrder.setPrice(price);
        editSavedOrderMB.updateProductOrder(myOrder);
    }
    
    public Double setLineTotal(int quantity, double unitPrice){
        Double total= quantity * unitPrice;
        return total;
    }
    
    public Integer getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public ProductOrder getMyOrder() {
        myOrder = (ProductOrder) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOrder");
        return myOrder;
    }
    
    public void setMyOrder(ProductOrder myOrder) {
        this.myOrder = myOrder;
    }
    
    public List<String> getCustomerInfor() {
        customerInfor = purchaseOrderMB.getCustomerCompany(userId);
        return customerInfor;
    }
    
    public void setCustomerInfor(List<String> customerInfor) {
        this.customerInfor = customerInfor;
    }
    
    public String getContactPerson() {
        myOrder.getContactPersonName();
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getContactNumber() {
        myOrder.getContactPersonPhoneNumber();
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getShipto() {
        myOrder.getShipTo();
        return shipto;
    }
    
    public void setShipTo(String shipto) {
        this.shipto = shipto;
    }
    
    public void setMyShipto() {
        if (contactPerson.isEmpty() || contactPerson == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contact Person is required"));
        }
        if (contactNumber.isEmpty() || contactNumber == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contact Number is required"));
        }
        if (address1.isEmpty() || address1 == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Address is requiered"));
        }
        if (city.isEmpty() || city == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "City is required"));
        }
        if (country.isEmpty() || country == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Country Person is required"));
        }
        if (postalCode.isEmpty() || postalCode == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Postal Code is required"));
        }
        shipto = address1 + " " + address2 + " " + address3 + " " + "," + city + "," + country + " " + postalCode;
        System.out.println("Shipto Adress is not null" + shipto);
    }
    
    public List<ProductOrderLineItem> getItemList() {
        myOrder.getProductOrderLineItemList();
        return itemList;
    }
    
    public void setItemList(List<ProductOrderLineItem> itemList) {
        this.itemList = itemList;
    }
    
    public int getQutatuonId() {
        myOrder.getQuotationId();
        return qutatuonId;
    }
    
    public void setQutatuonId(int qutatuonId) {
        this.qutatuonId = qutatuonId;
    }
    
    public String getAddress1() {
        return address1;
    }
    
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    
    public String getAddress2() {
        return address2;
    }
    
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    public String getAddress3() {
        return address3;
    }
    
    public void setAddress3(String address3) {
        this.address3 = address3;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
}
