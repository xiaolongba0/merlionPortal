/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import entity.SystemUser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.ChangePasswordSessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.utility.MD5Generator;
import org.primefaces.context.RequestContext;

/**
 *
 * @author manliqi
 */
@Named(value = "changePasswordBean")
@ViewScoped
public class ChangePasswordManagedBean {

    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private ChangePasswordSessionBean changePasswordBean;

    private SystemUser loginedUser;
    private String existingPassword;
    private String password;
    private String confirmPassword;

    public ChangePasswordManagedBean() {
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

    }

    public void changePassword() {
        if (existingPassword == null) {
            existingPassword = "";
        }
        if (confirmPassword == null) {
            confirmPassword = "";
        }
        if (password == null) {
            password = "";
        }
        RequestContext requestContext = RequestContext.getCurrentInstance();
        boolean result = false;
        if (changePasswordBean.checkExistingPassword(loginedUser.getSystemUserId(), MD5Generator.hash(existingPassword))) {
            if (password.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please enter a new password!", ""));
            } else {
                if (password.equals(confirmPassword)) {
                    result = changePasswordBean.changeToNewPassword(MD5Generator.hash(password), loginedUser.getSystemUserId());

                    if (result) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PasswordChanged", ""));
                        try {
                            if (uamb.isSystemAdminUser(loginedUser.getSystemUserId())) {
                                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/admin/dashboard.xhtml");
                            } else {
                                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/user/dashboard.xhtml");

                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Something is very wrong!", "Change password unsuccesful"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Password Mis-match!", "Please try again."));
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Existing Password is Wrong!", "Please try again."));
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getExistingPassword() {
        return existingPassword;
    }

    public void setExistingPassword(String existingPassword) {
        this.existingPassword = existingPassword;
    }
//</editor-fold>
}
