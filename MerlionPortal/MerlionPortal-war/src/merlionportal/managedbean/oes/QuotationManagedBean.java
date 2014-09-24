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
import merlionportal.oes.quotationmanagementmodule.CheckCustomerRoleSessionBean;
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
    private QuotationManagerSessionBean quotationMB;
    @EJB
    private CheckCustomerRoleSessionBean ccrsb;

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

    public boolean checkUserIsCustomer() {
        return ccrsb.checkUserIsCustomer(userId);
    }

    public Quotation getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(Quotation selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public List<Quotation> getAllQuotation() {
        if (!ccrsb.checkUserIsCustomer(userId)) {
            allQuotation = quotationMB.viewAllRequestForQuotation(companyId);
        } else {
            allQuotation = quotationMB.viewAllRequestForQuotation(companyId, userId);
        }
        return allQuotation;
    }

    public String showRequest() {

        if (selectedRequest == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one request ."));
            return "viewallrequests.xhtml";
        }
        return "displayrequestinfor.xhtml";
    }

    public String editQuotation() {

        if (selectedRequest == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select one request ."));
            return "viewallrequests.xhtml";
        }
        System.out.println("this line will executed");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "View", "Action Accepted"));
                System.out.println("this line  executed  finished");

        return "generatequotation.xhtml";

    }

    public String deletRequest() {
        System.out.println("Deleted quotation Id " + selectedRequest.getQuotationId());
        quotationMB.deleteRequest(selectedRequest.getQuotationId());
        return "viewallrequests.xhtml";

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
            quotationMB.generateQuotation(selectedRequest, inputText);
            System.out.println("Quotation Generated");
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
        int customerId = selectedRequest.getCustomerId();
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
        if (!ccrsb.checkUserIsCustomer(userId)) {
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
        if (selectedQuotation == null) {
            System.out.println("OMG IT IS NULLLLLLLLLL");
        } else {
            System.out.println("It is valued " + selectedQuotation.getQuotationId());
        }
        return "viewquotationinfor.xhtml";
    }

    public Boolean checkAcceptable() {
        return selectedQuotation.getStatus() == 2;
    }

    public void acceptQuotation() {
        quotationMB.acceptQuotation(selectedQuotation);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quotation accepted."));

    }

    public void rejectQuotation() {
        quotationMB.rejectQuotation(selectedQuotation);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quotation rejected."));

    }

    public void cancelQuotation() {
        quotationMB.cancelQuotation(selectedQuotation);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quotation canceled."));

    }

    public String goBackToall() {
        selectedQuotation = null;
        return "displayallquotations.xhtml";
    }

    public String goBackToAll() {
        return "viewallrequests.xhtml";
    }

}
