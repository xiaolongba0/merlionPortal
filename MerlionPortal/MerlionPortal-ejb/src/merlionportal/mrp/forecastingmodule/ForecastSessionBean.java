/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.mrp.forecastingmodule;

import entity.ForecastResult;
import entity.MonthForecastResult;
import entity.Mps;
import entity.Product;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author yao
 */
@Stateless
@LocalBean
public class ForecastSessionBean {

    @PersistenceContext
    private EntityManager entityManager;
    private Product product;

    Integer companyId;

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

    double expectedGrowth;
    int periodicity;

    public List<Product> getMyProducts(Integer companyId) {
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyId);
        return query.getResultList();
    }

    //take in two string from database (sales order fr OES), monthlyDate and salesdata, return two string
    //Parameter: ProducId
    public Vector<String> createPurchaseDate(List<String> dateList) {
        //produce a list of date correspond to sales
        //size need to be retreved/computed later
        // later get this size from managed bean   size = 24;
        monthlyDate = new Vector<String>();
        for (int i = 0; i < dateList.size(); i++) {
            monthlyDate.add(dateList.get(i));
        }
        return monthlyDate;
    }

    public List<MonthForecastResult> retrieveMonthForecastResult(Integer forecastResultID) {
        ForecastResult forecastRes = entityManager.find(ForecastResult.class, forecastResultID);
        List<MonthForecastResult> monthForecastResults = forecastRes.getMonthForecastResultList();
        System.out.println("FY monthForecastResult 1 : " + monthForecastResults.get(0).getMonthName());
        return monthForecastResults;
    }

    public Vector<Integer> createPurchaseData(List<Integer> quantityList) {
        //produce a list of date correspond to sales
        //size need to be retreved/computed later
        // later get this size from managed bean   size = 24;

        salesdata = new Vector();
        for (int i = 0; i < quantityList.size(); i++) {
            salesdata.add(quantityList.get(i));
        }
        return salesdata;
    }

    public Vector<Integer> computeResult(int periodicity, double expectedGrowth, List<String> dateList, List<Integer> quantityList) {
        this.periodicity = periodicity;
        this.expectedGrowth = expectedGrowth;

        expectedGrowth = (double) (expectedGrowth / 100.00) + 1.00;

//produce a list of date correspond to sales
        //size need to be retreved/computed later
        this.monthlyDate = createPurchaseDate(dateList);
        this.salesdata = createPurchaseData(quantityList);
        int size = salesdata.size();

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
        //periodicity = 12;
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
                forecastR.add((int) Math.round((m * i + b) * (finalSeasonalFactor.get(j)) * expectedGrowth));
                j++;
            }

            // get a max number for graph y-axis
            int max = 0;
            for (int i = 1; i <= periodicity; i++) {
                if (forecastR.get(i) > max) {
                    max = forecastR.get(i);
                }
            }

            //later return max to managed bean
            max = max + 2000;

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
                forecastR.add((int) Math.round((m * i + b) * (finalSeasonalFactor.get(j)) * expectedGrowth));
                j++;
            }

            int max = 0;
            for (int i = 1; i <= periodicity; i++) {
                if (forecastR.get(i) > max) {
                    max = forecastR.get(i);
                }
            }
            max = max + 2000;
        }

        return forecastR;
    }

    public Integer storeForecast(int periodicity, double expectedGrowth, List<String> dateList, List<Integer> quantityList) {
        ForecastResult forecastResult = new ForecastResult();
        forecastResult.setForecastResultDate(new Date());
        forecastResult.setPeriodicity(periodicity);
        forecastResult.setFluctuation(expectedGrowth);
        forecastResult.setProductId(1);

    //    Mps mps = new Mps();
     //   forecastResult.setMps(mps);

        List<MonthForecastResult> monthForecastResults = new ArrayList<MonthForecastResult>();

        Vector<String> dateNameTemp = this.yaxisDate();
        Vector<String> dateName = new Vector<String>();
        dateName.add("0");
        
        for (int i = 1; i < dateNameTemp.size(); i++) {
            dateName.add(dateNameTemp.get(i).substring(0, 7));
        }

        Vector<Integer> quantity = this.computeResult(periodicity, expectedGrowth, dateList, quantityList);

        for (int i = 1; i <= periodicity; i++) {
            MonthForecastResult monthForecastResult = new MonthForecastResult();
            monthForecastResult.setMonthName(dateName.get(i));

            monthForecastResult.setForecastedQuantity(quantity.get(i));

            monthForecastResult.setForecastResult(forecastResult);
            monthForecastResults.add(monthForecastResult);
        }

        forecastResult.setMonthForecastResultList(monthForecastResults);

        entityManager.persist(forecastResult);
        entityManager.flush();

        return forecastResult.getForecastResultId();
    }

    public Vector<String> yaxisDate() {
        monthlyDateR = new Vector<String>();
        monthlyDateR.add("0");

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
        monthlyDateR.add("2016-10-01");

        return monthlyDateR;
    }

}
