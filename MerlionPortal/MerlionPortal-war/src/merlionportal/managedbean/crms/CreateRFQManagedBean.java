/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceCatalog;
import entity.SystemUser;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.crms.contractmanagementmodule.QuotationManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "createRFQManagedBean")
@ViewScoped
public class CreateRFQManagedBean {

    /**
     * Creates a new instance of CreateRFQManagedBean
     */
    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    QuotationManagementSessionBean quotationManagementSB;

    private Integer userId;
    private Integer companyId;
    private String companyName;
    private SystemUser loginedUser;

    //User input
    private Date startDate;
    private Date endDate;
    private String origin;
    private String destination;
    private Date today;
    private Integer quantityPerMonth;

    private ServiceCatalog selectedService;

    public CreateRFQManagedBean() {
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
        selectedService = (ServiceCatalog) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedService");
        if (selectedService != null) {
            System.out.println("NOT NULL!!! so happy");
        }
        loginedUser = uamb.getUser(userId);
        companyName = uamb.getCompany(companyId).getName();
        today = new Date();
    }

    public void createRequestForQuotation() {
        int result = quotationManagementSB.createRequestForQuotation(selectedService.getServiceCatalogId(), selectedService.getServiceType(), startDate, endDate, companyId, selectedService.getCompanyId(), origin, destination, quantityPerMonth);
        if (result > 0) {
            this.clearAllField();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Request for quotation is sent"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrongs."));
        }

    }

    private void clearAllField() {
        startDate = null;
        endDate = null;
        origin = null;
        destination = null;
        quantityPerMonth = null;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public ServiceCatalog getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(ServiceCatalog selectedService) {
        this.selectedService = selectedService;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Integer getQuantityPerMonth() {
        return quantityPerMonth;
    }

    public void setQuantityPerMonth(Integer quantityPerMonth) {
        this.quantityPerMonth = quantityPerMonth;
    }

}
