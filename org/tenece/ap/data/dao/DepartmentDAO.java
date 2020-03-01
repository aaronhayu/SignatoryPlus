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
import org.tenece.web.data.Department;

/**
 *
 * @author jeffry.amachree
 */
public class DepartmentDAO extends BaseDAO{
    
    /**
     * Creates a new instance of DepartmentDAO
     */
    public DepartmentDAO() {
    }
    
    /* ************* Department ********** */
    public List<Department> getAllDepartments(){
        return getAllDepartments(null, null);
    }
    public List<Department> getAllDepartments(String criteria, String searchText){
        Connection connection = null;
        List<Department> records = new ArrayList<Department>();
        try{
            connection = this.getConnection(true);
            ResultSet rst = null;
            if(criteria == null || searchText == null){
                rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_SELECT, connection);
            }else{
                String query = "";

                if(criteria.equalsIgnoreCase("NAME")){
                    
                    query = DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_SELECT_SEARCH_BY_NAME;
                }else if(criteria.equalsIgnoreCase("LOCATION")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_SELECT_SEARCH_BY_LOCATION;
                }
                 
                query = query.replaceAll("_SEARCH_", searchText);
                System.out.println("One search Query...:" + query);
                rst = this.executeQuery(query, connection);
            }
            
            while(rst.next()){
                Department dept = new Department();
                dept.setId(rst.getInt("deptid"));
                dept.setManagerId(rst.getInt("managerid"));
                dept.setDepartmentName(rst.getString("deptname"));
                dept.setLocation(rst.getString("location"));
                dept.setShortDescription(rst.getString("deptdesc"));
                dept.setWorkDescription(rst.getString("workdesc"));
                dept.setParentId(rst.getInt("parent_id"));
                dept.setCompanyCode(rst.getString("company_code"));
                
                records.add(dept);
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

    public List<Department> getAllDepartmentsByCompany(String code){
        return getAllDepartmentsByCompany(code, null, null);
    }
    public List<Department> getAllDepartmentsByCompany(String code, String criteria, String searchText){
        Connection connection = null;
        List<Department> records = new ArrayList<Department>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(code); type.add("STRING");

            connection = this.getConnection(true);
            ResultSet rst = null;
            if(criteria == null || searchText == null){
                rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_SELECT_BY_COMPANY, param, type);
            }else{
                String query = "";

                if(criteria.equalsIgnoreCase("NAME")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_SELECT_BY_COMPANY_SEARCH_BY_NAME;
                }else if(criteria.equalsIgnoreCase("LOCATION")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_SELECT_BY_COMPANY_SEARCH_BY_LOCATION;
                }

                query = query.replaceAll("_SEARCH_", searchText);

                rst = this.executeParameterizedQuery(connection, query, param, type);
            }

            while(rst.next()){
                Department dept = new Department();
                dept.setId(rst.getInt("deptid"));
                dept.setManagerId(rst.getInt("managerid"));
                dept.setDepartmentName(rst.getString("deptname"));
                dept.setLocation(rst.getString("location"));
                dept.setShortDescription(rst.getString("deptdesc"));
                dept.setWorkDescription(rst.getString("workdesc"));
                dept.setParentId(rst.getInt("parent_id"));
                dept.setCompanyCode(rst.getString("company_code"));

                records.add(dept);
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

    public Department getDepartmentById(int id){
        Connection connection = null;
        Department record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_SELECT_BY_ID,
                    param, type);
            
            while(rst.next()){
                Department dept = new Department();
                dept.setId(rst.getInt("deptid"));
                dept.setManagerId(rst.getInt("managerid"));
                dept.setDepartmentName(rst.getString("deptname"));
                dept.setLocation(rst.getString("location"));
                dept.setShortDescription(rst.getString("deptdesc"));
                dept.setWorkDescription(rst.getString("workdesc"));
                dept.setParentId(rst.getInt("parent_id"));
                dept.setCompanyCode(rst.getString("company_code"));
                
                record = dept;
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
    
    public int createNewDepartment(Department dept){
        Connection connection = null;
        Department record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(dept.getManagerId()); type.add("NUMBER");
            param.add(dept.getDepartmentName()); type.add("STRING");
            param.add(dept.getLocation()); type.add("STRING");
            param.add(dept.getShortDescription()); type.add("STRING");
            param.add(dept.getWorkDescription()); type.add("STRING");
            if(dept.getParentId() > 0){
                param.add(dept.getParentId()); type.add("NUMBER");
            }else{
                param.add(0); type.add("NUMBER_NULL");
            }

            param.add(dept.getCompanyCode()); type.add("STRING");

            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_INSERT,
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
    
    public int updateDepartment(Department dept){
        Connection connection = null;
        Department record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(dept.getManagerId()); type.add("NUMBER");
            param.add(dept.getDepartmentName()); type.add("STRING");
            param.add(dept.getLocation()); type.add("STRING");
            param.add(dept.getShortDescription()); type.add("STRING");
            param.add(dept.getWorkDescription()); type.add("STRING");
            //param.add(dept.getParentId()); type.add("NUMBER");
            if(dept.getParentId() > 0){
                param.add(dept.getParentId()); type.add("NUMBER");
            }else{
                param.add(0); type.add("NUMBER_NULL");
            }
            param.add(dept.getCompanyCode()); type.add("STRING");
            
            param.add(dept.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_UPDATE,
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
    
    public int deleteDepartment(Department dept){
        Connection connection = null;
        Department record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(dept.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_DELETE,
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
    
    public int deleteDepartments(List<Integer> depts){
        Connection connection = null;
        Department record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            
            
            connection = this.getConnection(false);
            this.startTransaction(connection);
            for(int dept : depts){
                param.add(dept); type.add("NUMBER");
                int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().DEPARTMENT_DELETE,
                        param, type);
            }
            this.commitTransaction(connection);
            return 1;
        }catch(Exception e){
            try {
                this.rollbackTransaction(connection);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
