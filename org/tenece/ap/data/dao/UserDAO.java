/*
 * (c) Copyright 2008 Tenece Professional Services.
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
 * even if Strategiex has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */
package org.tenece.ap.data.dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.sql.CallableStatement;
import org.tenece.ap.dao.db.DatabaseQueryLoader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.tenece.ap.constant.AuditOperationCodeConstant;
import org.tenece.ap.security.SecurityEncoderImpl;
import org.tenece.web.data.AuditTrail;
import org.tenece.web.data.Category;
import org.tenece.web.data.Department;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Lock;
import org.tenece.web.data.MenuData;
import org.tenece.web.data.MenuSubMenuInfo;
import org.tenece.web.data.Role;
import org.tenece.web.data.Users;

/**
 *
 * @author jeffry.amachree
 */
public class UserDAO extends BaseDAO {
private EmployeeDAO employeeDAO = new EmployeeDAO();
    /**
     * Creates a new instance of DepartmentDAO
     */
    public UserDAO() {
    }

    /* ************* Userss ********** */
    public List<Users> getAllUsers() {
        Connection connection = null;
        List<Users> records = new ArrayList<Users>();
        try {
            connection = this.getConnection(true);
            ResultSet rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT, connection);

            while (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                item.setRoleName(rst.getString("RoleName"));
                item.setFullnames(rst.getString("fullname"));
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                records.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }
    public List<Users> getAllUsersWithOutUserID(int userId) {
        Connection connection = null;
        List<Users> records = new ArrayList<Users>();
        Vector param =new Vector();
        Vector type = new Vector();
        try {
            param.add(userId);
            type.add("NUMBER");
            param.add(userId);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_DETILS_WITH_APP_LEVEL_USER,
                    param, type);
          
            while (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                item.setRoleName(rst.getString("RoleName"));
                item.setFullnames(rst.getString("fullname"));
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                records.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }
    /* ************* Userss ********** */
    public List<Users> getAllUsersWithOutRole() {
        Connection connection = null;
        List<Users> records = new ArrayList<Users>();
        try {
            connection = this.getConnection(true);
            ResultSet rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_WITHOUT_ROLE, connection);

            while (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                
                item.setFullnames(rst.getString("fullname"));
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                if(item.getActive().trim().equals("1"))
                {
                   item.setStatusDes("Active");
                   
                }
                if(item.getActive().trim().equals("0"))
                {
                   item.setStatusDes("Inactive");
                   
                }
                if(item.getActive().trim().equals("P"))
                {
                   item.setStatusDes("Pending Approval");  
                }
                if(item.getActive().trim().equals("R"))
                {
                   item.setStatusDes("Rejected");  
                }
                records.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }
public List<Users> getAllUsers(String criteria, String searchText){
        Connection connection = null;
        List<Users> records = new ArrayList<Users>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;
            System.out.print("criteria ==" + criteria);
                System.out.print("searchText ==" + searchText);
            if(criteria == null || searchText == null){
                System.out.println(DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT);
                rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT,
                        param, type);
            }else{
                String query = "";
                //param.add(searchText); type.add("STRING");
               
                if(criteria.equalsIgnoreCase("NAME")){

                    query = DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_SELECT_ID;
                }
                query = query.replaceAll("_SEARCH_", searchText);

                System.out.println("One search Query...:" + query);
                
                rst = this.executeParameterizedQuery(connection, query,
                        param, type);
            }
            
            while(rst.next()){
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                item.setRoleName(rst.getString("RoleName"));
                item.setFullnames(rst.getString("fullname"));
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                records.add(item);
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }
public List<Users> getAllUsersWithOutRole(String criteria, String searchText){
        Connection connection = null;
        List<Users> records = new ArrayList<Users>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;
            System.out.print("criteria ==" + criteria);
                System.out.print("searchText ==" + searchText);
            if(criteria == null || searchText == null){
                System.out.println(DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT);
                rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT,
                        param, type);
            }else{
                String query = "";
                //param.add(searchText); type.add("STRING");
               
                if(criteria.equalsIgnoreCase("NAME")){

                    query = DatabaseQueryLoader.getQueryAssignedConstant().USER_WITHOUT_ROLE_SELECT_BY_SELECT_ID;
                }
                query = query.replaceAll("_SEARCH_", searchText);

                System.out.println("One search Query...:" + query);
                
                rst = this.executeParameterizedQuery(connection, query,
                        param, type);
            }
            
            while(rst.next()){
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                
                item.setFullnames(rst.getString("fullname"));
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                if(item.getActive().trim().equals("1"))
                {
                   item.setStatusDes("Active");
                   
                }
                if(item.getActive().trim().equals("0"))
                {
                   item.setStatusDes("Inactive");
                   
                }
                if(item.getActive().trim().equals("P"))
                {
                   item.setStatusDes("Pending Approval");  
                }
                if(item.getActive().trim().equals("R"))
                {
                   item.setStatusDes("Rejected");  
                }
                records.add(item);
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }
    public List<Users> getAllUsersByCompany(String code) {
        Connection connection = null;
        List<Users> records = new ArrayList<Users>();

        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(code);
            type.add("STRING");

            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_COMPANY, param, type);

            while (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                item.setRoleName(rst.getString("RoleName"));
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }

                records.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }

    public Users getUsersById(int id) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_ID_WITHOUT_UNION,
                    param, type);

            while (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                item.setRoleId(rst.getInt("roleid"));
                item.setRoleName(rst.getString("RoleName"));
                item.setFullnames(rst.getString("fullname"));
                
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }

                record = item;
            }
        } catch (Exception e) {
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return record;
    }
 public Users getUsersById(int id1,int id2) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(id1);
            type.add("NUMBER");
             param.add(id2);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_ID,
                    param, type);

            while (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                item.setRoleId(rst.getInt("roleid"));
                item.setRoleName(rst.getString("RoleName"));
                item.setFullnames(rst.getString("fullname"));
                
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }

                record = item;
            }
        } catch (Exception e) {
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return record;
    }

    /* ************* User Locks ********** */
    public List<Users> getAllLockedUsers() {
        Connection connection = null;
        List<Users> records = new ArrayList<Users>();
        try {
            connection = this.getConnection(true);
            ResultSet rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_LOCKED_STATUS, connection);

            while (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));

                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                item.setRoleName(rst.getString("RoleName"));
                

                records.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }

    public Lock getUserLockById(int id) {
        Connection connection = null;
        Lock record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_LOCK_SELECT_BY_ID,
                    param, type);

            while (rst.next()) {
                Lock item = new Lock();
                item.setId(rst.getInt("user_id"));
                item.setDateLocked(new Date(rst.getDate("datelock").getTime()));
                item.setReason(rst.getString("reasonlock"));
                item.setLockedBy(rst.getString("lockedby"));
                item.setActive(rst.getString("active"));

                record = item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return record;
    }

    public boolean createNewUsers(Users Users) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(false);

            param.add(Users.getUserName());
            type.add("STRING");
            // for FBN
            param.add(Users.getUserName());
            type.add("STRING");
            param.add(Users.getAdminUser());
            type.add("NUMBER");
            param.add(Users.getSuperAdmin());
            type.add("NUMBER");
            param.add(Users.getNumberLogins());
            type.add("NUMBER");
            param.add(Users.getActive());
            type.add("STRING");
            param.add(Users.getDateUpdated());
            type.add("DATE");
            param.add(Users.getDateSignup());
            type.add("DATE");
            param.add(Users.getLastLoginDate());
            type.add("DATE");
            param.add(Users.getRoleId());
            type.add("NUMBER");
            CallableStatement cs = null;
            if (Users.getEmployeeId() > 0) {
                param.add(Users.getEmployeeId());
                type.add("NUMBER");
                this.startTransaction(connection);
                cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_INSERT,
                        param, type);
            } else {
                param.add(Users.getFullnames());
                type.add("STRING");
                this.startTransaction(connection);
                
                cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_INSERT_WITHOUT_EMPL_ID,
                        param, type);
            }
            if (cs == null) {
                throw new Exception("Unable to execute procedure...");
            }
            this.commitTransaction(connection);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public boolean updateUsers(Users Users) {
        Connection connection = null;
        Department record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(false);
            param.add(Users.getActive());
            type.add("STRING");
            param.add(new Date());
            type.add("DATE");
            param.add(Users.getId());
            type.add("NUMBER");
            param.add(Users.getRoleId());
            type.add("NUMBER");
            this.startTransaction(connection);
            CallableStatement cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_UPDATE,
                    param, type);
            if (cs == null) {
                throw new Exception("Unable to execute procedure...");
            }
            this.commitTransaction(connection);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~
public int updateUsers(Users Users,Connection connection) {
        
        Department record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            
            param.add(Users.getActive());
            
            type.add("STRING");
            param.add(new Date());
            type.add("DATE");
            param.add(Users.getId());
            type.add("NUMBER");
            param.add(Users.getRoleId());
            type.add("NUMBER");
            
            CallableStatement cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_UPDATE,
                    param, type);
            if (cs == null) {
                throw new Exception("Unable to execute procedure...");
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } 
    }//:~
public boolean updateUsersDuringDeleteTransation(Users Users,Connection connection) {
        
        Department record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            
            param.add(Users.getActive());
            type.add("STRING");
            param.add(Users.getTransactionId());
            type.add("NUMBER");
            param.add(Users.getId());
            type.add("NUMBER");
            CallableStatement cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_UPDATE_DURING_DELETE_TXN,
                    param, type);
            if (cs == null) {
                throw new Exception("Unable to execute procedure...");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } 
    }//:~
    public int updateUsersLoginTries(Users Users) {
        Connection connection = null;
        Department record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(Users.getNumberLogins());
            type.add("NUMBER");
            param.add(Users.getId());
            type.add("NUMBER");

            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_UPDATE_LOGIN,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int updateUserPassword(long userId, String password) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(password);
            type.add("STRING");
            param.add(new Date());
            type.add("DATE");
            param.add(userId);
            type.add("NUMBER");

            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_UPDATE_PASSWORD,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int resetUserPassword(long userId, String password) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(password);
            type.add("STRING");
            //param.add(new Date()); type.add("DATE");
            param.add(userId);
            type.add("NUMBER");

            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_RESET_PASSWORD,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int updateUserLoginInfo(long userId) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(new Date());
            type.add("DATE");
            param.add(userId);
            type.add("NUMBER");

            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_UPDATE_OK_LOGIN,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public AuditTrail getAuditSummary() {
        Connection connection = null;
        AuditTrail record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_SUMMARY_SELECT,
                    param, type);

            while (rst.next()) {
                AuditTrail item = new AuditTrail();
                item.setTotalUsers(rst.getInt("user_count"));
                item.setTotalRecord(rst.getInt("total_records"));

                record = item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return record;
    }

    public boolean archiveAuditLog() {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {

            connection = this.getConnection(false);
            startTransaction(connection);

            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_ARCHIVE_INSERT,
                    param, type);

            int a = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_ARCHIVE_DELETE,
                    param, type);

            commitTransaction(connection);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                rollbackTransaction(connection);
            } catch (Exception ex) {
            }
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int deleteUsers(Users Users,String userName) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(Users.getId());
            type.add("NUMBER");

            connection = this.getConnection(false);
            String fullname = this.getEmployeeDAO().getUnAuthorisedSignatoryUserByUserName(userName);
            startTransaction(connection);
            if(!fullname.equals(null))
            {
                Vector param1 = new Vector();
                Vector type1 = new Vector();
                param1.add(userName);
                type1.add("STRING");
               int l= this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_UNAUTHORISED_EMPLOYEE_DELETE,
                    param1, type1);
            }
            
              int k = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_AUDIT_TRAIL_DELETE,
                    param, type);
            int j = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_ROLE_DELETE,
                    param, type);
            int i =0;
            
               i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_DELETE,
                    param, type);
            int l = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_WORKFLOW_DELETE,
                    param, type);
            commitTransaction(connection);
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            try{rollbackTransaction(connection);}catch(Exception er){}
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int deleteUsersAproval(Users Users,String userName,Connection connection) {
        
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(Users.getId());
            type.add("NUMBER");

            String fullname = this.getEmployeeDAO().getUnAuthorisedSignatoryUserByUserName(userName);
            if(!fullname.equals(null))
            {
                Vector param1 = new Vector();
                Vector type1 = new Vector();
                param1.add(userName);
                type1.add("STRING");
               int l= this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_UNAUTHORISED_EMPLOYEE_DELETE,
                    param1, type1);
            }
            
              int k = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_AUDIT_TRAIL_DELETE,
                    param, type);
            int j = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_ROLE_DELETE,
                    param, type);
            int i =0;
            int l = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_IN_WORKFLOW_DELETE,
                    param, type);
               i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_DELETE,
                    param, type);
            
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            try{rollbackTransaction(connection);}catch(Exception er){}
            return 0;
        } 
    
    }//:~
    public int lockUser(Lock lock) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(lock.getUserId());
            type.add("NUMBER");
            param.add(lock.getDateLocked());
            type.add("DATE");
            param.add(lock.getReason());
            type.add("STRING");
            param.add(lock.getLockedBy());
            type.add("STRING");
            param.add(lock.getActive());
            type.add("STRING");

            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_LOCK_INSERT,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int deactivateLock(Lock lock) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(false);
            startTransaction(connection);

            //param.add(lock.getActive()); type.add("STRING");
            param.add(lock.getUserId());
            type.add("NUMBER");

            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_LOCK_DEACTIVATE,
                    param, type);

            i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_RESET_LOGIN_TRIES,
                    param, type);

            commitTransaction(connection);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                rollbackTransaction(connection);
            } catch (Exception er) {
            }

            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public Users getUserByUserNameAndPassword(Users Users) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(Users.getUserName());
            type.add("STRING");
            param.add(Users.getPassword());
            type.add("STRING");

            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_UID_AND_PASSWORD,
                    param, type);

            if (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));

                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                record = item;
            }
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public Users getUserByUserNameAndEmployeeID(String userName, long employeeID) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(userName);
            type.add("STRING");
            param.add(employeeID);
            type.add("NUMBER");

            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_UID_AND_EMP_ID,
                    param, type);

            if (rst.next()) {
                Users item = new Users();
                item.setPassword(rst.getString("password"));
                item.setId(rst.getInt("user_id"));

                record = item;
            }
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public Users getUserByUserName(Users Users) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(Users.getUserName());
            type.add("STRING");
            param.add(Users.getUserName());
            type.add("STRING");
            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_UID,
                    param, type);

            if (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));
                item.setFullnames(rst.getString("fullname"));
                item.setRoleId(rst.getInt("RoleId"));
 
                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }
                java.sql.Date dateupdated = rst.getDate("dateupdated");
                if (dateupdated != null) {
                    item.setDateUpdated(new Date(dateupdated.getTime()));
                }
                
                java.sql.Date dateLogin = rst.getDate("datelogin");
                if (dateLogin != null) {
                    item.setDateLogin(new Date(dateLogin.getTime()));
                } else {
                    item.setDateLogin(null);
                }
                item.setLastLoginTime(rst.getString("Time"));
                record = item;
            }
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~
    public Users getUserByUserName(String UserName) {
        Connection connection = null;
        Users record = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(UserName);
            type.add("STRING");
            param.add(UserName);
            type.add("STRING");
            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_UID,
                    param, type);

            if (rst.next()) {
                Users item = new Users();
                item.setId(rst.getInt("user_id"));
                item.setEmployeeId(rst.getInt("emp_number"));
                item.setUserName(rst.getString("user_name"));
                item.setPassword(rst.getString("password"));
                item.setAdminUser(rst.getInt("admin_user"));
                item.setSuperAdmin(rst.getInt("superadmin"));
                item.setNumberLogins(rst.getInt("numlogins"));
                item.setActive(rst.getString("active"));

                java.sql.Date lastLoginDate = rst.getDate("lastlogindate");
                if (lastLoginDate != null) {
                    item.setLastLoginDate(new Date(lastLoginDate.getTime()));
                }

                java.sql.Date dateLogin = rst.getDate("datelogin");
                if (dateLogin != null) {
                    item.setDateLogin(new Date(dateLogin.getTime()));
                } else {
                    item.setDateLogin(null);
                }

                record = item;
            }
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int updateCategory(Category cat) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try {


            param.add(cat.getTransactionLimt());
            type.add("AMOUNT");
            param.add(cat.getKey());
            type.add("STRING");
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().CATEGORY_UPDATE,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~
 public int deleteMenuInRole(int id) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try {


            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().MENU_IN_ROLE_DELETE,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~
    public int saveRole(String roleName) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try {


            param.add(roleName);
            type.add("STRING");
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().ROLE_INSERT,
                    param, type);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public boolean checkIFAssigned(int roleId, int MenuId) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(roleId);
            type.add("NUMBER");
            param.add(MenuId);
            type.add("NUMBER");
            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().CHECK_IF_MENU_ASSIGNED,
                    param, type);

            if (rst.next()) {
                return true;
            } else {
                return false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean assignMenuToRole(int roleId, int MenuId) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try {


            param.add(roleId);
            type.add("NUMBER");
            param.add(MenuId);
            type.add("NUMBER");
            connection = this.getConnection(false);

            this.startTransaction(connection);
            CallableStatement cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().MENU_IN_ROLE_INSERT,
                    param, type);

            if (cs == null) {
                throw new Exception("Unable to execute procedure...");
            }
            this.commitTransaction(connection);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public MenuSubMenuInfo getMenu(int UserId) {
        List<MenuData> records = new ArrayList<MenuData>();
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        MenuSubMenuInfo menuSubMenuInfo = new MenuSubMenuInfo();
        Multimap<String, MenuData> multiMap = ArrayListMultimap.create();
        try {


            param.add(UserId);
            type.add("NUMBER");

            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().MENU_SELECT_BY_USERID,
                    param, type);
            while (rst.next()) {
                MenuData item = new MenuData();
                item.setMenuTitle(rst.getString("MenuTitle"));
                item.setDivClass(rst.getString("class"));
                item.setURL(rst.getString("URL"));
                item.setSubmenutitle(rst.getString("submenutitle"));
                item.setTarget(rst.getString("Location"));
                item.setOrderCode(rst.getInt("OrderCode"));
                if (item.getDivClass().trim().equals("submenu")) {
                    multiMap.put(item.getMenuTitle(), item);

                } else {
                    records.add(item);
                }

            }
            menuSubMenuInfo.setMenuList(records);
            menuSubMenuInfo.setSubMenuMap(multiMap);
            return menuSubMenuInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return new MenuSubMenuInfo();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public List<MenuData> getAllMenuItem(int roleId) {
        List<MenuData> records = new ArrayList<MenuData>();
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(false);
            ResultSet rst = null;
            if (roleId > 0) {
                param.add(roleId);
                type.add("NUMBER");
                rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().MENU_SELECT_BY_ASSIGN_ROLE,
                        param, type);
            } else {

                rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().MENU_SELECT,
                        param, type);
            }

            while (rst.next()) {
                MenuData item = new MenuData();
                item.setMenuTitle(rst.getString("MenuTitle"));
                item.setMenuId(rst.getInt("MenuId"));
                item.setSubmenutitle(rst.getString("submenutitle"));
                item.setId(rst.getInt("MenuId"));
                item.setMenuInRoleId(rst.getInt("Menu_In_RoleID"));
                item.setRoleName(rst.getString("RoleName"));
                item.setRoleId(rst.getInt("RoleId"));
                
                records.add(item);


            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }//:~

    public List<MenuData> getAllMenuItem() {
        List<MenuData> records = new ArrayList<MenuData>();
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().MENU_SELECT,
                    param, type);


            while (rst.next()) {
                MenuData item = new MenuData();
                item.setMenuTitle(rst.getString("MenuTitle"));
                item.setMenuId(rst.getInt("MenuId"));
                item.setSubmenutitle(rst.getString("submenutitle"));
                item.setId(rst.getInt("MenuId"));

                records.add(item);


            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }//:~

    public List<Role> getRoleList() {
        List<Role> records = new ArrayList<Role>();
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {


            connection = this.getConnection(false);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().ROLE_SELECT,
                    param, type);
            while (rst.next()) {
                Role item = new Role();
                item.setId(rst.getInt("RoleId"));
                item.setRoleName(rst.getString("RoleName"));
                records.add(item);

            }

            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Role>();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public boolean saveBulkUsersList(HttpServletRequest request, List<Users> users,int roleId) {
        Connection connection = null;
        Users record = null;
        boolean returnValue = false;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(false);
            

            for (Users user : users) {
                param = new Vector();
                type = new Vector();
                user.setRoleId(roleId);
                user.setActive("1");
                user.setDateUpdated(new Date());
                user.setDateSignup(new Date());
                user.setLastLoginDate(new Date());
                user.setLastLoginDate(new Date());
                user.setPassword(user.getUserName());
                user.setNumberLogins(0);
                user.setSuperAdmin(0);
                //startTransaction(connection);
                int employeeId = this.getEmployeeDAO().getEmployeeIdByUserName(user.getUserName(),connection);
                param.add(user.getUserName());
                type.add("STRING");
                param.add(user.getPassword());
                type.add("STRING");
                param.add(user.getAdminUser());
                type.add("NUMBER");
                param.add(user.getSuperAdmin());
                type.add("NUMBER");
                param.add(user.getNumberLogins());
                type.add("NUMBER");
                param.add(user.getActive());
                type.add("STRING");
                param.add(user.getDateUpdated());
                type.add("DATE");
                param.add(user.getDateSignup());
                type.add("DATE");
                param.add(user.getLastLoginDate());
                type.add("DATE");
                param.add(user.getRoleId());
                type.add("NUMBER");
                CallableStatement cs = null;
                if (employeeId > 0) {
                    param.add(employeeId);
                    type.add("NUMBER");
                    
                    this.startTransaction(connection);
                    cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_INSERT,
                            param, type);
                } else {
                    this.startTransaction(connection);
                    param.add(user.getFullnames());
                    type.add("String");
                    cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_BULK_USER_INSERT_WITHOUT_EMPL_ID,
                            param, type);
                }
                if (cs == null) {
                    throw new Exception("Unable to execute procedure...");
                }

               createNewAuditTrail(request, "New System User Created: Record[" + user.toString() + "]",AuditOperationCodeConstant.Create_New_System_User,connection);
            }
             this.commitTransaction(connection);
             
             return returnValue=true;
        } catch (Exception e) {
            e.printStackTrace();
             try{ rollbackTransaction(connection); }catch(Exception er){}
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    return returnValue;
    }
    public boolean saveBulkUsersList( List<Users> users,int transactionID,String status,Connection connection) throws  Exception{
     
        
        boolean returnValue = false;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            connection = this.getConnection(false);
            

            for (Users user : users) {
                param = new Vector();
                type = new Vector();
                user.setRoleId(user.getRoleId());
                user.setActive(status);
                user.setDateUpdated(new Date());
                user.setDateSignup(new Date());
                user.setLastLoginDate(new Date());
                user.setLastLoginDate(new Date());
                user.setPassword(user.getUserName());
                user.setNumberLogins(0);
                user.setSuperAdmin(0);
                //startTransaction(connection);
                int employeeId = this.getEmployeeDAO().getEmployeeIdByUserName(user.getUserName(),connection);
                param.add(user.getUserName());
                type.add("STRING");
                param.add(user.getPassword());
                type.add("STRING");
                param.add(user.getAdminUser());
                type.add("NUMBER");
                param.add(user.getSuperAdmin());
                type.add("NUMBER");
                param.add(user.getNumberLogins());
                type.add("NUMBER");
                param.add(user.getActive());
                type.add("STRING");
                param.add(user.getDateUpdated());
                type.add("DATE");
                param.add(user.getDateSignup());
                type.add("DATE");
                param.add(user.getLastLoginDate());
                type.add("DATE");
                param.add(user.getRoleId());
                type.add("NUMBER");
                param.add(transactionID);
                type.add("NUMBER");
                CallableStatement cs = null;
                if (employeeId > 0) {
                    param.add(employeeId);
                    type.add("NUMBER");
                    
                    
                    cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_INSERT,
                            param, type);
                } else {
                    
                    param.add(user.getFullnames());
                    type.add("String");
                    cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_BULK_USER_INSERT_WITHOUT_EMPL_ID,
                            param, type);
                }
                if (cs == null) {
                    throw new Exception("Unable to execute procedure...");
                }

               
            }
            
             
             return returnValue=true;
        } catch (Exception e) {
            e.printStackTrace();
             try{  }catch(Exception er){}
        } 

    return returnValue;
    }
    public String getRoleNameByRoleId(int id) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        String roleName = null;
        try {
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().ROLE_NAME_SELECT_BY_ID,
                    param, type);

            while (rst.next()) {
                
                roleName = (rst.getString("RoleName"));
              

                return roleName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return roleName;
    }
    public String getRoleNameByRoleName(String id) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        String roleName = null;
        try {
            param.add(id);
            type.add("STRING");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().ROLE_NAME_SELECT_BY_ROLE_NAME,
                    param, type);

            while (rst.next()) {
                
                roleName = (rst.getString("RoleName"));
              

                return roleName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return roleName;
    }
    public String getMenuNameByMenuId(int id) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        String menuName = null;
        try {
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().MENU_NAME_SELECT_BY_ID,
                    param, type);

            while (rst.next()) {
                
                menuName = (rst.getString("submenutitle"));
              

                return menuName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return menuName;
    }
public boolean createPasswordHistory(int Id,String Password) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try {
            param.add(Id);
            type.add("NUMBER");
            param.add(Password);
            type.add("STRING");
            CallableStatement cs = null;
            connection = this.getConnection(false);
            this.startTransaction(connection);
                cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_PASSWORD_HISTORY_INSERT,
                        param, type);
           if (cs == null) {
                throw new Exception("Unable to execute procedure...");
            }
            this.commitTransaction(connection);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

//:~
 public List<String> getLast12Password(int id) {
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        List<String> list = new ArrayList<String>();
        
        try {
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            System.out.println("from Password history UserId ==========="+id);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().PREVIOUS_12_PASSWORD_HISTORY_BY_USERID,
                    param, type);

            while (rst.next()) {
             
                String password = rst.getString("Password");
                System.out.println("from Password history ==========="+password);
                list.add(password);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
      /**
     * @return the employeeDAO
     */
    public EmployeeDAO getEmployeeDAO() {
        return employeeDAO;
    }
    
    /**
     * @param employeeDAO the employeeDAO to set
     */
    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }
    
    public boolean insert_User(Users user, Connection connection){

        Vector param =new Vector();
        Vector type = new Vector();
        try{
           param.add(user.getUserName());
            type.add("STRING");
            // for FBN
            param.add(user.getUserName());
            type.add("STRING");
            param.add(user.getAdminUser());
            type.add("NUMBER");
            param.add(user.getSuperAdmin());
            type.add("NUMBER");
            param.add(user.getNumberLogins());
            type.add("NUMBER");
            param.add(user.getActive());
            type.add("STRING");
            param.add(user.getDateUpdated());
            type.add("DATE");
            param.add(user.getDateSignup());
            type.add("DATE");
            param.add(user.getLastLoginDate());
            type.add("DATE");
            param.add(user.getRoleId());
            type.add("NUMBER");
            param.add(user.getTransactionId()); type.add("NUMBER");
 

         CallableStatement cs = null;
            if (user.getEmployeeId() > 0) {
                param.add(user.getEmployeeId());
                type.add("NUMBER");
                this.startTransaction(connection);
                cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_INSERT,
                        param, type);
            } else {
                param.add(user.getFullnames());
                type.add("STRING");
                this.startTransaction(connection);
                
                cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().PROC_USER_INSERT_WITHOUT_EMPL_ID,
                        param, type);
            }
            if (cs == null) {
                throw new Exception("Unable to execute procedure...");
            }
            this.commitTransaction(connection);
            return true;

           
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }//:~
    public Users getUserByTransaction(long id){
        return getUser(id, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_TRANSACTION);
    }
    public List<Users> getBulkUserByTransaction(long id){
        return getBulkUsers(id, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_TRANSACTION);
    }
    public Users getUserByTransaction(Connection connection, long id){
        return getUser(id, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_TRANSACTION, connection);
    }
    public List<Users> getBulkUserByTransaction(Connection connection, long id){
        return getBulkUsers(id, DatabaseQueryLoader.getQueryAssignedConstant().USER_SELECT_BY_TRANSACTION, connection);
    }
private Users getUser(long id, String query, Connection connection){
        Users records = null;

        Vector param =new Vector();
        Vector type = new Vector();
        try{
            //set parameter
            param.add(id); type.add("NUMBER");
            param.add(id); type.add("NUMBER");
            ResultSet rst = this.executeParameterizedQuery(connection, query, param, type);

            if(rst.next()){
                Users user = new Users();
                user.setId(rst.getInt("user_id"));
                user.setEmployeeId(rst.getInt("emp_number"));
                user.setUserName(rst.getString("user_name"));
                user.setPassword(rst.getString("password"));
                user.setAdminUser(rst.getInt("admin_user"));
                user.setSuperAdmin(rst.getInt("superadmin"));
                user.setNumberLogins(rst.getInt("numlogins"));
                user.setDateSignup(rst.getDate("datesignup"));
                user.setLastLoginDate(rst.getDate("lastlogindate"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setTransactionId(rst.getLong("transactionid"));
                user.setActive(rst.getString("active"));
                 user.setDateLogin(rst.getDate("datelogin"));
                user.setFullnames(rst.getString("fullname"));
                user.setRoleId(rst.getInt("RoleId"));
                
                records = user;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return records;
    }
private List<Users> getBulkUsers(long id, String query, Connection connection){
        Users records = null;
        List<Users> list = new ArrayList<Users>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            //set parameter
            param.add(id); type.add("NUMBER");
            param.add(id); type.add("NUMBER");
            ResultSet rst = this.executeParameterizedQuery(connection, query, param, type);

            while (rst.next()) {
                Users user = new Users();
                user.setId(rst.getInt("user_id"));
                user.setEmployeeId(rst.getInt("emp_number"));
                user.setUserName(rst.getString("user_name"));
                user.setPassword(rst.getString("password"));
                user.setAdminUser(rst.getInt("admin_user"));
                user.setSuperAdmin(rst.getInt("superadmin"));
                user.setNumberLogins(rst.getInt("numlogins"));
                user.setDateSignup(rst.getDate("datesignup"));
                user.setLastLoginDate(rst.getDate("lastlogindate"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setTransactionId(rst.getLong("transactionid"));
                user.setActive(rst.getString("active"));
                 user.setDateLogin(rst.getDate("datelogin"));
                user.setFullnames(rst.getString("fullname"));
                user.setRoleId(rst.getInt("RoleId"));
                
                list.add(user);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }
private Users getUser(long id, String query){
        Connection connection = null;
        Users records = null;
        
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            //set parameter
            param.add(id); type.add("NUMBER");
            param.add(id); type.add("NUMBER");
            connection = this.getConnection(true);
            System.out.println("Query: " + query);
            ResultSet rst = this.executeParameterizedQuery(connection, query, param, type);
            
            if(rst.next()){
                Users user = new Users();
                user.setId(rst.getInt("user_id"));
                user.setEmployeeId(rst.getInt("emp_number"));
                user.setUserName(rst.getString("user_name"));
                user.setPassword(rst.getString("password"));
                user.setAdminUser(rst.getInt("admin_user"));
                user.setSuperAdmin(rst.getInt("superadmin"));
                user.setNumberLogins(rst.getInt("numlogins"));
                user.setDateSignup(rst.getDate("datesignup"));
                user.setLastLoginDate(rst.getDate("lastlogindate"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setTransactionId(rst.getLong("transactionid"));
                user.setActive(rst.getString("active"));
                 user.setDateLogin(rst.getDate("datelogin"));
                 user.setFullnames(rst.getString("fullname"));
                 user.setRoleId(rst.getInt("RoleId"));
                records = user;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }
private List<Users> getBulkUsers(long id, String query){
        Connection connection = null;
        Users records = null;
        
        Vector param =new Vector();
        Vector type = new Vector();
        List<Users> list = new ArrayList<Users>();
        try{
            //set parameter
            param.add(id); type.add("NUMBER");
            param.add(id); type.add("NUMBER");
            connection = this.getConnection(true);
            System.out.println("Query: " + query);
            ResultSet rst = this.executeParameterizedQuery(connection, query, param, type);
            int count =1;
            while (rst.next()) {
                Users user = new Users();
                user.setId(rst.getInt("user_id"));
                user.setEmployeeId(rst.getInt("emp_number"));
                user.setUserName(rst.getString("user_name"));
                user.setPassword(rst.getString("password"));
                user.setAdminUser(rst.getInt("admin_user"));
                user.setSuperAdmin(rst.getInt("superadmin"));
                user.setNumberLogins(rst.getInt("numlogins"));
                user.setDateSignup(rst.getDate("datesignup"));
                user.setLastLoginDate(rst.getDate("lastlogindate"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setDateUpdated(rst.getDate("dateupdated"));
                user.setTransactionId(rst.getLong("transactionid"));
                user.setActive(rst.getString("active"));
                 user.setDateLogin(rst.getDate("datelogin"));
                 user.setFullnames(rst.getString("fullname"));
                 user.setRoleId(rst.getInt("RoleId"));
                 user.setRoleName(rst.getString("RoleName"));
                 user.setSerialNumber(count);
                 list.add(user);
                count=count+1;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
}
