/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this start1late file, choose Tools | Templates
 * and open the start1late in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.MonthForecastResult;
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
import merlionportal.mrp.forecastingmodule.RetrieveSalesDataSessionBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

@Named(value = "forecastResultManagedBean")
@ViewScoped
public class ForecastResultManagedBean implements Serializable {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    ForecastSessionBean fsb;
    @EJB
    RetrieveSalesDataSessionBean rsdsb;

    Integer companyId;

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

    double expectedGrowth;
    int periodicity;
    Integer productId;

    List<String> dateList;
    List<Integer> quantityList;
    private SystemUser loginedUser;
    Integer forecastResultID;
List<MonthForecastResult> monthForecastResults;
MonthForecastResult monthForecastResult;

 
    //variables for forecasting
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
        expectedGrowth = (double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("expectedGrowth");
        periodicity = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("periodicity");

        computeForecastResult();

    }

    public LineChartModel getForecastSales() {
        return forecastSales;

    }

    public void setForecastSales(LineChartModel forecastSales) {
        this.forecastSales = forecastSales;
    }
    
       public void setMonthForecastResults(List<MonthForecastResult> monthForecastResults) {
        this.monthForecastResults = monthForecastResults;
    }

    public List<MonthForecastResult> getMonthForecastResults() {
        monthForecastResults = fsb.retrieveMonthForecastResult(forecastResultID);
        return monthForecastResults;
    }
        public void setMonthForecastResult(MonthForecastResult monthForecastResult) {
        this.monthForecastResult = monthForecastResult;
    }

    public MonthForecastResult getMonthForecastResult() {
        return monthForecastResult;
    }
    

    public void computeForecastResult() {

        //produce a list of date correspond to sales
        //size need to be retrieved/computed later
        productId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productId");
        dateList = rsdsb.retrieveDateList(productId);
        quantityList = rsdsb.retrieveQuantityList(productId);

        forecastR = fsb.computeResult(periodicity, expectedGrowth, dateList, quantityList);
        monthlyDateR = fsb.yaxisDate();

        forecastResultID = fsb.storeForecast(periodicity, expectedGrowth, dateList, quantityList);

        //put results into the session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forecastR", forecastR);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("monthlyDateR", monthlyDateR);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forecastResultID",  forecastResultID);

        forecastSales = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Sales Figure");

        for (int i = 1; i <= periodicity; i++) {
            series1.set(monthlyDateR.get(i), forecastR.get(i));
        }

        forecastSales.addSeries(series1);

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
        axis.setMin("2014-07-01");
        axis.setMax("2016-03-01");
        axis.setTickFormat("%b, %y");

    }

}
