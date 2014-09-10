/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import merlionportal.utility.MD5Generator;
import entity.SystemUser;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import merlionportal.ci.administrationmodule.LoginSessionBean;

@Named(value = "loginBean")
@ViewScoped
public class LoginBean {

    private String username;
    private String password;
    @EJB
    LoginSessionBean loginSessionBean;

    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    HttpServletRequest request = (HttpServletRequest) context.getRequest();
    HttpServletResponse response = (HttpServletResponse) context.getResponse();

    private void Login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int tries = 0;
        if (request.getSession().getAttribute("logintries") != null) {
            tries = (int) request.getSession().getAttribute("logintries");
            if (tries > 5) {
                //Reset tries after 30 minutes
                Long startTry = (long) request.getSession().getAttribute("firstLoginTry");
                startTry += 1800000; //30 minutes
                Long nowDate = new Date().getTime();
                if (nowDate > startTry) {
                    tries = 0;
                }
            }
        }

        if (username != null) {
            if (password != null) {
                if (tries <= 5) {
                    SystemUser user = loginSessionBean.verifyAccount(username, MD5Generator.hash(password));
                    if (user == null) {
                        //Login failed
                        if (tries == 0) {
                            request.getSession().setAttribute("firstLoginTry", new Date().getTime());
                        }
                        tries += 1;
                        //Set number of tries
                        request.getSession().setAttribute("logintries", tries);
                    } else {
                        //Login Successful
                        tries = 0;
                        request.getSession().setAttribute("logintries", tries);
                        request.getSession().setAttribute("loginedUser", user); //session to store logined user object
                    }
                } else {
                    //Sorry you have tried more than 5 times...
                }
            }
        }
    }

    public LoginBean() {
    }

    public void loginInformation() {
        loginSessionBean.verifyAccount(username, password);

    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
//</editor-fold>
}
