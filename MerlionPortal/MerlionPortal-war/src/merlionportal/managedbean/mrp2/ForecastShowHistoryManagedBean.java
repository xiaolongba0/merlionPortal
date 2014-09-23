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

    Vector<Integer> deseasonizedD;
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
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!periodicity" + periodicity);

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

//Deseasonalize the demand (demand that would have been observed in the absence of seasonal fluctuations)
        deseasonizedD = new Vector();
        if (size % 2 == 0) {
            //size is even
            //start from month 7, put previous 6 month value as zero
            periodicity = 12;
            int start1 = periodicity / 2;
            int end1 = size - periodicity / 2 - 1;

            for (int i = 0; i < start1; i++) {
                deseasonizedD.add(0);
            }

            int v1 = 0;
            int v2 = 0;
            int v3 = 0;
            int value1 = 0;
            int value2 = 0;

            for (int i = start1; i <= end1; i++) {
                v1 = i - periodicity / 2;
                v2 = periodicity / 2 + i;
                v3 = v1 + 1;

                for (int j = v3; j < (v3 + (periodicity - 1)); j++) {
                    value1 = value1 + salesdata.get(j);
                }

                value2 = (salesdata.get(v1) + salesdata.get(v2) + 2 * value1) / (2 * periodicity);
                deseasonizedD.add(value2);

                value1 = 0;
                value2 = 0;
            }
            for (int i = 0; i < start1; i++) {
                deseasonizedD.add(0);
            }
        } else {
            //size is odd
        }

        //transform index 0 to 1
        deseasonizedDC = new Vector();
        deseasonizedDC.add(0);
        for (int i = 0; i < size; i++) {

            deseasonizedDC.add(deseasonizedD.get(i));
        }

        tValue = new Vector();
        for (int i = 0; i <= size; i++) {
            tValue.add(i);
        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + deseasonizedDC.get(0));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + tValue.get(0));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + deseasonizedDC.get(5));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + tValue.get(5));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + deseasonizedDC.get(6));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + tValue.get(6));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + deseasonizedDC.get(7));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + tValue.get(7));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + deseasonizedDC.get(18));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + tValue.get(18));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + deseasonizedDC.get(19));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + tValue.get(19));

//do linear regression and compute T and b. good!        from t=7 to t=18
        Double Xsum = 0.0;
        Double Ysum = 0.0;
        Double XYsum = 0.0;
        Double XXsum = 0.0;
        Double YYsum = 0.0;
        int N = size - periodicity;
        double m = 0;
        double b = 0;

        for (int i = (periodicity / 2 + 1); i <= (size - (periodicity / 2)); i++) {
            Xsum = Xsum + tValue.get(i);
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + Xsum);

        for (int i = (periodicity / 2 + 1); i <= (size - (periodicity / 2)); i++) {
            Ysum = Ysum + deseasonizedDC.get(i);
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + Ysum);

        for (int i = (periodicity / 2 + 1); i <= (size - (periodicity / 2)); i++) {
            XYsum = XYsum + (deseasonizedDC.get(i)) * (tValue.get(i));
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + XYsum);

        for (int i = (periodicity / 2 + 1); i <= (size - (periodicity / 2)); i++) {
            XXsum = XXsum + (tValue.get(i)) * (tValue.get(i));
        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + XXsum);

        for (int i = (periodicity / 2 + 1); i <= (size - (periodicity / 2)); i++) {
            YYsum = YYsum + (deseasonizedDC.get(i)) * (deseasonizedDC.get(i));
        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + YYsum);

        m = (Double) ((N * XYsum) - (Xsum * Ysum)) / ((N * XXsum) - (Xsum * Xsum));

        b = deseasonizedDC.get(periodicity / 2 + 1) - tValue.get(periodicity / 2 + 1) * m;

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + m);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + b);

        //predicted deseasonalized demand
        predictedDeseasonalizedD = new Vector();
        predictedDeseasonalizedD.add(0.0);

        for (int i = 1; i <= size; i++) {

            predictedDeseasonalizedD.add(m * i + b);
        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + predictedDeseasonalizedD.get(0));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + predictedDeseasonalizedD.get(1));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + predictedDeseasonalizedD.get(5));

        //compute seasonal factors
        seasonalFactor = new Vector();
        seasonalFactor.add(0.0);
        for (int i = 1; i <= size; i++) {

            seasonalFactor.add(salesdata.get(i - 1) / predictedDeseasonalizedD.get(i));
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + seasonalFactor.get(0));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + seasonalFactor.get(1));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + seasonalFactor.get(2));

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + seasonalFactor.get(3));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + seasonalFactor.get(4));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + seasonalFactor.get(5));

        finalSeasonalFactor = new Vector();
        finalSeasonalFactor.add(0.0);
        double seasonalFactorTemp = 0.0;
        for (int i = 1; i <= periodicity; i++) {

            for (int j = i; j <= size; j = j + (periodicity)) {

                seasonalFactorTemp = seasonalFactorTemp + seasonalFactor.get(j);

            }

            finalSeasonalFactor.add(seasonalFactorTemp / (size / periodicity));
            seasonalFactorTemp = 0;
        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!SeasonalFactorAveraged!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + finalSeasonalFactor.get(0));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + finalSeasonalFactor.get(1));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + finalSeasonalFactor.get(12));

        forecastR = new Vector();
        forecastR.add(0);
        int j = 1;
        for (int i = (size + 1); i <= size + periodicity; i++) {
            forecastR.add((int) Math.round((m * i + b) * (finalSeasonalFactor.get(j))));
            j++;
        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!1!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!predicted!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + forecastR.get(0));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + forecastR.get(1));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + forecastR.get(2));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + forecastR.get(3));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + forecastR.get(4));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + forecastR.get(5));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + forecastR.get(12));

        monthlyDateR = new Vector<String>();
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

        forecastSales = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Sales Figure");

        for (int i = 0; i < periodicity; i++) {
            series1.set(monthlyDateR.get(i), forecastR.get(i + 1));
        }

        forecastSales.addSeries(series1);

        forecastSales.setTitle("Predicted Sales on a Monthly Basis");
        forecastSales.setZoom(true);
        forecastSales.setAnimate(true);
        forecastSales.setLegendPosition("se");
        Axis yAxis = forecastSales.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(30000);

        DateAxis axis = new DateAxis("Dates");
        forecastSales.getAxes().put(AxisType.X, axis);
        axis.setMin("2014-05-01");
        axis.setMax("2015-10-01");
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

 /*   public String onParameterChange() {
      
         computeForecastResult();
         return "forecastResult?faces-redirect=true";
    }   */

}
