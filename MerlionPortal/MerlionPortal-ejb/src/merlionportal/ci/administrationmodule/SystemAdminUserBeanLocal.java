/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.ci.administrationmodule;

import javax.ejb.Local;


@Local
public interface SystemAdminUserBeanLocal {
    public void createUser();
    public void createRole();
    public void unlockUser();
    public void terminateUser();
    public void activateUser();
}
