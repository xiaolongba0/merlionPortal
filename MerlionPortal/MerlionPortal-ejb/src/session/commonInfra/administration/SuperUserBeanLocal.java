/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.commonInfra.administration;

import entity.SystemUser;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author manliqi
 */
@Local
public interface SuperUserBeanLocal {
    
    public boolean createRole(String roleName, String description, ArrayList rights);
    public boolean createSystemAdminUser(String firstName,String lastName,String emailAddress,String password,String postalAddress,String contactNumber,String salution,String userType, int userRole, int companyId);
    public boolean unlockSystemAdminUser(int systemAdminId);
    public boolean activateSystemAdminUser(int systemAdminId);
    public boolean terminateSystemAdminUser(int systemAdminId);
    public boolean changePasswordUponLogin(int systemAdminId);
    public ArrayList<SystemUser> getAllSystemAdminUser();
    public ArrayList<SystemUser> getAllSystemAdminUserFromCompany(int companyId); 
    

    
}
