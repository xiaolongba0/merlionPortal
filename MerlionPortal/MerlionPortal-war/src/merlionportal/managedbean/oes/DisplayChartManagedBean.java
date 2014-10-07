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
import merlionportal.oes.ordermanagement.CommonFunctionSessionBean;
import merlionportal.oes.reportmanagementmodule.ReportManagerSessionBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author mac
 */
@Named(value = "displayChartManagedBean")
@ViewScoped
public class DisplayChartManagedBean {

    @EJB
    private ReportManagerSessionBean reportMB;
    @EJB
    private CommonFunctionSessionBean commonMB;
    private Integer companyId;
    private Integer userId;
    private Date startDate;
    private Date endDate;
    private List<ProductOrder> productOrderList;
    private LineChartModel dateModel = new LineChartModel();
    private LineChartModel lineModel2;
    private Double min = 50.0;
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
        startDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("StartDate");
        endDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EndDate");
        productOrderList = (List<ProductOrder>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("OrderList");
        createMyModels();

    }

    private void createMyModels() {
        int totalMonth = reportMB.retrieveTotalMonth(startDate, endDate);
        if (totalMonth > 2) {
            lineModel2 = initMyModel();
        } else {
            lineModel2 = initDayModel();
        }
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
        int totalMonth = reportMB.retrieveTotalMonth(startDate, endDate);
        LineChartModel model = new LineChartModel();
        ChartSeries series1 = new ChartSeries();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int startMonth = cal.get(Calendar.MONTH);
        startMonth++;
        int startYear = cal.get(Calendar.YEAR);
        Calendar cal1 = Calendar.getInstance();
        cal.setTime(endDate);
        int endMonth = cal1.get(Calendar.MONTH);
        endMonth++;
        int endYear = cal1.get(Calendar.YEAR);

        for (int i = 1; i <= totalMonth; i++) {
            totalValue = reportMB.getTotalValueOfMonth(productOrderList, startMonth, startYear);
            if (totalValue < min) {
                min = totalValue;
            }
            if (totalValue > max) {
                max = totalValue;
            }
            String xPoint = Integer.toString(startYear) + "-" + Integer.toString(startMonth);
            series1.set(xPoint, totalValue);
            if (startMonth == 12) {
                startMonth = 1;
                startYear++;
            } else {
                startMonth++;
            }
        }

        series1.setLabel("Sales");

        model.addSeries(series1);

        return model;
    }

    private LineChartModel initDayModel() {
        System.out.println("start date and end date  " + startDate + " " + endDate);
        Double totalValue = 0.0;
        long totalDay = reportMB.retrieveTotalDay(startDate, endDate);
        System.out.println("total day of period  " + totalDay);
        LineChartModel model = new LineChartModel();
        ChartSeries series1 = new ChartSeries();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int startDay = cal.get(Calendar.DAY_OF_MONTH);
        int startMonth = cal.get(Calendar.MONTH);
        startMonth++;
        int startYear = cal.get(Calendar.YEAR);
        Calendar cal1 = Calendar.getInstance();
        cal.setTime(startDate);
        int endDay = cal1.get(Calendar.DAY_OF_MONTH);
        int endMonth = cal1.get(Calendar.MONTH);
        endMonth++;
        int endYear = cal1.get(Calendar.YEAR);

        for (int i = 1; i <= totalDay; i++) {
            totalValue = reportMB.getTotalValueofDay(productOrderList, startDay, startMonth, startYear);
            if (totalValue < min) {
                min = totalValue;
            }
            if (totalValue > max) {
                max = totalValue;
            }
            String xPoint = Long.toString(startDay);
            series1.set(xPoint, totalValue);
            if (startDay == 31 && (startMonth == 1 || startMonth == 3 || startMonth == 5 || startMonth == 7 || startMonth == 8 || startMonth == 10)) {
                startMonth++;
                startDay = 1;
            } else if (startMonth == 12 && startDay == 31) {
                startMonth = 1;
                startDay = 1;
                startYear++;
            } else if (startDay == 30 && (startMonth == 4 || startMonth == 6 || startMonth == 9 || startMonth == 11)) {
                startMonth++;
                startDay = 1;
            } else if (startMonth == 2 && startDay == 29 && startYear % 4 == 0) {
                startMonth++;
                startDay = 1;
            } else if (startMonth == 2 && startDay == 28 && startYear % 4 != 0) {
                startMonth++;
                startDay = 1;
            } else {
                startDay++;
            }
        }

        series1.setLabel("Sales");

        model.addSeries(series1);

        return model;
    }

    public String customerInfo(int customerId) {
        String result;
        result = commonMB.viewCustomerName(customerId);
        return result;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrder> productOrderList) {
        this.productOrderList = productOrderList;
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

}
