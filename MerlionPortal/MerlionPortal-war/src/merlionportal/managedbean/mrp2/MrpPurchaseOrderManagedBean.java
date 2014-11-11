/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.Mrp;
import entity.ProductOrder;
import entity.ProductOrderLineItem;
import entity.SystemUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.Query;
import merlionportal.ci.administrationmodule.LoginSessionBean;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.mrp.backordermodule.BackorderSessionBean;
import merlionportal.mrp.pomodule.PurchaseOrderSessionBean;
import merlionportal.oes.ordermanagement.PurchaseOrderManagerSessionBean;
import merlionportal.utility.MD5Generator;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author yao
 */
@Named(value = "mrpPurchaseOrderManagedBean")
@ViewScoped
public class MrpPurchaseOrderManagedBean {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    PurchaseOrderSessionBean productOrderSessionBean;
    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    LoginSessionBean loginSessionBean;

    private SystemUser loginedUser;
    private Integer companyId;
    private Integer productId;

    private List<ProductOrderLineItem> lineItemList;
    private List<ProductOrder> pos;
    private List<Mrp> mrps;

    @EJB
    private BackorderSessionBean backorderSB;
    @EJB
    private PurchaseOrderManagerSessionBean purchaseMB;
    @EJB
    private SystemAccessRightSessionBean systemAccessRightSB;
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
    private String userIDTemp;
    private Integer userIDID;

    private String password;
    private Boolean tempStatus;
    private int temp;
    private Integer poReference;
    private List<Integer> listOfSentPO;
    private List<ProductOrder> tempPOS;
    private List<Integer> tempToDeletePO;

    @PostConstruct
    public void init() {

        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            if (loginedUser != null) {
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
        productId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productId");
        mrps = (List<Mrp>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mrps");
        createPO();

    }

    public MrpPurchaseOrderManagedBean() {
    }

    public void createPO() {
        pos = productOrderSessionBean.createPO(productId, companyId, loginedUser, mrps);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pos", pos);
        systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "MRP create PO. ");
    }

    public void sendPO(Integer poReference) {
        //hash password
        HashMap<String, Integer> sessionMap = loginSessionBean.verifyAccount(userIDTemp, MD5Generator.hash(password));
        if (sessionMap == null) {
            //Login failed
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong username/password!", "Please try again"));
            System.out.println("+++++++++++++++++++++++++++wrong user name or password");
        } else {
            //create a query to get user id
            userIDID = productOrderSessionBean.checkValidAccessUser(userIDTemp, password, poReference);

            if (userIDID == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Quotation is not available!"));
                tempStatus = false;
            } else {
                if (systemAccessRightSB.checkOESGeneratePO(userIDID)) {
                    tempStatus = true;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correct", "PO is sent!"));

                    listOfSentPO = (List<Integer>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listOfSentPO");
                    listOfSentPO.add(poReference);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listOfSentPO", listOfSentPO);
                    for (int i = 0; i < listOfSentPO.size(); i++) {
                        System.out.println("11.23PM: check stored poReference: " + listOfSentPO.get(i));
                    }

                    System.out.println("+++++++++++++++++++++++++++quotation correct");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Quotation is not available!"));
                    tempStatus = false;
                    System.out.println("+++++++++++++++++++++++++++quotation wrong");
                }
            }
        }
    }

    public void cancelPO(Integer poReference) {

        productOrderSessionBean.cancelAPO(poReference);

    }

    public List<ProductOrderLineItem> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<ProductOrderLineItem> lineItemList) {
        this.lineItemList = lineItemList;
    }

    public List<ProductOrder> getPos() {
        return pos;
    }

    public void setPos(List<ProductOrder> pos) {
        this.pos = pos;
    }

    public void backToMrpHome() {
        listOfSentPO = (List<Integer>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listOfSentPO");
        // get list of all original POs
        tempPOS = (List<ProductOrder>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pos");
        tempToDeletePO = new ArrayList<Integer>();
        int count1 = 0;

        for (int i = 0; i < tempPOS.size(); i++) {
            for (int j = 0; j < listOfSentPO.size(); j++) {
                if (listOfSentPO.get(j).equals(tempPOS.get(i).getProductPOId())) {
                } else {
                    count1++;
                }
            }
            if (count1 == listOfSentPO.size()) {
                tempToDeletePO.add(tempPOS.get(i).getProductPOId());
                this.cancelPO(tempPOS.get(i).getProductPOId());
                System.out.println("12.05pm: to delete: " + tempPOS.get(i).getProductPOId());
            }

            count1 = 0;
        }

      //  return "mrp?faces-redirect=true";
    }

//    public String cancelPO() {
    //      return ("mrp");
    //}
    public void setUserIDTemp(String userIDTemp) {
        this.userIDTemp = userIDTemp;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserIDTemp() {
        return userIDTemp;
    }

    public String getPassword() {
        return password;
    }

    /*   public void setPoReference(Integer poReference) {
     this.poReference = poReference;
     }

     public Integer getPoReference() {
     return poReference;
     }*/
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

    public String checkAvailability(ProductOrderLineItem line) {
        String result = "Available";
        if (line != null) {
            Boolean productAvailable = false;
            if (!productAvailable && line.getQuantity() != null) {
                result = backorderSB.getWaitingTime(line.getProductproductId().getProductId(), line.getQuantity());
            }
        }
        return result;

    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Quantity Edited", ((ProductOrderLineItem) event.getObject()).getQuantity().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        ProductOrderLineItem testout = (ProductOrderLineItem) event.getObject();
        System.out.println("Set line item quantity" + testout.getQuantity());
    }

    public void onRowCancel(RowEditEvent event) {
//        QuotationLineItem myLine = (QuotationLineItem) event.getObject();
//        quotationMB.setLineItemPrice(selectedRequest, myLine, null);
        FacesMessage msg = new FacesMessage("Edit quantity cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public boolean checkUserIsCustomer() {
        return systemAccessRightSB.checkOESGeneratePO(userId);
    }

    public void searchForQuotation() {
        System.out.println(qutatuonId);
        if (purchaseMB.checkQuotationValidity(qutatuonId)) {
            itemList = purchaseMB.copyFromQuotation(qutatuonId);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Quotation id invalid"));
        }

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
        systemLogSB.recordSystemLog(userId, "OES Save Purchase Order for future process. ");
        int newPoId = purchaseMB.createPO(shipto, companyId, userId, qutatuonId, contactPerson, contactNumber);
        myPo = this.retrievePO(newPoId);
        if (itemList != null) {
            for (Object o : itemList) {
                ProductOrderLineItem pLine = (ProductOrderLineItem) o;
                purchaseMB.createProductList(pLine, myPo);
            }
        }
        purchaseMB.saveOrder(myPo);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved", "Order has been saved"));

    }

    public void submitOrder(ActionEvent event) {
        systemLogSB.recordSystemLog(userId, "OES Submit Purchase Order. ");
        boolean quantityIsEmpty = false;

        if (shipto == null || itemList.isEmpty() || itemList == null || contactPerson == null || contactNumber == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please make sure all compulsory filds are filled"));

        } else {
            for (Object o : itemList) {
                ProductOrderLineItem pLine = (ProductOrderLineItem) o;
                if (pLine.getQuantity() == null) {
                    quantityIsEmpty = true;
                }
            }

            if (quantityIsEmpty) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please enter quantity for the product"));
            } else {
                // int newPoId = purchaseMB.createPO(shipto, companyId, userId, qutatuonId, contactPerson, contactNumber);
                pos = productOrderSessionBean.createPO(productId, companyId, loginedUser, mrps);
            }
        }
    }

    private ProductOrder retrievePO(Integer poId) {
        systemLogSB.recordSystemLog(userId, "OES Retrieve saved order. ");
        return purchaseMB.retrieveProductOrder(poId);
    }

    public Boolean checkSubmittable() {
        if (myPo == null) {
            return true;
        }
        if (myPo.getStatus() == 2 || myPo.getStatus() == 14) {
            return false;
        }
        return true;
    }

}
