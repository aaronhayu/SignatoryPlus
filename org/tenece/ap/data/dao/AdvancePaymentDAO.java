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
import org.tenece.web.data.AdvanceCollection;

/**
 *
 * @author jeffry.amachree
 */
public class AdvancePaymentDAO extends BaseDAO{
    
    /**
     * Creates a new instance of DepartmentDAO
     */
    public AdvancePaymentDAO() {
    }
    
    /* ************* Holidays ********** */
    public List<AdvanceCollection> getAllAdvancePayment(long employeeId){
        return getAllAdvancePayment(employeeId, null, null);
    }


    public List<AdvanceCollection> getAllAdvancePayment(long employeeId, String criteria, String searchText){
        Connection connection = null;
        List<AdvanceCollection> records = new ArrayList<AdvanceCollection>();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(employeeId); type.add("NUMBER");
            
            connection = this.getConnection(true);
            ResultSet rst = null;
            if(criteria == null || searchText == null){
                rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_SELECT_BY_ACTIVE_FOR_EMPLOYEE,
                        param, type);
            }else{
                String query = "";

                if(criteria.equalsIgnoreCase("DESC")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_SELECT_BY_ACTIVE_AND_PURPOSE_FOR_EMPLOYEE;
                }
                
                query = query.replaceAll("_SEARCH_", searchText);

                rst = this.executeQuery(query, connection);
            }
            
            while(rst.next()){
                //id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active
                AdvanceCollection item = new AdvanceCollection();
                item.setId(rst.getLong("id"));
                item.setAdvanceTo(rst.getString("advance_to"));
                item.setApplicationDate(new java.util.Date(rst.getDate("application_date").getTime()));
                item.setFormNumber(rst.getString("form_number"));
                item.setEmployeeId(rst.getLong("emp_number"));
                item.setPurpose(rst.getString("purpose"));
                item.setAmount(rst.getFloat("amount"));
                item.setCurrentAdvance_Temp_Amount(rst.getFloat("current_advance_temp_amount"));
                item.setCurrentAdvance_Permanent_Amount(rst.getFloat("current_advance_perm_amount"));
                
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

    public AdvanceCollection getAdvancePaymentById(int id){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_SELECT_BY_ID,
                    param, type);

            if(rst.next()){
                AdvanceCollection item = new AdvanceCollection();
                item.setId(rst.getLong("id"));
                item.setAdvanceTo(rst.getString("advance_to"));
                item.setApplicationDate(new java.util.Date(rst.getDate("application_date").getTime()));
                item.setFormNumber(rst.getString("form_number"));
                item.setEmployeeId(rst.getLong("emp_number"));
                item.setPurpose(rst.getString("purpose"));
                item.setAmount(rst.getFloat("amount"));
                item.setCurrentAdvance_Temp_Amount(rst.getFloat("current_advance_temp_amount"));
                item.setCurrentAdvance_Permanent_Amount(rst.getFloat("current_advance_perm_amount"));

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

    public AdvanceCollection getAdvancePaymentClosedById(long id){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_CLOSE_SELECT,
                    param, type);

            if(rst.next()){
                AdvanceCollection item = new AdvanceCollection();
                item.setCloseId(rst.getLong("id"));
                item.setId(rst.getLong("advance_id"));
                item.setFormNumber(rst.getString("form_number"));
                item.setApplicationDate(new java.util.Date(rst.getDate("close_date").getTime()));
                item.setTotalExpenses(rst.getFloat("total_expenses"));
                item.setLessExpenses(rst.getFloat("less_expenses"));

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

    public Long checkNumberOfAdvanceForEmployee(long id){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_CHECK_PENDING,
                    param, type);

            if(rst.next()){
                return rst.getLong(1);
            }else{
                return 0L;
            }

        }catch(Exception e){
            return null;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int createNewAdvancePayment(AdvanceCollection item){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            //id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active
            param.add(item.getAdvanceTo()); type.add("STRING");
            param.add(item.getApplicationDate()); type.add("DATE");
            param.add(item.getFormNumber()); type.add("STRING");
            param.add(item.getEmployeeId()); type.add("NUMBER");
            param.add(item.getPurpose()); type.add("STRING");
            param.add(item.getAmount()); type.add("AMOUNT");
            param.add(item.getCurrentAdvance_Temp_Amount()); type.add("AMOUNT");
            param.add(item.getCurrentAdvance_Permanent_Amount()); type.add("AMOUNT");
            param.add("A"); type.add("STRING");

            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_INSERT,
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

    public int createNewClosedAdvancePayment(AdvanceCollection item){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(false);
            
            startTransaction(connection);

            param.add(item.getId()); type.add("NUMBER");
            param.add(item.getFormNumber()); type.add("STRING");
            param.add(item.getApplicationDate()); type.add("DATE");
            param.add(item.getTotalExpenses()); type.add("AMOUNT");
            param.add(item.getLessExpenses()); type.add("AMOUNT");
            //param.add("A"); type.add("STRING");

            
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_CLOSE_INSERT,
                    param, type);

            param =new Vector();
            type = new Vector();
            param.add(item.getId()); type.add("NUMBER");

            int x = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_CLOSE,
                    param, type);
            commitTransaction(connection);
            return i;
        }catch(Exception e){
            e.printStackTrace();
            try{rollbackTransaction(connection);}catch(Exception er){}
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//:~

    public int updateAdvancePayment(AdvanceCollection item){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(item.getAdvanceTo()); type.add("STRING");
            param.add(item.getApplicationDate()); type.add("DATE");
            param.add(item.getFormNumber()); type.add("STRING");
            param.add(item.getEmployeeId()); type.add("NUMBER");
            param.add(item.getPurpose()); type.add("STRING");
            param.add(item.getAmount()); type.add("AMOUNT");
            param.add(item.getCurrentAdvance_Temp_Amount()); type.add("AMOUNT");
            param.add(item.getCurrentAdvance_Permanent_Amount()); type.add("AMOUNT");
            param.add("A"); type.add("STRING");

            param.add(item.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_UPDATE,
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
    
    public int deleteAdvancePayment(AdvanceCollection item){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            
            param.add(item.getId()); type.add("NUMBER");
            
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_DELETE,
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
    
    public int deleteAdvancePayment(List<Integer> ids){
        Connection connection = null;
        AdvanceCollection record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            
            for(int id : ids){
                connection = this.getConnection(false);
                
                this.startTransaction(connection);
                param =new Vector();
                type = new Vector();
                param.add(id); type.add("NUMBER");

                int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().ADVANCE_PAYMENT_DELETE,
                        param, type);
                
                this.commitTransaction(connection);
            }
            return ids.size();
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
