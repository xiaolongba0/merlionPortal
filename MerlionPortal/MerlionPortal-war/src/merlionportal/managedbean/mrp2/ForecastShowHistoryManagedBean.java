/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this start1late file, choose Tools | Templates
 * and open the start1late in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.SystemUser;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.forecastingmodule.ForecastSessionBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

@Named(value = "forecastShowHistoryManagedBean")
@ViewScoped
public class ForecastShowHistoryManagedBean implements Serializable {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    ForecastSessionBean fsb;

    Integer companyId;

    private LineChartModel purchaseHistory;
    Vector<String> monthlyDate;
    Vector<Integer> salesdata;

    private SystemUser loginedUser;

    //variables for forecasting
    int size = 0;

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
        createPurchaseHistoryModels();

    }

    public LineChartModel getPurchaseHistory() {
        return purchaseHistory;
    }


    public void createPurchaseHistoryModels() {
        //produce a list of date correspond to sales
        //size need to be retreved/computed later
        size = fsb.createPurchaseDate().size();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("size", size);
        
        System.out.println("array size!!!!!!!!!!" + size);
        this.monthlyDate = fsb.createPurchaseDate();
        this.salesdata = fsb.createPurchaseData();

        purchaseHistory = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Sales Figure");

        for (int i = 0; i < 24; i++) {
            series1.set(monthlyDate.get(i), salesdata.get(i));
        }

        purchaseHistory.addSeries(series1);

        purchaseHistory.setTitle("Sales History on a Monthly Basis");
        purchaseHistory.setZoom(true);
        purchaseHistory.setAnimate(true);
        purchaseHistory.setLegendPosition("se");
        Axis yAxis = purchaseHistory.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(30000);

        DateAxis axis = new DateAxis("Dates");
        purchaseHistory.getAxes().put(AxisType.X, axis);
        axis.setMin("2012-05-01");
        axis.setMax("2014-10-01");
        axis.setTickFormat("%b, %y");

    }

}
