/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.quotationmanagementmodule;

import entity.SystemUser;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import merlionportal.ci.administrationmodule.CheckAccessRightBean;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class CheckCustomerRoleSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    CheckAccessRightBean carb;

    @PersistenceContext
    EntityManager em;

    public boolean checkUserIsCustomer(Integer customerId) {
        SystemUser user = (SystemUser) em.find(SystemUser.class, customerId);
        if (user != null) {
            return carb.userHasRight(user, Right.canGeneratePO);
        }
        System.out.println("System user is null");
        return false;
    }
}
