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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.GetCompanyRoleSessionBean;
import merlionportal.ci.administrationmodule.GetCompanySessionBean;
import merlionportal.ci.administrationmodule.RoleManagementSessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "viewCompanyRoles")
@ViewScoped
public class ViewCompanyRolesManagerBean {

    @EJB
    GetCompanyRoleSessionBean gcrsb;
    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    GetCompanySessionBean gcsb;
    @EJB
    RoleManagementSessionBean rmsb;

    private Integer companyId;
    private SystemUser loginedUser;
    private List<Company> companys;
    private Integer selectCompanyId;

    private UserRole selectedRole;
    private List<UserRole> roles;

    /**
     * Creates a new instance of ViewCompanyRoles
     */
    public ViewCompanyRolesManagerBean() {
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
        if (loginedUser.getUserType() != null) {
            companys = (List<Company>) gcsb.getCompanies();
        } else {
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            if (companyId != null) {

                roles = gcrsb.getAllRolesInCompany(companyId);
                if (roles == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No Record!", "There is not any role created yet"));

                }
            }
        }
    }

    public void onCompanyChange() {
        if (selectCompanyId != null) {
            roles = gcrsb.getAllRolesInCompany(selectCompanyId);
        }

    }

    public void deleteRole() {
        int result = 0;
        if (selectedRole != null) {
            result = rmsb.deleteCompanyRole(loginedUser.getSystemUserId(), selectedRole.getUserRoleId());
        }
        if (result == 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted!", "This Role is deleted."));

        } else if (result == -2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unsuccessful!", "Cannot deleted this role as there are users assigned to this role."));

        } else if (result == -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action."));

        } else {
//            direct to login page
        }
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public List<Company> getCompanys() {
        return companys;
    }

    public void setCompanys(List<Company> companys) {
        this.companys = companys;
    }

    public Integer getSelectCompanyId() {
        return selectCompanyId;
    }

    public void setSelectCompanyId(Integer selectCompanyId) {
        this.selectCompanyId = selectCompanyId;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public UserRole getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(UserRole selectedRole) {
        this.selectedRole = selectedRole;
    }

}
