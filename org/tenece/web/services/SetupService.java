
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

import java.util.ArrayList;
import java.util.List;
import org.tenece.ap.data.dao.ApplicationDAO;
import org.tenece.ap.data.dao.DepartmentDAO;
import org.tenece.ap.data.dao.AdvancePaymentDAO;
import org.tenece.ap.data.dao.CategoryDAO;
import org.tenece.ap.data.dao.JobTitleDAO;
import org.tenece.ap.data.dao.DivisionDAO;
import org.tenece.ap.data.dao.GradeDAO;
import org.tenece.web.data.Category;
import org.tenece.web.data.ControlData;
import org.tenece.web.data.Department;
import org.tenece.web.data.JobTitle;
import org.tenece.web.data.Division;
import org.tenece.web.data.Grade;

/**
 *
 * @author jeffry.amachree
 */
public class SetupService extends BaseService{
    private DepartmentDAO departmentDAO = null;
    private AdvancePaymentDAO holidayDAO = null;
    private JobTitleDAO jobTitleDAO = null;
    private DivisionDAO divisionDAO = null;
    private ApplicationDAO applicationDAO = null;
    private GradeDAO gradeDAO = null;
    private CategoryDAO categoryDAO = null;
    /** Creates a new instance of SetupService */
    public SetupService() {
    }
    
    /* ------------ DEPARTMENT -------------- */
    /**
     * This methos is used to update/create a new department
     * The Paramaters required are : Department and Mode (1 or 0)
     */
    public boolean updateDepartment(Department department, int mode){
        try{
            int rows = 0;
            if(this.MODE_INSERT == mode){
                rows = getDepartmentDAO().createNewDepartment(department);
            }else if(this.MODE_UPDATE == mode){
                rows = getDepartmentDAO().updateDepartment(department);
            }
            if(rows == 0){ return false; }
            return true;
        }catch(Exception e){
            return false;
        }
    }//: updateDepartment
    
    public List<Department> findAllDepartments(){
        try{
            return getDepartmentDAO().getAllDepartments();
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Department>();
        }
    }
    public List<Department> findAllDepartments(String criteria, String searchText){
        try{
            return getDepartmentDAO().getAllDepartments(criteria, searchText); 
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Department>();
        }
    }

    public List<Department> findAllDepartmentsByCompany(String code){
        try{
            return getDepartmentDAO().getAllDepartmentsByCompany(code);
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Department>();
        }
    }
    public List<Department> findAllDepartmentsByCompany(String code, String criteria, String searchText){
        try{
            return getDepartmentDAO().getAllDepartmentsByCompany(code, criteria, searchText);
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<Department>();
        }
    }

    public Department findDepartmentByID(int id){
        try{
            return getDepartmentDAO().getDepartmentById(id);
        }catch(Exception e){
            return null;
        }
    }
    
    public boolean deleteDepartment(int id){
        try{
            Department dept = new Department();
            dept.setId(id);
            getDepartmentDAO().deleteDepartment(dept);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public boolean deleteDepartment(List<Integer> ids){
        try{
            getDepartmentDAO().deleteDepartments(ids);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    /* ------------ JOB TITLE --------------------- */
    public boolean updateJobTitle(JobTitle jobTitle, int mode){
        try{
            int row = 0;
            if(this.MODE_INSERT == mode){
                row = getJobTitleDAO().createNewJobTitle(jobTitle);
            }else if(this.MODE_UPDATE == mode){
                row = getJobTitleDAO().updateJobTitle(jobTitle);
            }
            if(row == 0){
                return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public List<JobTitle> findAllJobTitles(){
        try{
            return getJobTitleDAO().getAllJobTitles();
        }catch(Exception e){
            return new ArrayList<JobTitle>();
        }
    }
 public List<Grade> findAllGrades(){
        try{
            return getGradeDAO().getAllGrades();
        }catch(Exception e){
            return new ArrayList<Grade>();
        }
    }
    public List<Category> findAllCategories(){
        try{
            return getCategoryDAO().getAllCategories();
        }catch(Exception e){
            return new ArrayList<Category>();
        }
    }
     public List<Category> findAllCategoryList(){
        try{
            return getCategoryDAO().getAllCategoryList();
        }catch(Exception e){
            return new ArrayList<Category>();
        }
    }
       public Category findCategoryByCode(String key){
        try{
            return getCategoryDAO().findCategoryByCode(key);
        }catch(Exception e){
            return new Category();
        }
    }
    public List<JobTitle> findAllJobTitlesByCompany(String code){
        try{
            return getJobTitleDAO().getAllJobTitlesByCompany(code);
        }catch(Exception e){
            return new ArrayList<JobTitle>();
        }
    }
    
    public JobTitle findJobTitleById(int id){
        try{
            return getJobTitleDAO().getJobTitleById(id);
        }catch(Exception e){
            return null;
        }
    }
    
    public boolean deleteJobTitle(int id){
        try{
            JobTitle jobTitle = new JobTitle();
            jobTitle.setId(id);
            getJobTitleDAO().deleteJobTitle(jobTitle);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    /* ---------------- Division -------------------- */
    public boolean updateDivision(Division division, int mode){
        try{
            if(this.MODE_INSERT == mode){
                getDivisionDAO().createNewDivision(division);
            }else if(this.MODE_UPDATE == mode){
                getDivisionDAO().updateDivision(division);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public List<Division> findAllDivisions(){
        try{
            return getDivisionDAO().getAllDivisions();
        }catch(Exception e){
            return new ArrayList<Division>();
        }
    }

    public List<Division> findAllDivisions(String criteria, String searchText){
        try{
            return getDivisionDAO().getAllDivisions(criteria, searchText);
        }catch(Exception e){
            return new ArrayList<Division>();
        }
    }
    public String findAllDivisionDescriptionBySolID(String SolID){
        try{
            return getDivisionDAO().findAllDivisionDescriptionBySolID(SolID);
        }catch(Exception e){
            return null;
        }
    }
    public Division findDivisionById(int id){
        try{
            return getDivisionDAO().getDivisionById(id);
        }catch(Exception e){
            return null;
        }
    }
    
    public boolean deleteDivision(int id){
        try{
            Division skill = new Division();
            skill.setId(id);
            getDivisionDAO().deleteDivision(skill);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    
    public List<ControlData> findAllCountries(){
        try{
            return getApplicationDAO().getCountryList();
        }catch(Exception e){
            return new ArrayList<ControlData>();
        }
    }

    public List<ControlData> findAllRelationships(){
        try{
            return getApplicationDAO().getRelationshipList();
        }catch(Exception e){
            return new ArrayList<ControlData>();
        }
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
      public GradeDAO getGradeDAO() {
        if(gradeDAO == null){
            gradeDAO = new GradeDAO();
        }
        return gradeDAO;
    }
public CategoryDAO getCategoryDAO() {
        if(categoryDAO == null){
            categoryDAO = new CategoryDAO();
        }
        return categoryDAO;
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
