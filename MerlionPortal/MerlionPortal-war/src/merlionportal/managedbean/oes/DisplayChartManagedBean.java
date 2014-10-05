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
    private Double min = 1000.0;
    private Double max = 1000.0;

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
        int totalMonth = reportMB.retrieveTotalMonth(startDate, endDate);
        LineChartModel model = new LineChartModel();
        ChartSeries series1 = new ChartSeries();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int startMonth = cal.get(Calendar.MONTH);
        startMonth++;
        int startYear = cal.get(Calendar.YEAR);
        System.out.println("+++++++++++++++++++++++Init category model " + startMonth + " " + startYear);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(endDate);
        int endMonth = cal1.get(Calendar.MONTH);
        int endYear = cal1.get(Calendar.YEAR);
        if (endMonth == 12) {
            endMonth = 1;
            endYear++;
        } else {
            endMonth++;
        }
        String maxDate = Integer.toString(endYear) + "-" + Integer.toString(endMonth);

        System.out.println("Total monthes selected " + totalMonth);

        for (int i = 1; i <= totalMonth; i++) {
            totalValue = reportMB.getTotalValueOfMonth(productOrderList, startMonth, startYear);
            if (totalValue < min) {
                min = totalValue;
            }
            if (totalValue > max) {
                max = totalValue;
            }
            String xPoint = Integer.toString(startYear) + "-" + Integer.toString(startMonth);
            System.out.println("starting year and month  " + startYear + " " + startMonth);
            System.out.println("Xpoint   " + xPoint);
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
