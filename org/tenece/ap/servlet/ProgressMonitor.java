/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tenece.ap.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tenece.web.data.ProgressDetails;

/**
 *
 * @author kenneth
 */
public class ProgressMonitor extends HttpServlet{
    protected void doPGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // get the taskId
      String taskId = request.getParameter("taskIdentity");
 
      // get the progres of this task
      ProgressDetails taskProgress = ProgressDetails.taskProgressHash.get(taskId);
 
      // write the progress in the response
      response.getWriter().write(taskProgress.toString());
   }
}
