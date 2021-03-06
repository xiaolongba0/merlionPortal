/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.AssetSchedule;
import entity.TransportationAsset;
import entity.Location;
import entity.Route;
import java.io.Serializable;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import entity.SystemUser;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Yuanbo
 */
@Named(value = "TTassetViewEditManagedBean")
@ViewScoped
public class TAssetViewEditManagedBean implements Serializable {

    @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private List<TransportationAsset> tassets;
    private List<AssetSchedule> toAssets;
    private List<AssetSchedule> fromAssets;
    private List<Location> locations;
    private TransportationAsset tAsset;
    private String description;

    private Integer locationId;
    private String assetType;
    private Integer capacity;
    private Integer speed;
    private Integer price;
    private Location locationlocationId;
    private String status;
    private Integer newAssetId;
    private Integer quantity;
    private Integer companyId;
    private Integer available;
    private Integer maintenance;
    private Integer total;
    private List<Route> toRoutes;
    private List<Route> fromRoutes;
    private Integer toRouteId;
    private Integer fromRouteId;

    private SystemUser loginedUser;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;

    public TAssetViewEditManagedBean() {
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
        locations = tassetManagementSessionBean.viewMyLocations(companyId);
        available = 0;
        maintenance = 0;
        total = 0;
        pieModel1 = createPieModel1(available, maintenance, total);
        pieModel2 = createPieModel2(available, maintenance, total);

    }

    public void onLocationChange() {
        System.out.println("===============================[In Managed Bean - get Locations]");
        System.out.println("[In Managed Bean - getLocation] location ID : " + locationId);
        if (locationId != null) {
            tassets = tassetManagementSessionBean.viewtAvailableAssetForALocation(locationId);
            if (tassets == null) {
                System.out.println("============== FAILED TO VIEW STORAGE TYPE ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Storage Type. Please check if warehouse ID exists! ", ""));
            }
        }

    }

    // Show routes from location A
    public void onLocationfromChange() {
        fromRoutes = tassetManagementSessionBean.viewRoutesForLocation(locationId);
    }

    public void onLocationChangeChart() {
        available = tassetManagementSessionBean.countAvailableAssetForALocation(locationId);
        System.out.println("Available Number : " + available);
        total = tassetManagementSessionBean.counttAssetForALocation(locationId);
        System.out.println("Total Number : " + total);
        pieModel1 = createPieModel1(available, 0, total);
    }

    public void onLocationChangeChart2() {
        maintenance = tassetManagementSessionBean.countMaintAssetForALocation(locationId);
        System.out.println("Maintenance Number : " + maintenance);
        total = tassetManagementSessionBean.counttAssetForALocation(locationId);
        pieModel2 = createPieModel2(0, maintenance, total);
    }

    // show routes to location A
    public void onLocationToChange() {
        toRoutes = tassetManagementSessionBean.viewRoutestoLocation(locationId);
    }

    public void onFromRouteChange() {
        System.out.println("===============================[In Managed Bean - From A Routes]");
        System.out.println("[In Managed Bean - From Route: " + fromRouteId);
        if (fromRouteId != null) {
            fromAssets = tassetManagementSessionBean.viewAssetScheduleOnRoute(fromRouteId);
        }

    }

    public void onToRouteChange() {
        System.out.println("===============================[In Managed Bean - To A Routes]");
        System.out.println("[In Managed Bean - From Route: " + toRouteId);
        if (toRouteId != null) {
            toAssets = tassetManagementSessionBean.viewAssetScheduleOnRoute(toRouteId);
        }

    }

    private PieChartModel createPieModel1(Integer available, Integer maintenance, Integer total) {
        PieChartModel pieModel2 = new PieChartModel();

        Integer a = available;
        Integer m = maintenance;
        Integer t = total;
        Integer u = t - a - m;

        pieModel2.set("Available", a);
        pieModel2.set("On task", u);

        pieModel2.setTitle("Transportation Task Summary");
        pieModel2.setLegendPosition("w");
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(t);

        return pieModel2;

    }

    private PieChartModel createPieModel2(Integer available, Integer maintenance, Integer total) {
        PieChartModel pieModel2 = new PieChartModel();

        Integer a = available;
        Integer m = maintenance;
        Integer t = total;
        Integer u = t - a - m;

        pieModel2.set("Not been Maintained", u);
        pieModel2.set("Have been Maintained", m);

        pieModel2.setTitle("Transportation Status Summary");
        pieModel2.setLegendPosition("w");
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(t);

        return pieModel2;

    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("IN ROW EDIT =================");
        tAsset = new TransportationAsset();
        tAsset = (TransportationAsset) event.getObject();
        System.out.println("[AFTER EDIT] Asset.getName(): " + tAsset.getAssetType());
        tassetManagementSessionBean.edittAsset(tAsset.getCapacity(), tAsset.getPrice(), tAsset.getSpeed(), tAsset.getStatus(), tAsset.getAssetId());
        FacesMessage msg = new FacesMessage("Transportation Asset with tAsset ID = " + tAsset.getAssetId() + " has sucessfully been edited");

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

    public void deleteTAsset(TransportationAsset tAsset) {
        try {
            System.out.println("[In WAR FILE - Delete TransportationAsset Function]" + tAsset);
            System.out.println("[In WAR FILE - Delete TransportationAsset Function] TransportationAsset Id===========:" + tAsset.getAssetId());
            tassetManagementSessionBean.deletetAsset(tAsset.getAssetId());
            tassets.remove(tAsset);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Transportation Asset is deleted"));
            systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS deleted transportation assset");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<TransportationAsset> getTassets() {
        return tassets;
    }

    public void setTassets(List<TransportationAsset> tassets) {
        this.tassets = tassets;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public TransportationAsset gettAsset() {
        return tAsset;
    }

    public void settAsset(TransportationAsset tAsset) {
        this.tAsset = tAsset;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Location getLocationlocationId() {
        return locationlocationId;
    }

    public void setLocationlocationId(Location locationlocationId) {
        this.locationlocationId = locationlocationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNewAssetId() {
        return newAssetId;
    }

    public void setNewAssetId(Integer newAssetId) {
        this.newAssetId = newAssetId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Integer maintenance) {
        this.maintenance = maintenance;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }

    public List<AssetSchedule> getToAssets() {
        return toAssets;
    }

    public void setToAssets(List<AssetSchedule> toAssets) {
        this.toAssets = toAssets;
    }

    public List<AssetSchedule> getFromAssets() {
        return fromAssets;
    }

    public void setFromAssets(List<AssetSchedule> fromAssets) {
        this.fromAssets = fromAssets;
    }

    public List<Route> getToRoutes() {
        return toRoutes;
    }

    public void setToRoutes(List<Route> toRoutes) {
        this.toRoutes = toRoutes;
    }

    public List<Route> getFromRoutes() {
        return fromRoutes;
    }

    public void setFromRoutes(List<Route> fromRoutes) {
        this.fromRoutes = fromRoutes;
    }

    public Integer getToRouteId() {
        return toRouteId;
    }

    public void setToRouteId(Integer toRouteId) {
        this.toRouteId = toRouteId;
    }

    public Integer getFromRouteId() {
        return fromRouteId;
    }

    public void setFromRouteId(Integer fromRouteId) {
        this.fromRouteId = fromRouteId;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

}
