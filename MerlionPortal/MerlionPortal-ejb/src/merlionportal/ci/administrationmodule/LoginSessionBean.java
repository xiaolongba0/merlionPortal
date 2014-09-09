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

@Stateless
@LocalBean
public class LoginSessionBean {

    @PersistenceContext
    EntityManager em;
    private int tryouts = 0;

    public SystemUser verifyAccount(String username, String password) {
        SystemUser user = em.find(SystemUser.class, username);
        //       if (user != null && lockAccount != true) {
        if (user != null) {
            String corrPassword = user.getPassword();
            if (password.equals(corrPassword)) {
                return user;
            }
        }
        return null;
    }
}
