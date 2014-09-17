/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.ci.administrationmodule;

import entity.Company;
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
public class CheckCompanyPackageSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    
    public int checkCompanyPackage( Integer companyId){
        Company company = em.find(Company.class, companyId);
        return company.getPackage1();
    }
}
