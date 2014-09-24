/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this start1late file, choose Tools | Templates
 * and open the start1late in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.SystemUser;
import java.io.IOException;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean
@RequestScoped
public class ForecastShowHistoryManagedBean implements Serializable {

    @EJB
    UserAccountManagementSessionBean uamb;

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
        computeForecastResult();

    }

    public LineChartModel getPurchaseHistory() {
        return purchaseHistory;
    }

    public LineChartModel getForecastSales() {
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
        size = 24;
        monthlyDate = new Vector<String>();
        monthlyDate.add("2012-08-01");
        monthlyDate.add("2012-09-01");
        monthlyDate.add("2012-10-01");
        monthlyDate.add("2012-11-01");
        monthlyDate.add("2012-12-01");
        monthlyDate.add("2013-01-01");
        monthlyDate.add("2013-02-01");
        monthlyDate.add("2013-03-01");
        monthlyDate.add("2013-04-01");
        monthlyDate.add("2013-05-01");
        monthlyDate.add("2013-06-01");
        monthlyDate.add("2013-07-01");
        monthlyDate.add("2013-08-01");
        monthlyDate.add("2013-09-01");
        monthlyDate.add("2013-10-01");
        monthlyDate.add("2013-11-01");
        monthlyDate.add("2013-12-01");
        monthlyDate.add("2014-01-01");
        monthlyDate.add("2014-02-01");
        monthlyDate.add("2014-03-01");
        monthlyDate.add("2014-05-01");
        monthlyDate.add("2014-06-01");
        monthlyDate.add("2014-07-01");
        monthlyDate.add("2014-08-01");

        salesdata = new Vector();
        salesdata.add(5000);
        salesdata.add(4000);
        salesdata.add(4000);
        salesdata.add(2000);
        salesdata.add(5000);
        salesdata.add(7000);
        salesdata.add(10000);
        salesdata.add(14000);
        salesdata.add(16000);
        salesdata.add(16000);
        salesdata.add(20000);
        salesdata.add(12000);
        salesdata.add(5000);
        salesdata.add(2000);
        salesdata.add(3000);
        salesdata.add(2000);
        salesdata.add(7000);
        salesdata.add(6000);
        salesdata.add(8000);
        salesdata.add(10000);
        salesdata.add(20000);
        salesdata.add(20000);
        salesdata.add(22000);
        salesdata.add(9000);

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
        //size need to be retreved/computed later

        size = 24;
        monthlyDate = new Vector<String>();
        monthlyDate.add("2012-08-01");
        monthlyDate.add("2012-09-01");
        monthlyDate.add("2012-10-01");
        monthlyDate.add("2012-11-01");
        monthlyDate.add("2012-12-01");
        monthlyDate.add("2013-01-01");
        monthlyDate.add("2013-02-01");
        monthlyDate.add("2013-03-01");
        monthlyDate.add("2013-04-01");
        monthlyDate.add("2013-05-01");
        monthlyDate.add("2013-06-01");
        monthlyDate.add("2013-07-01");
        monthlyDate.add("2013-08-01");
        monthlyDate.add("2013-09-01");
        monthlyDate.add("2013-10-01");
        monthlyDate.add("2013-11-01");
        monthlyDate.add("2013-12-01");
        monthlyDate.add("2014-01-01");
        monthlyDate.add("2014-02-01");
        monthlyDate.add("2014-03-01");
        monthlyDate.add("2014-05-01");
        monthlyDate.add("2014-06-01");
        monthlyDate.add("2014-07-01");
        monthlyDate.add("2014-08-01");

        salesdata = new Vector();
        salesdata.add(5000);
        salesdata.add(4000);
        salesdata.add(4000);
        salesdata.add(2000);
        salesdata.add(5000);
        salesdata.add(7000);
        salesdata.add(10000);
        salesdata.add(14000);
        salesdata.add(16000);
        salesdata.add(16000);
        salesdata.add(20000);
        salesdata.add(12000);
        salesdata.add(5000);
        salesdata.add(2000);
        salesdata.add(3000);
        salesdata.add(2000);
        salesdata.add(7000);
        salesdata.add(6000);
        salesdata.add(8000);
        salesdata.add(10000);
        salesdata.add(20000);
        salesdata.add(20000);
        salesdata.add(22000);
        salesdata.add(9000);

        //transform index (put index 0 = 0, populate the rest starting from index 1; Purpose: easier for computing)
        monthlyDateT = new Vector();
        monthlyDateT.add("0");
        for (int i = 0; i < size; i++) {
            monthlyDateT.add(monthlyDate.get(i));
        }

        salesdataT = new Vector();
        salesdataT.add(0);
        for (int i = 0; i < size; i++) {
            salesdataT.add(salesdata.get(i));
        }

        //Compute Deseasonalized Demand
        deseasonizedD = new Vector();
        periodicity = 12;
        if (periodicity % 2 == 0) {
            // periodicity is even
            //periodicity should be a input from user. For now, hard code first.

            int start1 = periodicity / 2 + 1;
            int end1 = size - periodicity / 2;

            //Filled previous Vector with zero before start1
            for (int i = 0; i < start1; i++) {
                deseasonizedD.add(0.0);
            }

            int v1 = 0;
            int v2 = 0;
            int v3 = 0;
            double value1 = 0.0;
            double value2 = 0.0;

            for (int i = start1; i <= end1; i++) {
                v1 = i - periodicity / 2;
                v2 = periodicity / 2 + i;
                v3 = v1 + 1;

                for (int j = v3; j <= (i - 1 + (periodicity / 2)); j++) {
                    value1 = value1 + salesdataT.get(j);
                }

                value2 = (salesdataT.get(v1) + salesdataT.get(v2) + 2 * value1) / (2 * periodicity);
                deseasonizedD.add(value2);

                value1 = 0;
                value2 = 0;
            }

            //Filled previous Vector with zero after end1
            for (int i = (size - periodicity / 2 + 1); i <= size; i++) {
                deseasonizedD.add(0.0);
            }

            //Create a reference list. eg 1, 2, 3....
            tValue = new Vector();
            for (int i = 0; i <= size; i++) {
                tValue.add(i);
            }

            //Linear regression to compute m (slope) and b (tangent)
            Double Xsum = 0.0;
            Double Ysum = 0.0;
            Double XYsum = 0.0;
            Double XXsum = 0.0;
            Double YYsum = 0.0;
            int N = size - periodicity;
            double m = 0;
            double b = 0;

            for (int i = start1; i <= end1; i++) {
                Xsum = Xsum + tValue.get(i);
            }

            for (int i = start1; i <= end1; i++) {
                Ysum = Ysum + deseasonizedD.get(i);
            }

            for (int i = start1; i <= end1; i++) {
                XYsum = XYsum + (deseasonizedD.get(i)) * (tValue.get(i));
            }

            for (int i = start1; i <= end1; i++) {
                XXsum = XXsum + (tValue.get(i)) * (tValue.get(i));
            }

            for (int i = start1; i <= end1; i++) {
                YYsum = YYsum + (deseasonizedD.get(i)) * (deseasonizedD.get(i));
            }

            m = (Double) ((N * XYsum) - (Xsum * Ysum)) / ((N * XXsum) - (Xsum * Xsum));
            b = (Double) ((XXsum * Ysum) - (Xsum * XYsum)) / ((N * XXsum) - (Xsum * Xsum));

            //Compute predicted deseasonalized demand
            predictedDeseasonalizedD = new Vector();
            predictedDeseasonalizedD.add(0.0);

            for (int i = 1; i <= size; i++) {
                predictedDeseasonalizedD.add(m * i + b);
            }

            //Compute seasonal factors
            seasonalFactor = new Vector();
            seasonalFactor.add(0.0);
            for (int i = 1; i <= size; i++) {
                seasonalFactor.add(salesdataT.get(i) / predictedDeseasonalizedD.get(i));
            }

            //Averaging the seasonal factor
            finalSeasonalFactor = new Vector();
            finalSeasonalFactor.add(0.0);
            double seasonalFactorTemp = 0.0;
            int count = 0;
            for (int i = 1; i <= periodicity; i++) {
                for (int j = i; j <= size; j = j + (periodicity)) {
                    seasonalFactorTemp = seasonalFactorTemp + seasonalFactor.get(j);
                    count++;
                }

                finalSeasonalFactor.add(seasonalFactorTemp / count);

                seasonalFactorTemp = 0;
                count = 0;
            }

            //Compute the final forecast result.
            forecastR = new Vector();
            forecastR.add(0);
            int j = 1;
            for (int i = (size + 1); i <= size + periodicity; i++) {
                forecastR.add((int) Math.round((m * i + b) * (finalSeasonalFactor.get(j))));
                j++;
            }

            // get a max number for graph y-axis
            int max = 0;
            for (int i = 1; i <= periodicity; i++) {
                if (forecastR.get(i) > max) {
                    max = forecastR.get(i);
                }
            }
            max = max + 2000;

            monthlyDateR = new Vector<String>();
            monthlyDateR.add("0");
            monthlyDateR.add("2014-09-01");
            monthlyDateR.add("2014-10-01");
            monthlyDateR.add("2014-11-01");
            monthlyDateR.add("2014-12-01");
            monthlyDateR.add("2015-01-01");
            monthlyDateR.add("2015-02-01");
            monthlyDateR.add("2015-03-01");
            monthlyDateR.add("2015-04-01");
            monthlyDateR.add("2015-05-01");
            monthlyDateR.add("2015-06-01");
            monthlyDateR.add("2015-07-01");
            monthlyDateR.add("2015-08-01");
            monthlyDateR.add("2015-09-01");
            monthlyDateR.add("2015-10-01");
            monthlyDateR.add("2015-11-01");
            monthlyDateR.add("2015-12-01");
            monthlyDateR.add("2016-01-01");
            monthlyDateR.add("2016-02-01");
            monthlyDateR.add("2016-03-01");
            monthlyDateR.add("2016-04-01");
            monthlyDateR.add("2016-05-01");
            monthlyDateR.add("2016-06-01");
            monthlyDateR.add("2016-07-01");
            monthlyDateR.add("2016-08-01");

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

        } else {

            // periodicity is odd
            //periodicity should be a input from user. For now, hard code first.
            int start1 = (periodicity - 1) / 2 + 1;
            int end1 = size - (periodicity - 1) / 2;

            //Filled previous Vector with zero before start1
            for (int i = 0; i < start1; i++) {
                deseasonizedD.add(0.0);
            }

            double value1 = 0.0;

            for (int i = start1; i <= end1; i++) {

                for (int j = (i - (periodicity - 1) / 2); j <= (i + (periodicity - 1) / 2); j++) {
                    value1 = value1 + salesdataT.get(j);
                }

                deseasonizedD.add(value1 / periodicity);
                value1 = 0;
            }

            //Filled previous Vector with zero after end1
            for (int i = size - (periodicity - 1) / 2 + 1; i <= size; i++) {
                deseasonizedD.add(0.0);
            }

            //Create a reference list. eg 1, 2, 3....
            tValue = new Vector();
            for (int i = 0; i <= size; i++) {
                tValue.add(i);
            }

//Linear regression to get m (gradient) and b (tangent)
            Double Xsum = 0.0;
            Double Ysum = 0.0;
            Double XYsum = 0.0;
            Double XXsum = 0.0;
            Double YYsum = 0.0;
            int N = size - periodicity + 1;
            double m = 0;
            double b = 0;

            for (int i = start1; i <= end1; i++) {
                Xsum = Xsum + tValue.get(i);
            }

            for (int i = start1; i <= end1; i++) {
                Ysum = Ysum + deseasonizedD.get(i);
            }

            for (int i = start1; i <= end1; i++) {
                XYsum = XYsum + (deseasonizedD.get(i)) * (tValue.get(i));
            }

            for (int i = start1; i <= end1; i++) {
                XXsum = XXsum + (tValue.get(i)) * (tValue.get(i));
            }

            for (int i = start1; i <= end1; i++) {
                YYsum = YYsum + (deseasonizedD.get(i)) * (deseasonizedD.get(i));
            }

            m = (Double) ((N * XYsum) - (Xsum * Ysum)) / ((N * XXsum) - (Xsum * Xsum));
            b = (Double) ((XXsum * Ysum) - (Xsum * XYsum)) / ((N * XXsum) - (Xsum * Xsum));

            //Compute predicted deseasonalized demand
            predictedDeseasonalizedD = new Vector();
            predictedDeseasonalizedD.add(0.0);

            for (int i = 1; i <= size; i++) {
                predictedDeseasonalizedD.add(m * i + b);
            }

            //Compute seasonal factors
            seasonalFactor = new Vector();
            seasonalFactor.add(0.0);
            for (int i = 1; i <= size; i++) {
                seasonalFactor.add(salesdataT.get(i) / predictedDeseasonalizedD.get(i));
            }

            //Averaging seasonal factor
            finalSeasonalFactor = new Vector();
            finalSeasonalFactor.add(0.0);
            double seasonalFactorTemp = 0.0;
            int count = 0;
            for (int i = 1; i <= periodicity; i++) {
                for (int j = i; j <= size; j = j + (periodicity)) {
                    seasonalFactorTemp = seasonalFactorTemp + seasonalFactor.get(j);
                    count++;
                }
                finalSeasonalFactor.add(seasonalFactorTemp / count);
                seasonalFactorTemp = 0;
                count = 0;
            }

            //Compute the final forecast result.
            forecastR = new Vector();
            forecastR.add(0);
            int j = 1;
            for (int i = (size + 1); i <= size + periodicity; i++) {
                forecastR.add((int) Math.round((m * i + b) * (finalSeasonalFactor.get(j))));
                j++;
            }

            int max = 0;
            for (int i = 1; i <= periodicity; i++) {
                if (forecastR.get(i) > max) {
                    max = forecastR.get(i);
                }
            }
            max = max + 2000;

            monthlyDateR = new Vector<String>();
            monthlyDateR.add("0");
            monthlyDateR.add("2014-09-01");
            monthlyDateR.add("2014-10-01");
            monthlyDateR.add("2014-11-01");
            monthlyDateR.add("2014-12-01");
            monthlyDateR.add("2015-01-01");
            monthlyDateR.add("2015-02-01");
            monthlyDateR.add("2015-03-01");
            monthlyDateR.add("2015-04-01");
            monthlyDateR.add("2015-05-01");
            monthlyDateR.add("2015-06-01");
            monthlyDateR.add("2015-07-01");
            monthlyDateR.add("2015-08-01");
            monthlyDateR.add("2015-09-01");
            monthlyDateR.add("2015-10-01");
            monthlyDateR.add("2015-11-01");
            monthlyDateR.add("2015-12-01");
            monthlyDateR.add("2016-01-01");
            monthlyDateR.add("2016-02-01");
            monthlyDateR.add("2016-03-01");
            monthlyDateR.add("2016-04-01");
            monthlyDateR.add("2016-05-01");
            monthlyDateR.add("2016-06-01");
            monthlyDateR.add("2016-07-01");
            monthlyDateR.add("2016-08-01");

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
            yAxis.setMax(30000);

            DateAxis axis = new DateAxis("Dates");
            forecastSales.getAxes().put(AxisType.X, axis);
            axis.setMin("2014-05-01");
            axis.setMax("2015-10-01");
            axis.setTickFormat("%b, %y");

        }

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

    /*   public String onParameterChange() {
      
     computeForecastResult();
     return "forecastResult?faces-redirect=true";
     }   */
}
