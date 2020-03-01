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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tenece.ap.constant.AuditOperationCodeConstant;
import org.tenece.ap.security.ActiveDirectory;
import org.tenece.ap.security.SecurityEncoderImpl;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Key;
import org.tenece.web.data.Lock;
import org.tenece.web.data.Users;
import org.tenece.web.filter.ApplicationFilter;
import org.tenece.web.services.EmployeeService;
import org.tenece.web.services.LoginService;
import org.tenece.web.services.UserService;

/**
 * Tenece Professional Services, Nigeria
 * @author strategiex
 */
public class LoginController extends BaseController {

    private LoginService loginService;
    private UserService userService;
    private EmployeeService employeeService;

    /** Creates a new instance of LoginController */
    public LoginController() {
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ModelAndView showLogin(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("login");

        String message = request.getParameter("message");
        view.addObject("xmessage", message);
        //getServletContext().getRealPath("/");
        //System.out.println(">>>>>>>>>>>>>>" + request.getSession().getServletContext().getRealPath("/WEB-INF/jsp/index.jsp"));

        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>" + request.getSession().getServletContext().getResourcePaths("/WEB-INF"));
        //System.out.println("*****Real Path:" + request.getSession().getServletContext().getRealPath("/"));
        //get session and kill all values
        HttpSession session = request.getSession(true);
        session.setAttribute("userPrincipal", null);

        return view;
    }

    public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //get all parameters from login page
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String locale = request.getParameter("locale");


        //validate user...against local db

        Users user = null;
        try {
            user = loginService.validateUser(request, userName, password);
        } catch (Exception ex) {
            ModelAndView view = null;
            view = showLogin(request, response);
            createNewAuditTrail(request, "Unsuccesfully Login.[" + userName + "]", AuditOperationCodeConstant.Unsuccessful_User_Login);
            view.addObject("message", "Failed Login. Contact your Administrator ");
            return view;
        }
        //check if user is valid
        if (user == null || password.equals("") ) {//user is invalid

            ModelAndView view = null;
            view = showLogin(request, response);
            //view.addObject("j_ct", "1");
            createNewAuditTrail(request, "Unsuccesfully Login.[" + userName + "]", AuditOperationCodeConstant.Unsuccessful_User_Login);
            view.addObject("message", "Failed Login. Contact your Administrator ");
            return view;
        } else {//valid user
            try {
// <<<<<<<<<<<<<<<<<<<<<<<comment out in order to login without AD authentication>>>>>>>>>>>>>>>>>>>>>>>>>
//                long loginCount = user.getNumberLogins();
//                if (loginCount >= 3) {
//
//                    ModelAndView view = showLogin(request, response);
//                    createNewAuditTrail(request, "Unsuccesfully Login.[" + userName + "]", AuditOperationCodeConstant.Unsuccessful_User_Login);
//                    view.addObject("message", "User account is locked. Contact System Administrator for Assistance.");
//                    return view;
//
//                }
//                Properties actConfig = ConfigReader.getActiveDirectoryConfig();
//                String servername = actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_LOCATION);
//                String domainName = actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_DOMAIN);
//                LdapContext ctx = ActiveDirectory.getConnection(userName, password, domainName, servername);
//               /// get session object
//                HttpSession session = request.getSession(true);
//
//               // assign values to session as attribute
//                session.setAttribute("password", password);
//                session.setAttribute("userName", userName);
//                session.setAttribute("empName", user.getFullnames());
//
//                ctx.close();

//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<end of comment<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            } catch (Exception e) {
                ModelAndView view = null;
                String errorMessage = "Failed Login";
                //if numlogin is 3 or greater, lock user
                System.out.println("---------LDAP AUTHENTICATION ERROR-------------");
                e.printStackTrace();
                if ((user.getNumberLogins() + 1) >= 3) {
                    //lock user
                    Lock lock = new Lock();
                    lock.setActive("A");
                    lock.setDateLocked(new Date());
                    lock.setLockedBy("0");
                    lock.setReason("User Login Tries Exceed Limit.");
                    lock.setUserId(user.getId());

                    userService.lockUser(lock);

                    //increase lock
                    user.setNumberLogins(user.getNumberLogins() + 1);
                    userService.updateUsersLoginTries(user);
                    //thorw exception with appropriate message
                    if (true) {
                        createNewAuditTrail(request, "User account is locked.[" + user.getUserName() + "]", AuditOperationCodeConstant.lock_User_Account);
                        // errorMessage = "Your account is locked. There was an attempt to access this account.";
                    }
                } else {
                    user.setNumberLogins(user.getNumberLogins() + 1);
                    userService.updateUsersLoginTries(user);
                }
                view = showLogin(request, response);
                createNewAuditTrail(request, "Unsuccesfully Login.[" + userName + "]", AuditOperationCodeConstant.Unsuccessful_User_Login);
                view.addObject("message", errorMessage);
                return view;
            }

            Users userPrincipal = (Users) user;
            //get session object
            HttpSession session = request.getSession(true);
            session.setAttribute("activityDate", new java.util.Date());

            //assign values to session as attribute
            session.setAttribute("userPrincipal", userPrincipal);
            session.setAttribute("userlang", locale);

            boolean updatedDate = loginService.updateLoginDateforUser(user);
            System.out.println(">>>>>>>>>>>Updated Login Date:" + updatedDate);
            ConfigReader.addUserSession(user.getUserName(), session.getId());
            //create audit
            String operation = Key.LOGIN_OPERATION + " by [" + user.getUserName() + "]";
            try {
                createNewAuditTrail(request, operation, AuditOperationCodeConstant.Successful_User_Login);
            } catch (Exception e) {
                //e.printStackTrace();
            }

            try {
                //build locale object
                Locale defLocale = new Locale(locale);
                //set local in session and in jstl configwelcome
                Config.set(session, Config.FMT_LOCALE, defLocale);
            } catch (Exception er) {
            }

        }
        return new ModelAndView(new RedirectView("default_welcome.html?"));

    }

    public ModelAndView welcomeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("welcome");

        return view;
    }

    public ModelAndView logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("logout");

        createNewAuditTrail(request, "Logout attempt was successful.", AuditOperationCodeConstant.Successful_User_Logout);
        //get user and delete the record
        Users user = this.getUserPrincipal(request);
        ConfigReader.deleteUserSession(user.getUserName());

        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
            session = null;
        }
        String message = "You have successfully signed out.";
        String key = request.getParameter("key");
        key = (key == null) ? "" : key;
        if (key.trim().equals("M")) {
            message = "Logged Out. You have logged in using another machine.";
        }
        view.addObject("message", message);
        return view;
    }
}
