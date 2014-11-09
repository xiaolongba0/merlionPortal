/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this start1late file, choose Tools | Templates
 * and open the start1late in the editor.
 */
package merlionportal.managedbean.mrp2;

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

@Named(value = "forecastShowHistoryManagedBean")
@ViewScoped
public class ForecastShowHistoryManagedBean implements Serializable {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    ForecastSessionBean fsb;
    @EJB
    RetrieveSalesDataSessionBean rsdsb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    Integer companyId;

    private LineChartModel purchaseHistory;
    Vector<String> monthlyDate;
    Vector<Integer> salesdata;
    Integer productId;

    List<String> dateList;
    List<Integer> quantityList;

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

        productId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productId");

        dateList = rsdsb.retrieveDateList(productId);
        quantityList = rsdsb.retrieveQuantityList(productId);

        this.monthlyDate = fsb.createPurchaseDate(dateList);
        this.salesdata = fsb.createPurchaseData(quantityList);

        //put session the size of the history
        size = salesdata.size();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("size", size);

        purchaseHistory = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Sales Figure");

        int k = 0;

        for (int i = (24 - size); i < 24; i++) {
            series1.set(monthlyDate.get(i), salesdata.get(i));
            //Assign largest sales data to k
            if (salesdata.get(i) >= k) {
                k = salesdata.get(i);
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
        String firstDateOnAxis = "2012-05-01";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(monthlyDate.get(24 - size));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -2);
            firstDateOnAxis = df.format(calendar.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(ForecastShowHistoryManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Compute ending date to be shown on x-axis
        String lastDateOnAxis = "2015-02-01";
        try {
            Date date = df.parse(monthlyDate.get(23));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, +2);
            lastDateOnAxis = df.format(calendar.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(ForecastShowHistoryManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        purchaseHistory.addSeries(series1);

        purchaseHistory.setTitle("Sales History on a Monthly Basis");
        purchaseHistory.setZoom(true);
        purchaseHistory.setAnimate(true);
        purchaseHistory.setLegendPosition("se");
        Axis yAxis = purchaseHistory.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(first);

        DateAxis axis = new DateAxis("Dates");
        purchaseHistory.getAxes().put(AxisType.X, axis);
        axis.setMin(firstDateOnAxis);
        axis.setMax(lastDateOnAxis);
        axis.setTickFormat("%b, %y");
        systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "MRP get sales history. ");
    }

    public int getFirstDigit(int num) {
        while (num < -9 || 9 < num) {
            num /= 10;
        }
        return Math.abs(num);
    }

}
