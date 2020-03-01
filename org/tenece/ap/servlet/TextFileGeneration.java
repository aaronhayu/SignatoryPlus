/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tenece.ap.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tenece.web.common.FileWriterProcesor;
import org.tenece.web.data.Employee;
import org.tenece.web.data.ProgressDetails;
import org.tenece.web.services.EmployeeService;

/**
 *
 * @author kenneth
 */
public class TextFileGeneration extends HttpServlet {
    public TextFileGeneration() {
        super();
    }
 
    private EmployeeService employeeService = new EmployeeService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     System.out.print("generateTextFileForNibss");
               String  path ="C:\\Temp\\NIBSSFILE.txt";
               // read the tadkId;
                String taskId = request.getParameter("taskIdentity"); 
                 System.out.print("taskId"+taskId);
                // some stuff here
                // some more stuff
                List<Employee> list = getEmployeeService().GetEmployeeInfoForTextFileGeneration();
                // create an object of ProgressDetails and set the total items to be processed
                ProgressDetails taskProgress = new ProgressDetails();
                
                taskProgress.setTotal(list.size());

                // store the taskProgress object using taskId as key
                ProgressDetails.taskProgressHash.put(taskId, taskProgress);
                int count= 0;
                // for each record to be processed
                Iterator<Employee> it = list.iterator();
                     while(it.hasNext()){
                
                 
                      Employee emp = it.next();
                      count = count +1;
                      String text = emp.getFirstName()
                        +","+emp.getOtherNames()
                        +","+emp.getLastName()
                        +","+emp.getCategory()
                        +","+emp.getSignatureNumber()
                        +","+emp.getStaffId()
                        +","+emp.getJobTitle();
                System.out.println("********"+text);
                FileWriterProcesor writer = new FileWriterProcesor(path,true);
                try
                {
                  writer.writeToFile(text);
                }
                catch (IOException ex)
                {
                   ex.printStackTrace();
                }
                // update the progress
                ProgressDetails.taskProgressHash.get(taskId).setTotalProcessed(count);

                }
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
    
}
