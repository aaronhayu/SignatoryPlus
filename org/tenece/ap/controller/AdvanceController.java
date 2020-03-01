
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.common.DateUtility;
import org.tenece.web.data.AdvanceCollection;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Division;
import org.tenece.web.services.AdvancePaymentService;
import org.tenece.web.services.EmployeeService;
import org.tenece.web.services.SetupService;

/**
 *
 * @author jeffry.amachree
 */
public class AdvanceController extends BaseController{
    
    private SetupService setupService = new SetupService();
    private EmployeeService employeeService = new EmployeeService();
    private AdvancePaymentService advancePaymentService = new AdvancePaymentService();

    /** Creates a new instance of divisionController */
    public AdvanceController() {
    }

    public ModelAndView viewAllEmployeeForAdvance(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("advance_payment_employee_view");
        //do search here...
        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");

        //get list of employee
        List<Employee> employees = null;
        if(cbSearch == null || txtSearch == null){
            employees = getEmployeeService().findAllEmployeeForBasic();
        }else{
            employees = getEmployeeService().findAllEmployeeForBasic(cbSearch, txtSearch);
        }
        view.addObject("result", employees);
        return view;
    }

    public ModelAndView viewAllEmployeeAdvancePayment(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("advance_list_view");
        String idx = request.getParameter("idx");
        if(idx == null){

        }
        long employeeId = Long.parseLong(idx);

        view.addObject("result", getAdvancePaymentService().findAllAdvancePayment(employeeId));

        return view;
    }

    public ModelAndView editAdvancePayment(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("advance_payment_edit");
        String strIdx = request.getParameter("idx");
        int id = Integer.parseInt(strIdx);

        AdvanceCollection collection = advancePaymentService.findAdvancePaymentById(id);
        List<Employee> list = new ArrayList<Employee>();
        list.add(getEmployeeService().findEmployeeBasicById(collection.getEmployeeId()));
        
        view.addObject("empList", list);
        view.addObject("advance", collection);

        return view;
    }

    public ModelAndView newAdvancePayment(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("advance_payment_edit");
        view.addObject("empList", getEmployeeService().findAllEmployeeForBasic());
        view.addObject("advance", new AdvanceCollection());
        
        return view;
    }

    public ModelAndView closeAdvancePayment(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("advance_payment_close");
        //String strEmp = request.getParameter("emp");
        String strIdx = request.getParameter("idx");
        int id = Integer.parseInt(strIdx);
        //long employeeId = Long.parseLong(strEmp);

           AdvanceCollection collection = advancePaymentService.findAdvancePaymentById(id);
        view.addObject("advance", collection);
        view.addObject("employee", getEmployeeService().findEmployeeBasicById(collection.getEmployeeId()));

        return view;
    }

    public ModelAndView processAdvancePaymentRequest(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("message");

        String operation = request.getParameter("txtMode");
        //operation mode: N= New, E=Edit
        if(operation.trim().equals("N")){
            AdvanceCollection advance = new AdvanceCollection();
            String formNumber = request.getParameter("txtForm");
            String strAppDate = request.getParameter("txtDate");
            String employeeId = request.getParameter("cbEmployee");
            String purpose = request.getParameter("txtPurpose");
            String advanceTo = request.getParameter("txtAdvanceTo");
            String tempAmount = request.getParameter("txtTempAmount");
            String permAmount = request.getParameter("txtPermAmount");
            String amount = request.getParameter("txtAmount");
            

            Date appDate = new Date();
            try {
                appDate = DateUtility.getDateFromString(strAppDate, ConfigReader.DATE_FORMAT);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            Long numberOfAdvance = getAdvancePaymentService().checkNumberOfAdvanceForEmployee(Long.parseLong(employeeId));

            if(numberOfAdvance >= ConfigReader.NUMBER_OF_APPROVAL_USER_LEVEL){
                view = new ModelAndView("message");
                view.addObject("message", "Maximum Number of Advance Payment Exceeded for the Specified Staff.");
                return view;
            }

            advance.setAdvanceTo(advanceTo);
            advance.setFormNumber(formNumber);
            advance.setApplicationDate(appDate);
            advance.setEmployeeId(Long.parseLong(employeeId));
            advance.setPurpose(purpose);
            advance.setCurrentAdvance_Temp_Amount(Float.parseFloat(tempAmount));
            advance.setCurrentAdvance_Permanent_Amount(Float.parseFloat(permAmount));
            advance.setAmount(Float.parseFloat(amount));

            boolean saved = advancePaymentService.updateAdvancePayment(advance, setupService.MODE_INSERT);
            if(saved == false){
                view = new ModelAndView("error");
                view.addObject("message", "Can not save advance payment information. Please try again later");
                return view;
            }

            view.addObject("message", "Record Saved Successfully.");

        }else if(operation.trim().equals("E")){
            AdvanceCollection advance = new AdvanceCollection();
            String formNumber = request.getParameter("txtForm");
            String strAppDate = request.getParameter("txtDate");
            String employeeId = request.getParameter("cbEmployee");
            String purpose = request.getParameter("txtPurpose");
            String advanceTo = request.getParameter("txtAdvanceTo");
            String tempAmount = request.getParameter("txtTempAmount");
            String permAmount = request.getParameter("txtPermAmount");
            String amount = request.getParameter("txtAmount");

            String id = request.getParameter("txtId");


            Date appDate = new Date();
            try {
                appDate = DateUtility.getDateFromString(strAppDate, ConfigReader.DATE_FORMAT);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            advance.setAdvanceTo(advanceTo);
            advance.setFormNumber(formNumber);
            advance.setApplicationDate(appDate);
            advance.setEmployeeId(Long.parseLong(employeeId));
            advance.setPurpose(purpose);
            advance.setCurrentAdvance_Temp_Amount(Float.parseFloat(tempAmount));
            advance.setCurrentAdvance_Permanent_Amount(Float.parseFloat(permAmount));
            advance.setAmount(Float.parseFloat(amount));
            advance.setId(Long.parseLong(id));

            boolean saved = advancePaymentService.updateAdvancePayment(advance, setupService.MODE_UPDATE);
            if(saved == false){
                view = new ModelAndView("error");
                view.addObject("message", "Can not modify advance payment information. Please try again later");
                return view;
            }

            view.addObject("message", "Record Saved Successfully.");
        }
        return view;
    }

    public ModelAndView processAdvancePaymentCloseRequest(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("message");
        String operation = request.getParameter("txtMode");
        //operation mode: N= New, E=Edit
        if(operation.trim().equals("N")){
            AdvanceCollection advance = new AdvanceCollection();
            String formNumber = request.getParameter("txtForm");
            String advanceId = request.getParameter("txtId");
            String totalAmount = request.getParameter("txtExpenses");
            String lessAmount = request.getParameter("txtLessAmt");


            Date appDate = new Date();
            
            advance.setFormNumber(formNumber);
            advance.setApplicationDate(appDate);
            advance.setId(Long.parseLong(advanceId));
            advance.setLessExpenses(Float.parseFloat(lessAmount));
            advance.setTotalExpenses(Float.parseFloat(totalAmount));

            boolean saved = advancePaymentService.updateCloseAdvancePayment(advance, setupService.MODE_INSERT);
            if(saved == false){
                view = new ModelAndView("error");
                view.addObject("message", "Can not save advance payment information. Please try again later");
                return view;
            }

            view.addObject("message", "Record Saved Successfully.");

        }else if(operation.trim().equals("E")){
            AdvanceCollection advance = new AdvanceCollection();
            String formNumber = request.getParameter("txtForm");
            String advanceId = request.getParameter("txtAdvanceId");
            String totalAmount = request.getParameter("txtExpenses");
            String lessAmount = request.getParameter("txtLessAmt");

            String id = request.getParameter("txtAdvanceId");


            Date appDate = new Date();
            
            advance.setFormNumber(formNumber);
            advance.setApplicationDate(appDate);
            advance.setId(Long.parseLong(advanceId));
            advance.setLessExpenses(Float.parseFloat(lessAmount));
            advance.setTotalExpenses(Float.parseFloat(totalAmount));

            boolean saved = advancePaymentService.updateCloseAdvancePayment(advance, setupService.MODE_UPDATE);
            if(saved == false){
                view = new ModelAndView("error");
                view.addObject("message", "Can not modify advance payment information. Please try again later");
                return view;
            }

            view.addObject("message", "Record Saved Successfully.");
        }
        return view;
    }

    /**
     * @return the setupService
     */
    public SetupService getSetupService() {
        return setupService;
    }

    /**
     * @param setupService the setupService to set
     */
    public void setSetupService(SetupService setupService) {
        this.setupService = setupService;
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

    /**
     * @return the advancePaymentService
     */
    public AdvancePaymentService getAdvancePaymentService() {
        return advancePaymentService;
    }

    /**
     * @param advancePaymentService the advancePaymentService to set
     */
    public void setAdvancePaymentService(AdvancePaymentService advancePaymentService) {
        this.advancePaymentService = advancePaymentService;
    }

}
