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

package org.tenece.web.common;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import org.tenece.web.data.SMTPSettings;

/**
 *
 * @author jeffry.amachree
 */
public class ConfigReader {
    public static String DATE_FORMAT = "dd-MM-yyyy";
    public static String REPORT_EXTENSION = "jasper";
    public static int NUMBER_OF_APPROVAL_USER_LEVEL = 0;
    
    
    private static String emailMessageTemplate = "";

    private static String REGION_CURRENCY_SYMBOL = "";
    private static String REGION_CURRENCY_NAME = "";
    private static String REGION_CURRENCY_DECIMAL_NAME = "";

    private static String FILE_PREFIX = "volmacht";
    
    private static Properties databaseConfig = new Properties();
    private static Properties activeDirectoryConfig = new Properties();
    private static String SIGNATORY_SCANNED_PATH = "";

    //property to get loggedin user
    private static Hashtable<String, String>  USER_SESSION = new Hashtable<String, String>();

    /**
     * @return the REGION_CURRENCY_SYMBOL
     */
    public static String getRegionCurrencySymbol() {
        return REGION_CURRENCY_SYMBOL;
    }

    /**
     * @param aREGION_CURRENCY_SYMBOL the REGION_CURRENCY_SYMBOL to set
     */
    public static void setRegionCurrencySymbol(String currencySymbol) {
        REGION_CURRENCY_SYMBOL = currencySymbol;
    }

    /**
     * @return the REGION_CURRENCY_NAME
     */
    public static String getRegionCurrencyName() {
        return REGION_CURRENCY_NAME;
    }

    /**
     * @param aREGION_CURRENCY_NAME the REGION_CURRENCY_NAME to set
     */
    public static void setRegionCurrencyName(String currencyName) {
        REGION_CURRENCY_NAME = currencyName;
    }

    /**
     * @return the REGION_CURRENCY_DECIMAL_NAME
     */
    public static String getRegionCurrencyDecimalName() {
        return REGION_CURRENCY_DECIMAL_NAME;
    }

    /**
     * @param aREGION_CURRENCY_DECIMAL_NAME the REGION_CURRENCY_DECIMAL_NAME to set
     */
    public static void setRegionCurrencyDecimalName(String decimalName) {
        REGION_CURRENCY_DECIMAL_NAME = decimalName;
    }

    /**
     * @return the FILE_PREFIX
     */
    public static String getFile_Prefix() {
        return FILE_PREFIX;
    }

    /**
     * @param aFILE_PREFIX the FILE_PREFIX to set
     */
    public static void setFile_Prefix(String aFILE_PREFIX) {
        FILE_PREFIX = aFILE_PREFIX;
    }

    /**
     * @return the SIGNATORY_SCANNED_PATH
     */
    public static String getSignatoryScannedPath() {
        return SIGNATORY_SCANNED_PATH;
    }

    public static void setSignatoryScannedPath(String path) {
        SIGNATORY_SCANNED_PATH = path;
    }

    /** Creates a new instance of ConfigReader */
    public ConfigReader() {
    }

    public static Vector getAllowedImagesContentType(){
        Vector types = new Vector();
        types.add("image/png");
        types.add("image/x-png");
        types.add("image/jpeg");
        types.add("image/x-jpeg");
        types.add("image/pjpeg");
        types.add("image/gif");
        types.add("image/x-gif");
        types.add("application/pdf");
        types.add("application/x-pdf");
        types.add("application/octet-stream");
        
        return types;
    }
    
    public static Vector getSpecialCharacters(){
        Vector<String> types = new Vector<String>();
        types.add("~");
        types.add("!");
        types.add("@");
        types.add("#");
        types.add("$");
        types.add("%");
        types.add("^");
        types.add("&");
        types.add("*");
        types.add("(");
        types.add(")");
        types.add("{");
        types.add("}");
        types.add("?");
        return types;
    }

    public static boolean validatePasswordPolicy(String password, int length){
        if(password.length() < length){
            return false;
        }
        boolean passwordCorrect = false;
        Vector<String> characters = ConfigReader.getSpecialCharacters();
        //char[] xChars = password.toCharArray();
        for(String specialChar : characters){
            if(password.contains(specialChar)){
                passwordCorrect = true;
                break;
            }
        }
        return passwordCorrect;
    }

    public static Properties getDatabaseConfig(){
        return databaseConfig;
    }
    public static void setDatabaseConfig(Properties properties){
        databaseConfig = properties;
    }
     public static Properties getActiveDirectoryConfig(){
        return activeDirectoryConfig;
    }
    public static void setActiveDirectoryConfig(Properties properties){
        activeDirectoryConfig = properties;
    }

    public static void setApprovalLevelCount(int levelCount){
        NUMBER_OF_APPROVAL_USER_LEVEL = levelCount;
    }
    

    public static String tag_report(String string) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        return string + calendar.get(calendar.MONTH) + calendar.get(calendar.DAY_OF_MONTH) + calendar.get(calendar.YEAR);
    }

    public static SMTPSettings loadSMTPSettings(String fileName) throws FileNotFoundException, IOException{
        return loadSMTPSettings();
    }
    public static SMTPSettings loadSMTPSettings() throws FileNotFoundException, IOException{
        File file = new File(ConfigReader.getFile_Prefix() + "-smtp.xml");

        System.out.println("Reading SMTP Configuration from source." + file.getAbsolutePath());

        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(file));
        
        SMTPSettings smtp = new SMTPSettings();
        smtp.setMailer(properties.getProperty("mailer"));
        smtp.setPassword(properties.getProperty("password"));
        smtp.setUserName(properties.getProperty("username"));
        smtp.setPort(Integer.parseInt(properties.getProperty("port")));
        smtp.setServerAddress(properties.getProperty("server"));
        smtp.setAuthenticated(properties.getProperty("authenticate").equalsIgnoreCase("TRUE") ? true : false);
        //return settings
        return smtp;
    }

    public static Properties loadSystemConfiguration() throws FileNotFoundException, IOException{
        return loadSystemConfiguration(ConfigReader.getFile_Prefix() + "-config.xml");
    }
    public static Properties loadSystemConfiguration(String fileName) throws FileNotFoundException, IOException{
        File file = new File(fileName);

        System.out.println("Reading Database Configuration from source." + file.getAbsolutePath());

        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(file));

        //return settings
        return properties;
    }

    public static String loadStandardEmailTemplate(){
        if(emailMessageTemplate == null || emailMessageTemplate.trim().equals("")){
            try{
                File file = new File(ConfigReader.getFile_Prefix() + "-email-template.vml");
                DataInputStream dIn = new DataInputStream(new FileInputStream(file));
                byte[] data = new byte[(int)file.length()];
                dIn.read(data);;
                dIn.close();
                emailMessageTemplate = new String(data);
            }catch(Exception e){
                return "";
            }
        }
        return emailMessageTemplate;
    }

    public static void addUserSession(String userId, String sessionId){
        try{
            if(USER_SESSION.containsKey(userId)){
                USER_SESSION.remove(userId);
            }
            USER_SESSION.put(userId, sessionId);
        }catch(Exception e){
            
        }
    }

    public static String getUserSession(String userId){
        return USER_SESSION.get(userId);
    }
    public static void deleteUserSession(String userId){
        USER_SESSION.remove(userId);
    }

    public static void main(String[] args){
        System.out.println(ConfigReader.validatePasswordPolicy("Aaron!", 8));
    }
}
