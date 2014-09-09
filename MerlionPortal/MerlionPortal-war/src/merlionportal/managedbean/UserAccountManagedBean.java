/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean;

import entity.SystemUser;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import merlionportal.ci.administrationmodule.CheckAccessRightBean;
import merlionportal.ci.administrationmodule.UserAccountManagementBean;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Named(value = "userAccountManagedBean")
@ViewScoped
public class UserAccountManagedBean {

    /**
     * Creates a new instance of UserAccountManagedBean
     */
    @EJB
    private UserAccountManagementBean uamb;

    @EJB
    private CheckAccessRightBean carb;

    private FacesContext ctx;

    @PersistenceContext
    EntityManager em;

    private Integer userId;
    private Integer companyId;
    private String companyName;
    private String companyAddress;
    private String contactNumber;
    private String contactPersonName;
    private String emailAddress;
    private String description;
    private Integer package1;

    private String firstNameNewUser;
    private String lastNameNewUser;
    private String emailAddressNewUser;
    private String passwordNewUser;
    private String postalAddressNewUser;
    private String contactNumberNewUser;
    private String salutionNewUser;
    private String userTypeNewUser;
    private Integer companyIdNewUser;

    public UserAccountManagedBean() {
    }

    public void createSystemAdmin(ActionEvent event) {

        Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        SystemUser operator = em.find(SystemUser.class, operatorId);
        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {
                int result = uamb.createCompanySystemAdminUser(firstNameNewUser, lastNameNewUser, emailAddressNewUser, passwordNewUser, postalAddressNewUser, contactNumberNewUser, salutionNewUser, userTypeNewUser, companyIdNewUser);
            } else {
                System.out.println("Access Denied");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action!"));
            }

        }

    }

    public void createSystemAdminRole(ActionEvent event) {
        Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        SystemUser operator = em.find(SystemUser.class, operatorId);

        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {
                int result = uamb.createSystemAdminRole(companyId);
            } else {
                System.out.println("Access Denied");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Access Denied."));
            }

        }
    }

    public void registerNewCompany(ActionEvent event) {
        int result = uamb.registerNewCompany(companyName, emailAddress, contactNumber, contactPersonName, emailAddress, description, package1);
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPackage1() {
        return package1;
    }

    public void setPackage1(Integer package1) {
        this.package1 = package1;
    }

    public String getFirstNameNewUser() {
        return firstNameNewUser;
    }

    public void setFirstNameNewUser(String firstNameNewUser) {
        this.firstNameNewUser = firstNameNewUser;
    }

    public String getLastNameNewUser() {
        return lastNameNewUser;
    }

    public void setLastNameNewUser(String lastNameNewUser) {
        this.lastNameNewUser = lastNameNewUser;
    }

    public String getEmailAddressNewUser() {
        return emailAddressNewUser;
    }

    public void setEmailAddressNewUser(String emailAddressNewUser) {
        this.emailAddressNewUser = emailAddressNewUser;
    }

    public String getPasswordNewUser() {
        return passwordNewUser;
    }

    public void setPasswordNewUser(String passwordNewUser) {
        this.passwordNewUser = passwordNewUser;
    }

    public String getPostalAddressNewUser() {
        return postalAddressNewUser;
    }

    public void setPostalAddressNewUser(String postalAddressNewUser) {
        this.postalAddressNewUser = postalAddressNewUser;
    }

    public String getContactNumberNewUser() {
        return contactNumberNewUser;
    }

    public void setContactNumberNewUser(String contactNumberNewUser) {
        this.contactNumberNewUser = contactNumberNewUser;
    }

    public String getSalutionNewUser() {
        return salutionNewUser;
    }

    public void setSalutionNewUser(String salutionNewUser) {
        this.salutionNewUser = salutionNewUser;
    }

    public String getUserTypeNewUser() {
        return userTypeNewUser;
    }

    public void setUserTypeNewUser(String userTypeNewUser) {
        this.userTypeNewUser = userTypeNewUser;
    }

    public Integer getCompanyIdNewUser() {
        return companyIdNewUser;
    }

    public void setCompanyIdNewUser(Integer companyIdNewUser) {
        this.companyIdNewUser = companyIdNewUser;
    }
}
