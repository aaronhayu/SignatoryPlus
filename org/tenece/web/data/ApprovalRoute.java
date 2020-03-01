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

/**
 * This is the Data object to handle Department records.
 * It is also tied to ApprovalRouteDAO for all Database Operation
 * @author jeffry.amachree
 */
public class ApprovalRoute extends BaseData {
    private int id;
    private int approvalLevel;
    private int userId;
    private long transactionType;
    private int authorizer;

    private String authorizerName;

    /** Creates a new instance of Approval Route */
    public ApprovalRoute() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the approvalLevel
     */
    public int getApprovalLevel() {
        return approvalLevel;
    }

    /**
     * @param approvalLevel the approvalLevel to set
     */
    public void setApprovalLevel(int approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    /**
     * @return the employeeId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
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
     * @return the authorizer
     */
    public int getAuthorizer() {
        return authorizer;
    }

    /**
     * @param authorizer the authorizer to set
     */
    public void setAuthorizer(int authorizer) {
        this.authorizer = authorizer;
    }

    /**
     * @return the authorizerName
     */
    public String getAuthorizerName() {
        return authorizerName;
    }

    /**
     * @param authorizerName the authorizerName to set
     */
    public void setAuthorizerName(String authorizerName) {
        this.authorizerName = authorizerName;
    }

}
