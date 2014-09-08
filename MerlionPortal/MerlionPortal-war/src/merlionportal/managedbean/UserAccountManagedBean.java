/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.managedbean;


import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementBean;

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
    
    private Integer userId;
    private Integer companyId;
    private String companyName;
    private String companyAddress;
    private String contactNumber;
    private String contactPersonName;
    private String emailAddress;
    private String description;
    
    public UserAccountManagedBean() {
    }
    
    public void createSystemAdmin(ActionEvent event){
        //boolean result = false;
        //int systemUserId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("systemUserId");
        //superUserBean.createSystemAdminUser(firstName, lastName, emailAddress, postalAddress, contactNumber, salution, userType, companyId, 1);
    
        //return result;
    }
    public void createSystemAdminRole(ActionEvent event){
        int result = uamb.createSystemAdminRole(userId, companyId);
        
    }
    public void registerNewCompany(ActionEvent event){
        int result = uamb.registerNewCompany(companyName, emailAddress, contactNumber, contactPersonName, emailAddress, description);
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
  
    
    
}
