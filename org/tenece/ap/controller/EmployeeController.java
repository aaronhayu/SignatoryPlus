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
package org.tenece.ap.controller;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.tenece.ap.form.MultiFileUploadForm;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.common.DateUtility;
import org.tenece.web.common.ImageProcessor;
import org.tenece.web.data.Category;
import org.tenece.web.data.Employee;
import org.tenece.web.data.FileUpload;
import org.tenece.web.exception.PDFException;
import org.tenece.web.services.EmployeeService;
import org.tenece.web.services.SetupService;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.tenece.ap.constant.AuditOperationCodeConstant;
import org.tenece.ap.security.ActiveDirectory;
import org.tenece.web.common.FileWriterProcesor;
import org.tenece.web.data.ProgressDetails;
import org.tenece.web.filter.ApplicationFilter;
import sun.misc.BASE64Encoder;

/**
 *
 * @author jeffry.amachree
 */
@Controller
public class EmployeeController extends BaseController {

    // PRIVATE 
    /**
     * Size of a byte buffer to read/write file
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Path of the file to be downloaded, relative to application's directory
     */
    private EmployeeService employeeService = new EmployeeService();
    private SetupService setupService = new SetupService();

    /** Creates a new instance of EmployeeController */
    public EmployeeController() {
    }

    public ModelAndView viewAllEmployee(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_view");

        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");

        List<Employee> list = null;
        if (cbSearch == null || txtSearch == null) {
            list = getEmployeeService().findAllEmployeeForBasic();
            //view.addObject("showCompany", "Y");
        } else {
            list = getEmployeeService().findAllEmployeeForBasic(cbSearch, txtSearch);
            //view.addObject("showCompany", "Y");
        }

        createNewAuditTrail(request, "View All Employee", AuditOperationCodeConstant.View_All_Employee);
        view.addObject("result", list);
        return view;
    }

    public ModelAndView viewDeprecateEmployees(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_deprecate_view");

        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");

        List<Employee> list = null;
        if (cbSearch == null || txtSearch == null) {
            list = getEmployeeService().findAllEmployeeForBasic();
            //view.addObject("showCompany", "Y");
        } else {
            list = getEmployeeService().findAllEmployeeForBasic(cbSearch, txtSearch);
            //view.addObject("showCompany", "Y");
        }

        createNewAuditTrail(request, "View Deprecated Employees", AuditOperationCodeConstant.View_Deprecated_Employees);
        view.addObject("result", list);
        return view;
    }

    public ModelAndView searchEmployee(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_search_view");

        String staffId = request.getParameter("txtStaffId");
        String fullName = request.getParameter("txtName");
        //String department = request.getParameter("cbDept");
        String division = request.getParameter("cbDivision");
        String type = request.getParameter("type");
        if (type == null) {
            type = "";
        }

        view.addObject("dept", setupService.findAllDepartments());
        //view.addObject("jobTitle", setupService.findAllJobTitles());
        view.addObject("divisionList", setupService.findAllDivisions());


        List<Employee> list = null;
        //basic search
        if (type.trim().equals("B")) {

            Employee employee = getEmployeeService().findEmployeeBasicByStaffId(staffId);
            list = new ArrayList<Employee>();
            if (employee != null) {
                list.add(employee);
            }
        } else if (type.trim().equals("A")) {

            list = getEmployeeService().findAllEmployeeBySearch(staffId, fullName, null, division);
        } else {
            list = new ArrayList<Employee>();
        }



        view.addObject("result", list);
        view.addObject("staffId", staffId);
        view.addObject("fullName", fullName);
       // view.addObject("deptId", department);
        view.addObject("divisionId", division);

        return view;
    }

    public ModelAndView viewEmployee(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_edit");

        String strId = request.getParameter("idx");
        if (strId == null || strId.trim().equals("")) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to preview employee data");
            return view;
        }
        int id = Integer.parseInt(strId);

        Employee employee = getEmployeeService().findEmployeeBasicById(id);
        try {
       if(employee.getDivisionId() >0 )
       {
         employee.setSolId(String.valueOf(employee.getDivisionId()));
       }} catch (Exception e) {
            e.printStackTrace();
          //  view.addObject("signature_picture", "");
        }
        //get company picture when loading data
        try {

            String filePath = request.getSession().getServletContext().getRealPath("./upload/" + String.valueOf(id) + "_" + employee.getSignatureName());
            File file = new File(filePath);
            if (employee.getSignature() != null) {
                DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file));
                dOut.write(employee.getSignature());
                dOut.flush();
                dOut.close();
            }
            view.addObject("signature_picture", String.valueOf(id) + "_" + employee.getSignatureName());

        } catch (Exception e) {
            e.printStackTrace();
            view.addObject("signature_picture", "");
        }

        view.addObject("dept", setupService.findAllDepartments());
        //view.addObject("jobTitle", setupService.findAllJobTitles());
        view.addObject("divisionList", setupService.findAllDivisions());
        view.addObject("jobTitleList", setupService.findAllJobTitles());
        view.addObject("grade", setupService.findAllGrades());
        view.addObject("employee", employee);
        ArrayList<Category> list = new ArrayList<Category>();
        Category cat1 = new Category();
        cat1.setKey("A");
        cat1.setDescription("Category A");
        Category cat2 = new Category();
        cat2.setKey("B");
        cat2.setDescription("Category B");
        list.add(cat1);
        list.add(cat2);
        view.addObject("categoryList", list);
        view.addObject("pageTitle", "edit");
        createNewAuditTrail(request, "View Existing Staff: Record[" + employee.toString() + "]", AuditOperationCodeConstant.View_Existing_Employee_Details);

        return view;
    }

    public ModelAndView deprecateEmployee(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_deprecate_edit");

        String strId = request.getParameter("idx");
        if (strId == null || strId.trim().equals("")) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to preview employee data");
            return view;
        }
        int id = Integer.parseInt(strId);

        Employee employee = getEmployeeService().findEmployeeBasicById(id);

        //get company picture when loading data
        try {
            String staffId = employee.getStaffId();
            String filePath = request.getSession().getServletContext().getRealPath("./upload/" + String.valueOf(id) + "_" + employee.getSignatureName());
            File file = new File(filePath);
            DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file));
            dOut.write(employee.getSignature());
            dOut.flush();
            dOut.close();

            view.addObject("signature_picture", String.valueOf(id) + "_" + employee.getSignatureName());

        } catch (Exception e) {
            e.printStackTrace();
            view.addObject("signature_picture", "");
        }

        view.addObject("dept", setupService.findAllDepartments());
        //view.addObject("jobTitle", setupService.findAllJobTitles());
        view.addObject("divisionList", setupService.findAllDivisions());

        view.addObject("employee", employee);
        createNewAuditTrail(request, "View deprecated employee information:[" + employee.toString() + "]", AuditOperationCodeConstant.View_deprecated_employee_information);
        return view;
    }

    //searchEmployeeView
    public ModelAndView searchEmployeeView(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_search_edit");

        String strId = request.getParameter("idx");


        if (strId == null || strId.trim().equals("")) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to preview employee data");
            return view;
        }
        int id = Integer.parseInt(strId);

        Employee employee = getEmployeeService().findEmployeeBasicById(id);

        //get company picture when loading data
        try {
            String staffId = employee.getStaffId();
            String filePath = request.getSession().getServletContext().getRealPath("./upload/" + String.valueOf(id) + "_" + employee.getSignatureName());
            File file = new File(filePath);
            DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file));
            dOut.write(employee.getSignature());
            dOut.flush();
            dOut.close();

            view.addObject("signature_picture", String.valueOf(id) + "_" + employee.getSignatureName());

        } catch (Exception e) {
            e.printStackTrace();
            view.addObject("signature_picture", "");
        }

        view.addObject("dept", setupService.findAllDepartments());
        //view.addObject("jobTitle", setupService.findAllJobTitles());
        view.addObject("divisionList", setupService.findAllDivisions());

        view.addObject("employee", employee);
        createNewAuditTrail(request, "Preview Employee Information through Search:[" + employee.toString() + "]", AuditOperationCodeConstant.View_Existing_Employee_Details);
        return view;
    }

    public ModelAndView newRecord(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_edit");

        //get all needed objects
        view.addObject("dept", setupService.findAllDepartments());
        //view.addObject("jobTitle", setupService.findAllJobTitles());
        view.addObject("divisionList", setupService.findAllDivisions());
        view.addObject("jobTitleList", setupService.findAllJobTitles());
        view.addObject("grade", setupService.findAllGrades());
        ArrayList<Category> list = new ArrayList<Category>();
        Category cat1 = new Category();
        cat1.setKey("A");
        cat1.setDescription("Category A");
        Category cat2 = new Category();
        cat2.setKey("B");
        cat2.setDescription("Category B");
        list.add(cat1);
        list.add(cat2);
        view.addObject("categoryList", list);
        //view.addObject("result", employeeService.findEmployeeBasicById(0));
        Employee employee = new Employee();
        //employee.setEmail("info@fbnnigeria.com");
        view.addObject("employee", employee);
        view.addObject("pageTitle", "new");
        createNewAuditTrail(request, "Accessing Create new Staff Signature Interface", AuditOperationCodeConstant.View_Create_New_Employee);
        return view;
    }

    public ModelAndView uploadPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("employee_picture_upload");

        String strIdx = request.getParameter("idx");
        int idx = Integer.parseInt(strIdx);

        view.addObject("employee_id", idx);

        return view;
    }

    public ModelAndView uploadSignature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("employee_signature_upload");

        return view;
    }

    public ModelAndView deleteEmployee(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("");
        List<Long> ids = new java.util.ArrayList<Long>();
        //get mode of request
        String mode = request.getParameter("mode");
        //check if mode is for more than one record
        //get mode, if mode is equals 1, then process for multiple
        if (mode != null && mode.trim().equals("1")) {
            String[] args = request.getParameterValues("_chk");
            for (String id : args) {
                ids.add(Long.parseLong(id));
            }
        } else {//then it is zero
            ids.add(Long.parseLong(request.getParameter("id")));
        }

        getEmployeeService().deActivateEmployee(ids);

        createNewAuditTrail(request, "Deleted Staffs: id(s)=" + ids.toString(), AuditOperationCodeConstant.Delete_Employee);
        //delete
        return viewAllEmployee(request, response);
    }

    public ModelAndView processSignaturePreviewRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_signature_upload");

        //check if file is attached and its the right type of request
        if (!(request instanceof MultipartHttpServletRequest)) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
            } catch (IOException ex) {
                System.out.println(ex);
            }
            return new ModelAndView("error");
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        String webURL = multipartRequest.getParameter("txtWebURL");

        String xIndex = multipartRequest.getParameter("x1");
        String yIndex = multipartRequest.getParameter("y1");
        String width = multipartRequest.getParameter("w");
        String height = multipartRequest.getParameter("h");

        try {

            String filePath = request.getSession().getServletContext().getRealPath("./upload/" + webURL);
            System.out.println("filePath=======" + filePath);
            File file = new File(filePath);
            byte[] byteContent = new byte[(int) file.length()];
            DataInputStream dIn = new DataInputStream(new FileInputStream(file));
            dIn.read(byteContent);
            dIn.close();

            int xPos = Integer.parseInt(xIndex);
            int yPos = Integer.parseInt(yIndex);
            int iWidth = Integer.parseInt(width);
            int iHeight = Integer.parseInt(height);
            byte[] croppedContent = ImageProcessor.cropImages(file, xPos, yPos, iWidth, iHeight);

            String filePathForSave = request.getSession().getServletContext().getRealPath("./upload/cropped_" + DateUtility.getDateAsString(new Date(), "ddMM_yyyy_SSss") + ".png");
            File fileCropped = new File(filePathForSave);
            DataOutputStream dOut = new DataOutputStream(new FileOutputStream(fileCropped));
            dOut.write(croppedContent);
            dOut.flush();
            dOut.close();

            view.addObject("preview_picture", fileCropped.getName());
            System.out.println("=======>" + fileCropped.getName());

            view.addObject("showPreview", "1");

        } catch (Exception e) {
            e.printStackTrace();
            view.addObject("preview_picture", "");
            view.addObject("showPreview", "0");
        }

        return view;
    }

    public ModelAndView processSignatureUploadRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_signature_upload");

        //check if file is attached and its the right type of request
        if (!(request instanceof MultipartHttpServletRequest)) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
            } catch (IOException ex) {
                System.out.println(ex);
            }
            return new ModelAndView("error");
        }

        boolean changeLogo = false;
        FileUpload fileUpload = new FileUpload();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("txtSig");

            //confirm if file size is valid.
            if (file.getSize() != 0) {
                //check validity of file by confirming extention
                String logoFileName = "";
                if ((file.getOriginalFilename().endsWith("gif")) || (file.getOriginalFilename().endsWith("jpg"))
                        || (file.getOriginalFilename().endsWith("png")) || (file.getOriginalFilename().endsWith("pdf"))) {
                    //check content type (if valid)
                    if (!ConfigReader.getAllowedImagesContentType().contains(file.getContentType())) {
                        view = new ModelAndView("error");
                        view.addObject("message", "Invalid File Type. Only the following file types are allowed: gif, jpg and png");
                        return view;
                    }
                    //build a dynamic file name that will be valid across system
                    logoFileName = "signature_" + DateUtility.getDateAsString(new Date(), "ddMMyyyy_HHmmss") + "." + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);
                } else {
                    view = new ModelAndView("error");
                    view.addObject("message", "Invalid File Type. Only the following file types are allowed: gif, jpg, png and pdf.");
                    return view;
                }

                File destination = null;
                if (file.getOriginalFilename().endsWith("pdf")) {
                    String pdfFileName = "pdf_signature_" + DateUtility.getDateAsString(new Date(), "ddMMyyyy_HHmmss");
                    String filePath = request.getSession().getServletContext().getRealPath("./upload/" + pdfFileName);

                    File pdfFile = new File(filePath);

                    DataOutputStream pdfOutStream = new DataOutputStream(new FileOutputStream(pdfFile));
                    pdfOutStream.write(file.getBytes());
                    pdfOutStream.flush();
                    pdfOutStream.close();

                    try {
                        byte[] convertedToImageBytes = ImageProcessor.convertPDFToImage(pdfFile, 1);

                        //set file name. this name will be used to access the image
                        fileUpload.setFileName(logoFileName);
                        fileUpload.setBytes(convertedToImageBytes);

                        fileUpload.setSize((int) pdfFile.length());
                        fileUpload.setContentType("application/pdf");
                        fileUpload.setOrginalFileName(pdfFile.getName());

                        //indicate the destination file Name
                        destination = new File("./" + pdfFile.getName());
                    } catch (Exception e) {
                        System.out.println("**********Error: " + e);
                        view.addObject("message", "Invalid PDF Document.");
                        throw new PDFException("System Error, Invalid PDF Document.");
                    }
                } else {

                    //set file name. this name will be used to access the image
                    fileUpload.setFileName(logoFileName);
                    try {
                        fileUpload.setBytes(file.getBytes());
                    } catch (IOException ex) {
                        throw ex;
                    }
                    fileUpload.setSize(file.getSize());
                    fileUpload.setContentType(file.getContentType());
                    fileUpload.setOrginalFileName(file.getOriginalFilename());
                    //indicate the destination file Name
                    destination = new File("./" + file.getOriginalFilename());
                }
                //save file in archive

                fileUpload.setAbsolutePath(destination.getAbsolutePath());
                try {
                    file.transferTo(destination);
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    throw ex;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
                changeLogo = true;
            }
        } catch (PDFException ex) {
            ex.printStackTrace();
            view.addObject("message", ex.getMessage());
            return view;
        } catch (Exception ex) {
            ex.printStackTrace();
            view.addObject("message", "System error while processing uploaded file. Try again later or with a valid file.");
            return view;
        }
        //preview time
        if (changeLogo == true) {
            //get company picture when loading data
            try {
                String filePath = request.getSession().getServletContext().getRealPath("./upload/" + fileUpload.getFileName());
                File file = new File(filePath);
                DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file));
                dOut.write(fileUpload.getBytes());
                dOut.flush();
                dOut.close();

                view.addObject("signature_picture", fileUpload.getFileName());
                System.out.println("=======>" + fileUpload.getFileName());

                view.addObject("showImage", "1");

            } catch (Exception e) {
                e.printStackTrace();
                view.addObject("signature_picture", "");
                view.addObject("showImage", "0");
                view.addObject("message", "Error previewing uploaded image. Please try again later.");
            }

        }
        return view;
    }

    public ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("message");
        String operation = request.getParameter("txtMode");
        String signatureUploaded = request.getParameter("signatureUploaded");
        String insertSignature = request.getParameter("chkSignature");

        signatureUploaded = (signatureUploaded == null) ? "" : signatureUploaded;

        //check if file is attached and its the right type of request
        if (!(request instanceof MultipartHttpServletRequest)) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
            } catch (IOException ex) {
                System.out.println(ex);
            }
            return new ModelAndView("error");
        }

        boolean changeLogo = false;
        FileUpload fileUpload = new FileUpload();

        if (signatureUploaded.trim().equals("2") && insertSignature.trim().equals("1")) {//no need for signature picture

            changeLogo = false;

        } else if (signatureUploaded.trim().equals("1")) {
            try {
                String webURL = request.getParameter("weburl");

                String filePath = request.getSession().getServletContext().getRealPath("./upload/" + webURL);

                File file = new File(filePath);
                byte[] byteContent = new byte[(int) file.length()];
                DataInputStream dIn = new DataInputStream(new FileInputStream(file));
                dIn.read(byteContent);
                dIn.close();
                fileUpload.setAbsolutePath(file.getAbsolutePath());
                fileUpload.setBytes(byteContent);
                fileUpload.setFileName(file.getName());
                fileUpload.setOrginalFileName(file.getName());
                fileUpload.setSize((int) file.length());

                changeLogo = true;

            } catch (Exception e) {
                createNewAuditTrail(request, "Error Saving Employee Signature File", AuditOperationCodeConstant.Error_Saving_Employee_Signature_File);
                view = new ModelAndView("error");
                view.addObject("message", "Invalid Signature File. Please upload and preview the signature to continue.");
                return view;
            }
        } else {

            try {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile file = multipartRequest.getFile("txtSig");

                //confirm if file size is valid.
                if (file.getSize() != 0) {
                    //check validity of file by confirming extention
                    String logoFileName = "";
                    if ((file.getOriginalFilename().endsWith("gif")) || (file.getOriginalFilename().endsWith("jpg"))
                            || (file.getOriginalFilename().endsWith("png"))) {
                        //check content type (if valid)
                        if (!ConfigReader.getAllowedImagesContentType().contains(file.getContentType())) {
                            view = new ModelAndView("error");
                            view.addObject("message", "Invalid File Type. Only the following file types are allowed: gif, jpg and png");
                            return view;
                        }
                        //build a dynamic file name that will be valid across system
                        logoFileName = "signature_" + DateUtility.getDateAsString(new Date(), "ddMMyyyy_HHmmss") + "." + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);
                    } else {
                        view = new ModelAndView("error");
                        createNewAuditTrail(request, "Invalid Signature File Type", AuditOperationCodeConstant.Invalid_Signature_File_Type);
                        view.addObject("message", "Invalid File Type. Only the following file types are allowed: gif, jpg and png");
                        return view;
                    }

                    //set file name. this name will be used to access the image
                    fileUpload.setFileName(logoFileName);
                    try {
                        fileUpload.setBytes(file.getBytes());
                    } catch (IOException ex) {
                        throw ex;
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
                        throw ex;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw ex;
                    }
                    changeLogo = true;

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //operation mode: N= New, E=Edit
        if (operation.trim().equals("N")) {
            Employee emp = new Employee();
            String firstName = request.getParameter("txtFirstName");
            String lastName = request.getParameter("txtLastName");
            String otherNames = request.getParameter("txtOtherName");
            String salutation = "";
            String dept = request.getParameter("cbDept");
            String phone = request.getParameter("txtPhone");
            String ext = request.getParameter("txtExt");
            String email = request.getParameter("txtEmail");
            String division = request.getParameter("txtSolId");
            String staffId = request.getParameter("txtStaffId");
            String signNo = request.getParameter("txtSignNo");
            String category = request.getParameter("cbCategory");
            String cbJobTitle = request.getParameter("cbJobTitle");
            String cbGrade = request.getParameter("cbGrade");
            String branchDesc = request.getParameter("txtBranch");
            
            emp.setBranchDesc(branchDesc);
            emp.setStaffId(staffId);
            emp.setSalutation(salutation);
            emp.setFirstName(firstName);
            emp.setLastName(lastName);
            emp.setOtherNames(otherNames);
            
            emp.setDepartmentId(Integer.parseInt("0"));
           
            emp.setCellPhone(phone);
            if (!ext.equals("")) {
                emp.setExtension(Integer.parseInt(ext));
            } else {
                ext = "0";
                emp.setExtension(Integer.parseInt(ext));
            }
            emp.setEmploymentStatus("ACTIVE");
            emp.setEmail(email);
            if(!division.equals(""))
            {
             emp.setDivisionId(Integer.parseInt(division));
            }
            else
            {emp.setDivisionId(Integer.parseInt("0"));}
            emp.setSignature(fileUpload.getBytes());
            emp.setSignatureName(fileUpload.getFileName());
            emp.setSignatureNumber(signNo);
            emp.setCategory(category);
            emp.setJobTitleId(Integer.parseInt(cbJobTitle));
            emp.setSalaryGradeId(Integer.parseInt(cbGrade));
            if (changeLogo == false) {
                //view = new ModelAndView("error");
                //view.addObject("message", "System Error, Signature Image is required.");
                //return view;
                emp.setSignature(null);
                emp.setSignatureName(null);
            }

            boolean saved = getEmployeeService().updateEmployeeBasic(emp, getEmployeeService().MODE_INSERT);
            if (saved == false) {
                view = new ModelAndView("error");
                view.addObject("message", "Unable to save new employee Signature.");
                return view;
            }
            createNewAuditTrail(request, "New Staff: Record[" + emp.toString() + "]", AuditOperationCodeConstant.Create_Employee_Record_Successfully);
            view.addObject("message", "Signature Saved Successfully.");

        } else if (operation.trim().equals("E")) {
            Employee emp = new Employee();
            String id = request.getParameter("txtId");

            String firstName = request.getParameter("txtFirstName");
            String lastName = request.getParameter("txtLastName");
            String otherNames = request.getParameter("txtOtherName");
            String salutation = "";
            String dept = request.getParameter("cbDept");
            String phone = request.getParameter("txtPhone");
            String ext = request.getParameter("txtExt");
            String email = request.getParameter("txtEmail");
            String division = request.getParameter("txtSolId");
            String staffId = request.getParameter("txtStaffId");
            String signNo = request.getParameter("txtSignNo");
            String category = request.getParameter("cbCategory");
            String cbJobTitle = request.getParameter("cbJobTitle");
            String cbGrade = request.getParameter("cbGrade");
            String branchDesc = request.getParameter("txtBranch");
            //id for the table
            emp.setBranchDesc(branchDesc);
            emp.setEmployeeNumber(Long.parseLong(id));
            emp.setStaffId(staffId);
            emp.setSalutation(salutation);
            emp.setFirstName(firstName);
            emp.setLastName(lastName);
            emp.setOtherNames(otherNames);
           
            emp.setDepartmentId(Integer.parseInt("0"));
           
            emp.setCellPhone(phone);
            if (!ext.equals("")) {
                emp.setExtension(Integer.parseInt(ext));
            } else {
                ext = "0";
                emp.setExtension(Integer.parseInt(ext));
            }
            emp.setEmail(email);
            if(!division.equals(""))
            {
              emp.setDivisionId(Integer.parseInt(division));
            }
            else
            {emp.setDivisionId(Integer.parseInt("0"));}
            if (changeLogo == true) {
                emp.setSignature(fileUpload.getBytes());
                emp.setSignatureName(fileUpload.getFileName());
            } else {
                //emp.setSignature(null);
                emp.setSignatureName(null);
            }

            emp.setSignatureNumber(signNo);
            emp.setCategory(category);
            emp.setJobTitleId(Integer.parseInt(cbJobTitle));

            emp.setSalaryGradeId(Integer.parseInt(cbGrade));
            //get previous employee record
            String previousEmp = this.getEmployeeService().findEmployeeBasicById(Long.parseLong(id)).toString();

            boolean saved = getEmployeeService().updateEmployeeBasic(emp, getEmployeeService().MODE_UPDATE);
            if (saved == false) {
                view = new ModelAndView("error");
                view.addObject("message", "Unable to modify existing employee Signature.");
                return view;
            }

            createNewAuditTrail(request, "Saved Employee: Old Record=[" + previousEmp + "], New Record[" + emp.toString() + "]", AuditOperationCodeConstant.Modify_Employee_Record_Successfully);

            view.addObject("message", "Signature Modified Successfully.");
        }
        return view;
    }

    public ModelAndView processDeprecateRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("message");

        Employee emp = new Employee();
        String id = request.getParameter("txtId");

        String comment = request.getParameter("txtComment");

        //id for the table
        emp.setEmployeeNumber(Long.parseLong(id));
        emp.setDeprecateReason(comment);
        emp.setActive("P");

        boolean saved = getEmployeeService().updateEmployeeDeprecateStatus(emp);
        if (saved == false) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to deprecate existing employee Signature.");
            return view;
        }
        createNewAuditTrail(request, "Deprecate Staff: Record[" + emp.toString() + "]", AuditOperationCodeConstant.Deprecated_Employee_Record_Successfully);
        view.addObject("message", "Signature Deprecation Successful.");

        return view;
    }

    //assignSignatureNumbers
    public ModelAndView assignSignatureNumbers(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("employee_signnumber_edit");

        //createNewAuditTrail(request, "Viewing Administration Module to Assign Signature Number");
        return view;
    }

    public ModelAndView processAssignSignatureRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("message");

        Employee emp = new Employee();
        String strCount = request.getParameter("txtCount");
        if (strCount == null || strCount.trim().equals("")) {
            strCount = "200";
        }

        boolean saved = getEmployeeService().assignSignatureNumbers(Integer.parseInt(strCount));
        if (saved == false) {
            view = new ModelAndView("error");
            view.addObject("message", "Unable to assign signature number existing employee Signatures.");
            return view;
        }
        // createNewAuditTrail(request, "Successfully assign signature numbers to staffs without signature number.");
        view.addObject("message", "Signature Automatically Assigned Successfully.");

        return view;
    }

    public SetupService getSetupService() {
        return setupService;
    }

    public void setSetupService(SetupService setupService) {
        this.setupService = setupService;
    }

    /**
     * @return the employeeService
     */
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    /**
     * @param employeeService the employeeService to set
     */
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public ModelAndView displaySignatureInfo(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("signature_display");

        String cbSearch = request.getParameter("cbSearch");
        String txtSearch = request.getParameter("txtSearch");

        Employee emp = null;

        emp = getEmployeeService().findEmployeeSignature(cbSearch, txtSearch);
        if(emp == null)
        {
            view = new ModelAndView("error");
            view.addObject("message", "User with the the User Name is not found");
            return view;
        }
        //view.addObject("showCompany", "Y");
        try {

            String filePath = request.getSession().getServletContext().getRealPath("./upload/" + String.valueOf(emp.getStaffId()) + "_" + emp.getSignatureName());
            File file = new File(filePath);
            if (emp.getSignature() != null) {
                DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file));
                dOut.write(emp.getSignature());
                dOut.flush();
                dOut.close();
            }
            view.addObject("signature_picture", String.valueOf(emp.getStaffId()) + "_" + emp.getSignatureName());
            view.addObject("fullName", emp.getFirstName() + " " + emp.getLastName());
            view.addObject("category", emp.getCategory());
            view.addObject("transactionLimit", emp.getFormattedtxnLimt());
            view.addObject("signatureNumber", emp.getSignatureNumber());
        } catch (Exception e) {
            e.printStackTrace();
            view.addObject("signature_picture", "");
        }

        createNewAuditTrail(request, "Display Signature of Employee: " + emp.getFirstName() + " " + emp.getLastName() + " with Signature: " + emp.getSignatureNumber() + " and category: " + emp.getCategory(), AuditOperationCodeConstant.View_Existing_Employee_Details);
        view.addObject("employee", emp);
        return view;
    }

    public ModelAndView viewEmployeeSignature(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("signature_display");


        return view;
    }

    public ModelAndView uploadBulkSignature(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("multiple_signature_upload");


        return view;
    }

    public ModelAndView viewPageForGenerateTextFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("Nibss_text_file_write");


        return view;
    }

    public ModelAndView gennerateTextFile(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("Nibss_text_file_write");
        String fileName = null;
        try {
            fileName = "NIBSSFILE " + "_" + org.tenece.web.common.DateUtility.getDateAsString(new Date(), "ddMMyyyyHHmmss");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //String  path ="C:\\Temp\\" +fileName+".txt";
        String path = request.getSession().getServletContext().getRealPath("./upload_textfile/" + fileName + ".txt");
        List<Employee> list = getEmployeeService().GetEmployeeInfoForTextFileGeneration();
        // create an object of ProgressDetails and set the total items to be processed
        //ProgressDetails taskProgress = new ProgressDetails();
        int success = 0;
        Iterator<Employee> it = list.iterator();
        while (it.hasNext()) {


            Employee emp = it.next();
            //   count = count +1;
            String text = emp.getFirstName()
                    + "," + emp.getOtherNames()
                    + "," + emp.getLastName()
                    + "," + emp.getCategory()
                    + "," + emp.getSignatureNumber()
                    + "," + emp.getStaffId()
                    + "," + emp.getJobTitle();
            //System.out.println("********"+text);

            FileWriterProcesor writer = new FileWriterProcesor(path, true);
            //writer.
            try {
                writer.writeToFile(text);
                success = 1;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // update the progress
            //ProgressDetails.taskProgressHash.get(taskId).setTotalProcessed(count);

        }
        if (success == 1) {
            view.addObject("message", "The File is successfully generated. The File is located at the server path:" + path);
            view.addObject("path", path);
            HttpSession session = request.getSession(false);
            session.setAttribute("filename", path);
        } else {

            view.addObject("message", "The File could not generated. Contact the System Administrator");

        }
        return view;
    }

    public ModelAndView processMultipleSignatureUploadRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("multiple_signature_upload");

        //check if file is attached and its the right type of request
        if (!(request instanceof MultipartHttpServletRequest)) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
            } catch (IOException ex) {
                System.out.println(ex);
            }
            return new ModelAndView("error");
        }

        boolean changeLogo = false;
        FileUpload fileUpload = new FileUpload();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("txtZipSig");
        try {


            //confirm if file size is valid.
            if (file.getSize() != 0) {
                //check validity of file by confirming extention
                String logoFileName = "";
                if (file.getOriginalFilename().endsWith("zip")) {
                    //check content type (if valid)

                    if (!ConfigReader.getAllowedImagesContentType().contains(file.getContentType())) {
                        view = new ModelAndView("error");
                        view.addObject("message", "Invalid File Type. Only the following file types are allowed: zip");
                        return view;
                    }
                    //build a dynamic file name that will be valid across system
                    logoFileName = "Zipped_signature_" + DateUtility.getDateAsString(new Date(), "ddMMyyyy_HHmmss") + "." + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);
                } else {
                    view = new ModelAndView("error");
                    view.addObject("message", "Invalid File Type. Only the following file types are allowed: zip");
                    return view;
                }

                fileUpload.setFileName(file.getOriginalFilename());
                try {
                    fileUpload.setBytes(file.getBytes());
                } catch (IOException ex) {
                    throw ex;
                }
                fileUpload.setSize(file.getSize());
                fileUpload.setContentType(file.getContentType());
                fileUpload.setOrginalFileName(file.getOriginalFilename());
                //indicate the destination file Name
                File destination = new File("./" + file.getOriginalFilename());

                //save file in archive

                fileUpload.setAbsolutePath(destination.getAbsolutePath());
                System.out.println("destination.getAbsolutePath() = " + destination.getAbsolutePath());
                File folder = new File(request.getSession().getServletContext().getRealPath("./upload/" + fileUpload.getFileName()));

                if (!folder.exists()) {
                    folder.delete();
                }
                try {
                    file.transferTo(destination);
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    throw ex;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
                changeLogo = true;
            }
        } catch (PDFException ex) {
            ex.printStackTrace();
            view.addObject("message", ex.getMessage());
            return view;
        } catch (Exception ex) {
            ex.printStackTrace();
            view.addObject("message", "System error while processing uploaded file. Try again later or with a valid file.");
            return view;
        }
        //preview time
        if (changeLogo == true) {
            //get company picture when loading data
            try {
                String filePathZip = request.getSession().getServletContext().getRealPath("./upload/" + fileUpload.getFileName());
                //String filePathFolder = removeExtension(filePathZip)+"/"+removeExtension(file.getOriginalFilename());
                String filePathFolder = request.getSession().getServletContext().getRealPath("./upload/unzip");
                System.out.println("filePathFolder= " + filePathFolder);
                File Zipfile = new File(filePathZip);
                DataOutputStream dOut = new DataOutputStream(new FileOutputStream(Zipfile));
                dOut.write(fileUpload.getBytes());
                dOut.flush();
                dOut.close();
                //un zip the file

                List<String> imageList = this.unZipIt(filePathZip, filePathFolder);

                String[] array = new String[imageList.size()];
                int imagecount = 0;
                Iterator<String> it = imageList.iterator();
                while (it.hasNext()) {
                    String imageUrl = it.next();
                    String imageUrl_New = imageUrl.replace("/", "\\");
                    System.out.print("imageUrl ==" + imageUrl_New);
                    array[imagecount] = imageUrl_New;
                    imagecount = imagecount + 1;

                }
                view.addObject("imageArray", array);
                request.setAttribute("imageArrayList", array);

            } catch (Exception e) {
                e.printStackTrace();
                view.addObject("imageArray", "");
                view.addObject("showImage", "0");
                view.addObject("message", "Error previewing uploaded image. Please try again later.");
            }

        }
        return view;
    }

    public static String removeExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1) {
            return s;
        }

        return s.substring(0, lastSeparatorIndex + 1)
                + filename.substring(0, extensionIndex);

    }

    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public List<String> unZipIt(String zipFile, String outputFolder) {

        List<String> list = new ArrayList();

        try {

            //create output directory is not exists
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                folder.delete();
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));


            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            String lastfileName = null;
            while (ze != null) {

                String fileName = ze.getName();
                String filePath = outputFolder + File.separator + ze.getName();
                //File newFile = new File(outputFolder + File.separator + fileName);

                if (!ze.isDirectory()) {
                    extractFile(zis, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                lastfileName = fileName;
                zis.closeEntry();
                ze = zis.getNextEntry();
                String FilePath = outputFolder + File.separator + fileName;
                if (FilePath.endsWith("jpg")) {
                    list.add(FilePath);
                }

            }

            // String new_Folder_Dir = removeExtension(outputFolder + File.separator + lastfileName);
            zis.close();

            System.out.println("Done");
            // System.out.println("new_Folder_Dir= "+ new_Folder_Dir);
            return list;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Extracts a single file
     * @param zipIn the ZipInputStream
     * @param filePath Path of the destination file
     * @throws IOException if any I/O error occurred
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public ModelAndView gennerateTextFileForEBook(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("Ebook_text_file_write");
        String fileName = null;
        String sigFolder = null;
        try {
            fileName = "FBN_ebook_File";
            sigFolder = "FBN_ebook_SigDir " + "_" + org.tenece.web.common.DateUtility.getDateAsString(new Date(), "ddMMyyyyHHmmss");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //String  path ="C:\\Temp\\" +fileName+".txt";
        String path = request.getSession().getServletContext().getRealPath("./upload_textfile/" + fileName + ".txt");
        String sigFolderDir = request.getSession().getServletContext().getRealPath("./upload_textfile/" + sigFolder);
        //create output directory is not exists
        File folder = new File(sigFolderDir);
        if (!folder.exists()) {
            folder.mkdir();
        }
        List<Employee> list = getEmployeeService().getEmployeeInfoForEbook();
        // create an object of ProgressDetails and set the total items to be processed
        //ProgressDetails taskProgress = new ProgressDetails();
        int success = 0;
        Iterator<Employee> it = list.iterator();
        while (it.hasNext()) {


            Employee emp = it.next();
            //   count = count +1;
            String text = emp.getFirstName() + " " + emp.getOtherNames() + " " + emp.getLastName()
                    + "|" + emp.getStaffId()
                    + "|" + emp.getSignatureNumber()
                    + "|" + emp.getCategory()
                    + "|" + emp.getSignatureName()
                    + "|" + emp.getFormattedtxnLimt();
            //System.out.println("********"+text);

            FileWriterProcesor writer = new FileWriterProcesor(path, true);
            //writer.
            try {
                writer.writeToFile(text);
                String filePathForSave = sigFolderDir + "/" + emp.getSignatureNumber() + ".jpg";
                File fileimage = new File(filePathForSave);
                DataOutputStream dOut = new DataOutputStream(new FileOutputStream(fileimage));
                dOut.write(emp.getSignature());
                dOut.flush();
                dOut.close();
                success = 1;
              
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        }
        if (success == 1) {
            view.addObject("message", "The File is successfully generated. The File is located at the server path:" + path);
            view.addObject("path", path);
            HttpSession session = request.getSession(false);
            session.setAttribute("filename2", path);
            System.out.println(session.getAttribute("filename2"));
            
        } else {

            view.addObject("message", "The File could not generated. Contact the System Administrator");

        }
        
        return view;
    }

    public ModelAndView viewEbookPageForGenerateTextFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("Ebook_text_file_write");


        return view;
    }

    public ModelAndView getEmployeeInfoFromActiveDirectory(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("employee_edit");
        String staffIdToSearch = request.getParameter("staffId");
        try {
            //get all needed objects
            view.addObject("dept", setupService.findAllDepartments());
            //view.addObject("jobTitle", setupService.findAllJobTitles());
            view.addObject("divisionList", setupService.findAllDivisions());
            view.addObject("jobTitleList", setupService.findAllJobTitles());
            view.addObject("grade", setupService.findAllGrades());
            ArrayList<Category> list = new ArrayList<Category>();
            Category cat1 = new Category();
            cat1.setKey("A");
            cat1.setDescription("Category A");
            Category cat2 = new Category();
            cat2.setKey("B");
            cat2.setDescription("Category B");
            list.add(cat1);
            list.add(cat2);
            view.addObject("categoryList", list);
            Properties actConfig = ConfigReader.getActiveDirectoryConfig();
            String servername = actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_LOCATION);
            String domainName = actConfig.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_DOMAIN);
            HttpSession session = request.getSession(true);
            String loginPassword = (String) session.getAttribute("password");
            String loginUserName = (String) session.getAttribute("userName");
            LdapContext ctx = ActiveDirectory.getConnection(loginUserName, loginPassword, domainName, servername);
            Employee emp = ActiveDirectory.getUser(staffIdToSearch, ctx);
            // String fullName = emp.getFullName();
            if (emp == null) {
                
                view.addObject("message", "Could not find the User in the System");
                return view;
            }
            emp.setSignatureNumber(staffIdToSearch);
            emp.setStaffId(staffIdToSearch);


            view.addObject("employee", emp);
            view.addObject("pageTitle", "new");
            return view;
            //assign values to session as attribute
        } catch (Exception e) {
        }


        return view;
    }
}