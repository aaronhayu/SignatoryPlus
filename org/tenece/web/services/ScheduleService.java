
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 27 May 2009, 08:51
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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.tenece.ap.data.dao.ApplicationDAO;
import org.tenece.ap.data.dao.DepartmentDAO;
import org.tenece.ap.data.dao.AdvancePaymentDAO;
import org.tenece.ap.data.dao.JobTitleDAO;
import org.tenece.ap.data.dao.DivisionDAO;
import org.tenece.ap.data.dao.EmployeeDAO;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.data.Employee;

/**
 *
 * @author jeffry.amachree
 */
public class ScheduleService extends BaseService{
    private DepartmentDAO departmentDAO = null;
    private AdvancePaymentDAO holidayDAO = null;
    private JobTitleDAO jobTitleDAO = null;
    private DivisionDAO divisionDAO = null;
    private ApplicationDAO applicationDAO = null;
    private EmployeeDAO employeeDAO = null;
    /** Creates a new instance of SetupService */
    public ScheduleService() {
    }

    public void processScannedSignatures(){
        File pathToSignature = new File(ConfigReader.getSignatoryScannedPath());
        System.out.println("**** signature path ***" + ConfigReader.getSignatoryScannedPath());
        //check if file exist
        
        if(pathToSignature.exists() && pathToSignature.isDirectory()){
            System.out.println("Processing signature********************" + new java.util.Date());
            //get all employee records without signatures
            List<Employee> employees = getEmployeeDAO().getAllEmployeeWithoutSignature();
            System.out.println("Total Records********************" + employees.size());
            //process and match staffid
            int i = 0;
            for(Employee employee : employees){
                System.out.println("Data********************" + employee.getStaffId());
                //check if signature is null
                i = i + 1;
                if(i >= 1000){
                    System.out.println("**********Breaking Processes**********");
                    break;
                }

                try{
                    //if(employee.getSignature() == null){
                        System.out.println(String.valueOf(i) + ":= data of signature********************" + employee.getStaffId());
                        //System.out.println(String.valueOf(i) + ":= data of signature********************" + employee.getSignatureNumber());
                        
                        //check if file exist
                        String signatureFileURL = pathToSignature.getAbsolutePath() + File.separator + "sn" + String.valueOf(employee.getStaffId()) + ".jpg";
                        //String signatureFileURL = pathToSignature.getAbsolutePath() + File.separator + String.valueOf(employee.getSignatureNumber()) + ".jpg";
                        System.out.println("Signature PATH********************" + signatureFileURL);
                        File signatureFile = new File(signatureFileURL);
                        //read file if it exists
                        if(signatureFile.exists()){

                            byte[] byteContent = new byte[(int)signatureFile.length()];
                            DataInputStream dIn = new DataInputStream(new FileInputStream(signatureFile));
                            dIn.read(byteContent);
                            dIn.close();

                            //add signature to employee info
                            employee.setSignature(byteContent);
                            employee.setSignatureName(signatureFile.getName());
                            //sterialize signature
                            getEmployeeDAO().updateEmployeeBasic(employee);
                            try{
                                signatureFile.delete();
                            }catch(Exception er){
                                System.out.println("ERR ****** Can not delete file ");
                            }
                            System.out.println("=============SAVED: " + employee.getStaffId() + "===============");
                        //}
                    }
                }catch(Exception e){
                    System.out.println("Error processing signature file for: " + employee.getStaffId());
                }
            }
            //process image and insert for student
        }
        System.out.println("<<<<<<<<<<< Processor Sleeping....>>>>>>>>>>>");
    }

    public void processSignatureNumber(){
        Connection connection = null;
        try{
            System.out.println("----------- Starting Signatiure Number Processing ----------");
            //check if file exist

            //get all employee records without signatures
            List<Employee> employees = getEmployeeDAO().getAllEmployeeWithoutSignatureNumber();
            //process and match staffid
            connection  = getEmployeeDAO().getConnection(false);

            for(Employee employee : employees){
                //check if signature is null
                try{
                    if(employee.getSignature() == null){
                        System.out.println("Processing Employee=============" + employee.getStaffId());

                        //sterialize signature
                        getEmployeeDAO().updateEmployeeSignatureNumber(employee, connection);

                        System.out.println("=============SAVED: " + employee.getStaffId() + "with Signature Number:===============");

                    }
                }catch(Exception e){
                    System.out.println("Error processing signature file for: " + employee.getStaffId());
                }
            }
            //process image and insert for student

            System.out.println("~~~~~~~~~~~~~~~~~~ Signature Number Processor Sleeping....~~~~~~~~~~~~~~");
        }catch(Exception e){
            //try{ connection.close(); }catch(Exception er){}
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public EmployeeDAO getEmployeeDAO() {
        if(employeeDAO == null){
            employeeDAO = new EmployeeDAO();
        }
        return employeeDAO;
    }

    public DepartmentDAO getDepartmentDAO() {
        if(departmentDAO == null){
            departmentDAO = new DepartmentDAO();
        }
        return departmentDAO;
    }

    public AdvancePaymentDAO getHolidaysDAO() {
        if(holidayDAO == null){
            holidayDAO = new AdvancePaymentDAO();
        }
        return holidayDAO;
    }

    public JobTitleDAO getJobTitleDAO() {
        if(jobTitleDAO == null){
            jobTitleDAO = new JobTitleDAO();
        }
        return jobTitleDAO;
    }

    public DivisionDAO getDivisionDAO() {
        if(divisionDAO == null){
            divisionDAO = new DivisionDAO();
        }
        return divisionDAO;
    }

    /**
     * @return the applicationDAO
     */
    public ApplicationDAO getApplicationDAO() {

        if(applicationDAO == null){
            applicationDAO = new ApplicationDAO();
        }
        return applicationDAO;
    }
}
