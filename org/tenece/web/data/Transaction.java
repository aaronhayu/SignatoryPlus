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

package org.tenece.web.data;

import java.util.Date;

/**
 *
 * @author jeffry.amachree
 */
public class Transaction {
    private long id;
    private String description;
    private String status;
    private long batchNumber;
    private long transactionReference;
    private long transactionType;
    private long initiator;
    //transient objects
    private Users userObj;
    private TransactionType tranTypeObject;
    private Date initiatedDate;
    
    /** Creates a new instance of JobTitle */
    public Transaction() {
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the batchNumber
     */
    public long getBatchNumber() {
        return batchNumber;
    }

    /**
     * @param batchNumber the batchNumber to set
     */
    public void setBatchNumber(long batchNumber) {
        this.batchNumber = batchNumber;
    }

    /**
     * @return the transactionReference
     */
    public long getTransactionReference() {
        return transactionReference;
    }

    /**
     * @param transactionReference the transactionReference to set
     */
    public void setTransactionReference(long transactionReference) {
        this.transactionReference = transactionReference;
    }

    /**
     * @return the transactionType
     */
    public long getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(long transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return the initiator
     */
    public long getInitiator() {
        return initiator;
    }

    /**
     * @param initiator the initiator to set
     */
    public void setInitiator(long initiator) {
        this.initiator = initiator;
    }

    /**
     * @return the userObj
     */
    public Users getUserObj() {
        return userObj;
    }

    /**
     * @param userObj the userObj to set
     */
    public void setUserObj(Users userObj) {
        this.userObj = userObj;
    }

    /**
     * @return the tranTypeObject
     */
    public TransactionType getTranTypeObject() {
        return tranTypeObject;
    }

    /**
     * @param tranTypeObject the tranTypeObject to set
     */
    public void setTranTypeObject(TransactionType tranTypeObject) {
        this.tranTypeObject = tranTypeObject;
    }

    /**
     * @return the initiatedDate
     */
    public Date getInitiatedDate() {
        return initiatedDate;
    }

    /**
     * @param initiatedDate the initiatedDate to set
     */
    public void setInitiatedDate(Date initiatedDate) {
        this.initiatedDate = initiatedDate;
    }

}
