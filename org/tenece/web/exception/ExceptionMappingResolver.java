/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tenece.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 *
 * @author jeffry.amachree
 */
public class ExceptionMappingResolver extends SimpleMappingExceptionResolver{

    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        //print stacktrace to console for debug purposes
        System.out.println("Exception Expected Handler..." + handler);

        ex.printStackTrace();
        //check if its null pointer and if user session is valid
        if(ex instanceof NullPointerException){
            //check if session for principal is null (since filter will ignore this)
            HttpSession session = request.getSession();
            if(session.getAttribute("userPrincipal") == null){
                return new ModelAndView("logout");
            }
        }
        
        ModelAndView view = new ModelAndView("error");
        view.addObject("message", "System Error, Unable to complete operation.");
        return view;
    }


}
