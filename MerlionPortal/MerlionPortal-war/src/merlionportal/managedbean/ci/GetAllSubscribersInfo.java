/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.managedbean.ci;

import entity.Company;
import entity.SystemUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.GetCompanySessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "getAllSubscribersInfo")
@ViewScoped
public class GetAllSubscribersInfo {

    /**
     * Creates a new instance of GetAllSubscribersInfo
     */
    private SystemUser loginedUser;
    private List<Company> companys;
    private List<Company> filteredCompanys;
    private List<String> packages;
    @EJB 
    UserAccountManagementSessionBean uamb;
    @EJB
    GetCompanySessionBean gcsb;
    
    public GetAllSubscribersInfo() {
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
        companys = (List<Company>) gcsb.getCompanies();
        packages = new ArrayList<>();
        packages.add("1");
        packages.add("2");
        packages.add("3");
        packages.add("4");

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

    public List<Company> getFilteredCompanys() {
        return filteredCompanys;
    }

    public void setFilteredCompanys(List<Company> filteredCompanys) {
        this.filteredCompanys = filteredCompanys;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }
    
    
}
