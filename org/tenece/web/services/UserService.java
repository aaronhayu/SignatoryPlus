
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 27 May 2009, 12:45
 *
 * ============================================================
 * Project Info:  http://www.tenece.com
 * Project Lead:  Amachree Jeffry (info@tenece.com);
 * ============================================================
 *
 *
 * Licensed under the Tenece Professional Services;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.tenece.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Strategiex. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "as is," without a warranty of any
 * kind. All express or implied conditions, representations and
 * warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose or non-infringement, are hereby
 * excluded.
 * Tenece and its licensors shall not be liable for any damages
 * suffered by licensee as a result of using, modifying or
 * distributing the software or its derivatives. In no event will Strategiex
 * or its licensors be liable for any lost revenue, profit or data, or
 * for direct, indirect, special, consequential, incidental or
 * punitive damages, however caused and regardless of the theory of
 * liability, arising out of the use of or inability to use software,
 * even if Tenece Professional Services has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package org.tenece.web.services;

import org.tenece.ap.data.dao.UserDAO;
import org.tenece.web.data.Users;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.tenece.web.data.AuditTrail;
import org.tenece.web.data.Category;
import org.tenece.web.data.Lock;
import org.tenece.web.data.MenuData;
import org.tenece.web.data.MenuSubMenuInfo;
import org.tenece.web.data.Role;

/**
 *
 * @author jeffry.amachree
 */
public class UserService extends BaseService{
    
    private UserDAO userDAO = null;

    /** Creates a new instance of PayrollService */
    public UserService() {
    }

    public boolean updateUsers(Users user, int mode){
         boolean saved = false;
         try{
            
            if(this.MODE_INSERT == mode){
                System.out.println("Insert User");
                saved = getUserDAO().createNewUsers(user);
                
            }else if(this.MODE_UPDATE == mode){
                System.out.println("Edit User");
                saved = getUserDAO().updateUsers(user);
            }
            
               
        }catch(Exception e){
            return false;
        }
        return  saved;
    }

    public List<Users> findAllUsers(){
        try{
            return getUserDAO().getAllUsers();
        }catch(Exception e){
            return new ArrayList<Users>();
        }
    }
      public List<Users> findAllUsersWithOutUserID(int userId ){
        try{
            return getUserDAO().getAllUsersWithOutUserID(userId);
        }catch(Exception e){
            return new ArrayList<Users>();
        }
    }
     public List<Users> findAllUsersWithOutRole(){
        try{
            return getUserDAO().getAllUsersWithOutRole();
        }catch(Exception e){
            return new ArrayList<Users>();
        }
    }
     public List<Users> findAllUsers(String criteria, String searchText){
        try{
            return getUserDAO().getAllUsers(criteria, searchText);
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Users>();
        }
    }
       public List<Users> findAllUsersOutRole(String criteria, String searchText){
        try{
            return getUserDAO().getAllUsersWithOutRole(criteria, searchText);
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Users>();
        }
    }
    public List<Users> findAllUsersByCompany(String code){
        try{
            return getUserDAO().getAllUsersByCompany(code);
        }catch(Exception e){
            return new ArrayList<Users>();
        }
    }

    public List<Users> findAllLockedUsers(){
        try{
            return getUserDAO().getAllLockedUsers();
        }catch(Exception e){
            return new ArrayList<Users>();
        }
    }

    public Users findUsersById(int id1,int id2){
        try{
            return getUserDAO().getUsersById(id1,id2);
        }catch(Exception e){
            return null;
        }
    }
// public Users findUsersById(int id){
//        try{
//            return getUserDAO().getUsersById(id);
//        }catch(Exception e){
//            return null;
//        }
//    }
    public Lock findLockById(int id){
        try{
            return getUserDAO().getUserLockById(id);
        }catch(Exception e){
            return null;
        }
    }

    public int deactivateLockOnUser(Lock lock){
        try{
            return getUserDAO().deactivateLock(lock);
        }catch(Exception e){
            return 0;
        }
    }

    public boolean deleteUsers(int id,String userName){
        try{
            Users user = new Users();
            user.setId(id);

            getUserDAO().deleteUsers(user,userName);
            return true;
        }catch(Exception e){
            return false;
        }
    }
  public int deleteMenuInRole(int id){
        try{
 
           return getUserDAO().deleteMenuInRole(id);
            
        }catch(Exception e){
            return 0;
        }
    }
    public Users findUserByUserNameAndEmployeeID(String userName, long employeeId){
        return getUserDAO().getUserByUserNameAndEmployeeID(userName, employeeId);
    }

    public AuditTrail getAuditSummary(){
        return getUserDAO().getAuditSummary();
    }

    public boolean archiveAuditLog(){ 
        return getUserDAO().archiveAuditLog();
    }
    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO() {
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        return userDAO;
    }
    
    
    
     public boolean updateCategory(Category cat){
        try{
            int saved = 0;
            
             saved = getUserDAO().updateCategory(cat);
           
            if(saved > 0)
                return true;
            else
                return false;
        }catch(Exception e){
            return false;
        }
    }
     public MenuSubMenuInfo getMenu(int userId){
       MenuSubMenuInfo  menuSubmenuInfo  = new MenuSubMenuInfo();
         try
         {
             
            menuSubmenuInfo = getUserDAO().getMenu(userId);
          }catch(Exception e){
            return new MenuSubMenuInfo();
          }
      
         return menuSubmenuInfo;
    }
     public List<MenuData> findAllMenuItems(int roleId){
     
         try
         {
             
            return  getUserDAO().getAllMenuItem(roleId);
          }catch(Exception e){
            return new ArrayList<MenuData>();
          }
      
         
    }
      public List<MenuData> findAllMenuItems(){
           
           
         try
         {
             
            return  getUserDAO().getAllMenuItem();
          }catch(Exception e){
            return new ArrayList<MenuData>();
          }
        
       }
      
      public List<Role> findRoles(){
       List<Role>  roleList  = null;
         try
         {
             
            roleList = getUserDAO().getRoleList();
          }catch(Exception e){
            return new ArrayList<Role>();
          }
      
         return roleList;
    }
     public int saveRole(String roleName){
        try{

             int saved = getUserDAO().saveRole(roleName);
             return saved;
              
        }catch(Exception e){
            return 0;
        }
    }
       public boolean assignMenuToRole(int roleId,int menuId){
        try{
            
             boolean saved = getUserDAO().assignMenuToRole(roleId,menuId);
             return saved;
   
        }
        catch(Exception e){
            return false;
        }
    }
       public boolean checkIFAssigned(int roleId,int menuId){
        try{

             boolean checked = getUserDAO().checkIFAssigned(roleId,menuId);
             return checked;
   
        }
        catch(Exception e){
            return false;
        }
    }
      public String findRoleNameByRoleId(int roleId){
        try{

             String roleName = getUserDAO().getRoleNameByRoleId(roleId);
             return roleName;
   
        }
        catch(Exception e){
            return null;
        }
    }
       public String findRoleNameByRoleName(String roleN){
        try{

             String roleName = getUserDAO().getRoleNameByRoleName(roleN.toUpperCase());
             return roleName;
   
        }
        catch(Exception e){
            return null;
        }
    }
      
     public String findMenuNameByMenuId(int menuId){
        try{

             String menuName = getUserDAO().getMenuNameByMenuId(menuId);
             return menuName;
   
        }
        catch(Exception e){
            return null;
        }
    }
        public int lockUser(Lock lock){
        try{
            
             int saved = getUserDAO().lockUser(lock);
             return saved;
   
        }
        catch(Exception e){
            return 0;
        }
    }
           public int updateUsersLoginTries(Users user){
        try{
            
             int saved = getUserDAO().updateUsersLoginTries(user);
             return saved;
   
        }
        catch(Exception e){
            return 0;
        }
    }
    public Users findUserByTransaction(long id){
        return getUserDAO().getUserByTransaction(id);
    }
    public List<Users> findBulkUserByTransaction(long id){
        return getUserDAO().getBulkUserByTransaction(id);
    }
    
}
