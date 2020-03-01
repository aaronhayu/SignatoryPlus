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


import java.sql.CallableStatement;
import org.tenece.ap.dao.db.DatabaseQueryLoader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.tenece.ap.exception.UnknownException;
import org.tenece.web.data.Employee;
import java.nio.charset.Charset;
import org.tenece.web.common.FileWriterProcesor;
import sun.nio.cs.StandardCharsets;


/**
 *
 * @author jeffry.amachree
 */
public class EmployeeDAO extends BaseDAO {
        
    
    
    /** Creates a new instance of EmployeeDAO */
    public EmployeeDAO() {
    }

    public List<Employee> getAllEmployeeBasic(){
        return getAllEmployeeBasic(null, null);
    }
    public List<Employee> getAllEmployeeBasic(String criteria, String searchText){
        Connection connection = null;
        List<Employee> records = new ArrayList<Employee>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;
            if(criteria == null || searchText == null){
                System.out.println(DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC);
                rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC,
                        param, type);
            }else{
                String query = "";
                //param.add(searchText); type.add("STRING");
                
                if(criteria.equalsIgnoreCase("FN")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_FNAME;
                }else if(criteria.equalsIgnoreCase("LN")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_LNAME;
                }else if(criteria.equalsIgnoreCase("ID")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_ID;
                }
                query = query.replaceAll("_SEARCH_", searchText);

                System.out.println("One search Query...:" + query);
                
                rst = this.executeParameterizedQuery(connection, query,
                        param, type);
            }
            
            while(rst.next()){
                Employee item = new Employee();
                item.setStaffId(rst.getString("staffid"));
                item.setEmployeeNumber(rst.getLong("emp_number"));
                item.setDepartmentId(rst.getInt("deptid"));
                item.setJobTitleId(rst.getInt("jobtitleid"));
                
                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));//,
                item.setOtherNames(rst.getString("othernames"));
                
                item.setActive(rst.getString("active"));

                item.setEmail(rst.getString("email"));
                item.setDivisionId(rst.getInt("divisionid"));
                item.setCellPhone(rst.getString("cellphone"));
                item.setExtension(rst.getInt("extension"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
                item.setFullName(item.getFirstName()+ " " + item.getLastName());
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
    
        public Employee getEmployeeSignature(String criteria, String searchText){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        Employee item = new Employee();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;
           
                
                //param.add(searchText); type.add("STRING");
                
                
               String   query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_SIGNATURE_SEARCH_ID;
                
                query = query.replaceAll("_SEARCH_", searchText);

                System.out.println("One search Query...:" + query);
                
                rst = this.executeParameterizedQuery(connection, query,
                        param, type);
            
            
            while(rst.next()){
                
                item.setStaffId(rst.getString("staffid"));
                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));//,
                item.setOtherNames(rst.getString("othernames"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
                item.setCategory(rst.getString("category"));
                item.setFormattedtxnLimt(String.format("%,.2f", rst.getFloat("Transaction_Limit")));
                
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
        return item;
    }
    public List<Employee> getAllEmployeeWithoutSignature(){
        Connection connection = null;
        List<Employee> records = new ArrayList<Employee>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;
            rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_WITHOUT_SIGNATURE,
                        param, type);

            while(rst.next()){
                Employee item = new Employee();
                item.setStaffId(rst.getString("staffid"));
                item.setEmployeeNumber(rst.getLong("emp_number"));
                item.setDepartmentId(rst.getInt("deptid"));
                item.setJobTitleId(rst.getInt("jobtitleid"));

                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));//,
                item.setOtherNames(rst.getString("othernames"));

                item.setActive(rst.getString("active"));

                item.setEmail(rst.getString("email"));
                item.setDivisionId(rst.getInt("divisionid"));
                item.setCellPhone(rst.getString("cellphone"));
                item.setExtension(rst.getInt("extension"));
                item.setSignature(null);//rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
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

    public List<Employee> getAllEmployeeWithoutSignatureNumber(){
        Connection connection = null;
        List<Employee> records = new ArrayList<Employee>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;
            rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_WITHOUT_SIGNATURE_NUMBER,
                        param, type);

            while(rst.next()){
                Employee item = new Employee();
                item.setStaffId(rst.getString("staffid"));
                item.setEmployeeNumber(rst.getLong("emp_number"));
                item.setDepartmentId(rst.getInt("deptid"));
                item.setJobTitleId(rst.getInt("jobtitleid"));

                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));//,
                item.setOtherNames(rst.getString("othernames"));

                item.setActive(rst.getString("active"));

                item.setEmail(rst.getString("email"));
                item.setDivisionId(rst.getInt("divisionid"));
                item.setCellPhone(rst.getString("cellphone"));
                item.setExtension(rst.getInt("extension"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
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

    public List<Employee> getAllEmployeeBySearch(String staffId, String fullName, String deptId, String division){
        Connection connection = null;
        List<Employee> records = new ArrayList<Employee>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;


            String query = "";
            //param.add(searchText); type.add("STRING");
            //'%_SEARCH_NAME_%' and staffid like '%_SEARCH_ID_%' and deptid between '_SEARCH_DEPT_FROM_' and '_SEARCH_DEPT_TO_' and divisionid between '_SEARCH_DIV_FROM_' and '_SEARCH_DIV_TO_'  ";
            query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH;

            if(staffId == null) staffId = "";
            if(fullName == null) fullName = "";
            if(deptId == null) deptId = "";
            if(division == null) division = "";

            query = query.replaceAll("_SEARCH_NAME_", fullName);
            query = query.replaceAll("_SEARCH_ID_", staffId);
            if(!deptId.trim().equals("")){
                query = query.replaceAll("_SEARCH_DEPT_FROM_", deptId);
                query = query.replaceAll("_SEARCH_DEPT_TO_", deptId);
            }
            if(!division.trim().equals("")){
                query = query.replaceAll("_SEARCH_DIV_FROM_", division);
                query = query.replaceAll("_SEARCH_DIV_TO_", division);
            }else{
                query = query.replaceAll("_SEARCH_DIV_FROM_", "");
                query = query.replaceAll("_SEARCH_DIV_TO_", "99");
            }
            System.out.println("multi search Query...:" + query);

            rst = this.executeParameterizedQuery(connection, query,
                    param, type);

            while(rst.next()){
                Employee item = new Employee();
                item.setStaffId(rst.getString("staffid"));
                item.setEmployeeNumber(rst.getLong("emp_number"));
                item.setDepartmentId(rst.getInt("deptid"));
                item.setJobTitleId(rst.getInt("jobtitleid"));

                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));//,
                item.setOtherNames(rst.getString("othernames"));

                item.setActive(rst.getString("active"));

                item.setEmail(rst.getString("email"));
                item.setDivisionId(rst.getInt("divisionid"));
                item.setCellPhone(rst.getString("cellphone"));
                item.setExtension(rst.getInt("extension"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
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

    public Employee getEmployeeBasicDataById(long id){
        
        
         Employee item = new Employee();
         Connection connection = null;
        try{
           Vector param =new Vector();
           Vector type = new Vector();

            param.add(id);
            type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_BASIC,
                    param, type);
            
            while(rst.next()){
                
                item.setEmployeeNumber(id);

                item.setStaffId(rst.getString("staffid"));
                item.setDepartmentId(rst.getInt("deptid"));
                item.setJobTitleId(rst.getInt("jobtitleid"));
                
                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));
                item.setOtherNames(rst.getString("othernames"));
                
                item.setActive(rst.getString("active"));
                
                item.setEmail(rst.getString("email"));
                item.setDivisionId(rst.getInt("divisionid"));
                //item.setSolId(String.valueOf(rst.getInt("divisionid")));
                item.setCellPhone(rst.getString("cellphone"));
                item.setExtension(rst.getInt("extension"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
                item.setCategory(rst.getString("category"));
                item.setSalaryGradeId(rst.getInt("gradeid"));
                item.setFullName(item.getFirstName()+ " "+ item.getLastName());
                item.setBranchDesc(rst.getString("branchDesc")); 
            }
        }catch(Exception e)
        {
           
             e.printStackTrace();
            
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return item;
    }
public String getUnAuthorisedSignatoryUserByUserName(String  userName){
        
        
         String fullName  = null;
         Connection connection = null;
        try{
           Vector param =new Vector();
           Vector type = new Vector();

            param.add(userName);
            type.add("STRING");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().UNAUTHORISED_SIGNATORY_EMPLOYEE_NAME_SELECT,
                    param, type);
            
            while(rst.next()){
                fullName = rst.getString("Employee_Name"); 
            }
        }catch(Exception e)
        {
           
            
            
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return  fullName ;
    }
    public Employee getEmployeeBasicDataByStaffId(String staffId){
        Connection connection = null;
        Employee record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(staffId);
            type.add("STRING");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_BASIC_BY_STAFFID,
                    param, type);

            while(rst.next()){
                Employee item = new Employee();
                
                item.setStaffId(rst.getString("staffid"));
                item.setEmployeeNumber(Long.parseLong(rst.getString("emp_number")));
                item.setDepartmentId(rst.getInt("deptid"));
                item.setJobTitleId(rst.getInt("jobtitleid"));

                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));
                item.setOtherNames(rst.getString("othernames"));

                item.setActive(rst.getString("active"));

                item.setEmail(rst.getString("email"));
                item.setDivisionId(rst.getInt("divisionid"));

                item.setCellPhone(rst.getString("cellphone"));
                item.setExtension(rst.getInt("extension"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));

                record = item;
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
        return record;
    }

    public int createBulkEmployeeNameList(List<Employee> employees){
        Connection connection = null;
        Employee record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(false);
            startTransaction(connection);

            for(Employee employee : employees){
                param =new Vector();
                type = new Vector();

                String fullName = employee.getFullNames().trim();
                fullName = fullName.replaceAll(" ", "-");
                System.out.println("*****:" + fullName);
                String names[] = fullName.split("-");
                employee.setLastName(names[0]);
                employee.setFirstName(names[1]);
                if(names.length == 3){
                    employee.setOtherNames(names[2]);
                }else{
                    employee.setOtherNames("");
                }
                //set default dept
                employee.setCellPhone("000000000");
                employee.setEmail("info@fbnnigeria.com");
                employee.setExtension(0);
                //append zero to the staff id
                param.add(employee.getStaffId()); type.add("STRING");
                param.add(employee.getDepartmentId()); type.add("NUMBER_NULL");
                param.add(employee.getLastName()); type.add("STRING");
                param.add(employee.getFirstName()); type.add("STRING");
                param.add(employee.getOtherNames()); type.add("STRING");
                
                param.add(employee.getCellPhone()); type.add("STRING");
                param.add(employee.getExtension()); type.add("NUMBER");
                param.add(employee.getEmail()); type.add("STRING");
                param.add(employee.getDivisionId()); type.add("NUMBER_NULL");

                param.add(employee.getSignature()); type.add("BYTES_NULL");
                param.add(employee.getSignatureName()); type.add("VARCHAR_NULL");
                param.add(employee.getSignatureNumber()); type.add("STRING");
                param.add(employee.getCategory()); type.add("STRING");
                
                int i = this.executeParameterizedUpdate(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_INSERT_BASIC_WITH_SIGN,
                        param, type);

                if(i == 0){
                    throw new UnknownException("Unable to save employee record");
                }
            }
            commitTransaction(connection);
            return employees.size();
        }catch(Exception e){
            e.printStackTrace();
            try{ rollbackTransaction(connection); }catch(Exception er){}
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public int createEmployee_WithBasic(Employee employee)throws Exception{
        Connection connection = null;
        Employee record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        
        try{
            param.add(employee.getStaffId()); type.add("STRING");
            param.add(employee.getDepartmentId()); type.add("NUMBER");
            param.add(employee.getLastName()); type.add("STRING");
            param.add(employee.getFirstName()); type.add("STRING");
            param.add(employee.getOtherNames()); type.add("STRING");
            //param.add(employee.getGender()); type.add("STRING");
            //param.add(employee.getMaritalStatus()); type.add("STRING");
            param.add(employee.getCellPhone()); type.add("STRING");
            param.add(employee.getExtension()); type.add("NUMBER");
            param.add(employee.getEmail()); type.add("STRING");
            param.add(employee.getDivisionId()); type.add("NUMBER");

            if(employee.getSignature() == null){
                param.add(employee.getSignature()); type.add("BYTES_NULL");
                param.add(employee.getSignatureName()); type.add("VARCHAR_NULL");
            }else{
                param.add(employee.getSignature()); type.add("BYTES");
                param.add(employee.getSignatureName()); type.add("STRING");
            }
            param.add(employee.getSignatureNumber()); type.add("STRING");
            param.add(employee.getCategory()); type.add("STRING");
            param.add(employee.getJobTitleId()); type.add("NUMBER");
            param.add(employee.getSalaryGradeId()); type.add("NUMBER");
            param.add(employee.getBranchDesc()); type.add("STRING");
            connection = this.getConnection(false);
            int i = this.executeParameterizedUpdate(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_INSERT_BASIC,
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
    
    public int updateEmployeeBasic(Employee employee) throws Exception{
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(employee.getStaffId()); type.add("STRING");
            param.add(employee.getDepartmentId()); type.add("NUMBER");
            param.add(employee.getLastName()); type.add("STRING");
            param.add(employee.getFirstName()); type.add("STRING");
            param.add(employee.getOtherNames()); type.add("STRING");
            //param.add(employee.getGender()); type.add("STRING");
            //param.add(employee.getMaritalStatus()); type.add("STRING");
            param.add(employee.getCellPhone()); type.add("STRING");
            param.add(employee.getExtension()); type.add("NUMBER");
            param.add(employee.getEmail()); type.add("STRING");
            param.add(employee.getDivisionId()); type.add("NUMBER");
            if(employee.getSignature() != null){
               
                param.add(employee.getSignature()); type.add("BYTES");
                param.add(employee.getSignatureName()); type.add("STRING");
            }
            param.add(employee.getSignatureNumber()); type.add("STRING");
            param.add(employee.getCategory()); type.add("STRING");
            param.add(employee.getJobTitleId()); type.add("NUMBER");
            param.add(employee.getSalaryGradeId()); type.add("NUMBER");
            param.add(employee.getBranchDesc()); type.add("STRING");
            param.add(employee.getEmployeeNumber()); type.add("NUMBER");
            int i = 0;
            if(employee.getSignature() == null){
                System.out.println("Query to update:" + DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_UPDATE_BASIC_WITHOUT_SIGNATURE);

                connection = this.getConnection(false);
                i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_UPDATE_BASIC_WITHOUT_SIGNATURE,
                        param, type);
            }else{
                
                System.out.println("Query to update:" + DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_UPDATE_BASIC);
                try
                {
                connection = this.getConnection(false);
                i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_UPDATE_BASIC,
                        param, type);
                
                }
                catch(Exception ex)
                {
                   ex.printStackTrace();
                }
            }
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

    public int updateEmployeeDeprecateStatus(Employee employee){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(employee.getActive()); type.add("STRING");
            param.add(employee.getDeprecateReason()); type.add("STRING");
            
            param.add(employee.getEmployeeNumber()); type.add("NUMBER");

            int i = 0;
            
            connection = this.getConnection(false);
            i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_UPDATE_DEPRECATE_STATUS,
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

    
    public int updateEmployeeSignatureNumber(Employee employee, Connection connection){
        //Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            //connection = this.getConnection(false);

            startTransaction(connection);
            int signatureNumber = 0;
            ResultSet rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().SIGNATURE_NUMBER, connection);
            if(rst.next()){
                signatureNumber = rst.getInt("signature_number");
            }else{
                throw new Exception("Max Signature Number Table returning no record");
            }
            System.out.println("Signature Number to Use for(" + employee.getEmployeeNumber() + "):" + signatureNumber);
            param.add(signatureNumber); type.add("NUMBER");

            param.add(employee.getEmployeeNumber()); type.add("NUMBER");

            int i = 0;

            i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_UPDATE_SIGNATURE_NUMBER,
                    param, type);

            int a = this.executeUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().INCREASE_SIGNATURE_NUMBER);

            commitTransaction(connection);
            return i;
        }catch(Exception e){
            e.printStackTrace();
            try{ rollbackTransaction(connection); }catch(Exception er){}
            return 0;
        }
    }

    public int deleteEmployee(List<Long> emps){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{


            connection = this.getConnection(false);
            this.startTransaction(connection);
            for(long id : emps){
                param.add(id); type.add("NUMBER");
                int i = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_DELETE,
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


    public boolean assignSignatureNumbers(int numberOfRecords){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(false);
            this.startTransaction(connection);

            param.add(numberOfRecords); type.add("NUMBER");

            CallableStatement cs = executeProcedure(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().AUTOMATED_MAX_SIGNATURE_NUMBER,
                    param, type);//, true, 4);
            if(cs == null){
                throw new Exception("Unable to execute procedure...");
            }
            this.commitTransaction(connection);
            return true;
        }catch(Exception e){
            try {
                this.rollbackTransaction(connection);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
 public List<Employee> getAllEmployeeBasicForUserCreate(){
        return getAllEmployeeBasicForUserCreate(null, null);
    }
    public List<Employee> getAllEmployeeBasicForUserCreate(String criteria, String searchText){
        Connection connection = null;
        List<Employee> records = new ArrayList<Employee>();
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            connection = this.getConnection(true);

            ResultSet rst = null;
            if(criteria == null || searchText == null){
                System.out.println(DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_FOR_USER_CREATE);
                rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_FOR_USER_CREATE,
                        param, type);
            }else{
                String query = "";
                //param.add(searchText); type.add("STRING");
                
                if(criteria.equalsIgnoreCase("FN")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_FNAME_FOR_USER_CREATE;
                }else if(criteria.equalsIgnoreCase("LN")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_LNAME_FOR_USER_CREATE;
                }else if(criteria.equalsIgnoreCase("ID")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_ID_FOR_USER_CREATE;
                }
                query = query.replaceAll("_SEARCH_", searchText);

                System.out.println("One search Query...:" + query);
                
                rst = this.executeParameterizedQuery(connection, query,
                        param, type);
            }
            
            while(rst.next()){
                Employee item = new Employee();
                item.setStaffId(rst.getString("staffid"));
                item.setEmployeeNumber(rst.getLong("emp_number"));
                item.setDepartmentId(rst.getInt("deptid"));
                item.setJobTitleId(rst.getInt("jobtitleid"));
                
                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));//,
                item.setOtherNames(rst.getString("othernames"));
                
                item.setActive(rst.getString("active"));

                item.setEmail(rst.getString("email"));
                item.setDivisionId(rst.getInt("divisionid"));
                item.setCellPhone(rst.getString("cellphone"));
                item.setExtension(rst.getInt("extension"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
                item.setFullName(item.getFirstName()+ " " + item.getLastName());
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
  
    public List<Employee>  GetEmployeeInfoForTextFileGeneration() throws Exception{
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        List<Employee> records = new ArrayList();
        try{
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().GENERATE_EMPLOYEE_INFO_FOR_NIBSS,
                        param, type);
            while(rst.next()){
                Employee item = new Employee();
                item.setFirstName(rst.getString("firstname"));
                item.setOtherNames(rst.getString("othernames"));
                item.setLastName(rst.getString("lastname"));
                item.setCategory(rst.getString("category"));
                item.setSignatureNumber(rst.getString("signature_number"));
                item.setStaffId(rst.getString("staffid"));
                item.setJobTitle(rst.getString("jobtitle"));
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
     public List<Employee> getEmployeeInfoForEbook(){
        Connection connection = null;
        Vector param =new Vector();
        Vector type = new Vector();
        
        List<Employee> records = new ArrayList();
        try{
            connection = this.getConnection(true);

            ResultSet rst = this.executeParameterizedQuery(connection,
                        DatabaseQueryLoader.getQueryAssignedConstant().GENERATE_EMPLOYEE_INFO_FOR_E_BOOK,
                        param, type);
            while(rst.next()){
                Employee item = new Employee();
                item.setStaffId(rst.getString("staffid"));
                item.setLastName(rst.getString("lastname"));
                item.setFirstName(rst.getString("firstname"));//,
                item.setOtherNames(rst.getString("othernames"));
                item.setSignature(rst.getBytes("signature"));
                item.setSignatureName(rst.getString("signature_name"));
                item.setSignatureNumber(rst.getString("signature_number"));
                item.setCategory(rst.getString("category"));
                item.setFormattedtxnLimt(Float.toString(rst.getFloat("Transaction_Limit")));
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
     
      public int getEmployeeIdByUserName(String username,Connection con){
        Vector param =new Vector();
        Vector type = new Vector();
        int employeeNumber = 0;
        try{
            param.add(username);
            type.add("STRING");
            ResultSet rst = this.executeParameterizedQuery(con,
                    DatabaseQueryLoader.getQueryAssignedConstant().EMPLOYEE_ID_BASIC_BY_USER_NAME,
                    param, type);

            while(rst.next()){
                 Long    employeeNumberee =  rst.getLong("emp_number");
                 employeeNumber = employeeNumberee.intValue();
                 return employeeNumber;   
            }
        }catch(Exception e){
            e.printStackTrace();
       
        }
        return employeeNumber;
    }
}
