package merlionportal.managedbean.mrp2;

import entity.Component;
import entity.Product;
import javax.faces.view.ViewScoped;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author yao
 */
@ViewScoped
@ManagedBean(name = "productComponentViewEditManagedBean")
public class ProductComponentViewEditManagedBean {

    @EJB
    private ProductSessionBean productSessionBean;
    private Integer companyId = 12345;

    private List<Component> components;
    private Component component1;
    private Double componentId;

    private String componentName;
    private String description;
    private Integer quantity;
    private String currency;
    private Double cost;
    private Integer leadTime;
    private String contactPerson;
    private String contactNumber;
    private String supplierDescription;
    private String contactEmail;

    private Double productId = 1.0;
    private final static String[] currencies;

    public ProductComponentViewEditManagedBean() {
    }

    @PostConstruct
    public void init() {
        int pdtTempId = productId.intValue();
        components = productSessionBean.getComponentsForAProduct(companyId, pdtTempId);
    }

    static {

        currencies = new String[20];
        currencies[0] = "US Dollar (USD)";
        currencies[1] = "European Euro (EUR)";
        currencies[2] = "Japan Yen (JPY)";
        currencies[3] = "Pound Sterling (GBP)";
        currencies[4] = "Australian Dollar (AUD)";
        currencies[5] = "Swiss Franc (CHF)";
        currencies[6] = "Canadian Dollar";
        currencies[7] = "Hong Kong Dollar (HKD)";
        currencies[8] = "Swedish Krona (SEK)";
        currencies[9] = "New Zealand Dollar (NZD)";
        currencies[10] = "South African Rand (ZAR)";
        currencies[11] = "Russian Ruble (RUB)";
        currencies[12] = "Indian Rupee (INR)";
        currencies[13] = "Singapore Dollar (SGD)";
        currencies[14] = "Bulgarian Lev (BGN)";
        currencies[15] = "Chinese Yuan Renminbi (CNY)";
        currencies[16] = "Thailand Baht (THB)";
        currencies[17] = "Hungary Forint(HUF)";
        currencies[18] = "Norwegian Krone (NOK)";
        currencies[19] = "Mexican Peso (MXN)";

    }

    public String[] getCurrencies() {
        return currencies;
    }

    public Double getComponentId() {
        return componentId;
    }

    public void setComponentId(Double componentId) {
        this.componentId = componentId;
    }

    public Double getProductId() {
        return productId;
    }

    public void setProductId(Double productId) {
        this.productId = productId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        int quantityTemp = quantity.intValue();
        this.quantity = quantityTemp;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Double leadTime) {
        int leadTimeTemp = leadTime.intValue();
        this.leadTime = leadTimeTemp;
    }

  /*  public String getContactPerson() {
        return component1.getSuppliersupplierCompanyId().getContactPerson();
    }

    public void setContactPerson(String contactPerson) {
        component1.getSuppliersupplierCompanyId().setContactPerson(contactPerson);
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
*/
    public List<Component> getComponents() {
        return components;
    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Component Edited");
        Component component = new Component();
        component = (Component) event.getObject();
        //  System.err.println("product.getProductName(): " + product.getProductName());
        int pdtTempId = productId.intValue();
        productSessionBean.editComponent(component.getComponentName(), component.getDescription(), component.getCost(), component.getCurrency(), component.getQuantity(), component.getLeadTime(),/* component.getSuppliersupplierCompanyId().getContactPerson(), component.getSuppliersupplierCompanyId().getContactNumber(), component.getSuppliersupplierCompanyId().getDescription(), component.getSuppliersupplierCompanyId().getContactEmail(),*/ companyId, pdtTempId, component.getComponentId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
