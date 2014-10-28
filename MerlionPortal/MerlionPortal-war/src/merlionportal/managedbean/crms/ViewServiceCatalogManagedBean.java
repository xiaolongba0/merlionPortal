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
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.accountmanagementmodule.ServiceCatalogSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewServiceCatalogManagedBean")
@ViewScoped
public class ViewServiceCatalogManagedBean {

    /**
     * Creates a new instance of ViewServiceCatalogManagedBean
     */
    @EJB
    ServiceCatalogSessionBean serviceCatalogSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

    private List<ServiceCatalog> services;
    private ServiceCatalog selectedService;
    private List<ServiceCatalog> filteredServices;

    public ViewServiceCatalogManagedBean() {
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
        services = serviceCatalogSB.getCompanyServices(companyId);
    }

    public void updateServiceCatalog() {
        int result = serviceCatalogSB.updateServiceCatalog(selectedService.getServiceCatalogId(), selectedService.getServiceName(), selectedService.getServiceDescription(), selectedService.getPublicView(), selectedService.getServiceType(), selectedService.getPricePerTEU());
        if (result > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Service is updated"));
            logSB.recordSystemLog(userId, "CRMS updated service catatlog");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "Something went wrong"));

        }

    }

    public void deleteServiceCatalog(ServiceCatalog passedInService) {
        int result = serviceCatalogSB.deleteServiceCatalog(passedInService.getServiceCatalogId());
        if (result == 1) {
            services.remove(passedInService);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Service is deleted"));
            logSB.recordSystemLog(userId, "CRMS deleted service catalog");

        } else if (result == 2) {
            services.remove(passedInService);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Service is marked as voided!", "There are quotations associated with this service, thus it cannot be deleted but is now marked as voided. You will not see it in the service list anymore"));
            logSB.recordSystemLog(userId, "CRMS marked a service catalog as deleted");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "Something went wrong"));

        }
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

    public void selectARow(ServiceCatalog service) {
        selectedService = service;
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

}
