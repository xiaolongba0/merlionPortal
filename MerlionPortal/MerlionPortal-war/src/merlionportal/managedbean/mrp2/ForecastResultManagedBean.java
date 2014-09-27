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

@Named(value = "forecastResultManagedBean")
@ViewScoped
public class ForecastResultManagedBean implements Serializable {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    ForecastSessionBean fsb;

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

    public void computeForecastResult() {

        //produce a list of date correspond to sales
        //size need to be retrieved/computed later

        forecastR = fsb.computeResult(periodicity, expectedGrowth);
        monthlyDateR = fsb.yaxisDate();

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
        axis.setMin("2014-05-01");
        axis.setMax("2016-01-01");
        axis.setTickFormat("%b, %y");

    }


}
