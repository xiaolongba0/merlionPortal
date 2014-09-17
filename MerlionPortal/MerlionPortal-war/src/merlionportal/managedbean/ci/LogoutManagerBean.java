/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.ci;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author manliqi
 */
@Named(value = "logoutManagerBean")
@ViewScoped
public class LogoutManagerBean {

    /**
     * Creates a new instance of LogoutManagerBean
     */
    public LogoutManagerBean() {
    }

    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/logout.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
