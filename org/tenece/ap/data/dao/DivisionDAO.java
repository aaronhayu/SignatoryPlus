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
import org.tenece.web.data.Division;
import org.tenece.web.data.Division;

/**
 *
 * @author jeffry.amachree
 */
public class DivisionDAO extends BaseDAO{
    
    /**
     * Creates a new instance of DepartmentDAO
     */
    public DivisionDAO() {
    }
    
    /* ************* Divisions ********** */
    public List<Division> getAllDivisions(){
        return getAllDivisions(null, null);
    }
    public List<Division> getAllDivisions(String criteria, String searchText){
        Connection connection = null;
        List<Division> records = new ArrayList<Division>();
        try{
            connection = this.getConnection(true);
            ResultSet rst = null;
            if(criteria == null || searchText == null){
                rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_SELECT, connection);
            }else{
                String query = "";

                if(criteria.equalsIgnoreCase("CODE")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_SELECT_SEARCH_BY_CODE;
                }else if(criteria.equalsIgnoreCase("NAME")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_SELECT_SEARCH_BY_NAME;
                }else if(criteria.equalsIgnoreCase("DESC")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_SELECT_SEARCH_BY_DESC;
                }

                query = query.replaceAll("_SEARCH_", searchText);

                rst = this.executeQuery(query, connection);
            }

            while(rst.next()){
                Division item = new Division();
               
                item.setId(rst.getInt("division_id"));
                item.setCode(rst.getString("division_code"));
                item.setName(rst.getString("division_name"));
                item.setDescription(rst.getString("division_description"));
             
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
public String findAllDivisionDescriptionBySolID(String solId){
        Connection connection = null;
        String SolID = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            
            param.add(solId);
            type.add("STRING");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_SELECT_BY_CODE,
                    param, type);
            
            while(rst.next()){
                
                 SolID = rst.getString("division_name");
                
            }
            return SolID;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return SolID;
    }

    public Division getDivisionById(int id){
        Connection connection = null;
        Division record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_SELECT_BY_ID,
                    param, type);
            
            while(rst.next()){
                Division item = new Division();
                item.setId(rst.getInt("division_id"));
                item.setCode(rst.getString("division_code"));
                item.setName(rst.getString("division_name"));
                item.setDescription(rst.getString("division_description"));
                
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
    
    public int createNewDivision(Division Division){
        Connection connection = null;
        Division record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(Division.getCode()); type.add("STRING");
            param.add(Division.getName()); type.add("STRING");
            param.add(Division.getDescription()); type.add("STRING");
            
            System.out.println("QUERY:" + DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_INSERT);
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_INSERT,
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
    
    public int updateDivision(Division Division){
        Connection connection = null;
        Department record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(Division.getCode()); type.add("STRING");
            param.add(Division.getName()); type.add("STRING");
            param.add(Division.getDescription()); type.add("STRING");
            param.add(Division.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_UPDATE,
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
    
    public int deleteDivision(Division Division){
        Connection connection = null;
        Division record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            
            param.add(Division.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().DIVISION_DELETE,
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
