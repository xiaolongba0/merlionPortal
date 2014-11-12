package merlionportal.ci.administrationmodule;

import entity.SystemUser;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class ChangePasswordSessionBean {
    
    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public boolean changeToNewPassword(String password, Integer userId) {
        boolean result = false;
        SystemUser user = em.find(SystemUser.class, userId);
        user.setPassword(password);
        user.setResetPasswordUponLogin(false);
        try {
            em.merge(user);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkExistingPassword(Integer userId, String existingPassword) {
        boolean result = false;
        SystemUser user = em.find(SystemUser.class, userId);
        if (user != null) {
            if (user.getPassword().equals(existingPassword)) {
                result = true;
            }
        }
        return result;
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
