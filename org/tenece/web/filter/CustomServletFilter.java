/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tenece.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.data.Users;



/**
 *
 * @author kenneth
 */
public class CustomServletFilter implements Filter{
     private FilterConfig filterConfig = null;
    /** Creates a new instance of AuthenticationFilter */
    public CustomServletFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("INIT METHOD FOR CUSTOM SERVLET FILTER");
        setFilterConfig(filterConfig);
    }

    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) _request;
        HttpServletResponse response = (HttpServletResponse) _response;

        StringBuffer url = request.getRequestURL();
        HttpSession session = request.getSession(false);
        Users userPrincipal = null;
        try{
            userPrincipal = (Users) session.getAttribute("userPrincipal");
        }catch(Exception e){}
     
        if(userPrincipal == null){
            //System.out.println("SecurityFilter::Session Has Expired..." + request.getUserPrincipal());
            filterConfig.getServletContext().getRequestDispatcher("/user_logout.html").forward(request,response);
        
        }else if(userPrincipal != null && request.getSession().getAttribute("sessionCount")==null){
            session.setAttribute("sessionCount", "1");

            String sessionID = session.getId();
            String storedSessionID = ConfigReader.getUserSession(userPrincipal.getUserName());
            if(storedSessionID == null){
                ConfigReader.addUserSession(userPrincipal.getUserName(), sessionID);
            }else if(!storedSessionID.equals(sessionID)){
                filterConfig.getServletContext().getRequestDispatcher("/user_logout.html?key=M").forward(request,response);
            }
        }
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        }catch(Throwable t) {
            problem = t;
            t.printStackTrace();
        }
        
        if (problem != null) {
            if (problem instanceof ServletException) throw (ServletException)problem;
            if (problem instanceof IOException) throw (IOException)problem;
            filterConfig.getServletContext().getRequestDispatcher("/error.html?message=System Error Loading Request.").forward(request,response);
        }
    }

    public void destroy() {
        System.out.println("DESTROY OF CUSTOM SERVLET FILTER");

    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
}
