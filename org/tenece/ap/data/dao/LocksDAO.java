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

import org.tenece.ap.dao.db.DatabaseQueryLoader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.tenece.web.data.Lock;

/**
 *
 * @author jeffry.amachree
 */
public class LocksDAO extends BaseDAO{
    
    /**
     * Creates a new instance of DepartmentDAO
     */
    public LocksDAO() {
    }
    
    /* ************* Locks ********** */
    public List<Lock> getAllLocks(){
        Connection connection = null;
        List<Lock> records = new ArrayList<Lock>();
        try{
            connection = this.getConnection(true);
            ResultSet rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().LOCKS_SELECT, connection);
            
            while(rst.next()){
                Lock item = new Lock();
                item.setId(rst.getInt("lockid"));
                item.setLockedBy(rst.getString("emp_number"));
                item.setDateLocked(rst.getDate("datelock"));
                item.setReason(rst.getString("reasonlock"));
                item.setLockedBy(rst.getString("lockedby"));
                item.setActive(rst.getString("active"));
                
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
    
    public Lock getLockById(int id){
        Connection connection = null;
        Lock record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().LOCKS_SELECT_BY_ID,
                    param, type);
            
            while(rst.next()){
                Lock item = new Lock();
                item.setId(rst.getInt("lockid"));
                item.setUserId(rst.getLong("user_id"));
                item.setDateLocked(rst.getDate("datelock"));
                item.setReason(rst.getString("reasonlock"));
                item.setLockedBy(rst.getString("lockedby"));
                item.setActive(rst.getString("active"));
                
                record = item;
            }
        }catch(Exception e){
            
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return record;
    }
    
    public int createNewLock(Lock Lock){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(Lock.getUserId()); type.add("NUMBER");
            param.add(Lock.getDateLocked()); type.add("DATE");
            param.add(Lock.getReason()); type.add("STRING");
            param.add(Lock.getLockedBy()); type.add("STRING");
            param.add(Lock.getActive()); type.add("STRING");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().LOCKS_INSERT,
                    param, type);
            
            return i;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~
    
    public int updateLock(Lock Lock){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(Lock.getUserId()); type.add("NUMBER");
            param.add(Lock.getDateLocked()); type.add("DATE");
            param.add(Lock.getReason()); type.add("STRING");
            param.add(Lock.getLockedBy()); type.add("STRING");
            param.add(Lock.getActive()); type.add("STRING");
            param.add(Lock.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().LOCKS_INSERT,
                    param, type);
            
            return i;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~
    
    public int deleteLock(Lock Lock){
        Connection connection = null;
        Lock record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            
            param.add(Lock.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().LOCKS_DELETE,
                    param, type);
            
            return i;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~
}
