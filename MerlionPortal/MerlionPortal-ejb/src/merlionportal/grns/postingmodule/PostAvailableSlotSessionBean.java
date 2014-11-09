/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.grns.postingmodule;

import entity.Post;
import entity.StorageBin;
import entity.WarehouseZone;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    public Integer AvailableBin (Integer warehouseID) {

        System.out.println("In viewStorageBinForType, WarehouseZoneID ============================= : " + warehouseZoneId);
        WarehouseZone typeTemp = null;

        if (warehouseZoneId != null) {
            typeTemp = em.find(warehouseID.WarehouseZone.warehousebin.class, warehouseId);
            System.out.println("In viewMyWarehouseZones, finding warehouse" + typeTemp);
        }
        return typeTemp.getStorageBinList();

    }
}
