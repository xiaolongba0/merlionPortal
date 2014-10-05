/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.oes.ordermanagement;

import entity.SystemUser;
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
public class CommonFunctionSessionBean {
    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public CommonFunctionSessionBean() {
    }
    
    public String viewCustomerName(int customerId){
        String result=" ";
        SystemUser myCustomer = em.find(SystemUser.class, customerId);
        if(myCustomer !=null){
            result=myCustomer.getFirstName()+" "+myCustomer.getLastName();
        }
        return result;
    }


}
