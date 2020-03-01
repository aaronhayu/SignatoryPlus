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

package org.tenece.ap.data.dao;

import org.tenece.ap.dao.db.DatabaseQueryLoader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.tenece.web.data.ReportTemplate;
import org.tenece.web.data.Report;

/**
 *
 * @author jeffry.amachree
 */
public class ReportDAO extends BaseDAO{
    
    /** Creates a new instance of CompanyDAO */
    public ReportDAO() {
    }

    public ReportTemplate getReportTemplateByCode(String code){
        Connection connection = null;
        ReportTemplate record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(code);
            type.add("STRING");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().REPORT_TEMPLATE_SELECT_BY_CODE,
                    param, type);

            while(rst.next()){
                ReportTemplate rpt = new ReportTemplate();
                rpt.setCode(rst.getString("code"));
                rpt.setControl(rst.getString("control"));
                rpt.setCaptions(rst.getString("control_caption"));
                rpt.setControlValue(rst.getString("control_value"));
                rpt.setProcedures(rst.getString("init_procedures"));
                rpt.setCustom1(rst.getString("custom1"));

                record = rpt;
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
        return record;
    }

    public Report getReportById(String id){
        Connection connection = null;
        Report record = null;
        Vector param =new Vector();
        Vector type = new Vector();
        try{
            param.add(id);
            type.add("STRING");
            connection = this.getConnection(true);
            ResultSet rst = this.executeParameterizedQuery(connection, DatabaseQueryLoader.getQueryAssignedConstant().REPORT_SELECT_BY_ID,
                    param, type);

            while(rst.next()){
                Report rpt = new Report();
                rpt.setId(rst.getString("id"));
                rpt.setTitle(rst.getString("title"));
                rpt.setReportTemplateID(rst.getString("report_template"));
                rpt.setQuery(rst.getString("report_query"));
                rpt.setProcedures(rst.getString("report_procedure"));
                rpt.setFile(rst.getString("report_file"));
                rpt.setSubReportQueries(rst.getString("subreports_query"));
                record = rpt;
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
        return record;
    }

}
