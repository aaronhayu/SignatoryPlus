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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tenece.ap.dao.db.DatabaseQueryLoader;
import org.tenece.web.data.ApprovalRoute;
import org.tenece.web.data.Company;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Transaction;
import org.tenece.web.data.TransactionAudit;
import org.tenece.web.data.TransactionType;
import org.tenece.web.data.Users;

/**
 *
 * @author jeffry.amachree
 */
public class TransactionDAO extends BaseDAO{
   // private CompanyDAO companyDAO;
    private EmployeeDAO employeeDAO;
    private TransactionProcessDAO transactionProcessDAO;
    
    /**
     * Creates a new instance of TransactionTypeDAO
     */
    public TransactionDAO() {
    }
    
    /* ************* TransactionType ********** */
    public List<TransactionType> getAllTransactionType(){
        Connection connection = null;
        List<TransactionType> records = new ArrayList<TransactionType>();
        try{
            connection = this.getConnection(true);
            ResultSet rst = this.executeQuery(DatabaseQueryLoader.getQueryAssignedConstant().TRANS_TYPE_SELECT, connection);
            
            while(rst.next()){
                TransactionType transType = new TransactionType();
                transType.setId(rst.getInt("id"));
                transType.setDescription(rst.getString("description"));
                transType.setApprovalURL(rst.getString("approval_url"));
                transType.setParentId(rst.getString("parentid"));
                
                records.add(transType);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return records;
    }

    public TransactionType getTransactionTypeById(long id){
        Connection connection = null;
        TransactionType transactionType = null;
        try{
            Vector param = new Vector();
            Vector type = new Vector();

            param.add(id); type.add("NUMBER");
            
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANS_TYPE_SELECT_BY_ID, param, type);

            if(rst.next()){
                TransactionType transType = new TransactionType();
                transType.setId(rst.getInt("id"));
                transType.setDescription(rst.getString("description"));
                transType.setApprovalURL(rst.getString("approval_url"));
                transType.setParentId(rst.getString("parentid"));

                transactionType = transType;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return transactionType;
    }

    public TransactionType getTransactionTypeByParent(String parentId){
        Connection connection = null;
        TransactionType transactionType = null;
        try{
            Vector param = new Vector();
            Vector type = new Vector();

            param.add(parentId); type.add("STRING");

            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANS_TYPE_SELECT_BY_PARENT, param, type);

            if(rst.next()){
                TransactionType transType = new TransactionType();
                transType.setId(rst.getInt("id"));
                transType.setDescription(rst.getString("description"));
                transType.setApprovalURL(rst.getString("approval_url"));
                transType.setParentId(rst.getString("parentid"));

                transactionType = transType;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return transactionType;
    }

        public List<ApprovalRoute> getUserApprovalRoute(int userId){
        Connection connection = null;
        List<ApprovalRoute> records = new ArrayList<ApprovalRoute>();
        Vector param = new Vector();
        Vector type = new Vector();
        
        try{
            param.add(userId); type.add("NUMBER");
            param.add(userId); type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_ROUTE_SELECT_BY_EMP, param, type);

            while(rst.next()){
                ApprovalRoute route = new ApprovalRoute();
                route.setId(rst.getInt("id"));
                route.setApprovalLevel(rst.getInt("approval_level"));
                route.setUserId(rst.getInt("user_id"));
                route.setTransactionType(rst.getLong("transactiontypeid"));
                route.setAuthorizer(rst.getInt("authorizer"));
                route.setAuthorizerName(rst.getString("fullname"));
                records.add(route);
            }
        }catch(Exception e){
            
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return records;
    }

    public List<ApprovalRoute> getEmployeeApprovalRouteByDefault(long employeeId){
        Connection connection = null;
        List<ApprovalRoute> records = new ArrayList<ApprovalRoute>();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(employeeId); type.add("NUMBER");

            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_ROUTE_GENERATED_SELECT_BY_EMP, param, type);

            while(rst.next()){
                ApprovalRoute route = new ApprovalRoute();
                route.setApprovalLevel(rst.getInt("approval_level"));
                route.setUserId(rst.getInt("user_id"));
                route.setAuthorizerName(rst.getString("approver_name"));

                records.add(route);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return records;
    }

    public boolean saveApprovalRoute(List<ApprovalRoute> routes){
        Connection connection = null;
        List<ApprovalRoute> records = new ArrayList<ApprovalRoute>();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            connection = this.getConnection(true);
            startTransaction(connection);

            //delete all records for that employee
            param.add(routes.get(0).getUserId()); type.add("NUMBER");
            int deleted = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_ROUTE_DELETE, param, type);

            //navigate and save the new record
            for(ApprovalRoute route: routes){
                //re-initialize parameter object
                param = new Vector();
                type = new Vector();
                //set values in parameter
                param.add(route.getApprovalLevel()); type.add("NUMBER");
                param.add(route.getUserId()); type.add("NUMBER");
                param.add(route.getTransactionType()); type.add("NUMBER");
                param.add(route.getAuthorizer()); type.add("NUMBER");

                int rows = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_ROUTE_INSERT, param, type);

            }

            commitTransaction(connection);
            return true;
        }catch(Exception e){
            try {
                rollbackTransaction(connection);
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int isApprovalRouteDefined(String parentId, long employeeId){
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();
        
        try{
            //get Employee Record
            Employee employee = getEmployeeDAO().getEmployeeBasicDataById(employeeId);
            //get company record
            
            
            connection = this.getConnection(true);

            //set values in parameter
            param.add(parentId); type.add("STRING");
            param.add(employeeId); type.add("NUMBER");
            
            ResultSet rst = null;
          
                //generate approval route
                boolean generated = getTransactionProcessDAO().generateDefaultApprovalRoute(employeeId);
                //check if route was generated
                if(generated == false){
                    throw new Exception("Unable to generate Default Approval Route.");
                }
                rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_ROUTE_IS_DEFINED_FOR_DEFAULT_TRANSACTION, param, type);
            
            System.out.println("checking QRY to use: " + DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_ROUTE_IS_DEFINED_FOR_DEFAULT_TRANSACTION);

            if(rst.next()){
                Integer maxLevelDefined = rst.getInt(1);
                if(maxLevelDefined == null){
                    
                    maxLevelDefined = 0;
                }
                return maxLevelDefined;
            }else{
                return 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /* --------------------- Transaction Table and Audit ------------------------- */
    public List<Transaction> getAllTransactionByEmployee(int employeeId){
        Connection connection = null;
        List<Transaction> records = new ArrayList<Transaction>();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(employeeId); type.add("NUMBER");

            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_SELECT_BY_EMPLOYEE, param, type);

            while(rst.next()){
                Transaction trnx = new Transaction();
                trnx.setId(rst.getInt("id"));
                trnx.setDescription(rst.getString("description"));
                trnx.setStatus(rst.getString("status"));
                trnx.setBatchNumber(rst.getLong("batch_number"));
                trnx.setTransactionReference(rst.getLong("transaction_ref"));
                trnx.setTransactionType(rst.getLong("transactiontypeid"));
                trnx.setInitiator(rst.getInt("initiator_id"));

                records.add(trnx);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return records;
    }

    public List<Transaction> getAllTransactionStatusByEmployee(long employeeId){
        return getAllTransactionStatusByEmployee(employeeId, null, null);
    }
    public List<Transaction> getAllTransactionStatusByEmployee(long employeeId, String criteria, String searchText){
        Connection connection = null;
        List<Transaction> records = new ArrayList<Transaction>();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(employeeId); type.add("NUMBER");

            connection = this.getConnection(true);
            ResultSet rst = null;
            if(criteria == null || searchText == null){
                rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_STATUS_SELECT, param, type);
            }else{
                String query = "";

                if(criteria.equalsIgnoreCase("NAME")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_STATUS_SELECT_SEARCH_BY_NAME;
                }else if(criteria.equalsIgnoreCase("STATUS")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_STATUS_SELECT_SEARCH_BY_STATUS;
                }

                query = query.replaceAll("_SEARCH_", searchText);

                rst = this.executeParameterizedQuery(connection, query, param, type);
            }

            while(rst.next()){
                Transaction trnx = new Transaction();
                trnx.setId(rst.getInt("id"));
                trnx.setStatus(rst.getString("status"));
                trnx.setDescription(rst.getString("description"));
                trnx.setInitiatedDate(new java.util.Date(rst.getDate("initiated_date").getTime()));
                trnx.setInitiator(rst.getInt("initiator_id"));

                records.add(trnx);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return records;
    }

    public Transaction getTransactionById(long transactionId){
        Connection connection = null;
        Transaction transaction = new Transaction();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(transactionId); type.add("NUMBER");

            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_SELECT_BY_ID, param, type);

            while(rst.next()){
                Transaction trnx = new Transaction();
                trnx.setId(rst.getInt("id"));
                trnx.setDescription(rst.getString("description"));
                trnx.setStatus(rst.getString("status"));
                trnx.setBatchNumber(rst.getLong("batch_number"));
                trnx.setTransactionReference(rst.getLong("transaction_ref"));
                trnx.setTransactionType(rst.getLong("transactiontypeid"));
                trnx.setInitiator(rst.getInt("initiator_id"));

                transaction = trnx;
            }
        }catch(Exception e){
            return null;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return transaction;
    }

    public List<Transaction> getInboxTransaction(long userId, String searchKey, String searchValue){
        Connection connection = null;
        List<Transaction> transactions = new ArrayList<Transaction>();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(userId); type.add("NUMBER");
            param.add(userId); type.add("NUMBER");
            connection = this.getConnection(true);
            ResultSet rst = null;
            //check search key and value
            if(searchKey == null || searchValue == null){
                rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_INBOX_SELECT, param, type);
            }else {
                String query = "";
                if(searchKey.trim().equals("TYPE")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_INBOX_SELECT_SEARCH_TRANSTYPE;
                    query = query.replaceAll("_SEARCH_", searchValue);
                }else if(searchKey.trim().equals("INIT")){
                    query = DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_INBOX_SELECT_SEARCH_INITIATOR;
                    query = query.replaceAll("_SEARCH_", searchValue);
                }
                rst = this.executeParameterizedQuery(connection, query, param, type);
            }

            while(rst.next()){
                Transaction trnx = new Transaction();
                trnx.setId(rst.getInt("id"));
                trnx.setDescription(rst.getString("description"));
                trnx.setStatus(rst.getString("status"));
                trnx.setBatchNumber(rst.getLong("batch_number"));
                trnx.setTransactionReference(rst.getLong("transaction_ref"));
                trnx.setTransactionType(rst.getLong("transactiontypeid"));
                trnx.setInitiator(rst.getInt("initiator_id"));
                Users user = new Users();
                user.setId((int)trnx.getInitiator());
                user.setFullnames(rst.getString("fullname"));
                
                TransactionType transType = new TransactionType();
                transType.setId((int)trnx.getTransactionType());
                transType.setDescription(rst.getString("transtypedesc"));
                trnx.setUserObj(user);
                trnx.setTranTypeObject(transType);
                transactions.add(trnx);
            }
        }catch(Exception e){
            return null;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return transactions;
    }

    public boolean saveTransaction(Transaction transaction){
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            
            //get connection to access database
            connection = this.getConnection(true);
            startTransaction(connection);

            //save transaction
            param.add(transaction.getDescription()); type.add("STRING");
            param.add(transaction.getStatus()); type.add("STRING");
            param.add(transaction.getBatchNumber()); type.add("NUMBER");
            param.add(transaction.getTransactionReference()); type.add("NUMBER");
            param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");

            int rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_INSERT, param, type);
            if(rowAffected == 0){
                throw new Exception("Invalid Transaction Entry.");
            }
            //get the transaction id
            long transactionID = 0L;
            param = new Vector(); type = new Vector();
            param.add(transaction.getInitiator()); type.add("NUMBER");
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_SELECT_EMPLOYEE_MAX_ID, param, type);
            if(rst.next()){
                transactionID = rst.getLong(1);
            }else{
                throw new Exception("Unable to get Transaction ID.");
            }
            System.out.println("Transaction id to use for audit: " + transactionID);

            //create new audit
            TransactionAudit audit = new TransactionAudit();
            audit.setActionBy(transaction.getInitiator());
            audit.setActionDate(new Date());
            audit.setActionStatus("A");
            audit.setStatus("A");
            audit.setTransactionId(transactionID);

            //save audit
            param = new Vector();
            type = new Vector();
            param.add(audit.getStatus()); type.add("STRING");
            param.add(audit.getActionStatus()); type.add("STRING");
            param.add(audit.getActionDate()); type.add("DATE");
            param.add(audit.getTransactionId()); type.add("NUMBER");
            param.add(audit.getActionBy()); type.add("NUMBER");

            rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_AUDIT_INSERT, param, type);
            
            commitTransaction(connection);
        }catch(Exception e){
            try {
                rollbackTransaction(connection);
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
    public boolean updateTransaction(Transaction transaction){
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            connection = this.getConnection(true);
            startTransaction(connection);

            //save transaction
            param.add(transaction.getStatus()); type.add("STRING");
            param.add(transaction.getId()); type.add("NUMBER");

            int rowAffected = this.executeParameterizedUpdate(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_UPDATE_STATUS, param, type);
            if(rowAffected == 0){
                throw new Exception("Invalid Transaction Entry.");
            }

            //create new audit
            TransactionAudit audit = new TransactionAudit();
            audit.setActionBy(transaction.getInitiator());
            audit.setActionDate(new Date());
            audit.setActionStatus("A");
            audit.setStatus("A");
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

            commitTransaction(connection);
        }catch(Exception e){
            try {
                rollbackTransaction(connection);
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    public List<TransactionAudit> getAllTransactionAuditByTransaction(long transactionId){
        Connection connection = null;
        List<TransactionAudit> record = new ArrayList<TransactionAudit>();
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(transactionId); type.add("NUMBER");

            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().TRANSACTION_AUDIT_SELECT_BY_TRANSACTION, param, type);

            while(rst.next()){
                TransactionAudit trnx = new TransactionAudit();
                trnx.setStatus(rst.getString("status"));
                trnx.setActionStatus(rst.getString("action_status"));
                trnx.setActionDate(new Date(rst.getDate("action_date").getTime()));
                trnx.setTransactionId(rst.getLong("transaction_id"));
                trnx.setActionBy(rst.getInt("action_by"));
                trnx.setActionName(rst.getString("firstname") + " " + rst.getString("lastname"));

                record.add(trnx);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return record;
    }

    public Long getFirstApprover(Transaction transaction, Connection connection){
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");

            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_FIRST_LEVEL_FOR_TRANSACTION, param, type);

            if(rst.next()){
                long nextApproverID = rst.getInt("authorizer");

                return nextApproverID;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Long getFirstApproverByDefault(Transaction transaction, Connection connection){
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            //param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");

            ResultSet rst = this.executeParameterizedQuery(connection,
                    DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_FIRST_LEVEL_BY_DEFAULT, param, type);

            if(rst.next()){
                long nextApproverID = rst.getInt("authorizer");

                return nextApproverID;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Long getNextApprover(Transaction transaction, Connection connection){
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(transaction.getId()); type.add("NUMBER");
            param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");

            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_NEXT_LEVEL_FOR_TRANSACTION, param, type);

            if(rst.next()){
                long nextApproverID = rst.getInt("authorizer");
                System.out.println();
                return nextApproverID;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Long getNextApproverByDefault(Transaction transaction, Connection connection){
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(transaction.getId()); type.add("NUMBER");
            //param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");
            param.add(transaction.getId()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");

            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_NEXT_LEVEL_BY_DEFAULT, param, type);

            if(rst.next()){
                long nextApproverID = rst.getInt("authorizer");

                return nextApproverID;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Long getNextPendingApprover(Transaction transaction){
        Connection connection = null;
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(transaction.getId()); type.add("NUMBER");

            connection = this.getConnection(true);

            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_NEXT_PENDING_LEVEL_FOR_TRANSACTION, param, type);

            if(rst.next()){
                long nextApproverID = rst.getInt("action_by");

                return nextApproverID;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Long getNextLevelIndex(Transaction transaction, Connection connection){
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");
            param.add(transaction.getId()); type.add("NUMBER");

            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_CONFIRM_NEXT_LEVEL_AVAILABILITY, param, type);

            if(rst.next()){
                long nextLevelIndex = rst.getInt(1);

                return nextLevelIndex;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public Long getNextLevelIndexByDefault(Transaction transaction, Connection connection){
        Vector param = new Vector();
        Vector type = new Vector();

        try{
            //param.add(transaction.getTransactionType()); type.add("NUMBER");
            param.add(transaction.getInitiator()); type.add("NUMBER");
            param.add(transaction.getId()); type.add("NUMBER");

            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().APPROVAL_CONFIRM_NEXT_LEVEL_AVAILABILITY_BY_DEFAULT, param, type);

            if(rst.next()){
                long nextLevelIndex = rst.getInt(1);

                return nextLevelIndex;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
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
     * @param employeeDAO the employeeDAO to set
     */
    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    /**
     * @return the transactionProcessDAO
     */
    public TransactionProcessDAO getTransactionProcessDAO() {
        if(transactionProcessDAO == null){
            transactionProcessDAO = new TransactionProcessDAO();
        }
        return transactionProcessDAO;
    }

    /**
     * @param transactionProcessDAO the transactionProcessDAO to set
     */
    public void setTransactionProcessDAO(TransactionProcessDAO transactionProcessDAO) {
        this.transactionProcessDAO = transactionProcessDAO;
    }
    

}
