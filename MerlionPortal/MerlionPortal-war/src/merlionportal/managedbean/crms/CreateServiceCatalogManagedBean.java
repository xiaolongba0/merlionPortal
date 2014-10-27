/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import java.io.IOException;
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
@Named(value = "createservicecatalog")
@ViewScoped
public class CreateServiceCatalogManagedBean {

    /**
     * Creates a new instance of CreateServiceCatalogManagerBean
     */
    @EJB
    ServiceCatalogSessionBean serviceCatalogSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer companyId;
    private Integer userId;

    private String serviceName;
    private String description;
    private boolean view;
    private String type;
    private double price;

    public CreateServiceCatalogManagedBean() {
        view = false;
        price = 0;

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

    }

    public void createServiceCatalog() {
        int serviceId = -1;
        System.out.println("Price is " + price);
        if (price != 0) {
            serviceId = serviceCatalogSB.createServiceCatalog(companyId, serviceName, description, view, type, price);
        } else {
            serviceId = serviceCatalogSB.createServiceCatalog(companyId, serviceName, description, view, type, 0);
        }
        if (serviceId > 0) {
            logSB.recordSystemLog(userId, "created a service catalog");

            this.clearAllFields();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Service created!", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OMG!", "Something went wrong..."));

        }
    }

    private void clearAllFields() {
        serviceName = null;
        description = null;
        view = false;
        type = null;
        price = 0;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
