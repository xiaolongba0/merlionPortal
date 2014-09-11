/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementBean;
import merlionportal.utility.MD5Generator;

/**
 *
 * @author manliqi
 */
@Named(value = "userAccountManagedBean")
@ViewScoped
public class UserAccountManagerBean {

    /**
     * Creates a new instance of UserAccountManagerBean
     */
    @EJB
    private UserAccountManagementBean uamb;

    private FacesContext ctx;

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

    

    public UserAccountManagerBean() {

        
    }

    public void createSystemAdmin(ActionEvent event) {

//        Integer operatorId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        Integer operatorId = 2;
        if (operatorId != null) {
            String hashedPass = MD5Generator.hash(passwordNewUser);
            int result = uamb.createCompanySystemAdminUser(operatorId, firstNameNewUser, lastNameNewUser, emailAddressNewUser, hashedPass, postalAddressNewUser, contactNumberNewUser, salutionNewUser, userTypeNewUser, companyIdNewUser);
            if (result == -1) {
                System.out.println("Access Denied");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action!"));
            } else if (result == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New System Admin Added!", ""));

            } else {
                // direct to login page
            }

        }

    }

    
    public void registerNewCompany(ActionEvent event) {

        int result = uamb.registerNewCompany(companyName, emailAddress, contactNumber, contactPersonName, emailAddress, description, package1);
        if (result > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Company Registered!", "Please wait for our system administrator to contact you."));
        }
    }

  
//    <editor-fold defaultstate="collapsed" desc="getters and setters">
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

    
//</editor-fold>
}
