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

package org.tenece.web.data;

import java.util.Date;

/**
 *
 * @author amachree
 */
public class Users {
    
    private int id;
    private int employeeId;
    private String userName;
    private String password;
    private int adminUser;
    private int superAdmin;
    private int numberLogins;
    private String active;
    private Date lastLoginDate;
    private Date dateUpdated;
    private String statusDes;
    
    private Date dateSignup;
    private int groupCompanyUser;
    private Date dateLogin;
    private int roleId;
    private String roleName;
    private String err;
    private String fullnames;
    private int serialNumber;
    private String lastLoginTime;
    private long nextApprovingOfficer;
     //used for eTable
    private long transactionId;
    /** Creates a new instance of Users */
    public Users() {
    }

    public String toString(){
        String values = "ID=" + getId()
                + ", userName=" + getUserName()
                + ", Full Name=" + getFullnames()
                + ", Role Name=" + getRoleName()
                + ", Status=" + getActive();
        return values;
    }
public String toStringForPassword(){
                String values = "ID=" + getId()
                + ", userName=" + getUserName()
                + ", Full Name=" + getFullnames()
                + ", Status=" + getActive();
        return values;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(int adminUser) {
        this.adminUser = adminUser;
    }

    public int getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(int superAdmin) {
        this.superAdmin = superAdmin;
    }

    public int getNumberLogins() {
        return numberLogins;
    }

    public void setNumberLogins(int numberLogins) {
        this.numberLogins = numberLogins;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the dateUpdated
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     * @param dateUpdated the dateUpdated to set
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /**
     * @return the lastLoginDate
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * @param lastLoginDate the lastLoginDate to set
     */
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /**
     * @return the dateSignup
     */
    public Date getDateSignup() {
        return dateSignup;
    }

    /**
     * @param dateSignup the dateSignup to set
     */
    public void setDateSignup(Date dateSignup) {
        this.dateSignup = dateSignup;
    }
        /**
     * @return the nextApprovingOfficer
     */
    public long getNextApprovingOfficer() {
        return nextApprovingOfficer;
    }

    /**
     * @param nextApprovingOfficer the nextApprovingOfficer to set
     */
    public void setNextApprovingOfficer(long nextApprovingOfficer) {
        this.nextApprovingOfficer = nextApprovingOfficer;
    }

    /**
     * @return the groupCompanyUser
     */
    public int getGroupCompanyUser() {
        if(getAdminUser() == 1 && getSuperAdmin() == 1){
            return 1;
        }else if(getAdminUser() == 0 && getSuperAdmin() == 1){
            return 0;
        }else{
            return 0;
        }
    }

    /**
     * @param groupCompanyUser the groupCompanyUser to set
     */
    public void setGroupCompanyUser(int groupCompanyUser) {
        this.groupCompanyUser = groupCompanyUser;
    }

    /**
     * @return the dateLogin
     */
    public Date getDateLogin() {
        return dateLogin;
    }

    /**
     * @param dateLogin the dateLogin to set
     */
    public void setDateLogin(Date dateLogin) {
        this.dateLogin = dateLogin;
    }
       /**
     * @return the dateLogin
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * @param dateLogin the dateLogin to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
           /**
     * @return the dateLogin
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param dateLogin the dateLogin to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
           /**
     * @return the dateLogin
     */
    public String getErr() {
        return err;
    }

    /**
     * @param dateLogin the dateLogin to set
     */
    public void setErr(String err) {
        this.err = err;
    }
      /** @return the dateLogin
     */
    public String getFullnames() {
        return fullnames;
    }

    /**
     * @param dateLogin the dateLogin to set
     */
    public void setFullnames(String fullnames) {
        this.fullnames = fullnames;
    }
       /** @return the dateLogin
     */
    public int getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param dateLogin the dateLogin to set
     */
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
        /** @return the lastLoginTime
     */
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime the lastLoginTime to set
     */
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    } 
    /**
     * @return the transactionId
     */
    public long getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
      /**
     * @return the statusDes
     */
    public String getStatusDes() {
        return statusDes;
    }

    /**
     * @param statusDes the statusDes to set
     */
    public void setStatusDes(String statusDes) {
        this.statusDes = statusDes;
    }
    
}
