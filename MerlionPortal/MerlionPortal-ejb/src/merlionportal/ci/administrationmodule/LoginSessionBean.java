/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.administrationmodule;

import entity.SystemUser;
import java.util.HashMap;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class LoginSessionBean {

    @PersistenceContext
    EntityManager em;
    private int tryouts = 0;

    public HashMap<String, Integer> verifyAccount(String username, String password) {
        Query q = em.createNamedQuery("SystemUser.findByEmailAddress").setParameter("emailAddress", username);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            SystemUser user = (SystemUser) q.getResultList().get(0);
            HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
            String corrPassword = user.getPassword();
            if (password.equals(corrPassword)) {
                sessionMap.put("userId", user.getSystemUserId());
                sessionMap.put("companyId", user.getCompanycompanyId().getCompanyId());
                return sessionMap;
            }
            return null;
        }
    }
}
