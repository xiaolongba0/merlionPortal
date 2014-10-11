/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import entity.Company;
import entity.SystemUser;
import entity.UserRole;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.GetCompanyRoleSessionBean;
import merlionportal.ci.administrationmodule.GetCompanySessionBean;
import merlionportal.ci.administrationmodule.RoleManagementSessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.utility.MD5Generator;
import merlionportal.utility.Postman;
import org.primefaces.context.RequestContext;

/**
 *
 * @author manliqi
 */
@Named(value = "createUserManagerBean")
@ViewScoped
public class CreateUserManagedBean {

    private SystemUser loginedUser;

    private Integer selectCompanyId;
    private String[] selectedRoles;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String postalAddress;
    private String contactNumber;
    private String salution;

    private Integer credit;

    private List<Company> companys;
    private List<UserRole> roles;

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    RoleManagementSessionBean rmb;
    @EJB
    GetCompanySessionBean gcsb;
    @EJB
    GetCompanyRoleSessionBean gcrsb;

    /**
     * Creates a new instance of createUserManagerBean
     */
    public CreateUserManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
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
        companys = (List<Company>) gcsb.getCompanies();

        roles = gcrsb.getAllRolesInCompany((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId"));

    }

    public void createSystemUser(ActionEvent event) {

        Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        Integer companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

        if (operatorId != null) {
            if (rmb.isSuperUser(operatorId)) {
                companyId = selectCompanyId;
            }
            ArrayList<String> roleList = new ArrayList<String>(Arrays.asList(selectedRoles));
            ArrayList<Integer> roleListInt = new ArrayList<>();
            for (Object o : roleList) {
                int i = Integer.parseInt((String) o);
                roleListInt.add(i);
            }
            //test if email alr exist in database
            if (uamb.getUserByEmail(emailAddress) == null) {
                int result = uamb.createSystemUser(operatorId, companyId, roleListInt, firstName, lastName, emailAddress, MD5Generator.hash(password), postalAddress,
                        contactNumber, salution, credit);
                if (result == -1) {
                    System.out.println("Access Denied");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action!"));
                } else if (result == 1) {
                    RequestContext requestContext = RequestContext.getCurrentInstance();
                    String sender = "merlionportal@nus.edu.sg";
                    String[] recipients = {emailAddress};
                    String subject = "System User Account";

                    String content = "<h2>Hi user" + lastName + " " + firstName + ",</h2><p>A new account has been created for you. <br/> Please kindly use your email " + emailAddress + " and the password below to login to your account.<br/><br/>"
                            + "Password: <strong>" + password + "</strong><br/><br/>"
                            + " Thank you. <br/><br/>Best Regards,<br/>Administrator, Merlion Portal";
                    this.clearAllFields();
                    if (Postman.sendMail(sender, recipients, subject, content)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New System User Added!", "Email notification is sent to user."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "New System User Added!", "Failed to send Email notification to user."));
                    }
                } else {
                    // direct to login page
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email Exist!", "This Email exist in database, please change another email"));

            }

        }

    }

    public void onCompanyChange() {
        if (selectCompanyId != null) {
            roles = gcrsb.getAllRolesInCompany(selectCompanyId);
        }
    }

    private void clearAllFields() {
        selectedRoles = null;
        firstName = null;
        lastName = null;
        emailAddress = null;
        password = null;
        postalAddress = null;
        contactNumber = null;
        salution = null;
        credit = null;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSalution() {
        return salution;
    }

    public void setSalution(String salution) {
        this.salution = salution;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getSelectCompanyId() {
        return selectCompanyId;
    }

    public void setSelectCompanyId(Integer selectCompanyId) {
        this.selectCompanyId = selectCompanyId;
    }

    public List<Company> getCompanys() {
        return companys;
    }

    public void setCompanys(List<Company> companys) {
        this.companys = companys;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

}
