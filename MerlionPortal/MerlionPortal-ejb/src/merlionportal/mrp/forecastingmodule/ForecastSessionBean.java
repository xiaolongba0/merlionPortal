/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.mrp.forecastingmodule;

import entity.Product;
import java.util.List;
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
public class ForecastSessionBean {

    @PersistenceContext
    private EntityManager entityManager;
    private Product product;

    public List<Product> getMyProducts(Integer companyId) {
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyId);
        return query.getResultList();
    }

}
