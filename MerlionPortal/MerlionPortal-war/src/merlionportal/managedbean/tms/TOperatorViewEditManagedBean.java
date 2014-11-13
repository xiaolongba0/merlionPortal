/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import entity.SystemUser;
import entity.Location;
import entity.Company;
import entity.TransportationOperator;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.tms.transportationhumanresourcemanagementmodule.TOperatormanagementSessionBean;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import java.io.Serializable;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Yuanbo
 */
@Named(value = "tOperatorViewEditManagedBean")
@ViewScoped
public class TOperatorViewEditManagedBean {

    @EJB
    private TOperatormanagementSessionBean toperatorManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;

    private List<TransportationOperator> operators;
    private TransportationOperator operator;

    private Integer companyId;
    private String operatorName;
    private String operatorStatus;
    private Date birthday;
    private String operatorGender;
    private String operatorType;

    private Integer newOperatorId;

    private SystemUser loginedUser;
    private BarChartModel barModel;
    private Integer avD;
    private Integer nD;
    private Integer avO;
    private Integer nO;

    /**
     * Creates a new instance of TOperatorViewEditManagedBean
     */
    public TOperatorViewEditManagedBean() {
    }

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
        operators = toperatorManagementSessionBean.viewMyOperator(companyId);
        avD = toperatorManagementSessionBean.countMyAvailableDriver(companyId);
        nD = toperatorManagementSessionBean.countMyNotAvailableDriver(companyId);
        avO = toperatorManagementSessionBean.countMyAvailableOperator(companyId);
        nO = toperatorManagementSessionBean.countMyNotAvailableOperator(companyId);
        barModel = initBarModel(avD, nD, avO, nO);

    }

    public void onCompanyChange() {
        System.out.println("===============================[In Managed Bean - get Operators]");
        System.out.println("[In Managed Bean - getCompany] company ID : " + companyId);
        if (companyId != null) {
            operators = toperatorManagementSessionBean.viewMyOperator(companyId);
            if (operators == null) {
                System.out.println("============== FAILED TO VIEW OPERATOR ===============");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to View Storage Type. Please check if company ID exists! ", ""));
            }

        }

    }

    private BarChartModel initBarModel(Integer availableDriver, Integer notDriver, Integer availableOpt, Integer notOpt) {
        BarChartModel model = new BarChartModel();

        ChartSeries boys = new ChartSeries();
        boys.setLabel("Available Driver");
        boys.set(" ", availableDriver);

        ChartSeries boys2 = new ChartSeries();
        boys2.setLabel("On Leave Driver");
        boys2.set(" ", notDriver);

        ChartSeries boys3 = new ChartSeries();
        boys3.setLabel("Available Operator");
        boys3.set(" ", availableOpt);

        ChartSeries girls = new ChartSeries();
        girls.setLabel("On Leave Opertor");
        girls.set(" ", notOpt);

        model.addSeries(boys);
        model.addSeries(boys2);
        model.addSeries(boys3);
        model.addSeries(girls);

        model.setTitle("HR Status Summary");
        model.setLegendPosition("ne");
        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel("Status");
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("HeadCount");

        return model;
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("IN ROW EDIT =================");
        operator = new TransportationOperator();
        operator = (TransportationOperator) event.getObject();
        System.out.println("[AFTER EDIT] Operator.getName(): " + operator.getOperatorName());
        toperatorManagementSessionBean.editOperator(operatorStatus, Boolean.FALSE);
        FacesMessage msg = new FacesMessage("Operator with operator ID = " + operator.getOperatorId() + " has sucessfully been edited");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteTOperator(TransportationOperator operator) {
        try {
            System.out.println("[In WAR FILE - Delete Operator Function]" + operator);
            System.out.println("[In WAR FILE - Delete Operator Function] TransportationAsset Id===========:" + operator.getOperatorId());
            toperatorManagementSessionBean.deleteOperator(operator.getOperatorId());
            operators.remove(operator);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Operaotr is deleted"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<TransportationOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<TransportationOperator> operators) {
        this.operators = operators;
    }

    public TransportationOperator getOperator() {
        return operator;
    }

    public void setOperator(TransportationOperator operator) {
        this.operator = operator;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(String operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getOperatorGender() {
        return operatorGender;
    }

    public void setOperatorGender(String operatorGender) {
        this.operatorGender = operatorGender;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getNewOperatorId() {
        return newOperatorId;
    }

    public void setNewOperatorId(Integer newOperatorId) {
        this.newOperatorId = newOperatorId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public Integer getAvD() {
        return avD;
    }

    public void setAvD(Integer avD) {
        this.avD = avD;
    }

    public Integer getnD() {
        return nD;
    }

    public void setnD(Integer nD) {
        this.nD = nD;
    }

    public Integer getAvO() {
        return avO;
    }

    public void setAvO(Integer avO) {
        this.avO = avO;
    }

    public Integer getnO() {
        return nO;
    }

    public void setnO(Integer nO) {
        this.nO = nO;
    }

}
