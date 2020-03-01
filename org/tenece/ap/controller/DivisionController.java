
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
import org.tenece.web.data.Division;
import org.tenece.web.services.SetupService;

/**
 *
 * @author jeffry.amachree
 */
public class DivisionController extends BaseController{
    
    private SetupService setupService = new SetupService();
    
    /** Creates a new instance of divisionController */
    public DivisionController() {
    }
    
    public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("division_view");

        //do search here...
        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");

        //get list of employee
        List<Division> list = null;
        if(cbSearch == null || txtSearch == null){

            list = setupService.findAllDivisions();
            //view.addObject("showCompany", "Y");
        }else{

            list = setupService.findAllDivisions(cbSearch, txtSearch);
            //view.addObject("showCompany", "Y");
            
        }
        createNewAuditTrail(request, "View All Branches",AuditOperationCodeConstant.VIEW_ALL_BRANCHES);
        view.addObject("result", list);
        return view;
    }
    
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("division_edit");
        String param = request.getParameter("idx");
        if(param == null){
            return viewAll(request, response);
        }
        int id = Integer.parseInt(param);
        Division division = setupService.findDivisionById(id);
        view.addObject("division", division);
        view.addObject("pageTitle", "edit");
        //get company info
        view = getAndAppendCompanyToView(view, request);

        createNewAuditTrail(request, "View Edit Branch: Record[" + division.toString() + "]",AuditOperationCodeConstant.View_Edit_Branch);

        return view;
    }
    
    public ModelAndView newRecord(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("division_edit");
        
        view.addObject("division", new Division());

        //get company info
        view = getAndAppendCompanyToView(view, request);
        view.addObject("pageTitle", "new");
        createNewAuditTrail(request, "Accessing Create new Branch Interface",AuditOperationCodeConstant.View_Create_New_Branch);
        return view;
    }
    
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("division_delete");
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
        for(int i : ids){
            setupService.deleteDivision(i);
        }
        createNewAuditTrail(request, "Deleted Divisions: id(s)=" + ids.toString(),AuditOperationCodeConstant.Delete_Branches);
       //delete
        return viewAll(request, response);
    }
    
    public ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("message");
        String operation = request.getParameter("txtMode");
        //operation mode: N= New, E=Edit
        if(operation.trim().equals("N")){
            Division division = new Division();
            String desc = request.getParameter("txtDesc");
            String name = request.getParameter("txtName");
            String code = request.getParameter("txtCode");
            
            division.setCode(code);
            division.setDescription(desc);
            division.setName(name);
            String description = setupService.findAllDivisionDescriptionBySolID(code);
            if(description != null)
            {
                view = new ModelAndView("error");
                view.addObject("message", "The SOL Id supplied already exist");
                return view;
            }
            boolean saved = setupService.updateDivision(division, setupService.MODE_INSERT);
            if(saved == false){
                createNewAuditTrail(request, "Error Saving New Branch.",AuditOperationCodeConstant.Error_While_Creating_Branch);
                view = new ModelAndView("error");
                view.addObject("message", "Error processing division setup request. Please try again later");
                return view;
            }
            createNewAuditTrail(request, "New Branch: Record[" + division.toString() + "]",AuditOperationCodeConstant.Create_New_Branch);
            view.addObject("message", "Record Saved Successfully.");
            
        }else if(operation.trim().equals("E")){
            Division division = new Division();
            String desc = request.getParameter("txtDesc");
            String name = request.getParameter("txtName");
            String code = request.getParameter("txtCode");
            
            String id = request.getParameter("txtId");
            if(id == null || id.trim().equals("")){
                createNewAuditTrail(request, "Error Modifying Branch. No Unique Header",AuditOperationCodeConstant.Error_While_Updating_Branch);
                view = new ModelAndView("error");
                view.addObject("message","Unable to update division information.");
                return view;
            }
            
            division.setId(Integer.parseInt(id));
            division.setCode(code);
            division.setDescription(desc);
            division.setName(name);

            //get previous division info.
            String previousDivision = this.getSetupService().findDivisionById(Integer.parseInt(id)).toString();

            boolean saved = setupService.updateDivision(division, setupService.MODE_UPDATE);
            if(saved == false){
                createNewAuditTrail(request, "Unable to modify Branch:[" + division.toString() + "]",AuditOperationCodeConstant.Error_While_Updating_Branch);
                view = new ModelAndView("error");
                view.addObject("message", "Error processing division setup request. Please try again later");
                return view;
            }
            
            createNewAuditTrail(request, "Saved Division: Old Record=[" + previousDivision + "], New Record[" + division.toString() + "]",AuditOperationCodeConstant.Modify_Branch_Successfully);

            view.addObject("message", "Record Saved Successfully.");
        }
        return view;
    }

    public SetupService getSetupService() {
        return setupService;
    }

    public void setSetupService(SetupService setupService) {
        this.setupService = setupService;
    }
    
}
