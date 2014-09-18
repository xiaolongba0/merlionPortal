/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.utility;

import entity.SystemUser;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.CheckCompanyPackageSessionBean;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;

/**
 *
 * @author manliqi
 */
@Named(value = "filterBean")
@SessionScoped
public class FilterBean implements Serializable {

    /**
     * Creates a new instance of FilterBean
     */
    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    CheckCompanyPackageSessionBean ccpsb;

    private SystemUser user;
    private int companyPac;

    public FilterBean() {
    }

    public SystemUser retrieveSystemUser(Integer userId) {
        user = uamb.getUser(userId);
        return user;
    }

    public boolean isSystemAdmin(Integer userId) {
        return uamb.isSystemAdminUser(userId);
    }

    public int retrieveCompanyPac(Integer companyId) {
        companyPac = ccpsb.checkCompanyPackage(companyId);
        return companyPac;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public int getCompanyPac() {
        return companyPac;
    }

    public void setCompanyPac(int companyPac) {
        this.companyPac = companyPac;
    }
    
}
