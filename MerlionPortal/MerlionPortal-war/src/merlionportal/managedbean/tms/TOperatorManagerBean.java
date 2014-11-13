package merlionportal.managedbean.tms;

import javax.inject.Named;
import entity.SystemUser;
import entity.Company;
import entity.TransportationOperator;
import entity.UserRole;
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
import merlionportal.ci.administrationmodule.GetCompanyRoleSessionBean;
import java.util.List;
import merlionportal.utility.MD5Generator;

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
    @EJB
    private GetCompanyRoleSessionBean gcrsb;

    private Integer companyId;

    private SystemUser loginedUser;

//  -------------Operator
    private String operatorName;
    private String operatorLastName;
    private String operatorStatus;
    private String gender;
    private Date birthday;
    private String operatorType;
    private List<TransportationOperator> operators;
    private Integer newOperatorId;
    private String emailaddress;
    private String password;
    private String contactNumber;
    private List<UserRole> roles;
    private Integer roleId;

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
        roles = gcrsb.getAllRolesInCompany(companyId);

    }

    public void createNewOperator(ActionEvent company) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Operator");
            System.out.println("OperatorType:" + operatorType);
            Integer operatorId = loginedUser.getSystemUserId();
            String hashedPass = MD5Generator.hash("pass");
            if (uamb.getUserByEmail(emailaddress) == null) {
                newOperatorId = toperatorManagementSessionBean.addNewOperator(operatorId, operatorName, operatorLastName, gender, birthday, operatorType, operatorStatus, companyId, emailaddress, contactNumber, hashedPass, roleId);
                System.out.println("NEW TRANSPORTATION ASSET ID =================: " + newOperatorId);
                if (newOperatorId == -1) {
                    clearAllFields();
                    System.out.println("============== FAILED TO ADD TRANSPORTATION ASSET DUE TO LOCATION ID ===============");

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to Add Transportation Operator. Please check Company Id! ", ""));
                } else {
                    clearAllFields();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Transportation Operator Added!", ""));
                }

                System.out.println("[WAR FILE]===========================Create New Transportation Asset");
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email Exists!", ""));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAllFields() {

        operatorName = null;
        operatorLastName = null;
        gender = null;
        birthday = null;
        operatorType = null;
        operatorStatus = null;
        contactNumber = null;
        password = null;
        emailaddress = null;
        roleId = null;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getOperatorLastName() {
        return operatorLastName;
    }

    public void setOperatorLastName(String operatorLastName) {
        this.operatorLastName = operatorLastName;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

}
