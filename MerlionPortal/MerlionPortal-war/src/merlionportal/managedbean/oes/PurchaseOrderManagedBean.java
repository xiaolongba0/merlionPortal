/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import entity.ProductOrderLineItem;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;
import merlionportal.oes.quotationmanagementmodule.CheckCustomerRoleSessionBean;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mac
 */
@Named(value = "purchaseOrderManagedBean")
@ViewScoped
public class PurchaseOrderManagedBean {

    @EJB
    private PurchaseOrderManagerSessionBean purchaseMB;
    @EJB
    private CheckCustomerRoleSessionBean ccrsb;

    private Integer companyId;
    private Integer userId;
    private List<String> customerInfor;
    private String contactPerson;
    private String contactNumber;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String country;
    private String shipto;
    private String postalCode;
    private List<ProductOrderLineItem> itemList;
    private int qutatuonId;
    private ProductOrder myPo;

    public PurchaseOrderManagedBean() {
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

    public void setShipto() {
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

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Price Edited", ((ProductOrderLineItem) event.getObject()).getQuantity().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
//        QuotationLineItem myLine = (QuotationLineItem) event.getObject();
//        quotationMB.setLineItemPrice(selectedRequest, myLine, null);
        FacesMessage msg = new FacesMessage("Price Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public boolean checkUserIsCustomer() {
        return ccrsb.checkUserIsCustomer(userId);
    }

    public void searchForQuotation() {
        itemList = purchaseMB.copyFromQuotation(qutatuonId);

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

    public List<String> getCustomerInfor() {
        customerInfor = purchaseMB.getCustomerCompany(userId);
        return customerInfor;
    }

    public void setCustomerInfor(List<String> customerInfor) {
        this.customerInfor = customerInfor;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<ProductOrderLineItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ProductOrderLineItem> itemList) {
        this.itemList = itemList;
    }

    public int getQutatuonId() {
        return qutatuonId;
    }

    public void setQutatuonId(int qutatuonId) {
        this.qutatuonId = qutatuonId;
    }

    public void saveOrder() {
        myPo = purchaseMB.createPO(shipto, companyId, userId, qutatuonId, contactPerson, contactNumber);
        for (Object o : itemList) {
            ProductOrderLineItem pLine = (ProductOrderLineItem) o;
            purchaseMB.createProductList(pLine, myPo);
        }
        purchaseMB.saveOrder(myPo);
    }

    public void submitOrder() {
        myPo = purchaseMB.createPO(shipto, companyId, userId, qutatuonId, contactPerson, contactNumber);
        for (Object o : itemList) {
            ProductOrderLineItem pLine = (ProductOrderLineItem) o;
            purchaseMB.createProductList(pLine, myPo);
        }
    }

}
