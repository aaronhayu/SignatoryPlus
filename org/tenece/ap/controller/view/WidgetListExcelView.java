/*
 * (c) Copyright 2009, 2010 The Tenece Professional Services.
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

package org.tenece.hr.controller.view;

import java.util.Date;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.tenece.web.common.DateUtility;
/**
 * Tenece Professional Services Limited
 * @author amachree
 */
public class WidgetListExcelView extends AbstractExcelView{

    public static final String WIDGET_EXCEL_KEY = "content";
    public static final String WIDGET_EXCEL_FILE_NAME = "fileName";

    protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = (HSSFWorkbook) model.get("content");
        String fileName = (String)model.get("fileName");
        if(fileName == null || fileName.trim().equals("")){
            fileName = DateUtility.getDateAsString(new Date(), "yyyyMMddHHmmss") + ".xls";
        }
        //workbook = wb;
        //THIS IS WHAT I NEEDED
        ServletOutputStream output = response.getOutputStream();
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        //THIS IS WHAT I NEEDED. Needed to write to the servlet outputstream.
        wb.write(output);
    }

    /* Sample Usage - 
    public ModelAndView downloadFile(HttpServletRequest request, HttpServletResponse response){
        try{
            Map model = new HashMap();

            List<ExcelContent> contents = new ArrayList<ExcelContent>();
            for(int i = 0; i < 3; i++){
                ExcelContent ct = new ExcelContent();
                ArrayList<Object> al = new ArrayList<Object>();
                ArrayList<Integer> types = new ArrayList<Integer>();
                al.add("My Own String " + i); types.add(ExcelContent.CELL_TYPE_STRING);
                al.add("19"); types.add(ExcelContent.CELL_TYPE_NUMBER);
                al.add(29.98d * (i + 1)); types.add(ExcelContent.CELL_TYPE_DECIMAL);
                al.add(new Date()); types.add(ExcelContent.CELL_TYPE_DATE);
                ct.setRowValues(al);
                ct.setRowValueTypes(types);
                contents.add(ct);
            }
            ExcelWriter r = new ExcelWriter();
            String fileName = DateUtility.getDateAsString(new Date(), "yyyyMMddHHmmss") + ".xls";
            model.put(WidgetListExcelView.WIDGET_EXCEL_FILE_NAME, fileName);
            model.put(WidgetListExcelView.WIDGET_EXCEL_KEY, r.generateExcelWorkBook(new FileOutputStream(fileName), contents));
            return new ModelAndView(new WidgetListExcelView(), model);
        }catch(Exception er){
            er.printStackTrace();
            return null;
        }
    }
    */
}
