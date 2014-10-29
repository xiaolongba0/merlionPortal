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

//    private Integer locationId;
//    private String assetType;
//    private Integer capacity;
//    private Integer speed;
//    private Integer price;
//    private Location locationlocationId;
//    private String status;
//    private Integer newAssetId;
//    private Integer quantity;
    private Integer companyId;
    private String operatorName;
    private String operatorStatus;
    private Date birthday;
    private String operatorGender;
    private String operatorType;

    private Integer newOperatorId;

    private SystemUser loginedUser;
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



}
