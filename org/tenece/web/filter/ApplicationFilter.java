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

package org.tenece.web.filter;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tenece.ap.security.SecurityEncoderImpl;
import org.tenece.web.common.ConfigReader;

/**
 *
 * @author amachree
 */
public class ApplicationFilter implements Filter{
    
    public static String APPLICATION_DBTYPE = "Database.Type";
    public static String APPLICATION_DATABASE_URL = "Database.Connection.URL";
    public static String APPLICATION_DATABASE_USERNAME = "Database.UserName";
    public static String APPLICATION_DATABASE_PASSWORD = "Database.Password";
    public static String APPLICATION_DATABASE_DRIVER = "Database.Driver";
    public static String APROVAL_LEVEL_COUNT = "approved.advance.count";
    public static String SIGNATORY_SCANNED_PATH = "signatory.scanned.path";
    public static String REGION_CURRENCY_SYMBOL = "region.currency.symbol";
    public static String REGION_CURRENCY_NAME = "region.currency.name";
    public static String REGION_CURRENCY_DECIMAL_NAME = "region.currency.decimal.symbol";
    public static String APPLICATION_CONFIG_FILE_PREFIX = "application.config.file.name.prefix";
    public static String ACTIVE_DIRECTORY_SERVER_LOCATION = "fbn.active.directory.server.location";
    public static String ACTIVE_DIRECTORY_SERVER_DOMAIN = "fbn.active.directory.server.domain.name";
    public static String ACTIVE_DIRECTORY_CHECK = "fbn.active.directory.check";
    
    private FilterConfig filterConfig = null;
    /** Creates a new instance of AuthenticationFilter */
    public ApplicationFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("APPLICATION FILTER- INIT METHOD FOR AUTH FILTER");
        setFilterConfig(filterConfig);
        initialize(filterConfig);
    }
    
    public void initialize(FilterConfig filterConfig){
        Properties configuration = null;
        String dbDriver = "";
        String dbURL = "";
        String dbUID = "";
        String dbPWD = "";
        String dbType = "";
        //get approval route level
        String approvalLevel = "";
        String signatoryScannedPath = "";
        //currency
        String currencySymbol = "";
        String currencyName = "Naira";
        String currencyDecimalName = "Kobo";
        String activeDirectoryServer = "";
        String activeDirectorydomain = "";
        String activeDirectorycheck = "";
        //get the application context
        ServletContext context = filterConfig.getServletContext();

        String configFilePrefix = context.getInitParameter(ApplicationFilter.APPLICATION_CONFIG_FILE_PREFIX);
        if(configFilePrefix != null){
            ConfigReader.setFile_Prefix(configFilePrefix);
        }
        try{
            configuration = ConfigReader.loadSystemConfiguration();
            
        }catch(Exception e){
            e.printStackTrace();
            configuration = null;
        }

        if(configuration != null){
            dbDriver = configuration.getProperty(ApplicationFilter.APPLICATION_DATABASE_DRIVER);
            dbURL = configuration.getProperty(ApplicationFilter.APPLICATION_DATABASE_URL);
            dbUID = configuration.getProperty(ApplicationFilter.APPLICATION_DATABASE_USERNAME);
            dbPWD = configuration.getProperty(ApplicationFilter.APPLICATION_DATABASE_PASSWORD);
            dbType = configuration.getProperty(ApplicationFilter.APPLICATION_DBTYPE);
            //approval level
            approvalLevel = configuration.getProperty(ApplicationFilter.APROVAL_LEVEL_COUNT);
            //SCANNED PATH
            signatoryScannedPath = configuration.getProperty(ApplicationFilter.SIGNATORY_SCANNED_PATH);
            //currency
            currencySymbol = configuration.getProperty(ApplicationFilter.REGION_CURRENCY_SYMBOL);
            currencyName = configuration.getProperty(ApplicationFilter.REGION_CURRENCY_NAME);
            currencyDecimalName = configuration.getProperty(ApplicationFilter.REGION_CURRENCY_DECIMAL_NAME);
            activeDirectoryServer = configuration.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_LOCATION);
            activeDirectorydomain = configuration.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_DOMAIN);
            activeDirectorycheck =configuration.getProperty(ApplicationFilter.ACTIVE_DIRECTORY_CHECK);
        }else{
            System.out.println("Unable to read configuration file. Using default setting in application");
            
            //database settings
            dbDriver = context.getInitParameter(ApplicationFilter.APPLICATION_DATABASE_DRIVER);
            dbURL = context.getInitParameter(ApplicationFilter.APPLICATION_DATABASE_URL);
            dbUID = context.getInitParameter(ApplicationFilter.APPLICATION_DATABASE_USERNAME);
            dbPWD = context.getInitParameter(ApplicationFilter.APPLICATION_DATABASE_PASSWORD);
            dbType = context.getInitParameter(ApplicationFilter.APPLICATION_DBTYPE);

            //get approval route level
            approvalLevel = context.getInitParameter(ApplicationFilter.APROVAL_LEVEL_COUNT);
            signatoryScannedPath = "";
        }
        ConfigReader.setApprovalLevelCount(Integer.parseInt(approvalLevel));
        ConfigReader.setRegionCurrencySymbol(currencySymbol);
        ConfigReader.setRegionCurrencyName(currencyName);
        ConfigReader.setRegionCurrencyDecimalName(currencyDecimalName);
        ConfigReader.setSignatoryScannedPath(signatoryScannedPath);
   

        //decrypt the encoded username and password to access database
        String strDBUserName = "";
        String strDBPassword = "";
        try{
            strDBUserName = SecurityEncoderImpl.decryptPasswordWithAES(dbUID);
        }catch(Exception e){
            strDBUserName = "";
            System.out.println(e + " =Invalid UID for Database");
        }
       
        try{
            strDBPassword = SecurityEncoderImpl.decryptPasswordWithAES(dbPWD);
        }catch(Exception e){
            strDBPassword = "";
            System.out.println(e + " =Invalid PWD for Database");
        }

        Properties dbProperties = new Properties();
        dbProperties.setProperty(ApplicationFilter.APPLICATION_DATABASE_DRIVER, dbDriver);
        dbProperties.setProperty(ApplicationFilter.APPLICATION_DATABASE_URL, dbURL);
        dbProperties.setProperty(ApplicationFilter.APPLICATION_DATABASE_USERNAME, strDBUserName);
        dbProperties.setProperty(ApplicationFilter.APPLICATION_DATABASE_PASSWORD, strDBPassword);
        dbProperties.setProperty(ApplicationFilter.APPLICATION_DBTYPE, dbType);
        //add properties to config Reader
        ConfigReader.setDatabaseConfig(dbProperties);
        
        Properties acProperties = new Properties();
        acProperties.setProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_DOMAIN, activeDirectorydomain);
        acProperties.setProperty(ApplicationFilter.ACTIVE_DIRECTORY_SERVER_LOCATION, activeDirectoryServer);
        //acProperties.setProperty(ApplicationFilter.ACTIVE_DIRECTORY_CHECK, activeDirectorycheck);
        
        ConfigReader.setActiveDirectoryConfig(acProperties);
    }

    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) _request;
        HttpServletResponse response = (HttpServletResponse) _response;
        
    }

    public void destroy() {
        System.out.println("APPLICATION FILTER - DESTROY OF AUTH FILTER");
        
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
    
}
