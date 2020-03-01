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

package org.tenece.web.services;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.tenece.ap.constant.AuditOperationCodeConstant;
import org.tenece.ap.data.dao.UserDAO;
import org.tenece.ap.security.SecurityEncoderImpl;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.data.AuditTrail;
import org.tenece.web.data.Lock;
import org.tenece.web.data.Users;
import org.tenece.web.filter.ApplicationFilter;


/**
 * Tenece Professional Services, Nigeria
 * @author Jeffry Amachree
 */
public class LoginService extends BaseService{
    private UserDAO userDAO = null;
    private ApplicationService applicationService = new ApplicationService();
    /** Creates a new instance of LoginService */
    public LoginService() {
    }

    public int updatePassword (long userId, String password){
        return getUserDAO().updateUserPassword(userId, password);
    }
    public int resetPassword (long userId, String password){
        return getUserDAO().resetUserPassword(userId, password);
    }
    public Users validateUser(HttpServletRequest request,String uid, String pwd){
        
        try {
            Users user = new Users();

            //set properties to use for validation
            user.setUserName(uid);
            //user.setPassword(pwd);
            System.out.println("Password >>>>>>>>>>>>>>>>."+pwd);
            //validate user name against database
            //this will be used to ensure that the user name exist on the system
            user = getUserDAO().getUserByUserName(user);
            if(user != null && (pwd != null || !pwd.equals("")|| !pwd.equals(null) )){
               // if it exist, check if account is locked
//                long userID = user.getId();
//                long loginCount = user.getNumberLogins();
//                if (loginCount >= 3) {
//                    
//                    throw new IllegalAccessException("User account is locked. Contact System Administrator for Assistance.");
//                }

                //get password
//                String userPassword = user.getPassword();
//                //decrypt password
//                String decryptedPassword = SecurityEncoderImpl.decryptPasswordWithAES(userPassword);
//                //compare with the password sent via request
//                if(!pwd.trim().equals(decryptedPassword)){
//
//                    //if numlogin is 3 or greater, lock user
//                    if((loginCount + 1) >= 3){
//                        //lock user
//                        Lock lock = new Lock();
//                        lock.setActive("A");
//                        lock.setDateLocked(new Date());
//                        lock.setLockedBy("0");
//                        lock.setReason("User Login Tries Exceed Limit.");
//                        lock.setUserId(userID);
//
//                        getUserDAO().lockUser(lock);
//
//                        //increase lock
//                        user.setNumberLogins(user.getNumberLogins() + 1);
//                        getUserDAO().updateUsersLoginTries(user);
//                        //thorw exception with appropriate message
//                        if(true){
//                            createNewAuditTrail(request, "User account is locked.["+ user.getUserName() + "]",AuditOperationCodeConstant.lock_User_Account);
//                            throw new IllegalAccessException("User account is locked. There was an attempt to access this account.");
//                        }
//                    }else{
//                        //increase login tries
//                        Properties actConfig = ConfigReader.getActiveDirectoryConfig();
//                        if(actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_CHECK)!= null && actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_CHECK).equals("Yes"))
//                         {
//                             changePassword(pwd,  user, request);
//                         }
//                        else
//                        {
//                            user.setNumberLogins(user.getNumberLogins() + 1);
//                            getUserDAO().updateUsersLoginTries(user);
//                            throw new Exception("Invalid User Name and Password Combination");
//                        }
                        
                   // }
//                }else{
//                    //reset login tries on successful login
//                    user.setNumberLogins(0);
//                    getUserDAO().updateUsersLoginTries(user);
//                    //getUserDAO().updateUserLoginInfo(user.getId());
//                }

            }else{
//                user.setNumberLogins(user.getNumberLogins() + 1);
//                getUserDAO().updateUsersLoginTries(user);
                this.setErrorMessage("Failed Login. Contact your Administrator ");
                throw new Exception("Failed Login. Contact your Administrator");
            }
            //return the user instance returned
            return user;
        }catch(IllegalAccessException ie){
            this.setErrorMessage(ie.getMessage());
            return null;
        }catch (Exception ex) {
            this.setErrorMessage("Please Contact your System Administrator ");
            return null;
        }
        
    }

    public boolean updateLoginDateforUser(Users user){
        int rows = getUserDAO().updateUserLoginInfo(user.getId());
        if(rows == 0){
            return false;
        }else{
            return true;
        }
    }
    public boolean createPasswordHistory(int Id,String Password){
        boolean saved = getUserDAO().createPasswordHistory(Id,Password);
        if(saved == true){
            return true;
        }else{
            return false;
        }
    }
    public List<String> findLast12Passwords (long userId){
        return getUserDAO().getLast12Password((int) userId);
    }
    public UserDAO getUserDAO() {
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        return userDAO;
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
    private Users getUserPrincipal(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
private void changePassword(String password, Users _user,HttpServletRequest request)
    {
          //encrypt
        String strPwd = "";
        try {
            strPwd = SecurityEncoderImpl.encryptPasswordWithAES(password);
        } catch (Exception ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> passwordHistoryList = findLast12Passwords(_user.getId());
        if(!passwordHistoryList.isEmpty() ||passwordHistoryList.size() > 0)
        {
           for (String returnpassword : passwordHistoryList)
           {
              

              if(returnpassword.equals(strPwd))
              {
//                    view.addObject("message", "Use another Password because the typed password was used in the last 12 password change");
//                    return view;
              
              }
           }
        }
        int row = updatePassword(_user.getId(), strPwd);
        if(row == 0){
            createNewAuditTrail(request, "Unable to Change User Login Information:[" + _user.getUserName() + "]",AuditOperationCodeConstant.Unable_To_Change_User_Login_Info);
//            view = new ModelAndView("error");
//            view.addObject("message", "Unable to change user login information");
//            return view;
        }
        //update user login date to remove first login
        try{
            boolean updated = updateLoginDateforUser(_user);
            System.out.println("Login Updated: " + updated);
        }catch(Exception er){
            er.printStackTrace();
        }
         //insert to Password History
        try{
            boolean created = createPasswordHistory(_user.getId(),strPwd);
            System.out.println("Password History Created: " + created);
        }catch(Exception er){
            er.printStackTrace();
        }
        //getUserDAO().updateUserLoginInfo(user.getId());
        //send mail
//        try{
//            int sent = mailService.sendChangePassword(_user, password, "");
//        }catch(Exception e){}
        //
        Users newUser = new Users();
        newUser.setPassword(strPwd);
        newUser.setUserName(_user.getUserName());
        newUser.setFullnames(_user.getFullnames());
        newUser.setId(_user.getId());
        newUser.setActive(_user.getActive());
        createNewAuditTrail(request, "System User Changed Password: Record[" + newUser.toStringForPassword() + "]Old Password: Record["+_user.toStringForPassword() +" ]",AuditOperationCodeConstant.Change_System_User_Password);
    }
}
