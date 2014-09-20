/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.wms.warehousemanagementmodule;

import entity.Company;
import entity.StorageType;
import entity.Warehouse;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author YunWei
 */
@Stateless
@LocalBean
public class AssetManagementSessionBean {

    @PersistenceContext
    EntityManager em;
    private Warehouse warehouse;
    private Integer comId;
    private ArrayList<StorageType> storageTypeList;
    private Company company;

    public List<Warehouse> viewAllWarehouses(Integer companyId) {
        Query query = em.createQuery("SELECT w FROM Warehouse w WHERE p=w.companyId = :inCompanyId");
        query.setParameter("inCompanyId", companyId);
        return query.getResultList();
    }

    public Integer addNewWarehouse(String warehouseName, String country, String city, String street,
            String description, Integer zipcode, Integer companyId) {
        System.out.println("[INSIDE EJB]================================Add New Warehouse");

        warehouse = new Warehouse();
   
        //Query query = em.createNamedQuery("Company.findByCompanyId");
        // System.out.println("================================Sucessfully Found Company");
        //query.setParameter("companyId", comId);
        // Company company = (Company) query.getSingleResult()
        // Company company = new Company(companyId);


        warehouse.setName(warehouseName);
        warehouse.setCountry(country);
        warehouse.setCity(city);
        warehouse.setStreet(street);
        warehouse.setDescription(description);
        warehouse.setZipcode(zipcode);
        warehouse.setCompanyId(companyId);

        storageTypeList = new ArrayList<StorageType>();
        warehouse.setStorageTypeList(storageTypeList);

        System.out.println("==========Warehouse Name========== :" + warehouseName);
        System.out.println("==========Country========== :" + country);
        System.out.println("==========City========== :" + city);
        System.out.println("==========Street========== :" + street);
        System.out.println("==========Description========== :" + description);
        System.out.println("==========Zip Code========== :" + zipcode);
        //System.out.println("=============Company============ :" + company);
        System.out.println("=============Company ID============ :" + companyId);

        em.persist(warehouse);
        em.flush();

        em.flush();

        System.out.println("[EJB]================================Add New Warehouse");

        return warehouse.getWarehouseId();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
