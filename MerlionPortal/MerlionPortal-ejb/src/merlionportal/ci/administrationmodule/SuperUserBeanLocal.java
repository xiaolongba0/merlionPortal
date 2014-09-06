/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.ci.administrationmodule;

import entity.SystemUser;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author manliqi
 */
@Local
public interface SuperUserBeanLocal {

    public boolean createSystemAdminUser(String firstName, String lastName, String emailAddress, String postalAddress, String contactNumber, String salution, String userType, int companyId, int creatorid);

    public boolean createSystemAdminRole(int creatorId, int companyId, String roleName, String description);

    public boolean unlockSystemAdminUser(int creatorId, int systemAdminId);

    public boolean activateSystemAdminUser(int creatorId, int systemAdminId);

    public boolean terminateSystemAdminUser(int creatorId, int systemAdminId);

    public boolean changePasswordUponLogin(int creatorId, int systemAdminId);

    public ArrayList getAllSystemAdminUser();

    public ArrayList<SystemUser> getAllSystemAdminUserFromCompany(int companyId);

    public boolean assignRoleToSystemAdmin(int creatorId, int userId, ArrayList roles);
    
    
    

    
}
