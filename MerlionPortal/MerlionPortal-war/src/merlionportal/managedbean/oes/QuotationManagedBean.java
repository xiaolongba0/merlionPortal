/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.Quotation;
import entity.QuotationLineItem;
import entity.SystemUser;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.oes.ordermanagement.CommonFunctionSessionBean;
import merlionportal.oes.quotationmanagementmodule.QuotationManagerSessionBean;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author manliqi
 */
@Named(value = "quotationManagedBean")
@SessionScoped
public class QuotationManagedBean implements Serializable {

    @EJB
    private CommonFunctionSessionBean commonSB;

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private QuotationManagerSessionBean quotationMB;

    @EJB
    private SystemAccessRightSessionBean systemAccessRightSB;

    private Quotation selectedRequest;
    private List<Quotation> allQuotation;
    private Integer companyId;
    private Integer userId;
    private List<QuotationLineItem> listItem;
    private SystemUser cutomer;
    private String inputText;
    private List<Quotation> allProcceQuotation;
    private String qStatus;
    private List<String> allStatus;
    private List<QuotationLineItem> qlistItem;
    private Quotation filteredQuotation;
    private Quotation selectedQuotation;
    private SystemUser mycustomer;
    private String currency;

    /**
     * Creates a new instance of QuotationManagedBean
     */
    public QuotationManagedBean() {
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

    public boolean checkUserGenerateQuotation() {
        return systemAccessRightSB.checkOESGenerateQuotation(userId);
    }

    public Quotation getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(Quotation selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public List<Quotation> getAllQuotation() {
        if (!systemAccessRightSB.checkOESGeneratePO(userId)) {
            System.out.println("=====================View all quoation This is staff =====================");
            allQuotation = quotationMB.viewAllRequestForQuotation(companyId);
        } else {
            System.out.println(" =====================View all quoation This is customer=====================");
            allQuotation = quotationMB.viewAllRequestForQuotation(companyId, userId);
        }
        return allQuotation;
    }

    public String showRequest() {

        if (selectedRequest == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one request ."));
            return "viewallrequests.xhtml?faces-redirect=true";
        }
        return "displayrequestinfor.xhtml?faces-redirect=true";
    }

    public String editQuotation() {

        if (selectedRequest == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one request ."));
            return "viewallrequests.xhtml";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "View", "Action Accepted"));
        systemLogSB.recordSystemLog(userId, "OES edited quotation");
        return "generatequotation.xhtml?faces-redirect=true";
    }

    public String deletRequest() {
        System.out.println("Deleted quotation Id " + selectedRequest.getQuotationId());
        quotationMB.deleteRequest(selectedRequest.getQuotationId());
        systemLogSB.recordSystemLog(userId, "OES deleted request");
        return "viewallrequests.xhtml?faces-redirect=true";

    }

    public void generateQuotation(ActionEvent event) {
        if (selectedRequest.getStatus() != 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Quotation Already Generated"));
        } else if (inputText.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Terms and Conditions cannot be empty"));
        } else if (!quotationMB.checkPrice(selectedRequest)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Line Item price cannot be empty"));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quotation generated."));
            quotationMB.generateQuotation(selectedRequest, inputText,currency);
            systemLogSB.recordSystemLog(userId, "OES Generate Quotation. ");
            inputText = null;
        }

    }

    public void onRowEdit(RowEditEvent event) {
        Double linItemPrice = ((QuotationLineItem) event.getObject()).getLineItemPrice();
        FacesMessage msg = new FacesMessage("Price Edited", ((QuotationLineItem) event.getObject()).getLineItemPrice().toString());
        QuotationLineItem myLine = (QuotationLineItem) event.getObject();
        System.out.println("SELECTED LINE" + myLine.getLine());
        System.out.println("SELECTED LINE PRICE" + myLine.getLineItemPrice());
        System.out.println("Price new " + linItemPrice);
        quotationMB.setLineItemPrice(selectedRequest, myLine, linItemPrice);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        QuotationLineItem myLine = (QuotationLineItem) event.getObject();
        quotationMB.setLineItemPrice(selectedRequest, myLine, null);
        FacesMessage msg = new FacesMessage("Price Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void setAllQuotation(List<Quotation> allQuotation) {
        this.allQuotation = allQuotation;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<QuotationLineItem> getListItem() {
        listItem = selectedRequest.getQuotationLineItemList();
        return listItem;
    }

    public void setListItem(List<QuotationLineItem> listItem) {
        this.listItem = listItem;
    }

    public SystemUser getCutomer() {
        int customerId = selectedQuotation.getCustomerId();
        cutomer = quotationMB.findCustomer(customerId);
        return cutomer;
    }

    public void setCutomer(SystemUser cutomer) {
        this.cutomer = cutomer;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public List<Quotation> getAllProcceQuotation() {
        if (!systemAccessRightSB.checkOESGeneratePO(userId)) {
            allProcceQuotation = quotationMB.viewAllProQuotation(companyId);
        } else {
            allProcceQuotation = quotationMB.viewAllProQuotation(companyId, userId);
        }
        return allProcceQuotation;
    }

    public void setAllProcceQuotation(List<Quotation> allProcceQuotation) {
        this.allProcceQuotation = allProcceQuotation;
    }

    public String getqStatus(int mStatus) {
        if (mStatus == 2) {
            qStatus = "Waiting for acception";
        }
        if (mStatus == 3) {
            qStatus = "Valid";
        }
        if (mStatus == 4) {
            qStatus = "Rejected";
        }
        if (mStatus == 5) {
            qStatus = "Canceled";
        }

        return qStatus;
    }

    public String viewCustomerName(int mycusId) {
        return commonSB.viewCustomerName(mycusId);
    }

    public void setqStatus(String qStatus) {
        this.qStatus = qStatus;
    }

    public List<String> getAllStatus() {
        allStatus = quotationMB.getAllStatus();

        return allStatus;
    }

    public void setAllStatus(List<String> allStatus) {
        this.allStatus = allStatus;
    }

    public List<QuotationLineItem> getQlistItem() {
        return qlistItem;
    }

    public void setQlistItem(List<QuotationLineItem> qlistItem) {
        this.qlistItem = qlistItem;
    }

    public Quotation getFilteredQuotation() {
        return filteredQuotation;
    }

    public void setFilteredQuotation(Quotation filteredQuotation) {
        this.filteredQuotation = filteredQuotation;
    }

    public Quotation getSelectedQuotation() {
        return selectedQuotation;
    }

    public void setSelectedQuotation(Quotation selectedQuotation) {
        this.selectedQuotation = selectedQuotation;
    }

    public String viewQuotation() {
        return "viewquotationinfor.xhtml?faces-redirect=true";
    }

    public Boolean checkAcceptable() {
        if (selectedQuotation.getStatus() == 2 && this.canGenerateRequest()) {
            return true;
        }
        return false;
    }

    public void acceptQuotation() {
        quotationMB.acceptQuotation(selectedQuotation);
        systemLogSB.recordSystemLog(userId, "OES Accept Quotation ");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quotation accepted."));

    }

    public void rejectQuotation() {
        systemLogSB.recordSystemLog(userId, "OES Reject Quotation. ");
        quotationMB.rejectQuotation(selectedQuotation);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quotation rejected."));

    }

    public void cancelQuotation() {
        systemLogSB.recordSystemLog(userId, "OES Cancel Quotation. ");
        quotationMB.cancelQuotation(selectedQuotation);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quotation canceled."));

    }

    public String goBackToall() {
        selectedQuotation = null;
        return "displayallquotations.xhtml?faces-redirect=true";
    }

    public String goBackToAll() {
        return "viewallrequests.xhtml?faces-redirect=true";
    }

    public Boolean canGenerateRequest() {
        return systemAccessRightSB.checkOESGeneratePO(userId);

    }

    public Boolean canCancelQuotation() {
        return systemAccessRightSB.checkOESGenerateQuotation(userId);
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public SystemUser getMycustomer() {
        mycustomer = quotationMB.findCustomer(selectedRequest.getCustomerId());
        return mycustomer;
    }

    public void setMycustomer(SystemUser mycustomer) {
        this.mycustomer = mycustomer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
