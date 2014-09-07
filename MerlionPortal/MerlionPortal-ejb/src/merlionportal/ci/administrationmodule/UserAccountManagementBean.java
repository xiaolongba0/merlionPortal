/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.administrationmodule;

import entity.Company;
import entity.SystemUser;
import entity.UserRole;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class UserAccountManagementBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //Used by platform superuser
    @PersistenceContext
    EntityManager em;
    
    @EJB
    private CheckAccessRightBean checkAccessRightBean;
    
    private SystemUser operator; 
    
    //if operator is null return 0, if access denied return -1, sucess return 1
    public int createSystemAdminRole(Integer operatorId, Integer companyId) {
        Company company = em.find(Company.class, companyId);
        operator = getOperator(operatorId);
        if (operator != null) {
            if (checkAccessRightBean.userHasRight(operator, Right.canManageUser)) {
            UserRole superRole = new UserRole();
            superRole.setRoleName("systemadmin");
            
            superRole.setCanManageUser(true);
            
            superRole.getCompanyList().add(company);
            superRole.setDescription("systemadmin for company"+company.getName());
            
            em.persist(superRole);
            em.flush();
            em.merge(company);
            return 1;
            }
            return -1;
        }
        return 0;

    }
    private SystemUser getOperator(Integer operatorId){
        return em.find(SystemUser.class, operatorId);
    }

    private Company getCompany(Integer companyId){
        return em.find(Company.class, companyId);
    }

    public int assignSystemAdminRoleToUser() {
        return 0;

    }

    //Used by company system Admin
    public int createCompanySystemAdminUser() {
        return 0;

    }

    public int createCompanyRole() {
        return 0;

    }

    public int createCompanySystemUser() {
        return 0;

    }

    public int assignRoleToCompanySystemUser() {
        return 0;

    }

    public int unlockUser(int creatorId, int systemAdminId) {
        return 0;

    }

    public int activateUser(int creatorId, int systemAdminId) {
        return 0;

    }
    public int terminateUser(int creatorId, int systemAdminId) {
        return 0;

    }
    public int changePasswordUponLogin(int creatorId, int systemAdminId) {
        return 0;

    }

}
