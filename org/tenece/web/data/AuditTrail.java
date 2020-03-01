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
 * Tenece Professional Services, Nigeria
 * @author strategiex
 */
public class AuditTrail extends BaseData {
    private long employeeId;
    private Date accessDate;
    private String operation;
    private String machineIdentity;
    private String operationCode;
    private long totalUsers = 0L;
    private long totalRecord = 0L;
    /** Creates a new instance of Audit Trail */
    public AuditTrail() {
    }

    /**
     * @return the employeeId
     */
    public long getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the accessDate
     */
    public Date getAccessDate() {
        return accessDate;
    }

    /**
     * @param accessDate the accessDate to set
     */
    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
     /**
     * @return the operation
     */
    public String getOperationCode() {
        return operationCode;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    /**
     * @return the machineIdentity
     */
    public String getMachineIdentity() {
        return machineIdentity;
    }

    /**
     * @param machineIdentity the machineIdentity to set
     */
    public void setMachineIdentity(String machineIdentity) {
        this.machineIdentity = machineIdentity;
    }

    /**
     * @return the totalUsers
     */
    public long getTotalUsers() {
        return totalUsers;
    }

    /**
     * @param totalUsers the totalUsers to set
     */
    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    /**
     * @return the totalRecord
     */
    public long getTotalRecord() {
        return totalRecord;
    }

    /**
     * @param totalRecord the totalRecord to set
     */
    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }
    
}
