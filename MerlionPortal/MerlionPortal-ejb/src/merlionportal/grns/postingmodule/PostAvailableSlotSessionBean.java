/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.grns.postingmodule;

import static entity.Contract_.warehouseId;
import entity.Post;
import entity.Warehouse;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import merlionportal.wms.warehousemanagementmodule.AssetManagementSessionBean;

/**
 *
 * @author wenxin274
 */
@Stateless
@LocalBean
public class PostAvailableSlotSessionBean {

    @PersistenceContext
    EntityManager em;
    private Post post;
    private Integer warehouseID;
    @EJB
    private AssetManagementSessionBean amsb;

    public Integer AvailableBin(Integer warehouseID) {

        System.out.println("In viewStorageBinForType, WarehouseID ============================= : " + warehouseId);
        Warehouse typeTemp = null;

        if (warehouseId != null) {
            typeTemp = em.find(warehouseID.WarehouseZone.warehousebin.class, warehouseId);
            System.out.println("In viewMyWarehouseZones, finding warehouse" + typeTemp);
        }
        amsb.viewAllMyBinsInAWarehouse(warehouseID);

    }
}
