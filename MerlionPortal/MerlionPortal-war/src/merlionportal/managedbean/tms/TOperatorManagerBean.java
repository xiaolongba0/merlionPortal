
package merlionportal.managedbean.tms;

import javax.inject.Named;
import entity.SystemUser;
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


/**
 *
 * @author Yuanbo
 */
@Named(value = "tOperatorManagerBean")
@RequestScoped
public class TOperatorManagerBean {

    @EJB
    private TOperatormanagementSessionBean toperatorManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    




    private Integer companyId;

    private SystemUser loginedUser;
    
//  -------------Operator
    private String operatorName;
    private String operatorStatus;
    private Date birthday;
    private String operatorGender;
    private String operatorType;
    private List<TransportationOperator> operators;
    private Integer newOperatorId;
    
    /**
     * Creates a new instance of AssestManagedBean
     */
    public TOperatorManagerBean() {
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
    


    public void createNewOperator (ActionEvent company) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Operator");
            System.out.println("OperatorType:" + operatorType);
           
            newOperatorId = toperatorManagementSessionBean.addNewOperator(operatorName, operatorGender, birthday, operatorType, operatorStatus, companyId);
            System.out.println("NEW TRANSPORTATION ASSET ID =================: " + newOperatorId);
            if (newOperatorId == -1) {
                clearAllFields();
                System.out.println("============== FAILED TO ADD TRANSPORTATION ASSET DUE TO LOCATION ID ===============");

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to Add Transportation Operator. Please check Company Id! ", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Transportation Operator Added!", ""));
            }
            
            System.out.println("[WAR FILE]===========================Create New Transportation Asset");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }   
    
    private void clearAllFields(){
        
        operatorName = null;
        operatorGender = null;
        birthday = null;
        operatorType = null;
        operatorStatus = null;
        
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
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

    public List<TransportationOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<TransportationOperator> operators) {
        this.operators = operators;
    }

    public Integer getNewOperatorId() {
        return newOperatorId;
    }

    public void setNewOperatorId(Integer newOperatorId) {
        this.newOperatorId = newOperatorId;
    }
     

  
    
}
