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
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class UserAccountManagementSessionBean {

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
        superUser.setCredit("Not Applicable");
        Company company = em.find(Company.class, companyId);
        if (company != null) {
            superUser.setCompanycompanyId(company);
            em.persist(superUser);
        } else {
            //Something is very wrong
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

        List<UserRole> roles = new ArrayList<>();
        company.setUserRoleList(roles);
        List<SystemUser> users = new ArrayList<>();
        company.setSystemUserList(users);

        em.persist(company);
        em.flush();
        em.refresh(company);
        if (company.getCompanyId() == null) {
            return -1;
        } else {
            return company.getCompanyId();
        }
    }

    //Used by super user and company superuser
    public int createSystemUser(Integer operatorId, Integer companyId, List<Integer> roles, String firstName, String lastName, String emailAddress, String password, String postalAddress,
            String contactNumber, String salution, String credit) {

        SystemUser operator = em.find(SystemUser.class, operatorId);
        boolean canRun = false;
        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }

            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }

            if (canRun) {

                SystemUser user = new SystemUser();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmailAddress(emailAddress);
                user.setPassword(password);
                user.setPostalAddress(postalAddress);
                user.setContactNumber(contactNumber);
                user.setSalution(salution);
                user.setLocked(false);
                user.setResetPasswordUponLogin(true);
                user.setCreatedDate(new Date());
                user.setActivated(true);
                user.setCredit(credit);

                Company company = getCompany(companyId);
                if (company != null) {
                    user.setCompanycompanyId(company);
                    company.getSystemUserList().add(user);
                } else {
                    System.out.println("Company is null");

                    return 0;
                }
                ArrayList<UserRole> userRoles = new ArrayList<>();
                for (Object o : roles) {
                    Integer roleId = (Integer) o;
                    Query q = em.createNamedQuery("UserRole.findByUserRoleId").setParameter("userRoleId", roleId);
                    UserRole userRole = (UserRole) q.getSingleResult();
                    userRoles.add(userRole);
                    em.merge(userRole);
                }

                user.setUserRoleList(userRoles);
                em.merge(company);
                em.persist(user);
                em.flush();

                return 1;
            } else {

                return -1;
            }
        } else {
            System.out.println("Operator is null");
            return 0;
        }
    }

    private SystemUser getOperator(Integer operatorId) {
        return em.find(SystemUser.class, operatorId);
    }

    private Company getCompany(Integer companyId) {
        return em.find(Company.class, companyId);
    }

    public int deleteSystemUser(int systemAdminId, Integer userId) {
        SystemUser operator = em.find(SystemUser.class, systemAdminId);
        boolean canRun = false;
        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }

            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }

            if (canRun) {

                SystemUser user = em.find(SystemUser.class, userId);
                if (user != null) {
                    Company company = user.getCompanycompanyId();
                    if (company.getSystemUserList().contains(user)) {
                        company.getSystemUserList().remove(user);
                        em.merge(company);
                        em.flush();
                    }
                    for (Object o : user.getUserRoleList()) {
                        UserRole role = (UserRole) o;
                        if (role.getSystemUserList().contains(user)) {
                            role.getSystemUserList().remove(user);
                            em.merge(role);
                            em.refresh(role);
                            em.flush();
                        }
                    }
                    em.remove(user);
                    em.flush();
                    return 1;

                }
                System.out.println("The user looking for is null");
                return 0;
            } else {
                System.out.println("Access Denied");
                return -1;
            }
        } else {
            System.out.println("Operator is null");
            return 0;
        }

    }

    public int updateUserInfo(Integer systemAdminId, SystemUser updatedUser) {
        SystemUser operator = em.find(SystemUser.class, systemAdminId);
        boolean canRun = false;
        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }

            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }

            if (canRun) {
                SystemUser user = em.find(SystemUser.class, updatedUser.getSystemUserId());
                if (user != null) {
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName(updatedUser.getLastName());
                    user.setEmailAddress(updatedUser.getEmailAddress());
                    user.setPassword(updatedUser.getPassword());
                    user.setPostalAddress(updatedUser.getPostalAddress());
                    user.setContactNumber(updatedUser.getContactNumber());
                    user.setSalution(updatedUser.getSalution());
                    user.setLocked(updatedUser.getLocked());
                    user.setResetPasswordUponLogin(updatedUser.getResetPasswordUponLogin());
                    user.setActivated(updatedUser.getActivated());
                    user.setCredit(updatedUser.getCredit());

                    em.merge(user);
                    em.flush();
                    return 1;
                }
                System.out.println("The user looking for is null");
                return 0;

            }
            System.out.println("Access Denied");
            return -1;

        }
        System.out.println("Operator is null");
        return 0;

    }

    public int detachRoleFromUser(Integer systemAdminId, Integer userId, Integer roleId) {
        SystemUser operator = em.find(SystemUser.class, systemAdminId);
        boolean canRun = false;
        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }

            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }

            if (canRun) {
                SystemUser user = em.find(SystemUser.class, userId);
                if (user != null) {
                    UserRole role = em.find(UserRole.class, roleId);
                    if (role != null) {
                        if (user.getUserRoleList().contains(role)) {
                            user.getUserRoleList().remove(role);
                            role.getSystemUserList().remove(user);
                            em.merge(role);
                            em.merge(user);
                            em.flush();
                            return 1;
                        }
                    }
                    System.out.println("role is null");
                    return 0;
                }
                System.out.println("user is null");
                return 0;
            }
            System.out.println("Access Denied");
            return -1;
        }
        System.out.println("operator is null");
        return 0;
    }

    public int addRoleToUser(Integer systemAdminId, Integer userId, Integer roleId) {
        SystemUser operator = em.find(SystemUser.class, systemAdminId);
        boolean canRun = false;
        if (operator != null) {
            if (operator.getUserType().equals("superuser")) {
                canRun = true;
            }

            if (carb.userHasRight(operator, Right.canManageUser)) {
                canRun = true;
            }

            if (canRun) {
                SystemUser user = em.find(SystemUser.class, userId);
                if (user != null) {
                    UserRole role = em.find(UserRole.class, roleId);
                    user.getUserRoleList().add(role);
                    role.getSystemUserList().add(user);
                    em.merge(role);
                    em.merge(user);
                    return 1;
                }
                System.out.println("User is null");
                return 0;
            }
            System.out.println("Access Denied");
            return 1;
        }

        System.out.println("System Admin is null");
        return 0;
    }
}
