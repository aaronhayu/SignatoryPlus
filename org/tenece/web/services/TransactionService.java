
/*
 * (c) Copyright 2009 The Tenece Professional Services.
 *
 * Created on 27 May 2009, 12:45
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
 * even if Tenece Professional Services has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package org.tenece.web.services;

import java.util.List;
import org.tenece.ap.data.dao.EmployeeDAO;
import org.tenece.ap.data.dao.TransactionDAO;
import org.tenece.ap.data.dao.TransactionProcessDAO;
import org.tenece.web.data.ApprovalRoute;
import org.tenece.web.data.Transaction;
import org.tenece.web.data.TransactionAudit;
import org.tenece.web.data.TransactionType;

/**
 *
 * @author jeffry.amachree
 */
public class TransactionService extends BaseService{
    private TransactionDAO transactionDAO = null;
    private TransactionProcessDAO transactionProcessDAO = null;
    private EmployeeDAO employeeDAO = null;
    
    /** Creates a new instance of PayrollService */
    public TransactionService() {
    }

    public boolean generateDefaultApprovalRoute(long employeeId){
        return getTransactionProcessDAO().generateDefaultApprovalRoute(employeeId);
    }

    public List<TransactionType> findAllTransactionType(){
        return getTransactionDAO().getAllTransactionType();
    }

    public TransactionType findTransactionTypeById(long id){
        return getTransactionDAO().getTransactionTypeById(id);
    }

    public TransactionType findAllTransactionType(String parentID){
        return getTransactionDAO().getTransactionTypeByParent(parentID);
    }

    public List<ApprovalRoute> findAllApprovalRoute(int userId){
        return getTransactionDAO().getUserApprovalRoute(userId);
    }

    public List<ApprovalRoute> findAllApprovalRouteByDefault(long employeeId){
        return getTransactionDAO().getEmployeeApprovalRouteByDefault(employeeId);
    }

    public boolean saveApprovalRoute(List<ApprovalRoute> routes){
        try{
            return getTransactionDAO().saveApprovalRoute(routes);
        }catch(Exception e){
            return false;
        }
    }
    
    public Long getFirstApprovalLevel(long transactionId) throws Exception {
        Transaction trnx = getTransactionDAO().getTransactionById(transactionId);
        return getTransactionDAO().getFirstApprover(trnx, getTransactionDAO().getConnection(false));
    }

    public Long getNextApprovalLevel(long transactionId) throws Exception{
        Transaction trnx = getTransactionDAO().getTransactionById(transactionId);
        return getTransactionDAO().getNextApprover(trnx, getTransactionDAO().getConnection(false));
    }

    public Long confirmNextApprovalLevel(long transactionId) throws Exception{
        Transaction trnx = getTransactionDAO().getTransactionById(transactionId);
        return getTransactionDAO().getNextLevelIndex(trnx, getTransactionDAO().getConnection(false));
    }

    /*-------------- Transaction table and audit -------------------------- */

    public List<Transaction> findAllTransactionStatusByEmployee(long employeemId){
        return getTransactionDAO().getAllTransactionStatusByEmployee(employeemId);
    }
    public List<Transaction> findAllTransactionStatusByEmployee(long employeemId, String criteria, String searchText){
        return getTransactionDAO().getAllTransactionStatusByEmployee(employeemId, criteria, searchText);
    }

    public List<Transaction> findAllTransactionForEmployee(int employeemId){
        return getTransactionDAO().getAllTransactionByEmployee(employeemId);
    }

    public List<Transaction> findInboxTransactions(int userId){
        return findInboxTransactions(userId, null, null);
    }
    public List<Transaction> findInboxTransactions(int userId, String searchKey, String searchValue){
        List<Transaction> trans = getTransactionDAO().getInboxTransaction(userId, searchKey, searchValue);
        
        return trans;
    }

    public Transaction findTransactionById(long transactionId){
        return getTransactionDAO().getTransactionById(transactionId);
    }

    private boolean saveTransaction(Transaction transaction){
        return getTransactionDAO().saveTransaction(transaction);
    }

    public boolean updateTransaction(Transaction transaction, int mode){
        if(mode == this.MODE_INSERT){
            return saveTransaction(transaction);
        }else if(mode == this.MODE_UPDATE){
            return getTransactionDAO().updateTransaction(transaction);
        }else{
            return false;
        }
    }//:~

    public List<TransactionAudit> findAllTransactionAudit(long transactionId){
        return getTransactionDAO().getAllTransactionAuditByTransaction(transactionId);
    }
    
    
    public boolean processTransaction(Transaction transaction, int authorizer) throws Exception{
        return getTransactionProcessDAO().updateTransaction(transaction, authorizer);
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
     * @return the transactionProcessDAO
     */
    public TransactionProcessDAO getTransactionProcessDAO() {
        if(transactionProcessDAO == null){
            transactionProcessDAO = new TransactionProcessDAO();
        }
        return transactionProcessDAO;
    }
}
