/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.Product;
import entity.Quotation;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.oes.quotationmanagementmodule.QuotationManagerSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "displayProductManagedBean")
@RequestScoped
public class DisplayProductManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private SystemAccessRightSessionBean accessRight;
    @EJB
    private QuotationManagerSessionBean quotationMB;

    private List<Product> products;
    private List<Product> selectedProducts;
    private Quotation newRequest;
    private Integer companyId;
    private Integer userId;
    private List<Product> filteredProducts;
    private List<String> categories;

    public DisplayProductManagedBean() {
    }

    /**
     * Creates a new instance of DisplayProductManagedBean
     */
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

    public List<Product> getProducts() {
        this.products = quotationMB.displayAllProducts(companyId);
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public Quotation getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(Quotation newRequest) {
        this.newRequest = newRequest;
    }

    public void submitRequest(ActionEvent event) {

        if (selectedProducts == null || selectedProducts.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select at least one product ."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Request generated."));
            newRequest = quotationMB.requestForQuotation(companyId, userId);
            for (Object o : selectedProducts) {
                Product p = (Product) o;
                quotationMB.createLineItem(companyId, p, newRequest);
            }
            systemLogSB.recordSystemLog(userId, "OES Generate request for quotation");

        }

        selectedProducts.clear();
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public List<String> getCategories() {
        categories = quotationMB.getAllTypes(companyId);

        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean canGenerateRequest() {
        return accessRight.checkOESGeneratePO(userId);

    }

}
