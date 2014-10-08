/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.mpr.materialrequirementmodule;

import entity.Component;
import entity.Product;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yao
 */
@Stateless
@LocalBean
public class MaterialReqSessionBean {
 @PersistenceContext
    private EntityManager entityManager;
 
       // Notes: take in companyId automatically after Login, later change to getMyProducts(Integer companyId)
    public List<Component> getComponentsForAProduct(Integer productId) {
        Product productTemp = entityManager.find(Product.class, productId);
        return productTemp.getComponentList();
    }
    
    
}
