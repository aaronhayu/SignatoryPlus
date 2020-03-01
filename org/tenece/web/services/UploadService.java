/*
 * (c) Copyright 2010 The Tenece Professional Services.
 *
 * Created on 6 February 2009, 09:57
 *
 * ============================================================
 * Project Info:  http://tenece.com
 * Project Lead:  Aaron Osikhena (aaron.osikhena@tenece.com);
 * ============================================================
 *
 *
 * Licensed under the Tenece Professional Services;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.tenece.com/
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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.tenece.ap.data.dao.EmployeeDAO;
import org.tenece.ap.data.dao.UserDAO;
import org.tenece.ap.exception.RecordExistException;
import org.tenece.ap.exception.UnknownException;
import org.tenece.web.data.Employee;
import org.tenece.web.data.FileUpload;
import org.tenece.api.excel.XLSReader;
import org.tenece.web.data.Users;

/**
 * Tenece Professional Services Limited
 * @author amachree
 */
public class UploadService extends BaseService {
    private EmployeeDAO employeeDAO = null;
    private UserDAO userDAO = new UserDAO();

    public List<Employee> saveUploadedNamelistFile(FileUpload fileUpload, String fileType) throws IllegalStateException, Exception{
        //get device type
        List<Employee> attendanceList = null;
        if(fileType.trim().equalsIgnoreCase("csv")){ //default
            attendanceList = getDefaultTemplate(fileUpload);
        }else if(fileType.trim().equalsIgnoreCase("xls")){
            try {
                attendanceList = getXLSTemplate(fileUpload);
            } catch (ParseException ex) {
                throw new Exception("Invalid numeric value in XLS file");
            } catch (IOException ex) {
                throw new Exception("Unable to read file, ensure the file is a valid Microsoft OLE file.");
            }catch (RecordExistException ex) {
                throw new Exception("Unable to process file.<br/>"+ex.getMessage());
            }catch (UnknownException ex) {
                throw new Exception("Unable to process file.<br/>"+ex.getMessage());
            }catch (NumberFormatException ex) {
                throw new Exception("Unable to process file, Invalid Numeric value specified in file.<br/>"+ex.getMessage());
            } catch (Exception ex) {
                throw new Exception("Invalid Microsoft OLE file. Ensure the file is a valid Excel File");
            }
        }

        return attendanceList;
    }

    public List<Employee> getXLSTemplate(FileUpload fileUpload) throws ParseException, IOException, Exception{

        List<Employee> attendanceList = new ArrayList<Employee>();

        //get xlsfile content into List array
        File xlsFile = new File(fileUpload.getAbsolutePath());
        XLSReader xlsReader = new XLSReader();
        List<List> records = xlsReader.convertXLSToList(xlsFile, 0);
        if(records.size() == 0){
            throw new IllegalStateException("No record in XLS file");
        }

        for(int a = 0; a < records.size(); a++){
            Employee attendance = new Employee();
            List<Object> colValue = records.get(a);
            String staffID = null;
            System.out.println(">>>>>>>:" + colValue.size());
            if(a > 0){
                try{
                    for(int i = 0; i < colValue.size(); i++){
                        if((i + 1) == 1){//position 3 -SaffID
                            int sn = (int) Math.round(Double.parseDouble(String.valueOf(colValue.get(i)).trim()));
                            attendance.setSerialNumber(sn);

                        }else if((i + 1) == 3){//position 3 -StaffID
                            staffID =  String.valueOf(colValue.get(i)).trim();
                            attendance.setStaffId(String.valueOf(staffID));
                            //(int) Math.round(Double.parseDouble(String.valueOf(colValue.get(i)).trim()))
                            //validate employee ID
                            try{
                                Employee employee = getEmployeeDAO().getEmployeeBasicDataByStaffId(attendance.getStaffId());
                                if(employee != null){
                                    throw new RecordExistException("Employee with staff ID (" + String.valueOf(staffID) + ") exists on the system.");
                                }
                                //attendance.setFullName(employee.getFullName());
                                attendance.setErr("0");
                            }catch(Exception err){
                                err.printStackTrace();
                                attendance.setErr("1");
                                throw new UnknownException("System error when confirming employee with staff ID (" + String.valueOf(staffID) + ").");
                            }

                        }else if((i + 1) == 2){
                            attendance.setFullName((String) colValue.get(i));
                        }else if((i + 1) == 4){
                            attendance.setSignatureNumber(String.valueOf(colValue.get(i)).trim());
                        }//:
                        else if((i + 1) == 5){
                            attendance.setCategory(String.valueOf(colValue.get(i)).trim());
                        }//:
                    }
                }catch(NumberFormatException er){
                    System.out.println("***Error processing record-:" + colValue);
                    throw new NumberFormatException(colValue.toString());
                }
            attendanceList.add(attendance);
            }//:
        }
        return attendanceList;
    }

    public List<Employee> getDefaultTemplate(FileUpload fileUpload){
        try{
            List<Employee> attendanceList = new ArrayList<Employee>();
            //get file content
            String fileContent = new String(fileUpload.getBytes());
            //get all rows as array based on new line
            String[] rows = fileContent.split("\n");

            for(String row : rows){
                //create a dummy object
                Employee attendance = new Employee();
                String[] columns = row.split(",");

                String serialNumber = columns[0];
                String strFullName = columns[1].trim();
                String strStaffID = columns[2].trim();
                String signatureNumber = columns[3].trim();
                String category = columns[4].trim();
                //get empID from empNumber
                Employee emp = getEmployeeDAO().getEmployeeBasicDataByStaffId(strStaffID);
                attendance.setErr("0");
                if(emp != null)
                { 
                    attendance.setErr("1");
                    throw new RecordExistException("Employee with staff ID (" + String.valueOf(strStaffID) + ") exists on the system.");
                               
                }
                attendance.setSignatureNumber(signatureNumber);
                attendance.setFullName(strFullName);
                attendance.setStaffId(strStaffID);
                attendance.setCategory(category);

                attendanceList.add(attendance);
            }
            return attendanceList;
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Employee>();
        }
    }

    public boolean saveBulkEmployeeNameList(List<Employee> employeeList){
        try{
            int count = getEmployeeDAO().createBulkEmployeeNameList(employeeList);
            if(count > 0){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveBulkUsersList(HttpServletRequest request,List<Users> userList, int roleid){
        try{
            boolean saved = getUserDAO().saveBulkUsersList(request,userList,roleid);
            return saved;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
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
     /**
     * @return the employeeDAO
     */
    public UserDAO getUserDAO() {
        return userDAO;
    }

    /**
     * @param employeeDAO the employeeDAO to set
     */
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
 public List<Users> saveUploadedUserlistFile(FileUpload fileUpload, String fileType,String roletext) throws IllegalStateException, Exception{
        //get device type
        List<Users> userList = null;
        if(fileType.trim().equalsIgnoreCase("csv")){ //default
            userList = getDefaultTemplateForUsersList(fileUpload,roletext);
        }else if(fileType.trim().equalsIgnoreCase("xls")){
            try {
                userList = getXLSTemplateForUsersList(fileUpload,roletext);
            } catch (ParseException ex) {
                throw new Exception("Invalid numeric value in XLS file");
            } catch (IOException ex) {
                throw new Exception("Unable to read file, ensure the file is a valid Microsoft OLE file.");
            }catch (RecordExistException ex) {
                throw new Exception("Unable to process file.<br/>"+ex.getMessage());
            }catch (UnknownException ex) {
                throw new Exception("Unable to process file.<br/>"+ex.getMessage());
            }catch (NumberFormatException ex) {
                throw new Exception("Unable to process file, Invalid Numeric value specified in file.<br/>"+ex.getMessage());
            } catch (Exception ex) {
                throw new Exception("Invalid Microsoft OLE file. Ensure the file is a valid Excel File");
            }
        }

        return userList;
    }
 public List<Users> getDefaultTemplateForUsersList(FileUpload fileUpload,String roletext){
        try{
            List<Users> userList = new ArrayList<Users>();
            //get file content
            String fileContent = new String(fileUpload.getBytes());
            //get all rows as array based on new line
            String[] rows = fileContent.split("\n");

            for(String row : rows){
                //create a dummy object
                Users userToUpload = new Users();
                String[] columns = row.split(",");

                String serialNumber = columns[0];
                String strFullName = columns[1].trim();
                String username = columns[2].trim();
                String roleName = roletext;
               
                //get empID from empNumber
                
                Users user = getUserDAO().getUserByUserName(username);
                userToUpload.setErr("0");
                if(user != null)
                { 
                    userToUpload.setErr("1");
                    throw new RecordExistException("User with user name (" + String.valueOf(username) + ") exists on the system.");
                               
                }
               
                userToUpload.setUserName(username);
                userToUpload.setFullnames(strFullName);
                userToUpload.setRoleName(roleName);
                userList.add(userToUpload);
            }
            return userList;
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Users>();
        }
    }
public List<Users> getXLSTemplateForUsersList(FileUpload fileUpload,String roletext) throws ParseException, IOException, Exception{

        List<Users> userList = new ArrayList<Users>();

        //get xlsfile content into List array
        File xlsFile = new File(fileUpload.getAbsolutePath());
        XLSReader xlsReader = new XLSReader();
        List<List> records = xlsReader.convertXLSToList(xlsFile, 0);
        if(records.size() == 0){
            throw new IllegalStateException("No record in XLS file");
        }

        for(int a = 0; a < records.size(); a++){
            Users userToUpload = new Users();
            List<Object> colValue = records.get(a);
            String userName = null;
            System.out.println(">>>>>>>:" + colValue.size());
            if(a > 0){
                try{
                    for(int i = 0; i < colValue.size(); i++){
                        if((i + 1) == 1){//position 3 -SaffID
                            int sn = (int) Math.round(Double.parseDouble(String.valueOf(colValue.get(i)).trim()));
                            userToUpload.setSerialNumber(sn);

                        }else if((i + 1) == 3){//position 3 -StaffID
                            userName =  String.valueOf(colValue.get(i)).trim();
                            userToUpload.setUserName(String.valueOf(userName));
                            //(int) Math.round(Double.parseDouble(String.valueOf(colValue.get(i)).trim()))
                            //validate employee ID
                            try{
                                Users user = getUserDAO().getUserByUserName(userName);
                                if(user != null){
                                    throw new RecordExistException("User with User Name (" + String.valueOf(userName) + ") exists on the system.");
                                }
                                //attendance.setFullName(employee.getFullName());
                                userToUpload.setErr("0");
                            }catch(Exception err){
                                err.printStackTrace();
                                userToUpload.setErr("1");
                                throw new UnknownException("System error when confirming User with User Name (" + String.valueOf(userName) + ").");
                            }

                        }else if((i + 1) == 2){
                            userToUpload.setFullnames((String) colValue.get(i));
                        }
                        userToUpload.setRoleName(roletext);
                    }
                }catch(NumberFormatException er){
                    System.out.println("***Error processing record-:" + colValue);
                    throw new NumberFormatException(colValue.toString());
                }
            userList.add(userToUpload);
            }//:
        }
        return userList;
    }

}
