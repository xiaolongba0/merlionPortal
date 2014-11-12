/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.ci.administrationmodule;

import entity.Company;
import entity.SystemUser;
import java.util.List;
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
public class GetCompanyUserSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    EntityManager em;
    
    public List<SystemUser> getAllUsersInCompany( int companyId){
        Company company = em.find(Company.class, companyId);
      if (company!=null){
          List<SystemUser> userList = company.getSystemUserList();
          return userList;
      }
      System.out.println("There is no users defined for this company");
      return null;
    }
}
