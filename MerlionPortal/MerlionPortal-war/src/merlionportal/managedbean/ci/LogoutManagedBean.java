/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "logoutManagerBean")
@ViewScoped
public class LogoutManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    UserAccountManagementSessionBean uamsb;

    /**
     * Creates a new instance of LogoutManagerBean
     */
    public LogoutManagedBean() {
    }

    public void logout() throws IOException {
        systemLogSB.recordSystemLog(uamsb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")).getSystemUserId(), "CI User logged out");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/logout.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
