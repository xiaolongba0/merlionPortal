/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.loggingmodule;

import entity.Company;
import entity.SystemLog;
import entity.SystemUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class SystemLogSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;
    private SystemLog newLog;

    public void persist(Object object) {
        em.persist(object);
    }

    public SystemLogSessionBean() {
    }

    public void recordSystemLog(int userId, String actionName) {
        SystemUser myUser = em.find(SystemUser.class, userId);
        newLog = new SystemLog();
        Date nowTime = new Date();
        newLog.setLogTime(nowTime);
        newLog.setSystemUsersystemUserId(myUser);
        newLog.setAction(actionName);
        em.persist(newLog);
    }

    public List<SystemLog> searchForAll() {
        List<SystemLog> allLog = new ArrayList();
        Query q = em.createQuery("SELECT s FROM SystemLog s");
        return q.getResultList();

    }

    public List<SystemLog> searchForLogByUserId(List<SystemLog> myList, Integer userId) {
        List<SystemLog> allLog = new ArrayList();
        for (Object o : myList) {
            SystemLog myLog = (SystemLog) o;
            if (Objects.equals(myLog.getSystemUsersystemUserId().getSystemUserId(), userId)) {
                allLog.add(myLog);
            }
        }
        return allLog;
    }

    public List<SystemLog> searchForLogByListOfUser(List<SystemLog> myList, String uN) {
        List<SystemLog> result = new ArrayList();
        List<SystemUser> userList = new ArrayList();
        userList = this.searchForUserByName(uN);
        for (Object o : userList) {
            int myU = ((SystemUser) o).getSystemUserId();
            List<SystemLog> indiList = new ArrayList();
            indiList = this.searchForLogByUserId(myList, myU);
            result.addAll(indiList);
        }
        return result;
    }

    public List<SystemLog> searchForLogAfter(List<SystemLog> myList, Date startDate) {
        List<SystemLog> allLog = new ArrayList();
        for (Object o : myList) {
            SystemLog myLog = (SystemLog) o;
            if (myLog.getLogTime().after(startDate)) {
                allLog.add(myLog);
            }
        }
        return allLog;
    }

    public List<SystemLog> searchForLogBefore(List<SystemLog> myList, Date endDate) {
        List<SystemLog> allLog = new ArrayList();
        for (Object o : myList) {
            SystemLog myLog = (SystemLog) o;
            if (myLog.getLogTime().before(endDate)) {
                allLog.add(myLog);
            }
        }
        return allLog;
    }

    public List<SystemLog> getAllLog(int userId) {
        List<SystemLog> allLog = new ArrayList();
        List<SystemLog> result = new ArrayList();

        SystemUser myUser = em.find(SystemUser.class, userId);
        Query q = em.createQuery("SELECT s FROM SystemLog s");
        for (Object o : q.getResultList()) {
            SystemLog sysLog = (SystemLog) o;
            allLog.add(sysLog);
        }
        if (myUser.getUserType() != null) {
            if (myUser.getUserType().equals("superuser")) {
                result = allLog;
            }

        } else {
            for (Object o : allLog) {
                SystemLog mylog = (SystemLog) o;
                if (mylog.getSystemUsersystemUserId().getSystemUserId() == userId) {
                    result.add(mylog);
                }
            }
        }
        return result;
    }

    private List<Company> searchForCompanyByName(String companyName) {
        List<Company> companyList = new ArrayList();

        Query q = em.createQuery("SELECT c FROM Company c");
        companyName = companyName.toUpperCase();
        for (Object c : q.getResultList()) {
            Company myCompany = (Company) c;
            String myCompanyName = myCompany.getName().toUpperCase();
            if (myCompanyName.contains(companyName)) {
                companyList.add(myCompany);
            }
        }

        return companyList;
    }

    private Company searchForCompanyById(int cId) {
        Company myCompany;
        myCompany = em.find(Company.class, cId);
        return myCompany;
    }

    private SystemUser searchForUserById(int uId) {
        SystemUser myUser;
        myUser = em.find(SystemUser.class, uId);
        return myUser;
    }

    private List<SystemUser> searchForUserByName(String uName) {
        List<SystemUser> result = new ArrayList();
        Query q = em.createQuery("SELECT c FROM SystemUser c");
        uName = uName.toUpperCase();
        for (Object c : q.getResultList()) {
            SystemUser myUser = (SystemUser) c;
            String myName = myUser.getFirstName();
            if (myName.contains(uName)) {
                result.add(myUser);
            }
        }
        return result;
    }

    private List<SystemUser> getAllUsersOfaCompany(int companyId) {
        List<SystemUser> result = new ArrayList();
        Query q = em.createQuery("SELECT s FROM SystemUser s");
        for (Object o : q.getResultList()) {
            SystemUser su = (SystemUser) o;
            if (companyId == su.getCompanycompanyId().getCompanyId()) {
                result.add(su);
            }
        }
        return result;
    }

    private List<Integer> getALlIdOfCompany(List<Company> companyList) {
        List<Integer> result = new ArrayList();
        for (Object o : companyList) {
            Company c = (Company) o;
            result.add(c.getCompanyId());
        }
        return result;
    }

    public String getCompanyName(SystemUser myUser) {
        String result;
        result = myUser.getCompanycompanyId().getName();
        return result;
    }

}
