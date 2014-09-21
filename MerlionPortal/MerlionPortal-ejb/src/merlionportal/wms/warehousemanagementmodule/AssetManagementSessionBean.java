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
import java.util.Objects;
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

    public List<Warehouse> viewMyWarehouses(Integer companyId) {
        List<Warehouse> allMyWarehouses = new ArrayList<>();
        System.out.println("In viewMyWarehouses, Company ID ============================= : " + companyId);
        Query query = em.createNamedQuery("Warehouse.findByCompanyId").setParameter("companyId", companyId);
        for (Object o : query.getResultList()) {
            warehouse = (Warehouse) o;
            allMyWarehouses.add(warehouse);
        }

        return allMyWarehouses;
    }

       public Boolean deleteWarehouse(Integer companyId, Integer warehouseId) {

        Query query = em.createNamedQuery("Warehouse.findByCompanyId").setParameter("companyId", companyId);
        List<Warehouse> allMyWarehouses = query.getResultList();

        for (Warehouse w : allMyWarehouses) {
            if (Objects.equals(w.getWarehouseId(), warehouseId)) {
                em.remove(w);
                em.flush();
                return true;
            }
        }
        return false;
    }
       
    public Integer addNewWarehouse(String warehouseName, String country, String city, String street,
            String description, Integer zipcode, Integer companyId) {
        System.out.println("[INSIDE EJB]================================Add New Warehouse");

        warehouse = new Warehouse();

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
        System.out.println("=============Company ID============ :" + companyId);

        em.persist(warehouse);
        em.flush();

        System.out.println("[EJB]================================Successfully Added a New Warehouse");

        return warehouse.getWarehouseId();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
