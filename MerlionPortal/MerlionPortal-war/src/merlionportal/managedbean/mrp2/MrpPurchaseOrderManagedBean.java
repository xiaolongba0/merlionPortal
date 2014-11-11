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

  

}
