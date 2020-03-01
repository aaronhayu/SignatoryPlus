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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tenece.web.data.AuditTrail;
import org.tenece.web.data.Company;
import org.tenece.web.data.EmailMessage;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Users;
import org.tenece.web.mail.EmailImpl;
import org.tenece.web.mail.SMTPMail;
import org.tenece.web.services.ApplicationService;
import org.tenece.web.services.EmployeeService;

/**
 *
 * @author strategiex
 */
public class BaseController extends MultiActionController {

    private ApplicationService applicationService;
    private EmployeeService employeeService;
    /**
     * Creates a new instance of BaseController
     */
    public BaseController() {
    }

    public String getActiveEmployeeCompanyCode(HttpServletRequest request){
        //get employee info and company code
        Employee activeEmployee = this.getEmployeeService().findEmployeeBasicById(this.getUserPrincipal(request).getEmployeeId());
        String companyCode = activeEmployee.getCompanyCode();
        return companyCode;
    }

    public ModelAndView getAndAppendCompanyToView(ModelAndView view, HttpServletRequest request) {

        return view;
    }
    
    /**
     * This method return an integer value. This will be used to determine if the user is ESS or Admin
     * Note:
     * 1=Admin
     * 0=ESS
     * @param request
     * @return
     */
    public int getUserLoginRole(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Users userPrincipal = null;
        try{
            userPrincipal = (Users) session.getAttribute("userPrincipal");
        }catch(Exception e){
            return 0;
        }
        return userPrincipal.getAdminUser();
    }

    public Users getUserPrincipal(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Users userPrincipal = null;
        try{
            userPrincipal = (Users) session.getAttribute("userPrincipal");
        }catch(Exception e){
            return null;
        }
        return userPrincipal;
    }

    public void createNewAuditTrail(HttpServletRequest request, String operation,String OperationCode){
        try{
            Users user = this.getUserPrincipal(request);
            String machineIdentity = request.getRemoteHost();
            Date accessDate = new Date();
            //create an instance of audit
            AuditTrail audit = new AuditTrail();
            audit.setAccessDate(accessDate);
            audit.setEmployeeId(user.getId());
            audit.setOperation(operation);
            audit.setMachineIdentity(machineIdentity);
            audit.setOperationCode(OperationCode);
            //save audit
            getApplicationService().createNewAuditTrail(audit);
        }catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static String getApplicationURL(HttpServletRequest req) {
        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath();   // /servlet/MyServlet

        // Reconstruct original requesting URL to get only Application Context
        String url = scheme+"://"+serverName+":"+serverPort+contextPath;
        //return built URL
        return url;
    }

    public static String getRequestedURL(HttpServletRequest req) {
        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath();   // /servlet/MyServlet
        String pathInfo = req.getPathInfo();         // /a/b;c=123
        String queryString = req.getQueryString();          // d=789

        // Reconstruct original requesting URL
        String url = scheme+"://"+serverName+":"+serverPort+contextPath+servletPath;
        if (pathInfo != null) {
            url += pathInfo;
        }
        if (queryString != null) {
            url += "?"+queryString;
        }
        return url;
    }


    public int sendEmail(String code, long receivingEmployeeId, Hashtable keyHolders){
        try{
            EmailMessage message = applicationService.findEmailMessageByCode(code);
            
            String messageBody = EmailImpl.formatEmailMessage(message.getMessage(), keyHolders);
            
            //return EmailImpl.sendEmail(employeeContact.getEmail(), message.getSenderEmail(), "", "", message.getSubject(),
            //        messageBody, new Vector());
            return 0;
        }catch(Exception e){
            //e.printStackTrace();
            return 0;
        }
    }
    

    /**
     * @return the applicationService
     */
    public ApplicationService getApplicationService() {
        return applicationService;
    }

    /**
     * @param applicationService the applicationService to set
     */
    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
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
