package merlionportal.managedbean.mrp2;

import entity.Component;
import entity.Product;
import entity.SystemUser;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.forecastingmodule.ForecastSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author yao
 */
//@ViewScoped
//@ManagedBean(name = "productComponentViewEditManagedBean")

@Named(value = "productComponentViewEditManagedBean")
@ViewScoped
public class ProductComponentViewEditManagedBean {

    @EJB
    private ProductSessionBean productSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    ForecastSessionBean forecastSessionBean;

    private List<Component> components;
    private Double componentId;
    private Integer companyId;
    private String componentName;
    private String description;
    private Integer quantity;
    private String currency;
    private Double cost;
    private Integer leadTime;
    private Integer orderQuantity;
    private int supplierCompanyId;
    private String supplierContactPerson;
    private String supplierContactNumber;
    private String supplierContactEmail;

    //private Double productId = 1.0;
    private final static String[] currencies;

    Integer productId;
    Product product;
    List<Product> products;

    private SystemUser loginedUser;

    public ProductComponentViewEditManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
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

        products = productSessionBean.getMyProducts(companyId);

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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /////change later
    public List<Product> getProducts() {
        return products;
    }
    
     public void setProducts(List<Product> products) {
        this.products = products;
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
    
        public Integer getOrderQuantity() {
        return orderQuantity;
    }

     public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
    

    public int getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(int supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierContactEmail() {
        return supplierContactEmail;
    }

    public void setSupplierContactEmail(String supplierContactEmail) {
        this.supplierContactEmail = supplierContactEmail;
    }

    public String getSupplierContactPerson() {
        return supplierContactPerson;
    }

    public void setSupplierContactPerson(String supplierContactPerson) {
        this.supplierContactPerson = supplierContactPerson;
    }

    public String getSupplierContactNumber() {
        return supplierContactNumber;
    }

    public void setSupplierContactNumber(String supplierContactNumber) {
        this.supplierContactNumber = supplierContactNumber;
    }

    public List<Component> getComponents() {
        return components;
    }

      public void deleteComponent(Component component1) {
        try {
            boolean result = productSessionBean.deleteComponents(component1.getComponentId());
            if (result) {
                components.remove(component1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Component is deleted"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void onRowEdit(RowEditEvent event) {
        
        Component component = new Component();
        component = (Component) event.getObject();
        productSessionBean.editComponent(component.getComponentName(), component.getDescription(), component.getCost(), component.getCurrency(), component.getQuantity(), component.getLeadTime(), component.getOrderQuantity(), component.getSupplierCompanyId(), component.getSupplierContactPerson(), component.getSupplierContactNumber(), component.getSupplierContactEmail(), companyId, productId, component.getComponentId());
       FacesMessage msg = new FacesMessage("Component Edited", String.valueOf(component.getComponentId()));
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

    public void onProductChange() {
        if (productId != null) {
            components = productSessionBean.getComponentsForAProduct(companyId, productId);

        }

    }

}
