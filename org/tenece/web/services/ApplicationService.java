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

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import org.tenece.ap.data.dao.ApplicationDAO;
import org.tenece.ap.data.dao.AuditTrailDAO;
import org.tenece.web.data.AuditTrail;
import org.tenece.web.data.Company;

/**
 * This class is used to instantiate EntityProcessor
 * @author jeffry.amachree
 */
public class ApplicationService extends BaseService{
    private AuditTrailDAO auditTrailDAO;
    private ApplicationDAO applicationDAO;
    /** Creates a new instance of ApplicationController */
    public ApplicationService() {
    }

    public List<AuditTrail> findAuditTrail(){
        return getAuditTrailDAO().getAllAuditTrails();
    }

    public List<AuditTrail> findAuditTrailByEmployee(long employee){
        return getAuditTrailDAO().getAuditTrailByEmployee(employee);
    }

    public List<AuditTrail> findAuditTrail(Date startDate, Date endDate){
        return getAuditTrailDAO().getAuditTrailByDate(startDate, endDate);
    }

    public boolean createNewAuditTrail(AuditTrail audit){
        try{
            int row = getAuditTrailDAO().createNewAuditTrail(audit);
            if(row == 0){
                return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }

    }
     public boolean createNewAuditTrail(AuditTrail audit,Connection connection){
        try{
            int row = getAuditTrailDAO().createNewAuditTrail(audit,connection);
            if(row == 0){
                return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }

    }


    /**
     * @return the auditTrailDAO
     */
    public AuditTrailDAO getAuditTrailDAO() {
        return auditTrailDAO;
    }

    /**
     * @param auditTrailDAO the auditTrailDAO to set
     */
    public void setAuditTrailDAO(AuditTrailDAO auditTrailDAO) {
        this.auditTrailDAO = auditTrailDAO;
    }

    /**
     * @return the applicationDAO
     */
    public ApplicationDAO getApplicationDAO() {
        return applicationDAO;
    }

    /**
     * @param applicationDAO the applicationDAO to set
     */
    public void setApplicationDAO(ApplicationDAO applicationDAO) {
        this.applicationDAO = applicationDAO;
    }
    /**
     * @return the auditTrailDAO
     */
//    public AuditTrailDAO getAuditTrailDAO() {
//        if(auditTrailDAO == null){
//            auditTrailDAO = new AuditTrailDAO();
//        }
//        return auditTrailDAO;
//    }
//
//
//    /**
//     * @return the companyDAO
//     */
//    public CompanyDAO getCompanyDAO() {
//        if(companyDAO == null){
//            companyDAO = new CompanyDAO();
//        }
//        return companyDAO;
//    }

    
}
