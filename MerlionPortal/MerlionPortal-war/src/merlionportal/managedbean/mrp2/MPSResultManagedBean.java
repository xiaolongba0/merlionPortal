/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.mrp2;

import entity.SystemUser;
import java.io.IOException;
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
import merlionportal.mrp.materialrequirementmodule.MaterialReqPlanningSessionBean;
import merlionportal.mrp.mpsmodule.MpsSessionBean;

/**
 *
 * @author yao
 */
@Named(value = "mPSResultManagedBean")
@ViewScoped
public class MPSResultManagedBean {

    @EJB
    MaterialReqPlanningSessionBean materialReqPlanningSessionBean;

    @EJB
    MpsSessionBean mpsSessionBean;
    @EJB
    UserAccountManagementSessionBean uamb;
    private SystemUser loginedUser;
    Integer companyId;
    Integer productId;

    List<Integer> weeklyDemand;
    Vector<Integer> forecastData;
    Vector<String> forecastDate;

    int thisMonthDemand = 4000;
    int buffer;
    int currentInv;
    int requiredDemand;
    int requiredAmt1;
    int requiredAmt2;
    String requiredDate1;
    String requiredDate2;
    int weeksPerMonth;

    String week1S;
    int week1WorkingDays;
    String week2S;
    int week2WorkingDays;
    String week3S;
    int week3WorkingDays;
    String week4S;
    int week4WorkingDays;
    String week5S;
    int week5WorkingDays;
    int totalWorkingDays;

    int wk1Demand;
    int wk2Demand;
    int wk3Demand;
    int wk4Demand;
    int wk5Demand;
    Integer fResultId;
    Integer mpsId;
    int tempNum;

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

        computeDemand();

    }

    public MPSResultManagedBean() {
    }

    public void computeDemand() {
        forecastData = (Vector<Integer>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("forecastR");
        forecastDate = (Vector<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("monthlyDateR");
        buffer = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("buffer");

        requiredAmt1 = forecastData.get(1);
        requiredAmt2 = forecastData.get(2);

        requiredDate1 = forecastDate.get(1);
        requiredDate2 = forecastDate.get(2);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1;
        try {
            //convert format to Date and get number of weeks in the month.
            date1 = format.parse(requiredDate1);
            Calendar ca = Calendar.getInstance();
            ca.setTime(date1);
            int start = ca.get(Calendar.WEEK_OF_MONTH);

            DateFormat df = new SimpleDateFormat("dd/MM");
            Date startDate = ca.getTime();
            System.out.println("Start Date: " + startDate);

            ca.add(Calendar.MONTH, 1);
            ca.add(Calendar.DATE, -1);
            int end = ca.get(Calendar.WEEK_OF_MONTH);
            Date endDate = ca.getTime();
            System.out.println("End Date: " + endDate);
            weeksPerMonth = end - start + 1;

            //reset time to the begining of the month
            ca.setTime(date1);
            ca.setMinimalDaysInFirstWeek(1);

            //produce the date string of week 1
            int j = 0;
            for (int i = 1; i <= 7; i++) {
                if (ca.get(Calendar.WEEK_OF_MONTH) == 1) {
                    ca.add(Calendar.DATE, +1);
                    j++;
                } else {
                    i = 8;
                }
            }

            week1S = df.format(date1) + "-" + df.format(ca.getTime());
            System.out.println("XXXXXXXXXXXXXXXX week 1 string:  " + week1S);
            week1WorkingDays = j - 1;
            System.out.println("XXXXXXXXXXXXXXXX week1WorkingDays:  " + week1WorkingDays);

            //produce the date string of week 2
            week2WorkingDays = 5;
            ca.add(Calendar.DATE, +1);
            week2S = df.format(ca.getTime());
            ca.add(Calendar.DATE, +6);
            week2S += "-" + df.format(ca.getTime());

            System.out.println("XXXXXXXXXXXXXXXX week 2 string:  " + week2S);
            System.out.println("XXXXXXXXXXXXXXXX week2WorkingDays:  " + week2WorkingDays);

            //produce the date string of week 3
            week3WorkingDays = 5;
            ca.add(Calendar.DATE, +1);
            week3S = df.format(ca.getTime());
            ca.add(Calendar.DATE, +6);
            week3S += "-" + df.format(ca.getTime());
            System.out.println("XXXXXXXXXXXXXXXX week 3 string:  " + week3S);
            System.out.println("XXXXXXXXXXXXXXXX week3WorkingDays:  " + week3WorkingDays);

            //produce the date string of week 4
            week4WorkingDays = 5;
            ca.add(Calendar.DATE, +1);
            week4S = df.format(ca.getTime());
            ca.add(Calendar.DATE, +6);
            week4S += "-" + df.format(ca.getTime());
            System.out.println("XXXXXXXXXXXXXXXX week 4 string:  " + week4S);
            System.out.println("XXXXXXXXXXXXXXXX week4WorkingDays:  " + week4WorkingDays);

            //produce the date string of week 5
            ca.add(Calendar.DATE, +1);
            week5S = df.format(ca.getTime());
            int a = 0;
            int b = 0;
            while (ca.get(Calendar.WEEK_OF_MONTH) == 5) {
                if (ca.getTime().before(endDate) || ca.getTime().equals(endDate)) {
                    a++;
                    ca.add(Calendar.DATE, +1);
                }

                if (ca.getTime().after(endDate)) {
                    b++;
                    ca.add(Calendar.DATE, +1);
                }

            }
            week5S += "-" + df.format(ca.getTime());

            week5WorkingDays = a;

            ca.add(Calendar.DATE, +6);

            System.out.println("XXXXXXXXXXXXXXXX week 5 string:  " + week5S);
            System.out.println("XXXXXXXXXXXXXXXX week5WorkingDays:  " + week5WorkingDays);

            //check if a date belongs to a month
            totalWorkingDays = week1WorkingDays + week2WorkingDays + week3WorkingDays + week4WorkingDays + week5WorkingDays;

            currentInv = 100;
            requiredDemand = requiredAmt1 - currentInv + buffer;

            System.out.println("SEE required demand: " + requiredDemand);

            wk1Demand = (week1WorkingDays * requiredDemand) / totalWorkingDays;
            wk2Demand = (week2WorkingDays * requiredDemand) / totalWorkingDays;
            wk3Demand = (week3WorkingDays * requiredDemand) / totalWorkingDays;
            wk4Demand = (week4WorkingDays * requiredDemand) / totalWorkingDays;
            wk5Demand = (week5WorkingDays * requiredDemand) / totalWorkingDays;

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("wk1Demand", wk1Demand);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("wk2Demand", wk2Demand);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("wk3Demand", wk3Demand);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("wk4Demand", wk4Demand);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("wk5Demand", wk5Demand);

        } catch (ParseException ex) {
            Logger.getLogger(MPSResultManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        fResultId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("forecastResultID");
        mpsId = mpsSessionBean.storeMPS(buffer, requiredDemand, fResultId);
        System.out.println("RRRRRRRRRRRRR: mps id: " + mpsId);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mpsId",  mpsId);
        
    }

    //intake string to display
    public void computeDate() {

    }

    public void onBufferChange() {
        if (buffer != 0) {
            requiredDemand = requiredAmt1 - currentInv + buffer;
            System.out.println("RRRRRRRRRRRRRRR");
        }
    }

    public int getThisMonthDemand() {
        return thisMonthDemand;
    }

    public int getRequiredAmt1() {
        return requiredAmt1;
    }

    public int getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public String getWeek1S() {
        return week1S;
    }

    public int getWeek1WorkingDays() {
        return week1WorkingDays;
    }

    public String getWeek2S() {
        return week2S;
    }

    public int getWeek2WorkingDays() {
        return week2WorkingDays;
    }

    public String getWeek3S() {
        return week3S;
    }

    public int getWeek3WorkingDays() {
        return week3WorkingDays;
    }

    public String getWeek4S() {
        return week4S;
    }

    public int getWeek4WorkingDays() {
        return week4WorkingDays;
    }

    public String getWeek5S() {
        return week5S;
    }

    public int getWeek5WorkingDays() {
        return week5WorkingDays;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public void setCurrentInv(int currentInv) {
        this.currentInv = currentInv;
    }

    public int getBuffer() {
        return buffer;
    }

    public int getCurrentInv() {
        return currentInv;
    }

    public void setRequiredDemand(int requiredDemand) {
        this.requiredDemand = requiredDemand;
    }

    public int getRequiredDemand() {
        return requiredDemand;
    }

    public int getWk1Demand() {
        return wk1Demand;
    }

    public int getWk2Demand() {
        return wk2Demand;
    }

    public int getWk3Demand() {
        return wk3Demand;
    }

    public int getWk4Demand() {
        return wk4Demand;
    }

    public int getWk5Demand() {
        return wk5Demand;
    }

    public String backToMPSBuffer() {
        return ("mps");
    }

    public String proceedToMaterialReq() {
        return ("materialreq");
    }

}
