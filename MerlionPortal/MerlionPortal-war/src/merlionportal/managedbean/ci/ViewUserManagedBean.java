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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.GetCompanyRoleSessionBean;
import merlionportal.ci.administrationmodule.GetCompanySessionBean;
import merlionportal.ci.administrationmodule.GetCompanyUserSessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.utility.MD5Generator;

/**
 *
 * @author manliqi
 */
@Named(value = "viewUserManagerBean")
@ViewScoped
public class ViewUserManagedBean {

    /**
     * Creates a new instance of ViewUserManagerBean
     */
    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    GetCompanySessionBean gcsb;
    @EJB
    GetCompanyUserSessionBean gcusb;
    @EJB
    GetCompanyRoleSessionBean gcrsb;

    private SystemUser loginedUser;
    private List<Company> companys;
    private Integer selectCompanyId;

    private List<SystemUser> companyUsers;
    private List<SystemUser> filteredUsers;
    private SystemUser selectedUser;
    private Integer companyId;

    private List<UserRole> rolesToDisplay;
    private Integer roleToAdd;

    public ViewUserManagedBean() {
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

                companyUsers = gcusb.getAllUsersInCompany(companyId);
                if (companyUsers == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No Record!", "There is no user created yet"));

                }
            }
        }
    }

    public void onCompanyChange() {
        if (selectCompanyId != null) {
            companyUsers = gcusb.getAllUsersInCompany(selectCompanyId);
        }

    }

    public void deleteUser() {
        int result = 0;
        if (selectedUser != null) {
            result = uamb.deleteSystemUser(loginedUser.getSystemUserId(), selectedUser.getSystemUserId());
        }
        if (result == 1) {
            companyUsers.remove(selectedUser);
            selectedUser = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted!", "This user is deleted."));

        } else if (result == -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action."));

        } else {
//            direct to login page
        }
    }

    public void updateUser() {

        int result = 0;
        if (selectedUser != null) {
            result = uamb.updateUserInfo(loginedUser.getSystemUserId(), selectedUser);
        }
        if (result == 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated!", "This user is updated."));

        } else if (result == -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action."));

        } else {
//            direct to login page
        }
    }

    public void detachRole(UserRole role) {
        int result = 0;
        if (selectedUser != null) {
            result = uamb.detachRoleFromUser(loginedUser.getSystemUserId(), selectedUser.getSystemUserId(), role.getUserRoleId());
        }
        if (result == 1) {
            selectedUser.getUserRoleList().remove(role);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Role Detached!", "This role is detached from this user."));

        } else if (result == -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action."));

        } else {
//            direct to login page
        }

    }

    public void addRole() {
        int result = 0;
        if (selectedUser != null) {
            result = uamb.addRoleToUser(loginedUser.getSystemUserId(), selectedUser.getSystemUserId(), roleToAdd);
        }
        if (result == 1) {

            selectedUser.getUserRoleList().add(gcrsb.getOneRole(roleToAdd));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Role Added!", "This role is added to this user."));

        } else if (result == -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Access Denied!", "You do not have sufficient right to perform this action."));

        } else {
//            direct to login page
        }
    }

    public void refreshDisplayedRoles() {
        System.err.println("here");
        if (selectCompanyId != null) {
            rolesToDisplay = (List<UserRole>) gcrsb.getAllRolesInCompany(selectCompanyId);
            System.out.println("rolesToDisplay" + rolesToDisplay.get(0).toString());
            for (Object o : selectedUser.getUserRoleList()) {
                UserRole hasRole = (UserRole) o;
                System.out.println("Role!!!!!!!!!!!!!!");

                if (rolesToDisplay.contains(hasRole)) {
                    System.out.println("has role");
                    rolesToDisplay.remove(hasRole);
                }
            }
        } else {
            rolesToDisplay = gcrsb.getAllRolesInCompany(companyId);
            for (Object o : selectedUser.getUserRoleList()) {
                UserRole hasRole = (UserRole) o;
                if (rolesToDisplay.contains(hasRole)) {
                    System.out.println("has role");
                    rolesToDisplay.remove(hasRole);
                }
            }
        }
    }

    public void selectUser(SystemUser user) {
        System.out.println("Hsjdhfkajsdhfklashdfkjas");
        selectedUser = user;
        System.out.println("User" +user.getLastName());
        System.out.println("Selected User" + selectedUser.getLastName());

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

    public List<SystemUser> getCompanyUsers() {
        return companyUsers;
    }

    public void setCompanyUsers(List<SystemUser> companyUsers) {
        this.companyUsers = companyUsers;
    }

    public SystemUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(SystemUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<SystemUser> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<SystemUser> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public List<UserRole> getRolesToDisplay() {
        return rolesToDisplay;
    }

    public void setRolesToDisplay(List<UserRole> rolesToDisplay) {
        this.rolesToDisplay = rolesToDisplay;
    }

    public Integer getRoleToAdd() {
        return roleToAdd;
    }

    public void setRoleToAdd(Integer roleToAdd) {
        this.roleToAdd = roleToAdd;
    }

}
