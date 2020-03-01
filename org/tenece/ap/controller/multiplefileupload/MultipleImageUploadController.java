/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tenece.ap.controller.multiplefileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.tenece.ap.form.MultiFileUploadForm;
import org.tenece.web.services.EmployeeService;
import org.tenece.web.services.SetupService;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author kenneth
 */
@Controller
public class MultipleImageUploadController  {
     
    LinkedList<MultiFileUploadForm> files = new LinkedList<MultiFileUploadForm>();
    MultiFileUploadForm fileMeta = null;

  
    //private EmployeeService employeeService = new EmployeeService();
    //private SetupService setupService = new SetupService();
    /** Creates a new instance of EmployeeController */
    public MultipleImageUploadController() 
    {
      
    }
      // PRIVATE 
/**
     * Size of a byte buffer to read/write file
     */
    private static final int BUFFER_SIZE = 4096;
             
    /**
     * Path of the file to be downloaded, relative to application's directory
     */
    private String filePath ;
    private EmployeeService employeeService = new EmployeeService();
    private SetupService setupService = new SetupService();
     @RequestMapping(value = "/save_bulk_signature_employees.html", method = RequestMethod.POST)
	public String save(
			@ModelAttribute("uploadForm") MultiFileUploadForm uploadForm,
					Model map, BindingResult result) {
         System.out.println("<<<<<<<<<<MMMMMMMMMMMM>>>>>>>>>>>>>>>");
//		MultipartFile[] files = uploadForm.getFiles();
//		List<String> fileNames = new ArrayList<String>();
//		
//		if(null != files && files.length > 0) {
//			for (MultipartFile multipartFile : files) {
//
//				String fileName = multipartFile.getOriginalFilename();
//				fileNames.add(fileName);
//				//Handle file content - multipartFile.getInputStream()
//                                System.out.println("fileName = "+fileName);
//
//			}
//		}
//		
//		map.addAttribute("files", fileNames);
		return "file_upload_success";
	}
   

     @RequestMapping(value = "/download_textFile.html", method = RequestMethod.GET)
    public void doDownload(HttpServletRequest  request,
            HttpServletResponse response) throws IOException {
       System.out.println("<<<<<<<<<<MMMMMMMMMMMM>>>>>>>>>>>>>>>");
        // get absolute path of the application
        ServletContext context = request.getSession().getServletContext();
 
        // construct the complete absolute path of the file  
        HttpSession session = request.getSession(false);

        String fullPath = (String)session.getAttribute("filename");
        System.out.println("<<<<<<<<<<fullPath>>>>>>>>>>>>>>>==="+fullPath);
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
         
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
 
        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
 
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);
 
        // get output stream of the response
        OutputStream outStream = response.getOutputStream();
 
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
 
        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
 
        inputStream.close();
        outStream.close();
 
    }
      
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    /**
     * @param employeeService the employeeService to set
     */
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
   
    /***************************************************
     * URL: /rest/controller/upload  
     * upload(): receives files
     * @param request : MultipartHttpServletRequest auto passed
     * @param response : HttpServletResponse auto passed
     * @return LinkedList<FileMeta> as json format
     ****************************************************/
     @RequestMapping(value="/upload.html", method = RequestMethod.POST)
     public @ResponseBody LinkedList<MultiFileUploadForm> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
         
         System.out.println(">>>>>>>>>>>>>upload>>>>>>>>>>");
        //1. build an iterator
         Iterator<String> itr =  request.getFileNames();
         MultipartFile mpf = null;
 
         //2. get each file
         while(itr.hasNext()){
 
             //2.1 get next MultipartFile
             mpf = request.getFile(itr.next()); 
             System.out.println(mpf.getOriginalFilename() +" uploaded! "+files.size());
 
             //2.2 if files > 10 remove the first from the list
             if(files.size() >= 10)
                 files.pop();
 
             //2.3 create new fileMeta
             fileMeta = new MultiFileUploadForm();
             fileMeta.setFileName(mpf.getOriginalFilename());
             fileMeta.setFileSize(mpf.getSize()/1024+" Kb");
             fileMeta.setFileType(mpf.getContentType());
 
             try {
                fileMeta.setBytes(mpf.getBytes());
 
                 // copy file to local disk (make sure the path "e.g. D:/temp/files" exists)            
                 FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream("D:/temp/files/"+mpf.getOriginalFilename()));
 
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             //2.4 add to files
             files.add(fileMeta);
         }
        // result will be like this
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
        return files;
    }
      @RequestMapping(value = "/get/{value}.html", method = RequestMethod.GET)
     public void get(HttpServletResponse response,@PathVariable String value){
         MultiFileUploadForm getFile = files.get(Integer.parseInt(value));
         try {      
                response.setContentType(getFile.getFileType());
                response.setHeader("Content-disposition", "attachment; filename=\""+getFile.getFileName()+"\"");
                FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
         }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
         }
     }
      /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
//    public void unZipIt(String zipFile, String outputFolder){
// 
//     byte[] buffer = new byte[1024];
// 
//     try{
// 
//    	//create output directory is not exists
//    	File folder = new File(outputFolder);
//    	if(!folder.exists()){
//    		folder.mkdir();
//    	}
// 
//    	//get the zip file content
//    	ZipInputStream zis = 
//    		new ZipInputStream(new FileInputStream(zipFile));
//    	//get the zipped file list entry
//    	ZipEntry ze = zis.getNextEntry();
// 
//    	while(ze!=null){
// 
//    	   String fileName = ze.getName();
//           File newFile = new File(outputFolder + File.separator + fileName);
// 
//           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
// 
//            //create all non exists folders
//            //else you will hit FileNotFoundException for compressed folder
//            new File(newFile.getParent()).mkdirs();
// 
//            FileOutputStream fos = new FileOutputStream(newFile);             
// 
//            int len;
//            while ((len = zis.read(buffer)) > 0) {
//       		fos.write(buffer, 0, len);
//            }
// 
//            fos.close();   
//            ze = zis.getNextEntry();
//    	}
// 
//        zis.closeEntry();
//    	zis.close();
// 
//    	System.out.println("Done");
// 
//    }catch(IOException ex){
//       ex.printStackTrace(); 
//    }
 //  }  
     @RequestMapping(value = "/downloadEbookTextFile_employee.html", method = RequestMethod.GET)
    public void doDownloadEbook(HttpServletRequest  request,
            HttpServletResponse response) throws IOException {
       System.out.println("<<<<<<<<<<MMMMMMMMMMMM>>>>>>>>>>>>>>>");
        // get absolute path of the application
        ServletContext context = request.getSession().getServletContext();
 
        // construct the complete absolute path of the file  
        HttpSession session = request.getSession(false);

        String fullPath = (String)session.getAttribute("filename2");
          //String fullPath = (String)session.getAttribute("filename");
        System.out.println("<<<<<<<<<<fullPath>>>>>>>>>>>>>>>==="+fullPath);
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
         
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
 
        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
 
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);
 
        // get output stream of the response
        OutputStream outStream = response.getOutputStream();
 
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
 
        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
 
        inputStream.close();
        outStream.close();
 
    }
      @RequestMapping(value = "/getBranchDescription.html", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<String> doGetBranchDescription(HttpSession session, @RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
         System.out.println("here===="+code);
        // return new ResponseEntity<AppraisalGroup>();
        String description = setupService.findAllDivisionDescriptionBySolID(code);
            //System.out.println("description===="+description);
        String json = null;
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("description", description);
        ObjectMapper map = new ObjectMapper();
        if (!result.isEmpty()) {
            try {
                json = map.writeValueAsString(result);
                System.out.println("Send Message  :::::::: : " + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
    }
       public SetupService getSetupService() {
        return setupService;
    }

    public void setSetupService(SetupService setupService) {
        this.setupService = setupService;
    }
}

