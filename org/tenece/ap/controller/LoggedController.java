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

package org.tenece.ap.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.tenece.web.common.DateUtility;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Users;
import org.tenece.web.services.EmployeeService;

/**
 *
 * @author strategiex
 */
public class LoggedController extends BaseController{
    private EmployeeService employeeService;
    private Users user;
    
    /** Creates a new instance of Login */
    public LoggedController() {
    }
    
    public ModelAndView loadHeader(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView view = new ModelAndView("header_user");
        System.out.println("getting header");

        Users user = this.getUserPrincipal(request);           
        java.util.Date lastDate = user.getLastLoginDate();  
        String lastTime = null;
        String lastLoginDate = null;
        if(lastDate != null)
        {
         lastTime = user.getLastLoginTime(); 
         lastLoginDate = org.tenece.web.common.DateUtility.getDateAsString(lastDate, "dd/MM/yyyy");
        }
        HttpSession session  = request.getSession();
        String typeOfMode = (String)session.getAttribute("menuMode");
        try{
            if(user.getSuperAdmin() == 1 && user.getAdminUser() == 0){
                view.addObject("displayMenuOption", "Y");
                if(typeOfMode == null || typeOfMode.trim().equals("E")){
                    view.addObject("menuOption", "E");
                }else{
                    view.addObject("menuOption", "A");
                }
            }else{
                view.addObject("displayMenuOption", "N");
            }
        }catch(Exception er){}
        view.addObject("lastLoginDate",lastLoginDate);
        view.addObject("lastTime",lastTime);
        return view;
    }

    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView view = new ModelAndView("home");
        
        return view;
    }
    public ModelAndView footer(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView view = new ModelAndView("footer");
        
        return view;
    }
    public ModelAndView reloadMenu(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //get session (the current session)
        HttpSession session = request.getSession();
        //get current user information
        Users user = this.getUserPrincipal(request);
        Date lastLoginDate = user.getDateLogin();

        if(lastLoginDate == null){
            session.setAttribute("firstLogin", 1); //true
        }else{
            session.setAttribute("firstLogin", 0); //false
        }
        
        //get parameter pass to this URL
        String type = (String) session.getAttribute("menuMode");
        //create a variable to hold the final value to set
        String value = "";
        if(type == null){ //because the menu by default will be ESS (E)
            value = "A";
        }else if(type.trim().equals("A")){
            value = "E";
        }else if(type.trim().equals("E")){
            value = "A";
        }
        session.setAttribute("menuMode", value);
        //session.setAttribute("lastDate", strLoginDate);
        return welcome(request, response);
    }

    public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView view = new ModelAndView("welcome");

        HttpSession session = request.getSession();
        Users user = this.getUserPrincipal(request);
        /*
        Employee employee = null;
        try{
            employee = getEmployeeService().findEmployeeBasicById((long)user.getEmployeeId());

            if(employee == null && user.getEmployeeId() <= 0){
                throw new Exception("No Employee");
            }

            //check if user can switch menu
            String typeOfMode = (String)session.getAttribute("menuMode");
            try{
                if(user.getSuperAdmin() == 1 && user.getAdminUser() == 0){
                    view.addObject("displayOption", "Y");
                }else{
                    view.addObject("displayOption", "N");
                }
            }catch(Exception er){}
        }catch(Exception er){
            employee = new Employee();
            employee.setFirstName("Advance+ Administrator");
            employee.setLastName("");
        }

        view.addObject("employee", employee);
        */
        return view;
    }
    /**
     * @return the employeeService
     */
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    /**
     * @param employeeService the employeeService to set
     */
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
}
