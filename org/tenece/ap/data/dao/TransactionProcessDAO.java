/*
 * (c) Copyright 2009 The Tenece Professional Services.
 * 
 * ============================================================
 * Project Info:  http://tenece.com
 * Project Lead:  Aaron Osikhena (aaron.osikhena@tenece.com);
 * ============================================================
 *
 *
 * Licensed under the Tenece Professional Services;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.tenece.com/
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

package org.tenece.ap.data.dao;

import java.sql.CallableStatement;
//import org.tenece.hr.db.DatabaseQueryLoader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.tenece.ap.dao.db.DatabaseQueryLoader;
import org.tenece.web.common.DateUtility;
//import org.tenece.web.data.Appraisal;
import org.tenece.web.data.Company;
import org.tenece.web.data.EmailMessage;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Transaction;
import org.tenece.web.data.TransactionAudit;
import org.tenece.web.data.TransactionType;
import org.tenece.web.data.Users;
import org.tenece.web.mail.EmailImpl;
import org.tenece.web.services.MailService;

/**
 *
 * @author jeffry.amachree
 */
public class TransactionProcessDAO extends BaseDAO{

    private TransactionDAO transactionDAO = null;
    private EmployeeDAO employeeDAO = null;
    private MailService mailService;
    private UserDAO userDAO;
    
    /**
     * Creates a new instance of TransactionTypeDAO
     */
    public TransactionProcessDAO() {
    }

    public boolean generateDefaultApprovalRoute(long employeeId){
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        try{
            //call procedure
            param.add(employeeId); type.add("NUMBER");

            connection = this.getConnection(true);

            CallableStatement cs = executeProcedure(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_ROUTE_GENERATION_FOR_EMPLOYEE,
                    param, type, true, 2);
            if(cs == null){
                throw new Exception("Unable to generate workflow approval route. Code " + -99);
            }
            int returnValue = cs.getInt(2);
            if(returnValue != 0){
                throw new Exception("Unable to generate workflow approval route. Code " + returnValue);
            }
            
            //return success
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public long saveTransaction(Transaction transaction, Object object, String parentId)throws IllegalAccessException,Exception{
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        //variable to hold transaction id
        long transactionID = 0L;
        
        try{
            
            connection = this.getConnection(true);
            startTransaction(connection);
            param = new Vector();
            type = new Vector();
            
            int saved = 0;
            
            Long nextEmployeeID = 0L;
           
                nextEmployeeID = getTransactionDAO().getFirstApprover(transaction, connection);
                
                System.out.println("nextEmployeeID = "+nextEmployeeID);
            
            if(nextEmployeeID == null){
                throw new IllegalAccessException("No Approval level Defined");
                
            }

            //save transaction
            param.add(transaction.getDescription()); type.add("STRING");
            param.add(transaction.getStatus()); type.add("STRING");
            param.add(transaction.getBatchNumber()); type.add("NUMBER");
            param.add(transaction.getTransactionReference()); type.add("NUMBER");
            param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");
            int rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_INSERT, param, type);
            if(rowAffected == 0){
                throw new IllegalAccessException("Invalid Transaction Entry.");
            }
            //get the transaction id
            
            param = new Vector(); type = new Vector();
            param.add(transaction.getInitiator()); type.add("NUMBER");
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_SELECT_EMPLOYEE_MAX_ID, param, type);
            if(rst.next()){
               
                transactionID = rst.getLong(1);
            }else{
                throw new IllegalAccessException("Unable to get Transaction ID.");
            }
            transaction.setId(transactionID);
 

            //special check for appraisal
            System.out.println("parentId = "+parentId);
            if(parentId.trim().equals("USER_CREATION")){
                Users user = (Users)object;
                user.setNextApprovingOfficer(nextEmployeeID);
                object = user;
            }
            //check transaction parent id and process item...
          boolean  savedbol = initiateTransactionItem(object, parentId, transactionID, connection);
            if(savedbol == true)
            { 
              saved=1;
            }
            else{saved=0;}
            System.out.println("saved = " + saved);
            if(saved == 0){
                throw new IllegalAccessException("Unable to complete transaction, try again later");
            }

            //create init transaction audit
            TransactionAudit audit = new TransactionAudit();
            audit.setActionBy(transaction.getInitiator());
            audit.setActionDate(new Date());
            audit.setActionStatus("A");
            audit.setStatus("A");
            audit.setTransactionId(transactionID);

            //save init audit
            param = new Vector();
            type = new Vector();
            param.add(audit.getStatus()); type.add("STRING");
            param.add(audit.getActionStatus()); type.add("STRING");
            param.add(audit.getActionDate()); type.add("DATE");
            param.add(audit.getTransactionId()); type.add("NUMBER");
            param.add(audit.getActionBy()); type.add("NUMBER");

            rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_AUDIT_INSERT, param, type);
            System.out.println("rowAffected>>>>>init transaction audit = " + rowAffected);
            //save the next authorizer
            audit = new TransactionAudit();
            audit.setActionBy(nextEmployeeID);
            audit.setActionDate(new Date());
            audit.setActionStatus("P");
            audit.setStatus("P");
            audit.setTransactionId(transactionID);

            //save init audit
            param = new Vector();
            type = new Vector();
            param.add(audit.getStatus()); type.add("STRING");
            param.add(audit.getActionStatus()); type.add("STRING");
            param.add(audit.getActionDate()); type.add("DATE");
            param.add(audit.getTransactionId()); type.add("NUMBER");
            param.add(audit.getActionBy()); type.add("NUMBER");

            rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_AUDIT_INSERT, param, type);
             System.out.println("rowAffected>>>>>next authorizer audit = " + rowAffected);
            //commit transaction
            commitTransaction(connection);

            //send mail to initiator and approver (ignore even if there is error)
            try{
                Hashtable keys = new Hashtable();
                //initiator
                try{
                    Employee employee = getEmployeeDAO().getEmployeeBasicDataById(transaction.getInitiator());
                    
                    keys.put("EMAIL_TRANSACTION_NAME", transaction.getDescription());
                    keys.put("EMAIL_NAME", employee.getFullName());
                    keys.put("EMAIL_TRANSACTION_ID", String.valueOf(transaction.getTransactionReference()));
                    keys.put("EMAIL_TRANSACTION_DATE", DateUtility.getDateAsString(new Date(),"dd/MM/yyyy"));

                    int sent = getMailService().sendEmail("TRANX_INIT", transaction.getInitiator(), keys);
                    System.out.println("mail sent: " + sent);
                }catch(Exception im){
                    im.printStackTrace();
                    System.out.println("Can not sent initiator mail for" + transaction.getId()); }
                
                //approver
                try{
                    Employee employee = getEmployeeDAO().getEmployeeBasicDataById(nextEmployeeID);
                    
                    keys = new Hashtable();
                    keys.put("EMAIL_TRANSACTION_NAME", transaction.getDescription());
                    keys.put("EMAIL_NAME", employee.getFullName());
                    keys.put("EMAIL_TRANSACTION_ID", String.valueOf(transaction.getTransactionReference()));
                    keys.put("EMAIL_TRANSACTION_DATE", DateUtility.getDateAsString(new Date(),"dd/MM/yyyy"));

                    int sent = getMailService().sendEmail("TRANX_APPROVER", nextEmployeeID, keys);
                    System.out.println("mail sent: " + sent);
                }catch(Exception im){
                    im.printStackTrace();
                    System.out.println("Can not sent initiator mail for" + transaction.getId()); }
                
            }catch(Exception e){} //handle exception

            //return the successful transaction ID
            return transactionID;
        }catch(Exception e){
            e.printStackTrace();
            try {
                rollbackTransaction(connection);
            } catch (SQLException ex) {
                Logger.getLogger(TransactionProcessDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw e;
           // return 0L;
        }
    }

    public boolean initiateTransactionItem(Object object, String parentId, long transactionID, Connection connection) throws Exception{
        boolean saved = false;
        if(parentId.trim().equals("USER_CREATION")){
            Users target = (Users)object;
            target.setTransactionId(transactionID);
            target.setActive("P"); 
            saved = getUserDAO().insert_User(target, connection);
        }
       else if(parentId.trim().equals("BULK_USER_CREATION")){
            List<Users> target = (List<Users>)object;
 
            saved = getUserDAO().saveBulkUsersList(target,(int)transactionID,"P", connection);
        }
        else if(parentId.trim().equals("USER_DELETION")){
        
 
            Users target = (Users)object;
            target.setTransactionId(transactionID);
            target.setActive("P"); 
            saved = getUserDAO().updateUsersDuringDeleteTransation(target, connection);
        }
        return saved;
    }

    public boolean isFinalAuthorizer(Transaction transaction, Connection connection) throws  Exception{
        try{
            long countedLevel = getTransactionDAO().getNextLevelIndex(transaction, connection);
            if(countedLevel == 1){
                return true;
            }else if(countedLevel > 1){
                return false;
            }else{
                throw new Exception("Unable to verify user status");
            }
        }catch(Exception e){
            throw e;
        }
    }
    public boolean isFinalAuthorizerByDefault(Transaction transaction, Connection connection) throws  Exception{
        try{
            long countedLevel = getTransactionDAO().getNextLevelIndexByDefault(transaction, connection);
            if(countedLevel == 1){
                return true;
            }else if(countedLevel > 1){
                return false;
            }else{
                throw new Exception("Unable to verify user status");
            }
        }catch(Exception e){
            throw e;
        }
    }
    public boolean updateTransaction(Transaction transaction, int authorizer){
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            TransactionType transactionType = getTransactionDAO().getTransactionTypeById(transaction.getTransactionType());


            
            connection = this.getConnection(true);
            startTransaction(connection);

            boolean isFinalAuthorizer = false;

            //check if the authorizer is the last user. if he/she is then set a final status to transaction
          
                isFinalAuthorizer = isFinalAuthorizer(transaction, connection);
          
            if(transaction.getStatus().trim().equals("R")){
                transaction.setStatus("R");
            }else{
                if(isFinalAuthorizer){
                    transaction.setStatus("A");
                }else{
                    transaction.setStatus("P");
                }
            }
            //save transaction
            param.add(transaction.getStatus()); type.add("STRING");
            param.add(transaction.getId()); type.add("NUMBER");

            int rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_UPDATE_STATUS, param, type);
            if(rowAffected == 0){
                throw new Exception("Invalid Transaction Entry.");
            }

            //create new audit
            TransactionAudit audit = new TransactionAudit();
            audit.setActionBy(authorizer);
            audit.setActionDate(new Date());
            if(transaction.getStatus().trim().equals("R")){
                audit.setActionStatus("R");
                audit.setStatus("R");
            }else{
                audit.setActionStatus("A");
                audit.setStatus("A");
            }
            audit.setTransactionId(transaction.getId());

            //save audit
            param = new Vector();
            type = new Vector();
            param.add(audit.getStatus()); type.add("STRING");
            param.add(audit.getActionStatus()); type.add("STRING");
            param.add(audit.getActionDate()); type.add("DATE");
            param.add(audit.getTransactionId()); type.add("NUMBER");
            param.add(audit.getActionBy()); type.add("NUMBER");

            rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_AUDIT_UPDATE_BY_TRANSACTION_AND_ACTIONBY, param, type);

            //check if there is still another authorizer to approve transaction
            Long nextAuthorizer = 0L;
            if(isFinalAuthorizer == false && (!transaction.getStatus().trim().equals("R"))){
               
                nextAuthorizer = getTransactionDAO().getNextApprover(transaction, connection);
               
                System.out.println("nextAuthorizer === "+nextAuthorizer);
                audit = new TransactionAudit();
                audit.setActionBy(nextAuthorizer);
                audit.setActionDate(new Date());
                audit.setActionStatus("P");
                audit.setStatus("P");
                audit.setTransactionId(transaction.getId());

                //save audit
                param = new Vector();
                type = new Vector();
                param.add(audit.getStatus()); type.add("STRING");
                param.add(audit.getActionStatus()); type.add("STRING");
                param.add(audit.getActionDate()); type.add("DATE");
                param.add(audit.getTransactionId()); type.add("NUMBER");
                param.add(audit.getActionBy()); type.add("NUMBER");

                rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_AUDIT_INSERT, param, type);

            }//:~if(isFinalAuthorizer == false)

            if(transaction.getStatus().trim().equals("R")){
                //reject both transaction and items
                int saved = rejectTransaction(transaction, transactionType, connection, authorizer);
                //check if update was ,successful
                if(saved == 0){
                    throw new Exception("Unable to complete transaction");
                }
            }else{

                //finalize transaction item
                if(isFinalAuthorizer == true && (!transaction.getStatus().trim().equals("R"))){
                    int saved = 0;
                    //finalize the transaction and the items
                    saved = this.finalizeTransaction(transaction, transactionType, connection, authorizer);

                    //confirm if the update was successful
                    if(saved == 0){
                        throw new Exception("Unable to complete transaction");
                    }
                }
            }//:else for reject transaction
            commitTransaction(connection);

            //send mail to initiator and approver (ignore even if there is error)
            try{
                if(transaction.getStatus().trim().equals("R")){
//                    EmailMessage emailMessage = getCompanyDAO().getEmailMessageByCode("TRANX_REJECT");
//                    Employee employee = getEmployeeDAO().getEmployeeBasicDataById(transaction.getInitiator());
//                    Employee contact = getEmployeeDAO().getEmployeeContactDataById(transaction.getInitiator());
//                    Hashtable keys = new Hashtable();
//                    keys.put("EMAIL_TRANSACTION_NAME", transaction.getDescription());
//                    keys.put("EMAIL_NAME", employee.getFullName());
//                    keys.put("EMAIL_TRANSACTION_ID", String.valueOf(transaction.getId()));
//
//                    String mailMessage = EmailImpl.formatEmailMessage(emailMessage.getMessage(), keys);
//                    int sent = EmailImpl.sendEmail(employee.getEmail(), emailMessage.getSenderEmail(),
//                            "", "", emailMessage.getSubject(), mailMessage, new Vector());
//
//                    System.out.println("rejected mail sent: " + sent);

                }else if(isFinalAuthorizer == true && (!transaction.getStatus().trim().equals("R"))){

//                    EmailMessage emailMessage = getCompanyDAO().getEmailMessageByCode("TRANX_COMPLETE");
//                    Employee employee = getEmployeeDAO().getEmployeeBasicDataById(transaction.getInitiator());
//                    Employee contact = getEmployeeDAO().getEmployeeContactDataById(transaction.getInitiator());
//                    Hashtable keys = new Hashtable();
//                    keys.put("EMAIL_TRANSACTION_NAME", transaction.getDescription());
//                    keys.put("EMAIL_NAME", employee.getFullName());
//                    keys.put("EMAIL_TRANSACTION_ID", String.valueOf(transaction.getId()));
//
//                    String mailMessage = EmailImpl.formatEmailMessage(emailMessage.getMessage(), keys);
//                    int sent = EmailImpl.sendEmail(employee.getEmail(), emailMessage.getSenderEmail(),
//                            "", "", emailMessage.getSubject(), mailMessage, new Vector());
//
//                    System.out.println("completed mail sent: " + sent);

                }else if(isFinalAuthorizer == false && (!transaction.getStatus().trim().equals("R"))){
//                    EmailMessage emailMessage = getCompanyDAO().getEmailMessageByCode("TRANX_APPROVER");
//                    Employee employee = getEmployeeDAO().getEmployeeBasicDataById(nextAuthorizer);
//                    //Employee contact = getEmployeeDAO().getEmployeeContactDataById(nextAuthorizer);
//                    // get the authorize into
//                    Employee authorizerInfo = getEmployeeDAO().getEmployeeBasicDataById(authorizer);
//                    Hashtable initiatorInfokey = getEmployeeDAO().getTransactionInitiator(transaction.getTransactionReference(),transaction.getInitiator());
//                    Hashtable keys = new Hashtable();
//                    keys.put("EMAIL_TRANSACTION_NAME", transaction.getDescription());
//                    keys.put("EMAIL_NAME", employee.getFullName());
//                    keys.put("EMAIL_TRANSACTION_ID", String.valueOf(transaction.getTransactionReference()));
//                    keys.put("EMAIL_TRANSACTION_DATE", initiatorInfokey.get("EMAIL_TRANSACTION_DATE"));
//                    keys.put("EMAIL_TRANSACTION_INITIATED_BY", initiatorInfokey.get("EMAIL_TRANSACTION_INITIATED_BY"));
//                    keys.put("EMAIL_TRANSACTION_FORWARDED_BY", authorizerInfo.getFullName());
//                    String mailMessage = EmailImpl.formatEmailMessage(emailMessage.getMessage(), keys);
//                    int sent = EmailImpl.sendEmail(employee.getEmail(), emailMessage.getSenderEmail(),
//                            "", "", emailMessage.getSubject(), mailMessage, new Vector());
//
//                    System.out.println("approver mail sent: " + sent);

                }
                

            }catch(Exception e){}
        }catch(Exception e){
            e.printStackTrace();
            try {
                rollbackTransaction(connection);
            } catch (SQLException ex) {
                Logger.getLogger(TransactionProcessDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
        return true;
    }

    private int finalizeTransaction(Transaction transaction, TransactionType transactionType, Connection connection, long supervisor) throws Exception{
        int saved = 0;
        //check transaction parent id
        if(transactionType.getParentId().trim().equals("USER_CREATION")){
           Users  user = getUserDAO().getUserByTransaction(connection, transaction.getId()); 
            user.setActive("1");
            saved = getUserDAO().updateUsers(user, connection);
        }
        else if(transactionType.getParentId().trim().equals("BULK_USER_CREATION")){
           List<Users>  userList = getUserDAO().getBulkUserByTransaction(connection, transaction.getId());
           for (Users user : userList) {
                user.setActive("1");
                saved = getUserDAO().updateUsers(user, connection);
            }
            
        }
        else if(transactionType.getParentId().trim().equals("USER_DELETION")){
           Users  user = getUserDAO().getUserByTransaction(connection, transaction.getId()); 
            
            saved = getUserDAO().deleteUsersAproval(user, user.getUserName(),connection);
        }
        return saved;
    }

    private int rejectTransaction(Transaction transaction, TransactionType transactionType, Connection connection, long supervisor) throws Exception{
        int saved = 0;
        
        //check transaction parent id
        if(transactionType.getParentId().trim().equals("USER_CREATION")){
            Users target = getUserDAO().getUserByTransaction(connection, transaction.getId()); //(PerformanceTarget)object;
            target.setActive("R");
            saved = getUserDAO().updateUsers(target, connection);
        }
        else if(transactionType.getParentId().trim().equals("BULK_USER_CREATION")){
           List<Users>  userList = getUserDAO().getBulkUserByTransaction(connection, transaction.getId());
          
           for (Users user : userList) {
                user.setActive("R");
                saved = getUserDAO().updateUsers(user, connection);
              
            }
            
        }
         else if(transactionType.getParentId().trim().equals("USER_DELETION")){
            Users target = getUserDAO().getUserByTransaction(connection, transaction.getId()); //(PerformanceTarget)object;
            target.setActive("1");
            saved = getUserDAO().updateUsers(target, connection);
        }
        if(saved == 0){
            throw new Exception("Unable to complete transaction");
        }
        return saved;
    }
    /**
     * @return the targetDAO
     */
    public UserDAO getUserDAO() {
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    /**
     * @return the transactionDAO
     */
    public TransactionDAO getTransactionDAO() {
        if(transactionDAO == null){
            transactionDAO = new TransactionDAO();
        }
        return transactionDAO;
    }

  

    /**
     * @return the employeeDAO
     */
    public EmployeeDAO getEmployeeDAO() {
        if(employeeDAO == null){
            employeeDAO = new EmployeeDAO();
        }
        return employeeDAO;
    }

   
    /**
     * @return the mailService
     */
    public MailService getMailService() {
        if(mailService == null){
            mailService = new MailService();
        }
        return mailService;
    }

    /**
     * @param mailService the mailService to set
     */
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
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
    
}
