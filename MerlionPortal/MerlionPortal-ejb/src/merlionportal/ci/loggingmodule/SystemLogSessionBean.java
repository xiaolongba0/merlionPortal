/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.ci.loggingmodule;

import entity.SystemLog;
import entity.SystemUser;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public void recordSystemLog(int userId, String actionName){
        SystemUser myUser = em.find(SystemUser.class, userId);
        newLog= new SystemLog();
        Date nowTime= new Date();
        newLog.setLogTime(nowTime);
        newLog.setSystemUsersystemUserId(myUser);
        newLog.setAction(actionName);
        em.persist(newLog);
    }
}
