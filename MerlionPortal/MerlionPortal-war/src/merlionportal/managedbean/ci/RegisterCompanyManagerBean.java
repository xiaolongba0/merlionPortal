/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "userAccountManagerBean")
@ViewScoped
public class RegisterCompanyManagerBean {

    /**
     * Creates a new instance of RegisterCompanyManagerBean
     */
    @EJB
    private UserAccountManagementSessionBean uamb;

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

   

    

    public RegisterCompanyManagerBean() {

        
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

//</editor-fold>
}
