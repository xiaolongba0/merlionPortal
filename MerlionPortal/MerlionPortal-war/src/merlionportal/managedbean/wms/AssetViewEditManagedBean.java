/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.wms;

import entity.SystemUser;
import entity.Warehouse;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author YunWei
 */
@Named(value = "assetViewEditManagedBean")
@ViewScoped
public class AssetViewEditManagedBean {

    @EJB
    private AssetManagementSessionBean assetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<Warehouse> warehouses;
    private Integer warehouseId;
    private String warehouseName;
    private final static String[] country;
    private String city;
    private String street;
    private String description;
    private Integer zipcode;
    private Integer companyId;
    
    private Warehouse warehouse;

    private SystemUser loginedUser;

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

    }

    /**
     * Creates a new instance of AssetViewEditManagedBean
     */
    public AssetViewEditManagedBean() {
    }

    public List<Warehouse> getWarehouses() {
        System.out.println("===============================[In Managed Bean - getWarehouses]");
        if (warehouses == null){
           warehouses = assetManagementSessionBean.viewMyWarehouses(companyId); 
        }       

        // for checking
        for (Object obj : warehouses) {
            System.out.println(obj);
        }
        return warehouses;
    }

    static {
        country = new String[10];
        country[0] = "Spain";
        country[1] = "Japan";
        country[2] = "USA";
        country[3] = "Australia";
        country[4] = "France";
        country[5] = "Singapore";
        country[6] = "Hong Kong";
        country[7] = "China";
        country[8] = "New Zeland";
        country[9] = "Taiwan";
    }

    public String[] getCountry() {
        return country;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }


    public void onRowEdit(RowEditEvent event) {
        System.out.println("ON ROW EDIT ===============================");
        warehouse = new Warehouse();
        warehouse = (Warehouse) event.getObject();
        // System.out.println("[Checking if input is correct] ====================== : " + warehouseName);
        System.out.println("[AFTER EDIT] warehouse.getName(): " + warehouse.getName());
        assetManagementSessionBean.editWarehouse(warehouse.getName(), warehouse.getCountry(), warehouse.getCity(),
                warehouse.getStreet(), warehouse.getDescription(), warehouse.getZipcode(), warehouse.getCompanyId(), warehouse.getWarehouseId());
        FacesMessage msg = new FacesMessage("Warehouse with Warehouse ID = " + warehouse.getWarehouseId() + " has sucessfully been edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        System.out.println("ON ROW CANCEL =============================");
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
