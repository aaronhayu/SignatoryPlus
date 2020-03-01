
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tenece.ap.constant.AuditOperationCodeConstant;
import org.tenece.ap.security.ActiveDirectory;
import org.tenece.ap.security.SecurityEncoderImpl;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.data.AuditTrail;
import org.tenece.web.data.Category;
import org.tenece.web.data.Employee;
import org.tenece.web.data.FileUpload;
import org.tenece.web.data.Lock;
import org.tenece.web.data.MenuData;
import org.tenece.web.data.Role;
import org.tenece.web.data.Users;
import org.tenece.web.filter.ApplicationFilter;
import org.tenece.web.services.EmployeeService;
import org.tenece.web.services.LoginService;
import org.tenece.web.services.MailService;
import org.tenece.web.services.SetupService;
import org.tenece.web.services.UploadService;
import org.tenece.web.services.UserService;

/**
 *
 * @author jeffry.amachree
 */
public class UserController extends BaseController {

    private UserService userService = new UserService();
    private EmployeeService employeeService = new EmployeeService();
    private LoginService loginService = new LoginService();
    private MailService mailService;
    private SetupService setupService = new SetupService();
    private UploadService uploadService = new UploadService();

    /** Creates a new instance of userController */
    public UserController() {
    }

    public ModelAndView viewPasswordRecall(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("password_recall");
        return view;
    }

    public ModelAndView savePasswordRecall(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("success_plain");
        String txtEmpNo = request.getParameter("txtNo");
        String txtUID = request.getParameter("txtName");
        String captchaText = request.getParameter("txtCaptcha");

        boolean validCaptcha = false;
        String sessionId = request.getSession().getId();
        try {
            validCaptcha = true;//getCaptchaService().validateResponseForID(sessionId, captchaText);
        } catch (Exception e) {
            //should not happen, may be thrown if the id is not valid
            view = viewPasswordRecall(request, response);
            view.addObject("message", "Unable to proceed with request, Code verification Error.");
            return view;
        }

        if (validCaptcha == false) {
            view = viewPasswordRecall(request, response);
            view.addObject("message", "Unable to proceed with request, Invalid verification code. Ensure you enter the exact text displayed on the image.");
            return view;
        }

        if (txtEmpNo == null || txtEmpNo.trim().equals("")) {
            view = viewPasswordRecall(request, response);
            view.addObject("message", "Unable to proceed with request, ensure that employee number is specified");
            return view;
        }
        //check if number is valid digit
        try {
            Long.parseLong(txtEmpNo);
        } catch (NumberFormatException e) {
            view = viewPasswordRecall(request, response);
            view.addObject("message", "Invalid Employee number, Only numbers are allowed.");
            return view;
        }
        if (txtUID == null || txtUID.trim().equals("")) {
            view = viewPasswordRecall(request, response);
            view.addObject("message", "To retrieve password, user name must be specified.");
            return view;
        }

        //use service to access data based on the combination
        Users user = this.userService.findUserByUserNameAndEmployeeID(txtUID, Long.parseLong(txtEmpNo));
        //if valid user, decrypt password
        if (user == null) {
            view = viewPasswordRecall(request, response);
            view.addObject("message", "Invalid User Name and Employee Number combination. Ensure that the user name is assigned to the employee with the number specified (" + txtEmpNo + ")");
            return view;
        }
        try {

            String decryptPassword = SecurityEncoderImpl.decryptPasswordWithAES(user.getPassword());
            //send mail to recipient
            int sent = mailService.sendPasswordRecall(Long.parseLong(txtEmpNo), decryptPassword, "");
            System.out.println(">>>>>>>>>>Sent Mail:" + sent);
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            view = viewPasswordRecall(request, response);
            view.addObject("message", "System error while processing password, contact your system administrator for assistance.");
            return view;
        }

        view.addObject("message", "Operation was successful, password has been sent to the registered email address.");
        return view;
    }

    public ModelAndView viewAllLockAccounts(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("users_locked_view");

        view.addObject("result", userService.findAllLockedUsers());
        createNewAuditTrail(request, "View all locked accounts", AuditOperationCodeConstant.View_all_locked_accounts);
        return view;
    }

    public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("users_view");
        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");
        List<Users> list = null;
        if (cbSearch == null || txtSearch == null) {
            list = userService.findAllUsersWithOutRole();
            //view.addObject("showCompany", "Y");
        } else {
            list = userService.findAllUsersOutRole(cbSearch, txtSearch);
            //view.addObject("showCompany", "Y");
        }


        view.addObject("showCompany", "Y");
        view.addObject("result", list);
        createNewAuditTrail(request, "View all system users.", AuditOperationCodeConstant.View_all_system_users);
        return view;
    }

    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("users_edit");

        String param = request.getParameter("userid");
        if (param == null) {
            return viewAll(request, response);
        }
        int id = Integer.parseInt(param);
        Users user = userService.findUsersById(id, id);
        view.addObject("showAll", "Y");
        view.addObject("user", user);
        Employee emp = employeeService.findEmployeeBasicById(user.getEmployeeId());
        if (emp != null) {
            view.addObject("fullName", emp.getFullName());
            view.addObject("employeeNumber", emp.getEmployeeNumber());
        } else {
            String fullName = employeeService.findUnAuthorisedSignatoryUserByUserName(user.getUserName());
            view.addObject("fullName", fullName);
            view.addObject("employeeNumber", 0);
        }
        view.addObject("roleList", userService.findRoles());
        view.addObject("userid", id);
        view.addObject("staffId", user.getUserName());
        view.addObject("pageTitle", "edit");
        createNewAuditTrail(request, "View System User: Record[" + user.toString() + "]", AuditOperationCodeConstant.View_System_User_Details);
        return view;
    }

    public ModelAndView prepareToDeleteUser(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("users_delete");

        String param = request.getParameter("userid");
        if (param == null) {
            return viewAll(request, response);
        }
        int id = Integer.parseInt(param);
        Users user = userService.findUsersById(id, id);
        view.addObject("showAll", "Y");
        view.addObject("user", user);
        Employee emp = employeeService.findEmployeeBasicById(user.getEmployeeId());
        if (emp != null) {
            view.addObject("fullName", emp.getFullName());
            view.addObject("employeeNumber", emp.getEmployeeNumber());
        } else {
            String fullName = employeeService.findUnAuthorisedSignatoryUserByUserName(user.getUserName());
            view.addObject("fullName", fullName);
            view.addObject("employeeNumber", 0);
        }
        view.addObject("roleList", userService.findRoles());
        view.addObject("userid", id);
        view.addObject("staffId", user.getUserName());
        view.addObject("pageTitle", "edit");
        createNewAuditTrail(request, "View System User: Record[" + user.toString() + "]", AuditOperationCodeConstant.View_System_User_Details);
        return view;
    }

    public ModelAndView editUserPassword(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("users_pwd_edit");
        String param = request.getParameter("idx");
        if (param == null) {
            return viewAll(request, response);
        }
        int id = Integer.parseInt(param);
        Users user = userService.findUsersById(id, id);
        System.out.println(param + ">>>>" + user);
        view.addObject("showAll", "Y");
        view.addObject("user", user);
        // view.addObject("employee", employeeService.findEmployeeBasicById(user.getEmployeeId()));

        createNewAuditTrail(request, "View System User To Change Password: Record[" + user.toString() + "]", AuditOperationCodeConstant.View_Edit_System_User);
        return view;
    }

    public ModelAndView editLock(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("users_locked_edit");
        String param = request.getParameter("idx");
        if (param == null) {
            return viewAll(request, response);
        }
        int id = Integer.parseInt(param);
        Users user = userService.findUsersById(id, id);
        view.addObject("user", user);
        //view.addObject("employee", employeeService.findEmployeeBasicById(user.getEmployeeId()));
        view.addObject("lock", getUserService().findLockById(id));
        createNewAuditTrail(request, "Viewing Locked User Account:[" + user.toString() + "]", AuditOperationCodeConstant.View_Edit_Locked_Account);
        return view;
    }

    public ModelAndView newRecord(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("users_edit");

        String cbCompany = request.getParameter("cbCompany");
        String employeeNumber = request.getParameter("employeeNumber");
        String fullName = request.getParameter("fullName");
        String staffId = request.getParameter("staffId");

        //get parameter to know which employee was selected
        String strEmpID = request.getParameter("cbEmp");
        Employee employee = new Employee();
        if (strEmpID != null && (!strEmpID.trim().equals(""))) {
            if (Integer.parseInt(strEmpID) > 0) {
                employee = employeeService.findEmployeeBasicById(Long.parseLong(strEmpID));

                Users _user = new Users();
                _user.setEmployeeId(Integer.parseInt(strEmpID));
                _user.setUserName(String.valueOf(employee.getEmployeeID()));
                view.addObject("user", _user);
                view.addObject("showAll", "Y");
            } else {
                view.addObject("showAll", "N");
            }
        } else {
            view.addObject("showAll", "N");
        }

        view.addObject("roleList", userService.findRoles());
        view.addObject("employeeNumber", employeeNumber);
        view.addObject("fullName", fullName);
        view.addObject("staffId", staffId);
        view.addObject("pageTitle", "new");
        view = getAndAppendCompanyToView(view, request);
        createNewAuditTrail(request, "View New System User Interface: Record", AuditOperationCodeConstant.View_New_System_User_Page);
        return view;
    }

    public ModelAndView changePassword(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("change_password");

        Users user = this.getUserPrincipal(request);
        view.addObject("user", user);
        createNewAuditTrail(request, "View User Change Password", AuditOperationCodeConstant.View_Change_Password_Page);
        return view;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("");
        List<Integer> ids = new ArrayList<Integer>();
        //get mode of request
        String mode = request.getParameter("mode");
        String userName = request.getParameter("userName");
        String txtId = request.getParameter("txtId");
        Users user = userService.findUsersById(Integer.parseInt(txtId), Integer.parseInt(txtId));
        Users initiator = this.getUserPrincipal(request);
        long transactionID = 0L;
        try {
            transactionID = processTransactionForUserDelete(user, initiator.getId());
        } catch (Exception ac) {
            view = new ModelAndView("error");
            view.addObject("message", ac.getMessage());
            return view;

        }

        if (transactionID == 0L) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to complete transaction request.");
            return view;
        }
        long nextApprover = userService.findNextApprovingOfficer(transactionID);
        Users supervisor = userService.findUsersById((int) nextApprover, (int) nextApprover);
        view = new ModelAndView("success");
        view.addObject("message", "The User Deletion has been forwarded to " + supervisor.getFullnames() + " for approval.");
        //check if mode is for more than one record
        //get mode, if mode is equals 1, then process for multiple
//        if (mode != null && mode.trim().equals("1")) {
//            String[] args = request.getParameterValues("_chk");
//            for (String id : args) {
//                ids.add(Integer.parseInt(id));
//            }
//        } else {//then it is zero
//            ids.add(Integer.parseInt(request.getParameter("id")));
//        }
//        for (int i : ids) {
//            userService.deleteUsers(i, userName);
//        }
        createNewAuditTrail(request, "Deleted System User: Record[" + ids.toString() + "]", AuditOperationCodeConstant.Delete_System_User);
        //delete
        //return viewAll(request, response);
        return view;
    }

    public ModelAndView processChangePasswordRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("message");

        Users user = this.getUserPrincipal(request);
        //get parameters
        String pwd1 = request.getParameter("txtPwd");
        String newPwd = request.getParameter("txtPwd2");
        String newPwd2 = request.getParameter("txtPwd3");

        if (newPwd == null || newPwd2 == null || pwd1 == null
                || newPwd.trim().equals("")) {
            view = new ModelAndView("error");
            view.addObject("message", "Invalid Data, Try again.");
            return view;
        }
        //confirm if the two new pwd is correct
        if (!newPwd.equals(newPwd2)) {
            view = new ModelAndView("error");
            view.addObject("message", "New Passwords does not match.");
            return view;
        }
        boolean isValidPassword = ConfigReader.validatePasswordPolicy(newPwd, 8);
        if (!isValidPassword) {
            view = new ModelAndView("error");
            view.addObject("message", "Password must include atleast 1 special character and can not be less than 8 aphanumeric characters.");
            return view;
        }
        //confirm if old password is valid
        Users _user = loginService.validateUser(request, user.getUserName(), pwd1);
        if (_user == null) {
            view = new ModelAndView("error");
            createNewAuditTrail(request, "Change Password Error Due to Invalid Active Password:[User Name=" + user.getUserName() + "]", AuditOperationCodeConstant.Error_Due_To_Invalid_Active_Password);
            view.addObject("message", "Current/Active password is invalid. User account will be locked after several attempt");
            return view;
        }

        //encrypt
        String strPwd = "";
        try {
            strPwd = SecurityEncoderImpl.encryptPasswordWithAES(newPwd2);
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> passwordHistoryList = loginService.findLast12Passwords(_user.getId());
        System.out.println("passwordHistoryList size" + passwordHistoryList.size());
        if (!passwordHistoryList.isEmpty() || passwordHistoryList.size() > 0) {
            for (String password : passwordHistoryList) {


                if (password.equals(strPwd)) {
                    view.addObject("message", "Use another Password because the typed password was used in the last 12 password change");
                    return view;

                }
            }
        }
        int row = loginService.updatePassword(_user.getId(), strPwd);
        if (row == 0) {
            createNewAuditTrail(request, "Unable to Change User Login Information:[" + _user.getUserName() + "]", AuditOperationCodeConstant.Unable_To_Change_User_Login_Info);
            view = new ModelAndView("error");
            view.addObject("message", "Unable to change user login information");
            return view;
        }
        //update user login date to remove first login
        try {
            boolean updated = loginService.updateLoginDateforUser(_user);
            System.out.println("Login Updated: " + updated);
        } catch (Exception er) {
            er.printStackTrace();
        }
        //insert to Password History
        try {
            boolean created = loginService.createPasswordHistory(_user.getId(), strPwd);
            System.out.println("Password History Created: " + created);
        } catch (Exception er) {
            er.printStackTrace();
        }
        //getUserDAO().updateUserLoginInfo(user.getId());
        //send mail
        try {
            int sent = mailService.sendChangePassword(_user, newPwd2, "");
        } catch (Exception e) {
        }
        //
        Users newUser = new Users();
        newUser.setPassword(strPwd);
        newUser.setUserName(_user.getUserName());
        newUser.setFullnames(_user.getFullnames());
        newUser.setId(_user.getId());
        newUser.setActive(_user.getActive());
        createNewAuditTrail(request, "System User Changed Password: Record[" + newUser.toStringForPassword() + "]Old Password: Record[" + _user.toStringForPassword() + " ]", AuditOperationCodeConstant.Change_System_User_Password);
        view.addObject("message", "Password was changed successfully.");
        return view;
    }

    public ModelAndView processPasswordRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("message");

        Users user = this.getUserPrincipal(request);
        //get parameters
        String pwd1 = request.getParameter("txtPwd");
        String newPwd = request.getParameter("txtPwd2");

        //confirm if the two new pwd is correct
        if (!newPwd.equals(newPwd)) {
            view = new ModelAndView("error");
            view.addObject("message", "New Password does not match with confirmation password.");
            return view;
        }
        boolean isValidPassword = ConfigReader.validatePasswordPolicy(newPwd, 8);
        if (!isValidPassword) {
            view = new ModelAndView("error");
            view.addObject("message", "Password must include atleast 1 special character and can not be less than 8 aphanumeric characters.");
            return view;
        }

        String id = request.getParameter("txtId");
        if (id == null || id.trim().equals("")) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to User information.");
            return view;
        }

        //encrypt
        String strPwd = "";
        try {
            strPwd = SecurityEncoderImpl.encryptPasswordWithAES(newPwd);
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Users _user = userService.findUsersById(Integer.parseInt(id), Integer.parseInt(id));

        int row = loginService.resetPassword(_user.getId(), strPwd);
        if (row == 0) {
            createNewAuditTrail(request, "Unable to Change User Login Information:[" + _user.getUserName() + "]", AuditOperationCodeConstant.Unable_To_Change_User_Login_Info);
            view = new ModelAndView("error");
            view.addObject("message", "Unable to change user login information");
            return view;
        }
        //send mail
        try {
            int sent = mailService.sendChangePassword(_user, newPwd, "");
        } catch (Exception e) {
        }
        user.setPassword(strPwd);
        createNewAuditTrail(request, "System User Changed Password: Record[" + user.toStringForPassword() + "]", AuditOperationCodeConstant.Change_System_User_Password);
        view.addObject("message", "Password was changed successfully.");
        return view;
    }

    public ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("******************** About to save new User account...");
        ModelAndView view = new ModelAndView("message");
        String operation = request.getParameter("txtMode");
        //operation mode: N= New, E=Edit
        if (operation.trim().equals("N")) {
            System.out.println("INSERT 1");
            Users user = new Users();
            //String empId = request.getParameter("txtEmp");
            String uid = request.getParameter("txtUserName");
            String role = request.getParameter("cbRole");
            String superAdmin = request.getParameter("chkSuper");
            String status = request.getParameter("cbStatus");
//            String pwd = String.valueOf(request.getParameter("txtPwd"));
//            String pwd2 = String.valueOf(request.getParameter("txtPwd2"));
            String employNo = request.getParameter("txtEmployeeNumber");
            String txtStaffId = request.getParameter("txtStaffId");
            String txtStaffId2 = request.getParameter("txtStaffId2");
            String fullName = request.getParameter("txtFullName");
            //invalid password

            //invalid password
//            if((!pwd.trim().equals(pwd2)) || pwd.trim().equals("null")){
//                return newRecord(request, response);
//            }

//            boolean isValidPassword = ConfigReader.validatePasswordPolicy(pwd, 8);
//            if(!isValidPassword){
//                view = new ModelAndView("error");
//                view.addObject("message", "Password must include atleast 1 special character and can not be less than 8 aphanumeric characters.");
//                return view;
//            }

//            String strPwd = "";
//            try {
//                strPwd = SecurityEncoderImpl.encryptPasswordWithAES(pwd);
//            } catch (Exception ex) {
//                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
//            }

            if (superAdmin == null) {
                superAdmin = "0";
            }
            //user.setEmployeeId(Integer.parseInt(empId));
            if (!txtStaffId.equals(null)) {
                user.setUserName(txtStaffId);
            }
            if (!txtStaffId2.equals(null)) {
                user.setUserName(txtStaffId2);
            }
            if (!uid.equals(null)) {
                user.setUserName(uid);
            }

            //user.setPassword(strPwd);
            user.setNumberLogins(0);
            user.setActive(status);
            user.setRoleId((Integer.parseInt(role)));
            String roleName = userService.findRoleNameByRoleId((Integer.parseInt(role)));
            user.setRoleName(roleName);
            user.setDateUpdated(new Date());
            user.setDateSignup(new Date());
            user.setLastLoginDate(new Date());
            int empId = 0;
            if (employNo != null && (!employNo.trim().equals(""))) {

                empId = Integer.parseInt(employNo);
                Employee employee = employeeService.findEmployeeBasicById(empId);
                user.setFullnames(employee.getFullName());
                user.setEmployeeId(empId);
            } else {
                user.setFullnames(fullName);
            }
            System.out.println("INSERT 2");
            //boolean saved = userService.updateUsers(user, userService.MODE_INSERT);
            Users initiator = this.getUserPrincipal(request);
            long transactionID = 0L;
            try {
                transactionID = processTransaction(user, initiator.getId());
            } catch (Exception ac) {
                view = new ModelAndView("error");
                view.addObject("message", ac.getMessage());
                return view;

            }

            if (transactionID == 0L) {
                view = new ModelAndView("error");
                view.addObject("message", "Unable to complete transaction request.");
                return view;
            }
            long nextApprover = userService.findNextApprovingOfficer(transactionID);
            Users supervisor = userService.findUsersById((int) nextApprover, (int) nextApprover);
            view = new ModelAndView("success");
            view.addObject("message", "The User Creation has been forwarded to " + supervisor.getFullnames() + " for approval.");
//            if(saved == false){
//                createNewAuditTrail(request, "Unable to Create New user:[" + user.toString() + "]",AuditOperationCodeConstant.Unable_To_Create_New_User);
//                view = new ModelAndView("error");
//                view.addObject("message", "Error processing user setup request. Please try again later");
//                return view;
//            }

            //send mail
//            try{
//                Employee employee = getEmployeeService().findEmployeeBasicById(user.getEmployeeId());
//                Hashtable keys = new Hashtable();
//                keys.put("EMAIL_USERNAME", user.getUserName());
//                keys.put("EMAIL_NAME", employee.getFullName());
//                keys.put("EMAIL_PASSWORD", pwd);
//
//                int sent = mailService.sendEmail("SIGNIN", user.getEmployeeId(), keys);
//                System.out.println("mail sent: " + sent);
//            }catch(Exception e){}

            createNewAuditTrail(request, "New System User Created: Record[" + user.toString() + "]", AuditOperationCodeConstant.Create_New_System_User);

            //   view = new ModelAndView("success");
            // view.addObject("message", "Record Saved Successfully.");

        } else if (operation.trim().equals("E")) {
            Users user = new Users();
            String empId = request.getParameter("txtEmp");
            String uid = request.getParameter("txtUserName");
            String role = request.getParameter("cbRole");
            String superAdmin = request.getParameter("chkSuper");
            String status = request.getParameter("cbStatus");
            String txtStaffId = request.getParameter("txtStaffId");
            String txtStaffId2 = request.getParameter("txtStaffId2");
            String id = request.getParameter("txtId");
            if (id == null || id.trim().equals("")) {
                view = new ModelAndView("error");
                view.addObject("message", "Unable to User information.");
                return view;
            }

            System.out.println("EDIT 1");
            if (superAdmin == null) {
                superAdmin = "0";
            }
            user.setId(Integer.parseInt(id));
            user.setEmployeeId(Integer.parseInt(empId));
            if (txtStaffId != null) {
                user.setUserName(txtStaffId);
            }
            if (txtStaffId2 != null) {
                user.setUserName(txtStaffId2);
            }
            user.setNumberLogins(0);
            user.setActive(status);
            user.setAdminUser(Integer.parseInt(role));
            user.setSuperAdmin(Integer.parseInt(superAdmin));
            user.setRoleId((Integer.parseInt(role)));
            String roleName = userService.findRoleNameByRoleId((Integer.parseInt(role)));
            user.setRoleName(roleName);
            user.setDateUpdated(new Date());
            user.setDateSignup(new Date());
            user.setLastLoginDate(new Date());
            user.setRoleId((Integer.parseInt(role)));
            String employNo = request.getParameter("txtEmployeeNumber");
            Users previousUser = userService.findUsersById(Integer.parseInt(id), Integer.parseInt(id));
            int empId2 = 0;
            if (employNo != null && (!employNo.trim().equals(""))) {

                empId2 = Integer.parseInt(employNo);
                user.setEmployeeId(empId2);
                Employee employee = employeeService.findEmployeeBasicById(empId2);
                user.setFullnames(employee.getFullName());
                previousUser.setFullnames(user.getFullnames());
            }


            boolean saved = userService.updateUsers(user, userService.MODE_UPDATE);
            if (saved == false) {
                createNewAuditTrail(request, "Unable to Modify Existing System User:[" + user.toString() + "]", AuditOperationCodeConstant.Unable_to_Modify_Existing_System_User);
                view = new ModelAndView("error");
                view.addObject("message", "Error processing user setup request. Please try again later");
                return view;
            }
            createNewAuditTrail(request, "System User Information Changed: Old Record[" + previousUser.toString() + "],    New Record[" + user.toString() + "]", AuditOperationCodeConstant.Modify_System_User_Information);

            view = new ModelAndView("success");
            view.addObject("message", "Record Saved Successfully.");
        }
        return view;
    }

    public ModelAndView processUnlockRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("message");
        //user id
        String id = request.getParameter("txtId");

        if (id == null || id.trim().equals("")) {
            view = new ModelAndView("error");
        }
        Lock lock = new Lock();
        lock.setActive("D");
        lock.setUserId(Long.parseLong(id));

        int saved = userService.deactivateLockOnUser(lock);
        if (saved == 0) {
            createNewAuditTrail(request, "Unable to Unlock User Account", AuditOperationCodeConstant.Unable_to_Unlock_User_Account);
            view = new ModelAndView("error");
            view.addObject("message", "Error Unlocking user account. Please try again later");
            return view;
        }

        String chkMail = request.getParameter("chkNotify");
        if (chkMail != null && chkMail.trim().equals("1")) {
            //send mail
            try {
            } catch (Exception e) {
            }
        }

        createNewAuditTrail(request, "User Account is unlocked", AuditOperationCodeConstant.Unlock_User_Account);
        view.addObject("message", "User Account is unlocked.");

        return view;
    }

    public ModelAndView viewArchiveLogSummary(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("archive_summary");

        AuditTrail audit = getUserService().getAuditSummary();
        Users user = this.getUserPrincipal(request);
        view.addObject("user", user);
        view.addObject("audit", audit);
        //createNewAuditTrail(request, "Click Archive Audit Trail: Record[" + user.toString() + "]");
        return view;
    }

    public ModelAndView archiveLog(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("success");
        System.out.println(">>>>>>>>>>Attempting to archive audit trail >>>>>>>>>>>");
        String strTotal = request.getParameter("txtHId");

        System.out.println(">>>>>>>>>>" + strTotal);
        boolean archived = getUserService().archiveAuditLog();
        System.out.println(">>>>>>>>>>" + archived);
        if (archived == false) {
            view = viewArchiveLogSummary(request, response);
            return view;
        }
        view.addObject("message", "Audit Trail Archived Successfully.");
        //craete an audit trail
        // createNewAuditTrail(request, "Archived Audit Trail (All Records).");
        return view;
    }

    /**
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
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
     * @return the loginService
     */
    public LoginService getLoginService() {
        return loginService;
    }

    /**
     * @param loginService the loginService to set
     */
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * @return the mailService
     */
    public MailService getMailService() {
        return mailService;
    }

    /**
     * @param mailService the mailService to set
     */
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public ModelAndView ViewCategories(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_category");

        view.addObject("result", setupService.findAllCategoryList());
        createNewAuditTrail(request, "View all Categories ", AuditOperationCodeConstant.View_Employees_Category);
        return view;
    }

    public ModelAndView EditCategory(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("edit_category");
        String key = request.getParameter("key");
        view.addObject("category", setupService.findCategoryByCode(key));
        ArrayList<Category> list = new ArrayList<Category>();
        Category cat1 = new Category();
        cat1.setKey("A");
        cat1.setDescription("Category A");
        Category cat2 = new Category();
        cat2.setKey("B");
        cat2.setDescription("Category B");
        list.add(cat1);
        list.add(cat2);
        view.addObject("categoryList", list);
        createNewAuditTrail(request, "Edit Category", AuditOperationCodeConstant.View_Edit_Employee_Category);
        return view;
    }

    public ModelAndView UpdateCategory(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("message");



        Category cat = new Category();
        String key = request.getParameter("txtKey");
        String amount = request.getParameter("txtAmount");
        String prevAmount = request.getParameter("txtPrevAmount");



        if (key == null || key.trim().equals("")) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to View the Edit Page.");
            return view;
        }
        cat.setKey(key);
        cat.setTransactionLimt(Float.parseFloat(amount));


        boolean saved = getUserService().updateCategory(cat);
        if (saved == false) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to modify existing Category.");
            return view;
        }

        createNewAuditTrail(request, "Modified Category: Old Record=[" + prevAmount + "], New Record[" + amount + "]", AuditOperationCodeConstant.Modify_Employee_Category);

        view.addObject("message", "Category Modified Successfully.");

        return view;
    }

    public ModelAndView viewEmployeeForCreateUser(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_view_for_user_create");

        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");


        List<Employee> list = null;
        if (cbSearch == null || txtSearch == null) {
            list = getEmployeeService().findAllEmployeeBasicForUserCreate();
            //view.addObject("showCompany", "Y");
        } else {
            list = getEmployeeService().findAllEmployeeBasicForUserCreate(cbSearch, txtSearch);
            //view.addObject("showCompany", "Y");
        }

        //createNewAuditTrail(request, "View All Employee");
        view.addObject("result", list);


        return view;
    }

    public ModelAndView ViewAllRoleAndMenu(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("role_view");

        List<Role> list = getUserService().findRoles();
        List<MenuData> menuAllList = getUserService().findAllMenuItems();
        view.addObject("roleList", list);
        view.addObject("menuAllList", menuAllList);
        createNewAuditTrail(request, "View All Role And Menu", AuditOperationCodeConstant.View_All_Role_And_Menu);

        return view;
    }

    public ModelAndView searchMenuAssignToRole(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("role_view");

        List<Role> list = getUserService().findRoles();
        List<MenuData> menuAllList = getUserService().findAllMenuItems();
        view.addObject("roleList", list);
        view.addObject("menuAllList", menuAllList);
        String cbRole = request.getParameter("roleid");
        List<MenuData> menulist = null;
        int roleId = 0;
        if (cbRole == null || cbRole.trim().equals("")) {
            view.addObject("roleId", roleId);
            return view;
        } else {
            roleId = Integer.parseInt(cbRole);
            menulist = getUserService().findAllMenuItems(roleId);
            //view.addObject("showCompany", "Y");
        }
        createNewAuditTrail(request, "View All Role And Menu", AuditOperationCodeConstant.View_All_Role_And_Menu);
        view.addObject("roleId", roleId);
        view.addObject("result", menulist);
        return view;
    }

    public ModelAndView saveRole(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("role_view");

        String txtRole = request.getParameter("txtRole");
        String roleName = getUserService().findRoleNameByRoleName(txtRole);


        if (roleName == null) {
            int saved = getUserService().saveRole(txtRole);
            if (saved == 0) {
                createNewAuditTrail(request, "Unable to Save Role Information:[" + txtRole + "]", AuditOperationCodeConstant.Unable_to_Save_Role_Information);
                view = new ModelAndView("error");
                view.addObject("message", "Unable to Save Role information");
                return view;
            }
            List<Role> list = getUserService().findRoles();
            createNewAuditTrail(request, "Save Role Information:[" + txtRole + "]", AuditOperationCodeConstant.Save_Role_Information);
            view.addObject("roleList", list);
            view.addObject("saved", "Role Entered has been added. Select the role dropdown below to see it");
            return view;
        } else {
            view = new ModelAndView("error");
            view.addObject("message", "The Role Name Entered already Exist");
            return view;
        }
    }

    public ModelAndView saveAssignMenuToRole(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("role_view");
        System.out.print("<<<<<<<<Here>>>>>>>>>>>>>>>>>>>>>>>>" );
        String txtRoleId = request.getParameter("cbRole1");
        String cbMenu = request.getParameter("cbMenu");
        System.out.print("roleId = " + txtRoleId);

        int roleId = Integer.parseInt(txtRoleId);
        int menuId = Integer.parseInt(cbMenu);
        String roleName = getUserService().findRoleNameByRoleId(roleId);
        String menuName = getUserService().findMenuNameByMenuId(menuId);
        //
        //check if already assigned

        boolean found = getUserService().checkIFAssigned(roleId, menuId);
        if (found == true) {
            createNewAuditTrail(request, "Unable to Assign Menu to Role because selected menu item is already assigned: Role =[" + roleName + "] and menu=[" + menuName + "]", AuditOperationCodeConstant.Unable_to_Save_Assigned_Menu_To_Role_Info);
            view.addObject("message", "Unable to Assign Menu to Role because selected menu item is already assigned");
            List<Role> list = getUserService().findRoles();
            List<MenuData> menuAllList = getUserService().findAllMenuItems();
            view.addObject("roleList", list);
            view.addObject("menuAllList", menuAllList);
            return view;

        }
        boolean saved = getUserService().assignMenuToRole(roleId, menuId);
        if (saved == false) {
            createNewAuditTrail(request, "Unable to Save assigned Menu to  Role Information: Role =[" + roleName + "]and menu =[" + menuName + "]", AuditOperationCodeConstant.Unable_to_Save_Assigned_Menu_To_Role_Info);
            view = new ModelAndView("error");
            view.addObject("message", "Unable to Save assigned Menu to  Role Information");
            return view;
        }
        List<Role> list = getUserService().findRoles();
        List<MenuData> menuAllList = getUserService().findAllMenuItems();
        view.addObject("roleList", list);
        view.addObject("menuAllList", menuAllList);
        createNewAuditTrail(request, "Save assign Menu to  Role Information  Role =[" + roleName + "]and menu =[" + menuName + "]", AuditOperationCodeConstant.Save_Assigned_Menu_To_Role_Info);
        return view;
    }

    public ModelAndView uploadSystemUsers(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("user_upload");
        view.addObject("roleList", userService.findRoles());
        view = getAndAppendCompanyToView(view, request);
        //createNewAuditTrail(request, "View New System User Interface: Record");
        return view;
    }

    public ModelAndView processUploadRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView view = new ModelAndView("user_upload_view");
        //get user information
        Users user = this.getUserPrincipal(request);

        if (!(request instanceof MultipartHttpServletRequest)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
            return null;
        }
        String fileType = request.getParameter("cbFileType");
        String roleid = request.getParameter("cbRole");
        String roletext = request.getParameter("roletext");
        System.out.println("roletext="+roletext);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("txtFile");

        org.tenece.web.data.FileUpload fileUpload = new FileUpload();
        fileUpload.setFileName(file.getName());
        try {
            fileUpload.setBytes(file.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        fileUpload.setSize(file.getSize());
        fileUpload.setContentType(file.getContentType());
        fileUpload.setOrginalFileName(file.getOriginalFilename());

        //save file in archive
        File destination = new File("./" + file.getOriginalFilename());
        fileUpload.setAbsolutePath(destination.getAbsolutePath());
        try {
            file.transferTo(destination);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //process file
        try {
            List<Users> userlist = getUploadService().saveUploadedUserlistFile(fileUpload, fileType, roletext);
            HttpSession session = request.getSession(false);
            String strId = user.getEmployeeId() + "_" + org.tenece.web.common.DateUtility.getDateAsString(new Date(), "ddMMyyyyHHmmss");
            session.setAttribute(strId, userlist);

            System.out.println("Number of uploaded records: " + userlist.size());
            view.addObject("result", userlist);
            view.addObject("idx", strId);
            view.addObject("roleid", roleid);

        } catch (Exception e) {
            view = upload(request, response);
            view.addObject("message", e.getMessage());
        }

        return view;
    }

    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("user_upload");
        view.addObject("roleList", userService.findRoles());

        return view;
    }

    //
    public ModelAndView confirmUploadRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("success");
        //get session index
        String strId = request.getParameter("txtHIdx");
        String roleid = request.getParameter("roleid");

        //use index to get value from session
        HttpSession session = request.getSession(false);

        List<Users> userList = (List<Users>) session.getAttribute(strId);
        //check if value is null
        if (userList == null) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to confirm request. Ensure that the uploaded file data is valid.");
            return view;
        }
        //remove value from session
        session.removeAttribute(strId);

        //save bulk
        int roleId = Integer.parseInt(roleid);
        //boolean saved = uploadService.saveBulkUsersList(request, userList, roleId);
        for (Users user : userList) {
            user.setRoleId(roleId);
        }
        Users initiator = this.getUserPrincipal(request);
        long transactionID = 0L;
        try {
            transactionID = processBulkTransaction(userList, initiator.getId());
        } catch (Exception exp) {
            view = new ModelAndView("error");
            view.addObject("message", exp.getMessage());
            return view;
        }
        if (transactionID == 0L) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to complete transaction request.");
            return view;
        }
        long nextApprover = userService.findNextApprovingOfficer(transactionID);
        Users supervisor = userService.findUsersById((int) nextApprover, (int) nextApprover);
        view = new ModelAndView("success");
        view.addObject("message", "The User Creation has been forwarded to " + supervisor.getFullnames() + " for approval.");


        return view;
    }

    public ModelAndView getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("users_edit");
        String userNameToSearch = request.getParameter("username");
        try {

            Properties actConfig = ConfigReader.getActiveDirectoryConfig();
            String servername = actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_LOCATION);
            String domainName = actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_DOMAIN);
            HttpSession session = request.getSession(true);
            String loginPassword = (String) session.getAttribute("password");
            String loginUserName = (String) session.getAttribute("userName");
            LdapContext ctx = ActiveDirectory.getConnection(loginUserName, loginPassword, domainName, servername);
            Employee emp = ActiveDirectory.getUser(userNameToSearch, ctx);
            System.out.println("AD Username----------> :"+emp);
            String fullName = emp.getFullName();
            if (emp == null) {
                view.addObject("message", "Could not find the User in the System");
                return view;
            }

            //get session object
            view.addObject("roleList", userService.findRoles());
            view.addObject("fullName", fullName);

            view.addObject("pageTitle", "new");
            view.addObject("employeeNumber", 0);
            view.addObject("staffId", null);
            System.out.println("newStaffId= " + userNameToSearch);
            view.addObject("newStaffId", userNameToSearch);
            return view;
            //assign values to session as attribute
        } catch (Exception e) {
        }


        return view;
    }

    public ModelAndView deleteMenuInRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String menuInRoleId = request.getParameter("menuInRoleId");
        String roleid = request.getParameter("roleid");
        int saved = getUserService().deleteMenuInRole(Integer.parseInt(menuInRoleId));
        if (saved == 0) {
            createNewAuditTrail(request, "Unable to Delete Menu In Role", "");
            ModelAndView view = null;
            view = new ModelAndView("error");
            view.addObject("message", "Error Unlocking user account. Please try again later");
            return view;
        }
        return new ModelAndView(new RedirectView("view_role_for_user.html"));

    }

    public long processTransaction(Users user, int initiatorId) throws Exception {
        long transId = 0L;
        try {
            transId = userService.initiateTransaction(user, "User Creation", initiatorId, "USER_CREATION");
            return transId;
        } catch (Exception ac) {
            throw ac;

        }

    }

    public long processTransaction(Object object, String description, int initiatorId, String transactionCode) throws Exception {
        long transId = 0L;
        try {
            transId = userService.initiateTransaction(object, description, initiatorId, transactionCode);
            return transId;
        } catch (Exception ac) {
            throw ac;

        }
    }

    public long processBulkTransaction(List<Users> user, int initiatorId) throws Exception {
        long transId = 0L;
        try {
            transId = userService.initiateTransaction(user, "Bulk User Creation", initiatorId, "BULK_USER_CREATION");
            return transId;
        } catch (Exception exp) {
            throw exp;
        }

    }

    public long processBulkTransaction(Object object, String description, int initiatorId, String transactionCode) throws Exception {
        long transId = 0L;
        try {
            transId = userService.initiateTransaction(object, description, initiatorId, transactionCode);
            return transId;
        } catch (Exception exp) {
            throw exp;
        }
    }

    public long processTransactionForUserDelete(Users user, int initiatorId) throws Exception {
        long transId = 0L;
        try {
            transId = userService.initiateTransaction(user, "User Deletion", initiatorId, "USER_DELETION");
            return transId;
        } catch (Exception exp) {
            throw exp;
        }

    }

    public long processTransactionForUserDelete(Object object, String description, int initiatorId, String transactionCode) throws Exception {
        long transId = 0L;
        try {
            transId = userService.initiateTransaction(object, description, initiatorId, transactionCode);
            return transId;
        } catch (Exception exp) {
            throw exp;
        }
    }

    /**
     * @return the uploadService
     */
    public UploadService getUploadService() {
        return uploadService;
    }

    /**
     * @param uploadService the uploadService to set
     */
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }
    //

    public SetupService getSetupService() {
        return setupService;
    }

    public void setSetupService(SetupService setupService) {
        this.setupService = setupService;
    }
}
