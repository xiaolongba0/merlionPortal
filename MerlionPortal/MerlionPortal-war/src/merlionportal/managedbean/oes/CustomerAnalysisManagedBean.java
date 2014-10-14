/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.oes.ordermanagement.CommonFunctionSessionBean;
import merlionportal.oes.reportmanagementmodule.ReportManagerSessionBean;
import merlionportal.utility.DTOReportList;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author mac
 */
@Named(value = "customerAnalysisManagedBean")
@ViewScoped
public class CustomerAnalysisManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private ReportManagerSessionBean reportMB;
    @EJB
    private CommonFunctionSessionBean commonMB;

    private Integer companyId;
    private Integer userId;
    private List<ProductOrder> productOrderList;
    private Integer customerId;
    private Date firstStartDate;
    private Date firstEndDate;
    private Date secondStartDate;
    private Date secondEndDate;
    private List<ProductOrder> secondOrderList;
    private LineChartModel dateModel = new LineChartModel();
    private LineChartModel lineModel2;
    private Double min = 50.0;
    private Double max = 50.0;
    private List firstList = new ArrayList();
    private List secondList = new ArrayList();
    private int minMonth;
    private List<DTOReportList> myReportList;

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
        firstStartDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("firstStartDate");
        firstEndDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("firstEndDate");
        customerId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("reportCustomer");
        secondStartDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("secondStartDate");
        secondEndDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("secondEndDate");
        productOrderList = (List<ProductOrder>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FirstOrderList");
        secondOrderList = (List<ProductOrder>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SecondOrderList");
        createMyModels();
        myReportList = this.initMyList(firstList, secondList);

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
        int totalMonth1 = reportMB.retrieveTotalMonth(firstStartDate, firstEndDate);
        int totalMonth2 = reportMB.retrieveTotalMonth(secondStartDate, secondEndDate);

        LineChartModel model = new LineChartModel();
        ChartSeries series1 = new ChartSeries();
        ChartSeries series2 = new ChartSeries();

        Calendar cal = Calendar.getInstance();
        cal.setTime(firstStartDate);
        int startMonth = cal.get(Calendar.MONTH);
        startMonth++;
        int startYear = cal.get(Calendar.YEAR);

        Calendar cal2 = Calendar.getInstance();
        cal.setTime(secondStartDate);
        int startMonth1 = cal.get(Calendar.MONTH);
        startMonth++;
        int startYear1 = cal.get(Calendar.YEAR);

        for (int i = 1; i <= totalMonth1; i++) {
            totalValue = reportMB.getTotalValueOfMonth(productOrderList, startMonth, startYear);
            if (totalValue < min) {
                min = totalValue;
            }
            if (totalValue > max) {
                max = totalValue;
            }
            String xPoint = Integer.toString(i);
            series1.set(xPoint, totalValue);
            firstList.add(totalValue);
            if (startMonth == 12) {
                startMonth = 1;
                startYear++;
            } else {
                startMonth++;
            }
        }
        for (int i = 1; i <= totalMonth1; i++) {
            totalValue = reportMB.getTotalValueOfMonth(secondOrderList, startMonth1, startYear1);
            if (totalValue < min) {
                min = totalValue;
            }
            if (totalValue > max) {
                max = totalValue;
            }
            String xPoint = Integer.toString(i);
            series2.set(xPoint, totalValue);
            secondList.add(totalValue);
            if (startMonth1 == 12) {
                startMonth1 = 1;
                startYear1++;
            } else {
                startMonth1++;
            }
        }

        series1.setLabel("First Period");
        series2.setLabel("Second Period");

        if (totalMonth1 >= totalMonth2) {
            minMonth = totalMonth2;
        } else {
            minMonth = totalMonth1;
        }

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }

    public CustomerAnalysisManagedBean() {
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

    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrder> productOrderList) {
        this.productOrderList = productOrderList;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getFirstStartDate() {
        return firstStartDate;
    }

    public void setFirstStartDate(Date firstStartDate) {
        this.firstStartDate = firstStartDate;
    }

    public Date getFirstEndDate() {
        return firstEndDate;
    }

    public void setFirstEndDate(Date firstEndDate) {
        this.firstEndDate = firstEndDate;
    }

    public Date getSecondStartDate() {
        return secondStartDate;
    }

    public void setSecondStartDate(Date secondStartDate) {
        this.secondStartDate = secondStartDate;
    }

    public Date getSecondEndDate() {
        return secondEndDate;
    }

    public void setSecondEndDate(Date secondEndDate) {
        this.secondEndDate = secondEndDate;
    }

    public List<ProductOrder> getSecondOrderList() {
        return secondOrderList;
    }

    public void setSecondOrderList(List<ProductOrder> secondOrderList) {
        this.secondOrderList = secondOrderList;
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

    public List getFirstList() {
        return firstList;
    }

    public void setFirstList(List firstList) {
        this.firstList = firstList;
    }

    public List getSecondList() {
        return secondList;
    }

    public void setSecondList(List secondList) {
        this.secondList = secondList;
    }

    public int getMinMonth() {
        return minMonth;
    }

    public void setMinMonth(int minMonth) {
        this.minMonth = minMonth;
    }

    public List<Map> initTable() {
        List<Map> myMap = new ArrayList();
        for (int i = 0; i < minMonth; i++) {
            Map mapA = new HashMap();
            mapA.put("first", firstList.get(i));
            mapA.put("second", secondList.get(i));
            myMap.add(mapA);

        }
        return myMap;
    }

    public List<DTOReportList> getMyReportList() {
        return myReportList;
    }

    public void setMyReportList(List<DTOReportList> myReportList) {
        this.myReportList = myReportList;
    }

    public List<DTOReportList> initMyList(List myFirst, List mySecond) {
        List<DTOReportList> myResult = new ArrayList();
        for (int i = 0; i < minMonth; i++) {
            DTOReportList d1 = new DTOReportList();
            d1.setFirst((Double) myFirst.get(i));
            d1.setSecond((Double) mySecond.get(i));
            myResult.add(d1);
        }
        return myResult;
    }

}
