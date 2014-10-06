/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.ServiceCatalog;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.crms.accountmanagementmodule.ServiceCatalogSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "serviceYellowPageManagerBean")
@ViewScoped
public class ServiceYellowPageManagedBean {

    /**
     * Creates a new instance of ServiceYellowPageManagerBean
     */
    @EJB
    ServiceCatalogSessionBean serviceCatalogSB;

    private Integer companyId;
    private Integer userId;

    private List<ServiceCatalog> services;
    private ServiceCatalog selectedService;
    private List<ServiceCatalog> filteredServices;

    private ServiceCatalog serviceCatalogToView;

    public ServiceYellowPageManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
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
        services = serviceCatalogSB.getAllPublicServices();
    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Double.valueOf(filterText)) < 0;
    }

    public String generateRequestForQuotation() {
        if (selectedService != null) {
            if ((int) selectedService.getCompanyId() != companyId) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedService", selectedService);
                return "createrequestforquotation.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You can not request your own company's service", "Please select a service from other companies"));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a service to request!", ""));
            return null;
        }

    }

    public void viewASingleServices(ServiceCatalog serviceCatalogView) {
        System.out.println("Rubbish!");
        serviceCatalogToView = serviceCatalogView;
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

    public List<ServiceCatalog> getServices() {
        return services;
    }

    public void setServices(List<ServiceCatalog> services) {
        this.services = services;
    }

    public ServiceCatalog getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(ServiceCatalog selectedService) {
        this.selectedService = selectedService;
    }

    public List<ServiceCatalog> getFilteredServices() {
        return filteredServices;
    }

    public void setFilteredServices(List<ServiceCatalog> filteredServices) {
        this.filteredServices = filteredServices;
    }

    public ServiceCatalog getServiceCatalogToView() {
        return serviceCatalogToView;
    }

    public void setServiceCatalogToView(ServiceCatalog serviceCatalogToView) {
        this.serviceCatalogToView = serviceCatalogToView;
    }

}
