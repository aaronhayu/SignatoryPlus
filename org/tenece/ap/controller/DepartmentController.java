
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 27 May 2009, 13:28
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

package org.tenece.ap.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.tenece.ap.constant.AuditOperationCodeConstant;
import org.tenece.web.data.Department;
import org.tenece.web.data.Employee;
import org.tenece.web.services.SetupService;

/**
 *
 * @author jeffry.amachree
 */
public class DepartmentController extends BaseController{
    
    private SetupService setupService = new SetupService();
    
    /** Creates a new instance of DepartmentController */
    public DepartmentController() {
    }
    
    public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("dept_view");

        //do search here...
        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");

        //get list of employee
        List<Department> list = null;
        if(cbSearch == null || txtSearch == null){

           
                list = setupService.findAllDepartments();
                view.addObject("showCompany", "Y");
            
        }else{

            
                list = setupService.findAllDepartments(cbSearch, txtSearch);
                view.addObject("showCompany", "Y");
            
        }

        view.addObject("result", list);
        createNewAuditTrail(request, "View Departments",AuditOperationCodeConstant.View_Departments);
        return view;
    }
    
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("dept_edit");
        String param = request.getParameter("idx");
        if(param == null){
            return viewAll(request, response);
        }
        int id = Integer.parseInt(param);
        Department dept = setupService.findDepartmentByID(id);

        view.addObject("department", dept);

        //view.addObject("employeeList", getEmployeeService().findAllEmployeeForBasic());
        view.addObject("deptList", this.getSetupService().findAllDepartments());
        view.addObject("pageTitle", "edit");
        //get company info
        view = getAndAppendCompanyToView(view, request);
        createNewAuditTrail(request, "View Edit Departments: Record" + dept.toString(),AuditOperationCodeConstant.View_Edit_Departments);
        return view;
    }
    
    public ModelAndView newRecord(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("dept_edit");

        view.addObject("department", new Department());
        
        //view.addObject("employeeList", getEmployeeService().findAllEmployeeForBasic());
        view.addObject("deptList", this.getSetupService().findAllDepartments());

        //get company info
        view = getAndAppendCompanyToView(view, request);
        view.addObject("pageTitle", "new");
        createNewAuditTrail(request, "Accessing Create New Departments Interface",AuditOperationCodeConstant.View_Create_New_Departments);
        return view;
    }
    
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("");
        List<Integer> ids = new ArrayList<Integer>();
        //get mode of request
        String mode = request.getParameter("mode");
        //check if mode is for more than one record
        //get mode, if mode is equals 1, then process for multiple
        if(mode != null && mode.trim().equals("1")){
            String[] args = request.getParameterValues("_chk");
            for(String id : args){
                ids.add(Integer.parseInt(id));
            }
        }else{//then it is zero
            ids.add(Integer.parseInt(request.getParameter("id")));
        }
        setupService.deleteDepartment(ids);
        //delete
        createNewAuditTrail(request, "Deleted Departments: id(s)=" + ids.toString(),AuditOperationCodeConstant.Delete_Departments);
        
        return viewAll(request, response);
    }
    
    public ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("message");
        String operation = request.getParameter("txtMode");
        //operation mode: N= New, E=Edit
        if(operation.trim().equals("N")){
            Department dept = new Department();
            String managerId = request.getParameter("cbManager");
            String deptName = request.getParameter("txtName");
            String location = request.getParameter("txtLocation");
            String shortDesc = request.getParameter("txtShortDesc");
            String workDesc = request.getParameter("txtWorkDesc");
            String parentId = request.getParameter("cbDept");
            String companyCode = "";
            
            if(managerId != null && (!managerId.equals(""))){
                dept.setManagerId(Integer.parseInt(managerId));
            }
            if(parentId != null && (!parentId.equals(""))){
                dept.setParentId(Integer.parseInt(parentId));
            }

            dept.setLocation(location);
            dept.setDepartmentName(deptName);
            dept.setShortDescription(shortDesc);
            dept.setWorkDescription(workDesc);
            dept.setCompanyCode(companyCode);

            boolean saved = setupService.updateDepartment(dept, setupService.MODE_INSERT);
            if(saved == false){
                view = new ModelAndView("error");
                createNewAuditTrail(request, "Error While Creating Department",AuditOperationCodeConstant.Error_While_Creating_Dept);
                view.addObject("message", "Can not save department information. Please try again later");
                return view;
            }
            
            createNewAuditTrail(request, "New Department: Record[" + dept.toString() + "]",AuditOperationCodeConstant.Create_New_Department);

            view.addObject("message", "Record Saved Successfully.");
            
        }else if(operation.trim().equals("E")){

            //populate new dept
            Department dept = new Department();
            String managerId = request.getParameter("cbManager");
            String deptName = request.getParameter("txtName");
            String location = request.getParameter("txtLocation");
            String shortDesc = request.getParameter("txtShortDesc");
            String workDesc = request.getParameter("txtWorkDesc");
            String parentId = request.getParameter("cbDept");
            String companyCode = request.getParameter("cbCompany");

            String deptId = request.getParameter("txtId");
            
            if(deptId == null || deptId.trim().equals("")){
                view = new ModelAndView("error");
                createNewAuditTrail(request, "Error While Updating Department",AuditOperationCodeConstant.Error_While_Updating_Dept);
                view.addObject("message","Unable to complete update.");
                return view;
            }
            //get previous department infor before attempt to save
            String previousDept = this.getSetupService().findDepartmentByID(Integer.parseInt(deptId)).toString();

            if(managerId != null && (!managerId.equals(""))){
                dept.setManagerId(Integer.parseInt(managerId));
            }
            if(parentId != null && (!parentId.equals(""))){
                dept.setParentId(Integer.parseInt(parentId));
            }

            dept.setId(Integer.parseInt(deptId));
            dept.setManagerId(Integer.parseInt(managerId));
            dept.setLocation(location);
            dept.setDepartmentName(deptName);
            dept.setShortDescription(shortDesc);
            dept.setWorkDescription(workDesc);
            dept.setCompanyCode(companyCode);
            
            boolean saved = setupService.updateDepartment(dept, setupService.MODE_UPDATE);
            if(saved == false){
                createNewAuditTrail(request, "Unable to modify Department:[" + dept.toString() + "]",AuditOperationCodeConstant.Error_While_Updating_Dept);
                view = new ModelAndView("error");
                view.addObject("message", "Can not save department information. Please try again later");
                return view;
            }

            createNewAuditTrail(request, "Saved Department: Old Record=[" + previousDept + "], New Record[" + dept.toString() + "]",AuditOperationCodeConstant.Modify_Department_Successfully);
            view.addObject("message", "Record Saved Successfully.");
        }
        return view;
    }

    private SetupService getSetupService() {
        return setupService;
    }

    private void setSetupService(SetupService setupService) {
        this.setupService = setupService;
    }
    
}
