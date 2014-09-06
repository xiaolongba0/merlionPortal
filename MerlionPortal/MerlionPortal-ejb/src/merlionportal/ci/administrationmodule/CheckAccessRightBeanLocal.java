/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.ci.administrationmodule;

import entity.SystemUser;
import entity.UserRole;
import javax.ejb.Local;

/**
 *
 * @author manliqi
 */
@Local
public interface CheckAccessRightBeanLocal {

    public boolean userHasRight(SystemUser user, int right);

    public boolean roleHasRight(UserRole role, int right);

    public void roleToggleRight(UserRole role, int right, boolean approve);
}
