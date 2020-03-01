
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 27 May 2009, 08:49
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

package org.tenece.web.services;

import java.util.Date;
import java.util.List;
import org.tenece.ap.data.dao.TransactionDAO;
import org.tenece.ap.data.dao.TransactionProcessDAO;
import org.tenece.web.common.DateUtility;
import org.tenece.web.data.EmailMessage;
import org.tenece.web.data.Transaction;
import org.tenece.web.data.TransactionType;
/**
 *
 * @author jeffry.amachree
 */
public class BaseService {
    
    public static int MODE_INSERT = 0;
    public static int MODE_UPDATE = 1;
     private TransactionProcessDAO transactionProcessDAO;
    private TransactionDAO transactionDAO;
    private String message = "";
    private String errorMessage = "";
    /** Creates a new instance of BaseService */
    public BaseService() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EmailMessage findEmailMessageByCode(String code){
        return null;//getCompanyDAO().getEmailMessageByCode(code);
    }
    /**
     * 
     * @return
     */
    public TransactionProcessDAO getTransactionProcessDAO() {
        if(transactionProcessDAO == null){
            transactionProcessDAO = new TransactionProcessDAO();
        }
        return transactionProcessDAO;
    }
   public long initiateTransaction(Object targetObject, String description, long initiator, String transactionTypeParentID)throws IllegalAccessException, Exception {
        try{
            Transaction transaction  = new Transaction();
            transaction.setDescription(description);
            transaction.setBatchNumber(0);
            transaction.setInitiator(initiator);
            transaction.setStatus("P");
            transaction.setTransactionReference(Long.parseLong(DateUtility.getDateAsString(new Date(), "yyyyMMddHHmmss"))*2);
            transaction.setTransactionType(getTransactionDAO().getTransactionTypeByParent(transactionTypeParentID).getId());
            long transactionID = getTransactionProcessDAO().saveTransaction(transaction, targetObject, transactionTypeParentID);

            return transactionID;
        }
        catch(IllegalAccessException ac)
        {
          
            throw new Exception(ac.getMessage());
        }
        catch(Exception e){
            
            throw e;
        }
        
    }
   
   public TransactionDAO getTransactionDAO() {
        if(transactionDAO == null){
            transactionDAO = new TransactionDAO();
        }
        return transactionDAO;
    }

    public int isApprovalRouteDefined(String parentId, long employeeId) {
        return getTransactionDAO().isApprovalRouteDefined(parentId, employeeId);
    }

    public TransactionType findTransactionTypeByParentID(String parentId){
        return getTransactionDAO().getTransactionTypeByParent(parentId);
    }

    public long findNextApprovingOfficer(long transactionID) {
        Transaction transaction = getTransactionDAO().getTransactionById(transactionID);
        if(transaction.getStatus().trim().equals("P")){
            Long nextApprover = getTransactionDAO().getNextPendingApprover(transaction);
            if(nextApprover == null){
                return -1;
            }else{
                return nextApprover;
            }
        }else{
            return 0;
        }
    }
 
}
