/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.oes.reportmanagementmodule.MonthlyReportManagerSessionBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author mac
 */
@Named(value = "monthlyReportManagedBean")
@ViewScoped
public class MonthlyReportManagedBean {
    
    @EJB
    private MonthlyReportManagerSessionBean monthlyMB;
    
    private Integer companyId;
    private Integer userId;
    private List<ProductOrder> currentMonthOrders;
    private List<ProductOrder> prevMonthOrders;
    private LineChartModel dateModel = new LineChartModel();
    private LineChartModel lineModel2;
    private Double min = 1000.0;
    private Double max = 50.0;
    
    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            
            if (userId != null) {
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
        
        createMyModels();
        
    }
    
    public MonthlyReportManagedBean() {
    }
    
    private void createMyModels() {
        
        lineModel2 = initMyModel();
        
        lineModel2.setTitle("Sales Trend ");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Value $");
        yAxis.setMin(min - 100);
        yAxis.setMax(max + 100);
    }
    
    private LineChartModel initMyModel() {
        Double totalValue = 0.0;
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int currentTotalDays = monthlyMB.getTotalNumberOfDays(currentMonth, currentYear);
        int prevMonth = currentMonth--;
        int prevYear = currentYear;
        if (prevMonth == 0) {
            prevMonth = 12;
            prevYear = currentYear - 1;
        }
        int prevTotalDays = monthlyMB.getTotalNumberOfDays(prevMonth, prevYear);
        
        LineChartModel model = new LineChartModel();
        ChartSeries series1 = new ChartSeries();
        ChartSeries series2 = new ChartSeries();
        
        currentMonthOrders = monthlyMB.findAllProduct(companyId, currentMonth);
        prevMonthOrders = monthlyMB.findAllProduct(companyId, prevMonth);
        
        for (int i = 0; i <= currentTotalDays; i++) {
            int day = i + 1;
            totalValue = monthlyMB.getTotalValueofDay(currentMonthOrders, day);
            if (totalValue < min) {
                min = totalValue;
            }
            if (totalValue > max) {
                max = totalValue;
            }
            String xPoint = Integer.toString(day);            
            series1.set(xPoint, totalValue);            
        }
        for (int i = 0; i <= prevTotalDays; i++) {
            int day = i + 1;
            totalValue = monthlyMB.getTotalValueofDay(prevMonthOrders, day);
            if (totalValue < min) {
                min = totalValue;
            }
            if (totalValue > max) {
                max = totalValue;
            }
            String xPoint = Integer.toString(day);            
            series2.set(xPoint, totalValue);            
        }
        
        series1.setLabel("Current Month");
        series2.setLabel("Previous Month");
        
        
        model.addSeries(series1);
        model.addSeries(series2);
        
        return model;
    }
    
    public Integer getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public List<ProductOrder> getCurrentMonthOrders() {
        return currentMonthOrders;
    }
    
    public void setCurrentMonthOrders(List<ProductOrder> currentMonthOrders) {
        this.currentMonthOrders = currentMonthOrders;
    }
    
    public List<ProductOrder> getPrevMonthOrders() {
        return prevMonthOrders;
    }
    
    public void setPrevMonthOrders(List<ProductOrder> prevMonthOrders) {
        this.prevMonthOrders = prevMonthOrders;
    }
    
    public LineChartModel getDateModel() {
        return dateModel;
    }
    
    public void setDateModel(LineChartModel dateModel) {
        this.dateModel = dateModel;
    }
    
    public LineChartModel getLineModel2() {
        return lineModel2;
    }
    
    public void setLineModel2(LineChartModel lineModel2) {
        this.lineModel2 = lineModel2;
    }
    
    public Double getMin() {
        return min;
    }
    
    public void setMin(Double min) {
        this.min = min;
    }
    
    public Double getMax() {
        return max;
    }
    
    public void setMax(Double max) {
        this.max = max;
    }
    
}
