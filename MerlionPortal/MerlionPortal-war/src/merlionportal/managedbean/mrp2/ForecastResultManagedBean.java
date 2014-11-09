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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
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
    @EJB
    private SystemLogSessionBean systemLogSB;

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

        forecastResultID = fsb.storeForecast(periodicity, expectedGrowth, dateList, quantityList, productId);

        //put results into the session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forecastR", forecastR);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("monthlyDateR", monthlyDateR);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forecastResultID", forecastResultID);

        forecastSales = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Sales Figure");

        int k = 0;
        for (int i = 1; i <= periodicity; i++) {
            series1.set(monthlyDateR.get(i), forecastR.get(i));
            //Assign largest sales data to k
            if (forecastR.get(i) >= k) {
                k = forecastR.get(i);
            }
        }

        //Compute Maximum Y axis value
        int length = String.valueOf(k).length();
        int first = this.getFirstDigit(k);
        first++;
        for (int i = 0; i < (length - 1); i++) {
            first = first * 10;
        }

        //Compute beginning date to be shown on x-axis
        String firstDateOnAxis = "2014-05-01";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(monthlyDateR.get(1));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -4);
            firstDateOnAxis = df.format(calendar.getTime());
            System.out.println("FY test new first date: " + firstDateOnAxis);
        } catch (ParseException ex) {
            Logger.getLogger(ForecastShowHistoryManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Compute ending date to be shown on x-axis
        String lastDateOnAxis = "2016-05-01";
        try {
            Date date = df.parse(monthlyDateR.get(periodicity));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, +4);
            lastDateOnAxis = df.format(calendar.getTime());
            System.out.println("FY test lastdate: " + lastDateOnAxis);
        } catch (ParseException ex) {
            Logger.getLogger(ForecastShowHistoryManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        forecastSales.addSeries(series1);

        forecastSales.setTitle("Predicted Sales on a Monthly Basis");
        forecastSales.setZoom(true);
        forecastSales.setAnimate(true);
        forecastSales.setLegendPosition("se");
        Axis yAxis = forecastSales.getAxis(AxisType.Y);
        yAxis.setLabel("Sales Volume (in pieces)");
        yAxis.setMin(0);
        yAxis.setMax(first);

        DateAxis axis = new DateAxis("Dates");
        forecastSales.getAxes().put(AxisType.X, axis);
        axis.setMin(firstDateOnAxis);
        axis.setMax(lastDateOnAxis);
        axis.setTickFormat("%b, %y");
        systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "MRP compute forecast result. ");
    }

    public int getFirstDigit(int num) {
        while (num < -9 || 9 < num) {
            num /= 10;
        }
        return Math.abs(num);
    }

}
