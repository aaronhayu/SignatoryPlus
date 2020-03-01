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
 * Neither the name of Tenece. or the names of
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
 * even if Tenece has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
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
import org.tenece.web.data.AuditTrail;
import org.tenece.web.data.Key;
import org.tenece.web.data.Users;
import org.tenece.web.services.ApplicationService;

/**
 *
 * @author amachree
 */
public class AuthenticationFilter implements Filter{

    private FilterConfig filterConfig = null;
    /** Creates a new instance of AuthenticationFilter */
    public AuthenticationFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("INIT METHOD FOR AUTH FILTER");
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

        List<String> excludeList = new ArrayList<String>();
        excludeList.add("user_login.html");
        excludeList.add("user_logout.html");
        excludeList.add("check_login.html");
        excludeList.add("view_user_password_recall.html");
        excludeList.add("save_user_password_recall.html");
        excludeList.add("index.html");
        excludeList.add("captcha_image.html");

        boolean isIndexFound = false;
        for(String tmpURL : excludeList){
            if(url.indexOf(tmpURL) >= 0){
                isIndexFound = true;
                break;
            }
        }

        if(isIndexFound == false && userPrincipal == null){
            //System.out.println("SecurityFilter::Session Has Expired..." + request.getUserPrincipal());
            filterConfig.getServletContext().getRequestDispatcher("/user_logout.html").forward(request,response);
        }else if(isIndexFound == true && userPrincipal == null){
            //do nothing and move on

        }else if(isIndexFound == false && userPrincipal != null){
            //do nothing and move on

            //check session date for last activity and check current time
            Date activityDate = (Date)session.getAttribute("activityDate");
            GregorianCalendar gCal = new GregorianCalendar();
            gCal.setTime(activityDate);
            gCal.add(Calendar.MINUTE, 5);

            if(gCal.getTime().before(new Date())){
                session.invalidate();
                filterConfig.getServletContext().getRequestDispatcher("/user_logout.html?key=M").forward(request,response);
            }else{
                session.removeAttribute("activityDate");
                session.setAttribute("activityDate", new java.util.Date());
            }

            String sessionID = session.getId();
            String storedSessionID = ConfigReader.getUserSession(userPrincipal.getUserName());
            if(storedSessionID == null){
                ConfigReader.addUserSession(userPrincipal.getUserName(), sessionID);
            }else if(!storedSessionID.equals(sessionID)){
                filterConfig.getServletContext().getRequestDispatcher("/user_logout.html?key=M").forward(request,response);
            }

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
        System.out.println("DESTROY OF AUTH FILTER");

    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
