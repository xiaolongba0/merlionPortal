/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.administrationmodule;

import entity.Company;
import entity.SystemUser;
import entity.UserRole;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
    private CheckAccessRightBean carb;

    public SystemUser getUser(int userid) {
        SystemUser user = null;
        Query q = em.createNamedQuery("SystemUser.findBySystemUserId").setParameter("systemUserId", userid);
        if (!q.getResultList().isEmpty()) {
            user = (SystemUser) q.getResultList().get(0);
        }
        return user;
    }

    public boolean checkSuperUser() {
        Query q = em.createNamedQuery("SystemUser.findByUserType").setParameter("userType", "superuser");
        List<SystemUser> superUserList = q.getResultList();
        return superUserList.isEmpty();
    }

    public void createSuperUser(String password, int companyId) {
        System.out.println("CREATE SUPER USER!!!");
        try {
            SystemUser superUser = new SystemUser();
            superUser.setFirstName("Administrator");
            superUser.setLastName("MerlionPortal");
            superUser.setEmailAddress("a0084692@nus.edu.sg");
            superUser.setPostalAddress("1 Computing Drive NUS");
            superUser.setPassword(password);
            superUser.setContactNumber("+65 9888 8888");
            superUser.setSalution("Mr.");
            superUser.setLocked(false);
            superUser.setResetPasswordUponLogin(false);
            superUser.setCreatedDate(new Date());
            superUser.setUserType("superuser");
            superUser.setActivated(true);
            superUser.setCredit("");
            Company company = em.find(Company.class, companyId);
            if (company != null) {
                superUser.setCompanycompanyId(company);
                em.persist(superUser);
            } else {
                //Something is very wrong
            }

            SystemUser testUser = new SystemUser();
            testUser.setFirstName("TEST");
            testUser.setLastName("TES555T");
            testUser.setEmailAddress("aaaaaaa@nus.edu.sg");
            testUser.setPostalAddress("1 Computing Drive NUS");
            testUser.setPassword(password);
            testUser.setContactNumber("+65 9888 8888");
            testUser.setSalution("Mr.");
            testUser.setLocked(false);
            testUser.setResetPasswordUponLogin(false);
            testUser.setCreatedDate(new Date());
            testUser.setUserType("a");
            testUser.setActivated(true);
            testUser.setCredit("");
            if (company != null) {
                testUser.setCompanycompanyId(company);
                em.persist(testUser);
                em.flush();
            } else {
                //Something is very wrong
            }
        } catch (ConstraintViolationException cve) {
            for (ConstraintViolation cv : cve.getConstraintViolations()) {
                System.out.println(cv.getRootBean().toString() + ":" + cv.getMessage());
            }
        }
    }

    // Used by anyone
    public int registerNewCompany(String name, String address, String contactNumber, String contactPersonName, String emailAddress, String description, Integer package1) {

        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        company.setContactNumber(contactNumber);
        company.setContactPersonName(contactPersonName);
        company.setEmailAddress(emailAddress);
        company.setDescription(description);
        company.setPackage1(package1);

        em.persist(company);
        em.flush();
        em.refresh(company);
        if (company.getCompanyId() == null) {
            return -1;
        } else {
            return company.getCompanyId();
        }
    }

    //Used by super user
    public int createCompanySystemAdminUser(Integer operatorId, String firstName, String lastName, String emailAddress, String password, String postalAddress,
            String contactNumber, String salution, String userType, Integer companyId) {

        SystemUser operator = em.find(SystemUser.class, operatorId);
        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {

                SystemUser user = new SystemUser();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmailAddress(emailAddress);
                user.setPassword(password);
                user.setPostalAddress(postalAddress);
                user.setContactNumber(contactNumber);
                user.setSalution(salution);
                user.setUserType(userType);

                Company company = getCompany(companyId);
                if (company != null) {
                    user.setCompanycompanyId(company);
                } else {
                    return 0;
                }

                List<UserRole> roles = new ArrayList<>();
                UserRole role = em.find(UserRole.class, 2);
                roles.add(role);
                user.setUserRoleList(roles);

                em.persist(user);
                em.flush();
                em.merge(company);
                em.merge(role);

                return 1;
            } else {
                return -1;
            }
        } else {

            return 0;
        }
    }

    public int createCompanySystemUser(Integer operatorId, ArrayList<Integer> userRoleIds, String firstName, String lastName, String emailAddress, String password, String postalAddress,
            String contactNumber, String salution, String userType, Integer companyId) {
        SystemUser operator = em.find(SystemUser.class, operatorId);
        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {

                SystemUser user = new SystemUser();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmailAddress(emailAddress);
                user.setPassword(password);
                user.setPostalAddress(postalAddress);
                user.setContactNumber(contactNumber);
                user.setSalution(salution);
                user.setUserType(userType);

                Company company = getCompany(companyId);
                if (company != null) {
                    user.setCompanycompanyId(company);
                } else {
                    return 0;
                }

                List<UserRole> roles = new ArrayList<>();

                Query q = em.createNamedQuery("UserRole.findByUserRoleId");
                //Set Parameter part Missing here
                UserRole role = (UserRole) q.getSingleResult();
                roles.add(role);
                user.setUserRoleList(roles);

                em.persist(user);
                em.flush();
                em.merge(company);
                em.merge(role);

                return 1;

            } else {
                return -1;
            }
        } else {

            return 0;
        }

    }

    private SystemUser getOperator(Integer operatorId) {
        return em.find(SystemUser.class, operatorId);
    }

    private Company getCompany(Integer companyId) {
        return em.find(Company.class, companyId);
    }

    public int unlockUser(int systemAdminId, Integer userId) {
        SystemUser operator = em.find(SystemUser.class, systemAdminId);
        if (operator != null) {
            if (carb.userHasRight(operator, Right.canManageUser)) {
                SystemUser user = em.find(SystemUser.class, userId);
                if (user != null) {
                    user.setLocked(false);

                    em.merge(user);
                    em.flush();
                    return 1;
                }
                return 0;
            } else {
                return -1;
            }
        } else {
            return 0;
        }

    }

    public int changePasswordUponLogin(Integer systemAdminId, Integer userId) {
        return 0;

    }

}
