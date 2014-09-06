/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.administrationmodule;

import entity.Company;
import entity.SystemUser;
import entity.UserRole;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author manliqi
 */
@Stateless
public class SuperUserBean implements SuperUserBeanLocal {

    @PersistenceContext
    EntityManager em;

    @EJB
    CheckAccessRightBean checkAccessRightBean;

    @Override
    public boolean createSystemAdminRole(int creatorId, int companyId, String roleName, String description) {
        boolean result = false;
        if (em.find(SystemUser.class, creatorId).getUserType().equals("superuser")) {

            UserRole userRole = new UserRole();
            userRole.setRoleName(roleName);
            userRole.setDescription(description);
            userRole.setCanManageUser(true);
            
            Company company = em.find(Company.class, companyId);
            userRole.getCompanyList().add(company);

            em.persist(userRole);
            em.flush();
            em.merge(company);
            result = true;
            return result;
        }
        return result;
    }

    @Override
    public boolean createSystemAdminUser(String firstName, String lastName, String emailAddress, String postalAddress, String contactNumber, String salution, String userType, int companyId, int creatorId) {
        boolean result = false;

//        SystemUser creator = em.find(SystemUser.class, creatorId);
        if (em.find(SystemUser.class, creatorId).getUserType().equals("superuser")) {
//        if (creator != null) {
//            if (checkAccessRightBean.userHasRight(creator, Right.canManageUser)) {
            SystemUser systemUser = new SystemUser();
            systemUser.setFirstName(firstName);
            systemUser.setLastName(lastName);
            systemUser.setEmailAddress(emailAddress);
            systemUser.setPostalAddress(postalAddress);
            systemUser.setContactNumber(contactNumber);
            systemUser.setSalution(salution);
            systemUser.setUserType(userType);

            systemUser.setLocked(false);
            systemUser.setActivated(false);
            systemUser.setTerminated(false);
            systemUser.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
            systemUser.setResetPasswordUponLogin(true);

            Query q = em.createNamedQuery("Company.findByCompanyId").setParameter("companyId", companyId);
            Company company = (Company) q.getSingleResult();

            systemUser.setCompanycompanyId(company);
            em.persist(systemUser);
            em.flush();

            em.merge(company);

            result = true;
        }

        return result;
    }

    @Override
    public boolean unlockSystemAdminUser(int creatorId, int systemAdminId) {

        boolean result = false;
        if (em.find(SystemUser.class, creatorId).getUserType().equals("superuser")) {
            Query q = em.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
            SystemUser systemUser = (SystemUser) q.getSingleResult();
            systemUser.setLocked(false);

            em.merge(systemUser);
            result = true;
            return result;
        }
        return result;
    }

    @Override
    public boolean activateSystemAdminUser(int creatorId, int systemAdminId) {
        boolean result = false;
        if (em.find(SystemUser.class, creatorId).getUserType().equals("superuser")) {
            Query q = em.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
            SystemUser systemUser = (SystemUser) q.getSingleResult();
            systemUser.setActivated(false);

            em.merge(systemUser);
            result = true;
            return result;
        }
        return result;
    }

    @Override
    public boolean terminateSystemAdminUser(int creatorId, int systemAdminId) {
        boolean result = false;
        if (em.find(SystemUser.class, creatorId).getUserType().equals("superuser")) {

            Query q = em.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
            SystemUser systemUser = (SystemUser) q.getSingleResult();
            systemUser.setTerminated(true);

            em.merge(systemUser);
            result = true;
            return result;
        }
        return result;
    }

    @Override
    public boolean changePasswordUponLogin(int creatorId, int systemAdminId) {
        boolean result = false;
        if (em.find(SystemUser.class, creatorId).getUserType().equals("superuser")) {
            Query q = em.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
            SystemUser systemUser = (SystemUser) q.getSingleResult();
            systemUser.setResetPasswordUponLogin(true);

            em.merge(systemUser);
            result = true;
            return result;
        }
        return result;
    }

    @Override
    public ArrayList getAllSystemAdminUser() {

        ArrayList<SystemUser> systemUserList = new ArrayList();
        SystemUser systemUser;
        Query q = em.createNamedQuery("SystemUser.findByUserType").setParameter("userType", "ClientSystemAdmin");
        for (Object o : q.getResultList()) {
            systemUser = (SystemUser) o;
            systemUserList.add(systemUser);

        }

        return systemUserList;

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public ArrayList<SystemUser> getAllSystemAdminUserFromCompany(int companyId) {
        ArrayList<SystemUser> systemUserList = new ArrayList();
        SystemUser systemUser;
        Query q = em.createQuery("SELECT s FROM SystemUser s WHERE s.userType = :userType AND s.companycompanyId = :companyId");
        q.setParameter("userType", "ClientSystemAdmin");
        q.setParameter("companyId", companyId);

        for (Object o : q.getResultList()) {
            systemUser = (SystemUser) o;
            systemUserList.add(systemUser);

        }
        return systemUserList;

    }

    @Override
    public boolean assignRoleToSystemAdmin(int creatorId, int userId, ArrayList roles) {

        boolean result = false;
        if (em.find(SystemUser.class, creatorId).getUserType().equals("superuser")) {
            if (roles != null) {
                SystemUser user = em.find(SystemUser.class, userId);
                UserRole userRole;
                for (Object o : roles) {
                    int roleId = (int) o;

                    userRole = em.find(UserRole.class, roleId);
                    user.getUserRoleList().add(userRole);
                    em.merge(userRole);

                }
                em.merge(user);
                result = true;
            }
            return result;
        }
        return result;
    }

}
