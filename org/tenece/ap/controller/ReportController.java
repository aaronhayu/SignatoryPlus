
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 10 June 2009, 12:36
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

package org.tenece.ap.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tenece.ap.constant.AuditOperationCodeConstant;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.data.Employee;
import org.tenece.web.data.Report;
import org.tenece.web.data.ReportTemplate;
import org.tenece.web.data.Users;
import org.tenece.web.services.ReportService;

/**
 * 
 * @author jeffry.amachree
 */
public class ReportController extends BaseController {
    
    private ReportService reportService = new ReportService();

    /** Creates a new instance of ReportController */
    public ReportController() {
    }

    public ModelAndView viewReportTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView view = new ModelAndView("report_template");
        try{
            //get parameters...
            String rptId = request.getParameter("rpt");
            
            //get report and template
            Report report = this.reportService.findReportByID(rptId);
            ReportTemplate rptTemplate = this.reportService.findReportTemplateByCode(report.getReportTemplateID());

            //check if template contains value for both "To" and "From"
            if(rptTemplate.getControl().indexOf("T") != -1
                    || rptTemplate.getControl().indexOf("D") != -1
                    || rptTemplate.getControl().indexOf("S") != -1
                    || rptTemplate.getControl().indexOf("N") != -1){
                //set value to 1 so as to show both
                view.addObject("showBoth", 1);
            }

            view.addObject("report", report);
            view.addObject("template", rptTemplate);
            
            //split template controls and get parameters based on those with default values
            List<Object> templateParams = reportService.getTemplateData(request,rptTemplate.getCaptions(), rptTemplate.getControl(), rptTemplate.getControlValue());

            view.addObject("params", templateParams);
            //System.out.println(templateParams);
            createNewAuditTrail(request, "Opening Report:[" + report.getTitle() + "]",AuditOperationCodeConstant.Opening_Report);
        }catch(Exception e){
            e.printStackTrace();
            view.addObject("error", "Unable to get report template");
        }
        return view;
    }

    public ModelAndView viewReport(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JasperReport jasperReport = null; 
        JasperPrint jasperPrint = null; 
        HttpSession session = request.getSession();
        try{

            //get user principal detail
            Users user = this.getUserPrincipal(request);

            Employee employee = this.getEmployeeService().findEmployeeBasicById(user.getEmployeeId());

            String viewType = request.getParameter("rbOption");
            String reportId = request.getParameter("rptId");
            int totalCount = Integer.parseInt(request.getParameter("txtParam"));
            //get report from service
            Report report = reportService.findReportByID(reportId);

            //other valid object to use after accessing database
            String reportFile = report.getFile();
            System.out.println(viewType + "<<<<<<<<<<" + reportFile);

            System.out.println("Logo Path: " + this.getApplicationURL(request) + "/images/FirstBank_logo1.jpg");
            //set paramaters
            Map parameters = new HashMap();
            parameters.put("HR_PAGE_TITLE", report.getTitle());
            if(report.getReportTemplateID().equals("SIGBOOK") 
                    || report.getReportTemplateID().equals("NAMELIST")
                    || report.getReportTemplateID().equals("DATE_BYUID")
                     || report.getReportTemplateID().equals("DNAMELIST")
                    || report.getReportTemplateID().equals("SIGDEP"))
            {
              parameters.put("COMPANY_LOGO", this.getApplicationURL(request) + "/images/firstbamk_logo.jpg");//"http://localhost:8080/xtremehr/upload/logo.gif");

            }
            else
            {
            parameters.put("COMPANY_LOGO", this.getApplicationURL(request) + "/images/FirstBank_logo1.jpg");//"http://localhost:8080/xtremehr/upload/logo.gif");

            }
                        parameters.put("CANCELLED_LOGO", this.getApplicationURL(request) + "/images/cancelled.jpg");
            parameters.put("BACKGROUND_IMAGE", this.getApplicationURL(request) + "/images/background.gif");

            //access database for report code information

            //pick the query and the jasper file name
            String query = report.getQuery();
            for(int i = 1; i <= totalCount; i++){
                String tmpParamTo = request.getParameter("PARAM_TO_" + String.valueOf(i));
                String tmpParamFrom = request.getParameter("PARAM_FROM_" + String.valueOf(i));
                if(tmpParamTo != null){
                    
                    if(tmpParamFrom != null && tmpParamFrom.equals("")){
                        tmpParamFrom = " ";
                    }
                    if(tmpParamTo.trim().equals("")){
                        tmpParamTo = "zz";
                    }
                }
                if(tmpParamTo != null){
                    query = query.replaceAll("PARAM_TO_" + String.valueOf(i), tmpParamTo);
                }
                if(tmpParamFrom != null){
                    
                    //check if from param is empty or not
                    if((tmpParamFrom.trim().equals("0") || tmpParamFrom.trim().equals("")) && query.contains("DECODE_FROM_" + String.valueOf(i) + "_NUMBER")){
                        query = query.replaceAll("DECODE_FROM_" + String.valueOf(i) + "_NUMBER", "999999999");
                        query = query.replaceAll("PARAM_FROM_" + String.valueOf(i), "-1");
                    }
                    //DO NOT REPLACE FOR THE FOLLOWING CONDITIONS
                    if((!tmpParamFrom.trim().equals("0")) && (!tmpParamFrom.trim().equals("")) && query.contains("DECODE_FROM_" + String.valueOf(i) + "_NUMBER")){
                        query = query.replaceAll("DECODE_FROM_" + String.valueOf(i) + "_NUMBER", tmpParamFrom);
                    }
                    if( (tmpParamFrom.trim().equals("0") ||tmpParamFrom.trim().equals("")) && query.contains("DECODE_FROM_" + String.valueOf(i) + "_STRING")){
                        query = query.replaceAll("DECODE_FROM_" + String.valueOf(i) + "_STRING", "zz");
                    }
                    if((!tmpParamFrom.trim().equals("")) && query.contains("DECODE_FROM_" + String.valueOf(i) + "_STRING")){
                        query = query.replaceAll("DECODE_FROM_" + String.valueOf(i) + "_STRING", tmpParamFrom);
                    }//:~
                    //REPLACE THE INITIAL PARAM 1
                    query = query.replaceAll("PARAM_FROM_" + String.valueOf(i), tmpParamFrom);
                }
            }
            //remove special keywords designed for report
            //(EMPLOYEEID, COMP_ID, USER_ROLE)
            query = query.replaceAll("PARAM_EMPLOYEEID", String.valueOf(user.getEmployeeId()));

            query = query.replaceAll("PARAM_COMPANY_CODE", "");
            query = query.replaceAll("PARAM_USER_ROLE", String.valueOf(this.getUserLoginRole(request)));

            System.out.println("Report Query:" + query);

            //get subreport queries
            String subReportQuery = report.getSubReportQueries();
            if(subReportQuery == null){
                subReportQuery = "";
            }
            String[] subQueries = subReportQuery.split(";");
            for(int i = 0; i < subQueries.length; i++){
                parameters.put("QUERY" + String.valueOf(i), subQueries[i]);
            }

            //create a jasper print... to hold the final output
            jasperPrint = reportService.fillReport(request.getSession().getServletContext().getRealPath("/WEB-INF/reports/"), request.getSession().getServletContext().getRealPath("/WEB-INF/reports/" + reportFile + "." + ConfigReader.REPORT_EXTENSION)
                    , parameters, query);

            ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
            if(viewType.trim().equals("HTML")){
                //text/html
                String fileName = ConfigReader.tag_report("report_") + ".html";
                String destRealPath = request.getSession().getServletContext().getRealPath(fileName);
                JasperExportManager.exportReportToHtmlFile(jasperPrint, destRealPath);

                ModelAndView view = new ModelAndView(new RedirectView("./" + fileName));

                return view;
            }else if(viewType.trim().equals("PDF")){
                //write output to browser
                String fileName = ConfigReader.tag_report("report_") + ".pdf";
                System.out.println("PDF>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                JasperExportManager.exportReportToPdfStream(jasperPrint, bOutput);
                writeExportToResponseStream("application/pdf", response, bOutput,fileName);

            }else if(viewType.trim().equals("XSL")){
                //application/vnd.ms-excel
                generateXLSOutput("excel", jasperPrint, response);

            }else if(viewType.trim().equals("XML")){
                //write output to browser
                String fileName = ConfigReader.tag_report("report_") + ".xml";
                JasperExportManager.exportReportToXmlStream(jasperPrint, bOutput);
                writeExportToResponseStream("text/xml", response, bOutput,fileName);

            }
            createNewAuditTrail(request, "Opened Report:[" + report.getTitle() + "]",AuditOperationCodeConstant.Opening_Report);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    
    private void generateXLSOutput(String reportname,
        JasperPrint jasperPrint, HttpServletResponse resp)throws IOException, JRException {
        String reportfilename = ConfigReader.tag_report(reportname) + ".xls";
        JExcelApiExporter exporterXLS = new JExcelApiExporter();

        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, resp.getOutputStream());
        resp.setHeader("Content-Disposition", "inline;filename=" + reportfilename);
        resp.setContentType("application/vnd.ms-excel");

        exporterXLS.exportReport();
    }

    /**
     * Export Report to User Interface Device (browser)
     * @param contentType
     * @param response
     * @param baos - The data to be shown
     */
    private void writeExportToResponseStream(String contentType, HttpServletResponse response, ByteArrayOutputStream baos,String fileName) {

        response.setContentType(contentType);
        response.setContentLength(baos.size());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                fileName);
        response.setHeader(headerKey, headerValue);
        try {
            ServletOutputStream out1 = response.getOutputStream();
            baos.writeTo(out1);
            out1.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @return the reportService
     */
    public ReportService getReportService() {
        return reportService;
    }

    /**
     * @param reportService the reportService to set
     */
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }
}
