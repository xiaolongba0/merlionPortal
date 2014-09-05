/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.commonInfra.administration;

import entity.Company;
import entity.SystemUser;
import entity.UserRole;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    EntityManager entityManager;
    
    @Override
    public boolean createRole(String roleName, String description, ArrayList rights) {
        boolean result = false;
        
        UserRole userRole = new UserRole();
        userRole.setRoleName(roleName);
        userRole.setDescription(description);
        
        for(Object object: rights){
            
        }
        
        
        result = true;
        return result;
    }

    @Override
    public boolean createSystemAdminUser(String firstName, String lastName, String emailAddress, String password, String postalAddress, String contactNumber, String salution, String userType, int userRoleId, int companyId) {
        boolean result= false;
        
        SystemUser systemUser = new SystemUser();
        systemUser.setFirstName(firstName);
        systemUser.setLastName(lastName);
        systemUser.setEmailAddress(emailAddress);
        systemUser.setPassword(password);
        systemUser.setPostalAddress(postalAddress);
        systemUser.setContactNumber(contactNumber);
        systemUser.setSalution(salution);
        systemUser.setUserType(userType);
        
        systemUser.setLocked(false);
        systemUser.setActivated(true);
        systemUser.setTerminated(false);
        systemUser.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
        systemUser.setResetPasswordUponLogin(true);
        
        
        
        Query q= entityManager.createNamedQuery("UserRole.findByUserRoleId").setParameter("userRoleId", userRoleId);
        UserRole userRole = (UserRole)q.getSingleResult();
        systemUser.setUserRoleuserRoleId(userRole);
        
        q = entityManager.createNamedQuery("Company.findByCompanyId").setParameter("companyId", companyId);
        Company company = (Company)q.getSingleResult();
        
        systemUser.setCompanycompanyId(company);
        entityManager.persist(systemUser);
        entityManager.flush();
        
        entityManager.merge(userRole);
        entityManager.merge(company);

        result = true;
        return result;
    }

    @Override
    public boolean unlockSystemAdminUser(int systemAdminId) {
        
        boolean result= false;
        Query q = entityManager.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
        SystemUser systemUser = (SystemUser) q.getSingleResult();
        systemUser.setLocked(false);
        
        entityManager.merge(systemUser);
        result = true;
        return result;
    }

    @Override
    public boolean activateSystemAdminUser(int systemAdminId) {
        boolean result= false;
        Query q = entityManager.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
        SystemUser systemUser = (SystemUser) q.getSingleResult();
        systemUser.setActivated(false);
        
        entityManager.merge(systemUser);
        result = true;
        return result;
    }

    @Override
    public boolean terminateSystemAdminUser(int systemAdminId) {
        boolean result= false;
        Query q = entityManager.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
        SystemUser systemUser = (SystemUser) q.getSingleResult();
        systemUser.setTerminated(true);
        
        entityManager.merge(systemUser);
        result = true;
        return result;
    }

    @Override
    public boolean changePasswordUponLogin(int systemAdminId) {
        boolean result= false;
        Query q = entityManager.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", systemAdminId);
        SystemUser systemUser = (SystemUser) q.getSingleResult();
        systemUser.setResetPasswordUponLogin(true);
        
        entityManager.merge(systemUser);
        result = true;
        return result;
    }

    @Override
    public ArrayList getAllSystemAdminUser() {
        
        ArrayList<SystemUser> systemUserList = new ArrayList();
        SystemUser systemUser;
        Query q = entityManager.createNamedQuery("SystemUser.findByUserType").setParameter("userType", "ClientSystemAdmin");
        for (Object o :q.getResultList()){
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
        Query q = entityManager.createQuery("SELECT s FROM SystemUser s WHERE s.userType = :userType AND s.companycompanyId = :companyId");
        q.setParameter("userType", "ClientSystemAdmin");
        q.setParameter("companyId", companyId);
        
        for (Object o :q.getResultList()){
            systemUser = (SystemUser) o;
            systemUserList.add(systemUser);
            
        }
        return systemUserList;
      
    }
    
    
            
}
