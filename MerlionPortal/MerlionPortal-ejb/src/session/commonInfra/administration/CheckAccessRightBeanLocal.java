/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.commonInfra.administration;

import entity.SystemUser;
import javax.ejb.Local;
import util.accessRightControl.Right;

/**
 *
 * @author manliqi
 */
@Local
public interface CheckAccessRightBeanLocal {
     public boolean userHasRight(SystemUser user, Right right);
}
