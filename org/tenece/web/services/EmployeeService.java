
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 27 May 2009, 10:25
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

import java.util.ArrayList;
import java.util.List;
import org.tenece.ap.data.dao.EmployeeDAO;
import org.tenece.ap.data.dao.LocksDAO;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Lock;

/**
 *
 * @author jeffry.amachree
 */
public class EmployeeService extends BaseService {
    private EmployeeDAO employeeDAO = new EmployeeDAO() ;
    private LocksDAO lockDAO = null;
    
    /** Creates a new instance of EmployeeService */
    public EmployeeService() {
    }

    public boolean assignSignatureNumbers(int recordCount){
        boolean saved = getEmployeeDAO().assignSignatureNumbers(recordCount);
        return saved;
    }

    public boolean updateEmployeeDeprecateStatus(Employee employee){
        int saved = getEmployeeDAO().updateEmployeeDeprecateStatus(employee);
        if(saved == 0){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateEmployeeBasic(Employee employee, int mode){
        try{
            int saved = 0;
            if(this.MODE_INSERT == mode){
                saved = getEmployeeDAO().createEmployee_WithBasic(employee);
            }else if(this.MODE_UPDATE == mode){
                saved = getEmployeeDAO().updateEmployeeBasic(employee);
            }
            if(saved == 0){
               
               //throw new Exception("Unable to save employee");
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Employee> findAllEmployeeForBasic(){
        try{
            return getEmployeeDAO().getAllEmployeeBasic();
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Employee>();
        }
    }

    public List<Employee> findAllEmployeeForBasic(String criteria, String searchText){
        try{
            return getEmployeeDAO().getAllEmployeeBasic(criteria, searchText);
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Employee>();
        }
    }
    public List<Employee> findAllEmployeeBasicForUserCreate(){
        try{
            return getEmployeeDAO().getAllEmployeeBasicForUserCreate();
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Employee>();
        }
    }

    public List<Employee> findAllEmployeeBasicForUserCreate(String criteria, String searchText){
        try{
            return getEmployeeDAO().getAllEmployeeBasicForUserCreate(criteria, searchText);
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Employee>();
        }
    }

    
    public Employee findEmployeeBasicById(long id){
        try{
            
            return getEmployeeDAO().getEmployeeBasicDataById(id);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
     public String findUnAuthorisedSignatoryUserByUserName(String  username){
        try{
            
            return getEmployeeDAO().getUnAuthorisedSignatoryUserByUserName(username);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Employee findEmployeeBasicByStaffId(String id){
        try{
            return getEmployeeDAO().getEmployeeBasicDataByStaffId(id);
        }catch(Exception e){
            return null;
        }
    }

    public List<Employee> findAllEmployeeBySearch(String staffId, String fullName, String deptId, String divisionId){
        try{
            return getEmployeeDAO().getAllEmployeeBySearch(staffId, fullName, deptId, divisionId);
        }catch(Exception e){
            return new ArrayList<Employee>();
        }
    }
    
    public boolean deActivateEmployee(List<Long> ids){
        try{
            int rows = getEmployeeDAO().deleteEmployee(ids);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    /* ------------- Employee Locks ------------------- */
    public boolean updateEmployeeLocks(Lock lock, int mode){
        try{
            if(this.MODE_INSERT == mode){
                getLockDAO().createNewLock(lock);
            }else{
                getLockDAO().updateLock(lock);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public Lock findEmployeeLock(int id){
        try{
            return getLockDAO().getLockById(id);
        }catch(Exception e){
            return null;
        }
    }
    
    public boolean deleteEmployeeLock(int id){
        try{
            Lock lock = new Lock();
            lock.setId(id);
            
            getLockDAO().deleteLock(lock);
            return true;
        }catch(Exception e){
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
     * @return the lockDAO
     */
    public LocksDAO getLockDAO() {
        return lockDAO;
    }

    /**
     * @param lockDAO the lockDAO to set
     */
    public void setLockDAO(LocksDAO lockDAO) {
        this.lockDAO = lockDAO;
    }
public Employee findEmployeeSignature(String criteria, String searchText){
        try{
            return getEmployeeDAO().getEmployeeSignature(criteria, searchText);
        }catch(Exception e){
            e.printStackTrace();
            return new Employee();
        }
    }
public List<Employee> getEmployeeInfoForEbook(){
        try{
            return getEmployeeDAO().getEmployeeInfoForEbook();
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Employee>();
        }
    }

public List<Employee> GetEmployeeInfoForTextFileGeneration(){
        try{

            return getEmployeeDAO().GetEmployeeInfoForTextFileGeneration();
             
        }
        catch(Exception e){
           e.printStackTrace();
           return new ArrayList<Employee>();
        }
    }  
}
