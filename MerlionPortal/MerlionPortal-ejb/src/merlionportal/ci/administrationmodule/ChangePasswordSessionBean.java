/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.ci.administrationmodule;

import entity.SystemUser;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class ChangePasswordSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PersistenceContext
    EntityManager em;
    public boolean changeToNewPassword(String password, Integer userId){
        boolean result = false;
        SystemUser user = em.find(SystemUser.class, userId);
        user.setPassword(password);
        em.merge(user);
        result = true;
        return result;
    }
}
