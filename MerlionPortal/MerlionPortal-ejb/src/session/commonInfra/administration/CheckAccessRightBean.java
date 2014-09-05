/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.commonInfra.administration;

import entity.SystemUser;
import entity.UserRole;
import javax.ejb.Stateless;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Stateless
public class CheckAccessRightBean implements CheckAccessRightBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    
    public boolean userHasRight(SystemUser user, Right right){
        UserRole role = user.getUserRoleuserRoleId();
//        if(roleHasRight(role, right)){
//            return true;
//        }
        return false;
    }
    private boolean roleHasRight(UserRole role, int right){
        boolean hasRight = false;
        
        switch (right) {
            case Right.canGeneratePO:
                if(role.getCanGeneratePO()) hasRight=true;
                break;
            case Right.canGenerateSO:  
                if(role.getCanGenerateSO()) hasRight=true;     
                break;
            case Right.canGenerateQuotationAndProductContract:  
                if(role.getCanGenerateQuotationAndProductContract()) hasRight=true;      
                     break;
//            case 4:  monthString = "April";
//                     break;
//            case 5:  monthString = "May";
//                     break;
//            case 6:  monthString = "June";
//                     break;
//            case 7:  monthString = "July";
//                     break;
//            case 8:  monthString = "August";
//                     break;
//            case 9:  monthString = "September";
//                     break;
//            case 10: monthString = "October";
//                     break;
//            case 11: monthString = "November";
//                     break;
//            case 12: monthString = "December";
//                     break;
//            default: monthString = "Invalid month";
//                     break;
        }
        return true;
    }
}
