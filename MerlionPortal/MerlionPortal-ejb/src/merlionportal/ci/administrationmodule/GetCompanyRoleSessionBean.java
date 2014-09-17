/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.ci.administrationmodule;

import entity.Company;
import entity.UserRole;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class GetCompanyRoleSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    
    public List<UserRole> getAllRolesInCompany(Integer companyId){
      Company company = em.find(Company.class, companyId);
      if (company!=null){
          List<UserRole> roleList = company.getUserRoleList();
          return roleList;
      }
      System.out.println("There is no roles defined for this company");
      return null;
    }
    
    public UserRole getOneRole (Integer userRoleId){
        if(userRoleId != null){
            Query q = em.createNamedQuery("UserRole.findByUserRoleId").setParameter("userRoleId", userRoleId);
            return (UserRole)q.getSingleResult();
        }
        return null;
    }
}
