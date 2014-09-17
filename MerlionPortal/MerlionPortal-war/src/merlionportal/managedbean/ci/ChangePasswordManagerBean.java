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

/**
 *
 * @author manliqi
 */
@Named(value = "changePasswordManagerBean")
@ViewScoped
public class ChangePasswordManagerBean {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    ChangePasswordSessionBean changePasswordBean;

    private SystemUser loginedUser;
    private String password;

    /**
     * Creates a new instance of ChangePasswordManagerBean
     */
    public ChangePasswordManagerBean() {
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
        boolean result = false;
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
                Logger.getLogger(ChangePasswordManagerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Something is very wrong!", "Change password unsuccesful"));

        }
    }
}
