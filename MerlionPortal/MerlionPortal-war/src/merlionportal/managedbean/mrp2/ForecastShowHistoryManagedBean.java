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
    List<String> yearMonth;
    List purchasingNum;
    Vector<String> monthlyDate;
    Vector<Integer> salesdata;

    Vector<String> monthlyDateT;
    Vector<Integer> salesdataT;

    Vector<Double> deseasonizedD;
    Vector<Integer> deseasonizedDC;
    Vector<Integer> tValue;
    Vector<Double> predictedDeseasonalizedD;
    Vector<Double> seasonalFactor;
    Vector<Double> finalSeasonalFactor;
    Vector<Integer> forecastR;

    Vector<String> monthlyDateR;
    LineChartModel forecastSales;

    int expectedGrowth;
    int periodicity;

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
       
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expectedGrowth", expectedGrowth);

        createResultModel();

        computeForecastResult();

    }

    public LineChartModel getPurchaseHistory() {
        return purchaseHistory;
    }

    public LineChartModel getForecastSales() {
        System.out.println("Get to this stage!!!!!!!!!!");
        return forecastSales;

    }

//1. based on today, count no of months of history
    //take last 24 months history
    //store in graph
    //prompt user if want to continue?
    //get a list of year mon + no.
    //assume we already have it
    public void createPurchaseHistoryModels() {
        //produce a list of date correspond to sales
        //size need to be retreved/computed later
        size = fsb.createPurchaseDate().size();
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

    public void computeForecastResult() {

        //produce a list of date correspond to sales
        //size need to be retrieved/computed later
        System.out.println("Periodicity BEFORE  " + periodicity);
        System.out.println("growth  BEFORE " + expectedGrowth);
        forecastR = fsb.computeResult(periodicity, expectedGrowth);
        monthlyDateR = fsb.yaxisDate();

        LineChartModel forecastSales1 = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Sales Figure");

        for (int i = 1; i <= periodicity; i++) {
            series1.set(monthlyDateR.get(i), forecastR.get(i));
        }

        forecastSales1.addSeries(series1);

        forecastSales1.setTitle("Predicted Sales on a Monthly Basis");
        forecastSales1.setZoom(true);
        forecastSales1.setAnimate(true);
        forecastSales1.setLegendPosition("se");
        Axis yAxis = forecastSales1.getAxis(AxisType.Y);
        yAxis.setLabel("Sales Volume (in pieces)");
        yAxis.setMin(0);
        yAxis.setMax(35000);

        DateAxis axis = new DateAxis("Dates");
        forecastSales1.getAxes().put(AxisType.X, axis);
        axis.setMin("2014-05-01");
        axis.setMax("2016-01-01");
        axis.setTickFormat("%b, %y");

        System.out.println("XXXXXXXXXLATER periodicity" + periodicity);
        System.out.println("XXXXXXXXXLATER growth" + expectedGrowth);

        forecastSales = forecastSales1;
    }

    public void createResultModel() {
        forecastSales = new LineChartModel();

        forecastSales.setTitle("Predicted Sales on a Monthly Basis");
        forecastSales.setZoom(true);
        forecastSales.setAnimate(true);
        forecastSales.setLegendPosition("se");
        Axis yAxis = forecastSales.getAxis(AxisType.Y);
        yAxis.setLabel("Sales Volume (in pieces)");
        yAxis.setMin(0);
        yAxis.setMax(35000);

        DateAxis axis = new DateAxis("Dates");
        forecastSales.getAxes().put(AxisType.X, axis);
        axis.setMin("2014-05-01");
        axis.setMax("2016-01-01");
        axis.setTickFormat("%b, %y");

    }

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }

    public int getExpectedGrowth() {
        return expectedGrowth;
    }

    public void setExpectedGrowth(int expectedGrowth) {
        this.expectedGrowth = expectedGrowth;
    }

}
