/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.managedbean.mrp2;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
 
@ManagedBean
public class ForecastShowHistoryManagedBean implements Serializable {
 
    private LineChartModel purchaseHistory;
 
 
    @PostConstruct
    public void init() {
        createPurchaseHistoryModels();
    }
 
    public LineChartModel getPurchaseHistory() {
        return purchaseHistory;
    }
 
    private void createPurchaseHistoryModels() {
        purchaseHistory = new LineChartModel();
        
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
 
        purchaseHistory.addSeries(series1);
         

        purchaseHistory.setTitle("Line Chart");
        purchaseHistory.setAnimate(true);
        purchaseHistory.setLegendPosition("se");
        Axis yAxis = purchaseHistory.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
         
    }
     
    
}
