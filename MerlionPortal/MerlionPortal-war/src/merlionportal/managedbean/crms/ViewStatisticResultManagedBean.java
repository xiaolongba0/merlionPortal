/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.accountmanagementmodule.KeyAccountManagementSessionBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 *
 * @author manliqi
 */
@Named(value = "viewStatisticResultManagedBean")
@ViewScoped
public class ViewStatisticResultManagedBean {

    /**
     * Creates a new instance of ViewStatisticResultManagedBean
     */
    private Integer companyId;
    private Integer userId;

    private Date startDate;
    private Date endDate;

    private List<Double> amounts;
    private List<String> companys;

    @EJB
    KeyAccountManagementSessionBean keyAccountMSB;
    @EJB
    SystemLogSessionBean logSB;

    private HorizontalBarChartModel horizontalBarModel;

    public ViewStatisticResultManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
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
        startDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("orderStatisticStartDate");
        endDate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("orderStatisticEndDate");

        amounts = keyAccountMSB.calculateOrderAmountForAllCompanyies(startDate, endDate, companyId);
        companys = keyAccountMSB.produceCompanyList(startDate, endDate, companyId);

        if (amounts != null || companys != null) {
            createBarModels();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no data available for analysis", "!"));
        }
        logSB.recordSystemLog(userId, "CRMS viewed statistic result of order amount");

    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    private void createBarModels() {
        createHorizontalBarModel();
    }

    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();

        ChartSeries data = new ChartSeries();
        data.setLabel("Company");
        for (int i = 0; i < amounts.size(); i++) {
            data.set(companys.get(i), amounts.get(i));
        }

        horizontalBarModel.addSeries(data);

        horizontalBarModel.setTitle("Company vs Order amount");
        horizontalBarModel.setLegendPosition("e");

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Order Amount");
//        xAxis.setMin(0);
//        xAxis.setMax(200);

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Company");
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

    public List<Double> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Double> amounts) {
        this.amounts = amounts;
    }

    public List<String> getCompanys() {
        return companys;
    }

    public void setCompanys(List<String> companys) {
        this.companys = companys;
    }

}
