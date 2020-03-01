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
import java.util.Date;
import org.tenece.web.data.AuditTrail;

/**
 *
 * @author jeffry.amachree
 */
public class AuditTrailDAO extends BaseDAO{
    
    /**
     * Creates a new instance of DepartmentDAO
     */
    public AuditTrailDAO() {
    }
    
    /* ************* AuditTrail ********** */
    public List<AuditTrail> getAllAuditTrails(){
        Connection connection = null;
        List<AuditTrail> records = new ArrayList<AuditTrail>();
        try{
            connection = this.getConnection(true);
            ResultSet rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_TRAIL_SELECT, connection);
            
            while(rst.next()){
                AuditTrail audit = new AuditTrail();
                audit.setEmployeeId(rst.getLong("emp_number"));
                audit.setAccessDate(new Date(rst.getDate("access_date").getTime()));
                audit.setOperation(rst.getString("operation"));
                audit.setMachineIdentity(rst.getString("machine_id"));
                records.add(audit);
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
    
    public List<AuditTrail> getAuditTrailByEmployee(long employeeId){
        Connection connection = null;
        List<AuditTrail> records = new ArrayList<AuditTrail>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(employeeId);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_TRAIL_SELECT_BY_EMP,
                    param, type);
            
            while(rst.next()){
                AuditTrail audit = new AuditTrail();
                audit.setEmployeeId(rst.getLong("emp_number"));
                audit.setAccessDate(new Date(rst.getDate("access_date").getTime()));
                audit.setOperation(rst.getString("operation"));
                audit.setMachineIdentity(rst.getString("machine_id"));
                records.add(audit);
            }
        }catch(Exception e){
            
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }

    public List<AuditTrail> getAuditTrailByDate(Date startDate, Date endDate){
        Connection connection = null;
        List<AuditTrail> records = new ArrayList<AuditTrail>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(startDate); type.add("DATE");
            param.add(endDate); type.add("DATE");

            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_TRAIL_SELECT_BY_DATE,
                    param, type);

            while(rst.next()){
                AuditTrail audit = new AuditTrail();
                audit.setEmployeeId(rst.getLong("emp_number"));
                audit.setAccessDate(new Date(rst.getDate("access_date").getTime()));
                audit.setOperation(rst.getString("operation"));
                audit.setMachineIdentity(rst.getString("machine_id"));
                records.add(audit);
            }
        }catch(Exception e){

        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }

    public int createNewAuditTrail(AuditTrail audit){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(audit.getEmployeeId()); type.add("NUMBER");
            param.add(audit.getAccessDate()); type.add("DATE");
            param.add(audit.getOperation()); type.add("STRING");
            param.add(audit.getMachineIdentity()); type.add("STRING");
            param.add(audit.getOperationCode()); type.add("STRING");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_TRAIL_INSERT,
                    param, type);
            
            return i;
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("Error creating audit: " + e.getMessage());
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                //ex.printStackTrace();
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }
    }//:~
    public int createNewAuditTrail(AuditTrail audit,Connection connection){
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(audit.getEmployeeId()); type.add("NUMBER");
            param.add(audit.getAccessDate()); type.add("DATE");
            param.add(audit.getOperation()); type.add("STRING");
            param.add(audit.getMachineIdentity()); type.add("STRING");
            param.add(audit.getOperationCode()); type.add("STRING");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().AUDIT_TRAIL_INSERT,
                    param, type);
            
            return i;
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("Error creating audit: " + e.getMessage());
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                //ex.printStackTrace();
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }
    }//:~
    
}
