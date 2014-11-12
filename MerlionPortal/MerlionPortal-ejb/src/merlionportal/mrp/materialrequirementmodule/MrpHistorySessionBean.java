/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.mrp.materialrequirementmodule;

import entity.Company;
import entity.Component;
import entity.MRPList;
import entity.Mrp;
import entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author yao
 */
@Stateless
@LocalBean
public class MrpHistorySessionBean {

    @PersistenceContext
    private EntityManager entityManager;
    private ArrayList<Component> componentList; //componentList refers to BOM of a product
    private Product product;


    // Notes: take in companyId automatically after Login, later change to getMyProducts(Integer companyId)
    public List<Product> getMyProducts(Integer companyId) {
        Query query = entityManager.createQuery("SELECT m FROM MrpList m WHERE m.productId = :inCompanyId");
        query.setParameter("inCompanyId", companyId);
        return query.getResultList();
    }
    
    public List<MRPList> viewMrpLists(Integer productId){
         Query query = entityManager.createQuery("SELECT m FROM MRPList m WHERE m.productId = :inProductId");
        query.setParameter("inProductId", productId);
        return query.getResultList();
    }
    
    public List<Mrp> viewMrps(Integer mrplistId){
        List<Mrp> mrps = entityManager.find(MRPList.class, mrplistId).getMrpList();
        return mrps;
    }
    
   
}
