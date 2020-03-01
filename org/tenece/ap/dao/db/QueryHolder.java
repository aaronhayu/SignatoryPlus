
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 24 May 2009, 22:11
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

package org.tenece.ap.dao.db;

/**
 *
 * @author jeffry.amachree
 */
public class QueryHolder{

    public QueryHolder(){
    
    }
    public String TEST_QUERY = "select 'mysql' from dual ";

    public  String ORDER_BY_1 = " order by 1";
    public  String ORDER_BY_2 = " order by 2";
    public  String ORDER_BY_3 = " order by 3";
    public  String ORDER_BY_4 = " order by 4";
    public  String ORDER_BY_5 = " order by 5";

    public  String COUNTRY_SELECT = "select code, description from country order by 1 ";
    public  String RELATIONSHIP_SELECT = "select code, description from relationship order by 1";

    public  String EMAIL_CONTENT_SELECT_BY_CODE = "select code, subject, message_body, sender_email, sender_name from email_messages where code=?";

    public  String AUDIT_SUMMARY_SELECT = "select count(distinct user_id) as user_count, count(id) as total_records  from audit_trail ";
    public  String AUDIT_ARCHIVE_INSERT = "insert into audit_trail_archive (audit_id, user_id, access_date, operation, machine_identity) select id, user_id, access_date, operation, machine_identity from audit_trail order by id ";
    public  String AUDIT_ARCHIVE_DELETE = "delete from audit_trail ";

    /* ----------- USERS ----------- */
    public  String USER_SELECT = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,r.RoleName,(e.firstname+ ' '+e.lastname) as fullname from users u, UsersInRoles ur, Roles r,employee e  where u.user_id=ur.UserId and ur.RoleId=r.RoleId and u.emp_number=e.emp_number and (u.active='1' or u.active='0'  or u.active='P' or u.active='R') "+
            " union select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,r.RoleName,(e.Employee_Name) as fullname from users u, UsersInRoles ur, Roles r,UnAuthorised_Employee e  where u.user_id=ur.UserId and ur.RoleId=r.RoleId and u.user_name=e.Staffid_UserName and (u.active='1' or u.active='0'  or u.active='P' or u.active='R')";
     public  String USER_SELECT_WITHOUT_ROLE = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,(e.firstname+ ' '+e.lastname) as fullname from users u,employee e  where  u.emp_number=e.emp_number and (u.active='1' or u.active='0'  or u.active='P' or u.active='R' ) "+
            " union select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,(e.Employee_Name) as fullname from users u,UnAuthorised_Employee e  where  u.user_name=e.Staffid_UserName and (u.active='1' or u.active='0'  or u.active='P' or u.active='R')";

    public  String USER_SELECT_BY_COMPANY = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated,r.RoleName from users u, employee e,UsersInRoles ur, Roles r where u.emp_number = e.emp_number and u.user_id=ur.UserId and ur.RoleId=r.RoleId and e.company_code=?  ";
    public  String USER_SELECT_BY_ID = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,ur.RoleId,r.RoleName,(e.firstname +' '+ e.lastname) as fullname from users u,UsersInRoles ur,Roles r,employee e where  u.user_id=ur.UserId and r.RoleId=ur.RoleId and e.emp_number=u.emp_number and user_id=? "+ 
                  " union select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,ur.RoleId,r.RoleName,(e.Employee_Name) as fullname from users u,UsersInRoles ur,Roles r,UnAuthorised_Employee e where  u.user_id=ur.UserId and r.RoleId=ur.RoleId and e.Staffid_UserName=u.user_name and user_id=? ";
    public  String USER_SELECT_BY_ID_WITHOUT_UNION = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,ur.RoleId,r.RoleName,(e.firstname +' '+ e.lastname) as fullname from users u,UsersInRoles ur,Roles r,employee e where  u.user_id=ur.UserId and r.RoleId=ur.RoleId and e.emp_number=u.emp_number and user_id=? ";
                 
    
    public  String USER_SELECT_BY_UID_AND_PASSWORD = "select user_id, emp_number, user_name, password, admin_user, superadmin, numlogins, active, dateupdated, lastlogindate, datelogin from users where user_name=? and password=? " +
            " and user_id not in (select user_id from locks where active='A') ";
    public  String USER_SELECT_BY_UID_AND_EMP_ID = "select password, user_id from users where user_name=? and " +
            " emp_number in (select emp_number from employee where emp_id = ?) ";
    
    public  String USER_SELECT_BY_UID = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,(e.firstname+' '+e.lastname) as fullname,r.RoleId,RIGHT(CONVERT(VARCHAR, u.lastlogindate, 100),7) as Time from users u, employee e, UsersInRoles r where u.user_name=? and u.active='1'  and e.emp_number=u.emp_number and r.UserId=u.user_id"
    + " union select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,e.Employee_Name as fullname,r.RoleId,RIGHT(CONVERT(VARCHAR, u.lastlogindate, 100),7) as Time from users u, UnAuthorised_Employee e,UsersInRoles r where u.user_name=? and u.active='1'  and u.user_name=e.Staffid_UserName and r.UserId=u.user_id";
    public  String USER_SELECT_BY_SELECT_ID = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,(e.firstname+' '+e.lastname) as fullname,r.RoleId,rs.RoleName from users u, employee e, UsersInRoles r,Roles rs where u.user_name like '%_SEARCH_%' and u.active=1  and e.emp_number=u.emp_number and r.UserId=u.user_id and rs.RoleId=r.RoleId union select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins,"
      + "u.active, u.dateupdated, u.lastlogindate, u.datelogin,e.Employee_Name as fullname,r.RoleId,rs.RoleName from users u, UnAuthorised_Employee e,UsersInRoles r,Roles rs where u.user_name like '%_SEARCH_%' and u.active=1  and u.user_name=e.Staffid_UserName and r.UserId=u.user_id and rs.RoleId=r.RoleId";
 public  String USER_WITHOUT_ROLE_SELECT_BY_SELECT_ID = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,(e.firstname+' '+e.lastname) as fullname from users u, employee e where u.user_name like '%_SEARCH_%' and u.active=1  and e.emp_number=u.emp_number  union select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins,"
      + "u.active, u.dateupdated, u.lastlogindate, u.datelogin,e.Employee_Name as fullname from users u, UnAuthorised_Employee e where u.user_name like '%_SEARCH_%' and u.active=1  and u.user_name=e.Staffid_UserName ";

    public  String USER_INSERT = "insert into users (emp_number, user_name, password, admin_user, superadmin, numlogins, active, dateupdated, datesignup, lastlogindate, loginip) values(null,?,?,?,?,?,?,?,?,?, ' ')";
    public  String USER_UPDATE = "update users set emp_number=null, user_name=?, admin_user=?, superadmin=?, active=?, dateupdated=? where user_id=?";
    public  String USER_UPDATE_PASSWORD = "update users set password=?, dateupdated=? where user_id=?";
    public  String USER_RESET_PASSWORD = "update users set password=?, datelogin=null where user_id=?";
    public  String USER_UPDATE_LOGIN = "update users set numlogins=? where user_id = ? ";
    public  String USER_DELETE = "delete from users where user_id = ? ";
    public  String USER_IN_WORKFLOW_DELETE = "delete from workflow_audit where action_by = ? ";
    public  String USER_IN_ROLE_DELETE = "delete from UsersInRoles where UserId = ? ";
    public  String USER_IN_AUDIT_TRAIL_DELETE = "delete from audit_trail where user_id = ? ";
    public  String USER_IN_UNAUTHORISED_EMPLOYEE_DELETE = "delete from UnAuthorised_Employee where Staffid_UserName = ? ";
    public  String USER_UPDATE_OK_LOGIN = "update users set lastlogindate=datelogin, datelogin=? where user_id = ? ";

    public  String USER_SELECT_BY_LOCKED_STATUS = "select t1.user_id, t1.emp_number, t1.user_name, t1.password, t1.admin_user,"+ 
        " t1.superadmin, t1.numlogins, t1.active, t1.dateupdated, t1.lastlogindate, t2.RoleName "+
        " from users t1, Roles t2,UsersInRoles t3 where "+
        " t1.user_id=t3.UserId and t2.RoleId=t3.RoleId and "+
        " t1.user_id in (select user_id from locks where active='A')  ";
    public  String USER_LOCK_SELECT_BY_ID = "select user_id, datelock, reasonlock, lockedby, active from locks  where user_id =? ";
    public  String USER_LOCK_INSERT = "insert into locks (user_id, datelock, reasonlock, lockedby, active) values(?,?,?,?,?) ";
    public  String USER_LOCK_DEACTIVATE = "update locks set active='D' where user_id=? ";
    public  String USER_RESET_LOGIN_TRIES = "update users set numlogins=0 where user_id=?";

    public  String USER_UPDATE_PASSWORD_FOR_USER_DEACTIVATION = "update users set password='', dateupdated=? where emp_number=?";
    
    public  String ROLE_NAME_SELECT_BY_ID = "select RoleName from Roles  where RoleId =? ";
    public  String ROLE_NAME_SELECT_BY_ROLE_NAME = "select UPPER(RoleName)as RoleName from Roles  where RoleName =? ";
    public  String MENU_NAME_SELECT_BY_ID = "select submenutitle from MENU  where MenuId =? ";
    /* -----------Department ---------*/
    public  String DEPARTMENT_SELECT = " select deptid, managerid, deptname, location, deptdesc, workdesc, parent_id, company_code from department order by deptname ";
    public  String DEPARTMENT_SELECT_SEARCH_BY_NAME = " select deptid, managerid, deptname, location, deptdesc, workdesc, parent_id, company_code from department where deptname like '%_SEARCH_%' ";
    public  String DEPARTMENT_SELECT_SEARCH_BY_LOCATION = " select deptid, managerid, deptname, location, deptdesc, workdesc, parent_id, company_code from department where location like '%_SEARCH_%' ";

    public  String DEPARTMENT_SELECT_BY_COMPANY = " select deptid, managerid, deptname, location, deptdesc, workdesc, parent_id, company_code from department where company_code=? ";
    public  String DEPARTMENT_SELECT_BY_COMPANY_SEARCH_BY_NAME = " select deptid, managerid, deptname, location, deptdesc, workdesc, parent_id, company_code from department where company_code=? and deptname like '%_SEARCH_%' ";
    public  String DEPARTMENT_SELECT_BY_COMPANY_SEARCH_BY_LOCATION = " select deptid, managerid, deptname, location, deptdesc, workdesc, parent_id, company_code from department where company_code=? and location like '%_SEARCH_%' ";

    public  String DEPARTMENT_SELECT_BY_ID = " select deptid, managerid, deptname, location, deptdesc, workdesc, parent_id, company_code from department where deptid = ? ";
    public  String DEPARTMENT_INSERT = " insert into department (managerid, deptname, location, deptdesc, workdesc, parent_id, company_code) values (?,?,?,?,?,?,?)";
    public  String DEPARTMENT_UPDATE = " update department set managerid=?, deptname=?, location=?, deptdesc=?, workdesc=?, parent_id=?, company_code=? where deptid=? ";
    public  String DEPARTMENT_DELETE = "delete from department where deptid = ? ";
    
    /* ---------- Job Title ----------*/
    public  String JOBTITLE_SELECT = "select jobid, jobtitle, jobdesc, '' as company_code from jobtitle ";
    public  String JOBTITLE_SELECT_BY_COMPANY = "select jobid, jobtitle, jobdesc, '' as company_code from jobtitle where company_code=? ";
    public  String JOBTITLE_SELECT_BY_ID = "select jobid, jobtitle, jobdesc, '' as company_code from jobtitle where jobid=?";
    public  String JOBTITLE_UPDATE = "update jobtitle set jobtitle=?, jobdesc=?, where jobid=? ";
    public  String JOBTITLE_INSERT = "insert into jobtitle(jobtitle, jobdesc) values(?,?) ";
    public  String JOBTITLE_DELETE = "delete from jobtitle where jobid = ? ";
    
    /* ---------- DIVISION -------------*/
    public  String DIVISION_SELECT = "select division_id, division_code, division_name, division_description from division order by division_name ";
    public  String DIVISION_SELECT_SEARCH_BY_CODE = "select division_id, division_code, division_name, division_description from division where division_code like '%_SEARCH_%'";
    public  String DIVISION_SELECT_SEARCH_BY_NAME = "select division_id, division_code, division_name, division_description from division where division_name like '%_SEARCH_%'";
    public  String DIVISION_SELECT_SEARCH_BY_DESC = "select division_id, division_code, division_name, division_description from division where division_description like '%_SEARCH_%' ";

    public  String DIVISION_SELECT_BY_ID = "select division_id, division_code, division_name, division_description from division where division_id=? ";
    public  String DIVISION_SELECT_BY_CODE = "select division_name from division where division_code=? ";
    public  String DIVISION_UPDATE = "update division set division_code=?, division_name=?, division_description=? where division_id=? ";
    public  String DIVISION_INSERT = "insert into division (division_code, division_name, division_description) values(?,?,?) ";
    public  String DIVISION_DELETE = "delete from division where division_id = ? ";
    
    /* --------- EMPLOYEE LOCKS ------------ */
    public  String LOCKS_SELECT = "select lockid, emp_number, datelock, reasonlock, lockedby, active from locks ";
    public  String LOCKS_SELECT_BY_ID = "select lockid, emp_number, datelock, reasonlock, lockedby, active from locks where emp_number=? ";
    public  String LOCKS_INSERT = "insert into locks (lockid, emp_number, datelock, reasonlock, lockedby, active) values(?,?,?,?,?,?) ";
    public  String LOCKS_UPDATE = "update locks set emp_number=?, datelock=?, reasonlock=?, lockedby=?, active=? where lockid=? ";
    public  String LOCKS_DELETE = "delete from locks where lockid=? ";
    
    /* ----------- EMPLOYEE --------------------------- */
    
    public  String EMPLOYEE_SELECT = "select * from employee where active=1  ";
    public  String EMPLOYEE_SELECT_FOR_ID = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, signature, signature_name, signature_number from employee where emp_id=? and active='A'  ";
    public  String EMPLOYEE_SELECT_FOR_ID_AND_COMPANY = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, active, email, divisionid, cellphone, extension, signature, signature_name, signature_number from employee where active='A' ";

    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where active='A'  ";
    public  String EMPLOYEE_SELECT_WITHOUT_SIGNATURE = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where signature is null and new_signs=1 AND active='A'  ";
    public  String EMPLOYEE_SELECT_WITHOUT_SIGNATURE_NUMBER = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where signature_number is null order by staffid  ";

    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_FNAME = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name,signature_number from employee where firstname like '%_SEARCH_%' and active='A'  ";
    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_LNAME = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where lastname like '%_SEARCH_%' and active='A' ";
    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_ID = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where staffid = '_SEARCH_' and active='A' ";
    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_COMPANY = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where company_code = '_SEARCH_' and active='A'  ";

    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where firstname + ' ' + lastname + ' ' + othernames like '%_SEARCH_NAME_%' and staffid like '%_SEARCH_ID_%'  and divisionid between '_SEARCH_DIV_FROM_' and '_SEARCH_DIV_TO_'  ";

    public  String EMPLOYEE_SELECT_BASIC = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, signature, signature_name, signature_number,category,jobtitleid,gradeid,branchDesc from employee where emp_number=? and active in ('A','P','T') ";
    public  String EMPLOYEE_SELECT_BASIC_BY_STAFFID = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, signature, signature_name, signature_number from employee where staffid =? ";
    public  String EMPLOYEE_ID_BASIC_BY_USER_NAME = "select emp_number from employee where staffid =? ";

    public  String EMPLOYEE_DELETE = "update employee set active='D' where emp_number = ? ";
    public  String EMPLOYEE_UPDATE_BASIC = "update employee set staffid=?, deptid=?, lastname=?, firstname=?, othernames=?, cellphone=?, extension=?, email=?, divisionid=?, signature=?, signature_name=?, signature_number=?, category =? , jobtitleid=?, gradeid=?,branchDesc=? where emp_number = ? ";
    public  String EMPLOYEE_UPDATE_BASIC_WITHOUT_SIGNATURE = "update employee set staffid=?, deptid=?, lastname=?, firstname=?, othernames=?, cellphone=?, extension=?, email=?, divisionid=?, signature_number=?, category =? , jobtitleid=? , gradeid=?,branchDesc=? where emp_number = ? ";
    public  String EMPLOYEE_INSERT_BASIC = "insert into employee(staffid, deptid, lastname, firstname, othernames, cellphone, extension, email, divisionid, signature, signature_name, signature_number, category, jobtitleid, active, create_date,gradeid,branchDesc) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,'A', getdate(),?,?) ";
    public  String EMPLOYEE_INSERT_BASIC_WITH_SIGN = "insert into employee(staffid, deptid, lastname, firstname, othernames, cellphone, extension, email, divisionid, signature, signature_name, signature_number,category, active, new_signs, create_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,'A', 0, getdate()) ";
    public  String EMPLOYEE_UPDATE_DEPRECATE_STATUS = "update employee set active=?, deprecate_reason=? where emp_number = ? ";
    public  String EMPLOYEE_UPDATE_SIGNATURE_NUMBER = "update employee set signature_number=? where emp_number = ? ";

    public  String EMPLOYEE_UPDATE_EMPLOYMENT_STATUS = "update employee set employment_status=?, active=? where emp_number = ? ";

    public String SIGNATURE_NUMBER = "select signature_number from max_signature_number ";
    public String INCREASE_SIGNATURE_NUMBER = "update max_signature_number set signature_number = signature_number + 1 ";
    /*  ------------- REPORT QUERY -------------------- */
    public  String REPORT_CODE_SELECT = "select * from report_template where code = ? ";
    
    /* ----------------- ADVANCE PAYMENT ----------------------- */
    //id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active
    public  String ADVANCE_PAYMENT_SELECT_BY_ACTIVE = "select id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active from advancepayment where active='A' ";
    public  String ADVANCE_PAYMENT_SELECT_BY_ACTIVE_AND_PURPOSE = "select id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active from advancepayment where active='A' AND purpose like '%_SEARCH_%'";

    public  String ADVANCE_PAYMENT_SELECT_BY_ACTIVE_FOR_EMPLOYEE = "select id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active from advancepayment where active='A' and emp_number=? ";
    public  String ADVANCE_PAYMENT_SELECT_BY_ACTIVE_AND_PURPOSE_FOR_EMPLOYEE = "select id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active from advancepayment where active='A' AND purpose like '%_SEARCH_%' and emp_number=?";

    public  String ADVANCE_PAYMENT_SELECT_BY_STATUS_AND_EMPLOYEE = "select id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active from advancepayment WHERE active=? and emp_number=? ";
    public  String ADVANCE_PAYMENT_SELECT_BY_EMPLOYEE = "select id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active from advancepayment WHERE emp_number=? ";
    public  String ADVANCE_PAYMENT_SELECT_BY_ID = "select id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active from advancepayment WHERE id=? ";
    public  String ADVANCE_PAYMENT_INSERT = "insert into advancepayment (advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active) values(?,?,?,?,?,?,?,?,?) ";
    public  String ADVANCE_PAYMENT_UPDATE = "update advancepayment set advance_to=?, application_date=?, form_number=?, emp_number=?, purpose=?, amount=?, current_advance_temp_amount=?, current_advance_perm_amount=?, active=? where id=? ";
    public  String ADVANCE_PAYMENT_DELETE = "update advancepayment set  active='D' where id=? ";
    public  String ADVANCE_PAYMENT_CLOSE = "update advancepayment set  active='C' where id=? ";
    public  String ADVANCE_PAYMENT_CHECK_PENDING = "SELECT COUNT(id) from advancepayment where active='A' and emp_number=?";

    public String ADVANCE_PAYMENT_CLOSE_SELECT = "id, advance_id, form_number, close_date, total_expenses, less_expenses from advanceclosed WHERE ID=?";
    public String ADVANCE_PAYMENT_CLOSE_INSERT = "INSERT INTO advanceclosed (advance_id, form_number, close_date, total_expenses, less_expenses) VALUES(?,?,?,?,?) ";
    public String ADVANCE_PAYMENT_CLOSE_UPDATE = "update advanceclosed SET advance_id=?, form_number=?, close_date=?, total_expenses=?, less_expenses=? WHERE ID=? ";
    
    /* --------------- AUDIT MTRAIL ---------------------- */
    public  String AUDIT_TRAIL_SELECT = "select user_id as emp_number, access_date, operation, machine_identity from audit_trail ";
    public  String AUDIT_TRAIL_SELECT_BY_EMP = "select user_id as emp_number, access_date, operation, machine_identity from audit_trail where user_id=? ";
    public  String AUDIT_TRAIL_SELECT_BY_DATE = "select user_id as emp_number, access_date, operation, machine_identity from audit_trail where access_date between ? and ? ";
    public  String AUDIT_TRAIL_INSERT = "insert into audit_trail (user_id, access_date, operation, machine_identity,operation_code) values(?,?,?,?,?) ";
    

    /* --------------- REPORT TEMPLATE (report_template) ---------------------- */
    public  String REPORT_TEMPLATE_SELECT_BY_CODE = "select code, control, control_caption, control_value, init_procedures, custom1 from report_template where code=? ";
    public  String REPORT_SELECT_BY_ID = "select id, title, report_template, report_query, report_procedure, report_file, subreports_query from report where id=? ";

    /* ----------- CHART ------------------- */
    public  String DASHBOARD_EMPLOYEE_COUNT_BY_ACTIVE = "select count(emp_number) as counted, 'Active Employee' as description from employee where active='A' union select count(emp_number) as counted, 'Deprecated Employee' as description from employee where active='P' ";
    public  String DASHBOARD_MAX_SIGNATURE_NUMBER = "select max(signature_number)as counted  from employee ";

    /* -----------CRON/AUTOMATED PROCESSES ------------------- */
    public  String AUTOMATED_MAX_SIGNATURE_NUMBER = " assignSignatureNumber(?) ";
    
    
     /* ---------- Grade  ----------*/
    public  String GRADE_SELECT = "select gradeid, gradetitle, gradedesc, '' as company_code from grade ";
    public  String GRADE_SELECT_BY_COMPANY = "select gradeid, gradetitle, gradedesc, '' as company_code from grade where company_code=? ";
    public  String GRADE_SELECT_BY_ID = "select gradeid, gradetitle, gradedesc, '' as company_code from grade where gradeid=?";
    public  String GRADE_UPDATE = "update grade set gradetitle=?, gradedesc=?, where gradeid=? ";
    public  String GRADE_INSERT = "insert into grade(gradetitle, gradedesc) values(?,?) ";
    public  String GRADE_DELETE = "delete from grade where gradeid = ? ";
    
    
    public  String EMPLOYEE_SELECT_SIGNATURE_SEARCH_FNAME = "select staffid, lastname, firstname, othernames,signature, signature_name,signature_number,category from employee where firstname like '%_SEARCH_%' and active='A'  ";
    public  String EMPLOYEE_SELECT_SIGNATURE_SEARCH_LNAME = "select staffid, lastname, firstname, othernames,signature, signature_name,signature_number,category from employee where lastname like '%_SEARCH_%' and active='A' ";
    public  String EMPLOYEE_SELECT_SIGNATURE_SEARCH_ID = "select emp.staffid, emp.lastname, emp.firstname, emp.othernames,emp.signature, emp.signature_name,emp.signature_number,emp.category,cat.Transaction_Limit from employee emp, Employee_Category cat where staffid = '_SEARCH_' and active='A' and emp.category=cat.CategoryCode ";
    public  String EMPLOYEE_CATEGORY_SELECT = "select CategoryCode, CategoryDescription from Employee_Category ";
    public  String EMPLOYEE_CATEGORY_SELECT_BASIC = "select CategoryCode, CategoryDescription,CurrencyCode,Transaction_Limit from Employee_Category ";
    public  String CATEGORY_SELECT_BY_ID = "select CategoryCode, CategoryDescription, CurrencyCode, Transaction_Limit from Employee_Category where CategoryCode=?";
    public  String CATEGORY_UPDATE = "update Employee_Category set Transaction_Limit=? where CategoryCode=?";
    public  String MENU_IN_ROLE_DELETE = "delete MENU_IN_ROLE where Menu_In_RoleID=?";


    // Menu
   public  String MENU_SELECT_BY_USERID = "select m.MenuTitle,m.class,m.URL,m.submenutitle,m.[Target] as Location,m.OrderCode from MENU m,MENU_IN_ROLE mr,Roles r,users u,UsersInRoles ur where m.MenuId=mr.MenuId and mr.RoleId=r.RoleId and r.RoleId = ur.RoleId and ur.UserId= u.user_id and u.user_id=?  order by OrderCode";
   // Role
    public  String ROLE_SELECT = "select RoleId,RoleName from Roles";
    public  String PREVIOUS_12_PASSWORD_HISTORY_BY_USERID = "select top 12 Password from PasswordHistory where UserId=? order by PasswordChangeCount desc";
    public  String PROC_USER_INSERT = " insertUserInfo(?,?,?,?,?,?,?,?,?,?,?,?) ";
    
    public  String PROC_USER_UPDATE = " updateUserInfo(?,?,?,?) ";
    public  String PROC_USER_UPDATE_DURING_DELETE_TXN = " updateUserInfoForDeleteTxn(?,?,?) ";
    public  String PROC_PASSWORD_HISTORY_INSERT = " insertPasswordHistory(?,?) ";
    public  String PROC_USER_INSERT_WITHOUT_EMPL_ID = " insertUserInfoWithoutEmply(?,?,?,?,?,?,?,?,?,?,?,?) ";
    public  String PROC_BULK_USER_INSERT_WITHOUT_EMPL_ID = " insertBulkUserInfoWithoutEmply(?,?,?,?,?,?,?,?,?,?,?,?) ";
    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_FOR_USER_CREATE = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where active='A'  and emp_number not in(select emp_number from users where emp_number is not null) ";
    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_FNAME_FOR_USER_CREATE = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name,signature_number from employee where firstname like '%_SEARCH_%' and active='A' and emp_number not in(select emp_number from users) ";
    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_LNAME_FOR_USER_CREATE = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where lastname like '%_SEARCH_%' and active='A' and emp_number not in(select emp_number from users) ";
    public  String EMPLOYEE_SELECT_ALL_FOR_BASIC_SEARCH_ID_FOR_USER_CREATE = "select staffid, emp_number, deptid, jobtitleid, lastname, firstname, othernames, active, email, divisionid, cellphone, extension, '' as signature, signature_name, signature_number from employee where staffid = '_SEARCH_' and active='A' and emp_number not in(select emp_number from users)";
    
    public  String ROLE_INSERT = "insert into Roles(RoleName) values(?)";
    public  String MENU_IN_ROLE_INSERT = "sp_Insert_MENU_IN_ROLE(?,?) ";
    public  String CHECK_IF_MENU_ASSIGNED = "select Menu_In_RoleID  from MENU_IN_ROLE where RoleId=? and MenuId=? ";
    public  String MENU_SELECT = "select MenuTitle, MenuId, submenutitle from MENU where class != 'submenuheader'";
    public  String MENU_SELECT_BY_ASSIGN_ROLE = "select r.RoleId,mr.Menu_In_RoleID,r.RoleName,m.MenuTitle, m.MenuId, m.submenutitle from MENU m , MENU_IN_ROLE mr, Roles r where m.MenuId=mr.MenuId and m.class != 'submenuheader' and r.RoleId=mr.RoleId and   mr.RoleId = ?";

    public  String GENERATE_EMPLOYEE_INFO_FOR_NIBSS = "select e.firstname,e.othernames, e.lastname,e.category, e.signature_number,e.staffid,j.jobtitle from employee e,jobtitle j where e.jobtitleid=j.jobid  order by e.staffid  ";

    public  String GENERATE_EMPLOYEE_INFO_FOR_E_BOOK = "select emp.staffid, emp.lastname, emp.firstname, emp.othernames,emp.signature, emp.signature_name,emp.signature_number,emp.category,cat.Transaction_Limit from employee emp, Employee_Category cat where  active='A' and emp.category=cat.CategoryCode  and emp.signature is not null";
    public  String UNAUTHORISED_SIGNATORY_EMPLOYEE_NAME_SELECT = "select Employee_Name from UnAuthorised_Employee where Staffid_UserName=? ";
    

    
    public String APPROVAL_ROUTE_GENERATION_FOR_EMPLOYEE = " stp_generate_workflow_for_employee(?,?) ";
    public  String APPROVAL_ROUTE_GENERATED_SELECT_BY_EMP = "SELECT approval_level, user_id, approver_name from vw_approvalroute_generated where user_id=? order by approval_level asc";
    public  String TRANSACTION_SELECT_BY_ID = "select id, description, status, batch_number, transaction_ref, transactiontypeid, initiator_id from workflow_table where id=?";
    public  String APPROVAL_NEXT_LEVEL_FOR_TRANSACTION = "select authorizer from approvalroute where approval_level = (select count(action_by) from workflow_audit where transaction_id=?)" +
            " and transactiontypeid=? and user_id=?  ";
    public  String TRANSACTION_AUDIT_SELECT_BY_TRANSACTION = "select status, action_status, action_date, transaction_id, action_by, e.firstname, e.lastname from workflow_audit a, employee e where e.emp_number=a.action_by and transaction_id=? order by action_date asc ";
    public  String TRANSACTION_STATUS_SELECT = "select id, status, description, module, initiated_date, initiator_id from vw_workflow_status where initiator_id = ? order by initiated_date desc";
    public  String TRANSACTION_STATUS_SELECT_SEARCH_BY_NAME = "select id, status, description, module, initiated_date, initiator_id from vw_workflow_status where description like '%_SEARCH_%' and initiator_id = ? order by initiated_date desc ";
    public  String TRANSACTION_STATUS_SELECT_SEARCH_BY_STATUS = "select id, status, description, module, initiated_date, initiator_id from vw_workflow_status where status='_SEARCH_' and initiator_id = ? order by initiated_date desc ";
 /* -------------- TRANSACTION OPERATIONS ----------------------------- */
    public  String TRANS_TYPE_SELECT = "select id, description, approval_url, parentid from workflowtype order by id ";
    public  String TRANS_TYPE_SELECT_BY_ID = "select id, description, approval_url, parentid from workflowtype where id=? ";
    public  String TRANS_TYPE_SELECT_BY_PARENT = "select id, description, approval_url, parentid from workflowtype where parentid=? ";
    public  String APPROVAL_ROUTE_SELECT_BY_EMP = "select a.id,a.approval_level,a.user_id,a.transactiontypeid,a.authorizer,(e.firstname+ ' '+e.lastname) as fullname from approvalroute a,users u,employee e where a.user_id=? and a.authorizer=u.user_id and u.emp_number=e.emp_number"+
            " union " +
            " select a.id,a.approval_level,a.user_id,a.transactiontypeid,a.authorizer,(e.Employee_Name) as fullname from approvalroute a,users u,UnAuthorised_Employee e where a.user_id=? and a.authorizer=u.user_id and u.user_name=e.Staffid_UserName "+
            " order by approval_level";
    public  String APPROVAL_ROUTE_INSERT = " insert into approvalroute (approval_level, user_id, transactiontypeid, authorizer) values (?,?,?,?) ";
    public  String APPROVAL_ROUTE_DELETE = "delete from approvalroute where user_id=?";
    public  String APPROVAL_ROUTE_IS_DEFINED_FOR_TRANSACTION = "select max(approval_level) from approvalroute a, workflowtype t where t.id = a.transactiontypeid and t.parentid=? and user_id=?";
    public  String APPROVAL_ROUTE_IS_DEFINED_FOR_DEFAULT_TRANSACTION = "select max(approval_level) from approvalroute_generated a, workflowtype t where t.id = a.transactiontypeid and t.parentid=? and user_id=? ";
    public  String TRANSACTION_SELECT_BY_EMPLOYEE = "select id, description, status, batch_number, transaction_ref, transactiontypeid, initiator_id from workflow_table where initiator_id=?";
 public  String TRANSACTION_INBOX_SELECT = "select t.id, t.description, t.status, t.batch_number, t.transaction_ref, t.transactiontypeid, t.initiator_id,(e.firstname+ ' '+ e.lastname) as fullname, r.description as transtypedesc  "+
             " from workflow_table t, employee e, workflowtype r,users u where t.initiator_id = u.user_id and e.emp_number=u.emp_number and t.transactiontypeid = r.id "+
             " and t.id in (select transaction_id from workflow_audit where action_status='P' and action_by=?) "+
             " union "+
             " select t.id, t.description, t.status, t.batch_number, t.transaction_ref, t.transactiontypeid, t.initiator_id,(e.Employee_Name) as fullname, r.description as transtypedesc"+ 
             " from workflow_table t, UnAuthorised_Employee e, workflowtype r,users u where t.initiator_id = u.user_id and e.Staffid_UserName=u.user_name and t.transactiontypeid = r.id"+
             " and t.id in (select transaction_id from workflow_audit where action_status='P' and action_by=?) ";
    public  String TRANSACTION_INBOX_SELECT_SEARCH_INITIATOR = "select t.id, t.description, t.status, t.batch_number, t.transaction_ref, t.transactiontypeid, t.initiator_id, (e.firstname+ ' '+ e.lastname) as fullname, r.description as transtypedesc"+ 
             " from workflow_table t, employee e, workflowtype r,users u where t.initiator_id = u.user_id and e.emp_number=u.emp_number and t.transactiontypeid = r.id "+
             " and t.id in (select transaction_id from workflow_audit where action_status='P' and action_by=?)"+
             " and e.firstname + ' ' + e.lastname like '%_SEARCH_%'"+
             " union "+
             " select t.id, t.description, t.status, t.batch_number, t.transaction_ref, t.transactiontypeid, t.initiator_id, (e.Employee_Name) as fullname, r.description as transtypedesc "+
             " from workflow_table t, UnAuthorised_Employee e, workflowtype r,users u where t.initiator_id = u.user_id and e.Staffid_UserName=u.user_name and t.transactiontypeid = r.id "+
             " and t.id in (select transaction_id from workflow_audit where action_status='P' and action_by=?)"+
             " and e.Employee_Name like '%_SEARCH_%'";
    
    public  String TRANSACTION_INBOX_SELECT_SEARCH_TRANSTYPE = " select t.id, t.description, t.status, t.batch_number, t.transaction_ref, t.transactiontypeid, t.initiator_id, (e.firstname+ ' '+ e.lastname) as fullname, r.description as transtypedesc "+
            " from workflow_table t, employee e, workflowtype r , users u where t.initiator_id = u.user_id and e.emp_number=u.emp_number and t.transactiontypeid = r.id "+
            " and t.id in (select transaction_id from workflow_audit where action_status='P' and action_by=?) "+
            " and r.description like '%_SEARCH_%' "+
            " union "+
            " select t.id, t.description, t.status, t.batch_number, t.transaction_ref, t.transactiontypeid, t.initiator_id, (e.Employee_Name) as fullname, r.description as transtypedesc "+
            " from workflow_table t, UnAuthorised_Employee e, workflowtype r , users u where t.initiator_id = u.user_id and e.Staffid_UserName=u.user_name and t.transactiontypeid = r.id "+
            " and t.id in (select transaction_id from workflow_audit where action_status='P' and action_by=?) "+
            " and r.description like '%_SEARCH_%' ";
    public  String TRANSACTION_INSERT = "insert into workflow_table (description, status, batch_number, transaction_ref, transactiontypeid, initiator_id) values(?,?,?,?,?,?) ";
    public  String TRANSACTION_SELECT_EMPLOYEE_MAX_ID = "select max(id) from workflow_table where initiator_id=?";
    public  String TRANSACTION_AUDIT_INSERT = "insert into workflow_audit (status, action_status, action_date, transaction_id, action_by) values(?,?,?,?,?) ";
    public  String TRANSACTION_UPDATE_STATUS = "update workflow_table set status=? where id=?";
    public  String APPROVAL_FIRST_LEVEL_FOR_TRANSACTION = "select authorizer from approvalroute where approval_level = 1 " +
            " and transactiontypeid=? and user_id=?  ";
    
    public  String APPROVAL_FIRST_LEVEL_BY_DEFAULT = "select authorizer from approvalroute_generated where approval_level = 1 " +
            " and user_id=?" ;
            
public  String APPROVAL_NEXT_LEVEL_BY_DEFAULT = " select authorizer from approvalroute_generated where approval_level = (select count(action_by) from workflow_audit where transaction_id=?) and emp_number=? and isdepartment=0" +
            " union " +
            " select d.managerid from approvalroute_generated a, department d where a.authorizer_group = d.deptid and approval_level = (select count(action_by) from workflow_audit where transaction_id=?) and emp_number=? and isdepartment=1  ";
 public  String APPROVAL_NEXT_PENDING_LEVEL_FOR_TRANSACTION = "select action_by from workflow_audit where transaction_id=? and action_status='P' ";
public  String APPROVAL_CONFIRM_NEXT_LEVEL_AVAILABILITY = "select ((select max(approval_level) + 1 from approvalroute where transactiontypeid=? and user_id=?) - " +
            "(select count(action_by) as counted from workflow_audit where transaction_id=? and status='A'))";
public  String APPROVAL_CONFIRM_NEXT_LEVEL_AVAILABILITY_BY_DEFAULT = "select ((select max(approval_level) + 1 from approvalroute_generated where emp_number=?) - " +
            "(select count(action_by) as counted from workflow_audit where transaction_id=? and status='A'))";
 public  String TRANSACTION_AUDIT_UPDATE_BY_TRANSACTION_AND_ACTIONBY = "update workflow_audit set status=?, action_status=?, action_date=? where transaction_id=? and action_by=? ";
public  String USER_SELECT_BY_TRANSACTION = "select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.datesignup, u.lastlogindate, u.dateupdated,u.active,u.datelogin,u.transactionid,(e.firstname+ ' '+ e.lastname)as fullname,r.RoleId,rs.RoleName from users u,employee e,UsersInRoles r,Roles rs where transactionid=? and u.emp_number=e.emp_number and r.UserId=u.User_id  and rs.RoleId=r.RoleId "+
        " union "+
        " select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.datesignup, u.lastlogindate, u.dateupdated,u.active,u.datelogin,u.transactionid,(e.Employee_Name)as fullname,r.RoleId,rs.RoleName from users u,UnAuthorised_Employee e, UsersInRoles r,Roles rs where transactionid=? and u.user_name=e.Staffid_UserName and r.UserId=u.User_id and rs.RoleId=r.RoleId";


 public  String USER_DETILS_WITH_APP_LEVEL_USER ="select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,ur.RoleId,r.RoleName,(e.firstname +' '+ e.lastname) as fullname from users u,UsersInRoles ur,Roles r,employee e where  u.user_id=ur.UserId and r.RoleId=ur.RoleId and e.emp_number=u.emp_number and user_id !=? "+
         " union select u.user_id, u.emp_number, u.user_name, u.password, u.admin_user, u.superadmin, u.numlogins, u.active, u.dateupdated, u.lastlogindate, u.datelogin,ur.RoleId,r.RoleName,(e.Employee_Name) as fullname from users u,UsersInRoles ur,Roles r,UnAuthorised_Employee e where  u.user_id=ur.UserId and r.RoleId=ur.RoleId and e.Staffid_UserName=u.user_name and user_id !=?";
}



