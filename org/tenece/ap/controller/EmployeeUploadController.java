/*
 * (c) Copyright 2009 The Tenece Professional Services.
 *
 * Created on June 17, 2009, 3:40 PM
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

package org.tenece.ap.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.tenece.web.data.Employee;
import org.tenece.web.data.FileUpload;
import org.tenece.web.data.Users;
import org.tenece.web.services.EmployeeService;
import org.tenece.web.services.UploadService;


/**
 *
 * @author jeffry.amachree
 */
public class EmployeeUploadController extends BaseController {
    private UploadService uploadService = new UploadService();
    private EmployeeService employeeService = new EmployeeService();
    
    
    /** Creates a new instance of EmployeeAttendanceController */
    public EmployeeUploadController() {
    }
    
    
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView view = new ModelAndView("employee_namelist_upload");
        
        return view;
    }
    
    public ModelAndView processUploadRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        ModelAndView view = new ModelAndView("employee_namelist_upload_view");
        //get user information
        Users user = this.getUserPrincipal(request);

        if (!(request instanceof MultipartHttpServletRequest)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
            return null;
        }
        String fileType = request.getParameter("cbFileType");
        
        System.out.println("-------1------------");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("txtFile");
        
        org.tenece.web.data.FileUpload fileUpload = new FileUpload();
        fileUpload.setFileName(file.getName());
        try {
            fileUpload.setBytes(file.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        fileUpload.setSize(file.getSize());
        fileUpload.setContentType(file.getContentType());
        fileUpload.setOrginalFileName(file.getOriginalFilename());
        
        //save file in archive
        File destination = new File("./" + file.getOriginalFilename());
        fileUpload.setAbsolutePath(destination.getAbsolutePath());
        try {
            file.transferTo(destination);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        //process file
        try{
            List<Employee> attendanceList = getUploadService().saveUploadedNamelistFile(fileUpload, fileType);
            HttpSession session = request.getSession(false);
            String strId = user.getEmployeeId() + "_" + org.tenece.web.common.DateUtility.getDateAsString(new Date(), "ddMMyyyyHHmmss");
            session.setAttribute(strId, attendanceList);

            System.out.println("Number of uploaded records: " + attendanceList.size());
            view.addObject("result", attendanceList);
            view.addObject("idx", strId);
        }catch(Exception e){
            view = upload(request, response);
            view.addObject("message", e.getMessage());
        }
        
        return view;
    }
    //
    public ModelAndView confirmUploadRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView view = new ModelAndView("success");
        //get session index
        String strId = request.getParameter("txtHIdx");
        
        //use index to get value from session
        HttpSession session = request.getSession(false);

        List<Employee> attendanceList = (List<Employee>)session.getAttribute(strId);
        //check if value is null
        if(attendanceList == null){
            view = new ModelAndView("error");
            view.addObject("message","Unable to confirm request. Ensure that the uploaded file data is valid.");
            return view;
        }
        //remove value from session
        session.removeAttribute(strId);

        //save bulk
        boolean saved = uploadService.saveBulkEmployeeNameList(attendanceList);
        if(saved == false){
            view = new ModelAndView("error");
            view.addObject("message","Error while saving employee attendance. Please try again later");
            return view;
        }else{
            view.addObject("message","Uploaded attendance successfully confirmed.");
        }

        return view;
    }
    
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * @return the uploadService
     */
    public UploadService getUploadService() {
        return uploadService;
    }

    /**
     * @param uploadService the uploadService to set
     */
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }
    
      /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public void unZipIt(String zipFile, String outputFolder){
 
     byte[] buffer = new byte[1024];
 
     try{
 
    	//create output directory is not exists
    	File folder = new File(outputFolder);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
 
    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){
 
    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);
 
           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
 
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
 
            FileOutputStream fos = new FileOutputStream(newFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
 
            fos.close();   
            ze = zis.getNextEntry();
    	}
 
        zis.closeEntry();
    	zis.close();
 
    	System.out.println("Done");
 
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
   }     
      
}
