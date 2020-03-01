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

package org.tenece.web.mail;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.data.SMTPSettings;

/**
 *
 * @author jeffry.amachree
 */
public class EmailImpl {

    public static String formatEmailMessage (String originalMessage, Hashtable keyHolders){
        String messageBody = originalMessage;
        Set keys = keyHolders.keySet();
        Iterator iterateKeys = keys.iterator();
        while(iterateKeys.hasNext()){
            String key = String.valueOf(iterateKeys.next());
            String value = String.valueOf(keyHolders.get(key));
            //format message
            messageBody = messageBody.replaceAll(key, value == null ? "" : value);
        }
        return messageBody;
    }
    public static int sendEmail(String toList, String from, String ccList, String bccList,
            String subject, String message, Vector attachment){
        try{
            //get settings
            SMTPSettings smtpSetting = ConfigReader.loadSMTPSettings("volmacht-smtp.xml");
            SMTPMail smtp = new SMTPMail();
            //set appropriate parameters
            smtp.setMailer(smtpSetting.getMailer());
            smtp.setPassword(smtpSetting.getPassword());
            smtp.setUserName(smtpSetting.getUserName());
            smtp.setSmtpPort(smtpSetting.getPort());
            smtp.setSmtpAddress(smtpSetting.getServerAddress());
            smtp.setUseAuthentication(smtpSetting.isAuthenticated());

            return smtp.sendAttachedMail(toList, from, bccList, ccList, subject, message, attachment);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
