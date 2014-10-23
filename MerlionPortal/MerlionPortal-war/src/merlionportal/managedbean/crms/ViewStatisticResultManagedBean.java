/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
    private HorizontalBarChartModel horizontalBarModel;

    public ViewStatisticResultManagedBean() {
    }

    @PostConstruct
    public void init() {
        createBarModels();
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    private void createBarModels() {
        createHorizontalBarModel();
    }

    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();

        ChartSeries companys = new ChartSeries();
        companys.setLabel("Boys");
        companys.set("1", 50);
        companys.set("3", 96);
        companys.set("6", 44);
        companys.set("7", 55);
        companys.set("9", 25);

        horizontalBarModel.addSeries(companys);

        horizontalBarModel.setTitle("Company vs Order amount");
        horizontalBarModel.setLegendPosition("e");

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Order Amount");
        xAxis.setMin(0);
        xAxis.setMax(200);

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Company");
    }

}
