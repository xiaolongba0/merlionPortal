/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "loginBean")
@ViewScoped
public class LoginBean {

    private String username;
    private String password;

    public LoginBean() {
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
